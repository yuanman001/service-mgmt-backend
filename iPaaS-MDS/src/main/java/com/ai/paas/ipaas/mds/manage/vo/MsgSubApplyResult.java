package com.ai.paas.ipaas.mds.manage.vo;

import java.io.Serializable;
import java.util.List;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;

public class MsgSubApplyResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5584757770801550478L;
	/**
	 * 
	 */
	private String resultCode = null;
	private String resultMsg = null;
	private String isExis = null;


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

	public String getIsExis() {
		return isExis;
	}

	public void setIsExis(String isExis) {
		this.isExis = isExis;
	}

	
}
