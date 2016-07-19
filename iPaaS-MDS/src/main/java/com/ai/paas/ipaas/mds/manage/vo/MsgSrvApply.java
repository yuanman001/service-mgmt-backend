package com.ai.paas.ipaas.mds.manage.vo;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;


public class MsgSrvApply extends ApplyInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1949057828063065625L;
	private String topicEnName;
	private String topicName;
	private int topicPartitions;
	private int msgReplica;
	private int maxSender;
	private int partition;
	private long offset;
	private String message;
	private String subscribeName;

	public String getTopicEnName() {
		return topicEnName;
	}

	public void setTopicEnName(String topicEnName) {
		this.topicEnName = topicEnName;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public int getTopicPartitions() {
		return topicPartitions;
	}

	public void setTopicPartitions(int topicPartitions) {
		this.topicPartitions = topicPartitions;
	}

	public int getMsgReplica() {
		return msgReplica;
	}

	public void setMsgReplica(int msgReplica) {
		this.msgReplica = msgReplica;
	}

	public int getMaxSender() {
		return maxSender;
	}

	public void setMaxSender(int maxSender) {
		this.maxSender = maxSender;
	}

	public int getPartition() {
		return partition;
	}

	public void setPartition(int partition) {
		this.partition = partition;
	}

	public long getOffset() {
		return offset;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSubscribeName() {
		return subscribeName;
	}

	public void setSubscribeName(String subscribeName) {
		this.subscribeName = subscribeName;
	}

	
}
