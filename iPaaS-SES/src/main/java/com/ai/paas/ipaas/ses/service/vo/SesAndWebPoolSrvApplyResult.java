package com.ai.paas.ipaas.ses.service.vo;

import java.io.Serializable;

/**
 * 
 * ClassName: SesSrvApplyResult 
 * date: 2016年9月8日 下午4:01:33 
 *
 * @author yinzf

 */
public class SesAndWebPoolSrvApplyResult extends SesSrvApplyResult implements Serializable {

	private static final long serialVersionUID = -7533786190992432696L;
	
	private String webUrl = null;

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	 
	
}
