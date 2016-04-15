package com.ai.paas.ipaas.rcs.util;

import java.util.Map;

import com.ai.paas.ipaas.rcs.service.constant.RcsConstants;
import com.google.gson.Gson;

public class RcsParamUtil {
	
	@SuppressWarnings("unchecked")
	public static Map<String ,String> getParamMap(String applyParam) {
		Gson gson = new Gson();
		return gson.fromJson(applyParam, Map.class);
	}
	 private static String successfulParam(String applyParam) {
		Map<String ,String> paramMap = getParamMap(applyParam);
		paramMap.remove(RcsConstants.USER_ID);
		paramMap.remove(RcsConstants.CLUSTER_TYPE);
		paramMap.remove(RcsConstants.APPLY_TYPE);
		paramMap.remove(RcsConstants.SERVICE_ID);
		paramMap.put(RcsConstants.RESULT_CODE, RcsConstants.SUCCESS_CODE);
		paramMap.put(RcsConstants.RESULT_MSG, RcsConstants.SUCCESS_INFO);
		Gson gson = new Gson();
		return gson.toJson(paramMap);
	}
	
	 private static String failedParam(String applyParam) {
		Map<String ,String> paramMap = getParamMap(applyParam);
		paramMap.remove(RcsConstants.USER_ID);
		paramMap.remove(RcsConstants.CLUSTER_TYPE);
		paramMap.remove(RcsConstants.APPLY_TYPE);
		paramMap.remove(RcsConstants.SERVICE_ID);
		paramMap.put(RcsConstants.RESULT_CODE, RcsConstants.FAIL_CODE);
		paramMap.put(RcsConstants.RESULT_MSG, RcsConstants.FAIL_INFO);
		Gson gson = new Gson();
		return gson.toJson(paramMap);
	}
	
	public static String getReturn(String applyParam,String res){
		if(RcsConstants.SUCCESS_FLAG.equals(res))
			return successfulParam(applyParam);
		return failedParam(applyParam);
	}
	

}
