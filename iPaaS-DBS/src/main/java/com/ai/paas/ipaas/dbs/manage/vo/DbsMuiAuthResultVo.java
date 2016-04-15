package com.ai.paas.ipaas.dbs.manage.vo;

import java.io.Serializable;

public class DbsMuiAuthResultVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private  String  resultCode;
	
	private boolean status;
	
	public boolean getStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	private  String  resultMsg;
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultMsg() {
		return resultMsg;
	}
	public void setResultMsg(String resultDesc) {
		this.resultMsg= resultDesc;
	}
	
}
