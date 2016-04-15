package com.ai.paas.ipaas.des.manage.model;

import java.io.Serializable;

public class DesUserServiceParam implements Serializable{
	private String userId;
	private String serviceId;
	private int state;

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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
