package com.ai.paas.ipaas.mds.dao.mapper.bo;

public class MdsUserServiceKey {
    private Integer srvInstId;

    private String userId;

    private String userSrvId;

    private Integer state;

    public Integer getSrvInstId() {
        return srvInstId;
    }

    public void setSrvInstId(Integer srvInstId) {
        this.srvInstId = srvInstId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserSrvId() {
        return userSrvId;
    }

    public void setUserSrvId(String userSrvId) {
        this.userSrvId = userSrvId == null ? null : userSrvId.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}