package com.ai.paas.ipaas.dss.dao.mapper.bo;

public class DssMcsInfo {
    private Integer id;

    private String mcsAddress;

    private Integer status;

    private Integer orgId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMcsAddress() {
        return mcsAddress;
    }

    public void setMcsAddress(String mcsAddress) {
        this.mcsAddress = mcsAddress == null ? null : mcsAddress.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}