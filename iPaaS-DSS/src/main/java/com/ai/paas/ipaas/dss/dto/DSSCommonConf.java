package com.ai.paas.ipaas.dss.dto;

import java.io.Serializable;

public class DSSCommonConf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 779138808019094468L;

	private String hosts;
	private String username;
	private String password;
	private String redisHosts;

	public String getRedisHosts() {
		return redisHosts;
	}

	public void setRedisHosts(String redisHosts) {
		this.redisHosts = redisHosts;
	}

	public String getHosts() {
		return hosts;
	}

	public void setHosts(String hosts) {
		this.hosts = hosts;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
