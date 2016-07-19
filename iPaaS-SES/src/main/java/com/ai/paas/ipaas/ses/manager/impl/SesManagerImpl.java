package com.ai.paas.ipaas.ses.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.ai.paas.ipaas.ses.manage.rest.interfaces.ISearchEngineServiceManager;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.ISesManage;
import com.ai.paas.ipaas.ses.service.vo.SesMappingApply;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApply;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApplyResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

@Service
public class SesManagerImpl implements ISearchEngineServiceManager {
	private static transient final Logger LOGGER = Logger
			.getLogger(SesManagerImpl.class);
	@Autowired
	ISesManage sesSrv;

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * 开通ses服务. 此处不在做输入参数的检查,放在rest层进行检查 为实现调用幂等性，先检查是否存在
	 */
	@Override
	public String create(String createApply) {

		Gson gson = new Gson();

		SesSrvApply sesApplyParam = gson.fromJson(createApply,
				SesSrvApply.class);
		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(sesApplyParam.getUserId());
		result.setServiceId(sesApplyParam.getServiceId());
		result.setApplyType(sesApplyParam.getApplyType());

		try {
			LOGGER.info("create ses service begin..........");
			sesSrv.createSesService(sesApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			LOGGER.error("create ses service error..........", e);
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	/**
	 * 创建数据模型
	 */
	@Override
	public String mapping(String mappingApply) {

		Gson gson = new Gson();

		SesMappingApply sesApplyParam = gson.fromJson(mappingApply,
				SesMappingApply.class);

		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(sesApplyParam.getUserId());
		result.setServiceId(sesApplyParam.getServiceId());
		result.setApplyType(sesApplyParam.getApplyType());

		try {
			sesSrv.putMapping(sesApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg(e.getMessage());
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	/**
	 * 创建索引.
	 */
	@Override
	public String index(String mappingApply) {
		
		Gson gson = new Gson();

		SesMappingApply sesApplyParam = gson.fromJson(mappingApply,
				SesMappingApply.class);

		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(sesApplyParam.getUserId());
		result.setServiceId(sesApplyParam.getServiceId());
		result.setApplyType(sesApplyParam.getApplyType());

		try {
			sesSrv.createIndex(sesApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	@Override
	public String start(String operateApply) {
		Gson gson = new Gson();

		ApplyInfo operateApplyParam = gson.fromJson(operateApply,
				ApplyInfo.class);

		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());

		try {
			sesSrv.start(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	@Override
	public String stop(String operateApply) {
		
		Gson gson = new Gson();

		ApplyInfo operateApplyParam = gson.fromJson(operateApply,
				ApplyInfo.class);

		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());

		try {
			sesSrv.stop(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	@Override
	public String recycle(String operateApply) {
		
		Gson gson = new Gson();

		ApplyInfo operateApplyParam = gson.fromJson(operateApply,
				ApplyInfo.class);

		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(operateApplyParam.getUserId());
		result.setServiceId(operateApplyParam.getServiceId());
		result.setApplyType(operateApplyParam.getApplyType());

		try {
			sesSrv.recycle(operateApplyParam);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (PaasException e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result); 
	}
}
