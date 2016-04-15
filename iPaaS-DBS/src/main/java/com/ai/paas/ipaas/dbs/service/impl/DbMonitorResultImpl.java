package com.ai.paas.ipaas.dbs.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher.Event.EventType;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcherEvent;
import com.ai.paas.ipaas.dbs.service.IDbMonitorResult;
import com.ai.paas.ipaas.dbs.service.IDbsResourceManage;
import com.ai.paas.ipaas.dbs.util.DbMonitorConstants;
import com.ai.paas.ipaas.dbs.util.SystemConfig;
import com.ai.paas.ipaas.util.Assert;
@Service
@DependsOn("serviceUtil")
public class DbMonitorResultImpl implements IDbMonitorResult {
	private static final Logger log = LogManager
			.getLogger(DbMonitorResultImpl.class.getName());
	@Autowired
	private ICCSComponentManageSv iCCSComponentManageSv;
	@Autowired
	private IDbsResourceManage iDbsResourceManage;

	//10.1.228.202
	private List<String> ips;
	//10.1.228.202=10.1.228.202_31306_devrdb11;10.1.228.202_31306_devrdb12
	private Map<String,List<String>> monitors = new HashMap<String,List<String>>();
	//10.1.228.202_31306_devrdb11=agent;clientxxx
	private Map<String,List<CCSSubListDTO>> statisticals = new HashMap<String,List<CCSSubListDTO>>();
	
	///dblist/10.1.228.202/dbmonitor/10.1.228.202_31306_devrdb11/agent true/false
	// watch dblist的孩子节点
	private IpListWatch ipListWatch;
	//ip--MonitorsWatch(dbmonitor的孩子节点)
	private Map<String,MonitorsWatch> monitorsWatchs = new HashMap<String,MonitorsWatch>();
	//ip-monitor--ServerWatch(10.1.228.202_31306_devrdb11的孩子节点)
	private Map<String,ServerWatch> serverWatchs = new HashMap<String,ServerWatch>();
	
	private Map<String,FinalWatch> finalWatchs = new HashMap<String,FinalWatch>();
	
	private CCSComponentOperationParam ipRootccs;
	private String userId;
	
	//记录/dblist/10.1.228.202/dbmonitor/10.1.228.202_31306_devrdb11添加watch的投票路径agent或clientxxx
	private Map<String,String> statisticalWatchPaths = new HashMap<String,String>();
	
	//记录watch节点的投票值
//	private Map<String,String> statisticalValues = new HashMap<String,String>();
	//记录前一次采集后的投票结果10.1.228.202_31306_devrdb11：true/false
	private Map<String,Boolean> preRes = new HashMap<String,Boolean>();


	/**
	 * 数据库宕机处理：获得宕机列表，触发处理流程
	 */
	@Override
	@PostConstruct
	public void crash() {
		log.info("----------start--IDbMonitorResult---------");
		userId = SystemConfig.getString("inner_user_id");
		Assert.notNull(userId, "没有加载到内部用户ID");
		//1、读取并设置ip列表
		setIpList(userId);
		//2、读取并设置monitor列表、遍历每一个ip_port_sid,统计投票结果；有宕机时，触发dbs处理
		setMonitorListAndstatisticalResults(userId);
	}
	/**
	 * 遍历监控每一个ip_port_sid,统计投票结果;
	 * 有宕机时，触发dbs处理机制
	 * 
	 * @param userId
	 */
	private void statisticalResults(String userId) {
		if(ips==null||ips.isEmpty()||monitors==null||monitors.isEmpty())
			return;
		for(String ip:ips){
			
			
			//遍历每一个ip_port_sid
			if(monitors.containsKey(ip)){
				for(final String monitor:monitors.get(ip)){
					final String path = DbMonitorConstants.DB_LIST_PATH_ROOT+"/"+ip+DbMonitorConstants.DRDS_DB_MONITOR_PATH+"/"+monitor;
					final CCSComponentOperationParam monitorccs = new CCSComponentOperationParam();
					monitorccs.setUserId(userId);
					monitorccs.setPathType(PathType.WRITABLE);
					monitorccs.setPath(path);
					ServerWatch serverWatch = null;
					if(serverWatchs.get(ip+"_"+monitor)!=null){
						serverWatch = serverWatchs.get(ip+"_"+monitor);
					}else{
						serverWatch = new ServerWatch(monitorccs,path,monitor);
						serverWatchs.put(ip+"_"+monitor, serverWatch);
					}
					try {
						List<CCSSubListDTO> statisticalNodes = iCCSComponentManageSv.listSubPathAndData(monitorccs, 
								serverWatch);
						statisticals.put(monitor, statisticalNodes);
						addFinaltWatch(path,statisticalNodes,monitorccs);
					} catch (PaasException e) {
						log.error("初始化投票结果读取出错："+e.getMessage(),e);
					}
				}
			}
			
		}
	}
	
	
	/**添加FinaltWatch
	 * @param path
	 * @param statisticalNodes
	 * @param monitorccs
	 * @return
	 */
	private boolean addFinaltWatch(String path, List<CCSSubListDTO> statisticalNodes,CCSComponentOperationParam monitorccs) {
		boolean addRes = false;
		///dblist/10.1.228.202/dbmonitor/10.1.228.202_31306_devrdb11
		String parentPath = path;
		String oldFinalPath = statisticalWatchPaths.get(parentPath);
		
		boolean isNeedAdd = true;
		if(oldFinalPath!=null){
			for(CCSSubListDTO statisticalNode:statisticalNodes){
				String lastPath = statisticalNode.getPath().substring(statisticalNode.getPath().lastIndexOf("/")+1);
				if(oldFinalPath.equals(lastPath)){
					isNeedAdd = false;
					break;
				}
			}
		}
		if(isNeedAdd&&statisticalNodes!=null&&statisticalNodes.size()>0){
			String nodePath = statisticalNodes.get(0).getPath();
			String lastPath = nodePath.substring(nodePath.lastIndexOf("/")+1);
			log.info("-addFinaltWatch---------------------"+path);
			try {
				String temp = path+"/"+lastPath;
				CCSComponentOperationParam tempOp = new CCSComponentOperationParam();
				tempOp.setPathType(monitorccs.getPathType());
				tempOp.setUserId(monitorccs.getUserId());
				tempOp.setPath(temp);
				FinalWatch finalWatch = null;
				if(finalWatchs.get(temp)!=null){
					finalWatch = finalWatchs.get(temp);
				}else{
					finalWatch = new FinalWatch(tempOp);
					finalWatchs.put(temp, finalWatch);
				}
				
				iCCSComponentManageSv.get(tempOp,finalWatch);
				statisticalWatchPaths.put(parentPath, lastPath);
				addRes = true;
			} catch (PaasException e) {
				log.error("添加FinalWatch出错："+e.getMessage(),e);
			}
			log.info(addRes+"-addFinaltWatch---------------------"+path);
		}
		return addRes;
	}
	
	
	/**
	 * 获得投票列表 ip_port_sid
	 * 
	 * @param userId
	 */
	private void setMonitorListAndstatisticalResults(String userId) {
		if(ips==null||ips.isEmpty())
			return;
		for(final String ip:ips){
			final CCSComponentOperationParam monitorRootccs = new CCSComponentOperationParam();
			monitorRootccs.setUserId(userId);
			monitorRootccs.setPath(DbMonitorConstants.DB_LIST_PATH_ROOT+"/"+ip+DbMonitorConstants.DRDS_DB_MONITOR_PATH);
			monitorRootccs.setPathType(PathType.WRITABLE);
			MonitorsWatch monitorsWatch = null;
			if(monitorsWatchs.get(ip)!=null){
				monitorsWatch = monitorsWatchs.get(ip);
			}else{
				monitorsWatch = new MonitorsWatch(monitorRootccs,ip);
				monitorsWatchs.put(ip, monitorsWatch);
			}
			try {
				List<String> ms = iCCSComponentManageSv.listSubPath(monitorRootccs, monitorsWatch);
				monitors.put(ip, ms);
			} catch (PaasException e) {
				log.error("初始化读取投票列表出错："+e.getMessage(),e);
			}
		}
		statisticalResults(userId);
	}
	
	/**
	 * 获得ip列表
	 * @param userId
	 */
	private void setIpList(final String userId) {
		ipRootccs = new CCSComponentOperationParam();
		ipRootccs.setUserId(userId);
		ipRootccs.setPath(DbMonitorConstants.DB_LIST_PATH_ROOT);
		ipRootccs.setPathType(PathType.WRITABLE);
		
		ipListWatch = new IpListWatch();
		this.userId = userId;
		try {
			ips = iCCSComponentManageSv.listSubPath(ipRootccs, ipListWatch);
		} catch (PaasException e) {
			log.error("初始化读取ip列表出错："+e.getMessage(),e);
		}
	}
	/**
	 * 与前一次采集后的投票结果有变化时，才通知
	 * @param path  10.1.228.202_31306_devrdb11
	 * @param res
	 * @return
	 */
	private boolean getIsNotify(String path, boolean res) {
		boolean notify = true;
		if(preRes.containsKey(path)){
			if(preRes.get(path).equals(res)){
				notify = false;
			}else{
				notify = true;
				preRes.put(path, res);
			}
		}else{
			//可能是在项目启动的时候。第一次 不通知
			preRes.put(path, res);
			notify = false;
		}
		return notify;
	}
	
	/**
	 * 通知dbs处理数据库宕机
	 * @param monitor
	 * @param monitorccs
	 * @param path
	 */
	private void notifyDbs(String monitor,
			CCSComponentOperationParam monitorccs,String path,boolean stats) {
		String[] info = monitor.split("_");
		String json = "{\"host\":\""+info[0]+"\",\"port\":"+info[1]+",\"instance\":\""+info[2]+"\"}";
		InterProcessLock lock = null;
		try {
			lock = iCCSComponentManageSv.createlock(monitorccs);
			// 阻塞模式
			log.info("Starting to acquire lock....." + path);
			if (lock.acquire(300, TimeUnit.SECONDS)) {
				//重启成功
				if(stats)
					iDbsResourceManage.recoverDistributeDb(json);
				else
					iDbsResourceManage.handleCrash(json);
			}
		} catch (Exception e) {
			log.error("数据库宕掉时，调用宕机处理出错："+e.getMessage(),e);
		} finally {
			try {
				if(lock != null)
					lock.release();
			} catch (Exception e1) {
				log.error("分布式锁解除时，出错："+e1.getMessage(),e1);
			}
		}
		
	}
	
	private String getPath(String eventPath){
		String temp = eventPath.substring(eventPath.indexOf(DbMonitorConstants.DB_LIST_PATH_ROOT+"/"));
		String parentPath = temp.substring(0,temp.lastIndexOf("/"));
		return parentPath;
	}
	private class IpListWatch extends ConfigWatcher{

		@Override
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null)
				return;
			// 事件类型
			EventType eventType = event.getType();
			if (EventType.NodeChildrenChanged == eventType){
				log.info("------monitor--ip------NodeChildrenChanged--------");
				try{
					ips = iCCSComponentManageSv.listSubPath(ipRootccs,this);
					//TODO
					monitors = new HashMap<String,List<String>>();
					statisticals = new HashMap<String,List<CCSSubListDTO>>();
					setMonitorListAndstatisticalResults(userId);
				} catch (PaasException e) {
					log.error("ip列表变化时，读取出错："+e.getMessage(),e);
				}
			}
		}
		
	}
	private class MonitorsWatch extends ConfigWatcher{
		private CCSComponentOperationParam  monitorRootccs;
		private String ip;
		
		public MonitorsWatch(CCSComponentOperationParam  monitorRootccs,String ip){
			this.monitorRootccs = monitorRootccs;
			this.ip = ip;
		}
		@Override
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null)
				return;
			// 事件类型
			EventType eventType = event.getType();
			if (EventType.NodeChildrenChanged == eventType){
				log.info("------monitor--ip_port_sid------NodeChildrenChanged--------");
				try{
					monitors.put(ip, iCCSComponentManageSv.listSubPath(monitorRootccs,this));
					//TODO 
					statisticals = new HashMap<String,List<CCSSubListDTO>>();
					statisticalResults(userId);
				} catch (PaasException e) {
					log.error("投票列表变化时，读取出错："+e.getMessage(),e);
				}
			}
		}
		
	}
	private class ServerWatch extends ConfigWatcher{
		private CCSComponentOperationParam monitorccs;
		private String path;
		private String monitor;
		
		public ServerWatch(CCSComponentOperationParam monitorccs,String path,String monitor){
			this.monitorccs = monitorccs;
			this.path = path;
			this.monitor = monitor;
		}
		
		@Override
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null)
				return;
			// 事件类型
			EventType eventType = event.getType();
			if (EventType.NodeChildrenChanged == eventType){
				log.info("------monitor--投票节点------NodeChildrenChanged--------");
				try{
					List<CCSSubListDTO> statisticalNodes = iCCSComponentManageSv.listSubPathAndData(monitorccs,this);
					boolean addRes = addFinaltWatch(path,statisticalNodes,monitorccs);
				} catch (PaasException e) {
					log.error("投票节点变化时，读取出错："+e.getMessage(),e);
				}
			}else{
				log.info("---eventType---ServerWatch--"+eventType);
			}
			
		}

		
		
	}
	
	private class FinalWatch extends ConfigWatcher{
		private CCSComponentOperationParam statisticalccs;
		
		public FinalWatch(CCSComponentOperationParam statisticalccs){
			this.statisticalccs = statisticalccs;
		}
		
		@Override
		public void processEvent(ConfigWatcherEvent event) {
			if (event == null)
				return;
			// 事件类型
			EventType eventType = event.getType();
			if (EventType.NodeDataChanged == eventType){
				log.info("------monitor--投票结果------NodeDataChanged--------");
				try{
					//循环watch
					String value = iCCSComponentManageSv.get(statisticalccs,this);
					
					String parentPath = getPath(event.getPath());
					String monitor = parentPath.substring(parentPath.lastIndexOf("/")+1);
				
					boolean isDown = true;
					//获得所有投票,全部为false时，触发dbs处理
					CCSComponentOperationParam tempOp = new CCSComponentOperationParam();
					tempOp.setPath(parentPath);
					tempOp.setUserId(userId);
					tempOp.setPathType(PathType.WRITABLE);
					List<CCSSubListDTO> datas = iCCSComponentManageSv.listSubPathAndData(tempOp);
					if(datas!=null){
						for(CCSSubListDTO dto : datas){
							if(DbMonitorConstants.STATISTICAL_TRUE.equals(dto.getData())){
								isDown = false;
								break;
							}
						}
					}
					log.info(monitor+"--isDown------"+isDown);
					if(getIsNotify(monitor,isDown)){
						log.info(monitor+"-FinalWatch--valueChanged----------");
						//与前一次采集后的投票结果有变化时，才通知
						if(isDown){
							log.error(monitor+"--DB is stoped......");
							notifyDbs(monitor,statisticalccs,parentPath,false);
						}else{
							log.warn(monitor+"--DB is restart......");
							notifyDbs(monitor,statisticalccs,parentPath,true);
						}

					}
						
				} catch (PaasException e) {
					log.error("投票结果变化时，读取出错："+e.getMessage(),e);
				}
			}else{
				log.info("---eventType---FinalWatch--"+eventType);
			}
			
		}

//		private boolean valueIsChange(String path, String value) {
//			boolean change;
//			if(statisticalValues.containsKey(path)){
//				if(statisticalValues.get(path).equals(value)){
//					change = false;
//				}else{
//					//变化
//					change =true;
//					statisticalValues.put(path, value.trim());
//				}
//			}else{
//				statisticalValues.put(path, value.trim());
//				change = false;
//			}
//			return change;
//		}
		
	}
	
	

}
