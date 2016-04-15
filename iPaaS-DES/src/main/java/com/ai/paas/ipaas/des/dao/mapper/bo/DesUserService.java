package com.ai.paas.ipaas.des.dao.mapper.bo;

public class DesUserService {
    private Integer srvApplyId;

    private String userId;

    private String serviceId;

    private Integer state;

    public Integer getSrvApplyId() {
        return srvApplyId;
    }

    public void setSrvApplyId(Integer srvApplyId) {
        this.srvApplyId = srvApplyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}