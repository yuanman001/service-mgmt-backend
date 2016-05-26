package com.ai.paas.ipaas.mcs.service.util;

public class McsProcessInfo {
	String cacheHostIp;
	String cachePath;
	Integer cachePort;
	Integer agentPort;

	public String getCacheHostIp() {
		return cacheHostIp;
	}

	public void setCacheHostIp(String cacheHostIp) {
		this.cacheHostIp = cacheHostIp;
	}

	public String getCachePath() {
		return cachePath;
	}

	public void setCachePath(String cachePath) {
		this.cachePath = cachePath;
	}

	public Integer getCachePort() {
		return cachePort;
	}

	public void setCachePort(Integer cachePort) {
		this.cachePort = cachePort;
	}

	public Integer getAgentPort() {
		return agentPort;
	}

	public void setAgentPort(Integer agentPort) {
		this.agentPort = agentPort;
	}
}
