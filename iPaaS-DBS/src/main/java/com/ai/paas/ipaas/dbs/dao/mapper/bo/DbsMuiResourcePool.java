package com.ai.paas.ipaas.dbs.dao.mapper.bo;

public class DbsMuiResourcePool {
    private Integer muiId;

    private String muiUrl;

    private String status;

    public Integer getMuiId() {
        return muiId;
    }

    public void setMuiId(Integer muiId) {
        this.muiId = muiId;
    }

    public String getMuiUrl() {
        return muiUrl;
    }

    public void setMuiUrl(String muiUrl) {
        this.muiUrl = muiUrl == null ? null : muiUrl.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }
}