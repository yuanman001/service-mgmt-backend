package com.ai.paas.ipaas.mcs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.ai.paas.ipaas.mcs.service.util.McsProcessInfo;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.DateTimeUtil;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class McsSvImpl implements IMcsSv {
	private static transient final Logger logger = LoggerFactory.getLogger(McsSvImpl.class);

	@Autowired 
	private McsSvHepler mcsSvHepler;
	
	@Autowired 
	private ICCSComponentManageSv iCCSComponentManageSv;

	/**
	 * 服务开通功能
	 */
	@Override
	public String openMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		String userId = map.get(McsConstants.USER_ID);
		String serviceId = map.get(McsConstants.SERVICE_ID);
		String serviceName = map.get(McsConstants.SERVICE_NAME);
		String haMode = map.get(McsConstants.HA_MODE);
		String capacity = map.get(McsConstants.CAPACITY);
		int cacheSize = Integer.valueOf(capacity);

		/** 判断用户的这个缓存服务是否已经开通 **/
		if (existsService(userId, serviceId)) {
			logger.info("------- 用户服务已存在，开通成功 -------");
			return McsConstants.SUCCESS_FLAG;
		}

		/** 开通单例  **/
		if (McsConstants.MODE_SINGLE.equals(haMode)) {
			/** 1.选择开通服务的资源：选择剩余内存最多的主机  **/
			McsResourcePool mcsResourcePool = selectMcsResSingle(cacheSize, 1);
			
			/** 需更新mcs资源表AgentCmd字段，只存agent端口号！ **/
			Integer agentPort = Integer.valueOf(mcsResourcePool.getAgentCmd());
			Integer cachePort = mcsResourcePool.getCachePort();
			String cacheHostIp = mcsResourcePool.getCacheHostIp();
			String cachePath = mcsResourcePool.getCachePath();
			String requirepass = mcsSvHepler.getRandomKey();

			/** 2.初始化agent  **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 3.创建端口号命名的文件夹  **/
			String commonconfigPath = cachePath + McsConstants.FILE_PATH;
			addConfigFolder(ac, commonconfigPath, cachePort);
			
			/** 4.创建配置文件并上传至服务器指定目录  **/
			addMcsConfig(ac, cachePath, cachePort, capacity, requirepass);
			
			/** 5.启动单例  **/
			startMcsIns(ac, commonconfigPath, cachePort);
			logger.info("---------启动redis成功!");
			
			/** 6.处理zk 配置 **/
			List<String> hostList = new ArrayList<String>();
			hostList.add(cacheHostIp + ":" + cachePort);
			addCcsConfig(userId, serviceId, hostList, requirepass);
			logger.info("----------处理zk 配置成功！");
			
			/** 7.mcs用户实例表增加纪录  **/
			addUserInstance(userId, serviceId, capacity, cacheHostIp, cachePort, requirepass, serviceName);
			logger.info("---------处理用户实例成功！");
			
		} else if (McsConstants.MODE_CLUSTER.equals(haMode)) {
			logger.info("-------- 开通集群模式的MCS服务 --------");
			/** 计算缓存大小 **/
			final int clusterCacheSize = Math.round(cacheSize / McsConstants.CACHE_NUM * 2);

			/** 从Mcs资源池中选取资源  **/
			List<McsProcessInfo> cacheInfoList = selectMcsResCluster(clusterCacheSize, McsConstants.CACHE_NUM);
			
			/** 增加集群中各server的目录及配置文件，并启动redis。 **/
			addMcsConfigCluster(cacheInfoList, userId, serviceId, clusterCacheSize);
			
			/** 组织集群创建的命令及返回值 **/
			String clusterInfo = getClusterInfo(cacheInfoList, " ");
			
			/** 在集群中的任意台主机上，执行redis集群创建的命令 **/
			McsProcessInfo vo = cacheInfoList.get(0);
			AgentClient ac = new AgentClient(vo.getCacheHostIp(), vo.getAgentPort());
			
			/** 在集群中的任意台主机上，执行redis集群创建的命令 **/
			createRedisCluster(ac, clusterInfo);
			
			/** 添加zk配置  **/
			String hostsCluster = getClusterInfo(cacheInfoList, ";");
			logger.info("-------- hostsCluster is :" + hostsCluster);
			
			List<String> hostList = new ArrayList<String>();
			hostList.add(hostsCluster.substring(1));
			addCcsConfig(userId, serviceId, hostList, null);
			logger.info("-------- 开通MCS集群模式，处理zk 配置 --------");
			
			/** 循环处理集群模式的MCS用户实例 **/
			for (McsProcessInfo cacheInfo : cacheInfoList) {
				logger.info("----add mcs_user_Instance---- userId:["+userId+"],serviceId:["+serviceId+"],"
						+ "clusterCacheSize:["+clusterCacheSize+",cacheIp["+cacheInfo.getCacheHostIp()+"],"
							+ "cachePort:["+cacheInfo.getCachePort()+"],serviceName:["+serviceName+"]");
				addUserInstance(userId, serviceId, clusterCacheSize + "", cacheInfo.getCacheHostIp(), 
						cacheInfo.getCachePort(), null, serviceName);
			}
		} else if (McsConstants.MODE_REPLICATION.equals(haMode)) {
			logger.info("1----------------进入主从模式选择主机");
			// --------选择剩余内存最多的主机 缓存资源
			McsResourcePool pool = selectMcsResSingle(cacheSize * 2, 2);
			logger.info("2----------------选择主机:" + pool.getCacheHostIp() + "端口：" + pool.getCachePort());
			
			//获取随机数作为redis密码。
			String requirepass = mcsSvHepler.getRandomKey();
			
			logger.info("3----------------处理mcs服务端");
			// 处理mcs服务端配置文件
			addMasterConfig(pool, pool.getCachePort(), capacity, requirepass);
			addSlaveConfig(pool, pool.getCachePort() - 1, capacity, requirepass);
			
			// 处理zk 配置
			logger.info("4----------------处理zk 配置");
			List<String> hostList = new ArrayList<String>();
			hostList.add(pool.getCacheHostIp() + ":" + pool.getCachePort());
			addCcsConfig(userId, serviceId, hostList, requirepass);
			
			// -------mcs_user_cache_instance表新增记录
			logger.info("5----------------处理用户实例");
			addUserInstance(userId, serviceId, capacity,
					pool.getCacheHostIp(), pool.getCachePort(), requirepass, serviceName);

		} else if (McsConstants.MODE_SENTINEL.equals(haMode)) {
			logger.info("1----------------进入集群选择主机");
			final int clusterCacheSize = Math.round(cacheSize / McsConstants.CACHE_NUM * 2);
			
			/** 选取 资源池  **/
			List<McsProcessInfo> mcsProcessList = selectMcsResCluster(
					clusterCacheSize, McsConstants.SENTINEL_NUM);
			logger.info("3----------------处理mcs服务端");
			
			/** 获取随机数作为redis密码。 **/
			String requirepass = mcsSvHepler.getRandomKey();
			
			/** 临时保存master信息，在配置slave、sentine时使用。 **/
			McsProcessInfo masterInfo = null;
			
			int counter = 0;
			/** 选取第一项为主，二三项为从，四五六启动sentinel进程  **/
			for(McsProcessInfo vo: mcsProcessList) {
				if(counter < 1) {
					// 选取第一项为主
					masterInfo = vo;
					configSenMaster(vo, capacity, requirepass);
				} else if (counter < 3) {
					// 选取第一项为主，二三项为从
					configSenSlave(vo, masterInfo, capacity, requirepass);
				} else if(counter < 6) {
					// 四五六启动sentinel进程
					configSentinel(vo, masterInfo);
				}
				
				counter++;
			}
			
			logger.info("4----------------处理zk 配置");
			List<String> hostList = new ArrayList<String>();
			for(McsProcessInfo vo: mcsProcessList) {
				hostList.add(vo.getCacheHostIp() + ":" + vo.getCachePort());
			}
			addCcsConfig(userId, serviceId, hostList, requirepass);
			
			logger.info("5----------------处理用户实例");
			for (String address : hostList) {
				String[] add = address.split(":");
				// -------mcs_user_cache_instance表新增记录
				addUserInstance(userId, serviceId, clusterCacheSize + "", add[0], Integer.parseInt(add[1]), null, serviceName);
			}
		}

		logger.info("----------------开通成功");
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
		String capacity = map.get(McsConstants.CAPACITY);
		
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(capacity, "capacity为空");
		
		logger.info("M1----------------获得已申请过的记录");
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()) {
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法修改！");
		}
		
		/** 计算扩容的容量大小。 **/
		logger.info("M2----------------修改mcs_resource");  
		int cacheSize = userInstanceList.size() == 1 ? Integer.valueOf(capacity)
				: Math.round(Integer.valueOf(capacity) / McsConstants.CACHE_NUM * 2);
		modifyMcsResource(userInstanceList, true, cacheSize);
		
		logger.info("M3----------------修改mcs服务端，重启redis");
		modifyMcsServerFileAndUserIns(userId, serviceId, userInstanceList, cacheSize, serviceName);
		
		logger.info("----------------修改成功");
		
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
		
		/** 根据user_id,service_id,获取用户实例信息 **/
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()) {
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法启动！");
		}
		
		/** 调用启动Mcs的处理方法 **/
		startMcs(userId, serviceId, userInstanceList);
		logger.info("----------启动["+userId+"]-["+serviceId+"]的MCS缓存服务,成功");
		
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
		
		/** 根据user_id,service_id,获取用户实例信息 **/
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()) {
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法停止！");
		}
		
		stopMcsInsForCacheIns(userInstanceList);
		logger.info("----------停止["+userId+"]-["+serviceId+"]的MCS缓存服务,成功");
		
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
		
		/** 根据user_id,service_id,获取用户实例信息 **/
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()){
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法重启！");
		}
		
		logger.info("------- 停止["+userId+"]-["+serviceId+"]的MCS缓存服务");
		stopMcsInsForCacheIns(userInstanceList);
		
		logger.info("------- 启动["+userId+"]-["+serviceId+"]的MCS缓存服务");
		startMcs(userId, serviceId, userInstanceList);
		
		logger.info("------- 重启["+userId+"]-["+serviceId+"]的MCS缓存服务OK.");
		
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
	
		/** 根据user_id,service_id,获取用户实例信息 **/
		List<McsUserCacheInstance> userInstanceList = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (userInstanceList == null || userInstanceList.isEmpty()) {
			throw new PaasException("UserId["+userId+"]的["+serviceId+"]服务未开通过，无法注销！");
		}
		
		logger.info("------- 注销["+userId+"]-["+serviceId+"]的MCS缓存服务,更新MCS资源表中已使用缓存的大小.");
		modifyMcsResource(userInstanceList, false, 0);
		
		logger.info("------- 注销["+userId+"]-["+serviceId+"]的MCS缓存服务,删除配置文件.");
		removeMcsServerFileAndUserIns(userId, serviceId, userInstanceList);

		logger.info("------- 注销["+userId+"]-["+serviceId+"]的MCS缓存服务,OK!");
		
		return McsConstants.SUCCESS_FLAG;
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
	 * @param cacheInfoList
	 * @param userId
	 * @param serviceId
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	private void addMcsConfigCluster(List<McsProcessInfo> cacheInfoList,
			String userId, String serviceId, int cacheSize)throws PaasException {
		logger.info("-------- 开始处理集群配置文件的time：" + new Date());
		for (McsProcessInfo proInfo : cacheInfoList) {
			String cacheHost = proInfo.getCacheHostIp();
			String cachePath = proInfo.getCachePath();
			Integer cachePort = proInfo.getCachePort();
			Integer agentPort = proInfo.getAgentPort();
			String clusterPath = cachePath + McsConstants.CLUSTER_FILE_PATH;
			String logPath = cachePath + McsConstants.LOG_PATH;

			/** 初始化Agentclint **/
			AgentClient ac = new AgentClient(cacheHost, agentPort);

			/** 创建redis集群的目录: ./redis/cluster/user_id+service_id **/
			String mkdir_cluster = "mkdir -p " + clusterPath + userId + "_" + serviceId;
			mcsSvHepler.excuteCommand(ac, mkdir_cluster);
			logger.info("-------- 创建redis集群的目录：" + mkdir_cluster +", OK!");
			
			/**
			 * 创建redis集群下的server目录: ./redis/cluster/user_id+service_id/redisPort
			 **/
			String mkdir_port = "mkdir -p " + clusterPath + userId + "_" + serviceId + "/" + cachePort;
			mcsSvHepler.excuteCommand(ac, mkdir_port);
			logger.info("-------- 创建redis集群下的server目录：" + mkdir_port +", OK!");
			
			/** 生成集群中server的配置文件，文件名要有全路径。 **/
			String configFile = clusterPath + userId + "_" + serviceId + "/" + cachePort + "redis-" + cachePort + ".conf";
			String configDetail = "include " + clusterPath
					+ "redis-common.conf \n" + "pidfile /var/run/redis-"
					+ cachePort + ".pid" + "\n" + "port " + cachePort + "\n"
					+ "cluster-enabled yes \n" + "maxmemory " + cacheSize
					+ "m \n" + "cluster-config-file nodes.conf \n"
					+ "cluster-node-timeout 5000 \n" + "appendonly yes \n";

			/** 上传配置文件 **/
			mcsSvHepler.uploadFile(ac, configFile, configDetail);
			logger.info("-------- 上传redis集群的配置文件：" + configFile +", OK!");

			/** 启动集群中的每个redis-server **/
			String cmd_start = "redis-server " + configFile + " > " + logPath + "redis-" + cachePort + ".log &";
			mcsSvHepler.excuteCommand(ac, cmd_start);
			logger.info("-------- 启动redis集群的server：" + cmd_start + ", OK!");
			
			logger.info("-------- 处理MCS_CLUSTER中的server["+cacheHost+":"+cachePort+"]结束, time:" + new Date());
		}
	}

	/**
	 * 生成"ip1:port1;ip2:port2"格式的集群信息串
	 * @param list
	 * @param separator
	 * @return
	 * @throws PaasException
	 */
	private String getClusterInfo(List<McsProcessInfo> list, String separator) throws PaasException {
		String cluster = "";
		for(McsProcessInfo vo: list) {
			cluster += vo.getCacheHostIp() + ":" + vo.getCachePort() + separator;
		}
		return cluster;
	}
	
	/**
	 * 在开通MCS服务的集群(cluster)模式/主从切换(sentinel)模式时，选择MCS资源。
	 * @param cacheSize  需要开通的每个实例的缓存大小
	 * @param redisInsNum  启动的redis实例数
	 * @return List<McsProcessInfo>
	 * @throws PaasException
	 */
	//TODO:需要重构处理逻辑
	private List<McsProcessInfo> selectMcsResCluster(int cacheSize, int redisInsNum) throws PaasException {
		/** defined return result. **/
		List<McsProcessInfo> cacheInfoList = new ArrayList<McsProcessInfo>();
		
		/** 如果资源主机数量少于redisInsNum，可能在一个资源主机上启动多个实例。 **/
		List<McsResourcePool> resourceList = getBestResource(redisInsNum);
		int hostNum = resourceList.size();   /** 一般资源池中所配主机为3台，会获取3条资源记录。 **/
		
		/** 已获取到的实例数量 **/
		int gotInsNum = 0;
		
		/** 资源主机数量 **/
		int k = hostNum;
		
		/** 记录while循环的次数. **/
		int loopCount = 0;
		
		int j = 1;
		
		String hostIp = null;
		int port = -1;
		
		/** 如果已选定的资源数量，小于申请的数量; 并且，while循环的次数，小于申请的数量+1，则继续资源选择处理. **/
		while (gotInsNum < redisInsNum && loopCount < (redisInsNum + 1)) {
			for (int m = 0; m < k; m++) {
				McsResourcePool pool = resourceList.get(m);
				hostIp = pool.getCacheHostIp();
				port = pool.getCachePort();
				
				/** 当前所选资源主机的已占用的内存大小 **/
				Integer usedCacheSize = pool.getCacheMemoryUsed();
				
				/** 资源主机的总内存大小 **/
				Integer cacheMemory = pool.getCacheMemory();
				
				/** 当前所选资源主机的可用内存，小于所申请的内存，则不再使用该资源，跳出循环。 **/
				if ((cacheMemory - usedCacheSize) < cacheSize * j) {
					k = m;     /** ??? **/
					j++;  	   /** 当前主机内存不够，下个资源主机则需承担此容量，下一次判断开通总容量需 *j，所以需要加权。 **/
					continue;  /** 跳出本次资源主机的选择逻辑 **/
				}
				
				/** cycle＝1, 表示端口循环使用 **/
				if (pool.getCycle() == 1) {
					/** 从MCS用户实例表中，查找一条失效状态的记录，使用此实例的端口 **/
					pool.setCachePort(getCanUseInstance(hostIp).getCachePort());
				} else {
					pool.setCachePort(port + 1);
					pool.setCacheMemoryUsed(usedCacheSize + cacheSize);
					if (pool.getCachePort() == pool.getMaxPort()) {
						pool.setCycle(1);
					}
					int changeRow = updateResource(pool, 1);
					if (changeRow != 1) {
						logger.error("---- selectMcsResCluster()中，选定资源后，更新资源失败！----");
						throw new PaasException("---- selectMcsResCluster()中，选定资源后，更新资源失败！----");
					}
				}
				
				logger.info("--------selectMcsResCluster(), 从主机:" + hostIp + "中，选定了端口：" + port);
				 
				/** 将所选定的主机、端口等信息，放到list返回值中，开通时遍历使用。 **/
				String cachePath = pool.getCachePath();
				Integer agentPort = Integer.parseInt(pool.getAgentCmd());
				
				McsProcessInfo vo = new McsProcessInfo();
				vo.setCacheHostIp(hostIp);
				vo.setCachePath(cachePath);
				vo.setCachePort(port);
				vo.setAgentPort(agentPort);
				cacheInfoList.add(vo);
				
				gotInsNum++;
			}
			
			loopCount++;
		}
		
		logger.info("----- selectMcsResCluster():申请["+redisInsNum+"]个实例，选定了["+gotInsNum+"]个实例 -----");
		
//		if (loopCount > redisInsNum) {
		/** 如果循环count次后，已选定的实例数量仍小于申请的数量，则表示资源不足 **/
		if (gotInsNum < redisInsNum) {
			logger.error("++++ 资源不足:申请开通["+redisInsNum+"]个size为["+cacheSize+"]实例，目前只选择了["+gotInsNum+"]个实例 ++++");
			throw new PaasException("++++++ mcs resource not enough. ++++++");
		}
		
		return cacheInfoList;
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
		List<McsResourcePool> resp = getBestResource(1);
		McsResourcePool mcsResourcePool = resp.get(0);
		
		/** 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号  **/
		if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
			mcsResourcePool.setCachePort(getCanUseInstance(mcsResourcePool.getCacheHostIp()).getCachePort());
		} else {
			if (mcsResourcePool.getCachePort() == mcsResourcePool.getMaxPort()) {
				mcsResourcePool.setCycle(1);
			}
			/** 从入参获取端口偏移量，开通单例模式端口+1, 主从复制模式端口+2。 **/
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

	/**
	 * 生成Mater节点的配置文件，并启动Master节点。
	 * @param mcsResourcePool
	 * @param redisPort
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addMasterConfig(McsResourcePool mcsResourcePool, int redisPort, 
			String capacity, String requirepass) throws PaasException {
		
		String cacheHostIp = mcsResourcePool.getCacheHostIp();
		String cachePath = mcsResourcePool.getCachePath();
		Integer agentPort = Integer.parseInt(mcsResourcePool.getAgentCmd());
		
		/** 初始化agent  **/
		AgentClient ac = new AgentClient(cacheHostIp, agentPort);
		
		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, redisPort);

		try {
			String fileName = cachePath + McsConstants.FILE_PATH + redisPort + "/" + "redis-" + redisPort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + redisPort + ".pid" + "\n"
					+ "port " + redisPort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + redisPort + ".log"
					+ "tcp-keepalive 60 \n"
					+ "maxmemory-policy noeviction \n"
					+ "appendonly yes \n"
					+ "appendfilename \"appendonly.aof\"  \n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
			
		startMcsIns(ac, commonconfigPath, redisPort);
		logger.info("---------启动redis成功!");
	}

	/**
	 * 生产Sentinel模式中，Master节点的配置文件，并启动master节点
	 * @param value
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void configSenMaster(McsProcessInfo value, String capacity, String requirepass) throws PaasException {
		
		String cacheHost = value.getCacheHostIp();
		String cachePath = value.getCachePath();
		Integer cachePort = value.getCachePort();
		Integer agentPort = value.getAgentPort();
		
		AgentClient ac = new AgentClient(cacheHost, agentPort);

		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, cachePort);
		
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + cachePort + "/" + "redis-" + cachePort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + cachePort + ".pid" + "\n"
					+ "port " + cachePort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + cachePort + ".log"
					+ "tcp-keepalive 60 \n"
					+ "maxmemory-policy noeviction \n"
					+ "appendonly yes \n"
					+ "appendfilename \"appendonly.aof\"  \n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("-------------上传文件成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}

		startMcsIns(ac, commonconfigPath, cachePort);
		logger.info("-------------启动redis成功!");
	}

	/**
	 * 生产Slave节点配置文件，并启动Slave节点
	 * @param mcsResourcePool
	 * @param redisPort
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addSlaveConfig(McsResourcePool mcsResourcePool, int redisPort,
			String capacity, String requirepass) throws PaasException {

		String cacheHostIp = mcsResourcePool.getCacheHostIp();
		String cachePath = mcsResourcePool.getCachePath();
		Integer agentPort = Integer.parseInt(mcsResourcePool.getAgentCmd());
		
		/** 初始化agent  **/
		AgentClient ac = new AgentClient(cacheHostIp, agentPort);
		
		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, redisPort);

		try {
			String fileName = cachePath + McsConstants.FILE_PATH + redisPort + "/" + "redis-" + redisPort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + redisPort + ".pid" + "\n"
					+ "port " + redisPort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + redisPort + ".log\n"
					+ "slaveof " + mcsResourcePool.getCacheHostIp() + " " + mcsResourcePool.getCachePort() + "\n"
					+ "masterauth " + requirepass + "\n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}

		startMcsIns(ac, commonconfigPath, redisPort);
		logger.info("---------启动redis成功!");
	}

	/**
	 * sentinal模式中，生成slave节点配置文件，并启动slave节点
	 * @param slaveInfo
	 * @param masterInfo
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void configSenSlave(McsProcessInfo slaveInfo, McsProcessInfo masterInfo, String capacity, String requirepass) 
			throws PaasException {
		String slaveIp = slaveInfo.getCacheHostIp();
		String cachePath = slaveInfo.getCachePath();
		Integer cachePort = slaveInfo.getCachePort();
		Integer agentPort = slaveInfo.getAgentPort();

		String masterIp = masterInfo.getCacheHostIp();
		Integer masterPort = masterInfo.getCachePort();

		/** 初始化agent  **/
		AgentClient ac = new AgentClient(slaveIp, agentPort);
		
		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, cachePort);
		
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + cachePort + "/" + "redis-" + cachePort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + cachePort + ".pid" + "\n"
					+ "port " + cachePort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + cachePort + ".log\n"
					+ "slaveof " + masterIp + " " + masterPort + "\n"
					+ "masterauth " + requirepass + "\n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
			
		startMcsIns(ac, commonconfigPath, cachePort);
		logger.info("---------启动redis成功!");
	}

	/**
	 * sentinal模式中，生成sentinel节点配置文件，并启动sentinel节点
	 * @param value
	 * @param master
	 * @throws PaasException
	 */
	private void configSentinel(McsProcessInfo value, McsProcessInfo master) throws PaasException {
		
		String cacheHost = value.getCacheHostIp();
		String cachePath = value.getCachePath();
		Integer cachePort = value.getCachePort();
		Integer agentPort =value.getAgentPort();
		
		/** 获取master节点的ip和端口 **/
		String masterIp = master.getCacheHostIp();
		Integer masterPort = master.getCachePort();
		
		/** 初始化agent  **/
		AgentClient ac = new AgentClient(cacheHost, agentPort);
		
		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, cachePort);
		
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + cachePort + "/" + "redis-" + cachePort + "-sentinel.conf";
			String configDetail = "port " + cachePort + "\n"
					+ "sentinel monitor mymaster " + masterIp + " " + masterPort + " 2 \n"
					+ "sentinel down-after-milliseconds mymaster 5000 \n"
					+ "sentinel failover-timeout mymaster 60000 \n"
					+ "sentinel parallel-syncs mymaster 1 \n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
		
		startSenIns(ac, commonconfigPath, cachePort);
		logger.info("3----------------启动redis成功!");
	}

	/**
	 * 上传配置文件，执行命令
	 * 
	 * @param mcsResourcePool
	 * @param capacity
	 * @param requirepass
	 * @throws PaasException
	 */
	private void addMcsConfig(AgentClient ac, String cachePath, int redisPort, String capacity, String requirepass) 
			throws PaasException {
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + redisPort + "/" + "redis-" + redisPort + ".conf";
			String configDetail = "include " + cachePath + McsConstants.FILE_PATH + "redis-common.conf" + "\n"
					+ "pidfile /var/run/redis-" + redisPort + ".pid" + "\n"
					+ "port " + redisPort + "\n"
					+ "maxmemory " + capacity + "m" + "\n"
					+ "requirepass " + requirepass + "\n"
					+ "logfile " + cachePath + "/redis/log/redis-" + redisPort + ".log";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			logger.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);
		}
	}

	/**
	 * 停redis，修改mcs服务端的配置文件，修改用户实例，启动redis
	 * 
	 * @param userInstanceList
	 * @param cacheSize
	 * @throws PaasException
	 */
	//TODO: 与 removeMcsServerFileAndUserIns() 方法类似，一起考虑重构。
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
			
			logger.info("------------- redis info cacheHostIp:["+cacheHostIp+"]-----------");
			logger.info("------------- redis info cachePort:["+cachePort+"]---------------");
			logger.info("------------- redis info requirepass:["+requirepass+"]-----------");
			logger.info("------------- redis info agentPort:["+agentPort+"]---------------");
			logger.info("------------- redis info commonconfigPath:["+commonconfigPath+"]-");
			
			/** 1.获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 2.停redis  **/
			logger.info("----- stop redis -----");
			stopMcsIns(ac, cachePort);
			
			/** 3.修改redis的配置文件  **/
			logger.info("----- modify redis config -----requirepass：" + requirepass);
			addMcsConfig(ac, cachePath, cachePort, cacheSize + "", requirepass);

			/** 4.启动redis **/
			logger.info("----- start redis -----");
			startMcsIns(ac, commonconfigPath, cachePort);
			
			/** 5.组织待更新的数据 **/
			logger.info("------- cacheSize："+cacheSize);
			tempIns.setCacheMemory(cacheSize);
			if (serviceName != null && serviceName.length() > 0){
				tempIns.setServiceName(serviceName);
			}
			
			/** 6.更新用户实例表的“已使用的内存”的字段  **/
			logger.info("----- update cache size info -------");
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(tempIns);
			
		} else {  /** 处理集群模式的redis **/
			McsResourcePool pool = null;
			List<McsProcessInfo> cacheInfoList = new ArrayList<McsProcessInfo>();
			for(McsUserCacheInstance tempIns : userInstanceList){
				/** 获取mcs资源表中的cachePath、agentPort信息 **/
				McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
				
				String cachePath = pool.getCachePath();
				Integer agentPort = Integer.parseInt(pool.getAgentCmd());
				String cacheHostIp = tempIns.getCacheHost();
				Integer cachePort = tempIns.getCachePort();
				
				/** 将集群配置文件、集群启动所需的信息，放到value中。 **/
				McsProcessInfo value = new McsProcessInfo();
				value.setCacheHostIp(cacheHostIp);
				value.setCachePort(cachePort);
				value.setAgentPort(agentPort);
				value.setCachePath(cachePath);
				cacheInfoList.add(value);
				
				logger.info("------- redis info cacheHostIp:["+cacheHostIp+"]---------");
				logger.info("------- redis info cachePort:["+cachePort+"]-------------");
				logger.info("------- redis info agentPort:["+agentPort+"]-------------");
				logger.info("------- redis info cachePath:["+cachePath+"]-------------");
				
				/** 获取agent客户端 **/
				AgentClient ac = new AgentClient(cacheHostIp, agentPort);
				
				/** 停redis  **/
				stopMcsIns(ac, cachePort);
				logger.info("----- stop cluster redis is successful-----");
				
				/** 组织待更新的数据 **/
				tempIns.setCacheMemory(cacheSize);
				if (serviceName != null && serviceName.length() > 0) {
					tempIns.setServiceName(serviceName);
				}
				
				/** 更新用户实例表 **/
				logger.info("----- update cache size info -------");
				McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
				im.updateByPrimaryKey(tempIns);
			}
			
			/** 处理redis集群中的每个server的目录、配置文件，并启动。 **/
			addMcsConfigCluster(cacheInfoList, userId, serviceId, cacheSize);
			
			/** 在集群中的任意台主机上，执行redis集群创建的命令 **/
			McsProcessInfo vo = cacheInfoList.get(0);
			AgentClient ac = new AgentClient(vo.getCacheHostIp(), vo.getAgentPort());
			
			/** 组织集群创建的命令及返回值 **/
			String clusterInfo = getClusterInfo(cacheInfoList, " ");
			String create_cluster = "redis-trib.rb create --replicas 1" + clusterInfo;
			logger.info("-------- 创建redis集群的命令:" + create_cluster);
			
			/** 创建redis集群 **/
			mcsSvHepler.excuteCommand(ac, create_cluster);
			logger.info("-------- 创建redis集群成功 --------");
		}
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
			 * 规则：“扩容”->“加值”；
			 * 		“注销”->“减值”。
			 * **/
			if(isAdd) {
				pool.setCacheMemoryUsed(curentCacheSize + cacheSize);
			} else {
				pool.setCacheMemoryUsed(pool.getCacheMemory() - curentCacheSize);
			}
			
			mapper.updateByExampleSelective(pool, condition);
		}
	}

	/**
	 * 获得最空闲的Mcs资源
	 * @param num
	 * @return
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
	 * @param host
	 * @return McsUserCacheInstance
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
	 * @param mcsResourcePool
	 * @return
	 */
	private int updateResource(McsResourcePool mcsResourcePool, int portOffset) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePoolCriteria condition = new McsResourcePoolCriteria();
		/** TODO:端口偏移量，需要重点验证。**/
		condition.createCriteria().andIdEqualTo(mcsResourcePool.getId())
			.andCachePortEqualTo(mcsResourcePool.getCachePort() - portOffset); 
		return rpm.updateByExampleSelective(mcsResourcePool, condition);
	}

	/**
	 * 停止Mcs服务，在管理控制台的“停止”／“重启”的功能中调用。
	 * @param userInstanceList
	 * @throws PaasException
	 */
	private void stopMcsInsForCacheIns(List<McsUserCacheInstance> userInstanceList) throws PaasException {
		McsResourcePool pool = null;
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		for(McsUserCacheInstance tempIns: userInstanceList) {
			if (pool == null) {
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
						.andCacheHostIpEqualTo(tempIns.getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
			}
			
			String cacheHostIp = pool.getCacheHostIp();
			Integer cachePort = tempIns.getCachePort();
			Integer agentPort = Integer.parseInt(pool.getAgentCmd());
			logger.info("------- redis info cacheHostIp:["+cacheHostIp+"]--------");
			logger.info("------- redis info cachePort:["+cachePort+"]--------");
			logger.info("------- redis info agentPort:["+agentPort+"]--------");
		
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			stopMcsIns(ac, tempIns.getCachePort());
		}
	}

	/**
	 * 启动mcs服务
	 * @param userId
	 * @param serviceId
	 * @param userInstance
	 * @throws PaasException
	 */
	private void startMcs(String userId, String serviceId, List<McsUserCacheInstance> userInstance) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePool pool = null;
		
		String clusterInfo = "";
		for (McsUserCacheInstance tempIns :userInstance) {
			if (pool == null) {
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS).andCacheHostIpEqualTo(tempIns.getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
			}
			
			String cachePath = pool.getCachePath();
			String cacheHostIp = pool.getCacheHostIp();
			Integer cachePort = tempIns.getCachePort();
			Integer agentPort = Integer.parseInt(pool.getAgentCmd());
			String commonconfigPath = cachePath + McsConstants.FILE_PATH;
			logger.info("------- redis info cacheHostIp:["+cacheHostIp+"]-----------");
			logger.info("------- redis info cachePort:["+cachePort+"]-----------");
			logger.info("------- redis info agentPort:["+agentPort+"]-----------");
			logger.info("------- redis info commonconfigPath:["+commonconfigPath+"]-------");
			
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			if (userInstance.size() == 1) {
				startMcsIns(ac, commonconfigPath, cachePort);
			} else {
				startMcsInsForCluster(ac, cachePath, userId + "_" + serviceId, pool.getCachePath(), tempIns.getCachePort());
			}
			
			/** 生成集群的 ip:port 串，用于拼装创建集群的命令。 **/
			clusterInfo += " " + tempIns.getCacheHost() + ":" + tempIns.getCachePort();
		}
		
		/** 如果Mcs服务为集群模式，需要执行创建redis集群的命令。 **/
		if (userInstance.size() > 1) {
			String cacheHost = userInstance.get(0).getCacheHost();
			Integer agentPort = Integer.valueOf(pool.getAgentCmd());
			AgentClient ac = new AgentClient(cacheHost, agentPort);
			
			logger.info("------创建["+userId+"]-["+serviceId+"]的MCS集群:"+clusterInfo);
			createRedisCluster(ac, clusterInfo);
		}
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
			logger.info("-------- removeMcsServerFileAndUserIns ---single-----");
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
			logger.info("------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
			logger.info("------- redis info cachePort:["+cachePort+"]-------------");
			logger.info("------- redis info requirepass:["+requirepass+"]-------------");
			logger.info("------- redis info agentPort:["+agentPort+"]-------------");
			logger.info("------- redis info commonconfigPath:["+commonconfigPath+"]--------");
			
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			/** 停redis  **/
			logger.info("----- stop redis ["+cacheHostIp+"]:["+cachePort+"]  -----");
			stopMcsIns(ac, cachePort);
			
			/** 删除redis的配置文件  **/
			logger.info("----- delete redis config file-----");
			removeMcsConfig(ac, commonconfigPath, agentPort);
			
			/** 删除zk的配置**/
			logger.info("------ delete config for ["+userId+"].["+serviceId+"] ------");
			CCSComponentOperationParam op = new CCSComponentOperationParam();
			op.setUserId(userId);
			op.setPath("/MCS/" + serviceId);
			op.setPathType(PathType.READONLY);
			iCCSComponentManageSv.delete(op);
			logger.info("------ delete ["+userId+"].["+serviceId+"]'s config successful! ------");
			
			/** 更新用户实例表，状态为2. **/
			logger.info("------- update mcs_user_instance status is ["+McsConstants.INVALIDATE_STATUS+"]");
			tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
			McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
			im.updateByPrimaryKey(tempIns);

		} else {
			logger.info("-------- removeMcsServerFileAndUserIns ---cluster-----");
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
				logger.info("------- redis info cacheHostIp:["+cacheHostIp+"] ----------");
				logger.info("------- redis info cachePort:["+cachePort+"] ----------");
				logger.info("------- redis info agentPort:["+agentPort+"] ----------");
				logger.info("------- redis info commonconfigPath:["+commonconfigPath+"]-----------");
				
				/** 获取agent客户端 **/
				AgentClient ac = new AgentClient(cacheHostIp, agentPort);
				
				/** 停redis  **/
				logger.info("----- stop redis ["+cacheHostIp+"]:["+cachePort+"]  -----");
				stopMcsIns(ac, cachePort);
				
				/** 删除redis的配置文件  **/
				logger.info("----- delete redis[cluster] config file -----");
				removeMcsConfig(ac, commonconfigPath, agentPort, userId, serviceId);
				
				/** 删除zk的配置 **/
				logger.info("----- delete zk config for ["+userId+"].["+serviceId+"] ---cluster-----");
				CCSComponentOperationParam op = new CCSComponentOperationParam();
				op.setUserId(userId);
				op.setPath("/MCS/" + serviceId);
				op.setPathType(PathType.READONLY);
				iCCSComponentManageSv.delete(op);
				logger.info("----- delete zk info ["+userId+"].["+serviceId+"]'s config successful! ------");

				/** 更新用户实例表，状态为2. **/
				tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
				McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
				im.updateByPrimaryKey(tempIns);
				logger.info("----- update mcs_user_instance status is ["+McsConstants.INVALIDATE_STATUS+"]");
			}
		}
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
			logger.error(e.getMessage(), e);
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
	private void startMcsInsForCluster(AgentClient ac, String cachePath, String dir, String cPath, int port) throws PaasException {
		try {
			String cmd_rm =  "cd "+cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+"|rm -fr appendonly.aof  dump.rdb  nodes.conf ";
			String cmd =  "redis-server " + cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+ "/redis-" + port + ".conf > redis.log &";
			ac.executeInstruction(cmd_rm);
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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
			String cmd =  "redis-sentinel " + path + port + "/redis-" + port + "-sentinel.conf &";
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("sentinel模式启动MCS异常，port：" + port, e);
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
			logger.error(e.getMessage(), e);
			throw new PaasException("++++++++ 停止Redis异常，port：" + port, e);
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
		try {
			String file = commonconfigPath + "redis-" + cachePort + ".conf";
			String cmd = "rm " + file;
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("删除redis的配置文件异常，fileInfo:" + commonconfigPath+cachePort, e);
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
		try {
			String file = commonconfigPath + userId + "_" + serviceId + "/redis-" + cachePort + ".conf";
			String cmd = "rm " + file;
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("集群--删除redis的配置文件异常，fileInfo：" + commonconfigPath+cachePort, e);
		}
	}

	/**
	 * 执行Redis集群创建的命令。
	 * @param ac
	 * @param clusterInfo
	 * @throws PaasException
	 */
	private void createRedisCluster(AgentClient ac, String clusterInfo) throws PaasException {
		try {
			String cmd_create_cluster = "redis-trib.rb create --replicas 1" + clusterInfo;
			ac.executeInstruction(cmd_create_cluster);
			logger.info("----------创建Redis集群["+clusterInfo+"]成功----------");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new PaasException("++++ 创建Redis集群["+clusterInfo+"]失败：" + e.getMessage(), e);
		}
	}
}
