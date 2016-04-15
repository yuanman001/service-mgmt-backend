package com.ai.paas.ipaas.des.dao.mapper.bo;

public class DesServiceBind {
    private String serviceId;

    private String dbsServiceId;

    private String dbsServicePassword;

    private String mdsServiceId;

    private String mdsServicePassword;

    private String mdsTopic;

    private Integer mdsPartition;

    private String userId;

    private String userName;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getDbsServiceId() {
        return dbsServiceId;
    }

    public void setDbsServiceId(String dbsServiceId) {
        this.dbsServiceId = dbsServiceId == null ? null : dbsServiceId.trim();
    }

    public String getDbsServicePassword() {
        return dbsServicePassword;
    }

    public void setDbsServicePassword(String dbsServicePassword) {
        this.dbsServicePassword = dbsServicePassword == null ? null : dbsServicePassword.trim();
    }

    public String getMdsServiceId() {
        return mdsServiceId;
    }

    public void setMdsServiceId(String mdsServiceId) {
        this.mdsServiceId = mdsServiceId == null ? null : mdsServiceId.trim();
    }

    public String getMdsServicePassword() {
        return mdsServicePassword;
    }

    public void setMdsServicePassword(String mdsServicePassword) {
        this.mdsServicePassword = mdsServicePassword == null ? null : mdsServicePassword.trim();
    }

    public String getMdsTopic() {
        return mdsTopic;
    }

    public void setMdsTopic(String mdsTopic) {
        this.mdsTopic = mdsTopic == null ? null : mdsTopic.trim();
    }

    public Integer getMdsPartition() {
        return mdsPartition;
    }

    public void setMdsPartition(Integer mdsPartition) {
        this.mdsPartition = mdsPartition;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }
}