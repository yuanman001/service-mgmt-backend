package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

public class StatusResult extends DSSResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6601566915895780010L;

	private String usedSize;
	private String size;

	public String getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(String usedSize) {
		this.usedSize = usedSize;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

}
