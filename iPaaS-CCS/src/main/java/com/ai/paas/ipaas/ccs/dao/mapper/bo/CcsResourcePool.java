package com.ai.paas.ipaas.ccs.dao.mapper.bo;

public class CcsResourcePool {
    private int id;

    private String zkAddress;

    private int zkTypeCode;

    private String zkDescription;

    private String superAuthName;

    private String superAuthPassword;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress == null ? null : zkAddress.trim();
    }

    public int getZkTypeCode() {
        return zkTypeCode;
    }

    public void setZkTypeCode(int zkTypeCode) {
        this.zkTypeCode = zkTypeCode;
    }

    public String getZkDescription() {
        return zkDescription;
    }

    public void setZkDescription(String zkDescription) {
        this.zkDescription = zkDescription == null ? null : zkDescription.trim();
    }

    public String getSuperAuthName() {
        return superAuthName;
    }

    public void setSuperAuthName(String superAuthName) {
        this.superAuthName = superAuthName == null ? null : superAuthName.trim();
    }

    public String getSuperAuthPassword() {
        return superAuthPassword;
    }

    public void setSuperAuthPassword(String superAuthPassword) {
        this.superAuthPassword = superAuthPassword == null ? null : superAuthPassword.trim();
    }
}