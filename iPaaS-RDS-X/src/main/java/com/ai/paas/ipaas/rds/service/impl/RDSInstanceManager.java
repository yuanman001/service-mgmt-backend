package com.ai.paas.ipaas.rds.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.agent.util.AgentUtil;
import com.ai.paas.ipaas.agent.util.AidUtil;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasImageResourceMapper;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResource;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResourceCriteria;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsIncBaseMapper;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsResourcePoolMapper;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBaseCriteria;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePoolCriteria;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.ai.paas.ipaas.rds.service.constant.InstanceType;
import com.ai.paas.ipaas.rds.service.constant.RDSCommonConstant;
import com.ai.paas.ipaas.rds.service.constant.ResponseResultMark;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateSRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetIncInfo;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetInstanceInfoRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.InstanceBaseSimple;
import com.ai.paas.ipaas.rds.service.transfer.vo.MedthodDescribe;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.RDSResourcePlan;
import com.ai.paas.ipaas.rds.service.transfer.vo.ResIncPlan;
import com.ai.paas.ipaas.rds.service.transfer.vo.RestartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.RestartResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDSResult;
import com.ai.paas.ipaas.rds.service.util.EntityToWhere;
import com.ai.paas.ipaas.rds.service.util.GsonSingleton;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 传输对象命名规则就是对应方法名
 * 拓扑结构可以随意更改 
 * 空间是通过修改mysql配置和扩充磁盘阵列实现 
 * @author 作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:48:55
 * @version
 * @since
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSInstanceManager implements IRDSInstanceManager {
	
	private static transient final Logger LOG = LoggerFactory.getLogger(RDSInstanceManager.class);

	@Autowired
	GsonSingleton g;

	@Autowired
	ICCSComponentManageSv iCCSComponentManageSv;

	/**
	 * 注销实例
	 * 可以是单个，也可以是多个
	 * @throws PaasException 
	 */
	@Override
	public String cancel(String cancel) {
		// 解析JSON对象
		CancelRDS cancelObject = g.getGson().fromJson(cancel, CancelRDS.class);
		CancelRDSResult cancelResult = new CancelRDSResult();
		Stack<RdsIncBase> instanceStack ;
		// 检查用户权限
		if(!CheckCancelLegal(cancelObject)){
			cancelResult.setStatus(ResponseResultMark.ERROR_ILLEGAL_AUTHORITY);
			return g.getGson().toJson(cancelResult);
		}

		// 检查数据完整性
		if(!CheckCancelDataLegal(cancelObject)){
			cancelResult.setStatus(ResponseResultMark.ERROR_LESS_IMP_PARAM);
			return g.getGson().toJson(cancelResult);
		}
		
		// 查询实例情况
		instanceStack = getInstanceStack(cancelObject.instanceid);
		
		if(instanceStack.isEmpty()){
			cancelResult.setStatus(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(cancelResult);
		}
		
		while(!instanceStack.isEmpty()){
			RdsIncBase instance = instanceStack.pop();
			instance.setIncStatus(RDSCommonConstant.INS_FREEZE);
			RdsIncBaseMapper statusMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
			statusMapper.updateByPrimaryKeySelective(instance);
			try {
				dealCancelInstanceDevided(instance);
				deleteZK(instance);
			} catch (IOException | PaasException e) {
				e.printStackTrace();
				cancelResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
				return g.getGson().toJson(cancelResult);
			}
		};

		cancelResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(cancelResult);
	}


	/**
	 * 通过一个实例id查询到实例和实例的主备、主从实例
	 * @param instanceid
	 * @return
	 */
	private Stack<RdsIncBase> getInstanceStack(int instanceid) {
		Stack<RdsIncBase> instanceStack = new Stack<RdsIncBase>();
		RdsIncBaseMapper ibm = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase instanceInfo = ibm.selectByPrimaryKey(instanceid);
		if(null != instanceInfo)
			instanceStack.push(instanceInfo);
		if(InstanceType.MASTER == instanceInfo.getIncType()){
//			System.out.println("$$$$$$$$$$$$$$$$$$$$"+instanceInfo.getBakId());
			if(null != instanceInfo.getBakId() && !instanceInfo.getBakId().equals("")){
				RdsIncBase rib = ibm.selectByPrimaryKey(getIdArrayFromString(instanceInfo.getBakId()).get(0));
				instanceStack.push(rib);
			}
			if(null != instanceInfo.getSlaverId() && !instanceInfo.getSlaverId().equals("")){
				for(Integer is : getIdArrayFromString(instanceInfo.getSlaverId())){
					RdsIncBase ribs = ibm.selectByPrimaryKey(is);
					instanceStack.push(ribs);
				}
			}
		}
		
		return instanceStack;
	}
	
	

	private List<Integer> getIdArrayFromString(String bakId) {
		// 如果使用split作为分隔符，则
		String[] idArray = bakId.split("\\|");
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for(int i = 0; i < idArray.length; i++){
			if(idArray[i] != null && !idArray[i].isEmpty() && !idArray[i].equals(""))
				idList.add(Integer.valueOf(idArray[i]));
		}
		return idList;
	}

	/**
	 * 数据库已经添加了触发器trigger
	 * 从表将与主表一同删除
	 * @param instanceBase
	 * @throws PaasException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private void dealCancelInstanceDevided(RdsIncBase instanceBase) throws ClientProtocolException, IOException, PaasException {
		// 停止运行实例，并移除相关镜像、配置、数据
		stopInstance(instanceBase);
		removeInstance(instanceBase);
		// 删除RdsIncBase表中的信息
		RdsIncBaseMapper instanceBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		instanceBaseMapper.deleteByPrimaryKey(instanceBase.getId());
		// 资源恢复更新
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool rdsres = resPoolMapper.selectByPrimaryKey(instanceBase.getResId());
		rdsres.setUsedmemory(rdsres.getUsedmemory().intValue() - instanceBase.getDbStoreage().intValue());
		resPoolMapper.updateByPrimaryKey(rdsres);
	}


	private boolean CheckCancelDataLegal(CancelRDS cancelObject) {
//		if((0 < cancelObject.instanceid) && null == cancelObject.token && null == cancelObject.user_id)
//			return false;
		return true;
	}

	private boolean CheckCancelLegal(CancelRDS cancelObject) {
		return true;
	}

	// @ExceptionHandler(Exception.class)
	// private void exception(Exception e){
	// e.printStackTrace();
	// }

	/**
	 * 创建实例 
	 * 这里主要是创建一个主实例，并创建相应的主备、主从实例
	 * 单独创建备、从实例是在其他方法当中
	 * create中必须存在的字段
	 * －－－－
	 * token/
	 * instanceName/
	 * user_id/
	 * serial_number/
	 * instancenetworktype/
	 * instancespaceinfo/
	 * instancebaseconfig/
	 * instanceimagebelonger/
	 * instancerootaccount/
	 * 可选字段（延时生成字段）
	 * instanceslaver/
	 * instancebatmaster/
	 * 生成字段-RDSResourcePlan
	 * instanceipport/
	 * instancestatus/
	 * instanceresourcebelonger/
	 */
	@Override
	public String create(String create) {
		// 解析JSON对象
		CreateRDS createObject = g.getGson().fromJson(create, CreateRDS.class);
		CreateRDSResult createResult = new CreateRDSResult(ResponseResultMark.WARN_INIT_STATUS);
		List<RdsResourcePool> exceptionResPoolList = new LinkedList<RdsResourcePool>();
		// 检查用户操作权限是否合法
//		if (false == CheckLegal(createObject.instanceBase.getUserId(), createObject.instanceBase.getServiceId(),
//				createObject.token)) {
//			createResult.setStatus(ResponseResultMark.ERROR_ILLEGAL_AUTHORITY);
//			return g.getGson().toJson(createResult);
//		}
		// 检查数据是否合法
		if (false == CheckData(createObject)) {
			createResult.setStatus(ResponseResultMark.ERROR_LESS_IMP_PARAM);
			return g.getGson().toJson(createResult);
		}
		
		if(createObject.instanceBase.getImgId() == null){
			createObject.instanceBase.setImgId(5);
		}
		if(createObject.instanceBase.getMysqlHome() == null){
			createObject.instanceBase.setMysqlHome("/aifs01");
		}
		if(createObject.instanceBase.getMysqlDataHome() == null){
			createObject.instanceBase.setMysqlDataHome("/aifs01/mysqldata");
		}
		if(createObject.instanceBase.getMysqlVolumnPath() == null){
			createObject.instanceBase.setMysqlVolumnPath("");
		}
		if(createObject.instanceBase.getRootName() == null){
			createObject.instanceBase.setRootName("root");
		}
		if(createObject.instanceBase.getRootPassword() == null){
			createObject.instanceBase.setRootPassword("123456");
		}
		if(createObject.instanceBase.getBakId() == null){
			createObject.instanceBase.setBakId("");
		}
		if(createObject.instanceBase.getSlaverId() == null){
			createObject.instanceBase.setSlaverId("");;
		}
		if(createObject.instanceBase.getContainerName() == null){
			createObject.instanceBase.setContainerName("");
		}
		if(createObject.instanceBase.getIncLocation() == null){
			createObject.instanceBase.setIncLocation("");
		}
		
		
		
		// 查询资源情况，根据请求情况与资源情况获取分配计划
		RdsResourcePoolMapper rdsResPoolMapper =  ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePoolCriteria rdsResPoolCri = new RdsResourcePoolCriteria();
		rdsResPoolCri.createCriteria().andCurrentportLessThan(65000);
		List<RdsResourcePool> allResource = rdsResPoolMapper.selectByExample(rdsResPoolCri);
		RDSResourcePlan resourcePlan = getResourcePlan(createObject, allResource);
		if(null == resourcePlan.instanceresourcebelonger){
			createResult.setStatus(ResponseResultMark.ERROR_LESS_MEMORY_SPACE);
			return g.getGson().toJson(createResult);
		}
		
		// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
		RdsIncBase savedRdsIncBase = savePlan(resourcePlan, createObject.instanceBase, InstanceType.MASTER);
		createResult.incSimList.add(new InstanceBaseSimple(savedRdsIncBase));
		
		// 对实例进行配置,并启动 master/slaver/batmaster 通过AgentClient
		
		// 如果实例配置成功则启动实例
//		instanceRun = startInstance(savedRdsIncBase);
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		savedRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTING);
		incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
		// 启动mysql服务
//		save2ZK(savedRdsIncBase);
		boolean isRightConfig = false;
		try {
			isRightConfig = InstanceConfig(savedRdsIncBase);
		} catch (IOException | PaasException e) {
			// 处理。。。
			e.printStackTrace();
			createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
			return g.getGson().toJson(createResult);
		}
		// 修改数据库中服务器状态
		savedRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTED);
		incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
		// 将拓扑结构保存至注册中心（zk）,由于数据不全则将延迟保存到ZK
		
		// 添加到被排除列表中
		exceptionResPoolList.add(resourcePlan.instanceresourcebelonger);
		
		// 根据可选字段创建mysql从服务器、创建mysql主备服务器，存储在RdsIncBase表中
		if ((createObject.instanceBase.getIncType() == InstanceType.MASTER) && (true == isRightConfig)) {
//			RdsIncBase batMasterInstance = null;
//			RdsInstancebatmaster savedBatmasterInstance = null;
//			List<RdsIncBase> slaverInstanceList = new LinkedList<RdsIncBase>();
//			List<RdsInstanceSlaver> slaverForMasterInstance = new LinkedList<RdsInstanceSlaver>();
			if (1 == createObject.createBatmasterNum) {
				// 包装一个创建主备实例的类，并保存到数据库
				// 查询资源情况，根据请求情况与资源情况获取分配计划
				RDSResourcePlan resourceBatMasterPlan = getExpectResourcePlan(createObject.instanceBase, allResource, exceptionResPoolList);
				if(null == resourceBatMasterPlan.instanceresourcebelonger){
					createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
					return g.getGson().toJson(createResult);
				}
				RdsIncBase batInCopy = savedRdsIncBase.clone();
				batInCopy.setMasterid(savedRdsIncBase.getId());
				// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
				RdsIncBase batMasterInstance = savePlan(resourceBatMasterPlan, batInCopy, InstanceType.BATMASTER);
				createResult.incSimList.add(new InstanceBaseSimple(batMasterInstance));
				exceptionResPoolList.add(resourceBatMasterPlan.instanceresourcebelonger);
				// 对需要外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
				savedRdsIncBase.setBakId(batMasterInstance.getId() + "");
				// 保存信息到实例
				incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
				// 对实例进行远程配置
				boolean isRightBatMasterConfig = false;
				try {
					// 如果实例配置成功则启动实例
//					boolean batMasterInstanceRun = startInstance(batMasterInstance);
					batMasterInstance.setIncStatus(RDSCommonConstant.INS_STARTING);
					incBaseMapper.updateByPrimaryKey(batMasterInstance);
					isRightBatMasterConfig = InstanceConfig(batMasterInstance);
					// 修改数据库中服务器状态
					batMasterInstance.setIncStatus(RDSCommonConstant.INS_STARTED);
					incBaseMapper.updateByPrimaryKey(batMasterInstance);
					
					// 将拓扑结构保存至注册中心（zk）
					save2ZK(batMasterInstance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
					return g.getGson().toJson(createResult);
				}
			}

			if (0 < createObject.createSlaverNum) {
				for (int i = 0; i < createObject.createSlaverNum; i++) {
					// 包装多个创建从服务器实例的类，并保存到数据库
					RDSResourcePlan resourceSlaverPlan = getExpectResourcePlan(createObject.instanceBase, allResource,exceptionResPoolList);
					if(null == resourceSlaverPlan.instanceresourcebelonger){
						createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
						return g.getGson().toJson(createResult);
					}
					RdsIncBase slaInCopy = savedRdsIncBase.clone();
					slaInCopy.setMasterid(savedRdsIncBase.getId());
					// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
					RdsIncBase ib = savePlan(resourceSlaverPlan, slaInCopy, InstanceType.SLAVER);
					createResult.incSimList.add(new InstanceBaseSimple(ib));
					exceptionResPoolList.add(resourceSlaverPlan.instanceresourcebelonger);
					// 对需要外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
					if(savedRdsIncBase.getSlaverId().isEmpty() || savedRdsIncBase.getSlaverId().equals("") || savedRdsIncBase.getSlaverId() == null){
						savedRdsIncBase.setSlaverId(ib.getId() + "");
					}else{
						savedRdsIncBase.setSlaverId(savedRdsIncBase.getSlaverId() + "|" + ib.getId());
					}
					incBaseMapper.updateByPrimaryKey(savedRdsIncBase);
					// 对实例进行远程配置
					boolean isRightSlaverConfig = false;
					try {
						// 如果实例配置成功则启动实例
//						boolean slaverInstanceRun = startInstance(ib);
						ib.setIncStatus(RDSCommonConstant.INS_STARTING);
						incBaseMapper.updateByPrimaryKey(ib);
						isRightSlaverConfig = InstanceConfig(ib);
						// 修改数据库中服务器状态
						ib.setIncStatus(RDSCommonConstant.INS_STARTED);
						incBaseMapper.updateByPrimaryKey(ib);
						
						// 将拓扑结构保存至注册中心（zk）
						save2ZK(ib);
					} catch (IOException | PaasException e) {
						e.printStackTrace();
						createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
						return g.getGson().toJson(createResult);
					}
				}
			}
		}else{
			createResult.isInstanceConfig = isRightConfig;
			createResult.isInstanceRun = false;
			createResult.setStatus(ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER);
			return g.getGson().toJson(createResult);
		}

		// 将拓扑结构保存至注册中心（zk）
		save2ZK(savedRdsIncBase);
		
		createResult.isInstanceConfig = isRightConfig;
		createResult.isInstanceRun = true;
		createResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(createResult);
	}
	
	
	@Override
	public String createslobm(String create) {
		CreateSRDS createObject = g.getGson().fromJson(create, CreateSRDS.class);
		CreateSRDSResult createResult = new CreateSRDSResult(ResponseResultMark.WARN_INIT_STATUS);
		
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase masterInstance = incBaseMapper.selectByPrimaryKey(createObject.masterinstanceid);
		
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		
		// 查询实例情况
		Stack<RdsIncBase> instanceStack = getInstanceStack(createObject.masterinstanceid);
		List<RdsResourcePool> exceptResourceResourceList = new ArrayList<RdsResourcePool>();
		
		for(int i = 0; i < instanceStack.size(); i++){
//			resPoolMapper.selectByPrimaryKey(instanceStack.get(i).getResId());
			exceptResourceResourceList.add(resPoolMapper.selectByPrimaryKey(instanceStack.get(i).getResId()));
		}
		RdsResourcePoolCriteria cri = new RdsResourcePoolCriteria();
		cri.createCriteria().andCurrentportBetween(0, 100000);
		List<RdsResourcePool> allRes = resPoolMapper.selectByExample(cri);
		
		// 查询资源情况，根据请求情况与资源情况获取分配计划
		RDSResourcePlan resourceBatMasterPlan = getExpectResourcePlan(masterInstance, allRes, exceptResourceResourceList);
		if(null == resourceBatMasterPlan.instanceresourcebelonger){
			createResult.setStatus(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
			return g.getGson().toJson(createResult);
		}
		
		RdsIncBase masterCopy = masterInstance.clone();
		masterCopy.setMasterid(createObject.masterinstanceid);
		// 对资源分配后的情况保存到数据库，修改资源池的情况，插入实例信息
		RdsIncBase saveRdsIncBase = savePlan(resourceBatMasterPlan, masterCopy, createObject.thisInstanceType);

		// 对需要外键延期保存的数据，单独进行保存（instanceslaver、instancebatmaster）
		switch(saveRdsIncBase.getIncType()){
		case InstanceType.MASTER:
			createResult.setStatus(ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD);
			return g.getGson().toJson(createResult);
		case InstanceType.BATMASTER:
			masterInstance.setBakId(saveRdsIncBase.getId() + "");
			incBaseMapper.updateByPrimaryKey(masterInstance);
			break;
		case InstanceType.SLAVER:
			if(masterInstance.getSlaverId().isEmpty()){
				masterInstance.setSlaverId(saveRdsIncBase.getId() + "");
			}else{
				masterInstance.setSlaverId(masterInstance.getServiceId() + "|" + saveRdsIncBase.getId());
			}
			incBaseMapper.updateByPrimaryKey(masterInstance);
			break;
		default:
			createResult.setStatus(ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE);
			return g.getGson().toJson(createResult);
		}
			
		// 保存信息到实例
//		savedRdsIncBase.instancebatmaster = savedBatmasterInstance;
		// 对实例进行远程配置
		boolean isRightBatMasterConfig = false;
		try {
			
			// 如果实例配置成功则启动实例
			saveRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTING);
			incBaseMapper.updateByPrimaryKey(saveRdsIncBase);
			// 启动mysql服务
			isRightBatMasterConfig = InstanceConfig(saveRdsIncBase);
			// 修改数据库中服务器状态
			saveRdsIncBase.setIncStatus(RDSCommonConstant.INS_STARTED);
			incBaseMapper.updateByPrimaryKey(saveRdsIncBase);
						
			// 将拓扑结构保存至注册中心（zk）
			save2ZK(saveRdsIncBase);
			
		} catch (IOException | PaasException e) {
			e.printStackTrace();
			createResult.setStatus(ResponseResultMark.ERROR_BAD_CONFIG);
			return g.getGson().toJson(createResult);
		}
		if (true == isRightBatMasterConfig) {
			
		}
		createResult.setStatus(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(createResult);
	}

	

	/** ansible hosts */
	public static final String CREATE_ANSIBLE_HOSTS = "rds/init_ansible_ssh_hosts.sh {0} {1} {2}";
	
	public static final String DOCKER_MASTER_PARAM = "rds/ansible_master_run_image.sh  {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14}";
	public static final String DOCKER_SLAVER_PARAM = "rds/ansible_slaver_run_image.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16}";
	public static final String DOCKER_BATMASTER_PARAM = "rds/ansible_run_image.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12} {13} {14} {15} {16}";
	public static final String DOCKER_COMMAND_PARAM = "rds/ansible_command_image.sh {1} {2} {3} "
			+ "{4} {5} {6} {7}";
	
	public static String fillStringByArgs(String str, String[] arr) {
		Matcher m = Pattern.compile("\\{(\\d+)\\}").matcher(str);
		while (m.find()) {
			str = str.replace(m.group(), arr[Integer.parseInt(m.group(1))]);
		}
		return str;
	}
	
	
	/**
	 * 
	 * 坑们
	 * 1、不能存在第一个元素传入 {0}
	 * 2、对应文件名字一定要修改好
	 * 3、playbook传参超过十个则用大括号阔入
	 * 4、修改final定义的字符串与文件名保持匹配
	 * 5、修改.sh文件中的.yml文件名
	 *
	 * 配置MySQL
	 * Master\Slaver\BatMaster
	 * 
	 * @param savedRdsIncBase
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws PaasException
	 */
	private boolean InstanceConfig(RdsIncBase savedRdsIncBase)
			throws ClientProtocolException, IOException, PaasException {
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase masterInc = null;
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(savedRdsIncBase.getResId());
		IpaasImageResourceMapper imgResMapper = ServiceUtil.getMapper(IpaasImageResourceMapper.class);
//		IpaasImageResource imgRes = imgResMapper.selectByPrimaryKey(savedRdsIncBase.getImgId());
		IpaasImageResourceCriteria imgCri = new IpaasImageResourceCriteria();
		imgCri.createCriteria().andImageCodeEqualTo("mysql").andServiceCodeEqualTo("RDS").andStatusEqualTo(1);
		List<IpaasImageResource> imgResConstant = imgResMapper.selectByExample(imgCri);
		IpaasImageResource imgRes = imgResConstant.get(0);
		
		if(savedRdsIncBase.getIncType().intValue() > 1 ){
			if(savedRdsIncBase.getMasterid() > 0){
				masterInc = incBaseMapper.selectByPrimaryKey(savedRdsIncBase.getMasterid());
			}else{
				return false;
			}
		}
		switch(savedRdsIncBase.getIncType()){
		/**
		 * rdspath
		 * filename
		 * sshuser
		 * sshpassword		
		 * image?
		 * ip
		 * port
		 * datapath
		 * homepath
		 * (mysqlVolumnPath)
		 * container-name {user_id}-{service_id}-{port}
		 * server-id
		 * dbStorage
		 * instanceType
		 * path?与sshhost命令中的第一个参数对应
		 * !whitelist
		 * !slaver_name
		 * !slaver_password
		 */
		case InstanceType.MASTER:
			String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
			String rdsPath = basePath + "rds";
			LOG.debug("---------rdsPath {}----------", rdsPath);
			// 1.先将需要执行镜像命令的机器配置文件上传上去。
			InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
			String[] cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

			// 2.执行这个初始化命令
			String mkSshHosts = fillStringByArgs(CREATE_ANSIBLE_HOSTS,
					new String[] { rdsPath, 
							savedRdsIncBase.getIncIp().replace(".", ""),
							savedRdsIncBase.getIncIp() });
			LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
			AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_master.yml");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/rdsimage_master.yml", cnt, AidUtil.getAid());

			// 还得上传文件
			in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_master_run_image.sh");
			cnt = AgentUtil.readFileLines(in);
			in.close();
			AgentUtil.uploadFile("rds/ansible_master_run_image.sh", cnt, AidUtil.getAid());
			AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_master_run_image.sh", AidUtil.getAid());
			// 开始执行
			String runImage = fillStringByArgs(DOCKER_MASTER_PARAM,
					new String[] { "",
							rdsPath, 
							savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
							incRes.getSshuser(),
							incRes.getSshpassword(),
							savedRdsIncBase.getIncIp(),
							imgRes.getImageRepository() + "/" + imgRes.getImageName(),
							savedRdsIncBase.getIncPort() + "",
							savedRdsIncBase.getMysqlDataHome(),
							savedRdsIncBase.getMysqlHome(),
							incRes.getVolumnPath(),
							savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
							savedRdsIncBase.getDbServerId(),
							savedRdsIncBase.getDbStoreage() + "",
							getIncTypeById(savedRdsIncBase.getIncType())});

			LOG.debug("---------runImage {}----------", runImage);
			AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());

			return true;
			/**
			 * filename
			 * sshuser
			 * sshpassword		
			 * image?
			 * ip
			 * port
			 * datapath
			 * homepath
			 * (mysqlVolumnPath)
			 * container-name {user_id}-{service_id}-{port}
			 * server-id
			 * dbStorage
			 * instanceType
			 * path?与sshhost命令中的第一个参数对应
			 * masterip
			 * masterport
			 * 
			 * !whitelist
			 * !slaver_name
			 * !slaver_password
			 */
			case InstanceType.SLAVER:
				String basePath_s = AgentUtil.getAgentFilePath(AidUtil.getAid());
				String rdsPath_s = basePath_s + "rds";
				// 1.先将需要执行镜像命令的机器配置文件上传上去。
				InputStream in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
				String[] cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt_s, AidUtil.getAid());
				AgentUtil.executeCommand("chmod +x " + basePath_s + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

				// 2.执行这个初始化命令
				String mkSshHosts_s = fillStringByArgs(CREATE_ANSIBLE_HOSTS,
						new String[] { rdsPath_s, 
								savedRdsIncBase.getIncIp().replace(".", ""),
								savedRdsIncBase.getIncIp() });
				LOG.debug("---------mkSshHosts {}----------", mkSshHosts_s);
				AgentUtil.executeCommand(basePath_s + mkSshHosts_s, AidUtil.getAid());

				in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_slaver.yml");
				cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/rdsimage_slaver.yml", cnt_s, AidUtil.getAid());

				// 还得上传文件
				in_s = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_slaver_run_image.sh");
				cnt_s = AgentUtil.readFileLines(in_s);
				in_s.close();
				AgentUtil.uploadFile("rds/ansible_slaver_run_image.sh", cnt_s, AidUtil.getAid());
				AgentUtil.executeCommand("chmod +x " + basePath_s + "rds/ansible_slaver_run_image.sh", AidUtil.getAid());
				// 开始执行
				String runImage_s = fillStringByArgs(DOCKER_SLAVER_PARAM,
						new String[] { "",
								rdsPath_s, 
								savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
								incRes.getSshuser(),
								incRes.getSshpassword(),
								savedRdsIncBase.getIncIp(),
								imgRes.getImageRepository() + "/" + imgRes.getImageName(),
								savedRdsIncBase.getIncPort() + "",
								savedRdsIncBase.getMysqlDataHome(),
								savedRdsIncBase.getMysqlHome(),
								incRes.getVolumnPath(),
								savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
								savedRdsIncBase.getDbServerId(),
								savedRdsIncBase.getDbStoreage() + "",
								getIncTypeById(savedRdsIncBase.getIncType()),
								masterInc.getIncIp(),
								masterInc.getIncPort() + ""});

				LOG.debug("---------runImage {}----------", runImage_s);
				AgentUtil.executeCommand(basePath_s + runImage_s, AidUtil.getAid());

				return true;
				/**
				 * filename
				 * sshuser
				 * sshpassword		
				 * image?
				 * ip
				 * port
				 * datapath
				 * homepath
				 * (mysqlVolumnPath)
				 * container-name {user_id}-{service_id}-{port}
				 * server-id
				 * dbStorage
				 * instanceType
				 * path?与sshhost命令中的第一个参数对应
				 * !whitelist
				 * !slaver_name
				 * !slaver_password
				 */
//				case InstanceType.BATMASTER:
//					String basePath_b = AgentUtil.getAgentFilePath(AidUtil.getAid());
//					String rdsPath_b = basePath_b + "rds";
//					// 1.先将需要执行镜像命令的机器配置文件上传上去。
//					InputStream in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
//					String[] cnt_b = AgentUtil.readFileLines(in_b);
//					in_b.close();
//					AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt_b, AidUtil.getAid());
//					AgentUtil.executeCommand("chmod +x " + basePath_b + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());
//
//					// 2.执行这个初始化命令
//					String mkSshHosts_b = fillStringByArgs(CREATE_ANSIBLE_HOSTS,
//							new String[] { rdsPath_b, 
//									savedRdsIncBase.getIncIp().replace(".", ""),
//									savedRdsIncBase.getIncIp() });
//					LOG.debug("---------mkSshHosts {}----------", mkSshHosts_b);
//					AgentUtil.executeCommand(basePath_b + mkSshHosts_b, AidUtil.getAid());
//
//					in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage.yml");
//					cnt_b = AgentUtil.readFileLines(in_b);
//					in_b.close();
//					AgentUtil.uploadFile("rds/rdsimage.yml", cnt_b, AidUtil.getAid());
//
//					// 还得上传文件
//					in_b = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_run_image.sh");
//					cnt_b = AgentUtil.readFileLines(in_b);
//					in_b.close();
//					AgentUtil.uploadFile("rds/ansible_run_image.sh", cnt_b, AidUtil.getAid());
//					AgentUtil.executeCommand("chmod +x " + basePath_b + "rds/ansible_run_image.sh", AidUtil.getAid());
//					// 开始执行
//					String runImage_b = fillStringByArgs(DOCKER_BATMASTER_PARAM,
//							new String[] { "",
//									rdsPath_s, 
//									savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
//									incRes.getSshuser(),
//									incRes.getSshpassword(),
//									savedRdsIncBase.getIncIp(),
//									imgRes.getImageRepository() + "/" + imgRes.getImageName(),
//									savedRdsIncBase.getIncPort() + "",
//									savedRdsIncBase.getMysqlDataHome(),
//									savedRdsIncBase.getMysqlHome(),
//									incRes.getVolumnPath(),
//									savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort(),
//									savedRdsIncBase.getDbServerId(),
//									savedRdsIncBase.getDbStoreage() + "",
//									getIncTypeById(savedRdsIncBase.getIncType()),
//									masterInc.getIncIp(),
//									masterInc.getIncPort() + ""});
//
//					LOG.debug("---------runImage {}----------", runImage_b);
//					AgentUtil.executeCommand(basePath_b + runImage_b, AidUtil.getAid());
//
//					return true;
		default:
			return false;
		}
		
		
//		return true;
		
	}

	private String getIncTypeById(Integer incType) {
		switch(incType){
		case InstanceType.MASTER:
			return "master";
		case InstanceType.SLAVER:
			return "slave";
		case InstanceType.BATMASTER:
			return "batmaster";
		}
		return null;
	}


	/**
	 * 停止单个实例
	 * 通过ansible执行脚本停止docker中mysql实例的运行
	 * @param instanceBase
	 * @throws PaasException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private void stopInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException  {
			commandInstance(savedRdsIncBase,"stop");
	}
	
	private void startInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"start");
	}
	
	private void removeInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"rm");
	}
	
	private void restartInstance(RdsIncBase savedRdsIncBase) throws ClientProtocolException, IOException, PaasException {
			commandInstance(savedRdsIncBase,"restart");
	}
	private void configModify(RdsIncBase ib, int argmentedExternalStorage) {
		//TODO doing some thing
	}
	
	private void commandInstance(RdsIncBase savedRdsIncBase, String command) throws ClientProtocolException, IOException, PaasException {
		RdsResourcePoolMapper resPoolMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		RdsResourcePool incRes = resPoolMapper.selectByPrimaryKey(savedRdsIncBase.getResId());
		
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		String rdsPath = basePath + "rds";
		LOG.debug("---------rdsPath {}----------", rdsPath);
		// 1.先将需要执行镜像命令的机器配置文件上传上去。
		InputStream in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/init_ansible_ssh_hosts.sh");
		String[] cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/init_ansible_ssh_hosts.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath + "rds/init_ansible_ssh_hosts.sh", AidUtil.getAid());

		// 2.执行这个初始化命令
		String mkSshHosts = fillStringByArgs(CREATE_ANSIBLE_HOSTS,
				new String[] { rdsPath, 
						savedRdsIncBase.getIncIp().replace(".", ""),
						savedRdsIncBase.getIncIp() });
		LOG.debug("---------mkSshHosts {}----------", mkSshHosts);
		AgentUtil.executeCommand(basePath + mkSshHosts, AidUtil.getAid());

		in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/rdsimage_command.yml");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/rdsimage_command.yml", cnt, AidUtil.getAid());

		// 还得上传文件
		in = RDSInstanceManager.class.getResourceAsStream("/playbook/rds/ansible_command_image.sh");
		cnt = AgentUtil.readFileLines(in);
		in.close();
		AgentUtil.uploadFile("rds/ansible_command_image.sh", cnt, AidUtil.getAid());
		AgentUtil.executeCommand("chmod +x " + basePath + "rds/ansible_command_image.sh", AidUtil.getAid());
		// 开始执行
		String runImage = fillStringByArgs(DOCKER_COMMAND_PARAM,
				new String[] { "",
						rdsPath, 
						savedRdsIncBase.getIncIp().replace(".", ""),// .cfg 文件名称 
						incRes.getSshuser(),
						incRes.getSshpassword(),
						savedRdsIncBase.getIncIp(),
						command,
						savedRdsIncBase.getUserId() + "-" + savedRdsIncBase.getServiceId() + "-" + savedRdsIncBase.getIncPort()
//						imgRes.getImageRepository() + "/" + imgRes.getImageName(),
//						savedRdsIncBase.getIncPort() + ""
						});

		LOG.debug("---------runImage {}----------", runImage);
		AgentUtil.executeCommand(basePath + runImage, AidUtil.getAid());
		
	}

//	private RDSResourcePlan getExpectResourcePlan(CreateRDS createObject, List<RdsResourcePool> resourceList,
//			RdsIncBase masterInstance) {
//		RDSResourcePlan resourcePlan = new RDSResourcePlan();
//		// 根据需求找到可用资源列表
//		List<RdsResourcePool> usableResourceList = getMasterUsableResource(createObject, resourceList);
//
//		ChoiceResStrategy crs = new ChoiceResStrategy(new MoreMemIdleChoice());
//		List<RdsResourcePool> exceptList = new LinkedList<RdsResourcePool>();
//		exceptList.add(masterInstance.instanceresourcebelonger);
//		if (null != masterInstance) {
//			exceptList.add(masterInstance.instanceresourcebelonger);
//		}
//		if (slaverInstanceList.size() > 0) {
//			for (int i = 0; i < slaverInstanceList.size(); i++) {
//				exceptList.add(slaverInstanceList.get(i).instanceresourcebelonger);
//			}
//		}
//		RdsResourcePool decidedRes = crs.makeDecision(usableResourceList, exceptList);
//
//		if (null == decidedRes) {
//			return null;
//		}
//		// 生成资源分配信息
//		decidedRes.currentport = decidedRes.currentport + 1;
//		decidedRes.usedmemory = decidedRes.usedmemory + createObject.instanceBase.instancespaceinfo.externalStorage;
//		resourcePlan.instanceresourcebelonger = decidedRes;
//
//		InstanceIpPort instanceIpPort = new InstanceIpPort(decidedRes.hostip, decidedRes.currentport,
//				Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
//		instanceIpPort.setInstanceipportbelonger(createObject.instanceBase);
//		resourcePlan.instanceIpPort = instanceIpPort;
//
//		InstanceStatus instanceStatus = new InstanceStatus(RDSCommonConstant.INS_ACTIVATION,
//				Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
//		instanceStatus.setInstancestatusbelonger(createObject.instanceBase);
//		resourcePlan.instanceStatus = instanceStatus;
//		return resourcePlan;
//	}
	
	private RDSResourcePlan getResourcePlan(CreateRDS createObject, List<RdsResourcePool> resourceList) {
		
		// 根据需求找到可用资源列表
		List<RdsResourcePool> usableResourceList = getMasterUsableResource(createObject.instanceBase.getDbStoreage(), resourceList);
		switch (createObject.instanceBase.getIncType()) {
		case InstanceType.MASTER:
			RDSResourcePlan resourcePlan = new RDSResourcePlan();
			// 选择适当的主机进行分配资源
			ChoiceResStrategy crs = new ChoiceResStrategy(new MoreMemIdleChoice());
			RdsResourcePool decidedRes = crs.makeDecision(usableResourceList);
			if (null == decidedRes) {
				return null;
			}
			// 生成资源分配信息
			decidedRes.setCurrentport(decidedRes.getCurrentport() + 1);
			decidedRes.setUsedmemory(decidedRes.getUsedmemory().intValue() + createObject.instanceBase.getDbStoreage());
			
			resourcePlan.instanceresourcebelonger = decidedRes;
			resourcePlan.ip = decidedRes.getHostip();
			resourcePlan.port = decidedRes.getCurrentport();
			resourcePlan.Status = RDSCommonConstant.INS_ACTIVATION;
			
			return resourcePlan;
		case InstanceType.BATMASTER:
		case InstanceType.SLAVER:
		default:
		}
		return null;
	}
	
	/**
	 * 不允许在同一台机子上跑多个实例
	 * 测试时条件有限，所以先这样哈哈
	 * 
	 * @param masterInstance
	 * @param resourceList
	 * @param exceptInstanceList
	 * @return
	 */
	private RDSResourcePlan getExpectResourcePlan(RdsIncBase masterInstance, List<RdsResourcePool> resourceList,
			List<RdsResourcePool> exceptInstanceList) {
		
		RDSResourcePlan resourcePlan = new RDSResourcePlan();
		// 根据需求找到可用资源列表
		List<RdsResourcePool> usableResourceList = getMasterUsableResource(masterInstance.getDbStoreage(), resourceList);

		ChoiceResStrategy crs = new ChoiceResStrategy(new MoreMemIdleChoice());
//		RdsResourcePool decidedRes = crs.makeDecision(usableResourceList, exceptInstanceList);
		RdsResourcePool decidedRes = crs.makeDecision(usableResourceList);

		if (null == decidedRes) {
			return null;
		}
		// 生成资源分配信息
		decidedRes.setCurrentport(decidedRes.getCurrentport() + 1);
		decidedRes.setUsedmemory(decidedRes.getUsedmemory() + masterInstance.getDbStoreage());
		resourcePlan.instanceresourcebelonger = decidedRes;

		resourcePlan.instanceresourcebelonger = decidedRes;
		resourcePlan.ip = decidedRes.getHostip();
		resourcePlan.port = decidedRes.getCurrentport();
		resourcePlan.Status = RDSCommonConstant.INS_ACTIVATION;
		return resourcePlan;
	}

	private boolean CheckData(CreateRDS createObject) {
//		if ((null == createObject.token) && (null == createObject.instanceBase.getRdsRdsIncBase().getInstancename())
//				&& (null == createObject.instanceBase.getRdsRdsIncBase().getUserId()) 
//				&& (null == createObject.instanceBase.getRdsRdsIncBase().getSerialNumber())
//				&& (1 > createObject.instanceBase.getRdsRdsIncBase().getInstancenetworktype())
//				&& (null == createObject.instanceBase.getRdsInstanceSpaceInfo())
//				&& (null == createObject.instanceBase.getRdsRdsIncBaseConfig())
//				&& (null == createObject.instanceBase.getRdsImageResource())
//				&& (null == createObject.instanceBase.getRdsInstanceRootAccount())) {
//			return false;
//		} else {
//			return true;
//		}
		return true;
	}



//	/**
//	 * 获取可用资源 空间满足需求，状态满足需求，端口满足需求
//	 * 
//	 * @param createObject
//	 * @param resourceList
//	 * @return
//	 */
//	private List<RdsResourcePool> getMasterUsableResource(CreateRDS createObject, List<RdsResourcePool> resourceList) {
//		// for(int i = 0; i < resourceList.size(); i++){
//		List<RdsResourcePool> canUseRes = new LinkedList<RdsResourcePool>();
//		for (RdsResourcePool rp : resourceList) {
//			int canUseExtMemSize = rp.getTotalmemory() - rp.getUsedmemory();
//			if ((RDSCommonConstant.RES_STATUS_USABLE == rp.getStatus()) && (RDSCommonConstant.RES_CYCLE_USABLE == rp.getCycle())
//					&& (canUseExtMemSize > createObject.instanceBase.getDbStoreage())
//					&& ((rp.getCurrentport() + 1) < rp.getMaxport())) {
//				// if((decidedRes.currentport+1) < decidedRes.maxport){
//				canUseRes.add(rp);
//			}
//		}
//		return canUseRes;
//	}
	/**
	 * 获取可用资源 空间满足需求，状态满足需求，端口满足需求
	 * 
	 * @param createObject
	 * @param resourceList
	 * @return
	 */
	private List<RdsResourcePool> getMasterUsableResource(int externalStorage, List<RdsResourcePool> resourceList) {
		// for(int i = 0; i < resourceList.size(); i++){
		List<RdsResourcePool> canUseRes = new LinkedList<RdsResourcePool>();
		for (RdsResourcePool rp : resourceList) {
			int canUseExtMemSize = rp.getTotalmemory() - rp.getUsedmemory();
			if ((RDSCommonConstant.RES_STATUS_USABLE == rp.getStatus()) && (RDSCommonConstant.RES_CYCLE_USABLE == rp.getCycle())
					&& (canUseExtMemSize > externalStorage)
					&& ((rp.getCurrentport() + 1) < rp.getMaxport())) {
				// if((decidedRes.currentport+1) < decidedRes.maxport){
				canUseRes.add(rp);
			}
		}
		return canUseRes;
	}


	/**
	 * WARNING!!!只能传入对象的拷贝！
	 * 当前存储字段有
	 * 
	 * @param resourcePlan
	 * @param createObject
	 * @return 返回当前账户的ID
	 * 
	 */
	private RdsIncBase savePlan(RDSResourcePlan resourcePlan, RdsIncBase instanceBase,int RdsIncBaseNetworkType) {
//		RdsIncBase instanceBase = createObject.instanceBase;
		instanceBase.setId(null);
		
		instanceBase.setIncType(RdsIncBaseNetworkType);
		if(RdsIncBaseNetworkType == InstanceType.BATMASTER){
//			instanceBase.setMasterid(instanceBase.getId());
			instanceBase.setIncName(instanceBase.getIncName() + "-BATMASTER-" + Math.random());
		}
		if(RdsIncBaseNetworkType == InstanceType.SLAVER){
//			instanceBase.setMasterid(instanceBase.getId());
			instanceBase.setIncName(instanceBase.getIncName() + "-SLAVER-" + Math.random());
		}
		
		// RdsIncBase作为子表时的指针指向ImageResource和RdsResourcePool，但因为ImageResource已经存在，不需要关联
		instanceBase.setResId(resourcePlan.instanceresourcebelonger.getResourceid());
		// 将生成信息保存到RdsIncBase
		instanceBase.setIncIp(resourcePlan.ip);
		instanceBase.setIncPort(resourcePlan.port);
		instanceBase.setIncStatus(resourcePlan.Status);
		instanceBase.setMysqlVolumnPath(resourcePlan.instanceresourcebelonger.getVolumnPath());
		instanceBase.setCreateTime(new Timestamp(System.currentTimeMillis()));
		instanceBase.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		
		// 保存实例
		RdsIncBaseMapper resMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		resMapper.insert(instanceBase);
		RdsIncBaseCriteria cri = new RdsIncBaseCriteria();
		cri.createCriteria().andIncNameEqualTo(instanceBase.getIncName()).andIncTypeEqualTo(instanceBase.getIncType()).andIncIpEqualTo(instanceBase.getIncIp()).andIncPortEqualTo(instanceBase.getIncPort());
		// 刚出炉的数据
		instanceBase = resMapper.selectByExample(cri).get(0);
		// 更新资源
		RdsResourcePoolMapper rdsResMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		rdsResMapper.updateByPrimaryKey(resourcePlan.instanceresourcebelonger);

		return instanceBase;
	}

	private boolean CheckLegal(String user_id, String serial_id, String token) {
		return true;
	}

	/**
	 * master修改后关联slaver和batmaster也要修改 但是无法直接修改slaver和batmaster
	 * 这里主要是指修改instancespaceinfo即空间信息
	 * modify中只能扩充容量无法缩小容量
	 * http://www.linuxidc.com/Linux/2015-01/112245.htm
	 */
	@Override
	public String modify(String modify) {
		ModifyRDS modifyRDSObject = g.getGson().fromJson(modify, ModifyRDS.class);
		Stack<RdsIncBase> instanceStack ;
		Stack<RdsIncBase> instanceStackBack = new Stack<RdsIncBase>();
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(modifyRDSObject.instanceid);
		
		RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		
		if(instanceCheck.getIncType() == InstanceType.MASTER){
			// 暂停所有服务
			instanceStack = getInstanceStack(modifyRDSObject.instanceid);
			if(!instanceStack.isEmpty()){
				while(!instanceStack.isEmpty()){
					RdsIncBase instance = instanceStack.pop();
					instance.setIncStatus(RDSCommonConstant.INS_STOPPING);
					incBaseMapper.updateByPrimaryKey(instance);
					// 启动mysql服务
					try {
						stopInstance(instance);
					} catch (IOException | PaasException e) {
						e.printStackTrace();
						ModifyRDSResult stopRDSResult = new ModifyRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
						return g.getGson().toJson(stopRDSResult);
					}
					// 修改数据库中服务器状态
					instance.setIncStatus(RDSCommonConstant.INS_STOPPED);
					instanceStackBack.add(instance);
				};
			}else{
				ModifyRDSResult stopRDSResult = new ModifyRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
				return g.getGson().toJson(stopRDSResult);
			}
			// 对master、batmaster、slaver选择进行扩充，数据库修改空间配置
			ResIncPlan resIncPlan = ResIncPlanSchemer(instanceStackBack,modifyRDSObject.argmentedExternalStorage);
			if(resIncPlan.increaseList.size() > 0 && resIncPlan.unincreaseList.size() == 0 ){
				for(RdsIncBase ib : resIncPlan.increaseList){
					
					RdsResourcePool resPool = resMapper.selectByPrimaryKey(ib.getResId());
					resPool.setUsedmemory(resPool.getUsedmemory() 
							- ib.getDbStoreage() + modifyRDSObject.argmentedExternalStorage);
					ib.setDbStoreage(modifyRDSObject.argmentedExternalStorage);
					resMapper.updateByPrimaryKey(resPool);
					incBaseMapper.updateByPrimaryKey(ib);
					
					configModify(ib,modifyRDSObject.argmentedExternalStorage);
				}
			} else {
				ModifyRDSResult modifyRDSResult = new ModifyRDSResult(ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE);
				return g.getGson().toJson(modifyRDSResult);
			}
			// 如果修改成功则启动所用相关服务
			ModifyRDSResult modifyRDSResult = new ModifyRDSResult(ResponseResultMark.SUCCESS);
			return g.getGson().toJson(modifyRDSResult);
		}else {
			ModifyRDSResult modifyRDSResult = new ModifyRDSResult(ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY);
			return g.getGson().toJson(modifyRDSResult);
		}
	}


	/**
	 * 传入的栈顶必须是master
	 * @param instanceStackBack
	 * @param argmentedExternalStorage 扩充到的容量大小
	 * @return
	 */
	private ResIncPlan ResIncPlanSchemer(Stack<RdsIncBase> instanceStackBack, int argmentedExternalStorage) {
		ResIncPlan resIncPlan = new ResIncPlan();
		List<RdsIncBase> increaseList = new ArrayList<RdsIncBase>();
		List<RdsIncBase> unincreaseList = new ArrayList<RdsIncBase>();
		// 判断master、slaver、batslaver所在的资源是否支持扩容，如资源不足则列为不支持扩容并返回不支持列表，将不支持扩容工作生成工单手工操作
		for(RdsIncBase ib : instanceStackBack){
			RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
			RdsResourcePool resPool = resMapper.selectByPrimaryKey(ib.getResId());
			if((resPool.getTotalmemory() - resPool.getUsedmemory() 
					+ ib.getDbStoreage() - argmentedExternalStorage) < RDSCommonConstant.LIMIT_IDLE_USEABLE_EXTERNAL_STORAGE){
				increaseList.add(ib);
			} else {
				unincreaseList.add(ib);
			}
		}
		resIncPlan.increaseList = increaseList;
		resIncPlan.unincreaseList = unincreaseList;
		
		return resIncPlan;
	}

	/**
	 * 获取全部公有方法
	 */
	@Override
	public String getFuncList() {
		List<MedthodDescribe> list = new LinkedList<MedthodDescribe>();
		Method[] methodList = RDSInstanceManager.class.getMethods();
		for(Method m : methodList){
			MedthodDescribe md = new MedthodDescribe();
			md.methodName = m.getName();
			md.methodReturnType = m.getReturnType().getName();
			list.add(md);
		}
		return g.getGson().toJson(list);
	}

	@Override
	public String start(String startApply) {
		StartRDS startRDSObject = g.getGson().fromJson(startApply, StartRDS.class);
		Stack<RdsIncBase> instanceStack ;
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(startRDSObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		
		// 判断用户权限
		// 判断服务器状态
		instanceStack = getInstanceStack(startRDSObject.instanceid);
		if(!instanceStack.isEmpty()){
			while(!instanceStack.isEmpty()){
				RdsIncBase instance = instanceStack.pop();
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					startInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					StartRDSResult stopRDSResult = new StartRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
					return g.getGson().toJson(stopRDSResult);
				}
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(startRDSResult);
		}
		

		StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(startRDSResult);
	}

	@Override
	public String stop(String stopApply) {
		StopRDS stopRDSObject = g.getGson().fromJson(stopApply, StopRDS.class);
		Stack<RdsIncBase> instanceStack;
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(stopRDSObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		// 判断用户权限
		// 判断服务器状态
		instanceStack = getInstanceStack(stopRDSObject.instanceid);
		if(!instanceStack.isEmpty()){
			while(!instanceStack.isEmpty()){
				RdsIncBase instance = instanceStack.pop();
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					stopInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.ERROR_BAD_CONFIG);
					return g.getGson().toJson(stopRDSResult);
				}
				
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(stopRDSResult);
		}
		// 修改数据库中服务器状态

		StopRDSResult stopRDSResult = new StopRDSResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(stopRDSResult);
	}
	
	


	/**
	 * 
	 */
	@Override
	public String restart(String restartApply) {
		RestartRDS restartObject = g.getGson().fromJson(restartApply, RestartRDS.class);
		Stack<RdsIncBase> instanceStack;
		Stack<RdsIncBase> instanceStackBack = new Stack<RdsIncBase>();
		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		// 只接受修改主mysql
		RdsIncBase instanceCheck = incBaseMapper.selectByPrimaryKey(restartObject.instanceid);
//		if(instanceCheck.getIncType() != InstanceType.MASTER){
//			StartRDSResult startRDSResult = new StartRDSResult(ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE);
//			return g.getGson().toJson(startRDSResult);
//		}
		instanceStack = getInstanceStack(restartObject.instanceid);
		if(!instanceStack.isEmpty()){
			while(!instanceStack.isEmpty()){
				RdsIncBase instance = instanceStack.pop();
				instance.setIncStatus(RDSCommonConstant.INS_STARTING);
				incBaseMapper.updateByPrimaryKey(instance);
				// 启动mysql服务
				try {
					restartInstance(instance);
				} catch (IOException | PaasException e) {
					e.printStackTrace();
					RestartResult restartRDSResult = new RestartResult(ResponseResultMark.ERROR_BAD_CONFIG);
					return g.getGson().toJson(restartRDSResult);
				}
				// 修改数据库中服务器状态
				instance.setIncStatus(RDSCommonConstant.INS_STARTED);
				incBaseMapper.updateByPrimaryKey(instance);
			};
		}else{
			RestartResult restartRDSResult = new RestartResult(ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY);
			return g.getGson().toJson(restartRDSResult);
		}
		
		RestartResult restartRDSResult = new RestartResult(ResponseResultMark.SUCCESS);
		return g.getGson().toJson(restartRDSResult);
	}



	@Override
	public String getinstanceinfo(String getinstanceinfo) {
//		GetInstanceInfoRDS getStatusObject = g.getGson().fromJson(getinstanceinfo, GetInstanceInfoRDS.class);
////		List<RdsIncBase> instanceList = new ArrayList<RdsIncBase>();
////		EntityToWhere<RdsIncBase> e2Where = new EntityToWhere<RdsIncBase>();
//		// 获取mysql服务状态
//		RdsIncBaseMapper incBaseMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
//		List<RdsIncBase> rdsIncBaseList = incBaseMapper.selectByExample(getStatusObject.rdsIncBaseCriteria);
////		if(null != getStatusObject.sort){
////			try {
////				instanceList = SpecialQueryRepo.findByInstanceParam(e2Where.entity2WhereSort(getStatusObject.instancebase,getStatusObject.sort));
////			} catch (IllegalArgumentException | IllegalAccessException e) {
////				e.printStackTrace();
////			}
////		} else {
////			try {
////				instanceList = SpecialQueryRepo.findByInstanceParam(e2Where.entity2Where(getStatusObject.instancebase));
////			} catch (IllegalArgumentException | IllegalAccessException e) {
////				e.printStackTrace();
////			}
////		}
//		return g.getGson().toJson(rdsIncBaseList);
		
		
		GetIncInfo getIncInfo = g.getGson().fromJson(getinstanceinfo, GetIncInfo.class);
		RdsIncBaseMapper incMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		if(getIncInfo.getAll == 1){
			RdsIncBaseCriteria cri = new RdsIncBaseCriteria();
			cri.createCriteria().andIdBetween(1, 10000000);
			List<RdsIncBase> resultList = incMapper.selectByExample(cri);
			return g.getGson().toJson(resultList);
		} else {
			List<RdsIncBase> resultList = incMapper.selectByExample(getIncInfo.rdsIncBaseCri);
			return g.getGson().toJson(resultList);
		}
	}
	
	/**
	 * 删除相应ZK节点
	 * @param instanceRDS
	 * @throws PaasException 
	 */
	private void deleteZK(RdsIncBase instanceRDS) throws PaasException {
		CCSComponentOperationParam op = getZKBase(instanceRDS.getUserId());
		op.setPath(RDSCommonConstant.RDS_ZK_PATH + instanceRDS.getServiceId());
		iCCSComponentManageSv.delete(op);
	}
	
	/**
	 * 查看节点信息
	 * @param userId
	 * @return
	 */
	protected CCSComponentOperationParam getZKBase(String userId) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPathType(PathType.READONLY);
		return op;
	}
	
	private void save2ZK(RdsIncBase instanceRDS) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		System.out.println("save2ZK");
		op.setUserId(instanceRDS.getUserId());
		op.setPath(RDSCommonConstant.RDS_ZK_PATH + instanceRDS.getServiceId());
		op.setPathType(PathType.READONLY);
		System.out.println(op);
		try {
			iCCSComponentManageSv.add(op, g.getGson().toJson(instanceRDS));
		} catch (PaasException e) {
			e.printStackTrace();
		}
	}

}
