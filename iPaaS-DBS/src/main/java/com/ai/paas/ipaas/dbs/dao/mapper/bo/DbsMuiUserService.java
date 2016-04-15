package com.ai.paas.ipaas.dbs.dao.mapper.bo;

public class DbsMuiUserService {
    private Integer serviceId;

    private Integer muiId;

    private String userId;

    private String status;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getMuiId() {
        return muiId;
    }

    public void setMuiId(Integer muiId) {
        this.muiId = muiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}