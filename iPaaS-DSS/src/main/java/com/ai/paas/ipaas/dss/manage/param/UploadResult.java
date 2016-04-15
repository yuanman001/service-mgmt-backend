package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

public class UploadResult extends DSSResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2888698857927364735L;
	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
