package com.ai.paas.ipaas.dbs.dto;

public class AuthResult {
	/**
	 * 配置地址，ZK模式，IP:PORT;IP:PORT
	 */
	private String configAddr = null;
	public String getConfigAddr() {
		return configAddr;
	}
	public void setConfigAddr(String configAddr) {
		this.configAddr = configAddr;
	}
	public String getConfigUser() {
		return configUser;
	}
	public void setConfigUser(String configUser) {
		this.configUser = configUser;
	}
	public String getConfigPasswd() {
		return configPasswd;
	}
	public void setConfigPasswd(String configPasswd) {
		this.configPasswd = configPasswd;
	}
	public String getUserId() {
		return userId;
	}
	public AuthResult(String configAddr, String configUser,
			String configPasswd, String userId) {
		super();
		this.configAddr = configAddr;
		this.configUser = configUser;
		this.configPasswd = configPasswd;
		this.userId = userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	private String configUser = null;
	private String configPasswd = null;
	private String userId=null;
}
