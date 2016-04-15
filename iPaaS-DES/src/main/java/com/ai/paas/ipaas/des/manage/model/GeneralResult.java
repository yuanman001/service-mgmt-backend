package com.ai.paas.ipaas.des.manage.model;

import java.io.Serializable;

public class GeneralResult implements Serializable {

	private String resultCode;

	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
