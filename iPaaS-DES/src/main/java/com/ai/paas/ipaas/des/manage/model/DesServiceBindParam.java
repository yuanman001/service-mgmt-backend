package com.ai.paas.ipaas.des.manage.model;

public class DesServiceBindParam {
	private String serviceId;
	private String dbsServiceId;
	private String dbsServicePassword;
	private String mdsServiceId;
	private String mdsServicePassword;
	private String userId;
	private String userName;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getDbsServiceId() {
		return dbsServiceId;
	}

	public void setDbsServiceId(String dbsServiceId) {
		this.dbsServiceId = dbsServiceId;
	}

	public String getDbsServicePassword() {
		return dbsServicePassword;
	}

	public void setDbsServicePassword(String dbsServicePassword) {
		this.dbsServicePassword = dbsServicePassword;
	}

	public String getMdsServiceId() {
		return mdsServiceId;
	}

	public void setMdsServiceId(String mdsServiceId) {
		this.mdsServiceId = mdsServiceId;
	}

	public String getMdsServicePassword() {
		return mdsServicePassword;
	}

	public void setMdsServicePassword(String mdsServicePassword) {
		this.mdsServicePassword = mdsServicePassword;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
