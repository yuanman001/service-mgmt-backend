package com.ai.paas.ipaas.mds.manage.vo;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;


public class MsgSrvUsageApplyResult extends ApplyInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6658601803293833600L;
	/**
	 * 
	 */
	private String topicEnName;
	private int partitionId;
	private long totalOffset;
	private long avalOffset;
	private long consumedOffset;

	public String getTopicEnName() {
		return topicEnName;
	}

	public void setTopicEnName(String topicEnName) {
		this.topicEnName = topicEnName;
	}

	public int getPartitionId() {
		return partitionId;
	}

	public void setPartitionId(int partitionId) {
		this.partitionId = partitionId;
	}

	public long getTotalOffset() {
		return totalOffset;
	}

	public void setTotalOffset(long totalOffset) {
		this.totalOffset = totalOffset;
	}

	public long getConsumedOffset() {
		return consumedOffset;
	}

	public void setConsumedOffset(long consumedOffset) {
		this.consumedOffset = consumedOffset;
	}

	public long getAvalOffset() {
		return avalOffset;
	}

	public void setAvalOffset(long avalOffset) {
		this.avalOffset = avalOffset;
	}
	
}
