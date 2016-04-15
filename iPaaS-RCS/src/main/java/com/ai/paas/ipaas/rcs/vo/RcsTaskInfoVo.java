package com.ai.paas.ipaas.rcs.vo;

import java.sql.Timestamp;

public class RcsTaskInfoVo {
	private long id;

	private String name;

	private long clusterId;

	private Timestamp registerDt;

	private Timestamp cancelDt;

	private String status;

	private String comments;

	private int numWorkers;

	private String jarfilepath;

	private String registerUserId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name == null ? null : name.trim();
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public Timestamp getRegisterDt() {
		return registerDt;
	}

	public void setRegisterDt(Timestamp registerDt) {
		this.registerDt = registerDt;
	}

	public Timestamp getCancelDt() {
		return cancelDt;
	}

	public void setCancelDt(Timestamp cancelDt) {
		this.cancelDt = cancelDt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments == null ? null : comments.trim();
	}

	public int getNumWorkers() {
		return numWorkers;
	}

	public void setNumWorkers(int numWorkers) {
		this.numWorkers = numWorkers;
	}

	public String getJarfilepath() {
		return jarfilepath;
	}

	public void setJarfilepath(String jarfilepath) {
		this.jarfilepath = jarfilepath == null ? null : jarfilepath.trim();
	}

	public String getRegisterUserId() {
		return registerUserId;
	}

	public void setRegisterUserId(String registerUserId) {
		this.registerUserId = registerUserId == null ? null : registerUserId
				.trim();
	}
}
