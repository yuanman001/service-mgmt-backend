package com.ai.paas.ipaas.mcs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.agent.client.AgentClient;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.mcs.dao.interfaces.McsResourcePoolMapper;
import com.ai.paas.ipaas.mcs.dao.interfaces.McsUserCacheInstanceMapper;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsResourcePool;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsResourcePoolCriteria;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstance;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstanceCriteria;
import com.ai.paas.ipaas.mcs.service.constant.McsConstants;
import com.ai.paas.ipaas.mcs.service.interfaces.IMcsSv;
import com.ai.paas.ipaas.mcs.service.util.McsParamUtil;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.DateTimeUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class McsSvImpl implements IMcsSv {
	private static transient final Logger log = LoggerFactory.getLogger(McsSvImpl.class);

	@Autowired 
	private McsSvHepler mcsSvHepler;
	
	@Autowired 
	private ICCSComponentManageSv iCCSComponentManageSv;	

	// 代理 上传文件的地址
	private String agentFile = "";
	// 代理 执行CMD命令的地址
	private String agentCmd = "";
	// 服务端 用户路径
	private String cachePath = "";

	/**
	 * 服务开通功能
	 */
	@Override
	public String openMcs(String param) throws PaasException {
		/** 获取服务号配置参数  **/
		Map<String, String> map = McsParamUtil.getParamMap(param);
		final String userId = map.get(McsConstants.USER_ID);
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		String serviceName = map.get(McsConstants.SERVICE_NAME);
		String haMode = map.get(McsConstants.HA_MODE);
		String capacity = map.get(McsConstants.CAPACITY);
		int cacheSize = Integer.valueOf(capacity);

		/** 判断用户的这个缓存服务是否已经开通 **/
		if (existsService(userId, serviceId)) {
			log.info("------- 用户服务已存在，开通成功");
			return McsConstants.SUCCESS_FLAG;
		}

		/** 开通单例  **/
		if (McsConstants.MODE_SINGLE.equals(haMode)) {
			/** 1.选择开通服务的资源：选择剩余内存最多的主机  **/
			McsResourcePool mcsResourcePool = selectMcsResourcePool(cacheSize);
			
			/** 需更新mcs资源表AgentCmd字段，只存agent端口号！ **/
			Integer agentPort = Integer.valueOf(mcsResourcePool.getAgentCmd());
			Integer cachePort = mcsResourcePool.getCachePort();
			String cacheHostIp = mcsResourcePool.getCacheHostIp();
			String requirepass = mcsSvHepler.getRandomKey();

			/** 2.初始化agent  **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 3.创建端口号命名的文件夹  **/
			String commonconfigPath = mcsResourcePool.getCachePath() + McsConstants.FILE_PATH;
			addConfigFolder(ac, commonconfigPath, cachePort);
			
			/** 4.创建配置文件并上传至服务器指定目录  **/
			addMcsConfig(ac, cachePort, capacity, requirepass);
			
			/** 5.启动单例  **/
			startMcsIns(ac, commonconfigPath, cachePort);
			log.info("---------启动redis成功!");
			
			/** 6.处理zk 配置 **/
			addCcsConfig(userId, serviceId, cacheHostIp + ":" + cachePort, requirepass);
			log.info("----------处理zk 配置成功！");
			
			/** 7.mcs用户实例表增加纪录  **/
			addUserInstance(userId, serviceId, capacity, cacheHostIp, cachePort, requirepass, serviceName);
			log.info("---------处理用户实例成功！");
			
		} else if (McsConstants.MODE_CLUSTER.equals(haMode)) {
			log.info("1----------------进入集群选择主机");
			final int clusterCacheSize = Math.round(cacheSize / McsConstants.CACHE_NUM * 2);

			// 选取 资源池  
			final List<String> resultList = selectMcsRessCluster(clusterCacheSize, McsConstants.CACHE_NUM);
			
			// 处理mcs服务端文件等
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(new Runnable() {
				public void run() {
					try {
						log.info("3----------------处理mcs服务端");
						String hostsCluster = addMcsConfigCluster(resultList,
								userId, serviceId, clusterCacheSize);
						// 添加zk配置
						log.info("4----------------处理zk 配置");
						addCcsConfig(userId, serviceId,
								hostsCluster.substring(1), null);
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			});

			log.info("5----------------处理用户实例");
			for (String address : resultList) {
				String[] add = address.split(":");
				// -------mcs_user_cache_instance表新增记录
				addUserInstance(userId, serviceId, clusterCacheSize + "",
						add[0], Integer.parseInt(add[1]), null, serviceName);
			}

		} else if (McsConstants.MODE_REPLICATION.equals(haMode)) {
			log.info("1----------------进入主从模式选择主机");
			// --------选择剩余内存最多的主机 缓存资源
			McsResourcePool mcsResourcePool = selectMcsRepResource(cacheSize * 2);
			log.info("2----------------选择主机:"
					+ mcsResourcePool.getCacheHostIp() + "端口："
					+ mcsResourcePool.getCachePort());
			
			//获取随机数作为redis密码。
			String requirepass = mcsSvHepler.getRandomKey();
			
			log.info("3----------------处理mcs服务端");
			// 处理mcs服务端配置文件
			addMasterConfig(mcsResourcePool, mcsResourcePool.getCachePort(),
					capacity, requirepass);
			addSlaveConfig(mcsResourcePool, mcsResourcePool.getCachePort() - 1,
					capacity, requirepass);
			
			// 处理zk 配置
			log.info("4----------------处理zk 配置");
			addCcsConfig(userId, serviceId, mcsResourcePool.getCacheHostIp()
					+ ":" + mcsResourcePool.getCachePort(), requirepass);
			
			// -------mcs_user_cache_instance表新增记录
			log.info("5----------------处理用户实例");
			addUserInstance(userId, serviceId, capacity,
					mcsResourcePool.getCacheHostIp(),
					mcsResourcePool.getCachePort(), requirepass, serviceName);

		} else if (McsConstants.MODE_SENTINEL.equals(haMode)) {
			log.info("1----------------进入集群选择主机");
			final int clusterCacheSize = Math.round(cacheSize
					/ McsConstants.CACHE_NUM * 2);
			
			// 选取 资源池
			final List<String> resultList = selectMcsRessCluster(
					clusterCacheSize, McsConstants.SENTINEL_NUM);
			log.info("2----------------选择主机:" + resultList.toString());
			log.info("3----------------处理mcs服务端");
			
			//获取随机数作为redis密码。
			String requirepass = mcsSvHepler.getRandomKey();
			
			// 选取第一项为主，二三项为从，四五六启动sentinel进程
			String masterInfo = resultList.get(0);
			configSenMaster(masterInfo, capacity, requirepass);
			for (int k = 1; k <= 2; k++) {
				configSenSlave(resultList.get(k), resultList.get(0), capacity,
						requirepass);
			}
			
			for (int j = 3; j <= 5; j++) {
				configSentinel(resultList.get(j), masterInfo);
			}
			
			log.info("4----------------处理zk 配置");
			confSenZk(userId, serviceId, resultList, requirepass);
			
			log.info("5----------------处理用户实例");
			for (String address : resultList) {
				String[] add = address.split(":");
				// -------mcs_user_cache_instance表新增记录
				addUserInstance(userId, serviceId, clusterCacheSize + "",
						add[0], Integer.parseInt(add[1]), null, serviceName);
			}
		}

		// 开通成功
		log.info("----------------开通成功");
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 选择缓存资源
	 * 
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	private McsResourcePool selectMcsRepResource(int cacheSize) throws PaasException {
		List<McsResourcePool> resp = getBestResource(1);
		McsResourcePool mcsResourcePool = null;
		if(resp.size()>0) {
			mcsResourcePool = resp.get(0);
		}
		// 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号
		if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
			mcsResourcePool.setCachePort(getCanUseInstance(
					mcsResourcePool.getCacheHostIp()).getCachePort());
		} else {
			if (mcsResourcePool.getCachePort() == mcsResourcePool.getMaxPort()) {
				mcsResourcePool.setCycle(1);
			}
			mcsResourcePool.setCachePort(mcsResourcePool.getCachePort() + 2);
			mcsResourcePool.setCacheMemoryUsed(mcsResourcePool
					.getCacheMemoryUsed() + cacheSize);
			int changeRow = updateMcsResource(mcsResourcePool);

			if (changeRow != 1) {
				throw new PaasException("更新资源失败");
			}
		}
		
		//TODO: ???? 
		cachePath = mcsResourcePool.getCachePath();
		return mcsResourcePool;
	}

	private int updateMcsResource(McsResourcePool mcsResourcePool)
			throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
		rpmc.createCriteria().andIdEqualTo(mcsResourcePool.getId())
				.andCachePortEqualTo(mcsResourcePool.getCachePort() - 2);
		return rpm.updateByExampleSelective(mcsResourcePool, rpmc);
	}

	/**
	 * 服务是否已经存在
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	private boolean existsService(String userId, String serviceId) {
		McsUserCacheInstanceCriteria cc = new McsUserCacheInstanceCriteria();
		cc.createCriteria().andUserIdEqualTo(userId).andSerialNumberEqualTo(serviceId)
			.andStatusEqualTo(McsConstants.VALIDATE_STATUS);
		McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
		return im.countByExample(cc) > 0;
	}

	/**
	 * 集群模式时，新增服务端的配置文件
	 * 
	 * @param resultList
	 * @param userId
	 * @param cacheSize
	 * @throws PaasException
	 */
	//TODO: 此方法过大，重构，并修改调用agent部分。
	private String addMcsConfigCluster(List<String> resultList, String userId,
			String serviceId, int cacheSize) throws PaasException {
		String redisIpCluster = null;
		String redisPortCluster = null;
		String memorySizeCluster = cacheSize + "m";
		String fileNameCluster = null;
		String userDirNameCluster = userId + "_" + serviceId;
		String fileDetailCluster = null;

		String cmdCluster = null;
		String listCmdCluster = "CMD| path=" + cachePath
				+ McsConstants.CLUSTER_FILE_PATH + userDirNameCluster
				+ ",cmd=redis-trib.rb create --replicas 1";
		
		String hostsCluster = "";
		String uriCmdCluster = "";

		String address2 = resultList.get(0);
		String[] add2 = address2.split(":");
		redisIpCluster = add2[0];
		String uriCmdCluster2 = McsConstants.AGENT_URL_BASE + redisIpCluster
				+ agentCmd;
		
		for (String address : resultList) {
			String[] add = address.split(":");
			redisIpCluster = add[0];
			uriCmdCluster = McsConstants.AGENT_URL_BASE + redisIpCluster
					+ agentCmd;
			String uriCreateCluster = McsConstants.AGENT_URL_BASE
					+ redisIpCluster + agentFile;
			redisPortCluster = add[1];
			fileNameCluster = "redis-" + redisPortCluster + ".conf";
			cmdCluster = "CMD| path=" + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + ",cmd=mkdir -p "
					+ userDirNameCluster;
			try {
				log.info("启动time-----------" + new Date());
				executeInstruction(uriCmdCluster, cmdCluster);
				log.info("-----------启动time" + new Date());
				cmdCluster = "CMD| path=" + cachePath
						+ McsConstants.CLUSTER_FILE_PATH + userDirNameCluster
						+ ",cmd=mkdir -p " + redisPortCluster;
				executeInstruction(uriCmdCluster, cmdCluster);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// throw new
				// PaasException(ResourceUtil.getMessage(McsConstants.CREATE_FILE_ERROR)+e.getMessage(),
				// e);
				throw new PaasException("集群 生成文件失败：" + e.getMessage(), e);
			}
			
//			fileDetailCluster = fileNameCluster + "||CACHE|" + cachePath
//					+ McsConstants.CLUSTER_FILE_PATH + userDirNameCluster + "/"
//					+ redisPortCluster + "/" + "||include " + cachePath
//					+ McsConstants.CLUSTER_FILE_PATH + "redis-common.conf"
//					+ "||pidfile " + cachePath + McsConstants.FILE_PATH
//					+ "rdpid/redis-" + redisPortCluster + ".pid" + "||port "
//					+ redisPortCluster + "||cluster-enabled yes"
//					+ "||maxmemory " + memorySizeCluster
//					+ "||cluster-config-file nodes.conf"
//					+ "||cluster-node-timeout 5000" + "||appendonly yes";
			
			try {
//				uploadCacheFile(uriCreateCluster, fileDetailCluster);
				
				log.info("上传time-----------" + new Date());
				String clusterConfig = fileNameCluster;
				
				//TODO: 配置文件可提炼出共用的方法。
				//TODO: 可通用，传入 redisPort, memorySize, password 即可。
				String configDetail = "||include " + cachePath
						+ McsConstants.CLUSTER_FILE_PATH + "redis-common.conf"
						+ "||pidfile " + cachePath + McsConstants.FILE_PATH + "rdpid/redis-" + redisPortCluster + ".pid" 
						+ "||port " + redisPortCluster 
						+ "||cluster-enabled yes"
						+ "||maxmemory " + memorySizeCluster
						+ "||cluster-config-file nodes.conf"
						+ "||cluster-node-timeout 5000"
						+ "||appendonly yes";
				
				AgentClient ac = new AgentClient("10.1.228.199", 60004);
				mcsSvHepler.uploadFile(ac, clusterConfig, configDetail);
				log.info("上传time-----------" + new Date());
				log.info("上传配置文件成功！");
				
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				// throw new
				// PaasException(ResourceUtil.getMessage(McsConstants.UPLOAD_FILE_ERROR)+e.getMessage(),
				// e);
				throw new PaasException("集群 上传文件失败：" + e.getMessage(), e);
			}
			
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage(), e1);
			}
			
			cmdCluster = "CMD| path=" + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + userDirNameCluster + "/"
					+ redisPortCluster + ",cmd=redis-server " + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + userDirNameCluster + "/"
					+ redisPortCluster + "/redis-" + redisPortCluster
					+ ".conf > redis.log &";
			log.info("-----------:" + cmdCluster);
			
			try {
				executeInstruction(uriCmdCluster, cmdCluster);
				log.info("启动redis成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				 throw new PaasException(McsConstants.START_ERROR+ e.getMessage(), e);
//				throw new PaasException("集群 启动单个失败：" + e.getMessage(), e);
			}
			
			listCmdCluster = listCmdCluster + " " + address;
			hostsCluster = hostsCluster + ";" + address;
		}
		
		try {
			log.info("集群命令：" + listCmdCluster + "----------uri" + uriCmdCluster2);
			executeInstruction(uriCmdCluster2, listCmdCluster);
			log.info("生成集群成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			 throw new PaasException(McsConstants.START_ERROR+ e.getMessage(), e);
//			throw new PaasException("集群启动失败：" + e.getMessage(), e);
		}
		
		return hostsCluster;
	}

	/**
	 * 集群模式，选择资源池
	 * 
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	//TODO: 方法较大，需要重构
	private List<String> selectMcsRessCluster(int cacheSize, int size)
			throws PaasException {
		
		//TODO: ???? 成员变量为何再次处赋值 ？？？ 需在使用处进行赋值，或提前赋值。
		List<McsResourcePool> cacheResourceList = getBestResource(size);
		agentFile = cacheResourceList.get(0).getAgentFile();
		agentCmd = cacheResourceList.get(0).getAgentCmd();
		cachePath = cacheResourceList.get(0).getCachePath();
		
		int hostNum = cacheResourceList.size();
		int i = 0;
		int j = 1;
		int k = hostNum;
		
		List<String> resultList = new ArrayList<>();
		String ip = null;
		int port = -1;
		int count = 0;
		while (i < size && count < (size + 1)) {
			for (int m = 0; m < k; m++) {
				if ((cacheResourceList.get(m).getCacheMemory() - cacheResourceList
						.get(m).getCacheMemoryUsed()) < cacheSize * j) {
					k = m;
					j++;
					continue;
				}
				ip = cacheResourceList.get(m).getCacheHostIp();
				if (cacheResourceList.get(m) != null
						&& cacheResourceList.get(m).getCycle() == 1) {
					cacheResourceList.get(m).setCachePort(
							getCanUseInstance(
									cacheResourceList.get(m).getCacheHostIp())
									.getCachePort());
				} else {
					cacheResourceList.get(m).setCachePort(
							cacheResourceList.get(m).getCachePort() + 1);
					cacheResourceList.get(m).setCacheMemoryUsed(
							cacheResourceList.get(m).getCacheMemoryUsed()
									+ cacheSize);
					if (cacheResourceList.get(m).getCachePort() == cacheResourceList
							.get(m).getMaxPort()) {
						cacheResourceList.get(m).setCycle(1);
					}
					int changeRow = updateResource(cacheResourceList.get(m));
					;
					if (changeRow != 1) {
						throw new PaasException("更新资源失败");
					}
				}
				port = cacheResourceList.get(m).getCachePort();
				resultList.add(ip + ":" + port);
				i++;
				log.info("2----------------选择主机:" + ip + "端口：" + port);
			}
			
			count++;
		}
		if (count > size)
			throw new PaasException("mcs resource not enough.");
		
		return resultList;
	}

	/**
	 * 选择缓存资源
	 * 
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	//TODO: 与方法 selectMcsRepResource(int) 有何不同？？？ 如果功能相近，考虑重构合并。
	private McsResourcePool selectMcsResourcePool(int cacheSize)
			throws PaasException {
		List<McsResourcePool> resp = getBestResource(1);
		McsResourcePool mcsResourcePool = resp.get(0);
		
		// 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号
		if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
			mcsResourcePool.setCachePort(getCanUseInstance(mcsResourcePool.getCacheHostIp()).getCachePort());
		} else {
			if (mcsResourcePool.getCachePort() == mcsResourcePool.getMaxPort()) {
				mcsResourcePool.setCycle(1);
			}
			mcsResourcePool.setCachePort(mcsResourcePool.getCachePort() + 1);
			mcsResourcePool.setCacheMemoryUsed(mcsResourcePool.getCacheMemoryUsed() + cacheSize);
			int changeRow = updateResource(mcsResourcePool);

			if (changeRow != 1) {
				throw new PaasException("更新资源失败");
			}
		}
		
		//TODO: 成员变量为何在此处赋值 ？？？
		cachePath = mcsResourcePool.getCachePath();
		
		return mcsResourcePool;
	}

	/**
	 * 新增用户的缓存实例
	 * 
	 * @param userId
	 * @param serviceId
	 * @param capacity
	 * @param mcsResourcePool
	 * @throws PaasException
	 */
	private void addUserInstance(String userId, String serviceId,
			String capacity, String ip, int port, String pwd, String serviceName) throws PaasException {
		McsUserCacheInstance bean = new McsUserCacheInstance();
		bean.setUserId(userId); // 用户id
		bean.setCacheHost(ip); // 资源主机ip
		bean.setCacheMemory(Integer.valueOf(capacity)); // 分配内存大小
		bean.setStatus(McsConstants.VALIDATE_STATUS);
		bean.setBeginTime(DateTimeUtil.getNowTimeStamp());
		bean.setEndTime(DateTimeUtil.getNowTimeStamp());
		bean.setSerialNumber(serviceId);
		bean.setCachePort(port);
		bean.setPwd(pwd);
		bean.setServiceName(serviceName);

		ServiceUtil.getMapper(McsUserCacheInstanceMapper.class).insert(bean);
	}

	/**
	 * 在zk中记录申请信息
	 * 
	 * @param userId
	 * @param serviceId
	 * @param mcsResourcePool
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addCcsConfig(String userId, String serviceId, String hosts, String requirepass) throws PaasException {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();
		dataJson.addProperty("hosts", hosts);
		if (requirepass != null && requirepass.length() > 0) {
			dataJson.addProperty("password", CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));
		}

		iCCSComponentManageSv.add(op, dataJson.toString());
		log.info("-----------在zk中记录申请信息成功!");

		op.setPath(McsConstants.MCS_ZK_COMMON_PATH);
		if (!iCCSComponentManageSv.exists(op)) {
			iCCSComponentManageSv.add(op, McsConstants.MCS_ZK_COMMON);
			log.info("-----------在zk中记录COMMON信息成功!");
		}
	}

	//TODO:此方法与上面的 addCcsConfig() 方法，有什么区别？？？ 是否可以合并？？
	private void confSenZk(String userId, String serviceId, List<String> hosts, String requirepass) throws PaasException {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();
		dataJson.addProperty("master", hosts.get(0));
		dataJson.addProperty("slave", hosts.get(1) + ";" + hosts.get(2));
		dataJson.addProperty("sentinel", hosts.get(3) + ";" + hosts.get(4) + ";" + hosts.get(5));
		if (requirepass != null && requirepass.length() > 0) {
			dataJson.addProperty("password", CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));
		}
		
		iCCSComponentManageSv.add(op, dataJson.toString());
		log.info("-----------在zk中记录申请信息成功!");

		op.setPath(McsConstants.MCS_ZK_COMMON_PATH);
		if (!iCCSComponentManageSv.exists(op)) {
			iCCSComponentManageSv.add(op, McsConstants.MCS_ZK_COMMON);
			log.info("-----------在zk中记录COMMON信息成功!");
		}
	}

	//TODO: 需要重构，调整调用 agent 的逻辑。
	private void addMasterConfig(McsResourcePool mcsResourcePool, int redisPort, 
			String capacity, String requirepass) throws PaasException {
		// 调用restful服务
		String memorySize = capacity + "m";

		String fileName = "redis-" + redisPort + ".conf";

		String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH
				+ ",cmd=mkdir -p " + redisPort;

		//TODO: 可通用，传入 redisPort, memorySize, password 即可。
		String configDetail = "||include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf"
				+ "||pidfile /var/run/redis-" + redisPort + ".pid"
				+ "||port " + redisPort
				+ "||maxmemory " + memorySize
				+ "||requirepass " + requirepass
				+ "||logfile " + cachePath + "/log/" + redisPort + "/redis-" + redisPort + ".log"
				+ "||tcp-keepalive 60||maxmemory-policy noeviction||appendonly yes||appendfilename \"appendonly.aof\"";
		
		String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p " + McsConstants.LOG_PATH;

		String uri = McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp() + mcsResourcePool.getAgentFile();
		String commandUri = McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp() + mcsResourcePool.getAgentCmd();

		//TODO: 修改调用 agent 的方法。上传文件的逻辑，可考虑抽象成新方法。
		try {
			executeInstruction(commandUri, mkdircmd);
			executeInstruction(commandUri, logdir);
			
//			uploadCacheFile(uri, fileDetail);
			
			AgentClient ac = new AgentClient("10.1.228.199", 60004);
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			
			//TODO: 是否需要上传空的日志文件？？？？
//			uploadCacheFile(uri, logfile);
			
			log.info("3----------------上传文件成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);
		}

		startMcsIns(
				McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
						+ mcsResourcePool.getAgentCmd(),
				mcsResourcePool.getCachePath(), redisPort);
		log.info("3----------------启动redis成功!");
	}

	//TODO: 需要重构，调整调用 agent 的逻辑。与上面的 addMasterConfig() 
	private void configSenMaster(String result, String capacity, String requirepass) throws PaasException {
		// 调用restful服务
		String memorySize = capacity + "m";
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];
		
		String fileName = "redis-" + port + ".conf";
		String uriCmdCluster = McsConstants.AGENT_URL_BASE + ip + agentCmd;
		String uriCreateCluster = McsConstants.AGENT_URL_BASE + ip + agentFile;
		String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + port;
		String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p " + McsConstants.LOG_PATH;
		String logName = "redis-" + port + ".log";
		
//		String fileDetail = fileName
//				+ "||CACHE|" + cachePath + McsConstants.FILE_PATH + port + "/"
//				+ "||include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf"
//				+ "||pidfile /var/run/redis-" + port + ".pid||port " + port
//				+ "||maxmemory " + memorySize
//				+ "||requirepass " + requirepass
//				+ "||logfile " + cachePath + "/log/" + port + "/redis-" + port + ".log"
//				+ "||tcp-keepalive 60||maxmemory-policy noeviction||appendonly yes||appendfilename \"appendonly.aof\"";

//		String logfile = logName + "||CACHE|" + cachePath
//				+ McsConstants.LOG_PATH + port + "/" + "||";

		try {
			executeInstruction(uriCmdCluster, mkdircmd);
			executeInstruction(uriCmdCluster, logdir);
			
//			uploadCacheFile(uriCreateCluster, fileDetail);
			//TODO: 配置文件可提炼出共用的方法。
			String configDetail = "||include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf"
					+ "||pidfile /var/run/redis-" + port + ".pid||port " + port
					+ "||maxmemory " + memorySize
					+ "||requirepass " + requirepass
					+ "||logfile " + cachePath + "/log/" + port + "/redis-" + port + ".log"
					+ "||tcp-keepalive 60||maxmemory-policy noeviction||appendonly yes||appendfilename \"appendonly.aof\"";
			
			//TODO: 可以放到最上面，共用此对象。
			AgentClient ac = new AgentClient("10.1.228.199", 60004);
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			
			//TODO：生成日志文件??? 为什么要生成一个空的日志文件？？是否可以去掉？？
//			uploadCacheFile(uriCreateCluster, logfile);
			mcsSvHepler.uploadFile(ac, logName, " ");
			
			log.info("3----------------上传文件成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);
		}

		startMcsIns(uriCmdCluster, cachePath, new Integer(port));
		log.info("3----------------启动redis成功!");

	}

	//TODO: 重构此方法，拆分逻辑；调用agent的部分需要调整。
	private void addSlaveConfig(McsResourcePool mcsResourcePool, int redisPort,
			String capacity, String requirepass) throws PaasException {
		// 调用restful服务
		String memorySize = capacity + "m";

		String fileName = "redis-" + redisPort + ".conf";

		String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH
				+ ",cmd=mkdir -p " + redisPort;

//		String fileDetail = fileName + "||CACHE|" + cachePath
//				+ McsConstants.FILE_PATH + redisPort + "/" + "||include "
//				+ cachePath + McsConstants.FILE_PATH + "redis-common.conf||"
//				+ "pidfile /var/run/redis-" + redisPort + ".pid||port "
//				+ redisPort + "||maxmemory " + memorySize + "||requirepass "
//				+ requirepass + "||" + "logfile " + cachePath + "/log/"
//				+ redisPort + "/redis-" + redisPort + ".log" + "||slaveof "
//				+ mcsResourcePool.getCacheHostIp() + " "
//				+ mcsResourcePool.getCachePort() + "||masterauth " + requirepass;
		
		String uri = McsConstants.AGENT_URL_BASE
				+ mcsResourcePool.getCacheHostIp()
				+ mcsResourcePool.getAgentFile();
		
		String commandUri = McsConstants.AGENT_URL_BASE
				+ mcsResourcePool.getCacheHostIp()
				+ mcsResourcePool.getAgentCmd();
		
		String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p "
				+ McsConstants.LOG_PATH;
//		String logName = "redis-" + redisPort + ".log";

		try {
			executeInstruction(commandUri, mkdircmd);
			executeInstruction(commandUri, logdir);
			
//			uploadCacheFile(uri, fileDetail);
			//TODO: 配置文件可提炼出共用的方法。
			String configDetail = "||include "
					+ cachePath + McsConstants.FILE_PATH + "redis-common.conf"
					+ "||pidfile /var/run/redis-" + redisPort + ".pid"
					+ "||port " +redisPort
					+ "||maxmemory " + memorySize 
					+ "||requirepass " + requirepass
					+ "||" + "logfile " + cachePath + "/log/" + redisPort + "/redis-" + redisPort + ".log"
					+ "||slaveof " + mcsResourcePool.getCacheHostIp() + " " + mcsResourcePool.getCachePort() 
					+ "||masterauth " + requirepass;
			
			//TODO: 可以放到最上面，共用此对象。
			AgentClient ac = new AgentClient("10.1.228.199", 60004);
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			
			//TODO: 是否需要上传空的log文件 ???
//			uploadCacheFile(uri, logfile);
			
			log.info("3----------------上传文件成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

		}

		//TODO:启动redis实例，为什么要放在addSlaveConfig方法里面，比较晦涩，建议移除单独定义此方法，并在需要处调用。
		startMcsIns(
				McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
						+ mcsResourcePool.getAgentCmd(),
				mcsResourcePool.getCachePath(), redisPort);
		log.info("3----------------启动redis成功!");

	}

	//TODO: 同 addSlaveConfig 一样，需要重构，主要解决 agent 调用的逻辑，同时整理并重构逻辑。
	private void configSenSlave(String result, String masterResult, String capacity, String requirepass) throws PaasException {
		// 调用restful服务
		String memorySize = capacity + "m";
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];

		String[] masterInfo = masterResult.split(":");
		String masterIp = masterInfo[0];
		String masterPort = masterInfo[1];

		String fileName = "redis-" + port + ".conf";
		String uriCmdCluster = McsConstants.AGENT_URL_BASE + ip + agentCmd;
		String uriCreateCluster = McsConstants.AGENT_URL_BASE + ip + agentFile;
		String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH
				+ ",cmd=mkdir -p " + port;

//		String fileDetail = fileName + "||CACHE|" + cachePath
//				+ McsConstants.FILE_PATH + port + "/" + "||include "
//				+ cachePath + McsConstants.FILE_PATH + "redis-common.conf||"
//				+ "pidfile /var/run/redis-" + port + ".pid||port " + port
//				+ "||maxmemory " + memorySize + "||requirepass " + requirepass
//				+ "||" + "logfile " + cachePath + "/log/" + port + "/redis-"
//				+ port + ".log" + "||slaveof " + masterIp + " " + masterPort
//				+ "||masterauth " + requirepass;

		String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p "
				+ McsConstants.LOG_PATH;

//		String logName = "redis-" + port + ".log";
//		String logfile = logName + "||CACHE|" + cachePath
//				+ McsConstants.LOG_PATH + port + "/" + "||";
		
		try {
			executeInstruction(uriCmdCluster, mkdircmd);
			executeInstruction(uriCmdCluster, logdir);

			String configDetail = "||include "
					+ cachePath + McsConstants.FILE_PATH + "redis-common.conf"
					+ "||pidfile /var/run/redis-" + port + ".pid"
					+ "||port " + port
					+ "||maxmemory " + memorySize
					+ "||requirepass " + requirepass
					+ "||logfile " + cachePath + "/log/" + port + "/redis-" + port + ".log"
					+ "||slaveof " + masterIp + " " + masterPort
					+ "||masterauth " + requirepass;
			
			//TODO: 可以放到最上面，共用此对象。
			AgentClient ac = new AgentClient("10.1.228.199", 60004);
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			
//			uploadCacheFile(uriCreateCluster, fileDetail);
//			uploadCacheFile(uriCreateCluster, logfile);
			log.info("3----------------上传文件成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

		}
		
		//TODO: 启动 redis，单独定义方法，并在外围逻辑中调用，不与 configSenSlave 混合在一起！！
		startMcsIns(uriCmdCluster, cachePath, new Integer(port));
		log.info("3----------------启动redis成功!");
	}

	//TODO: 同configSenSlave方法，需要重构。（agent、逻辑梳理）
	private void configSentinel(String result, String master)
			throws PaasException {
		// 获取当前节点的ip和端口
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];

		// 获取监控节点的ip和端口
		String[] masterInfo = master.split(":");
		String masterIp = masterInfo[0];
		String masterPort = masterInfo[1];

		String uriCmdCluster = McsConstants.AGENT_URL_BASE + ip + agentCmd;
		String uriCreateCluster = McsConstants.AGENT_URL_BASE + ip + agentFile;
		String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + port;
		String logdir = "CMD| path=" + cachePath + McsConstants.LOG_PATH + ",cmd=touch  redis-" + port + ".log";
		
		String fileName = cachePath + McsConstants.FILE_PATH + port + "/"+"redis-" + port + "-sentinel.conf";
//		String fileDetail = fileName + "||CACHE|"
//				+ cachePath + McsConstants.FILE_PATH + port + "/"
//				+ "||port  " + port
//				+ "||sentinel monitor mymaster " + masterIp + " " + masterPort + " 2"
//				+ "||sentinel down-after-milliseconds mymaster 5000"
//				+ "||sentinel failover-timeout mymaster 60000"
//				+ "||sentinel parallel-syncs mymaster 1";

//		String logName = "redis-" + port + ".log";
//		String logfile = logName + "||CACHE|" + cachePath
//				+ McsConstants.LOG_PATH + port + "/" + "||";
		try {
			executeInstruction(uriCmdCluster, mkdircmd);
			executeInstruction(uriCmdCluster, logdir);
			
			String configDetail = "||port  " + port
					+ "||sentinel monitor mymaster " + masterIp + " " + masterPort + " 2"
					+ "||sentinel down-after-milliseconds mymaster 5000"
					+ "||sentinel failover-timeout mymaster 60000"
					+ "||sentinel parallel-syncs mymaster 1";
			//TODO: 可以放到最上面，共用此对象。
			AgentClient ac = new AgentClient("10.1.228.199", 60004);
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			
//			uploadCacheFile(uriCreateCluster, fileDetail);
			
			//TODO:是否需要增加空的日志文件 ？？？？
//			uploadCacheFile(uriCreateCluster, logfile);
			log.info("3----------------上传文件成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

		}
		
		//TODO: 重新定义redis启动方法，并放到外围逻辑进行调用。
		startSenIns(uriCmdCluster, cachePath, new Integer(port));
		log.info("3----------------启动redis成功!");
	}

	/**
	 * 创建存放配置文件的文件夹
	 * @param ac
	 * @param path
	 * @param redisPort
	 * @throws PaasException
	 */
	private void addConfigFolder(AgentClient ac, String path, int redisPort) throws PaasException {
		String cmd = "mkdir " + path + "/" + redisPort;
		ac.executeInstruction(cmd);
	}
	
	/**
	 * 上传配置文件，执行命令
	 * 
	 * @param mcsResourcePool
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addMcsConfig(AgentClient ac, int redisPort, String capacity, String requirepass) throws PaasException {
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + redisPort + "/" + "redis-" + redisPort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + redisPort + ".pid" + "\n"
					+ "port " + redisPort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + redisPort + ".log";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			log.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 允许修改容量、描述，serviceId userId capacity 都不能为空
	 * @throws PaasException
	 */
	//TODO:此方法与上面的open方法类似，统一考虑重构。
	@Override
	public String modifyMcs(String param) throws PaasException {
		/** 获取服务号配置参数  **/
		Map<String, String> map = McsParamUtil.getParamMap(param);
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		String serviceName = map.get(McsConstants.SERVICE_NAME);
		final String userId = map.get(McsConstants.USER_ID);
		String capacity = map.get(McsConstants.CAPACITY);
		
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(capacity, "capacity为空");
		
		log.info("M1----------------获得已申请过的记录");
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (cis == null || cis.isEmpty()) {
			throw new PaasException("该服务未开通过，无法修改！");
		}
		
		log.info("M2----------------修改mcs_resource");  //TODO: 此处可考虑重构为一个方法。
		final int cacheSize = cis.size() == 1 ? Integer.valueOf(capacity)
				: Math.round(Integer.valueOf(capacity) / McsConstants.CACHE_NUM * 2);
		modifyMcsResource(cis, cacheSize);
		
		log.info("M3----------------修改mcs服务端，重启redis");
		modifyMcsServerFileAndUserIns(userId, serviceId, cis, cacheSize, serviceName);
		
		
		
		log.info("----------------修改成功");
		
		return McsConstants.SUCCESS_FLAG;
	}

	
	
	
	/**
	 * 停redis，修改mcs服务端的配置文件，修改用户实例，启动redis
	 * 
	 * @param userInstanceList
	 * @param cacheSize
	 * @throws PaasException
	 */
	//TODO: 方法过大，梳理逻辑并重构；主要修改 agent 调用的部分。与 removeMcsServerFileAndUserIns() 方法类似，一起考虑重构。
	private void modifyMcsServerFileAndUserIns(final String userId,
			final String serviceId, List<McsUserCacheInstance> userInstanceList,
			final int cacheSize, String serviceName) throws PaasException {
		
		if (userInstanceList.size() == 1) {
			McsUserCacheInstance tempIns = userInstanceList.get(0);
			McsResourcePoolMapper mapper = ServiceUtil.getMapper(McsResourcePoolMapper.class);
			McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
			rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
			
			List<McsResourcePool> pools = mapper.selectByExample(rpmc);
			McsResourcePool pool = pools.get(0);

			String cachePath = pool.getCachePath();
			String cacheHostIp = pool.getCacheHostIp();
			Integer agentPort = Integer.parseInt(pool.getAgentCmd());
			Integer cachePort = tempIns.getCachePort();
			String requirepass = tempIns.getPwd();
			String commonconfigPath = cachePath + McsConstants.FILE_PATH;
			
			log.info("------------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
			log.info("------------- redis info cachePort:["+cachePort+"]-------------");
			log.info("------------- redis info requirepass:["+requirepass+"]-------------");
			log.info("------------- redis info agentPort:["+agentPort+"]-------------");
			log.info("------------- redis info commonconfigPath:["+commonconfigPath+"]-------------");
			
			/** 1.获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 2.停redis  **/
			log.info("----- stop redis -----");
			stopMcsIns(ac, cachePort);
			
			/** 3.修改redis的配置文件  **/
			log.info("----- modify redis config -----requirepass：" + requirepass);
			addMcsConfig(ac, cachePort, cacheSize + "", requirepass);

			/** 4.启动redis **/
			log.info("----- start redis -----");
			startMcsIns(ac, commonconfigPath, cachePort);
			
			/** 5.组织待更新的数据 **/
			log.info("------- cacheSize："+cacheSize);
			tempIns.setCacheMemory(cacheSize);
			if (serviceName != null && serviceName.length() > 0){
				tempIns.setServiceName(serviceName);
			}
			
			/** 6.更新 用户实例表，使用内存  **/
			log.info("----- update cache size info -------");
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(tempIns);
			
		} else {  /** 处理集群模式的redis **/
			McsResourcePool pool = null;
			final List<String> resultList = new ArrayList<String>();
			for(McsUserCacheInstance tempIns : userInstanceList){
				Integer agentPort = null;
				String cachePath = "";
				String cacheHostIp = tempIns.getCacheHost();
				Integer cachePort = tempIns.getCachePort();
				
				/** 对于某个serviceID，一个资源主机IP，可以对应2条用户实例纪录；通过IP+状态获取资源表的信息。**/
				if (pool == null) {
					McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
					McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
					rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
					List<McsResourcePool> pools = rpm.selectByExample(rpmc);
					
					pool = pools.get(0);
					cachePath = pool.getCachePath();
					agentPort = Integer.parseInt(pool.getAgentCmd());
				}
				
				log.info("------------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
				log.info("------------- redis info cachePort:["+cachePort+"]-------------");
				log.info("------------- redis info agentPort:["+agentPort+"]-------------");
				log.info("------------- redis info cachePath:["+cachePath+"]-------------");
				
				/** 获取agent客户端 **/
				AgentClient ac = new AgentClient(cacheHostIp, agentPort);
				
				/** 停redis  **/
				log.info("----- stop cluster redis -----");
				stopMcsIns(ac, cachePort);
				
				/** 将处理的Ip＋port，放入list **/
				log.info("-------add ["+cacheHostIp+"]:["+cachePort+"] into resultlist ------");
				resultList.add(cacheHostIp + ":" + cachePort);
				
				/** 组织待更新的数据,并更新用户实例表 **/
				tempIns.setCacheMemory(cacheSize);
				if (serviceName != null && serviceName.length() > 0) {
					tempIns.setServiceName(serviceName);
				}
				McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
				log.info("----- update cache size info -------");
				im.updateByPrimaryKey(tempIns);
			}
			
			//TODO:可抽象出单独方法，然后再在主逻辑中进行调用，梳理清楚业务逻辑。
			// 处理mcs服务端文件等
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			executorService.execute(new Runnable() {
				public void run() {
					try {
						log.info("M3.3-----cluster-----------修改文件，重启redis");
						addMcsConfigCluster(resultList, userId, serviceId, cacheSize);
						
						//TODO: 单独显示的调用重启redis方法。
						
					} catch (Exception e) {
						log.error(e.getMessage(), e);
					}
				}
			});
		}
	}

	/**
	 * @param uri rest地址
	 * @param cPath redis用户路径
	 * @param port redis的port
	 * @throws PaasException
	 */
	/** TODO: need modify **/
	private void stopMcsIns(String uri, String cPath, int port)
			throws PaasException {
		try {
			String cmd_stop = "CMD| path=" + cPath + ",cmd=ps -ef | grep " + port + " | awk '{print $2}' | xargs kill -9";
			// String
			// cmd_stop="CMD| path="+cPath+",cmd=./redis-cli -p "+port+" shutdown";
			executeInstruction(uri, cmd_stop);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("停止MCS异常，port：" + port, e);
		}
	}

	/**
	 * 使用新的agent接口，停redis服务。
	 * @param ac
	 * @param port
	 * @throws PaasException
	 */
	private void stopMcsIns(AgentClient ac, int port) throws PaasException {
		try {
			String cmd =  "ps -ef | grep " + port + " | awk '{print $2}' | xargs kill -9";
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("++++++++ 停止Redis异常，port：" + port, e);
		}
	}
	
	/**
	 * 修改缓存资源池的使用内存量
	 * 
	 * @param cis
	 * @param cacheSize
	 */
	private void modifyMcsResource(List<McsUserCacheInstance> cis, int cacheSize) {
		int oldSize = cis.get(0).getCacheMemory();
		Map<String, Integer> hostM = new HashMap<String, Integer>();
		for (int i = 0; i < cis.size(); i++) {
			McsUserCacheInstance tempIns = cis.get(i);
			if (hostM.containsKey(tempIns.getCacheHost())) {
				hostM.put(tempIns.getCacheHost(), hostM.get(tempIns.getCacheHost()) + cacheSize - oldSize);
			} else {
				hostM.put(tempIns.getCacheHost(), cacheSize - oldSize);
			}
		}
		
		if (!hostM.isEmpty()) {
			for (String key : hostM.keySet()) {
				McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(key);
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				McsResourcePool pool = pools.get(0);
				
				/** 更新CacheMemoryUsed字段  **/
				pool.setCacheMemoryUsed(pool.getCacheMemoryUsed() + hostM.get(key));
				rpm.updateByExampleSelective(pool, rpmc);
			}
		}
	}

	/**
	 * 获得最空闲的资源
	 * 
	 * @param num
	 * @return
	 */
	private List<McsResourcePool> getBestResource(int num) {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
		rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS);
		rpmc.setLimitStart(0);
		rpmc.setLimitEnd(num);
		rpmc.setOrderByClause("(ifnull(cache_memory, 0) - ifnull(cache_memory_used, 0)) desc");
		return rpm.selectByExample(rpmc);
	}

	/**
	 * 获得UseInstance中 失效的记录
	 * 
	 * @param host
	 * @return
	 */
	private McsUserCacheInstance getCanUseInstance(String host) throws PaasException {
		McsUserCacheInstanceCriteria imc = new McsUserCacheInstanceCriteria();
		imc.createCriteria().andStatusNotEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostEqualTo(host);
		imc.setLimitStart(0);
		imc.setLimitEnd(1);
		
		McsUserCacheInstance bean = null;
		McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
		List<McsUserCacheInstance> list = im.selectByExample(imc);
		if (list != null && list.size() > 0) {
			bean = list.get(0);
		}
		
		return bean;
	}

	/**
	 * 更新资源
	 * 
	 * @param mcsResourcePool
	 * @return
	 */
	private int updateResource(McsResourcePool mcsResourcePool) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
		condition.createCriteria().andIdEqualTo(mcsResourcePool.getId()).andCachePortEqualTo(mcsResourcePool.getCachePort() - 1);
		return rpm.updateByExampleSelective(mcsResourcePool, condition);
	}

	/**
	 * 上传cache文件到服务器
	 * 
	 * @param uri
	 * @param dstFileDir
	 * @throws PaasException
	 */
	//TODO:需重构，直接使用AgentClint类，则不用再使用此方法。
//	private void uploadCacheFile(String uri, String dstFileDir) throws PaasException {
//		try {
//			ClientResource clientResource = new ClientResource(uri);
//			clientResource.release();
//			Thread.sleep(10);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//			throw new PaasException("", e);
//		} catch (Error e) {
//			log.error(e.getMessage(), e);
//		}
//	}
	
	/**
	 * 执行服务器端命令
	 * 
	 * @param uri
	 * @param cmd
	 * @return
	 * @throws PaasException
	 */
	//TODO: 重新构造此方法，使用新的AgentClint类及其方法。
	private String executeInstruction(String uri, String cmd) throws PaasException {
		String result = null;
		try {
			ClientResource clientResource = new ClientResource(uri);
			Representation representation = clientResource.post(cmd);
			result = representation.getText();
			clientResource.release();
			Thread.sleep(10);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("", e);
		}
		return result;
	}

	@Override
	public String stopMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		if (!McsConstants.APPLY_TYPE_STOP.equals(map.get(McsConstants.APPLY_TYPE))) {
			throw new PaasException("停止缓存服务，服务类型不对！");
		}
		
		// 获取服务号配置参数
		// TODO: 局域变量，为什么要定义成 finnal ？？？
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (cis == null || cis.isEmpty()) {
			throw new PaasException("该服务未开通过，无法停止！");
		}
		
		stopMcsInsForCacheIns(cis);
		log.info("----------------停止缓存服务,成功");
		if (cis.size() > 1) {
			// TODO 验证集群的是否能停掉
		}
		
		return McsConstants.SUCCESS_FLAG;
	}

	//TODO: 需要重构，逻辑不够清晰。
	private void stopMcsInsForCacheIns(List<McsUserCacheInstance> cis) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePool pool = null;
		for (int i = 0; i < cis.size(); i++) {
			if (pool == null) {
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria()
						.andStatusEqualTo(McsConstants.VALIDATE_STATUS)
						.andCacheHostIpEqualTo(cis.get(i).getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
			}
			
			stopMcsIns(McsConstants.AGENT_URL_BASE + cis.get(i).getCacheHost()
					+ pool.getAgentCmd(), pool.getCachePath(), cis.get(i).getCachePort());
		}
	}

	@Override
	public String startMcs(String param) throws PaasException {
		//TODO: 对于apply_type 的判断，可以放到最外层逻辑；或者，如果此参数为固定值，可去掉此处的校验。
		Map<String, String> map = McsParamUtil.getParamMap(param);
		if (!McsConstants.APPLY_TYPE_START.equals(map.get(McsConstants.APPLY_TYPE))) {
			throw new PaasException("启动缓存服务，服务类型不对！");
		}
		
		// 获取服务号配置参数
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(
				serviceId, userId);
		if (cis == null || cis.isEmpty())
			throw new PaasException("该服务未开通过，无法启动！");
		startMcs(userId, serviceId, cis);
		return McsConstants.SUCCESS_FLAG;
	}

	private void startMcs(String userId, String serviceId,
			List<McsUserCacheInstance> cis) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil
				.getMapper(McsResourcePoolMapper.class);
		McsResourcePool pool = null;
		String ipPorts = "";
		for (int i = 0; i < cis.size(); i++) {
			if (pool == null) {
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria()
						.andStatusEqualTo(McsConstants.VALIDATE_STATUS)
						.andCacheHostIpEqualTo(cis.get(i).getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
				cachePath = pool.getCachePath();
			}
			ipPorts += " " + cis.get(i).getCacheHost() + ":"
					+ cis.get(i).getCachePort();
			if (cis.size() == 1)
				startMcsIns(McsConstants.AGENT_URL_BASE
						+ cis.get(i).getCacheHost() + pool.getAgentCmd(),
						pool.getCachePath(), cis.get(i).getCachePort());
			else
				startMcsInsForCluster(McsConstants.AGENT_URL_BASE
						+ cis.get(i).getCacheHost() + pool.getAgentCmd(),
						userId + "_" + serviceId, pool.getCachePath(),
						cis.get(i).getCachePort());
		}
		if (cis.size() > 1) {
			String listCmdCluster = "CMD| path=" + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + userId + "_" + serviceId
					+ ",cmd=redis-trib.rb create --replicas 1" + ipPorts;
			try {
				executeInstruction(McsConstants.AGENT_URL_BASE
						+ cis.get(0).getCacheHost() + pool.getAgentCmd(),
						listCmdCluster);
				log.info("集群启动成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new PaasException("集群启动失败：" + e.getMessage(), e);
			}
		}
	}

	private void startSenIns(String uri, String cPath, int port)
			throws PaasException {
		try {
			String cmd = "CMD| path=" + cPath + McsConstants.FILE_PATH + port
					+ "/,cmd=redis-sentinel " + cPath + McsConstants.FILE_PATH
					+ port + "/redis-" + port + "-sentinel.conf &";
			executeInstruction(uri, cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("启动MCS异常，port：" + port, e);
		}
	}

	/**
	 * 使用新的agent接口，sentnel模式启动redis服务。
	 * @param ac
	 * @param path
	 * @param port
	 * @throws PaasException
	 */
	private void startSenIns(AgentClient ac, String path, int port) throws PaasException {
		try {
			String cmd =  "redis-sentinel " + path + port + "/redis-" + port + ".conf &";
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("sentinel模式启动MCS异常，port：" + port, e);
		}
	}
	
	private void startMcsIns(String uri, String cPath, int port)
			throws PaasException {
		try {
			String cmd = "CMD| path=" + cPath + McsConstants.FILE_PATH + port
					+ "/,cmd=redis-server " + cPath + McsConstants.FILE_PATH
					+ port + "/redis-" + port + ".conf &";

			executeInstruction(uri, cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("启动MCS异常，port：" + port, e);
		}
	}
	
	/**
	 * 使用新的agent接口，启动redis服务。
	 * @param ac
	 * @param path
	 * @param port
	 * @throws PaasException
	 */
	private void startMcsIns(AgentClient ac, String path, int port) throws PaasException {
		try {
			String cmd =  "redis-server " + path + port + "/redis-" + port + ".conf &";
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("启动MCS异常，port：" + port, e);
		}
	}

	private void startMcsInsForCluster(String uri, String dir, String cPath,
			int port) throws PaasException {
		try {
			String cmd_rm = "CMD| path=" + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+ ",cmd=rm -fr appendonly.aof  dump.rdb  nodes.conf ";
			String cmd = "CMD| path=" + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+ ",cmd=redis-server " + cachePath
					+ McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+ "/redis-" + port + ".conf > redis.log &";
			executeInstruction(uri, cmd_rm);
			executeInstruction(uri, cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("启动MCS异常，port：" + port, e);
		}
	}

	@Override
	public String restartMcs(String param) throws PaasException {
		//TODO: 对于apply_type 的判断，可以放到最外层逻辑；或者，如果此参数为固定值，可去掉此处的校验。
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String applyType = map.get(McsConstants.APPLY_TYPE);
		if (!McsConstants.APPLY_TYPE_RESTART.equals(applyType))
			throw new PaasException("重启缓存服务，服务类型不对！");
		
		// 获取服务号配置参数
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(
				serviceId, userId);
		if (cis == null || cis.isEmpty())
			throw new PaasException("该服务未开通过，无法重启！");
		stopMcsInsForCacheIns(cis);
		startMcs(userId, serviceId, cis);
		return McsConstants.SUCCESS_FLAG;
	}

	@Override
	public String cancelMcs(String param) throws PaasException {
		//TODO: 对于apply_type 的判断，可以放到最外层逻辑；或者，如果此参数为固定值，可去掉此处的校验。
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String applyType = map.get(McsConstants.APPLY_TYPE);
		if (!McsConstants.APPLY_TYPE_R.equals(applyType))
			throw new PaasException("注销缓存服务，服务类型不对！");
		// 获取服务号配置参数
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		log.info("注销----------------获得已申请过的记录");
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(
				serviceId, userId);
		if (cis == null || cis.isEmpty())
			throw new PaasException("该服务未开通过，无法注销！");

		log.info("注销----------------修改mcs_resource");
		modifyMcsResource(cis, 0);
		log.info("注销----------------修改mcs服务端，重启redis");
		removeMcsServerFileAndUserIns(userId, serviceId, cis);
		log.info("----------------注销成功");
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 停redis，删除mcs服务端的配置文件，修改用户实例为失效
	 * 
	 * @param userInstanceList
	 * @param cacheSize
	 * @throws PaasException
	 */
	private void removeMcsServerFileAndUserIns(final String userId, final String serviceId, 
			List<McsUserCacheInstance> userInstanceList) throws PaasException {
		if (userInstanceList.size() == 1) {
			log.info("-------- removeMcsServerFileAndUserIns ---single-----");
			McsUserCacheInstance tempIns = userInstanceList.get(0);
			McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
			McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
			rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
			List<McsResourcePool> pools = rpm.selectByExample(rpmc);
			McsResourcePool pool = pools.get(0);

			String cachePath = pool.getCachePath();
			String cacheHostIp = pool.getCacheHostIp();
			Integer agentPort = Integer.parseInt(pool.getAgentCmd());
			Integer cachePort = tempIns.getCachePort();
			String requirepass = tempIns.getPwd();
			String commonconfigPath = cachePath + McsConstants.FILE_PATH;
			log.info("------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
			log.info("------- redis info cachePort:["+cachePort+"]-------------");
			log.info("------- redis info requirepass:["+requirepass+"]-------------");
			log.info("------- redis info agentPort:["+agentPort+"]-------------");
			log.info("------- redis info commonconfigPath:["+commonconfigPath+"]-------------");
			
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 停redis  **/
			log.info("----- stop redis -----");
			stopMcsIns(ac, cachePort);
			
			/** 删除redis的配置文件  **/
			log.info("----- delete redis config file-----");
			removeMcsConfig(ac, commonconfigPath, agentPort);
			
			/** 删除zk的配置**/
			log.info("-------delete config for ["+userId+"].["+serviceId+"] ---------");
			CCSComponentOperationParam op = new CCSComponentOperationParam();
			op.setUserId(userId);
			op.setPath("/MCS/" + serviceId);
			op.setPathType(PathType.READONLY);
			iCCSComponentManageSv.delete(op);
			log.info("------delete ["+userId+"].["+serviceId+"]'s config successful! --------");
			
			/** 更新用户实例表，状态为2. **/
			log.info("------- update mcs_user_instance status is ["+McsConstants.INVALIDATE_STATUS+"]");
			tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(tempIns);

		} else {
			log.info("-------- removeMcsServerFileAndUserIns ---cluster-----");
			McsResourcePool pool = null;
			for (McsUserCacheInstance tempIns :userInstanceList) {
				if (pool == null) {
					McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
					McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
					rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
					List<McsResourcePool> pools = rpm.selectByExample(rpmc);
					pool = pools.get(0);
				}
				
				String cachePath = pool.getCachePath();
				String cacheHostIp = pool.getCacheHostIp();
				Integer agentPort = Integer.parseInt(pool.getAgentCmd());
				Integer cachePort = tempIns.getCachePort();
				String commonconfigPath = cachePath + McsConstants.FILE_PATH;
				log.info("------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
				log.info("------- redis info cachePort:["+cachePort+"]-------------");
				log.info("------- redis info agentPort:["+agentPort+"]-------------");
				log.info("------- redis info commonconfigPath:["+commonconfigPath+"]-------------");
				
				/** 获取agent客户端 **/
				AgentClient ac = new AgentClient(cacheHostIp, agentPort);
				
				/** 停redis  **/
				log.info("----- stop redis -----");
				stopMcsIns(ac, cachePort);
				
				/** 删除redis的配置文件  **/
				log.info("----- delete redis[cluster] config file-----");
				removeMcsConfig(ac, commonconfigPath, agentPort, userId, serviceId);
				
				/** 删除zk的配置 **/
				log.info("------delete zk config for ["+userId+"].["+serviceId+"] ---cluster-----");
				CCSComponentOperationParam op = new CCSComponentOperationParam();
				op.setUserId(userId);
				op.setPath("/MCS/" + serviceId);
				op.setPathType(PathType.READONLY);
				iCCSComponentManageSv.delete(op);
				log.info("------delete zk info ["+userId+"].["+serviceId+"]'s config successful! ------");

				/** 更新用户实例表，状态为2. **/
				tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
				McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
				im.updateByPrimaryKey(tempIns);
				log.info("------update mcs_user_instance status is ["+McsConstants.INVALIDATE_STATUS+"]");
			}
		}
	}

	/**
	 * 删除 单例的redis配置文件
	 * @param ac
	 * @param cachePath
	 * @param cachePort
	 * @throws PaasException
	 */
	private void removeMcsConfig(AgentClient ac, String commonconfigPath, Integer cachePort) throws PaasException {
		String file = commonconfigPath + "redis-" + cachePort + ".conf";
		try {
			String cmd = "rm " + file;
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("删除redis的配置文件异常，file:" + file, e);
		}
	}
	
	/**
	 * 删除 集群的redis配置文件
	 * @param ac
	 * @param commonconfigPath
	 * @param cachePort
	 * @param userId
	 * @param serviceId
	 * @throws PaasException
	 */
	private void removeMcsConfig(AgentClient ac, String commonconfigPath, Integer cachePort, 
			String userId, String serviceId) throws PaasException {
		String file = commonconfigPath + userId + "_" + serviceId + "/redis-" + cachePort + ".conf";
		try {
			String cmd = "rm " + file;
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("集群--删除redis的配置文件异常，file：" + file, e);
		}
	}

}
