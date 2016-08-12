package com.ai.paas.ipaas.mcs.dao.mapper.bo;

import java.sql.Timestamp;

public class McsUserCacheInstance {
    private Integer id;

    private String userId;

    private String cacheHost;

    private Integer cacheMemory;

    private Integer status;

    private Timestamp beginTime;

    private Timestamp endTime;

    private String serialNumber;

    private Integer cachePort;

    private String pwd;

    private String serviceName;

    private String containerName;

    private String redisImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCacheHost() {
        return cacheHost;
    }

    public void setCacheHost(String cacheHost) {
        this.cacheHost = cacheHost == null ? null : cacheHost.trim();
    }

    public Integer getCacheMemory() {
        return cacheMemory;
    }

    public void setCacheMemory(Integer cacheMemory) {
        this.cacheMemory = cacheMemory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Timestamp beginTime) {
        this.beginTime = beginTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public Integer getCachePort() {
        return cachePort;
    }

    public void setCachePort(Integer cachePort) {
        this.cachePort = cachePort;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName == null ? null : containerName.trim();
    }

    public String getRedisImage() {
        return redisImage;
    }

    public void setRedisImage(String redisImage) {
        this.redisImage = redisImage == null ? null : redisImage.trim();
    }
}