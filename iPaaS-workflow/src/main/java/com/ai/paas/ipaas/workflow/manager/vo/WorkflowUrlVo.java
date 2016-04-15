package com.ai.paas.ipaas.workflow.manager.vo;

import java.io.Serializable;

public class WorkflowUrlVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String url;
	
	private String urlDesc;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlDesc() {
		return urlDesc;
	}

	public void setUrlDesc(String urlDesc) {
		this.urlDesc = urlDesc;
	}

}
