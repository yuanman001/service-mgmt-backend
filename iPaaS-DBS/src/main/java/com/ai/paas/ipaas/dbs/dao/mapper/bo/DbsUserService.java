package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;

public class DbsUserService {
    private Integer usedId;

    private String userId;

    private String userServiceId;

    private Timestamp createTime;

    private String isTxs;

    private Integer seqDbId;

    private String isMysqlProxy;

    private String isAutoswitch;

    public Integer getUsedId() {
        return usedId;
    }

    public void setUsedId(Integer usedId) {
        this.usedId = usedId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserServiceId() {
        return userServiceId;
    }

    public void setUserServiceId(String userServiceId) {
        this.userServiceId = userServiceId == null ? null : userServiceId.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getIsTxs() {
        return isTxs;
    }

    public void setIsTxs(String isTxs) {
        this.isTxs = isTxs == null ? null : isTxs.trim();
    }

    public Integer getSeqDbId() {
        return seqDbId;
    }

    public void setSeqDbId(Integer seqDbId) {
        this.seqDbId = seqDbId;
    }

    public String getIsMysqlProxy() {
        return isMysqlProxy;
    }

    public void setIsMysqlProxy(String isMysqlProxy) {
        this.isMysqlProxy = isMysqlProxy == null ? null : isMysqlProxy.trim();
    }

    public String getIsAutoswitch() {
        return isAutoswitch;
    }

    public void setIsAutoswitch(String isAutoswitch) {
        this.isAutoswitch = isAutoswitch == null ? null : isAutoswitch.trim();
    }
}