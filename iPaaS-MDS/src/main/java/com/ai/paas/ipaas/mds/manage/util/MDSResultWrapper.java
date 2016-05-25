package com.ai.paas.ipaas.mds.manage.util;

import java.util.List;

import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApplyResult;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.rpc.api.vo.BaseResult;
import com.google.gson.Gson;

public class MDSResultWrapper {
	public static String wrapRestfulResult(String resultCode, String message,
			MsgSrvApply apply) {
		Gson gson = new Gson();
		BaseResult applyResult = new BaseResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		if (null != apply) {
			applyResult.setUserId(apply.getUserId());
			applyResult.setApplyType(apply.getApplyType());
			applyResult.setServiceId(apply.getServiceId());
		}
		return gson.toJson(applyResult);
	}

	public static String wrapRestfulResult(String resultCode, String message,
			MsgSrvApply apply, List<MsgSrvUsageApplyResult> topicUsage) {
		Gson gson = new Gson();
		MsgSrvApplyResult applyResult = new MsgSrvApplyResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		if (null != apply) {
			applyResult.setUserId(apply.getUserId());
			applyResult.setApplyType(apply.getApplyType());
			applyResult.setServiceId(apply.getServiceId());
		}
		applyResult.setTopicUsage(topicUsage);
		return gson.toJson(applyResult);
	}

	public static String wrapRestfulResult(String resultCode, String message,
			MsgSrvApply apply, String topicMessage) {
		Gson gson = new Gson();
		MsgSrvApplyResult applyResult = new MsgSrvApplyResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		if (null != apply) {
			applyResult.setUserId(apply.getUserId());
			applyResult.setApplyType(apply.getApplyType());
			applyResult.setServiceId(apply.getServiceId());
		}
		applyResult.setTopicMessage(topicMessage);
		return gson.toJson(applyResult);
	}
}
