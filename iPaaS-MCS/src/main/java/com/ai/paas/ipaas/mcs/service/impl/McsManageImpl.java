package com.ai.paas.ipaas.mcs.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.agent.util.AgentUtil;
import com.ai.paas.ipaas.agent.util.AidUtil;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasImageResourceMapper;
import com.ai.paas.ipaas.base.dao.interfaces.IpaasSysConfigMapper;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResource;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResourceCriteria;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfig;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasSysConfigCriteria;
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
import com.ai.paas.ipaas.mcs.service.util.McsProcessInfo;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.DateTimeUtil;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class McsManageImpl implements IMcsSv {
	private static transient final Logger logger = LoggerFactory.getLogger(McsManageImpl.class);

	@Autowired 
	private McsSvHepler mcsSvHepler;
	
	@Autowired 
	private ICCSComponentManageSv iCCSComponentManageSv;
	
	//protected static boolean excuteFlag = true;
	
	@Override
	public String openMcs(String param) throws PaasException {
		Map<String, String> paraMap = McsParamUtil.getParamMap(param);
		String userId = paraMap.get(McsConstants.USER_ID);
		String serviceId = paraMap.get(McsConstants.SERVICE_ID);
		String haMode = paraMap.get(McsConstants.HA_MODE);
 
		if (existsService(userId, serviceId)) {
			logger.info("用户服务已存在，开通成功.");
			return McsConstants.SUCCESS_FLAG;
		}
		
		switch(haMode) {
		case McsConstants.MODE_SINGLE:
			logger.info("---- 开通单节点的MCS服务 ----");
			openSingleMcs(paraMap);
			break;
		case McsConstants.MODE_CLUSTER:
			logger.info("---- 开通集群模式的MCS服务 ----");
			openClusterMcs(paraMap);
			break;
		case McsConstants.MODE_REPLICATION:
			logger.info("---- 开通主从模式的MCS服务 ----");
			openReplicationMcs(paraMap);
			break;
		case McsConstants.MODE_SENTINEL:
			logger.info("---- 开通sentinel模式的MCS服务 ----");
			openSentinelMcs(paraMap);
			break;
		}
		
		return McsConstants.SUCCESS_FLAG;
	}
	
	/**
	 * 开通单例模式的Mcs服务。
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	private String openSingleMcs(Map<String, String> paraMap) throws PaasException {
		String userId = paraMap.get(McsConstants.USER_ID);
		String serviceId = paraMap.get(McsConstants.SERVICE_ID);
		String serviceName = paraMap.get(McsConstants.SERVICE_NAME);
		String capacity = paraMap.get(McsConstants.CAPACITY);
		Integer cacheSize = Integer.parseInt(capacity);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		/** 1.获取mcs资源. **/
		McsResourcePool mcsResourcePool = selectMcsResSingle(cacheSize, 1);
		String hostIp = mcsResourcePool.getCacheHostIp();
		Integer cachePort = mcsResourcePool.getCachePort();
		String requirepass = mcsSvHepler.getRandomKey();

		/** 2.获取执行ansible命令所需要的主机信息，以及docker镜像信息. **/
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		IpaasImageResource redisImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_IMAGE_CODE);
		String image = redisImage.getImageRepository() + "/" + redisImage.getImageName();
		
		/** 3.容器名称：userId_serviceId_port **/
		String containerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + cachePort;
				
		/** 4.创建 mcs_host.cfg 文件，并写入hostIp. **/
		createHostCfg(basePath);
		writeHostCfg(basePath, hostIp);
		logger.info("-----创建 mcs_host.cfg 成功！");

		/** 5.上传 ansible的 playbook 文件. **/
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SINGLE_YML);
		logger.info("-----上传 mcs_single.yml 成功！");

		/** 6.生成ansible-playbook命令,并执行. **/
		String ansibleCommand = getRedisServerCommand(capacity, basePath, hostIp, cachePort, 
				requirepass, McsConstants.MODE_SINGLE, sshUser, sshUserPwd, containerName, image);
		runAnsileCommand(ansibleCommand);
		logger.info("-----执行ansible-playbook 成功！");

		try {
			/** 7.处理zk配置. **/
			List<String> hostList = new ArrayList<String>();
			hostList.add(hostIp + ":" + cachePort);
			addCcsConfig(userId, serviceId, hostList, requirepass);
			logger.info("----------处理zk 配置成功！");
	
			/** 8.添加mcs用户实例信息. **/
			addUserInstance(userId, serviceId, capacity, hostIp, cachePort, requirepass, serviceName, containerName, image);
			logger.info("---------记录用户实例成功！");
		} catch (Exception ex) {
			logger.error("==== open cluster error, msg:" + ex.getMessage());
			/** 如果在启动docker成功后出现异常，需要stop并rm，所启动的docker容器。 **/
			writeHostCfg(basePath, hostIp);
			releaseDocker(hostIp, containerName, McsConstants.DOCKER_COMMAND_STOP);
			releaseDocker(hostIp, containerName, McsConstants.DOCKER_COMMAND_REMOVE);
			throw new PaasException("==== open single MCS error ====");
		}
		
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 开通集群模式的Mcs服务。
	 * @param paraMap
	 * @return
	 * @throws Exception
	 */
	private String openClusterMcs(Map<String, String> paraMap) throws PaasException {
		String userId = paraMap.get(McsConstants.USER_ID);
		String serviceId = paraMap.get(McsConstants.SERVICE_ID);
		String serviceName = paraMap.get(McsConstants.SERVICE_NAME);
		String capacity = paraMap.get(McsConstants.CAPACITY);
		Integer cacheSize = Integer.parseInt(capacity);
		final int clusterCacheSize = Math.round((cacheSize * 2) / McsConstants.CLUSTER_CACHE_NUM);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		/** 获取执行ansible命令所需要的主机信息，以及docker镜像信息. **/
		final String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		final String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		final IpaasImageResource redisImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_IMAGE_CODE);
		final String image = redisImage.getImageRepository() + "/" + redisImage.getImageName();
		IpaasImageResource redisClusterImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_CLUSTER_IMAGE_CODE);
		
		/** 上传 ansible的 playbook 文件:mcs_single.yml, mcs_cluster.yml **/
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SINGLE_YML);
		logger.info("-----上传 mcs_single.yml 成功！");
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_CLUSTER_YML);
		logger.info("-----上传 mcs_cluster.yml 成功！");

		/** 创建空的执行ansible-playbook所用host.cfg文件 **/
		createHostCfg(basePath);
		logger.info("-----创建 mcs_host.cfg 成功！");
		
		/** 从MCS资源池中选取资源  **/
		List<McsProcessInfo> cacheInfoList = selectMcsResCluster(userId, serviceId, clusterCacheSize, McsConstants.CLUSTER_CACHE_NUM);
		logger.info("-----已获取开通redis集群所需资源主机["+cacheInfoList.size() +"]台。");
		
		/** 循环处理redis-server节点。 **/
		String hostIp = "";
		for (McsProcessInfo proInfo : cacheInfoList) {
			hostIp = proInfo.getCacheHostIp();
			Integer cachePort = proInfo.getCachePort();
			String containerName = proInfo.getContainerName();
			String requirepass = mcsSvHepler.getRandomKey();
			
			String redisRun = getRedisServerCommand(clusterCacheSize+"", basePath, hostIp, cachePort, 
					requirepass, McsConstants.MODE_CLUSTER, sshUser, sshUserPwd, containerName, image);
			
			/** 将hostIp写入 mcs_host.cfg 文件. **/
			writeHostCfg(basePath, hostIp);
			logger.info("-----[" + hostIp + "]写入 mcs_host.cfg 成功！");
			
			/** 顺序执行启动 redis_server 的命令. **/
			runAnsileCommand(redisRun);
			logger.info("-----执行ansible-playbook 成功！");
			
//			/** 并发执行创建server的命令 **/
//			excuteRunRedis(redisRun);
//			if(!excuteFlag) {
//				throw new PaasException("开通集群模式MCS时，创建其中["+hostIp+":"+cachePort+"]的redis服务出现异常.");
//			}
		}

		try{
			/** 执行redis集群创建命令
			 *  不在重新写入 mcs_host.cfg 文件，直接在最后启动的redis-server主机上，run集群创建docker。 **/
			String clusterInfo = getClusterInfo(cacheInfoList, "|");
			String containerName = userId + "-" + serviceId + "-" + "-createRedisCluster";
			String redisClusterRun = getCreateClusterCommand(basePath, hostIp, sshUser, sshUserPwd, clusterInfo, containerName, redisClusterImage);
			runAnsileCommand(redisClusterRun);
			logger.info("-------- 开通MCS集群模式，执行集群创建命令成功！");
			
			/** 添加zk配置 **/
			addZKConfig(userId, serviceId, cacheInfoList);
			logger.info("-------- 开通MCS集群模式，处理zk 配置成功！");
			
			/** 记录用户的MCS开通实例信息 **/
			addMcsUserInstance(userId, serviceId, serviceName, clusterCacheSize, cacheInfoList, image);
			logger.info("-------- 开通MCS集群模式，记录用户的MCS开通实例信息成功！");
			
			/** 释放掉创建集群的docker容器 **/
			//releaseDocker(hostIp, containerName, McsConstants.DOCKER_COMMAND_REMOVE);
		} catch(Exception ex) {
			logger.error("==== open cluster error, msg:" + ex.getMessage());
			/** 如果在启动docker成功后出现异常，需要stop并rm，所有已启动的docker容器。 **/
			for (McsProcessInfo proInfo : cacheInfoList) {
				writeHostCfg(basePath, hostIp);
				releaseDocker(proInfo.getCacheHostIp(), proInfo.getContainerName(), McsConstants.DOCKER_COMMAND_STOP);
				releaseDocker(proInfo.getCacheHostIp(), proInfo.getContainerName(), McsConstants.DOCKER_COMMAND_REMOVE);
			}
			throw new PaasException("==== open cluster MCS error ====");
		}
		
		return McsConstants.SUCCESS_FLAG;
	}

//	private void excuteRunRedis(String redisRun) {
//		Thread runThread = new Thread(new Runnable() {
//			public void run() {
//				try {
//					runAnsileCommand(redisRun);
//					setExcuteFlag(true);
//				} catch (Exception ex) {
//					logger.error("------- excute ansible-playbook error -------");
//					setExcuteFlag(false);
//				}
//			}
//		});
//		
//		runThread.start();
//	}

//	private static synchronized void setExcuteFlag(boolean flag) {
//		excuteFlag = flag;
//	}
	
	/**
	 * 开通主备模式的Mcs服务。
	 * 在同一台资源主机上，先启动一个single模式的redis，再增加一个slave节点.
	 * slave节点的port值，是master节点port值－1.
	 * @param paraMap
	 * @return
	 * @throws PaasException
	 */
	private String openReplicationMcs(Map<String, String> paraMap) throws PaasException {
		String userId = paraMap.get(McsConstants.USER_ID);
		String serviceId = paraMap.get(McsConstants.SERVICE_ID);
		String serviceName = paraMap.get(McsConstants.SERVICE_NAME);
		String capacity = paraMap.get(McsConstants.CAPACITY);
		Integer cacheSize = Integer.parseInt(capacity);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		/** 1.获取mcs资源. **/
		McsResourcePool mcsResourcePool = selectMcsResSingle(cacheSize * 2, 2);
		String hostIp = mcsResourcePool.getCacheHostIp();
		Integer masterPort = mcsResourcePool.getCachePort();
		Integer slavePort = masterPort -1;
		String masterPwd = mcsSvHepler.getRandomKey();
		logger.info("-----所选资源主机[" + hostIp + ":" + masterPort + "], masterPwd:" + masterPwd);

		/** 2.获取执行ansible命令所需要的主机信息，以及docker镜像信息. **/
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		IpaasImageResource redisImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_IMAGE_CODE);
		String image = redisImage.getImageRepository() + "/" + redisImage.getImageName();
		
		/** 容器名称：userId_serviceId_port **/
		String masterContainerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + masterPort;
		String slaveContainerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + slavePort;
		
		/** 3.创建 mcs_host.cfg 文件，并写入hostIp. **/
		createHostCfg(basePath);
		writeHostCfg(basePath, hostIp);
		logger.info("-----创建 mcs_host.cfg 成功！");

		/** 4.上传 ansible的 playbook 文件. **/
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SINGLE_YML);
		logger.info("-----上传 mcs_single.yml 成功！");
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_REPLICATION_YML);
		logger.info("-----上传 mcs_replication.yml 成功！");

		/** 5.生成创建master节点的命令,并执行. **/
		String ansibleCommand = getRedisServerCommand(capacity, basePath, hostIp, masterPort, 
				masterPwd, McsConstants.MODE_SINGLE, sshUser, sshUserPwd, masterContainerName, image);
		runAnsileCommand(ansibleCommand);
		logger.info("-----执行ansible-playbook 成功！");
		
		/** 6.生成创建slave节点的命令,并执行. **/
		String slaveCommand = getRedisSlaveCommand(capacity, basePath, hostIp, slavePort, masterPwd,  
				McsConstants.MODE_REPLICATION, sshUser, sshUserPwd, hostIp, masterPort, slaveContainerName, redisImage);
		runAnsileCommand(slaveCommand);
		logger.info("-----执行slaveCommand 成功！");
		
		/** 7.处理zk配置. **/
		List<String> hostList = new ArrayList<String>();
		hostList.add(hostIp + ":" + masterPort);
		addCcsConfig(userId, serviceId, hostList, masterPwd);
		logger.info("----------处理zk 配置成功！");

		/** 8.添加mcs用户master实例信息. **/
		addUserInstance(userId, serviceId, capacity, hostIp, masterPort, masterPwd, serviceName, masterContainerName, image);
		
		/** 9.添加mcs用户slave实例信息. **/
		addUserInstance(userId, serviceId, capacity, hostIp, slavePort, masterPwd, serviceName, slaveContainerName, image);
		logger.info("---------记录用户实例成功！");
		
		return McsConstants.SUCCESS_FLAG;
	}
	
	private String openSentinelMcs(Map<String, String> paraMap) throws PaasException {
		String userId = paraMap.get(McsConstants.USER_ID);
		String serviceId = paraMap.get(McsConstants.SERVICE_ID);
		String serviceName = paraMap.get(McsConstants.SERVICE_NAME);
		String capacity = paraMap.get(McsConstants.CAPACITY);
		Integer cacheSize = Integer.parseInt(capacity);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		/** 1.获取mcs资源. **/
		McsResourcePool mcsResourcePool = selectMcsResSingle(cacheSize * 2, 3);
		String hostIp = mcsResourcePool.getCacheHostIp();
		Integer sentinelPort = mcsResourcePool.getCachePort();
		Integer slavePort = sentinelPort -1;
		Integer masterPort =sentinelPort -2;

		String masterPwd = mcsSvHepler.getRandomKey();
		logger.info("-----所选资源主机[" + hostIp + ":" + masterPort + "], masterPwd:" + masterPwd);

		/** 2.获取执行ansible命令所需要的主机信息，以及docker镜像信息. **/
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		IpaasImageResource redisImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_IMAGE_CODE);
		String image = redisImage.getImageRepository() + "/" + redisImage.getImageName();
		
		/** 容器名称：userId_serviceId_port **/
		String masterContainerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + masterPort;
		String slaveContainerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + slavePort;
		String sentinelContainerName = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + sentinelPort;
		
		/** 3.创建 mcs_host.cfg 文件，并写入hostIp. **/
		createHostCfg(basePath);
		writeHostCfg(basePath, hostIp);
		logger.info("-----创建 mcs_host.cfg 成功！");

		/** 4.上传 ansible的 playbook 文件. **/
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SINGLE_YML);
		logger.info("-----上传 mcs_single.yml 成功！");
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_REPLICATION_YML);
		logger.info("-----上传 mcs_replication.yml 成功！");
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SENTINEL_YML);
		logger.info("-----上传 mcs_sentinel.yml 成功！");

		/** 5.生成创建master节点的命令,并执行. **/
		String ansibleCommand = getRedisServerCommand(capacity, basePath, hostIp, masterPort, 
				masterPwd, McsConstants.MODE_SINGLE, sshUser, sshUserPwd, masterContainerName, image);
		runAnsileCommand(ansibleCommand);
		logger.info("-----执行ansible-playbook 成功！");
		
		/** 6.生成创建slave节点的命令,并执行. **/
		String slaveCommand = getRedisSlaveCommand(capacity, basePath, hostIp, slavePort, masterPwd,  
				McsConstants.MODE_REPLICATION, sshUser, sshUserPwd, hostIp, masterPort, slaveContainerName, redisImage);
		runAnsileCommand(slaveCommand);
		logger.info("-----执行slaveCommand 成功！");
		
		/** 7.生成创建sentinel节点的命令,并执行. **/
		String sentinelCommand = getRedisSentinelCommand(basePath, hostIp, slavePort, masterPwd,  
				McsConstants.MODE_SENTINEL, sshUser, sshUserPwd, hostIp, masterPort, sentinelContainerName, redisImage);
		runAnsileCommand(sentinelCommand);
		logger.info("-----执行sentinelCommand 成功！");
		
		/** 8.处理zk配置. **/
		List<String> hostList = new ArrayList<String>();
		hostList.add(hostIp + ":" + masterPort);
		addCcsConfig(userId, serviceId, hostList, masterPwd);
		logger.info("----------处理zk 配置成功！");

		/** 9.添加mcs用户master实例信息. **/
		addUserInstance(userId, serviceId, capacity, hostIp, masterPort, masterPwd, serviceName, masterContainerName, image);
		
		/** 10.添加mcs用户slave实例信息. **/
		addUserInstance(userId, serviceId, capacity, hostIp, slavePort, masterPwd, serviceName, slaveContainerName, image);
		logger.info("---------记录用户实例成功！");
		
		/** 11.添加mcs用户sentinel实例信息. **/
		addUserInstance(userId, serviceId, "0", hostIp, sentinelPort, masterPwd, serviceName, sentinelContainerName, image);
		logger.info("---------记录用户实例成功！");
		
		return McsConstants.SUCCESS_FLAG;
	}
	
	/**
	 * 门户管理控制台功能：启动MCS
	 */
	@Override
	public String startMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> userInstanceList = getMcsServiceInfo(serviceId, userId);
		
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_OPERATE_YML);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_START);
		
		/** 如果MCS是集群模式，需要运行集群创建的docker. **/
		if(userInstanceList.size() > 1) {
			createClusterDocker(userInstanceList);
		}
		
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 门户管理控制台功能：停止Mcs服务
	 */
	@Override
	public String stopMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> userInstanceList = getMcsServiceInfo(serviceId, userId);
		
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_OPERATE_YML);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_STOP);
		
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 门户管理控制台功能：重启MCS服务。
	 */
	@Override
	public String restartMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> userInstanceList = getMcsServiceInfo(serviceId, userId);
		
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_OPERATE_YML);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_STOP);

		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_START);
		
		/** 如果MCS是集群模式，需要运行集群创建的docker. **/
		if(userInstanceList.size() > 1) {
			createClusterDocker(userInstanceList);
		}
		
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 门户管理控制台功能：注销MCS服务
	 */
	@Override
	public String cancelMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String userId = map.get(McsConstants.USER_ID);
	
		List<McsUserCacheInstance> userInstanceList = getMcsServiceInfo(serviceId, userId);
		
		uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_OPERATE_YML);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_STOP);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_REMOVE);
		
		delZKConfig(userId, serviceId);
		
		updateUserInstance(userInstanceList, McsConstants.INVALIDATE_STATUS);
		
		modifyMcsResource(userInstanceList, true, 0);
		
		logger.info("-------- 注销成功 --------");
		
		return McsConstants.SUCCESS_FLAG;
	}

	/**
	 * 允许修改容量、描述，serviceId userId capacity 都不能为空
	 * @throws PaasException
	 */
	@Override
	public String modifyMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String serviceName = map.get(McsConstants.SERVICE_NAME);
	    String userId = map.get(McsConstants.USER_ID);
	    String haMode = map.get(McsConstants.HA_MODE);
		String capacity = map.get(McsConstants.CAPACITY);
		int cacheSize = Integer.valueOf(capacity);
		
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(capacity, "capacity为空");
		
		List<McsUserCacheInstance> userInstanceList = getMcsServiceInfo(serviceId, userId);
		
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_STOP);
		operateDocker(userInstanceList, McsConstants.DOCKER_COMMAND_REMOVE);
		
		/** 重新指定docker运行的缓存大小的参数值. **/
		runDocker(userInstanceList, haMode, cacheSize);
		
		/** 如果扩容的MCS是集群模式，需要执行集群创建的命令. **/
		if(userInstanceList.size() > 1) {
			createClusterDocker(userInstanceList);
		}
		
		updateUserInstance(userInstanceList, cacheSize, serviceName);
		
		modifyMcsResource(userInstanceList, false, cacheSize);
		
		logger.info("-------- 扩容成功 --------");
		
		return McsConstants.SUCCESS_FLAG;
	}
	
	/**
	 * 修改缓存资源池的使用内存量
	 * 
	 * @param userInstanceList
	 * @param cacheSize
	 */
	private void modifyMcsResource(List<McsUserCacheInstance> userInstanceList, Boolean isAdd, int cacheSize) {
		for(McsUserCacheInstance userInstance: userInstanceList) {
			String host = userInstance.getCacheHost();
			Integer curentCacheSize = userInstance.getCacheMemory();
			
			/** 获取当前用户实例的值对象 **/
			McsResourcePoolMapper mapper = ServiceUtil.getMapper(McsResourcePoolMapper.class);
			McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
			condition.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(host);
			List<McsResourcePool> pools = mapper.selectByExample(condition);
			McsResourcePool pool = pools.get(0);
			
			/** 
			 * 根据操作类型“扩容”／“注销”，更新MCS资源表中的“已使用缓存容量”字段.
			 * 规则：“注销”->“回收资源，加值”；
			 * 		“扩容”->“使用资源，减值”。
			 * **/
			if(isAdd) {
				pool.setCacheMemoryUsed(curentCacheSize + cacheSize);
			} else {
				pool.setCacheMemoryUsed(pool.getCacheMemory() - curentCacheSize);
			}
			
			mapper.updateByExampleSelective(pool, condition);
		}
	}
	
	private List<McsUserCacheInstance> getMcsServiceInfo(String serviceId, String userId) throws PaasException {
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()) {
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法处理！");
		}
		return userInstanceList;
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

	private void runAnsileCommand(String ansibleCommand) throws PaasException {
		try {
			AgentUtil.executeCommand(ansibleCommand, AidUtil.getAid());
		} catch (Exception ex) {
			logger.error("Excute runAnsileCommand() error, command is " + ansibleCommand);
			ex.printStackTrace();
			throw new PaasException("runAnsileCommand() exception.");
		}
	}

	private String getRedisServerCommand(String capacity, String basePath,
			String hostIp, Integer cachePort, String requirepass, String mode,
			String sshUser, String sshUserPwd, String containerName, String mcsImage) {
		StringBuilder ansibleCommand = new StringBuilder("/usr/bin/ansible-playbook -i ")
			.append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
			.append(McsConstants.PLAYBOOK_HOST_CFG).append(" ")
			.append(basePath).append("/mcs/").append(McsConstants.PLAYBOOK_SINGLE_YML)
			.append(" --user=").append(sshUser)
			.append(" --extra-vars \"ansible_ssh_pass=").append(sshUserPwd)
			.append(" container_name=").append(containerName)
			.append(" image=").append(mcsImage)
			.append(" REDIS_PORT=").append(cachePort)
			.append(" START_MODE=").append(mode)
			.append(" host=").append(hostIp)
			.append(" user=").append(sshUser)
			.append(" MAX_MEM=").append(capacity).append("m")
			.append(" PASSWORD=").append(requirepass).append("\"");
		logger.info("-----ansibleCommand:" + ansibleCommand.toString());
		return ansibleCommand.toString();
	}
	
	private String getCreateClusterCommand(String basePath, String hostIp, String sshUser, 
			String sshUserPwd, String clusterInfo, String containerName, IpaasImageResource mcsImage) {
		StringBuilder commond = new StringBuilder("/usr/bin/ansible-playbook -i ")
			.append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
			.append(McsConstants.PLAYBOOK_HOST_CFG).append(" ")
			.append(basePath).append("/mcs/").append(McsConstants.PLAYBOOK_CLUSTER_YML)
			.append(" --user=").append(sshUser)
			.append(" --extra-vars \"ansible_ssh_pass=").append(sshUserPwd)
			.append(" container_name=").append(containerName)
			.append(" host=").append(hostIp)
			.append(" user=").append(sshUser)
			.append(" image=").append(mcsImage.getImageRepository()).append("/").append(mcsImage.getImageName())
			.append(" CLUSTER_INFO=").append(clusterInfo).append("\"");
		logger.info("-----createClusterCommand:" + commond.toString());
		return commond.toString();
	}
	
	private String getRedisSlaveCommand(String capacity, String basePath, String hostIp, Integer cachePort,
			String masterpass, String mode, String sshUser, String sshUserPwd, String masterIp, Integer masterPort,
			String containerName, IpaasImageResource mcsImage) {
		StringBuilder ansibleCommand = new StringBuilder("/usr/bin/ansible-playbook -i ")
			.append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
			.append(McsConstants.PLAYBOOK_HOST_CFG).append(" ")
			.append(basePath).append("/mcs/").append(McsConstants.PLAYBOOK_REPLICATION_YML)
			.append(" --user=").append(sshUser)
			.append(" --extra-vars \"ansible_ssh_pass=").append(sshUserPwd)
			.append(" container_name=").append(containerName)
			.append(" image=").append(mcsImage.getImageRepository()).append("/").append(mcsImage.getImageName())
			.append(" REDIS_PORT=").append(cachePort)
			.append(" START_MODE=").append(mode)
			.append(" host=").append(hostIp)
			.append(" user=").append(sshUser)
			.append(" MAX_MEM=").append(capacity).append("m")
			.append(" PASSWORD=").append(masterpass)
			.append(" MASTER_IP=").append(masterIp)
			.append(" MASTER_PORT=").append(masterPort).append("\"");
		logger.info("-----ansibleCommand:" + ansibleCommand.toString());
		return ansibleCommand.toString();
	}
	
	private String getRedisSentinelCommand(String basePath, String hostIp, Integer cachePort,
			String masterpass, String mode, String sshUser, String sshUserPwd, String masterIp, Integer masterPort,
			String containerName, IpaasImageResource mcsImage) {
		StringBuilder ansibleCommand = new StringBuilder("/usr/bin/ansible-playbook -i ")
			.append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
			.append(McsConstants.PLAYBOOK_HOST_CFG).append(" ")
			.append(basePath).append("/mcs/").append(McsConstants.PLAYBOOK_SENTINEL_YML)
			.append(" --user=").append(sshUser)
			.append(" --extra-vars \"ansible_ssh_pass=").append(sshUserPwd)
			.append(" container_name=").append(containerName)
			.append(" image=").append(mcsImage.getImageRepository()).append("/").append(mcsImage.getImageName())
			.append(" REDIS_PORT=").append(cachePort)
			.append(" START_MODE=").append(mode)
			.append(" host=").append(hostIp)
			.append(" user=").append(sshUser)
			.append(" PASSWORD=").append(masterpass)
			.append(" MASTER_IP=").append(masterIp)
			.append(" MASTER_PORT=").append(masterPort).append("\"");
		logger.info("-----ansibleCommand:" + ansibleCommand.toString());
		return ansibleCommand.toString();
	}
	
	private String getDockerOperateCommand(String basePath, String hostIp, String sshUser, 
			String sshUserPwd, String operate, String containerName) {
		StringBuilder commond = new StringBuilder("/usr/bin/ansible-playbook -i ")
			.append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
			.append(McsConstants.PLAYBOOK_HOST_CFG).append(" ")
			.append(basePath).append("/mcs/").append(McsConstants.PLAYBOOK_OPERATE_YML)
			.append(" --user=").append(sshUser)
			.append(" --extra-vars \"ansible_ssh_pass=").append(sshUserPwd)
			.append(" operate=").append(operate)
			.append(" host=").append(hostIp)
			.append(" user=").append(sshUser)
			.append(" container_name=").append(containerName).append("\"");
		return commond.toString();
	}
	
	private String getMcsSSHInfo(String field_code) throws PaasException {
		IpaasSysConfigMapper sysconfigDao = ServiceUtil.getMapper(IpaasSysConfigMapper.class);
		IpaasSysConfigCriteria rpmc = new IpaasSysConfigCriteria();
		rpmc.createCriteria().andTableCodeEqualTo(McsConstants.SERVICE_CODE).andFieldCodeEqualTo(field_code);
		List<IpaasSysConfig> res = sysconfigDao.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("MCS ssh user not config.");
		return res.get(0).getFieldValue();
	}
	
	private IpaasImageResource getMcsImage(String serviceCode, String imageCode) throws PaasException {
		IpaasImageResourceMapper rpm = ServiceUtil.getMapper(IpaasImageResourceMapper.class);
		IpaasImageResourceCriteria rpmc = new IpaasImageResourceCriteria();
		rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
				.andServiceCodeEqualTo(serviceCode).andImageCodeEqualTo(imageCode);
		List<IpaasImageResource> res = rpm.selectByExample(rpmc);
		if (res == null || res.isEmpty())
			throw new PaasException("MCS IMAGE not config.");
		return res.get(0);
	}
	
	/**
	 * 创建ansibe-playbook所用的 mcs_host.cfg 文件
	 */
	private void createHostCfg(String basePath) throws PaasException {
		StringBuilder command = new StringBuilder();
		command.append(" mkdir -p ").append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
		.append(" &&cd ").append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
		.append(" &&touch ").append(McsConstants.PLAYBOOK_HOST_CFG);
		
		runAnsileCommand(command.toString());
	}
	
	/**
	 * 将IP信息写入 mcs_host.cfg 文件
	 */
	private void writeHostCfg(String basePath, String hostIp) throws PaasException {
		StringBuilder command = new StringBuilder();
		command.append("cd ").append(basePath).append(McsConstants.PLAYBOOK_CFG_PATH)
		.append(" &&echo ").append(hostIp).append("> ").append(McsConstants.PLAYBOOK_HOST_CFG);

		runAnsileCommand(command.toString());
	}
	
	/** 
	 * 上传文件
	 ***/
	private void uploadMcsFile(String destPath, String fileName) throws PaasException {
		InputStream in = McsManageImpl.class.getResourceAsStream(destPath + fileName);
		try{
			AgentUtil.uploadFile("mcs/"+fileName, AgentUtil.readFileLines(in), AidUtil.getAid());
			in.close();
		} catch (Exception ex) {
			logger.error("Excute uploadMcsFiles() failed," + ex.getMessage());
			ex.printStackTrace();
			throw new PaasException("openSingleMcs.uploadMcsFile() exception.");
		}
	}

	private void runDocker(List<McsUserCacheInstance> userInstanceList, String haMode, int addCacheSize)
			throws PaasException {
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		for (McsUserCacheInstance ins :userInstanceList) {
			String hostIp = ins.getCacheHost();
			Integer cachePort = ins.getCachePort();
			String requirepass = ins.getPwd(); 
			String containerName = ins.getContainerName();
			String redisImage = ins.getRedisImage();
			String capacity = (ins.getCacheMemory() + addCacheSize) + ""; 
			
			writeHostCfg(basePath, hostIp);
			uploadMcsFile(McsConstants.PLAYBOOK_MCS_PATH, McsConstants.PLAYBOOK_SINGLE_YML);
			String ansibleCommand = getRedisServerCommand(capacity, basePath, hostIp, cachePort, requirepass, 
					haMode, sshUser, sshUserPwd, containerName, redisImage);
			
			logger.info("-------- docker command :" + ansibleCommand);
			runAnsileCommand(ansibleCommand);
		}
	}
	
	private void createClusterDocker(List<McsUserCacheInstance> userInstanceList) throws PaasException {
		IpaasImageResource redisClusterImage = getMcsImage(McsConstants.SERVICE_CODE, McsConstants.REDIS_CLUSTER_IMAGE_CODE);
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		String userId = "", serviceId = "", hostIp = "", clusterInfo = "";
		for(McsUserCacheInstance vo: userInstanceList) {
			clusterInfo += vo.getCacheHost()+ ":" + vo.getCachePort() + " ";
			hostIp = vo.getCacheHost();
			userId = vo.getUserId();
			serviceId = vo.getSerialNumber();
		}
		
		/** 不再上传mcs_cluster.yml, 需要将hostIp>host.cfg文件；
		 * 创建redis集群在任意台资源主机上执行命令均可。
		 * 执行redis集群创建命令 **/
		writeHostCfg(basePath, hostIp);
		String containerName = userId + "-" + serviceId + "-createRedisCluster";
		String redisClusterRun = getCreateClusterCommand(basePath, hostIp, sshUser, sshUserPwd, clusterInfo, containerName, redisClusterImage);
		runAnsileCommand(redisClusterRun);
		logger.info("-------- 创建MCS集群成功！");
		
		/** 释放掉创建集群的docker容器 **/
		releaseDocker(hostIp, containerName, McsConstants.DOCKER_COMMAND_REMOVE);
	}
	
	private void operateDocker(List<McsUserCacheInstance> userInstanceList, String command)
			throws PaasException {
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		for (McsUserCacheInstance ins :userInstanceList) {
			String hostIp = ins.getCacheHost();
			String containerName = ins.getContainerName();

			writeHostCfg(basePath, hostIp);
	
			String ansibleCommand = getDockerOperateCommand(basePath, hostIp, sshUser,
					sshUserPwd, command, containerName);
			logger.info("-------- docker command :" + ansibleCommand);
			runAnsileCommand(ansibleCommand);
		}
	}
	
	private void releaseDocker(String hostIp, String containerName, String command)
			throws PaasException {
		String sshUser = getMcsSSHInfo(McsConstants.SSH_USER_CODE);
		String sshUserPwd = getMcsSSHInfo(McsConstants.SSH_USER_PWD_CODE);
		String basePath = AgentUtil.getAgentFilePath(AidUtil.getAid());
		
		String ansibleCommand = getDockerOperateCommand(basePath, hostIp, sshUser,
					sshUserPwd, command, containerName);
		logger.info("-------- docker command :" + ansibleCommand);
		
		runAnsileCommand(ansibleCommand);
	}
	
	/**
	 * 生成"ip1:port1;ip2:port2"格式的集群信息串
	 */
	private String getClusterInfo(List<McsProcessInfo> list, String separator) throws PaasException {
		String cluster = "";
		for(McsProcessInfo vo: list) {
			cluster += vo.getCacheHostIp() + ":" + vo.getCachePort() + separator;
		}
		
		return cluster;
	}
	
	/**
	 * 在开通MCS服务的集群(cluster)模式时，选择MCS资源。
	 * @param cacheSize 每个实例的缓存大小
	 * @param redisInsNum 启动的redis实例数
	 * @return List<McsProcessInfo>
	 * @throws PaasException
	 */
	private List<McsProcessInfo> selectMcsResCluster(String userId, String serviceId, 
			int cacheSize, int redisInsNum) throws PaasException {
		List<McsProcessInfo> returnList = new ArrayList<McsProcessInfo>();

		/**  
		 * 每次在一台资源主机上，启动2个redis实例，1个master/1个slave；
		 * 如果资源主机数量少于redisInsNum，可能在一个资源主机上启动多个实例。
		 * 注：由于没有提前判断资源主机的可用内存，故每次获取＋2个资源。
		 */
		List<McsResourcePool> resourceList = getBestResource(redisInsNum + 2);
		for(int i=0; i < redisInsNum/2; i++){
			/** 筛选并指定资源 **/
			McsResourcePool pool = getResourceInfo(resourceList, cacheSize*2, i);
			if(null==pool) {
				logger.error("++++ 所有可用资源主机的内存均不足，无法开通redis集群 ++++");
				throw new PaasException("mcs resource memory not enough.");
			}
			
			/** 已取到的资源信息 **/
			String hostIp = pool.getCacheHostIp();
			Integer currentPort = pool.getCachePort();
			Integer masterPort = currentPort+1;
			Integer slavePort = currentPort+2;
			Integer usedCacheSize = pool.getCacheMemoryUsed();
			String masterContainer = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + masterPort;
			String slaveContainer = userId + "-" + serviceId + "-" + hostIp.replace(".", "") + "-" + slavePort;
			
			/** 设置master所用资源 **/
			McsProcessInfo masterVo = new McsProcessInfo();
			masterVo.setCacheHostIp(hostIp);
			masterVo.setCachePort(masterPort);
			masterVo.setContainerName(masterContainer);
			returnList.add(masterVo);
			
			/** 设置slave所用资源 **/
			McsProcessInfo slaveVo = new McsProcessInfo();
			slaveVo.setCacheHostIp(hostIp);
			slaveVo.setCachePort(slavePort);
			slaveVo.setContainerName(slaveContainer);
			returnList.add(slaveVo);
			
			/** 更新mcs资源池记录 **/
			pool.setCachePort(currentPort+2);
			pool.setCacheMemoryUsed(usedCacheSize + cacheSize * 2);
			updateResource(pool);
		}
		
		if(returnList.size() < redisInsNum) {
			logger.error("++++ 资源不足:申请开通["+redisInsNum+"]个size为["+cacheSize+"]实例，只能开通["+returnList.size()+"]个实例. ++++");
			throw new PaasException("mcs resource not enough. ");
		}
		
		return returnList;
	}
	
/**
	 * 获取符合条件的资源主机
	 * @param resourceList
	 * @param cacheSize
	 * @param seed
	 * @return McsResourcePool
	 */
	private McsResourcePool getResourceInfo(List<McsResourcePool> resourceList, int cacheSize, int seed) {
		/** 首先根据mod值，从资源list中选取一条，并判断是否符合条件，如果符合条件则返回。 **/
		int mod = seed%(resourceList.size()); 
		McsResourcePool value = resourceList.get(mod);
		if((value.getCacheMemory() - value.getCacheMemoryUsed()) > cacheSize) {
			return value;
		} 
		/** 如果根据mod值所取的资源不符合条件，则循环资源list，判断并获取符合条件的资源，没有匹配的资源则最后返回null。
		 * 注意：由于资源list已按可用内存大小降序排列，所以根据mod值，向上循环list即可，mod值以下的资源主机不必再判断。 **/
		else {
			for(int k = mod; k >= 0; k--){
				value = resourceList.get(k);
				if((value.getCacheMemory() - value.getCacheMemoryUsed()) > cacheSize) {
					return value;
				}
			}
			return null;
		}
	}

	/**
	 * 在开通MCS服务的集群(single)模式/主从复制(replication)模式时，选择MCS资源。
	 * 单例模式端口+1, 主从复制模式端口+2
	 * @param cacheSize
	 * @param portOffset
	 * @return McsResourcePool
	 * @throws PaasException
	 */
	private McsResourcePool selectMcsResSingle(int cacheSize, int portOffset) throws PaasException {
		/** 注:由于没有提前判断资源主机的可用内存，故每次获取＋2个资源. **/
		List<McsResourcePool> resp = getBestResource(3);
		McsResourcePool mcsResourcePool = resp.get(0);
		
		/** 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号  **/
		if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
			mcsResourcePool.setCachePort(getCanUseInstance(mcsResourcePool.getCacheHostIp()).getCachePort());
		} else {
			if (mcsResourcePool.getCachePort() == mcsResourcePool.getMaxPort()) {
				mcsResourcePool.setCycle(1);
			}
			/** 从入参获取端口偏移量，开通单例模式端口+1, 主从复制模式端口+2, sentinel模式端口+3。 **/
			mcsResourcePool.setCachePort(mcsResourcePool.getCachePort() + portOffset);
			mcsResourcePool.setCacheMemoryUsed(mcsResourcePool.getCacheMemoryUsed() + cacheSize);
			int changeRow = updateResource(mcsResourcePool,  portOffset);

			if (changeRow != 1) {
				throw new PaasException("更新资源失败");
			}
		}
		
		return mcsResourcePool;
	}
	
	/**
	 * 新增用户的缓存实例
	 */
	private void addUserInstance(String userId, String serviceId, String capacity, String ip, int port, String pwd,
			String serviceName, String containerName, String imageInfo) throws PaasException {
		McsUserCacheInstance bean = new McsUserCacheInstance();
		bean.setUserId(userId);
		bean.setCacheHost(ip);
		bean.setCacheMemory(Integer.valueOf(capacity));
		bean.setStatus(McsConstants.VALIDATE_STATUS);
		bean.setBeginTime(DateTimeUtil.getNowTimeStamp());
		bean.setEndTime(DateTimeUtil.getNowTimeStamp());
		bean.setSerialNumber(serviceId);
		bean.setCachePort(port);
		bean.setPwd(pwd);
		bean.setServiceName(serviceName);
		bean.setContainerName(containerName);
		bean.setRedisImage(imageInfo);
		
		ServiceUtil.getMapper(McsUserCacheInstanceMapper.class).insert(bean);
	}

	/**
	 * 批量新增用户实例
	 */
	private void addMcsUserInstance(String userId, String serviceId, String serviceName, 
			final int clusterCacheSize, List<McsProcessInfo> cacheInfoList, String image) throws PaasException {
		for (McsProcessInfo cacheInfo : cacheInfoList) {
			logger.info("----add mcs_user_Instance---- userId:["+userId+"],serviceId:["+serviceId+"],"
					+ "clusterCacheSize:["+clusterCacheSize+",cacheIp["+cacheInfo.getCacheHostIp()+"],"
						+ "cachePort:["+cacheInfo.getCachePort()+"],serviceName:["+serviceName+"]");
			StringBuilder containerName = new StringBuilder("");
			containerName.append(userId).append("-").append(serviceId).append("-")
				.append(cacheInfo.getCacheHostIp().replace(".", "")).append("-").append(cacheInfo.getCachePort());
			addUserInstance(userId, serviceId, clusterCacheSize + "", cacheInfo.getCacheHostIp(), 
					cacheInfo.getCachePort(), null, serviceName, containerName.toString(), image);
		}
	}
	
	/**
	 * 更新用户的缓存实例
	 */
	private void updateUserInstance(List<McsUserCacheInstance> userInstanceList, int status) throws PaasException {
		for (McsUserCacheInstance ins :userInstanceList) {
			ins.setStatus(status);
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(ins);
		}
	}
	
	/**
	 * 更新缓存扩容后的实例表
	 */
	private void updateUserInstance(List<McsUserCacheInstance> userInstanceList, int cacheSize,
			String serviceName) throws PaasException {
		for (McsUserCacheInstance ins :userInstanceList) {
			ins.setCacheMemory(cacheSize);
			ins.setServiceName(serviceName);
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(ins);
		}
	}
	
//	/**
//     * 判断用户的zk节点是否存在
//     * @param client
//     * @param nodePath
//     * @return
//     * @throws ConfigException
//     */
//    private boolean userNodeIsExist(String userId) throws ConfigException {
//        boolean result = false;
//        try {
//        	CCSComponentOperationParam op = new CCSComponentOperationParam();
//    		op.setUserId(userId);
//    		op.setPath("/");
//    		op.setPathType(PathType.READONLY);
//    		result = iCCSComponentManageSv.exists(op);
//        } catch (Exception e) {
//            if (e instanceof KeeperException.NoAuthException) {
//                throw new ConfigException(ResourceUtil.getMessage(BundleKeyConstant.USER_AUTH_FAILED));
//            }
//            throw new ConfigException(ResourceUtil.getMessage(BundleKeyConstant.USER_NODE_NOT_EXISTS), e);
//        }
//        return result;
//    }
    
	/**
	 * 在zk中记录申请信息
	 */
	private void addCcsConfig(String userId, String serviceId, List<String> hosts, String requirepass) throws PaasException {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();
		for(String host: hosts) {
			dataJson.addProperty("hosts", host);
		}
		
		logger.info("------ Mcs服务需要在 zk中记录的信息 ------");
		logger.info("------ userId:["+userId+"] ------");
		logger.info("------ zkPath:["+McsConstants.MCS_ZK_PATH + serviceId+"]");
		logger.info("------ dataJson[hosts]:" + dataJson.toString());
		logger.info("------ dataJson[password]:" + requirepass);
		
		if (requirepass != null && requirepass.length() > 0) {
			logger.info("------ CiperUtil.encrypt(password):" + CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));
			dataJson.addProperty("password", CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));
		}
		
		iCCSComponentManageSv.add(op, dataJson.toString());
		logger.info("------ 在zk的"+McsConstants.MCS_ZK_PATH + serviceId+"路径下，记录MCS申请信息成功!");

		op.setPath(McsConstants.MCS_ZK_COMMON_PATH);
		if (!iCCSComponentManageSv.exists(op)) {
			logger.info("------ 设置zk中的 /MCS/COMMON 路径 ------");
			iCCSComponentManageSv.add(op, McsConstants.MCS_ZK_COMMON);
			logger.info("------ 在zk中记录COMMON信息成功!");
		}
	}

	private void addZKConfig(String userId, String serviceId,
			List<McsProcessInfo> cacheInfoList) throws PaasException {
		String redisCluster4ZK = getClusterInfo(cacheInfoList, ";");
		logger.info("-------- redisCluster4ZK is :" + redisCluster4ZK);
		List<String> hostList = new ArrayList<String>();
		hostList.add(redisCluster4ZK.substring(1));
		addCcsConfig(userId, serviceId, hostList, null);
	}
	
	private void delZKConfig(String userId, String serviceId) throws PaasException {
		logger.info("----- delete zk config for ["+userId+"].["+serviceId+"] -----");
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath("/MCS/" + serviceId);
		op.setPathType(PathType.READONLY);
		iCCSComponentManageSv.delete(op);
	}
	
	/**
	 * 获得最空闲的Mcs资源
	 */
	private List<McsResourcePool> getBestResource(int num) {
		McsResourcePoolMapper mapper = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
		condition.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS);
		condition.setLimitStart(0);
		condition.setLimitEnd(num);
		condition.setOrderByClause("(ifnull(cache_memory, 0) - ifnull(cache_memory_used, 0)) desc");
		return mapper.selectByExample(condition);
	}

	/**
	 * 获得UseInstance中"失效"的记录
	 */
	private McsUserCacheInstance getCanUseInstance(String host) throws PaasException {
		McsUserCacheInstanceCriteria condition = new McsUserCacheInstanceCriteria();
		condition.createCriteria().andStatusNotEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostEqualTo(host);
		condition.setLimitStart(0);
		condition.setLimitEnd(1);
		
		McsUserCacheInstance bean = null;
		McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
		List<McsUserCacheInstance> list = im.selectByExample(condition);
		if (list != null && list.size() > 0) {
			bean = list.get(0);
		}
		
		return bean;
	}

	/**
	 * 更新Mcs资源池
	 */
	private int updateResource(McsResourcePool mcsResourcePool, int portOffset) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
		/** TODO:端口偏移量，重点验证。**/
		condition.createCriteria().andIdEqualTo(mcsResourcePool.getId())
			.andCachePortEqualTo(mcsResourcePool.getCachePort() - portOffset); 
		return rpm.updateByExampleSelective(mcsResourcePool, condition);
	}
	
	/**
	 * 更新Mcs资源池
	 */
	private int updateResource(McsResourcePool mcsResourcePool) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
		condition.createCriteria().andIdEqualTo(mcsResourcePool.getId()); 
		return rpm.updateByExampleSelective(mcsResourcePool, condition);
	}
}
