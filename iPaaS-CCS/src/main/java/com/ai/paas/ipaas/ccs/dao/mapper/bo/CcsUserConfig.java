package com.ai.paas.ipaas.ccs.dao.mapper.bo;

import java.sql.Timestamp;

public class CcsUserConfig {
    private int id;

    private String userId;

    private String zkUserName;

    private String zkPassword;

    private String zkAddress;

    private Timestamp updateTime;

    private int ccsResourceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getZkUserName() {
        return zkUserName;
    }

    public void setZkUserName(String zkUserName) {
        this.zkUserName = zkUserName == null ? null : zkUserName.trim();
    }

    public String getZkPassword() {
        return zkPassword;
    }

    public void setZkPassword(String zkPassword) {
        this.zkPassword = zkPassword == null ? null : zkPassword.trim();
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress == null ? null : zkAddress.trim();
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getCcsResourceId() {
        return ccsResourceId;
    }

    public void setCcsResourceId(int ccsResourceId) {
        this.ccsResourceId = ccsResourceId;
    }
}