package com.ai.paas.ipaas.mds.manage.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;

/**
 * @author DOUXF
 *
 */
public interface IMsgSrvManager {
	/**
	 * 开通消息服务，同时默認创建一个队列
	 * 
	 * @param msgSrvApply
	 * @throws PaasException
	 */
	public void createMessageService(MsgSrvApply msgSrvApply)
			throws PaasException;

	/**
	 * 注销服务，删除所有的队列，资源回收
	 * 
	 * @param msgSrvApply
	 * @throws PaasException
	 */
	public void cancelMessageService(MsgSrvApply msgSrvApply)
			throws PaasException;

	/**
	 * 创建一个队列
	 * 
	 * @param msgSrvApply
	 * @throws PaasException
	 */
	public void createTopic(MsgSrvApply msgTopicApply) throws PaasException;

	/**
	 * 删除一个队列
	 * 
	 * @param msgTopicApply
	 * @throws PaasException
	 */
	public void removeTopic(MsgSrvApply msgTopicApply) throws PaasException;

	/**
	 * 获取消息队列的消息情况，包括队列中每个分区的消息总数，消费数
	 * 
	 * @param msgTopicApply
	 * @return
	 * @throws PaasException
	 */
	public List<MsgSrvUsageApplyResult> getTopicUsage(MsgSrvApply msgTopicApply)
			throws PaasException;
	
	/**
	 * 获得消息队列下所有的消费者
	 * 
	 * @param msgTopicApply
	 * @return
	 * @throws PaasException
	 */
	public List<String> getListSubPath(MsgSrvApply msgTopicApply)
			throws PaasException;
	

	/**
	 * 获取消息队列某个分区的某条消息
	 * 
	 * @param msgTopicApply
	 * @return
	 */
	public String getTopicMessage(MsgSrvApply msgTopicApply);

	/**
	 * 调整队列的消费偏移量
	 * 
	 * @param msgTopicApply
	 * @return
	 */
	public void adjustTopicOffset(MsgSrvApply msgTopicApply);
	
	
	/**
	 * 发送消息
	 * @param msgTopicApply
	 */
	public void sendMessage(MsgSrvApply msgTopicApply);

	/**
	 * 创建一个订阅者
	 * 
	 * @param subscribeApply
	 * @throws PaasException
	 */
	public void createSubscribe(MdsUserSubscribe subscribeApply) throws PaasException;
	
	/**
	 * 获取订阅者
	 * 
	 * @param msgTopicApply
	 * @return
	 * @throws PaasException
	 */
	public List<MdsUserSubscribe> getSubscribe(MdsUserSubscribe subscribeApply)
			throws PaasException;
	
}
