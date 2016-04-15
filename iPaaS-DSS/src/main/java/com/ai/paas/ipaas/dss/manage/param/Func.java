package com.ai.paas.ipaas.dss.manage.param;

import java.io.Serializable;

public class Func implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2481540869749215981L;

	private String funcName;
	private String funcUrl;

	public Func() {

	}

	public Func(String funcName, String funcUrl) {
		this.funcName = funcName;
		this.funcUrl = funcUrl;
	}

	public String getFuncName() {
		return funcName;
	}

	public void setFuncName(String funcName) {
		this.funcName = funcName;
	}

	public String getFuncUrl() {
		return funcUrl;
	}

	public void setFuncUrl(String funcUrl) {
		this.funcUrl = funcUrl;
	}

}
