package com.ai.paas.ipaas.mds.manage.vo;

import java.io.Serializable;
import java.util.List;

public class MsgSrvApplyResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1908779564711939717L;
	private String userId = null;
	private String applyType = null;
	private String serviceId = null;
	private String resultCode = null;
	private String resultMsg = null;
	private String topicMessage = null;
	private List<MsgSrvUsageApplyResult> topicUsage = null;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public List<MsgSrvUsageApplyResult> getTopicUsage() {
		return topicUsage;
	}

	public void setTopicUsage(List<MsgSrvUsageApplyResult> topicUsage) {
		this.topicUsage = topicUsage;
	}

	public String getTopicMessage() {
		return topicMessage;
	}

	public void setTopicMessage(String topicMessage) {
		this.topicMessage = topicMessage;
	}

}
