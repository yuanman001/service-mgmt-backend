package com.ai.paas.ipaas.dss.dto;

import java.io.Serializable;

public class DSSConf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5370916574736884081L;

	private String dbName;
	private String size;
	private String limitSize;

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(String limitSize) {
		this.limitSize = limitSize;
	}

}
