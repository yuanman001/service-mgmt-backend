package com.ai.paas.ipaas.mds.manage.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;

public interface IMsgConfigHelper {
	/**
	 * 写入统一配置中心
	 * 
	 * @param userTopic
	 * @throws PaasException
	 */
	public void createConfigInfo(MdsUserTopic userTopic) throws PaasException;

	/**
	 * 写入统一配置中心---订阅者
	 * 
	 * @param userTopic
	 * @throws PaasException
	 */
	public void createConsums(MdsUserSubscribe subscribe) throws PaasException;
	/**
	 * 删除用户队列信息
	 * 
	 * @param userTopic
	 * @throws PaasException
	 */
	public void removeConfigInfo(MdsUserTopic userTopic) throws PaasException;

	/**
	 * 调整某个用户的
	 * 
	 * @param path
	 * @param userId
	 * @param newOffSet
	 * @throws PaasException
	 */
	public void adjustMsgOffSets(String path, String userId, long newOffSet)
			throws PaasException;

	public String getTopicSendConf(String userId, String srvId, String topic)
			throws PaasException;
}
