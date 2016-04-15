package com.ai.paas.ipaas.ccs.service.dto;

import java.io.Serializable;

public class CCSFuncList implements Serializable {

    private String url; //请求url

    private String applyType; //请求类型

    private String description; //描述信息

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getApplyType() {
		return applyType;
	}

	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
}
