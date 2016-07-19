package com.ai.paas.ipaas.mds.manage.service;

import java.util.List;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;

public interface IMsgKafkaHelper {

	/**
	 * 获取消息队列的使用情况
	 * 
	 * @param userId
	 * @param userSrvId
	 * @param topic
	 * @param kafkaCluster
	 * @return
	 */
	public List<MsgSrvUsageApplyResult> getTopicOffsets(String userId,
			String userSrvId, String topic,String subscribeName, MdsResourcePool kafkaCluster);
	
	/**
	 * 获得消息队列下所有的消费者
	 * 
	 * @param userId
	 * @param userSrvId
	 * @param topic
	 * @param kafkaCluster
	 * @return
	 */
	public List<String> getListSubPath(String userId,
			String userSrvId, String topic);
	

	/**
	 * 检查topic是否存在
	 * 
	 * @param kafkaCluster
	 * @param topicName
	 * @return
	 */
	public boolean isTopicExist(MdsResourcePool kafkaCluster, String topicName);

	/**
	 * 在kafka集群建立topic
	 * 
	 * @param userTopic
	 * @param kafkaCluster
	 */
	public void createTopic(MdsUserTopic userTopic, MdsResourcePool kafkaCluster);

	/**
	 * 删除kafka中队列
	 * 
	 * @param zkAddress
	 * @param topic
	 */
	public void deleteTopic(MdsResourcePool kafkaCluster, String topic);

	/**
	 * 获取队列某个分区的消息，消息仅以字符串形式进行反序列化
	 * 
	 * @param kafkaCluster
	 * @param topic
	 * @param partition
	 * @param offset
	 * @return
	 */
	public String getTopicMessage(MdsResourcePool kafkaCluster, String topic,
			int partition, long offset);

	/**
	 * 想队列的指定分区发送消息
	 * @param topic
	 * @param parition
	 * @param senderConf json格式的消息配置
	 * @param message
	 */
	public void sendMessage(String topic, int parition, String senderConf,
			byte[] message);

}