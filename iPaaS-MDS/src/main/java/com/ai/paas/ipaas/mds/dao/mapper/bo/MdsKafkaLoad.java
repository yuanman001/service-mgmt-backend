package com.ai.paas.ipaas.mds.dao.mapper.bo;

public class MdsKafkaLoad implements Comparable<MdsKafkaLoad> {
	private int clusterId;

	private int userNum;

	public int getClusterId() {
		return clusterId;
	}

	public void setClusterId(int clusterId) {
		this.clusterId = clusterId;
	}

	public int getUserNum() {
		return userNum;
	}

	public void setUserNum(int userNum) {
		this.userNum = userNum;
	}

	@Override
	public int compareTo(MdsKafkaLoad o) {
		if (null == o)
			return 1;
		return this.userNum - o.getClusterId();
	}

}