package com.ai.paas.ipaas.rcs.vo;

public class StormClusterInfoVo {
	private Float id;

	private String clusterId;

	private int clusterType;

	private int serverCount;

	private Float bandwidth;

	private String password;

	private String comments;

	private String hostCpu;

	private String hostMemory;

	private String hostDisk;

	private int orderStatus;

	private String orderUser;

	private String zkIp;

	private String zkPort;

	private String serviceId;

	public Float getId() {
		return id;
	}

	public void setId(Float id) {
		this.id = id;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId == null ? null : clusterId.trim();
	}

	public int getClusterType() {
		return clusterType;
	}

	public void setClusterType(int clusterType) {
		this.clusterType = clusterType;
	}

	public int getServerCount() {
		return serverCount;
	}

	public void setServerCount(int serverCount) {
		this.serverCount = serverCount;
	}

	public Float getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(Float bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments == null ? null : comments.trim();
	}

	public String getHostCpu() {
		return hostCpu;
	}

	public void setHostCpu(String hostCpu) {
		this.hostCpu = hostCpu == null ? null : hostCpu.trim();
	}

	public String getHostMemory() {
		return hostMemory;
	}

	public void setHostMemory(String hostMemory) {
		this.hostMemory = hostMemory == null ? null : hostMemory.trim();
	}

	public String getHostDisk() {
		return hostDisk;
	}

	public void setHostDisk(String hostDisk) {
		this.hostDisk = hostDisk == null ? null : hostDisk.trim();
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser == null ? null : orderUser.trim();
	}

	public String getZkIp() {
		return zkIp;
	}

	public void setZkIp(String zkIp) {
		this.zkIp = zkIp == null ? null : zkIp.trim();
	}

	public String getZkPort() {
		return zkPort;
	}

	public void setZkPort(String zkPort) {
		this.zkPort = zkPort == null ? null : zkPort.trim();
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId == null ? null : serviceId.trim();
	}
}
