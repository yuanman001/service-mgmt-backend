package com.ai.paas.ipaas.dss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.dss.dto.DSSConf;
import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CancelDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CleanDSSParam;
import com.ai.paas.ipaas.dss.manage.param.DSSResult;
import com.ai.paas.ipaas.dss.manage.param.Func;
import com.ai.paas.ipaas.dss.manage.param.ModifyParam;
import com.ai.paas.ipaas.dss.manage.param.RecordParam;
import com.ai.paas.ipaas.dss.manage.param.RecordResult;
import com.ai.paas.ipaas.dss.manage.param.StatusParam;
import com.ai.paas.ipaas.dss.manage.param.StatusResult;
import com.ai.paas.ipaas.dss.manage.param.UploadParam;
import com.ai.paas.ipaas.dss.service.IDSSSv;

@Service
@Transactional(rollbackFor = Exception.class)
public class DSSvImpl implements IDSSSv {

	private static final Logger log = LogManager.getLogger(DSSvImpl.class
			.getName());

	@Autowired
	private ICCSComponentManageSv iCCSComponentManageSv;
	@Autowired
	private DSSSvImplHelper dssvImplHelper;

	private static final String GET_FUNCLIST = "getFuncList";
	private static final String GET_FUNCLIST_URL = "/dss/manage/getFuncList";
	private static final String CREATE = "create";
	private static final String CREATE_URL = "/dss/manage/create";
	private static final String CANCEL = "cancel";
	private static final String CLEAN_ALL = "cleanAll";
	private static final String CLEAN_ONE = "cleanOne";
	private static final String GET_STATUS = "getStatus";
	private static final String GET_RECORD = "getRecord";
	private static final String MODIFY = "modify";
	private static final String UPLOAD = "upload";

	// private DSSResult result;

	/**
	 * createDSS创建DSS方法
	 */
	@Override
	public DSSResult createDSS(ApplyDSSParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), CREATE);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			int capacity = Integer.parseInt(applyObj.getCapacity());
			int fileLimitSize = Integer.parseInt(applyObj.getSingleFileSize());
			if (capacity <= 0 || fileLimitSize <= 0) {
				log.error("申请容量或单文件限制大小超出可申请范围！");
				log.error("capacity:" + capacity + "m");
				log.error("fileLimitSize:" + fileLimitSize + "m");
				throw new Exception("申请容量或单文件限制大小超出可申请范围！" + "/n" + "capacity:"
						+ capacity + "m" + "/n" + "fileLimitSize:"
						+ fileLimitSize + "m");
			}
			int res = 0;
			// 校验serviceId是否存在
			// 如果存在返回成功结果
			log.info("<3>校验serviceId是否存在");
			if ((res = dssvImplHelper.dssInstanceExist(applyObj.getUserId(),
					applyObj.getServiceId())) == 1) {
				// 若存在直接返回
				return dssvImplHelper.successResult(applyObj);
			}
			// 校验ZK中是否存在userId的DSS COMMON节点
			// 若存在创建新的ZK COLLECTION节点
			// 若不存在创建ZK DSS COMMON节点、COLLECTION节点
			log.info("<4>判断新老用户");
			if (dssvImplHelper.existConf(
					dssvImplHelper.getZKBase(applyObj.getUserId()),
					dssvImplHelper.DSS_COMMON_ZK_CONF)) {
				log.info("<5>老用户创建相应collection");
				if (!dssvImplHelper.existConf(
						dssvImplHelper.getZKBase(applyObj.getUserId()),
						dssvImplHelper.DSS_BASE_ZK_CONF
								+ applyObj.getServiceId())) {
					DSSConf dssConfig = dssvImplHelper
							.createAnotherDBUserCollection(applyObj);
					log.info("<6>校验返回待存入ZK信息");
					dssvImplHelper.verifyParam(dssConfig);
					log.info("<7>格式化待存入ZK信息");
					String dssConf = dssvImplHelper.Obj2Json(dssConfig);
					log.info("<8>信息存入ZK");
					dssvImplHelper.writeZKConf(
							dssvImplHelper.getZKBase(applyObj.getUserId()),
							dssvImplHelper.DSS_BASE_ZK_CONF
									+ applyObj.getServiceId(), dssConf);
				}
				log.info("<9>返回成功结果");
				return dssvImplHelper.successResult(applyObj);
			} else {
				log.info("<5>新用户创建相应common信息与collection");
				Object[] resultArray = dssvImplHelper
						.createDBUserCollection(applyObj);
				// 校验resultArray信息
				dssvImplHelper.verifyParam(resultArray);
				log.info("<6>校验common信息与collection");
				for (Object obj : resultArray) {
					dssvImplHelper.verifyParam(obj);
				}
				log.info("<7>格式化待存入ZK common信息与collection");
				String dssCommConf = dssvImplHelper.Obj2Json(resultArray[0]);
				if (!dssvImplHelper.existConf(
						dssvImplHelper.getZKBase(applyObj.getUserId()),
						dssvImplHelper.DSS_BASE_ZK_CONF
								+ applyObj.getServiceId())) {
					String dssConf = dssvImplHelper.Obj2Json(resultArray[1]);
					dssvImplHelper.writeZKConf(
							dssvImplHelper.getZKBase(applyObj.getUserId()),
							dssvImplHelper.DSS_BASE_ZK_CONF
									+ applyObj.getServiceId(), dssConf);
				}
				log.info("<8>存入ZK common信息与collection");
				dssvImplHelper.writeZKConf(
						dssvImplHelper.getZKBase(applyObj.getUserId()),
						dssvImplHelper.DSS_COMMON_ZK_CONF, dssCommConf);
				log.info("<9>返回成功结果");
				return dssvImplHelper.successResult(applyObj);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}

	}

	/**
	 * getFuncList获取功能列表方法
	 */
	@Override
	public List<Func> getFuncList() throws Exception {
		List<Func> funcs = new ArrayList<>();
		Func f1 = new Func(GET_FUNCLIST, GET_FUNCLIST_URL);
		funcs.add(f1);
		Func f2 = new Func(CREATE, CREATE_URL);
		funcs.add(f2);
		return funcs;
	}

	@Override
	public DSSResult cancelDSS(CancelDSSParam cancelObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(cancelObj.getApplyType(), CANCEL);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(cancelObj);
			// 执行注销
			log.info("<3>开始执行注销");
			dssvImplHelper.deleteCollection(cancelObj);
			log.info("<4>返回成功结果");
			return dssvImplHelper.successResult(cancelObj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	@Override
	public DSSResult cleanDSS(CleanDSSParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), CLEAN_ALL);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			log.info("<3>开始执行清理");
			dssvImplHelper.clean(applyObj);
			log.info("<4>返回成功结果");
			return dssvImplHelper.successResult(applyObj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}

	}

	@Override
	public DSSResult cleanOneDSS(CleanDSSParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), CLEAN_ONE);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			log.info("<3>开始执行清理");
			dssvImplHelper.clean(applyObj);
			log.info("<4>返回成功结果");
			return dssvImplHelper.successResult(applyObj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	@Override
	public StatusResult getStatusDSS(StatusParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), GET_STATUS);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			log.info("<3>开始执行，并返回结果");
			return dssvImplHelper.getStatus(applyObj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	@Override
	public RecordResult getRecordDSS(RecordParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), GET_RECORD);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			log.info("<3>开始执行，并返回结果");
			return dssvImplHelper.getRecordResult(applyObj,
					dssvImplHelper.getRecord(applyObj));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}

	}

	@Override
	public DSSResult modifyDSS(ModifyParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), MODIFY);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			int capacity = Integer.parseInt(applyObj.getSize());
			int fileLimitSize = Integer.parseInt(applyObj.getLimitFileSize());
			if (capacity <= 0 || fileLimitSize <= 0) {
				log.error("申请容量或单文件限制大小超出可申请范围！");
				log.error("capacity:" + capacity + "m");
				log.error("fileLimitSize:" + fileLimitSize + "m");
				throw new Exception("申请容量或单文件限制大小超出可申请范围！" + "/n" + "capacity:"
						+ capacity + "m" + "/n" + "fileLimitSize:"
						+ fileLimitSize + "m");
			}
			dssvImplHelper.modifyAttribute(applyObj);
			return dssvImplHelper.successResult(applyObj);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	@Override
	public DSSResult uploadFile(UploadParam applyObj) throws Exception {
		try {
			// 校验请求类型
			log.info("<1>校验请求类型");
			dssvImplHelper.verifyApplyType(applyObj.getApplyType(), UPLOAD);
			// 校验请求参数是否合法
			log.info("<2>校验请求参数");
			dssvImplHelper.verifyParam(applyObj);
			log.info("<3>开始执行，并返回结果");
			return dssvImplHelper.getUploadResult(applyObj,
					dssvImplHelper.uploadFile(applyObj));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new Exception(e);
		}
	}

	public static void main(String[] args) {

	}

}
