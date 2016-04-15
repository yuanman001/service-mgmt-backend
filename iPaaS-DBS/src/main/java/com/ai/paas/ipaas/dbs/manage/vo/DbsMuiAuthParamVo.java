package com.ai.paas.ipaas.dbs.manage.vo;

import java.io.Serializable;

public class DbsMuiAuthParamVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String userId;   //用户ID
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private String url;     //验证ip
}
