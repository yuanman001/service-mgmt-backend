package com.ai.paas.ipaas.ses.manager.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.ses.manage.rest.interfaces.IRPCIKDictionary;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.IIKDictonary;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApplyResult;
import com.ai.paas.ipaas.util.CloneTool;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.vo.ses.RPCDictionay;
import com.ai.paas.ipaas.vo.ses.SesUserIndexWord;
import com.ai.paas.ipaas.vo.ses.SesUserStopWord;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class RPCIKDictionaryImpl implements IRPCIKDictionary {
	@Autowired
	IIKDictonary ikDict;

	@Override
	public void saveDictonaryWord(RPCDictionay rpcDict) {
		ikDict.saveDictonaryWord(
				convertIndexWord(rpcDict.getIndexWordList(),
						rpcDict.getUserId(), rpcDict.getServiceId()),
				convertStopWord(rpcDict.getStopWordList(), rpcDict.getUserId(),
						rpcDict.getServiceId()), rpcDict.getUserId(), rpcDict
						.getServiceId());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List> getUserDictionary(String userId, String serviceId,
			Integer start, Integer rows) {
		Map<String, List> result = ikDict.getUserDictionary(userId, serviceId,
				start, rows);
		if (null == result)
			return null;
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> indexWordList = result
				.get("allIndexWordList");
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> stopWordList = result
				.get("allStopWordList");
		Map<String, List> userDict = new HashMap<String, List>();
		userDict.put("allIndexWordList", convertRestIndexWord(indexWordList));
		userDict.put("allStopWordList", convertRestStopWord(stopWordList));
		return userDict;
	}

	private List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> convertStopWord(
			String stopWordList, String userId, String serviceId) {
		if (StringUtil.isBlank(stopWordList))
			return null;
		String[] splits = stopWordList.split(" ");
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> result = new ArrayList<>();
		com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord userStopWord = null;
		for (String stopWord : splits) {
			userStopWord = new com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord();
			userStopWord.setWord(stopWord);
			userStopWord.setUserId(userId);
			userStopWord.setServiceId(serviceId);
			userStopWord.setCreateTime(createTime);
			result.add(userStopWord);
		}
		return result;
	}

	private List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> convertIndexWord(
			String indexWordList, String userId, String serviceId) {
		if (StringUtil.isBlank(indexWordList))
			return null;
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> result = new ArrayList<>();
		String[] splits = indexWordList.split(" ");
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		for (String indexWord : splits) {
			com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord userIndexWord = new com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord();
			userIndexWord.setWord(indexWord);
			userIndexWord.setUserId(userId);
			userIndexWord.setServiceId(serviceId);
			userIndexWord.setCreateTime(createTime);
			// 这里太慢了，自己初始化
			result.add(userIndexWord);
		}
		return result;
	}

	private List<SesUserStopWord> convertRestStopWord(
			List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> stopWordList) {
		if (null == stopWordList || stopWordList.size() <= 0)
			return null;
		List<SesUserStopWord> result = new ArrayList<>();
		for (com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord stopWord : stopWordList) {
			result.add(CloneTool.<SesUserStopWord> clone(stopWord,
					SesUserStopWord.class));
		}
		return result;
	}

	private List<SesUserIndexWord> convertRestIndexWord(
			List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> indexWordList) {
		if (null == indexWordList || indexWordList.size() <= 0)
			return null;
		List<SesUserIndexWord> result = new ArrayList<>();
		for (com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord indexWord : indexWordList) {
			result.add(CloneTool.<SesUserIndexWord> clone(indexWord,
					SesUserIndexWord.class));
		}
		return result;
	}

	@Override
	public String saveAllStopWords(String allStopWords) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(allStopWords, JsonObject.class);
		String userId = json.get("userId").getAsString();
		String serviceId = json.get("serviceId").getAsString();
		String words = json.get("words").getAsString();
		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(userId);
		result.setServiceId(serviceId);
		result.setApplyType("saveAllStopWords");

		try {
			ikDict.saveAllStopWords(userId, serviceId, words);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (Exception e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	@Override
	public String saveAllIndexWords(String allIndexWords) {
		Gson gson = new Gson();
		JsonObject json = gson.fromJson(allIndexWords, JsonObject.class);
		String userId = json.get("userId").getAsString();
		String serviceId = json.get("serviceId").getAsString();
		String words = json.get("words").getAsString();
		SesSrvApplyResult result = new SesSrvApplyResult();

		result.setUserId(userId);
		result.setServiceId(serviceId);
		result.setApplyType("saveAllIndexWords");

		try {
			ikDict.saveAllIndexWords(userId, serviceId, words);
			result.setResultCode(SesConstants.SUCCESS_CODE);
			result.setResultMsg("success");
		} catch (Exception e) {
			result.setResultCode(SesConstants.FAIL_CODE);
			result.setResultMsg("fail");
			return new Gson().toJson(result);
		}
		return new Gson().toJson(result);
	}

	@Override
	public String getUserIndexWords(String userId, String serviceId,
			Integer start, Integer rows) {
		return ikDict.getUserIndexWords(userId, serviceId, start, rows);
	}

	@Override
	public String getUserStopWords(String userId, String serviceId,
			Integer start, Integer rows) {
		return ikDict.getUserStopWords(userId, serviceId, start, rows);
	}

	@Override
	public void clearAllIndexWords(String userId, String serviceId) {
		ikDict.clearAllIndexWords(userId, serviceId);
	}

	@Override
	public void clearAllStopWords(String userId, String serviceId) {
		ikDict.clearAllStopWords(userId, serviceId);
	}

}
