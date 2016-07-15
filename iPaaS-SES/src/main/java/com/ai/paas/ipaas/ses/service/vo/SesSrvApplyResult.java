package com.ai.paas.ipaas.ses.service.vo;

import java.io.Serializable;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;

/**
 * 
 * ClassName: SesSrvApplyResult 
 * date: 2015年8月18日 下午4:01:33 
 *
 * @author jianhua.ma

 */
public class SesSrvApplyResult extends ApplyInfo implements Serializable {
	private static final long serialVersionUID = -305386031613493497L;
	/**
	 * 返回结果码
	 */
	private String resultCode = null;
	/**
	 * 返回信息内容
	 */
	private String resultMsg = null;
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
