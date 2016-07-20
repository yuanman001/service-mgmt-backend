package com.ai.paas.ipaas.ses.manager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.ses.manage.rest.interfaces.IIKDictionary;
import com.ai.paas.ipaas.util.CloneTool;
import com.ai.paas.ipaas.vo.ses.SesUserIndexWord;
import com.ai.paas.ipaas.vo.ses.SesUserStopWord;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class IKDictionaryImpl implements IIKDictionary {
	@Autowired
	com.ai.paas.ipaas.ses.service.interfaces.IIKDictonary ikDict;

	@Override
	public void saveDictonaryWord(List<SesUserIndexWord> indexWordList,
			List<SesUserStopWord> stopWordList, String userId, String serviceId) {
		ikDict.saveDictonaryWord(convertIndexWord(indexWordList),
				convertStopWord(stopWordList), userId, serviceId);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Map<String, List> getUserDictionary(String userId, String serviceId) {
		Map<String, List> result = ikDict.getUserDictionary(userId, serviceId);
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
			List<SesUserStopWord> stopWordList) {
		if (null == stopWordList || stopWordList.size() <= 0)
			return null;
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> result = new ArrayList<>();
		for (SesUserStopWord stopWord : stopWordList) {
			result.add(CloneTool
					.<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord> clone(
							stopWord,
							com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord.class));
		}
		return result;
	}

	private List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> convertIndexWord(
			List<SesUserIndexWord> indexWordList) {
		if (null == indexWordList || indexWordList.size() <= 0)
			return null;
		List<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> result = new ArrayList<>();
		for (SesUserIndexWord indexWord : indexWordList) {
			result.add(CloneTool
					.<com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord> clone(
							indexWord,
							com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord.class));
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

}
