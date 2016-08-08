package com.ai.paas.ipaas.mds.manage.util;

import java.util.List;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.manage.vo.MsgLstSubPathApplyResult;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApplyResult;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.mds.manage.vo.MsgSubApplyResult;
import com.ai.paas.ipaas.mds.manage.vo.MdsSrvApplyResult;
//import com.ai.paas.ipaas.rpc.api.vo.ApplyResult;
import com.google.gson.Gson;

public class MDSResultWrapper {
	public static String wrapRestfulResult(String resultCode, String message,
			MsgSrvApply apply) {
		Gson gson = new Gson();
		MdsSrvApplyResult applyResult = new MdsSrvApplyResult();
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
	
	public static String wrapSubRestfulResult(String resultCode, String message,
			MsgSrvApply apply, String isExis) {
		Gson gson = new Gson();
		MsgSubApplyResult applyResult = new MsgSubApplyResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		applyResult.setIsExis(isExis);
		return gson.toJson(applyResult);
	}
	
	public static String wraplistSubPathfulResult(String resultCode, String message,
			MsgSrvApply apply, List<String> listSubPath) {
		Gson gson = new Gson();
		MsgLstSubPathApplyResult applyResult = new MsgLstSubPathApplyResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		applyResult.setListSubPath(listSubPath);
		return gson.toJson(applyResult);
	}
	
	public static String wrapSubRestfulResult(String resultCode, String message,
			MdsUserSubscribe apply) {
		Gson gson = new Gson();
		MdsSrvApplyResult applyResult = new MdsSrvApplyResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		if (null != apply) {
			applyResult.setUserId(apply.getUserId());
		}
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
