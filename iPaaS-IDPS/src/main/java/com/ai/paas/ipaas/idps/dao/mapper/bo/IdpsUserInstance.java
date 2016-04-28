package com.ai.paas.ipaas.idps.dao.mapper.bo;

import java.sql.Timestamp;

public class IdpsUserInstance {
    private Integer id;

    private String userId;

    private String idpsHostIp;

    private Integer idpsHostPort;

    private String serviceId;

    private String serviceName;

    private Integer status;

    private Timestamp beginTime;

    private Timestamp endTime;

    private Integer type;

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

    public String getIdpsHostIp() {
        return idpsHostIp;
    }

    public void setIdpsHostIp(String idpsHostIp) {
        this.idpsHostIp = idpsHostIp == null ? null : idpsHostIp.trim();
    }

    public Integer getIdpsHostPort() {
        return idpsHostPort;
    }

    public void setIdpsHostPort(Integer idpsHostPort) {
        this.idpsHostPort = idpsHostPort;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}