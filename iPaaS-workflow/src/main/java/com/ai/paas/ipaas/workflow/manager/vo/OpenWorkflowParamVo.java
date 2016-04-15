package com.ai.paas.ipaas.workflow.manager.vo;

import java.io.Serializable;

public class OpenWorkflowParamVo implements Serializable{

	private static final long serialVersionUID = 1L;

	private String userId; // 用户ID

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

	private String serviceId; // 服务ID
}
