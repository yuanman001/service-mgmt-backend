package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

import com.ai.paas.ipaas.rest.vo.BaseInfo;

public class ModifyParam extends BaseInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8172867586514038405L;
	private String size;
	private String limitFileSize;

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getLimitFileSize() {
		return limitFileSize;
	}

	public void setLimitFileSize(String limitFileSize) {
		this.limitFileSize = limitFileSize;
	}
}
