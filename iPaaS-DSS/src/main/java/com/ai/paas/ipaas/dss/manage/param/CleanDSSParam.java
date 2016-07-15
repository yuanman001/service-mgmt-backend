package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;


public class CleanDSSParam extends ApplyInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -878109683297863991L;

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
