package com.ai.paas.ipaas.des.manage.model;

/**
 * des服务设置观察表的参数对象
 * 
 * @author bixy
 * 
 */
public class DesFilterTableParam {
	private String serviceId;
	private String userId;
	private String dbsServiceId;
	private String[] tables;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDbsServiceId() {
		return dbsServiceId;
	}

	public void setDbsServiceId(String dbsServiceId) {
		this.dbsServiceId = dbsServiceId;
	}

	public String[] getTables() {
		return tables;
	}

	public void setTables(String[] tables) {
		this.tables = tables;
	}

}
