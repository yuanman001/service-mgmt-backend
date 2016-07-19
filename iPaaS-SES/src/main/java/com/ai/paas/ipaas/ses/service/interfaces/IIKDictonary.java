package com.ai.paas.ipaas.ses.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord;

public interface IIKDictonary {

	public void saveDictonaryWord(List<SesUserIndexWord> indexWordList,
			List<SesUserStopWord> stopWordList, String userId, String serviceId);

	@SuppressWarnings("rawtypes")
	public Map<String, List> getUserDictionary(String userId, String serviceId);
}
