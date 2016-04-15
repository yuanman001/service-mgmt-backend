package com.ai.paas.ipaas.dss.dao.mapper.bo;

public class DssResourcePool {
	private Integer hostId;

	private String ip;

	private Integer port;

	private Integer groupId;

	private Integer status;

	private Integer leftSize;

	public Integer getHostId() {
		return hostId;
	}

	public void setHostId(Integer hostId) {
		this.hostId = hostId;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLeftSize() {
		return leftSize;
	}

	public void setLeftSize(Integer leftSize) {
		this.leftSize = leftSize;
	}
}