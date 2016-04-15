package com.ai.paas.ipaas.workflow.manager.vo;

import java.io.Serializable;
import java.util.List;

public class OpenWorkflowResultVo implements Serializable {

	private static final long serialVersionUID = 5116269289362772039L;

	private  String  resultCode;

	private  String  resultDesc;
	
	private String userId;
	
	private String serviceId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	private  List	 resultContent;
	
	public List getResultContent() {
		return resultContent;
	}

	public void setResultContent(List resultContent) {
		this.resultContent = resultContent;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDesc() {
		return resultDesc;
	}

	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}

}
