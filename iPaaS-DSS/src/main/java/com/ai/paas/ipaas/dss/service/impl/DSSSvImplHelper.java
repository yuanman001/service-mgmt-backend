package com.ai.paas.ipaas.dss.service.impl;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import com.ai.dubbo.ext.vo.BaseInfo;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.dss.dao.interfaces.DssMcsInfoMapper;
import com.ai.paas.ipaas.dss.dao.interfaces.DssResourcePoolMapper;
import com.ai.paas.ipaas.dss.dao.interfaces.DssUserInstanceMapper;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssMcsInfo;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssMcsInfoCriteria;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssResourcePool;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssResourcePoolCriteria;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssUserInstance;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssUserInstanceCriteria;
import com.ai.paas.ipaas.dss.dto.DSSCommonConf;
import com.ai.paas.ipaas.dss.dto.DSSConf;
import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CancelDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CleanDSSParam;
import com.ai.paas.ipaas.dss.manage.param.DSSResult;
import com.ai.paas.ipaas.dss.manage.param.ModifyParam;
import com.ai.paas.ipaas.dss.manage.param.RecordParam;
import com.ai.paas.ipaas.dss.manage.param.RecordResult;
import com.ai.paas.ipaas.dss.manage.param.StatusParam;
import com.ai.paas.ipaas.dss.manage.param.StatusResult;
import com.ai.paas.ipaas.dss.manage.param.UploadParam;
import com.ai.paas.ipaas.dss.manage.param.UploadResult;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.ai.paas.ipaas.util.CiperUtil;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

@Service
@Transactional(rollbackFor = Exception.class)
public class DSSSvImplHelper {

	@Autowired
	protected ICCSComponentManageSv iCCSComponentManageSv;

	private static final Logger log = LogManager
			.getLogger(DSSSvImplHelper.class.getName());

	protected static final String DSS_COMMON_ZK_CONF = "/DSS/COMMON";
	protected static final String DSS_BASE_ZK_CONF = "/DSS/";
	protected static final String SUCCESS = "000000";
	protected static final String FAIL = "999999";
	protected static final String SUCCESS_MSG = "Request successful!";
	protected static final String FAIL_MSG = "Request failed!";
	protected static final String PWD_KEY = "BaryTukyTukyBary";
	protected static final boolean TRUE = true;
	protected static final boolean FALSE = false;

	private static final String ADMIN = "admin";

	private static final String USERS = "system.users";
	private static final String USER = "user";
	private static final String DB = "db";
	private static final String REMARK = "remark";

	private static final String LEFT_SIZE_NOT_ENOUGH = "磁盘空间不足，请与管理员联系！";

	/**
	 * 
	 * <创建COMMON and COLLECTION ZK中不存在COMMON信息>
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 * @see [类、类#方法、类#成员]
	 */
	protected Object[] createDBUserCollection(ApplyDSSParam applyObj)
			throws Exception {
		List<DssResourcePool> dssResPoolList = getBestDssResource();
		// int dssNum = dssResPoolList.size();
		// int perCapacity = Integer.parseInt(applyObj.getCapacity()) / dssNum
		// + Integer.parseInt(applyObj.getCapacity()) % dssNum;
		int leftSize = dssResPoolList.get(0).getLeftSize()
				- Integer.parseInt(applyObj.getCapacity());
		int groupId = dssResPoolList.get(0).getGroupId();
		if (leftSize < 0) {
			log.error(LEFT_SIZE_NOT_ENOUGH);
			throw new Exception(LEFT_SIZE_NOT_ENOUGH);
		}
		String hostStr = getHostStr(dssResPoolList);
		String pwd = getPwd() + "";
		// 初始化MongoDB 并创建COLLECTION
		if (initMongoDB(hostStr, applyObj.getUserId(), applyObj.getServiceId(),
				pwd)) {
			DSSCommonConf dssCommConf = new DSSCommonConf();
			dssCommConf.setHosts(hostStr);
			dssCommConf.setPassword(CiperUtil.encrypt(PWD_KEY, pwd));
			DssMcsInfo dssMcsInfo = getMcsResource();
			if (!dssUserInstanceExist(applyObj.getUserId(),
					applyObj.getServiceId())) {
				DssUserInstance dssInstance = new DssUserInstance();
				dssInstance.setCollectionName(applyObj.getServiceId());
				dssInstance.setDbName(applyObj.getUserId());
				dssInstance.setFileLimitSize(Double.parseDouble(applyObj
						.getSingleFileSize()));
				dssInstance.setGroupId(groupId);
				dssInstance.setOssSize(Double.parseDouble(applyObj
						.getCapacity()));
				dssInstance.setRedisId(dssMcsInfo.getId());
				dssInstance.setStartDate(new Timestamp(System
						.currentTimeMillis()));
				dssInstance.setUserId(applyObj.getUserId());
				dssInstance.setServiceName(applyObj.getServiceName());
				// 存入数据库
				insertInstance(dssInstance);
				updateResourceLeftSize(groupId, leftSize);
			}
			List<String> redisAddress = getRedisStrList(dssMcsInfo);
			dssCommConf.setRedisHosts(List2Str(redisAddress));
			dssCommConf.setUsername(applyObj.getUserId());
			DSSConf dssConf = new DSSConf();
			dssConf.setDbName(applyObj.getServiceId());
			dssConf.setLimitSize(applyObj.getSingleFileSize());
			dssConf.setSize(applyObj.getCapacity());
			// 返回待存入ZK信息
			Object[] result = { dssCommConf, dssConf };
			return result;
		}
		return null;
	}

	/**
	 * 创建COLLECTION ZK中存在COMMON信息
	 * 
	 * @param applyObj
	 * @return
	 * @throws PaasException
	 * @throws JsonSyntaxException
	 */
	protected DSSConf createAnotherDBUserCollection(ApplyDSSParam applyObj)
			throws Exception {
		// 获取COMMON信息
		DSSCommonConf commConf = (DSSCommonConf) getConfObj(
				applyObj.getUserId(), DSS_COMMON_ZK_CONF);
		// 初始化MongoDB 并创建COLLECTION
		int dssNum = commConf.getHosts().split(";").length;
		// int perCapacity = Integer.parseInt(applyObj.getCapacity()) / dssNum
		// + Integer.parseInt(applyObj.getCapacity()) % dssNum;
		int groupId = getGroupid(commConf.getHosts().split(";")[0].split(":")[0]);
		int leftSize = getDssResource(groupId).get(0).getLeftSize()
				- Integer.parseInt(applyObj.getCapacity());
		if (leftSize < 0) {
			log.error(LEFT_SIZE_NOT_ENOUGH);
			throw new Exception(LEFT_SIZE_NOT_ENOUGH);
		}
		if (initMongoDB(commConf, applyObj.getUserId(), applyObj.getServiceId())) {
			DssUserInstance dssInstance = new DssUserInstance();
			dssInstance.setCollectionName(applyObj.getServiceId());
			dssInstance.setDbName(applyObj.getUserId());
			dssInstance.setFileLimitSize(Double.parseDouble(applyObj
					.getSingleFileSize()));
			dssInstance.setGroupId(groupId);
			dssInstance.setOssSize(Double.parseDouble(applyObj.getCapacity()));
			dssInstance.setRedisId(getMcsId(commConf.getRedisHosts()));
			dssInstance.setStartDate(new Timestamp(System.currentTimeMillis()));
			dssInstance.setUserId(applyObj.getUserId());
			dssInstance.setServiceName(applyObj.getServiceName());
			// 存入数据库
			insertInstance(dssInstance);
			updateResourceLeftSize(groupId, leftSize);
			DSSConf dssConf = new DSSConf();
			dssConf.setDbName(applyObj.getServiceId());
			dssConf.setSize(applyObj.getCapacity());
			dssConf.setLimitSize(applyObj.getSingleFileSize());
			// 返回待存入ZK信息
			return dssConf;
		}
		return null;
	}

	/**
	 * 注销存储操作
	 * 
	 * @param cancelObj
	 * @throws Exception
	 */
	protected void deleteCollection(CancelDSSParam cancelObj) throws Exception {
		// 校验用户信息是否存在
		if (existConf(getZKBase(cancelObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(cancelObj.getUserId()), DSS_BASE_ZK_CONF
						+ cancelObj.getServiceId())) {
			DSSCommonConf commConf = null;
			// 获取配置信息
			if (getConfObj(cancelObj.getUserId(), DSS_COMMON_ZK_CONF) instanceof DSSCommonConf) {
				commConf = (DSSCommonConf) getConfObj(cancelObj.getUserId(),
						DSS_COMMON_ZK_CONF);
			}
			DSSConf dssConf = null;
			if (getConfObj(cancelObj.getUserId(),
					DSS_BASE_ZK_CONF + cancelObj.getServiceId()) instanceof DSSConf) {
				dssConf = (DSSConf) getConfObj(cancelObj.getUserId(),
						DSS_BASE_ZK_CONF + cancelObj.getServiceId());
			}

			if (commConf != null) {
				// 删除redis中KEY
				deleteMCSKey(cancelObj.getUserId(), cancelObj.getServiceId(),
						commConf.getRedisHosts().split(";"));
				// 删除Collection
				deleteCollection(commConf, cancelObj.getUserId(),
						cancelObj.getServiceId());
			} else {
				log.info("commConf为NULL");
			}
			// 判断该用户存在存储个数
			if (getInstanceNum(cancelObj.getUserId()) == 1) {
				deleteDBandUser(commConf, cancelObj.getUserId());
				deleteConf(cancelObj.getUserId(), DSS_COMMON_ZK_CONF);
				deleteConf(cancelObj.getUserId(),
						DSS_BASE_ZK_CONF + cancelObj.getServiceId());
			} else if (getInstanceNum(cancelObj.getUserId()) > 1) {
				deleteConf(cancelObj.getUserId(),
						DSS_BASE_ZK_CONF + cancelObj.getServiceId());
			} else if (getInstanceNum(cancelObj.getUserId()) == 0) {
				log.error("数据异常");
				throw new Exception("数据异常");
			}
			// 更新数据库end date
			if (dssUserInstanceExist(cancelObj.getUserId(),
					cancelObj.getServiceId())) {
				addEndDateInstance(cancelObj.getUserId(),
						cancelObj.getServiceId());
			}
			// 归还容量
			int groupId = getGroupid(commConf.getHosts().split(";")[0]
					.split(":")[0]);
			int leftSize = 0;
			if (dssConf != null) {
				leftSize = getDssResource(groupId).get(0).getLeftSize()
						+ Integer.parseInt(dssConf.getSize());
			}
			updateResourceLeftSize(groupId, leftSize);
		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	/**
	 * 清空或者根据KEY清理一个文件
	 * 
	 * @param applyObj
	 * @throws Exception
	 */
	protected void clean(CleanDSSParam applyObj) throws Exception {
		// 校验用户信息是否存在
		if (existConf(getZKBase(applyObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId())) {
			DSSCommonConf commConf = null;
			if (getConfObj(applyObj.getUserId(), DSS_COMMON_ZK_CONF) instanceof DSSCommonConf) {
				commConf = (DSSCommonConf) getConfObj(applyObj.getUserId(),
						DSS_COMMON_ZK_CONF);
			}
			if (commConf != null) {
				if ("cleanOne".equals(applyObj.getApplyType())
						&& applyObj.getKey() != null
						&& !"".equals(applyObj.getKey())) {
					JedisCluster jc = getJedisClusterClient(commConf
							.getRedisHosts().split(";"));
					jc.decrBy(
							applyObj.getUserId() + applyObj.getServiceId(),
							deleteMongoValue(
									getServerAddressList(commConf.getHosts()),
									applyObj.getUserId(),
									CiperUtil.decrypt(PWD_KEY,
											commConf.getPassword()),
									applyObj.getServiceId(), applyObj.getKey()));
				} else if ("cleanAll".equals(applyObj.getApplyType())
						&& applyObj.getKey() == null) {
					// TODO
					JedisCluster jc = getJedisClusterClient(commConf
							.getRedisHosts().split(";"));
					reinitDB(getServerAddressList(commConf.getHosts()),
							applyObj.getUserId(),
							CiperUtil.decrypt(PWD_KEY, commConf.getPassword()),
							applyObj.getServiceId());
					jc.decrBy(
							applyObj.getUserId() + applyObj.getServiceId(),
							Long.parseLong(jc.get(applyObj.getUserId()
									+ applyObj.getServiceId())));
				} else {
					log.error("参数错误");
					throw new Exception("参数错误");
				}
			}

		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	/**
	 * 获取参数
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 */
	protected StatusResult getStatus(StatusParam applyObj) throws Exception {
		// 校验用户信息是否存在
		if (existConf(getZKBase(applyObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId())) {
			DSSCommonConf commConf = (DSSCommonConf) getConfObj(
					applyObj.getUserId(), DSS_COMMON_ZK_CONF);
			DSSConf dssConf = (DSSConf) getConfObj(applyObj.getUserId(),
					DSS_BASE_ZK_CONF + applyObj.getServiceId());
			if (commConf == null || dssConf == null) {
				log.error("系统异常-----commConf or dssConf为空");
				throw new Exception("系统异常-----commConf or dssConf为空");
			}
			long size = Long.parseLong(dssConf.getSize());
			JedisCluster jc = getJedisClusterClient(commConf.getRedisHosts()
					.split(";"));
			long usedSize = jc.incrBy(
					applyObj.getUserId() + applyObj.getServiceId(), 0);
			return getStatusResult(applyObj, size + "", usedSize + "");
		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	/**
	 * 获取上传记录
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	protected Map getRecord(RecordParam applyObj) throws Exception {
		// TODO
		if (existConf(getZKBase(applyObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId())) {
			DSSCommonConf commConf = (DSSCommonConf) getConfObj(
					applyObj.getUserId(), DSS_COMMON_ZK_CONF);
			if (commConf != null) {
				return getGridFSDBFile(
						getServerAddressList(commConf.getHosts()),
						applyObj.getUserId(),
						CiperUtil.decrypt(PWD_KEY, commConf.getPassword()),
						applyObj.getServiceId(), applyObj.getKey());
			} else {
				log.error("系统异常-----commConf为null");
				throw new Exception("系统异常-----commConf为null");
			}
		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	/**
	 * 修改容量、文件限制大小
	 * 
	 * @param applyObj
	 * @throws Exception
	 */
	protected void modifyAttribute(ModifyParam applyObj) throws Exception {
		// 校验用户信息是否存在
		if (existConf(getZKBase(applyObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId())) {
			DSSCommonConf commConf = (DSSCommonConf) getConfObj(
					applyObj.getUserId(), DSS_COMMON_ZK_CONF);
			if (commConf == null) {
				log.error("系统异常-----commConf为null");
				throw new Exception("系统异常-----commConf为null");
			}

			if (doModify(applyObj, commConf)) {
				log.error("申请变更大小小于已使用容量");
				throw new Exception("申请变更大小小于已使用容量");
			}

			int groupId = getGroupid(commConf.getHosts().split(";")[0]
					.split(":")[0]);
			DSSConf dssConf = (DSSConf) getConfObj(applyObj.getUserId(),
					DSS_BASE_ZK_CONF + applyObj.getServiceId());
			int dssNum = commConf.getHosts().split(";").length;
			// int perCapacity = Integer.parseInt(applyObj.getSize()) / dssNum
			// + Integer.parseInt(applyObj.getSize()) % dssNum;
			int differenceSzie = Integer.parseInt(applyObj.getSize())
					- Integer.parseInt(applyObj.getSize());
			int leftSize = getResourceLeftSize(groupId) - differenceSzie;
			if (leftSize < 0) {
				log.error(LEFT_SIZE_NOT_ENOUGH);
				throw new Exception(LEFT_SIZE_NOT_ENOUGH);
			}
			int limitFileSize = 0;
			if (applyObj.getLimitFileSize() != null
					&& !"".equals(applyObj.getLimitFileSize())) {
				limitFileSize = Integer.parseInt(applyObj.getLimitFileSize());
				dssConf.setLimitSize(limitFileSize + "");
			}
			int size = 0;
			if (applyObj.getSize() != null && !"".equals(applyObj.getSize())) {
				size = Integer.parseInt(applyObj.getSize());
				dssConf.setSize(size + "");
			}
			if (dssUserInstanceExist(applyObj.getUserId(),
					applyObj.getServiceId())) {
				updateBothSize(applyObj.getUserId(), applyObj.getServiceId(),
						size, limitFileSize);
				String dssConfStr = Obj2Json(dssConf);
				updateZKConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId(), dssConfStr);

				updateResourceLeftSize(groupId, leftSize);
			} else {
				log.error("用户信息数据不存在");
				throw new Exception("用户信息数据不存在");
			}

		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	/**
	 * 上传文件
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 */
	protected String uploadFile(UploadParam applyObj) throws Exception {
		if (existConf(getZKBase(applyObj.getUserId()), DSS_COMMON_ZK_CONF)
				&& existConf(getZKBase(applyObj.getUserId()), DSS_BASE_ZK_CONF
						+ applyObj.getServiceId())) {
			DSSCommonConf commConf = (DSSCommonConf) getConfObj(
					applyObj.getUserId(), DSS_COMMON_ZK_CONF);
			DSSConf dssConf = (DSSConf) getConfObj(applyObj.getUserId(),
					DSS_BASE_ZK_CONF + applyObj.getServiceId());
			if (commConf == null || dssConf == null) {
				log.error("系统异常，commConf or dssConf is null");
				throw new Exception("系统异常，commConf or dssConf is null");
			}
			JedisCluster jc = getJedisClusterClient(commConf.getRedisHosts()
					.split(";"));
			long fileSize = applyObj.getBytes().length;
			long usedSize = jc.incrBy(
					applyObj.getUserId() + applyObj.getServiceId(), 0);
			long size = m2b(Long.parseLong(dssConf.getSize()));
			long fileLimitSize = m2b(Long.parseLong(dssConf.getLimitSize()));
			if (!okSize(size - usedSize, fileSize)) {
				log.error("系统异常，容量不足");
				throw new Exception("系统异常，容量不足");
			}
			if (!okSize(fileLimitSize, fileSize)) {
				log.error("系统异常，文件大小超出限制");
				throw new Exception("系统异常，文件大小超出限制");
			}
			String key = save(applyObj, commConf);
			jc.incrBy(applyObj.getUserId() + applyObj.getServiceId(), fileSize);
			return key;
		} else {
			log.error("用户信息不存在");
			throw new Exception("用户信息不存在");
		}
	}

	private List<ServerAddress> getServerAddressList(String hostInfoStr) {
		List<ServerAddress> saList = new ArrayList<>();
		String[] hostInfoArray = hostInfoStr.split(";");
		String[] address = null;
		for (String hostInfo : hostInfoArray) {
			address = hostInfo.split(":");
			try {
				saList.add(new ServerAddress(address[0], Integer
						.parseInt(address[1])));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return saList;
	}

	private int getPwd() {
		Random random = new Random();
		return random.nextInt(899999) + 100000;
	}

	// private boolean databaseExists(MongoClient mc, String dbName) {
	// log.info("1--------------------------------------------校验");
	//
	// mc.getDatabase(dbName);
	// return true;
	// }

	protected CCSComponentOperationParam getZKBase(String userId) {
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPathType(PathType.READONLY);
		return op;
	}

	protected boolean existConf(CCSComponentOperationParam op, String path)
			throws PaasException {
		op.setPath(path);
		return iCCSComponentManageSv.exists(op);
	}

	protected void writeZKConf(CCSComponentOperationParam op, String path,
			String jsonStr) throws PaasException {
		op.setPath(path);
		iCCSComponentManageSv.add(op, jsonStr);
	}

	protected void updateZKConf(CCSComponentOperationParam op, String path,
			String jsonStr) throws Exception {
		op.setPath(path);
		try {
			iCCSComponentManageSv.modify(op, jsonStr);
		} catch (PaasException e) {
			log.error(e.getMessage());
			throw new Exception(e);
		}
	}

	private Object getConfObj(String userId, String path)
			throws JsonSyntaxException, PaasException {
		CCSComponentOperationParam op = getZKBase(userId);
		op.setPath(path);
		Gson gson = new Gson();
		if (DSS_COMMON_ZK_CONF.equals(path)) {
			if (!"".equals(iCCSComponentManageSv.get(op))) {
				return gson.fromJson(iCCSComponentManageSv.get(op),
						DSSCommonConf.class);
			}
		} else {
			if (!"".equals(iCCSComponentManageSv.get(op))) {
				return gson.fromJson(iCCSComponentManageSv.get(op),
						DSSConf.class);
			}
		}
		return null;
	}

	private void deleteConf(String userId, String path) throws PaasException {
		CCSComponentOperationParam op = getZKBase(userId);
		op.setPath(path);
		iCCSComponentManageSv.delete(op);
	}

	protected String Obj2Json(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj, obj.getClass());
	}

	protected DSSResult getResult(ApplyInfo applyObj) {
		DSSResult result = new DSSResult();
		result.setApplyType(applyObj.getApplyType());
		result.setResultCode(FAIL);
		result.setResultMsg(FAIL_MSG);
		result.setServiceId(applyObj.getServiceId());
		result.setUserId(applyObj.getUserId());
		return result;
	}

	protected DSSResult successResult(ApplyInfo applyObj) {
		DSSResult result = getResult(applyObj);
		result.setResultCode(SUCCESS);
		result.setResultMsg(SUCCESS_MSG);
		return result;
	}

	protected StatusResult getStatusResult(ApplyInfo applyObj, String size,
			String usedSize) {
		StatusResult result = new StatusResult();
		DSSResult re = successResult(applyObj);
		result.setApplyType(re.getApplyType());
		result.setResultCode(re.getResultCode());
		result.setResultMsg(re.getResultMsg());
		result.setServiceId(re.getServiceId());
		result.setUserId(re.getUserId());
		result.setSize(size);
		result.setUsedSize(usedSize);
		return result;
	}

	@SuppressWarnings("rawtypes")
	protected RecordResult getRecordResult(ApplyInfo applyObj, Map record) {
		RecordResult result = new RecordResult();
		DSSResult re = successResult(applyObj);
		result.setApplyType(re.getApplyType());
		result.setResultCode(re.getResultCode());
		result.setResultMsg(re.getResultMsg());
		result.setServiceId(re.getServiceId());
		result.setUserId(re.getUserId());
		result.setRecordJson(record);
		return result;
	}

	protected UploadResult getUploadResult(ApplyInfo applyObj, String key) {
		UploadResult result = new UploadResult();
		DSSResult re = successResult(applyObj);
		result.setApplyType(re.getApplyType());
		result.setKey(key);
		result.setServiceId(re.getServiceId());
		result.setUserId(re.getUserId());
		result.setResultCode(re.getResultCode());
		result.setResultMsg(re.getResultMsg());
		return result;
	}

	private String List2Str(List<String> list) {
		StringBuffer sb = new StringBuffer("");
		for (String str : list) {
			sb.append(";" + str);
		}
		return new String(sb.substring(1));
	}

	private String getHostStr(List<DssResourcePool> hostInfoList) {
		StringBuffer sb = new StringBuffer("");
		for (DssResourcePool drp : hostInfoList) {
			sb.append(";" + drp.getIp() + ":" + drp.getPort());
		}
		return sb.substring(1);
	}

	private List<String> getRedisStrList(DssMcsInfo dssMcsInfo) {
		List<String> result = new ArrayList<>();
		String hostInfo = dssMcsInfo.getMcsAddress();
		for (String str : hostInfo.split(";")) {
			result.add(str);
		}
		return result;
	}

	/**
	 * 获得最空闲的资源
	 * 
	 * @return List<DssResourcePool>
	 */
	private List<DssResourcePool> getBestDssResource() {
		DssResourcePoolMapper dpm = ServiceUtil
				.getMapper(DssResourcePoolMapper.class);
		DssResourcePoolCriteria dpmc = new DssResourcePoolCriteria();
		DssResourcePoolCriteria dpmcs = new DssResourcePoolCriteria();
		dpmcs.createCriteria().andStatusEqualTo(1);
		dpmcs.setOrderByClause("ifnull(left_size,0) desc");
		dpmcs.setLimitStart(0);
		dpmcs.setLimitEnd(1);
		int gid = dpm.selectByExample(dpmcs).get(0).getGroupId();
		dpmc.createCriteria().andGroupIdEqualTo(gid);
		return dpm.selectByExample(dpmc);
	}

	/**
	 * 获得资源
	 * 
	 * @return List<DssResourcePool>
	 */
	private List<DssResourcePool> getDssResource(int groupId) {
		DssResourcePoolMapper dpm = ServiceUtil
				.getMapper(DssResourcePoolMapper.class);
		DssResourcePoolCriteria dpmc = new DssResourcePoolCriteria();
		dpmc.createCriteria().andGroupIdEqualTo(groupId).andStatusEqualTo(1);
		return dpm.selectByExample(dpmc);
	}

	/**
	 * 根据ip查询group_id
	 * 
	 * @return
	 */
	private int getGroupid(String ip) {
		DssResourcePoolMapper dpm = ServiceUtil
				.getMapper(DssResourcePoolMapper.class);
		DssResourcePoolCriteria dpmc = new DssResourcePoolCriteria();
		dpmc.createCriteria().andIpEqualTo(ip);
		List<DssResourcePool> drpList = dpm.selectByExample(dpmc);
		return drpList.get(0).getGroupId();
	}

	/**
	 * 获得最空闲的资源
	 * 
	 * @return DssMcsInfo
	 */
	private DssMcsInfo getMcsResource() {
		DssMcsInfoMapper dmm = ServiceUtil.getMapper(DssMcsInfoMapper.class);
		DssMcsInfoCriteria dmmc = new DssMcsInfoCriteria();
		dmmc.createCriteria().andStatusEqualTo(1);
		dmmc.setOrderByClause("rand()");
		dmmc.setLimitStart(0);
		dmmc.setLimitEnd(1);
		return dmm.selectByExample(dmmc).get(0);
	}

	/**
	 * 根据address查询mcs_id
	 * 
	 * @return
	 */
	private int getMcsId(String address) {
		DssMcsInfoMapper dmm = ServiceUtil.getMapper(DssMcsInfoMapper.class);
		DssMcsInfoCriteria dmmc = new DssMcsInfoCriteria();
		dmmc.createCriteria().andMcsAddressEqualTo(address);
		return dmm.selectByExample(dmmc).get(0).getId();
	}

	/**
	 * 记录信息
	 * 
	 * @return
	 */
	private int insertInstance(DssUserInstance dssInstance) {
		DssUserInstanceMapper im = ServiceUtil
				.getMapper(DssUserInstanceMapper.class);
		return im.insert(dssInstance);
	}

	/**
	 * 记录信息
	 * 
	 * @return
	 */
	protected int dssInstanceExist(String userId, String serviceId) {
		DssUserInstanceMapper duim = ServiceUtil
				.getMapper(DssUserInstanceMapper.class);
		DssUserInstanceCriteria duic = new DssUserInstanceCriteria();
		duic.createCriteria().andUserIdEqualTo(userId)
				.andCollectionNameEqualTo(serviceId);
		return duim.selectByExample(duic).size();
	}

	/**
	 * 更新资源表
	 * 
	 * @return DssMcsInfo
	 */
	private int updateResourceLeftSize(int groupId, int leftSize) {
		DssResourcePoolMapper dmm = ServiceUtil
				.getMapper(DssResourcePoolMapper.class);
		DssResourcePoolCriteria drpc = new DssResourcePoolCriteria();
		drpc.createCriteria().andGroupIdEqualTo(groupId);
		DssResourcePool drp = new DssResourcePool();
		drp.setLeftSize(leftSize);
		return dmm.updateByExampleSelective(drp, drpc);
	}

	// protected void updateMcsResourceLeftSize(int groupId, int leftSize) {
	// DssResourcePoolMapper dmm =
	// ServiceUtil.getMapper(DssResourcePoolMapper.class);
	// DssResourcePoolCriteria drpc = new DssResourcePoolCriteria();
	// drpc.createCriteria().andGroupIdEqualTo(groupId);
	// DssResourcePool drp = new DssResourcePool();
	// drp.setLeftSize(leftSize);
	// dmm.updateByExampleSelective(drp, drpc);
	// }

	private boolean initMongoDB(String hostStr, String userId,
			String collectionName, String pwd) throws Exception {
		try {
			createDB(hostStr, userId, userId, pwd);
		} catch (Exception e) {
			log.error("创建数据库、用户异常/n" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		try {
			createCollection(hostStr,
					createCredentialList(userId, userId, pwd), userId,
					collectionName);
		} catch (Exception e) {
			log.error("创建collection异常/n" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		return true;
	}

	private boolean initMongoDB(DSSCommonConf commConf, String userId,
			String collectionName) throws Exception {
		try {
			createCollection(
					commConf.getHosts(),
					createCredentialList(userId, userId,
							CiperUtil.decrypt(PWD_KEY, commConf.getPassword())),
					userId, collectionName);
		} catch (Exception e) {
			log.error("创建collection异常/n" + e.getMessage());
			e.printStackTrace();
			throw new Exception(e.getMessage(), e);
		}
		return true;
	}

	private void deleteCollection(DSSCommonConf commConf, String userId,
			String collectionName) {
		MongoCredential credential = MongoCredential.createCredential(commConf
				.getUsername(), userId,
				CiperUtil.decrypt(PWD_KEY, commConf.getPassword())
						.toCharArray());
		MongoClient mc = new MongoClient(
				getServerAddressList(commConf.getHosts()),
				Arrays.asList(credential));
		MongoDatabase db = mc.getDatabase(userId);
		db.getCollection(collectionName).drop();
		mc.close();
	}

	private void deleteDBandUser(DSSCommonConf commConf, String userId) {
		MongoCredential credential = MongoCredential.createCredential(commConf
				.getUsername(), userId,
				CiperUtil.decrypt(PWD_KEY, commConf.getPassword())
						.toCharArray());
		MongoClient mc = new MongoClient(
				getServerAddressList(commConf.getHosts()),
				Arrays.asList(credential));
		MongoDatabase db = mc.getDatabase(userId);
		db.drop();
		mc.close();
		mc = new MongoClient(getServerAddressList(commConf.getHosts()),
				MongoClientOptions.builder().serverSelectionTimeout(50000)
						.build());
		db = mc.getDatabase("admin");
		BasicDBObject bobj = new BasicDBObject();
		bobj.put("_id", userId + "." + userId);
		db.getCollection("system.users").deleteOne(bobj);
		mc.close();
	}

	protected boolean verifyParam(Object obj) throws Exception {
		try {
			if (obj == null) {
				log.error("参数不能为null或者\"\"");
				throw new Exception("参数不能为null或者\"\"");
			}
			Map<String, Object> fieldsMap = getClassFields(obj);
			String value = null;
			for (String key : fieldsMap.keySet()) {
				value = fieldsMap.get(key).toString();
				if ("".equals(value) || value == null) {
					log.error("参数" + key + "不能为null或者\"\"");
					throw new Exception("参数" + key + "不能为null或者\"\"");
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException e) {
			log.error(e);
			throw new Exception(e);
		}
		return TRUE;
	}

	@SuppressWarnings("rawtypes")
	private Map<String, Object> getClassFields(Object obj)
			throws IllegalArgumentException, IllegalAccessException {
		List<Class> classList = new ArrayList<>();
		classList = getClass(obj.getClass(), classList);
		Map<String, Object> fieldsMap = new HashMap<>();
		for (Class c : classList) {
			for (Field f : c.getDeclaredFields()) {
				f.setAccessible(TRUE);
				fieldsMap.put(f.getName(), f.get(obj));
			}
		}
		return fieldsMap;
	}

	@SuppressWarnings("rawtypes")
	private List<Class> getClass(Class clazz, List<Class> classList) {
		if (clazz.getSuperclass() != null) {
			if (BaseInfo.class.getName()
					.equals(clazz.getSuperclass().getName())) {
				return classList;
			} else {
				classList.add(clazz.getSuperclass());
				return getClass(clazz.getSuperclass(), classList);
			}
		}
		return classList;
	}

	protected boolean verifyApplyType(String applyType, String thisType) {
		if (thisType.equals(applyType)) {
			return TRUE;
		} else {
			log.error("applyType wrong");
			return FALSE;
		}
	}

	private int getInstanceNum(String userId) {
		DssUserInstanceMapper duim = ServiceUtil
				.getMapper(DssUserInstanceMapper.class);
		DssUserInstanceCriteria duic = new DssUserInstanceCriteria();
		duic.createCriteria().andEndDateIsNull().andUserIdEqualTo(userId);
		return duim.selectByExample(duic).size();
	}

	private void addEndDateInstance(String userId, String serviceId) {
		DssUserInstanceMapper duim = ServiceUtil
				.getMapper(DssUserInstanceMapper.class);
		DssUserInstanceCriteria duic = new DssUserInstanceCriteria();
		duic.createCriteria().andEndDateIsNull().andUserIdEqualTo(userId)
				.andCollectionNameEqualTo(serviceId);
		DssUserInstance dui = new DssUserInstance();
		dui.setEndDate(new Timestamp(System.currentTimeMillis()));
		duim.updateByExampleSelective(dui, duic);
	}

	private void deleteMCSKey(String userId, String serviceId, String[] hosts) {
		if (hosts != null && userId != null && serviceId != null) {
			JedisCluster jc = getJedisClusterClient(hosts);
			jc.del(userId + serviceId);
		}
	}

	private JedisCluster getJedisClusterClient(String[] hosts) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		for (String address : hosts) {
			String[] ipAndPort = address.split(":");
			jedisClusterNodes.add(new HostAndPort(ipAndPort[0], Integer
					.parseInt(ipAndPort[1])));
			log.debug(address);
		}
		return new JedisCluster(jedisClusterNodes);
	}

	// private void verifyMongoUser(List<ServerAddress> seeds, String userId,
	// String pwd) {
	// System.out.println("密码是--------------------------" + pwd);
	// log.info("校1验---------------------------------------------");
	// MongoCredential credential = MongoCredential.createCredential(userId,
	// userId, pwd.toCharArray());
	// log.info("校2验---------------------------------------------");
	// List<MongoCredential> mcList = new ArrayList<MongoCredential>();
	// log.info("校3验---------------------------------------------");
	// mcList.add(credential);
	// log.info("校4验---------------------------------------------");
	// MongoClient mo = new MongoClient(seeds, mcList, MongoClientOptions
	// .builder().serverSelectionTimeout(50000).build());
	// log.info("校5验---------------------------------------------");
	// MongoDatabase db = mo.getDatabase(userId);
	// log.info("校6验---------------------------------------------");
	// db.getName();
	// log.info("校7验---------------------------------------------");
	// }

	private DB getDB(List<ServerAddress> seeds, String userId, String pwd) {
		MongoCredential credential = MongoCredential.createCredential(userId,
				userId, pwd.toCharArray());
		List<MongoCredential> mcList = new ArrayList<MongoCredential>();
		mcList.add(credential);
		MongoClient mo = new MongoClient(seeds, mcList, MongoClientOptions
				.builder().serverSelectionTimeout(50000).build());
		return mo.getDB(userId);
	}

	private long deleteMongoValue(List<ServerAddress> seeds, String userId,
			String pwd, String serviceId, String key) throws Exception {
		DB db = getDB(seeds, userId, pwd);
		GridFS fs = new GridFS(db, serviceId);
		GridFSDBFile dbFile = null;
		try {
			dbFile = fs.findOne(new ObjectId(key));
		} catch (IllegalArgumentException e) {
			log.error("无效的key", e);
			throw new Exception("无效的key!", e);
		}
		if (dbFile == null) {
			log.error("文件为null!");
			throw new Exception("文件为null!");
		}
		long size = dbFile.getLength();
		fs.remove(dbFile);
		return size;
	}

	private void reinitDB(List<ServerAddress> seeds, String userId, String pwd,
			String serviceId) throws Exception {
		try {
			DB db = getDB(seeds, userId, pwd);
			db.getCollection(serviceId).drop();
			db.getCollection(serviceId + ".files").drop();
			db.getCollection(serviceId + ".chunks").drop();
			if (!db.collectionExists(serviceId)) {
				DBObject dbo = new BasicDBObject();
				dbo.put("capped", false);
				dbo.put("autoIndexId", true);
				db.createCollection(serviceId, dbo);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e);
		}
	}

	@SuppressWarnings("rawtypes")
	private Map getGridFSDBFile(List<ServerAddress> seeds, String userId,
			String pwd, String serviceId, String key) throws Exception {
		DB db = getDB(seeds, userId, pwd);
		GridFS fs = new GridFS(db, serviceId);
		GridFSDBFile dbFile = null;
		try {
			dbFile = fs.findOne(new ObjectId(key));
		} catch (IllegalArgumentException e) {
			log.error("KEY非法", e);
			throw new Exception("KEY非法", e);
		}
		if (dbFile != null) {
			Map<String, String> recordJsonMap = new HashMap<String, String>();
			recordJsonMap.put("filename", dbFile.getFilename());
			recordJsonMap.put("contentType", dbFile.getContentType());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			recordJsonMap.put("uploadDate", sdf.format(dbFile.getUploadDate()));
			recordJsonMap.put("remark", dbFile.getMetaData().get("remark")
					.toString());
			return recordJsonMap;
		} else {
			log.error("文件为null!");
			throw new Exception("文件为null!");
		}
	}

	private void updateBothSize(String userId, String serviceId, int size,
			int limitFileSize) {
		DssUserInstanceMapper duim = ServiceUtil
				.getMapper(DssUserInstanceMapper.class);
		DssUserInstanceCriteria duic = new DssUserInstanceCriteria();
		duic.createCriteria().andUserIdEqualTo(userId)
				.andCollectionNameEqualTo(serviceId);
		DssUserInstance dui = new DssUserInstance();
		dui.setOssSize(Double.parseDouble(size + ""));
		dui.setFileLimitSize(Double.parseDouble(limitFileSize + ""));
		duim.updateByExampleSelective(dui, duic);
	}

	// private void updateLimitFileSize(String userId, String serviceId,
	// int limitFileSize) {
	// DssUserInstanceMapper duim = ServiceUtil
	// .getMapper(DssUserInstanceMapper.class);
	// DssUserInstanceCriteria duic = new DssUserInstanceCriteria();
	// duic.createCriteria().andUserIdEqualTo(userId)
	// .andCollectionNameEqualTo(serviceId);
	// DssUserInstance dui = new DssUserInstance();
	// dui.setFileLimitSize(Double.parseDouble(limitFileSize + ""));
	// duim.updateByExampleSelective(dui, duic);
	// }

	private List<MongoCredential> createCredentialList(String username,
			String dbName, String pwd) {
		MongoCredential credential = MongoCredential.createCredential(username,
				dbName, pwd.toCharArray());
		List<MongoCredential> mcList = new ArrayList<MongoCredential>();
		mcList.add(credential);
		return mcList;
	}

	private void createDB(String hostStr, String dbName, String username,
			String pwd) {
		MongoClient mc = new MongoClient(getServerAddressList(hostStr),
				MongoClientOptions.builder().serverSelectionTimeout(50000)
						.build());
		MongoDatabase db = mc.getDatabase(dbName);
		if (!userExist(hostStr, dbName, username)) {
			Map<String, Object> commandArguments = new BasicDBObject();
			commandArguments.put("createUser", username);
			commandArguments.put("pwd", pwd);
			String[] roles = { "readWrite" };
			commandArguments.put("roles", roles);
			BasicDBObject command = new BasicDBObject(commandArguments);
			db.runCommand(command);
		}
		mc.close();
	}

	private void createCollection(String hostStr,
			List<MongoCredential> credentialList, String dbName,
			String collectionName) {
		if (!collectionExist(getServerAddressList(hostStr), credentialList,
				MongoClientOptions.builder().serverSelectionTimeout(50000)
						.build(), dbName, collectionName)) {
			MongoClient mo = new MongoClient(getServerAddressList(hostStr),
					credentialList, MongoClientOptions.builder()
							.serverSelectionTimeout(50000).build());
			mo.getDatabase(dbName).createCollection(collectionName);
		}
	}

	private boolean userExist(String hostStr, String dbName, String username) {
		MongoClient mo = new MongoClient(getServerAddressList(hostStr),
				MongoClientOptions.builder().serverSelectionTimeout(50000)
						.build());
		MongoDatabase db = mo.getDatabase(ADMIN);
		BasicDBObject bobj = new BasicDBObject();
		bobj.put("_id", dbName + "." + username);
		for (Document d : db.getCollection(USERS).find(bobj)) {
			if (username.equals(d.get(USER)) || dbName.equals(d.get(DB))) {
				return true;
			}
		}
		return false;
	}

	private boolean collectionExist(List<ServerAddress> serverAddressList,
			List<MongoCredential> credentialList,
			MongoClientOptions mongoClientOptions, String dbName,
			String collectionName) {
		MongoClient mongo = new MongoClient(serverAddressList, credentialList,
				mongoClientOptions);
		MongoDatabase mongoDatabase = mongo.getDatabase(dbName);
		for (String name : mongoDatabase.listCollectionNames()) {
			if (collectionName.equals(name)) {
				log.info(name + "已存在");
				mongo.close();
				return true;
			}
		}
		mongo.close();
		return false;
	}

	private int getResourceLeftSize(int groupId) {
		DssResourcePoolMapper dmm = ServiceUtil
				.getMapper(DssResourcePoolMapper.class);
		DssResourcePoolCriteria drpc = new DssResourcePoolCriteria();
		drpc.createCriteria().andGroupIdEqualTo(groupId);
		return dmm.selectByExample(drpc).get(0).getLeftSize();
	}

	private boolean dssUserInstanceExist(String userId, String serviceId) {
		int num = dssInstanceExist(userId, serviceId);
		if (num == 1) {
			return TRUE;
		}
		return FALSE;
	}

	private String save(UploadParam applyObj, DSSCommonConf commConf)
			throws Exception {
		DB db = null;
		GridFS fs = null;
		try {
			db = getDB(getServerAddressList(commConf.getHosts()),
					applyObj.getUserId(),
					CiperUtil.decrypt(PWD_KEY, commConf.getPassword()));
			fs = new GridFS(db, applyObj.getServiceId());

		} catch (Exception e) {
			log.error("获取数据库失败" + e.getMessage(), e);
			throw new Exception("获取数据库失败" + e.getMessage(), e);
		}
		GridFSInputFile dbFile;
		try {
			dbFile = fs.createFile(applyObj.getBytes());
			DBObject dbo = new BasicDBObject();
			dbo.put(REMARK, applyObj.getRemark());
			dbFile.setMetaData(dbo);
			dbFile.setFilename(applyObj.getFileName());
			dbFile.setContentType(applyObj.getFileType());
			dbFile.save();
			return dbFile.getId().toString();
		} catch (Exception e) {
			log.error("文件存储异常" + e.getMessage());
			throw new Exception(e.getMessage(), e);
		}
	}

	private Long m2b(long mSize) {
		BigDecimal mbd = new BigDecimal(mSize);
		mbd = mbd.multiply(new BigDecimal(1024).multiply(new BigDecimal(1024)));
		return mbd.longValue();
	}

	private boolean okSize(long a, long b) {
		return (a - b) >= 0 ? TRUE : FALSE;
	}

	private boolean doModify(ModifyParam applyObj, DSSCommonConf commConf) {
		JedisCluster jc = getJedisClusterClient(commConf.getRedisHosts().split(
				";"));
		Long nowSize = Long.parseLong(jc.get(applyObj.getUserId()
				+ applyObj.getServiceId()));
		Long size = Long.parseLong(applyObj.getSize()) * 1024L * 1024L;
		return (size - nowSize) < 0;
	}
}
