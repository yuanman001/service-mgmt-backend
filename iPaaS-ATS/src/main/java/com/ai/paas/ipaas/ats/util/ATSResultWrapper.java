package com.ai.paas.ipaas.ats.util;

import com.ai.paas.ipaas.rpc.api.vo.BaseResult;
import com.google.gson.Gson;

public class ATSResultWrapper {

	public static String wrapRestfulResult(String resultCode, String message) {
		Gson gson = new Gson();
		BaseResult applyResult = new BaseResult();
		applyResult.setResultCode(resultCode);
		applyResult.setResultMsg(message);
		return gson.toJson(applyResult);
	}
}
