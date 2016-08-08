package com.ai.paas.ipaas.ses.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord;

public interface IIKDictonary {

	public void saveDictonaryWord(List<SesUserIndexWord> indexWordList,
			List<SesUserStopWord> stopWordList, String userId, String serviceId);

	@SuppressWarnings("rawtypes")
	public Map<String, List> getUserDictionary(String userId, String serviceId,
			int start, int rows);

	public void saveAllIndexWords(String userId, String serviceId, String words);

	public void saveAllStopWords(String userId, String serviceId, String words);

	public String getUserIndexWords(String userId, String serviceId, int start,
			int rows);

	public String getUserStopWords(String userId, String serviceId, int start,
			int rows);

	public void clearAllIndexWords(String userId, String serviceId);

	public void clearAllStopWords(String userId, String serviceId);
}
