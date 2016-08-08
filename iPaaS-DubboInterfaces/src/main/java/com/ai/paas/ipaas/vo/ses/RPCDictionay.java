package com.ai.paas.ipaas.vo.ses;

import com.ai.dubbo.ext.vo.BaseInfo;

public class RPCDictionay extends BaseInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9058478573287539688L;
	private String indexWordList;
	private String stopWordList;
	private String userId;
	private String serviceId;

	public String getIndexWordList() {
		return indexWordList;
	}

	public void setIndexWordList(String indexWordList) {
		this.indexWordList = indexWordList;
	}

	public String getStopWordList() {
		return stopWordList;
	}

	public void setStopWordList(String stopWordList) {
		this.stopWordList = stopWordList;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
