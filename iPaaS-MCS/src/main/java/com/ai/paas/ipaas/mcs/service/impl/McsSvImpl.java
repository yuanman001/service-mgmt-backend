package com.ai.paas.ipaas.mcs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
	private static transient final Logger log = LoggerFactory.getLogger(McsSvImpl.class);

	@Autowired 
	private McsSvHepler mcsSvHepler;
	
	@Autowired 
	private ICCSComponentManageSv iCCSComponentManageSv;	

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
			List<McsProcessInfo> cacheInfoList = selectMcsResCluster(clusterCacheSize, McsConstants.CACHE_NUM);
			
			/** 组织集群创建的命令及返回值 **/
			String clusterInfo = getClusterInfo(cacheInfoList, " ");
			
			/** 在集群中的任意台主机上，执行redis集群创建的命令 **/
			McsProcessInfo vo = cacheInfoList.get(0);
			AgentClient ac = new AgentClient(vo.getCacheHostIp(), vo.getAgentPort());
			
			String create_cluster = "redis-trib.rb create --replicas 1" + clusterInfo;
			log.info("-------- 创建redis集群的命令:" + create_cluster);
			mcsSvHepler.excuteCommand(ac, create_cluster);
			log.info("-------- 创建redis集群成功 --------");
			
			String hostsCluster = getClusterInfo(cacheInfoList, ";");
			log.info("---------- hostsCluster is :" + hostsCluster);
			
			// 添加zk配置
			log.info("4----------------处理zk 配置");
			addCcsConfig(userId, serviceId, hostsCluster.substring(1), null);
			
			log.info("5----------------处理用户实例");
			/** TODO:需重构，传入值对象，只操作一次数据库。 **/
			for (McsProcessInfo cacheInfo : cacheInfoList) {
				addUserInstance(userId, serviceId, clusterCacheSize + "", cacheInfo.getCacheHostIp(), 
						cacheInfo.getCachePort(), null, serviceName);
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
	 * @param cacheInfoList
	 * @param userId
	 * @param serviceId
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	private void addMcsConfigCluster(List<McsProcessInfo> cacheInfoList,
			String userId, String serviceId, int cacheSize)throws PaasException {
		
		for (McsProcessInfo proInfo : cacheInfoList) {
			String host = proInfo.getCacheHostIp();
			String cachePath = proInfo.getCachePath();
			Integer cachePort = proInfo.getCachePort();
			Integer agentPort = proInfo.getAgentPort();
			String clusterPath = cachePath + McsConstants.CLUSTER_FILE_PATH;
			String logPath = cachePath + McsConstants.LOG_PATH;

			log.info("-------- 开始处理集群配置文件的time：" + new Date());

			/** 初始化Agentclint **/
			AgentClient ac = new AgentClient(host, agentPort);

			/** 创建redis集群的目录: ./redis/cluster/user_id+service_id **/
			String mkdir_cluster = "mkdir -p " + clusterPath + userId + "_" + serviceId;
			mcsSvHepler.excuteCommand(ac, mkdir_cluster);

			/**
			 * 创建redis集群下的server目录: ./redis/cluster/user_id+service_id/redisPort
			 **/
			String mkdir_port = "mkdir -p " + clusterPath + userId + "_" + serviceId + "/" + cachePort;
			mcsSvHepler.excuteCommand(ac, mkdir_port);

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
			log.info("-------- 处理集群配置文件结束的time：" + new Date());
			log.info("-------- 上传配置文件成功！---------");

			/** 启动集群中的redis-server **/
			String cmd_start = "redis-server " + configFile + " > " + logPath + "redis-" + cachePort + ".log &";
			mcsSvHepler.excuteCommand(ac, cmd_start);
			log.info("-------- 启动redis[" + host + ":" + cachePort + "]成功 --------");
		}
	}

	private String getClusterInfo(List<McsProcessInfo> list, String separator) throws PaasException {
		String cluster = "";
		for(McsProcessInfo vo: list) {
			cluster += vo.getCacheHostIp() + ":" + vo.getCachePort() + separator;
		}
		return cluster;
	}
	
	/**
	 * 集群模式，选择资源池
	 * 
	 * @param cacheSize
	 * @return
	 * @throws PaasException
	 */
	//TODO:需要删除此方法，统一用返回list<McsProcessInfo>的方法。
	private List<String> selectMcsRessCluster(int cacheSize, int size)
			throws PaasException {
		
		List<McsResourcePool> cacheResourceList = getBestResource(size);
		String agentFile = cacheResourceList.get(0).getAgentFile();
		String agentCmd = cacheResourceList.get(0).getAgentCmd();
		String cachePath = cacheResourceList.get(0).getCachePath();
		
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
	 * 选择开通Mcs集群资源
	 * @param cacheSize
	 * @param size
	 * @return
	 * @throws PaasException
	 */
	//TODO:此方法的逻辑较复杂，需重构。
	private List<McsProcessInfo> selectMcsResCluster(int cacheSize, int size)
			throws PaasException {
		
		List<McsResourcePool> cacheResourceList = getBestResource(size);
		String agentCmd = cacheResourceList.get(0).getAgentCmd();
		String cachePath = cacheResourceList.get(0).getCachePath();
		
		int hostNum = cacheResourceList.size();
		int i = 0;
		int j = 1;
		int k = hostNum;
		
		List<McsProcessInfo> cacheInfoList = new ArrayList<McsProcessInfo> ();
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
				
				Integer agentPort = Integer.parseInt(cacheResourceList.get(m).getAgentCmd());
				
				McsProcessInfo vo = new McsProcessInfo();
				vo.setCacheHostIp(ip);
				vo.setCachePath(cachePath);
				vo.setCachePort(port);
				vo.setAgentPort(agentPort);
				cacheInfoList.add(vo);
				
				i++;
				log.info("2----------------选择主机:" + ip + "端口：" + port);
			}
			
			count++;
		}
		if (count > size)
			throw new PaasException("mcs resource not enough.");
		
		return cacheInfoList;
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

	//TODO: 需要重构，调整逻辑，抽象方法。
	private void addMasterConfig(McsResourcePool mcsResourcePool, int redisPort, 
			String capacity, String requirepass) throws PaasException {
		
		String cacheHostIp = mcsResourcePool.getCacheHostIp();
		String cachePath = mcsResourcePool.getCachePath();
		
		/** 初始化agent  **/
		AgentClient ac = new AgentClient(cacheHostIp, redisPort);
		
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
			log.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
			
		//TODO: 是否需要上传空的日志文件？？？？
//		uploadCacheFile(uri, logfile);

		/** 启动redis TODO: 需要放到外层的业务逻辑中。 **/
		startMcsIns(ac, commonconfigPath, redisPort);
		log.info("---------启动redis成功!");
	}

	private void configSenMaster(String result, String capacity, String requirepass) throws PaasException {
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];
		Integer redisPort = Integer.parseInt(port);
		
		//TODO:需要确认此port，是否为 agent port ？？？
		//TODO:此端口应该是 redis的port，需要获取 agentPort。
		AgentClient ac = new AgentClient(ip, 00000);

		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, redisPort);
		
		//TODO: 是否需要创建空的日志文件夹。
		//executeInstruction(uriCmdCluster, logdir);
		//TODO：生成日志文件??? 为什么要生成一个空的日志文件？？是否可以去掉？？
		//uploadCacheFile(uriCreateCluster, logfile);
		//mcsSvHepler.uploadFile(ac, logName, " ");
		
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
			log.info("-------------上传文件成功!");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		} 

		/** 启动redis TODO: 需要放到外层的业务逻辑中。 **/
		startMcsIns(ac, commonconfigPath, redisPort);
		log.info("-------------启动redis成功!");
	}

	//TODO: 重构此方法，拆分逻辑；调用agent的部分需要调整。
	private void addSlaveConfig(McsResourcePool mcsResourcePool, int redisPort,
			String capacity, String requirepass) throws PaasException {
		
		String cacheHostIp = mcsResourcePool.getCacheHostIp();
		String cachePath = mcsResourcePool.getCachePath();
		
		//TODO:需要从资源池表的 agentCmd字段获取 agentPort，需重新初始化数据，重点验证！！
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
			log.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
			
		//TODO: 是否需要上传空的日志文件？？？？
//		uploadCacheFile(uri, logfile);

		/** 启动redis TODO: 需要放到外层的业务逻辑中。 **/
		startMcsIns(ac, commonconfigPath, redisPort);
		log.info("---------启动redis成功!");
	}

	//TODO: 同 addSlaveConfig 一样，需要重构，主要解决 agent 调用的逻辑，同时整理并重构逻辑。
	private void configSenSlave(String result, String masterResult, String capacity, String requirepass) throws PaasException {
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];
		Integer redisPort = Integer.parseInt(port);

		String[] masterInfo = masterResult.split(":");
		String masterIp = masterInfo[0];
		String masterPort = masterInfo[1];

		String cacheHostIp = ip;
		
		//TODO:需要确认此port，是否为 agent port ？？？
		//TODO:此端口应该是 redis的port，需要获取 agentPort。
		Integer agentPort = 0;

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
					+ "slaveof " + masterIp + " " + masterPort + "\n"
					+ "masterauth " + requirepass + "\n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			log.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
			
		//TODO: 是否需要上传空的日志文件？？？？
//		uploadCacheFile(uri, logfile);

		/** 启动redis TODO: 需要放到外层的业务逻辑中。 **/
		startMcsIns(ac, commonconfigPath, redisPort);
		log.info("---------启动redis成功!");
	}

	//TODO: 需要重构，提炼部分逻辑，抽象出共用方法。
	private void configSentinel(String result, String master) throws PaasException {
		// 获取当前节点的ip和端口
		String[] info = result.split(":");
		String ip = info[0];
		String port = info[1];
		Integer redisPort = Integer.parseInt(port);
		
		// 获取监控节点的ip和端口
		String[] masterInfo = master.split(":");
		String masterIp = masterInfo[0];
		String masterPort = masterInfo[1];
		
		String cacheHostIp = ip;
		
		//TODO:需要确认此port，是否为 agent port ？？？
		//TODO:此端口应该是 redis的port，需要获取 agentPort。
		Integer agentPort = 0;

		/** 初始化agent  **/
		AgentClient ac = new AgentClient(cacheHostIp, agentPort);
		
		/** 创建端口号命名的文件夹  **/
		String commonconfigPath = cachePath + McsConstants.FILE_PATH;
		addConfigFolder(ac, commonconfigPath, redisPort);
		
		try {
			String fileName = cachePath + McsConstants.FILE_PATH + redisPort + "/" + "redis-" + redisPort + "-sentinel.conf";
			String configDetail = "port " + redisPort + "\n"
					+ "sentinel monitor mymaster " + masterIp + " " + masterPort + " 2 \n"
					+ "sentinel down-after-milliseconds mymaster 5000 \n"
					+ "sentinel failover-timeout mymaster 60000 \n"
					+ "sentinel parallel-syncs mymaster 1 \n";
			
			mcsSvHepler.uploadFile(ac, fileName, configDetail);
			log.info("---------新生成的redis配置文件，上传成功!");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("上传文件失败：" + e.getMessage(), e);
		}
		
		/** 启动redis TODO: 需要放到外层的业务逻辑中。 **/
		startSenIns(ac, commonconfigPath, redisPort);
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
			List<McsProcessInfo> cacheInfoList = new ArrayList<McsProcessInfo>();
			final List<String> resultList = new ArrayList<String>();
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
			
			/** 处理redis集群中的每个server的目录、配置文件，并启动。 **/
			addMcsConfigCluster(cacheInfoList, userId, serviceId, cacheSize);
			
			/** 组织集群创建的命令及返回值 **/
			String clusterInfo = getClusterInfo(cacheInfoList, " ");
			
			/** 在集群中的任意台主机上，执行redis集群创建的命令 **/
			McsProcessInfo vo = cacheInfoList.get(0);
			AgentClient ac = new AgentClient(vo.getCacheHostIp(), vo.getAgentPort());
			
			String create_cluster = "redis-trib.rb create --replicas 1" + clusterInfo;
			log.info("-------- 创建redis集群的命令:" + create_cluster);
			mcsSvHepler.excuteCommand(ac, create_cluster);
			log.info("-------- 创建redis集群成功 --------");
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
	 * 执行服务器端命令
	 * 
	 * @param uri
	 * @param cmd
	 * @return
	 * @throws PaasException
	 */
	//TODO: 重新构造此方法，使用新的AgentClint类及其方法。
//	private String executeInstruction(String uri, String cmd) throws PaasException {
//		String result = null;
//		try {
//			ClientResource clientResource = new ClientResource(uri);
//			Representation representation = clientResource.post(cmd);
//			result = representation.getText();
//			clientResource.release();
//			Thread.sleep(10);
//		} catch (Exception e) {
//			log.error(e.getMessage(), e);
//			throw new PaasException("", e);
//		}
//		return result;
//	}

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
	private void stopMcsInsForCacheIns(List<McsUserCacheInstance> userInstanceList) throws PaasException {
		McsResourcePool pool = null;
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		for(McsUserCacheInstance tempIns: userInstanceList) {
			if (pool == null) {
				McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
				rpmc.createCriteria()
						.andStatusEqualTo(McsConstants.VALIDATE_STATUS)
						.andCacheHostIpEqualTo(tempIns.getCacheHost());
				List<McsResourcePool> pools = rpm.selectByExample(rpmc);
				pool = pools.get(0);
			}
			
			String cacheHostIp = pool.getCacheHostIp();
			Integer cachePort = tempIns.getCachePort();
			Integer agentPort = Integer.parseInt(pool.getAgentCmd());
			log.info("------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
			log.info("------- redis info cachePort:["+cachePort+"]-------------");
			log.info("------- redis info agentPort:["+agentPort+"]-------------");
		
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			stopMcsIns(ac, tempIns.getCachePort());
		}
	}

	@Override
	public String startMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (cis == null || cis.isEmpty()) {
			throw new PaasException("该服务未开通过，无法启动！");
		}
		
		startMcs(userId, serviceId, cis);
		
		return McsConstants.SUCCESS_FLAG;
	}

	private void startMcs(String userId, String serviceId, List<McsUserCacheInstance> userInstance) throws PaasException {
		McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
		McsResourcePool pool = null;
		
		String ipPorts = "";
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
			log.info("------- redis info cacheHostIp:["+cacheHostIp+"]-------------");
			log.info("------- redis info cachePort:["+cachePort+"]-------------");
			log.info("------- redis info agentPort:["+agentPort+"]-------------");
			log.info("------- redis info commonconfigPath:["+commonconfigPath+"]-------------");
			
			/** 获取agent客户端 **/
			AgentClient ac = new AgentClient(cacheHostIp, agentPort);
			
			ipPorts += " " + tempIns.getCacheHost() + ":" + tempIns.getCachePort();
			if (userInstance.size() == 1) {
				startMcsIns(ac, commonconfigPath, cachePort);
			} else {
				//TODO: 需重点验证。另外，是否可以提到外层逻辑中执行？？？
				startMcsInsForCluster(ac, userId + "_" + serviceId, pool.getCachePath(), tempIns.getCachePort());
			}
		}
		
		//TODO:????? 此处的创建集群，可考虑定义成一个方法。
		if (userInstance.size() > 1) {
			String listCmdCluster = "redis-trib.rb create --replicas 1" + ipPorts;
			try {
				//TODO: 此处的 agentPort，需要从 AgentCmd 字段获取。
				AgentClient ac = new AgentClient(userInstance.get(0).getCacheHost(), 0000000);
				ac.executeInstruction(listCmdCluster);
				log.info("集群启动成功");
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new PaasException("集群启动失败：" + e.getMessage(), e);
			}
		}
	}

	@Override
	public String restartMcs(String param) throws PaasException {
		// 获取服务号配置参数
		Map<String, String> map = McsParamUtil.getParamMap(param);
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (cis == null || cis.isEmpty()){
			throw new PaasException("该服务未开通过，无法重启！");
		}
		
		stopMcsInsForCacheIns(cis);
		
		startMcs(userId, serviceId, cis);
		
		return McsConstants.SUCCESS_FLAG;
	}

	@Override
	public String cancelMcs(String param) throws PaasException {
		Map<String, String> map = McsParamUtil.getParamMap(param);
		final String serviceId = map.get(McsConstants.SERVICE_ID);
		final String userId = map.get(McsConstants.USER_ID);
		log.info("注销----------------获得已申请过的记录");
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
		if (cis == null || cis.isEmpty()) {
			throw new PaasException("该服务未开通过，无法注销！");
		}
		
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
			log.error(e.getMessage(), e);
			throw new PaasException("sentinel模式启动MCS异常，port：" + port, e);
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

	/**
	 * 使用新的agent接口，启动redis服务。
	 * @param ac
	 * @param path
	 * @param port
	 * @throws PaasException
	 */
	private void startMcsInsForCluster(AgentClient ac, String dir, String cPath, int port) throws PaasException {
		try {
			String cmd_rm =  "cd "+cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+"|rm -fr appendonly.aof  dump.rdb  nodes.conf ";
			String cmd =  "redis-server " + cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
					+ "/redis-" + port + ".conf > redis.log &";
			ac.executeInstruction(cmd_rm);
			ac.executeInstruction(cmd);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new PaasException("启动MCS异常，port：" + port, e);
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
