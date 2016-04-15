package com.ai.paas.ipaas.dss.dao.mapper.bo;

import java.sql.Timestamp;

public class DssUserInstance {
	private Integer ossId;

	private String userId;

	private String dbName;

	private String collectionName;

	private Double ossSize;

	private Double fileLimitSize;

	private Timestamp startDate;

	private Timestamp endDate;

	private Integer groupId;

	private Integer redisId;

	private String serviceName;

	public Integer getOssId() {
		return ossId;
	}

	public void setOssId(Integer ossId) {
		this.ossId = ossId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId == null ? null : userId.trim();
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName == null ? null : dbName.trim();
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName == null ? null : collectionName
				.trim();
	}

	public Double getOssSize() {
		return ossSize;
	}

	public void setOssSize(Double ossSize) {
		this.ossSize = ossSize;
	}

	public Double getFileLimitSize() {
		return fileLimitSize;
	}

	public void setFileLimitSize(Double fileLimitSize) {
		this.fileLimitSize = fileLimitSize;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getEndDate() {
		return endDate;
	}

	public void setEndDate(Timestamp endDate) {
		this.endDate = endDate;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getRedisId() {
		return redisId;
	}

	public void setRedisId(Integer redisId) {
		this.redisId = redisId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName == null ? null : serviceName.trim();
	}
}