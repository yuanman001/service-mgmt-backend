package com.ai.paas.ipaas.dbs.manage.vo;

import java.io.Serializable;

public class OpenResourceResultVo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private  String  resultCode;
	
	private  String  resultMsg;
	
	private  String	 url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
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
		this.resultMsg = resultDesc;
	}
	
}
