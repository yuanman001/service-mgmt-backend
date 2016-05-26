package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;
import java.util.Map;

public class RecordResult extends DSSResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1107793912677827257L;
	@SuppressWarnings("rawtypes")
	private Map recordJson;

	@SuppressWarnings("rawtypes")
	public Map getRecordJson() {
		return recordJson;
	}

	@SuppressWarnings("rawtypes")
	public void setRecordJson(Map recordJson) {
		this.recordJson = recordJson;
	}

}
