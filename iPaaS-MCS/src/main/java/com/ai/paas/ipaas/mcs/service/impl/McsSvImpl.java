package com.ai.paas.ipaas.mcs.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private ICCSComponentManageSv iCCSComponentManageSv;

  @Autowired
  private McsSvHepler mcsSvHepler;

  // 代理 上传文件的地址
  private String agentFile = "";
  // 代理 执行CMD命令的地址
  private String agentCmd = "";
  // 服务端 用户路径
  private String cachePath = "";

  @Override
  public String openMcs(String param) throws PaasException {
    Map<String, String> map = McsParamUtil.getParamMap(param);
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_C.equals(applyType)) throw new PaasException("缓存开通，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    String capacity = map.get(McsConstants.CAPACITY);
    String haMode = map.get(McsConstants.HA_MODE);
    final int cacheSize = Integer.valueOf(capacity);
    String serviceName = map.get(McsConstants.SERVICE_NAME);

    // 判断用户的这个缓存服务是否已经开通
    if (existsService(userId, serviceId)) {
      log.info("----------------用户服务已存在，开通成功");
      return McsConstants.SUCCESS_FLAG;
    }

    if (McsConstants.MODE_SINGLE.equals(haMode)) {
      log.info("1----------------进入单实例选择主机");
      // --------选择剩余内存最多的主机 缓存资源
      McsResourcePool mcsResourcePool = selectMcsResourcePool(cacheSize);
      log.info("2----------------选择主机:" + mcsResourcePool.getCacheHostIp() + "端口："
          + mcsResourcePool.getCachePort());
      // 根据用户账号（user_id）随机产生redis密码
      Random rand = new Random();
      int i = rand.nextInt(900000) + 100000;
      String requirepass = i + "";
      log.info("3----------------处理mcs服务端");
      // 处理mcs服务端配置文件
      addMcsConfig(mcsResourcePool, mcsResourcePool.getCachePort(), capacity, requirepass);
      // 处理zk 配置
      log.info("4----------------处理zk 配置");
      addCcsConfig(userId, serviceId,
          mcsResourcePool.getCacheHostIp() + ":" + mcsResourcePool.getCachePort(), requirepass);
      // -------mcs_user_cache_instance表新增记录
      log.info("5----------------处理用户实例");
      addUserInstance(userId, serviceId, capacity, mcsResourcePool.getCacheHostIp(),
          mcsResourcePool.getCachePort(), requirepass, serviceName);

    } else if (McsConstants.MODE_CLUSTER.equals(haMode)) {
      log.info("1----------------进入集群选择主机");
      final int clusterCacheSize = Math.round(cacheSize / McsConstants.CACHE_NUM * 2);
      // 选取 资源池
      final List<String> resultList =
          selectMcsRessCluster(clusterCacheSize, McsConstants.CACHE_NUM);
      // 处理mcs服务端文件等
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.execute(new Runnable() {
        public void run() {
          try {
            log.info("3----------------处理mcs服务端");
            String hostsCluster =
                addMcsConfigCluster(resultList, userId, serviceId, clusterCacheSize);
            // 添加zk配置
            log.info("4----------------处理zk 配置");
            addCcsConfig(userId, serviceId, hostsCluster.substring(1), null);
          } catch (Exception e) {
            log.error(e.getMessage(), e);
          }
        }
      });

      log.info("5----------------处理用户实例");
      for (String address : resultList) {
        String[] add = address.split(":");
        // -------mcs_user_cache_instance表新增记录
        addUserInstance(userId, serviceId, clusterCacheSize + "", add[0], Integer.parseInt(add[1]),
            null, serviceName);
      }

    } else if (McsConstants.MODE_REPLICATION.equals(haMode)) {
      log.info("1----------------进入主从模式选择主机");
      // --------选择剩余内存最多的主机 缓存资源
      McsResourcePool mcsResourcePool = selectMcsRepResource(cacheSize * 2);
      log.info("2----------------选择主机:" + mcsResourcePool.getCacheHostIp() + "端口："
          + mcsResourcePool.getCachePort());
      // 根据用户账号（user_id）随机产生redis密码
      Random rand = new Random();
      int i = rand.nextInt(900000) + 100000;
      String requirepass = i + "";
      log.info("3----------------处理mcs服务端");
      // 处理mcs服务端配置文件
      // addMcsConfig(mcsResourcePool, mcsResourcePool.getCachePort(), capacity, requirepass);
      addMasterConfig(mcsResourcePool, mcsResourcePool.getCachePort(), capacity, requirepass);
      addSlaveConfig(mcsResourcePool, mcsResourcePool.getCachePort() - 1, capacity, requirepass);
      // 处理zk 配置
      log.info("4----------------处理zk 配置");
      addCcsConfig(userId, serviceId,
          mcsResourcePool.getCacheHostIp() + ":" + mcsResourcePool.getCachePort(), requirepass);
      // -------mcs_user_cache_instance表新增记录
      log.info("5----------------处理用户实例");
      addUserInstance(userId, serviceId, capacity, mcsResourcePool.getCacheHostIp(),
          mcsResourcePool.getCachePort(), requirepass, serviceName);

    } else if (McsConstants.MODE_SENTINEL.equals(haMode)) {
      log.info("1----------------进入集群选择主机");
      final int clusterCacheSize = Math.round(cacheSize / McsConstants.CACHE_NUM * 2);
      // 选取 资源池
      final List<String> resultList =
          selectMcsRessCluster(clusterCacheSize, McsConstants.SENTINEL_NUM);
      log.info("2----------------选择主机:" + resultList.toString());
      log.info("3----------------处理mcs服务端");
      // 选取第一项为主，二三项为从，四五六启动sentinel进程
      Random rand = new Random();
      int i = rand.nextInt(900000) + 100000;
      String requirepass = i + "";
      String masterInfo = resultList.get(0);
      configSenMaster(masterInfo, capacity, requirepass);
      for (int k = 1; k <= 2; k++) {
        configSenSlave(resultList.get(k), resultList.get(0), capacity, requirepass);
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
        addUserInstance(userId, serviceId, clusterCacheSize + "", add[0], Integer.parseInt(add[1]),
            null, serviceName);
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
    McsResourcePool mcsResourcePool = resp.get(0);
    // 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号
    if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
      mcsResourcePool.setCachePort(getCanUseInstance(mcsResourcePool.getCacheHostIp())
          .getCachePort());
    } else {
      if (mcsResourcePool.getCachePort() == mcsResourcePool.getMaxPort()) {
        mcsResourcePool.setCycle(1);
      }
      mcsResourcePool.setCachePort(mcsResourcePool.getCachePort() + 2);
      mcsResourcePool.setCacheMemoryUsed(mcsResourcePool.getCacheMemoryUsed() + cacheSize);
      int changeRow = updateMcsResource(mcsResourcePool);

      if (changeRow != 1) {
        throw new PaasException("更新资源失败");
      }

    }
    cachePath = mcsResourcePool.getCachePath();
    return mcsResourcePool;
  }

  private int updateMcsResource(McsResourcePool mcsResourcePool) throws PaasException {
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
  private String addMcsConfigCluster(List<String> resultList, String userId, String serviceId,
      int cacheSize) throws PaasException {
    String redisIpCluster = null;
    String redisPortCluster = null;
    String memorySizeCluster = cacheSize + "m";
    String fileNameCluster = null;
    // Date date = new Date();
    // SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddhhmmss");
    // String userDirNameCluster = userId+"_"+sdf.format(date);
    String userDirNameCluster = userId + "_" + serviceId;
    String fileDetailCluster = null;

    String cmdCluster = null;
    String listCmdCluster =
        "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + userDirNameCluster
            + ",cmd=redis-trib.rb create --replicas 1";
    String hostsCluster = "";
    String uriCmdCluster = "";

    String address2 = resultList.get(0);
    String[] add2 = address2.split(":");
    redisIpCluster = add2[0];
    String uriCmdCluster2 = McsConstants.AGENT_URL_BASE + redisIpCluster + agentCmd;
    for (String address : resultList) {
      String[] add = address.split(":");
      redisIpCluster = add[0];
      uriCmdCluster = McsConstants.AGENT_URL_BASE + redisIpCluster + agentCmd;
      String uriCreateCluster = McsConstants.AGENT_URL_BASE + redisIpCluster + agentFile;
      redisPortCluster = add[1];
      fileNameCluster = "redis-" + redisPortCluster + ".conf";
      cmdCluster =
          "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + ",cmd=mkdir -p "
              + userDirNameCluster;
      try {
        log.info("启动time-----------" + new Date());
        executeInstruction(uriCmdCluster, cmdCluster);
        log.info("-----------启动time" + new Date());
        cmdCluster =
            "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + userDirNameCluster
                + ",cmd=mkdir -p " + redisPortCluster;
        executeInstruction(uriCmdCluster, cmdCluster);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        // throw new
        // PaasException(ResourceUtil.getMessage(McsConstants.CREATE_FILE_ERROR)+e.getMessage(),
        // e);
        throw new PaasException("集群 生成文件失败：" + e.getMessage(), e);
      }
      fileDetailCluster =
          fileNameCluster + "||CACHE|" + cachePath + McsConstants.CLUSTER_FILE_PATH
              + userDirNameCluster + "/" + redisPortCluster + "/" + "||include " + cachePath
              + McsConstants.CLUSTER_FILE_PATH + "redis-common.conf" + "||pidfile " + cachePath
              + McsConstants.FILE_PATH + "rdpid/redis-" + redisPortCluster + ".pid" + "||port "
              + redisPortCluster + "||cluster-enabled yes" + "||maxmemory " + memorySizeCluster
              + "||cluster-config-file nodes.conf" + "||cluster-node-timeout 5000"
              + "||appendonly yes";
      try {
        log.info("上传time-----------" + new Date());
        uploadCacheFile(uriCreateCluster, fileDetailCluster);
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
      cmdCluster =
          "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + userDirNameCluster + "/"
              + redisPortCluster + ",cmd=redis-server " + cachePath
              + McsConstants.CLUSTER_FILE_PATH + userDirNameCluster + "/" + redisPortCluster
              + "/redis-" + redisPortCluster + ".conf > redis.log &";
      log.info("-----------:" + cmdCluster);
      try {
        executeInstruction(uriCmdCluster, cmdCluster);
        log.info("启动redis成功");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        // throw new
        // PaasException(ResourceUtil.getMessage(McsConstants.START_ERROR)+e.getMessage(),
        // e);
        throw new PaasException("集群 启动单个失败：" + e.getMessage(), e);
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
      // throw new
      // PaasException(ResourceUtil.getMessage(McsConstants.CLUSTER_START_ERROR)+e.getMessage(),
      // e);
      throw new PaasException("集群启动失败：" + e.getMessage(), e);
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
  private List<String> selectMcsRessCluster(int cacheSize, int size) throws PaasException {
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
        if ((cacheResourceList.get(m).getCacheMemory() - cacheResourceList.get(m)
            .getCacheMemoryUsed()) < cacheSize * j) {
          k = m;
          j++;
          continue;
        }
        ip = cacheResourceList.get(m).getCacheHostIp();
        if (cacheResourceList.get(m) != null && cacheResourceList.get(m).getCycle() == 1) {
          cacheResourceList.get(m).setCachePort(
              getCanUseInstance(cacheResourceList.get(m).getCacheHostIp()).getCachePort());
        } else {
          cacheResourceList.get(m).setCachePort(cacheResourceList.get(m).getCachePort() + 1);
          cacheResourceList.get(m).setCacheMemoryUsed(
              cacheResourceList.get(m).getCacheMemoryUsed() + cacheSize);
          if (cacheResourceList.get(m).getCachePort() == cacheResourceList.get(m).getMaxPort()) {
            cacheResourceList.get(m).setCycle(1);
          }
          int changeRow = updateResource(cacheResourceList.get(m));;
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
    if (count > size) throw new PaasException("mcs resource not enough.");
    return resultList;
  }

  /**
   * 选择缓存资源
   * 
   * @param cacheSize
   * @return
   * @throws PaasException
   */
  private McsResourcePool selectMcsResourcePool(int cacheSize) throws PaasException {
    List<McsResourcePool> resp = getBestResource(1);
    McsResourcePool mcsResourcePool = resp.get(0);
    // 如果该主机端口已经用完，从mcs_user_cache_instance选择该主机最小的已经失效的端口号
    if (mcsResourcePool != null && mcsResourcePool.getCycle() == 1) {
      mcsResourcePool.setCachePort(getCanUseInstance(mcsResourcePool.getCacheHostIp())
          .getCachePort());
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
  private void addUserInstance(String userId, String serviceId, String capacity, String ip,
      int port, String pwd, String serviceName) throws PaasException {
    McsUserCacheInstance mcsUserCacheInstance = new McsUserCacheInstance();
    mcsUserCacheInstance.setUserId(userId); // 用户id
    mcsUserCacheInstance.setCacheHost(ip); // 资源主机ip
    mcsUserCacheInstance.setCacheMemory(Integer.valueOf(capacity)); // 分配内存大小
    mcsUserCacheInstance.setStatus(McsConstants.VALIDATE_STATUS);

    mcsUserCacheInstance.setBeginTime(DateTimeUtil.getNowTimeStamp());
    mcsUserCacheInstance.setEndTime(DateTimeUtil.getNowTimeStamp());
    mcsUserCacheInstance.setSerialNumber(serviceId);
    mcsUserCacheInstance.setCachePort(port);
    mcsUserCacheInstance.setPwd(pwd);
    mcsUserCacheInstance.setServiceName(serviceName);

    int changeRow = addInstance(mcsUserCacheInstance);
    if (changeRow != 1) {
      throw new PaasException("新增用户实例失败");
    }
    log.info("5----------------新增用户实例成功");

  }

  /**
   * 添加zk记录
   * 
   * @param userId
   * @param serviceId
   * @param mcsResourcePool
   * @param requirepass
   * @throws PaasException
   */
  private void addCcsConfig(String userId, String serviceId, String hosts, String requirepass)
      throws PaasException {
    // 在zk中记录申请信息
    CCSComponentOperationParam op = new CCSComponentOperationParam();
    op.setUserId(userId);
    op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
    op.setPathType(PathType.READONLY);

    JsonObject dataJson = new JsonObject();

    // dataJson.addProperty("hosts",
    // cr.getCacheHostIp()+":"+cr.getCachePort());
    dataJson.addProperty("hosts", hosts);
    if (requirepass != null && requirepass.length() > 0)
      dataJson.addProperty("password", CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));

    iCCSComponentManageSv.add(op, dataJson.toString());
    log.info("在zk中记录申请信息成功!");

    op.setPath(McsConstants.MCS_ZK_COMMON_PATH);
    if (!iCCSComponentManageSv.exists(op)) {
      iCCSComponentManageSv.add(op, McsConstants.MCS_ZK_COMMON);
      log.info("4----------------在zk中记录申请信息成功!");
    }
  }

  private void confSenZk(String userId, String serviceId, List<String> hosts, String requirepass)
      throws PaasException {
    // 在zk中记录申请信息
    CCSComponentOperationParam op = new CCSComponentOperationParam();
    op.setUserId(userId);
    op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
    op.setPathType(PathType.READONLY);

    JsonObject dataJson = new JsonObject();

    // dataJson.addProperty("hosts",
    // cr.getCacheHostIp()+":"+cr.getCachePort());
    dataJson.addProperty("master", hosts.get(0));
    dataJson.addProperty("slave", hosts.get(1) + ";" + hosts.get(2));
    dataJson.addProperty("sentinel", hosts.get(3) + ";" + hosts.get(4) + ";" + hosts.get(5));
    if (requirepass != null && requirepass.length() > 0)
      dataJson.addProperty("password", CiperUtil.encrypt(McsConstants.PWD_KEY, requirepass));

    iCCSComponentManageSv.add(op, dataJson.toString());
    log.info("在zk中记录申请信息成功!");

    op.setPath(McsConstants.MCS_ZK_COMMON_PATH);
    if (!iCCSComponentManageSv.exists(op)) {
      iCCSComponentManageSv.add(op, McsConstants.MCS_ZK_COMMON);
      log.info("4----------------在zk中记录申请信息成功!");
    }
  }

  private void addMasterConfig(McsResourcePool mcsResourcePool, int redisPort, String capacity,
      String requirepass) throws PaasException {
    // 调用restful服务
    String memorySize = capacity + "m";

    String fileName = "redis-" + redisPort + ".conf";

    String mkdircmd =
        "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + redisPort;

    String fileDetail =
        fileName
            + "||CACHE|"
            + cachePath
            + McsConstants.FILE_PATH
            + redisPort
            + "/"
            + "||include "
            + cachePath
            + McsConstants.FILE_PATH
            + "redis-common.conf||"
            + "pidfile /var/run/redis-"
            + redisPort
            + ".pid||port "
            + redisPort
            + "||maxmemory "
            + memorySize
            + "||requirepass "
            + requirepass
            + "||"
            + "logfile "
            + cachePath
            + "/log/"
            + redisPort
            + "/redis-"
            + redisPort
            + ".log"
            + "||tcp-keepalive 60||maxmemory-policy noeviction||appendonly yes||appendfilename \"appendonly.aof\"";
    String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p " + McsConstants.LOG_PATH;

    String logName = "redis-" + redisPort + ".log";

    String logfile =
        logName + "||CACHE|" + cachePath + McsConstants.LOG_PATH + redisPort + "/" + "||";

    String uri =
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentFile();
    String commandUri =
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentCmd();

    try {
      executeInstruction(commandUri, mkdircmd);
      executeInstruction(commandUri, logdir);
      uploadCacheFile(uri, fileDetail);
      // 上传日志文件
      uploadCacheFile(uri, logfile);

      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }

    startMcsIns(
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentCmd(), mcsResourcePool.getCachePath(), redisPort);
    log.info("3----------------启动redis成功!");

  }

  private void configSenMaster(String result, String capacity, String requirepass)
      throws PaasException {
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
    String fileDetail =
        fileName
            + "||CACHE|"
            + cachePath
            + McsConstants.FILE_PATH
            + port
            + "/"
            + "||include "
            + cachePath
            + McsConstants.FILE_PATH
            + "redis-common.conf||"
            + "pidfile /var/run/redis-"
            + port
            + ".pid||port "
            + port
            + "||maxmemory "
            + memorySize
            + "||requirepass "
            + requirepass
            + "||"
            + "logfile "
            + cachePath
            + "/log/"
            + port
            + "/redis-"
            + port
            + ".log"
            + "||tcp-keepalive 60||maxmemory-policy noeviction||appendonly yes||appendfilename \"appendonly.aof\"";

    String logfile = logName + "||CACHE|" + cachePath + McsConstants.LOG_PATH + port + "/" + "||";

    try {
      executeInstruction(uriCmdCluster, mkdircmd);
      executeInstruction(uriCmdCluster, logdir);
      uploadCacheFile(uriCreateCluster, fileDetail);
      // 生成日志文件
      uploadCacheFile(uriCreateCluster, logfile);
      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }

    startMcsIns(uriCmdCluster, cachePath, new Integer(port));
    log.info("3----------------启动redis成功!");

  }

  private void addSlaveConfig(McsResourcePool mcsResourcePool, int redisPort, String capacity,
      String requirepass) throws PaasException {
    // 调用restful服务
    String memorySize = capacity + "m";

    String fileName = "redis-" + redisPort + ".conf";

    String mkdircmd =
        "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + redisPort;

    String fileDetail =
        fileName + "||CACHE|" + cachePath + McsConstants.FILE_PATH + redisPort + "/" + "||include "
            + cachePath + McsConstants.FILE_PATH + "redis-common.conf||"
            + "pidfile /var/run/redis-" + redisPort + ".pid||port " + redisPort + "||maxmemory "
            + memorySize + "||requirepass " + requirepass + "||" + "logfile " + cachePath + "/log/"
            + redisPort + "/redis-" + redisPort + ".log" + "||slaveof "
            + mcsResourcePool.getCacheHostIp() + " " + mcsResourcePool.getCachePort()
            + "||masterauth " + requirepass;
    String uri =
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentFile();
    String commandUri =
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentCmd();
    String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p " + McsConstants.LOG_PATH;

    String logName = "redis-" + redisPort + ".log";

    String logfile =
        logName + "||CACHE|" + cachePath + McsConstants.LOG_PATH + redisPort + "/" + "||";

    try {
      executeInstruction(commandUri, mkdircmd);
      executeInstruction(commandUri, logdir);
      uploadCacheFile(uri, fileDetail);
      uploadCacheFile(uri, logfile);
      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }

    startMcsIns(
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentCmd(), mcsResourcePool.getCachePath(), redisPort);
    log.info("3----------------启动redis成功!");

  }

  private void configSenSlave(String result, String masterResult, String capacity,
      String requirepass) throws PaasException {
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
    String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + port;

    String fileDetail =
        fileName + "||CACHE|" + cachePath + McsConstants.FILE_PATH + port + "/" + "||include "
            + cachePath + McsConstants.FILE_PATH + "redis-common.conf||"
            + "pidfile /var/run/redis-" + port + ".pid||port " + port + "||maxmemory " + memorySize
            + "||requirepass " + requirepass + "||" + "logfile " + cachePath + "/log/" + port
            + "/redis-" + port + ".log" + "||slaveof " + masterIp + " " + masterPort
            + "||masterauth " + requirepass;

    String logdir = "CMD| path=" + cachePath + ",cmd=mkdir -p " + McsConstants.LOG_PATH;

    String logName = "redis-" + port + ".log";

    String logfile = logName + "||CACHE|" + cachePath + McsConstants.LOG_PATH + port + "/" + "||";
    try {
      executeInstruction(uriCmdCluster, mkdircmd);
      executeInstruction(uriCmdCluster, logdir);
      uploadCacheFile(uriCreateCluster, fileDetail);
      uploadCacheFile(uriCreateCluster, logfile);
      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }
    startMcsIns(uriCmdCluster, cachePath, new Integer(port));
    log.info("3----------------启动redis成功!");

  }

  private void configSentinel(String result, String master) throws PaasException {
    // 获取当前节点的ip和端口
    String[] info = result.split(":");
    String ip = info[0];
    String port = info[1];

    // 获取监控节点的ip和端口
    String[] masterInfo = master.split(":");
    String masterIp = masterInfo[0];
    String masterPort = masterInfo[1];

    String fileName = "redis-" + port + "-sentinel.conf";
    String uriCmdCluster = McsConstants.AGENT_URL_BASE + ip + agentCmd;
    String uriCreateCluster = McsConstants.AGENT_URL_BASE + ip + agentFile;
    String mkdircmd = "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + port;
    String logdir =
        "CMD| path=" + cachePath + McsConstants.LOG_PATH + ",cmd=touch  redis-" + port + ".log";
    String fileDetail =
        fileName
            + "||CACHE|"
            + cachePath
            + McsConstants.FILE_PATH
            + port
            + "/"
            + "||port  "
            + port
            + "||sentinel monitor mymaster "
            + masterIp
            + " "
            + masterPort
            + " 2"
            + "||sentinel down-after-milliseconds mymaster 5000||sentinel failover-timeout mymaster 60000||sentinel parallel-syncs mymaster 1";

    String logName = "redis-" + port + ".log";

    String logfile = logName + "||CACHE|" + cachePath + McsConstants.LOG_PATH + port + "/" + "||";
    try {
      executeInstruction(uriCmdCluster, mkdircmd);
      executeInstruction(uriCmdCluster, logdir);
      uploadCacheFile(uriCreateCluster, fileDetail);
      uploadCacheFile(uriCreateCluster, logfile);
      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }
    startSenIns(uriCmdCluster, cachePath, new Integer(port));
    log.info("3----------------启动redis成功!");

  }

  /**
   * 上传配置文件，执行命令
   * 
   * @param mcsResourcePool
   * @param capacity
   * @param requirepass
   * @throws PaasException
   */
  private void addMcsConfig(McsResourcePool mcsResourcePool, int redisPort, String capacity,
      String requirepass) throws PaasException {
    // 调用restful服务
    String memorySize = capacity + "m";

    String fileName = "redis-" + redisPort + ".conf";

    String mkdircmd =
        "CMD| path=" + cachePath + McsConstants.FILE_PATH + ",cmd=mkdir -p " + redisPort;

    String fileDetail =
        fileName + "||CACHE|" + cachePath + McsConstants.FILE_PATH + redisPort + "/" + "||include "
            + cachePath + McsConstants.FILE_PATH + "redis-common.conf||"
            + "pidfile /var/run/redis-" + redisPort + ".pid||port " + redisPort + "||maxmemory "
            + memorySize + "||requirepass " + requirepass + "||" + "logfile " + cachePath
            + "/redis/log/redis-" + redisPort + ".log";
    String uri =
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentFile();
    
    log.info("++++++++++addMcsConfig.fileDetail:" + fileDetail);
    log.info("++++++++++addMcsConfig.mkdircmd:" + mkdircmd);
    log.info("++++++++++addMcsConfig.uri:" + uri);
    
    try {
      executeInstruction(McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
          + mcsResourcePool.getAgentCmd(), mkdircmd);
      
      uploadCacheFile(uri, fileDetail);
      log.info("3----------------上传文件成功!");
    } catch (PaasException e) {
      log.error(e.getMessage(), e);
      throw new PaasException("单例 上传文件失败：" + e.getMessage(), e);

    }

    startMcsIns(
        McsConstants.AGENT_URL_BASE + mcsResourcePool.getCacheHostIp()
            + mcsResourcePool.getAgentCmd(), mcsResourcePool.getCachePath(), redisPort);
    log.info("3----------------启动redis成功!");

  }

  /**
   * 只修改容量，描述可以修改。 serviceId userId capacity 都不能为空
   * 
   * @throws PaasException
   */
  @Override
  public String modifyMcs(String param) throws PaasException {
    Map<String, String> map = McsParamUtil.getParamMap(param);
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_M.equals(applyType)) throw new PaasException("缓存容量修改，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    String capacity = map.get(McsConstants.CAPACITY);
    Assert.notNull(serviceId, "serviceId为空");
    Assert.notNull(userId, "userId为空");
    Assert.notNull(capacity, "capacity为空");
    String serviceName = map.get(McsConstants.SERVICE_NAME);
    log.info("M1----------------获得已申请过的记录");
    List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
    if (cis == null || cis.isEmpty()) throw new PaasException("该服务未开通过，无法修改！");
    log.info("M2----------------修改mcs_resource");
    final int cacheSize =
        cis.size() == 1 ? Integer.valueOf(capacity) : Math.round(Integer.valueOf(capacity)
            / McsConstants.CACHE_NUM * 2);
    modifyMcsResource(cis, cacheSize);
    log.info("M3----------------修改mcs服务端，重启redis");
    modifyMcsServerFileAndUserIns(userId, serviceId, cis, cacheSize, serviceName);
    log.info("----------------修改成功");
    return McsConstants.SUCCESS_FLAG;
  }

  /**
   * 停redis，修改mcs服务端的配置文件，修改用户实例，启动redis
   * 
   * @param cis
   * @param cacheSize
   * @throws PaasException
   */
  @SuppressWarnings("rawtypes")
  private void modifyMcsServerFileAndUserIns(final String userId, final String serviceId,
      List<McsUserCacheInstance> cis, final int cacheSize, String serviceName) throws PaasException {
    McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);

    if (cis.size() == 1) {
      McsUserCacheInstance tempIns = cis.get(0);
      McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
      McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
      rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
          .andCacheHostIpEqualTo(tempIns.getCacheHost());
      List<McsResourcePool> pools = rpm.selectByExample(rpmc);
      McsResourcePool pool = pools.get(0);

      log.info("M3.1----------------停redis");
      // 停redis
      String uri = McsConstants.AGENT_URL_BASE + pool.getCacheHostIp() + pool.getAgentCmd();
      stopMcsIns(uri, pool.getCachePath(), tempIns.getCachePort());

      CCSComponentOperationParam op = new CCSComponentOperationParam();
      op.setUserId(userId);
      op.setPath(McsConstants.MCS_ZK_PATH + serviceId);
      op.setPathType(PathType.READONLY);
      String param = iCCSComponentManageSv.get(op);
      Gson gson = new Gson();
      Map map = gson.fromJson(param, Map.class);
      log.info("M3.2----------------修改文件，重启redis");
      // 修改文件，重启redis
      cachePath = pool.getCachePath();
      addMcsConfig(pool, tempIns.getCachePort(), cacheSize + "", map.get("password").toString());

      log.info("M3.3----------------用户实例表，使用内存");
      // 更新 用户实例表，使用内存
      tempIns.setCacheMemory(cacheSize);
      if (serviceName != null && serviceName.length() > 0) tempIns.setServiceName(serviceName);
      im.updateByPrimaryKey(tempIns);

    } else {
      final List<String> resultList = new ArrayList<String>();
      McsResourcePool pool = null;
      for (int i = 0; i < cis.size(); i++) {
        McsUserCacheInstance tempIns = cis.get(i);
        if (pool == null) {
          McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
          McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
          rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
              .andCacheHostIpEqualTo(tempIns.getCacheHost());
          List<McsResourcePool> pools = rpm.selectByExample(rpmc);
          pool = pools.get(0);
          cachePath = pool.getCachePath();
          agentCmd = pool.getAgentCmd();
          agentFile = pool.getAgentFile();
        }
        log.info("M3.1------cluster----------停redis");
        // 停redis
        String uri = McsConstants.AGENT_URL_BASE + tempIns.getCacheHost() + pool.getAgentCmd();
        stopMcsIns(uri, pool.getCachePath(), tempIns.getCachePort());

        resultList.add(tempIns.getCacheHost() + ":" + tempIns.getCachePort());
        log.info("M3.2-------cluster---------用户实例表，使用内存");
        // 更新 用户实例表，使用内存
        tempIns.setCacheMemory(cacheSize);
        if (serviceName != null && serviceName.length() > 0) tempIns.setServiceName(serviceName);
        im.updateByPrimaryKey(tempIns);
      }
      // 处理mcs服务端文件等
      ExecutorService executorService = Executors.newSingleThreadExecutor();
      executorService.execute(new Runnable() {
        public void run() {
          try {
            log.info("M3.3-----cluster-----------修改文件，重启redis");
            addMcsConfigCluster(resultList, userId, serviceId, cacheSize);
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
  private void stopMcsIns(String uri, String cPath, int port) throws PaasException {
    try {
      String cmd_stop =
          "CMD| path=" + cPath + ",cmd=ps -ef | grep " + port
              + " | awk '{print $2}' | xargs kill -9";
      // String
      // cmd_stop="CMD| path="+cPath+",cmd=./redis-cli -p "+port+" shutdown";
      executeInstruction(uri, cmd_stop);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("停止MCS异常，port：" + port, e);
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
        rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
            .andCacheHostIpEqualTo(key);
        List<McsResourcePool> pools = rpm.selectByExample(rpmc);
        McsResourcePool pool = pools.get(0);
        // 更新CacheMemoryUsed
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
    rpmc.setOrderByClause("(ifnull(cache_memory, 0) - ifnull(cache_memory_used, 0)) desc");
    rpmc.setLimitStart(0);
    rpmc.setLimitEnd(num);
    return rpm.selectByExample(rpmc);
  }

  /**
   * 获得UseInstance中 失效的记录
   * 
   * @param host
   * @return
   */
  private McsUserCacheInstance getCanUseInstance(String host) {
    McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
    McsUserCacheInstanceCriteria imc = new McsUserCacheInstanceCriteria();
    imc.createCriteria().andStatusNotEqualTo(McsConstants.VALIDATE_STATUS)
        .andCacheHostEqualTo(host);
    imc.setLimitStart(0);
    imc.setLimitEnd(1);
    List<McsUserCacheInstance> list = im.selectByExample(imc);
    if (list != null && list.size() > 0) return list.get(0);
    return null;
  }

  /**
   * 更新资源
   * 
   * @param mcsResourcePool
   * @return
   */
  private int updateResource(McsResourcePool mcsResourcePool) throws PaasException {
    McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
    McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
    rpmc.createCriteria().andIdEqualTo(mcsResourcePool.getId())
        .andCachePortEqualTo(mcsResourcePool.getCachePort() - 1);
    return rpm.updateByExampleSelective(mcsResourcePool, rpmc);
  }

  /**
   * 添加用户实例
   * 
   * @param mcsResourcePool
   * @return
   */
  private int addInstance(McsUserCacheInstance mcsUserCacheInstance) throws PaasException {
    McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
    return im.insert(mcsUserCacheInstance);
  }

  /**
   * 上传cache文件到服务器
   * 
   * @param uri
   * @param dstFileDir
   * @throws PaasException
   */
  @SuppressWarnings("unused")
  private void uploadCacheFile(String uri, String dstFileDir) throws PaasException {
    try {
      ClientResource clientResource = new ClientResource(uri);
      Representation representation = clientResource.post(dstFileDir);
      clientResource.release();

      Thread.sleep(10);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("", e);
    } catch (Error e) {
      log.error(e.getMessage(), e);
    }

  }

  /**
   * 执行服务器端命令
   * 
   * @param uri
   * @param cmd
   * @return
   * @throws PaasException
   */
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
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_STOP.equals(applyType)) throw new PaasException("停止缓存服务，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
    if (cis == null || cis.isEmpty()) throw new PaasException("该服务未开通过，无法停止！");
    stopMcsInsForCacheIns(cis);
    log.info("----------------停止缓存服务,成功");
    if (cis.size() > 1) {
      // TODO 验证集群的是否能停掉
    }
    return McsConstants.SUCCESS_FLAG;
  }

  private void stopMcsInsForCacheIns(List<McsUserCacheInstance> cis) throws PaasException {
    McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
    McsResourcePool pool = null;
    for (int i = 0; i < cis.size(); i++) {
      if (pool == null) {
        McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
        rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
            .andCacheHostIpEqualTo(cis.get(i).getCacheHost());
        List<McsResourcePool> pools = rpm.selectByExample(rpmc);
        pool = pools.get(0);
      }
      stopMcsIns(McsConstants.AGENT_URL_BASE + cis.get(i).getCacheHost() + pool.getAgentCmd(),
          pool.getCachePath(), cis.get(i).getCachePort());
    }

  }

  @Override
  public String startMcs(String param) throws PaasException {
    Map<String, String> map = McsParamUtil.getParamMap(param);
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_START.equals(applyType))
      throw new PaasException("启动缓存服务，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
    if (cis == null || cis.isEmpty()) throw new PaasException("该服务未开通过，无法启动！");
    startMcs(userId, serviceId, cis);
    return McsConstants.SUCCESS_FLAG;
  }

  private void startMcs(String userId, String serviceId, List<McsUserCacheInstance> cis)
      throws PaasException {
    McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
    McsResourcePool pool = null;
    String ipPorts = "";
    for (int i = 0; i < cis.size(); i++) {
      if (pool == null) {
        McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
        rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
            .andCacheHostIpEqualTo(cis.get(i).getCacheHost());
        List<McsResourcePool> pools = rpm.selectByExample(rpmc);
        pool = pools.get(0);
        cachePath = pool.getCachePath();
      }
      ipPorts += " " + cis.get(i).getCacheHost() + ":" + cis.get(i).getCachePort();
      if (cis.size() == 1)
        startMcsIns(McsConstants.AGENT_URL_BASE + cis.get(i).getCacheHost() + pool.getAgentCmd(),
            pool.getCachePath(), cis.get(i).getCachePort());
      else
        startMcsInsForCluster(
            McsConstants.AGENT_URL_BASE + cis.get(i).getCacheHost() + pool.getAgentCmd(), userId
                + "_" + serviceId, pool.getCachePath(), cis.get(i).getCachePort());
    }
    if (cis.size() > 1) {
      String listCmdCluster =
          "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + userId + "_" + serviceId
              + ",cmd=redis-trib.rb create --replicas 1" + ipPorts;
      try {
        executeInstruction(
            McsConstants.AGENT_URL_BASE + cis.get(0).getCacheHost() + pool.getAgentCmd(),
            listCmdCluster);
        log.info("集群启动成功");
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new PaasException("集群启动失败：" + e.getMessage(), e);
      }
    }
  }

  private void startSenIns(String uri, String cPath, int port) throws PaasException {
    try {
      String cmd =
          "CMD| path=" + cPath + McsConstants.FILE_PATH + port + "/,cmd=redis-sentinel " + cPath
              + McsConstants.FILE_PATH + port + "/redis-" + port + "-sentinel.conf &";
      executeInstruction(uri, cmd);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("启动MCS异常，port：" + port, e);
    }
  }

  private void startMcsIns(String uri, String cPath, int port) throws PaasException {
    try {
      String cmd =
          "CMD| path=" + cPath + McsConstants.FILE_PATH + port + "/,cmd=redis-server " + cPath
              + McsConstants.FILE_PATH + port + "/redis-" + port + ".conf &";

      executeInstruction(uri, cmd);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("启动MCS异常，port：" + port, e);
    }
  }

  private void startMcsInsForCluster(String uri, String dir, String cPath, int port)
      throws PaasException {
    try {
      String cmd_rm =
          "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
              + ",cmd=rm -fr appendonly.aof  dump.rdb  nodes.conf ";
      String cmd =
          "CMD| path=" + cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/" + port
              + ",cmd=redis-server " + cachePath + McsConstants.CLUSTER_FILE_PATH + dir + "/"
              + port + "/redis-" + port + ".conf > redis.log &";
      executeInstruction(uri, cmd_rm);
      executeInstruction(uri, cmd);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("启动MCS异常，port：" + port, e);
    }
  }

  @Override
  public String restartMcs(String param) throws PaasException {
    Map<String, String> map = McsParamUtil.getParamMap(param);
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_RESTART.equals(applyType))
      throw new PaasException("重启缓存服务，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
    if (cis == null || cis.isEmpty()) throw new PaasException("该服务未开通过，无法重启！");
    stopMcsInsForCacheIns(cis);
    startMcs(userId, serviceId, cis);
    return McsConstants.SUCCESS_FLAG;
  }

  @Override
  public String cancelMcs(String param) throws PaasException {
    Map<String, String> map = McsParamUtil.getParamMap(param);
    String applyType = map.get(McsConstants.APPLY_TYPE);
    if (!McsConstants.APPLY_TYPE_R.equals(applyType)) throw new PaasException("注销缓存服务，服务类型不对！");
    // 获取服务号配置参数
    final String serviceId = map.get(McsConstants.SERVICE_ID);
    final String userId = map.get(McsConstants.USER_ID);
    log.info("注销----------------获得已申请过的记录");
    List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId, userId);
    if (cis == null || cis.isEmpty()) throw new PaasException("该服务未开通过，无法注销！");

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
   * @param cis
   * @param cacheSize
   * @throws PaasException
   */
  private void removeMcsServerFileAndUserIns(final String userId, final String serviceId,
      List<McsUserCacheInstance> cis) throws PaasException {
    McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);

    if (cis.size() == 1) {
      McsUserCacheInstance tempIns = cis.get(0);
      McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
      McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
      rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
          .andCacheHostIpEqualTo(tempIns.getCacheHost());
      List<McsResourcePool> pools = rpm.selectByExample(rpmc);
      McsResourcePool pool = pools.get(0);

      log.info("注销----------------停redis");
      // 停redis
      String uri = McsConstants.AGENT_URL_BASE + pool.getCacheHostIp() + pool.getAgentCmd();
      stopMcsIns(uri, pool.getCachePath(), tempIns.getCachePort());
      log.info("注销----------------删除redis配置文件");
      removeMcsConfig(uri, pool.getCachePath(), tempIns.getCachePort());

      CCSComponentOperationParam op = new CCSComponentOperationParam();
      op.setUserId(userId);
      op.setPath("/MCS/" + serviceId);
      op.setPathType(PathType.READONLY);
      log.info("注销----------------删除在zk的配置");
      iCCSComponentManageSv.delete(op);

      log.info("注销----------------用户实例表，使用内存");
      // 更新 用户实例表，状态
      tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
      im.updateByPrimaryKey(tempIns);

    } else {
      final List<String> resultList = new ArrayList<String>();
      McsResourcePool pool = null;
      for (int i = 0; i < cis.size(); i++) {
        McsUserCacheInstance tempIns = cis.get(i);
        if (pool == null) {
          McsResourcePoolMapper rpm = ServiceUtil.getMapper(McsResourcePoolMapper.class);
          McsResourcePoolCriteria rpmc = new McsResourcePoolCriteria();
          rpmc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
              .andCacheHostIpEqualTo(tempIns.getCacheHost());
          List<McsResourcePool> pools = rpm.selectByExample(rpmc);
          pool = pools.get(0);
        }
        log.info("注销----cluster------------停redis");
        // 停redis
        String uri = McsConstants.AGENT_URL_BASE + tempIns.getCacheHost() + pool.getAgentCmd();
        stopMcsIns(uri, pool.getCachePath(), tempIns.getCachePort());
        log.info("注销----cluster------------删除redis配置文件");
        removeMcsConfig(uri, pool.getCachePath(), tempIns.getCachePort(), userId, serviceId);

        CCSComponentOperationParam op = new CCSComponentOperationParam();
        op.setUserId(userId);
        op.setPath("/MCS/" + serviceId);
        op.setPathType(PathType.READONLY);
        log.info("注销----cluster------------删除在zk的配置");
        iCCSComponentManageSv.delete(op);

        log.info("注销----cluster------------用户实例表，使用内存");
        // 更新 用户实例表，状态
        tempIns.setStatus(McsConstants.INVALIDATE_STATUS);
        im.updateByPrimaryKey(tempIns);
        resultList.add(tempIns.getCacheHost() + ":" + tempIns.getCachePort());
      }
    }
  }

  /**
   * 删除 单例的redis配置文件
   * 
   * @param uri
   * @param cachePath
   * @param cachePort
   * @throws PaasException
   */
  private void removeMcsConfig(String uri, String cachePath, Integer cachePort)
      throws PaasException {
    String file = cachePath + McsConstants.FILE_PATH + "redis-" + cachePort + ".conf";
    try {
      String cmd = "CMD| path=" + cachePath + ",cmd=rm " + file;
      executeInstruction(uri, cmd);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("删除redis的配置文件异常，file：" + file, e);
    }

  }

  /**
   * 删除 集群的redis配置文件
   * 
   * @param uri
   * @param cachePath
   * @param cachePort
   * @throws PaasException
   */
  private void removeMcsConfig(String uri, String cachePath, Integer cachePort, String userId,
      String serviceId) throws PaasException {
    String file =
        cachePath + McsConstants.CLUSTER_FILE_PATH + userId + "_" + serviceId + "/redis-"
            + cachePort + ".conf";
    try {
      String cmd = "CMD| path=" + cachePath + ",cmd=rm " + file;
      executeInstruction(uri, cmd);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new PaasException("集群--删除redis的配置文件异常，file：" + file, e);
    }

  }

}
