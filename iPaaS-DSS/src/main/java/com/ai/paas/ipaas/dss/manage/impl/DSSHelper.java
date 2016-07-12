package com.ai.paas.ipaas.dss.manage.impl;

import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CancelDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CleanDSSParam;
import com.ai.paas.ipaas.dss.manage.param.DSSResult;
import com.ai.paas.ipaas.dss.manage.param.ModifyParam;
import com.ai.paas.ipaas.dss.manage.param.RecordParam;
import com.ai.paas.ipaas.dss.manage.param.StatusParam;
import com.ai.paas.ipaas.dss.manage.param.UploadParam;
import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;
import com.google.gson.Gson;

public class DSSHelper {

	private static final String FAIL = "999999";
	private static final String GET_FUNCLIST = "getFuncList";

	public static ApplyDSSParam getRequestParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, ApplyDSSParam.class);
	}

	public static CancelDSSParam getCancelDSSParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, CancelDSSParam.class);
	}

	public static String getDSSResult(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj, obj.getClass());
	}

	public static CleanDSSParam getCleanDSSParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, CleanDSSParam.class);
	}

	public static StatusParam getStatusParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, StatusParam.class);
	}

	public static RecordParam getRecordParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, RecordParam.class);
	}

	public static ModifyParam getModifyParamm(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, ModifyParam.class);
	}

	public static UploadParam getUploadParam(String param) {
		Gson gson = new Gson();
		return gson.fromJson(param, UploadParam.class);
	}

	public static DSSResult getResult(ApplyInfo applyObj, String failMsg) {
		DSSResult result = new DSSResult();
		result.setApplyType(applyObj.getApplyType());
		result.setResultCode(FAIL);
		result.setResultMsg(failMsg);
		result.setServiceId(applyObj.getServiceId());
		result.setUserId(applyObj.getUserId());
		return result;
	}

	public static DSSResult getResult(String failMsg) {
		DSSResult result = new DSSResult();
		result.setApplyType(GET_FUNCLIST);
		result.setResultCode(FAIL);
		result.setResultMsg(failMsg);
		return result;
	}

}
