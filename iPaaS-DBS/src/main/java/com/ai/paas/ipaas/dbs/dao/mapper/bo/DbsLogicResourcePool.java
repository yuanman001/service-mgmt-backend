package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;

public class DbsLogicResourcePool {
    private Integer logicId;

    private String logicName;

    private Timestamp createTime;

    private String resUseType;

    private Integer usedId;

    public Integer getLogicId() {
        return logicId;
    }

    public void setLogicId(Integer logicId) {
        this.logicId = logicId;
    }

    public String getLogicName() {
        return logicName;
    }

    public void setLogicName(String logicName) {
        this.logicName = logicName == null ? null : logicName.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getResUseType() {
        return resUseType;
    }

    public void setResUseType(String resUseType) {
        this.resUseType = resUseType == null ? null : resUseType.trim();
    }

    public Integer getUsedId() {
        return usedId;
    }

    public void setUsedId(Integer usedId) {
        this.usedId = usedId;
    }
}