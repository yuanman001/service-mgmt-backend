package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;

public class DbsPhysicalResourcePool {
    private Integer resId;

    private String resHost;

    private String resName;

    private Integer resPort;

    private String resInstance;

    private String resUser;

    private String resPassword;

    private String isUsed;

    private String resStatus;

    private Timestamp createTime;

    private Timestamp usedTime;

    private String createAuthor;

    private Timestamp lastModifyTime;

    private String lastModifyAuthor;

    private String msFlag;

    private String resUseType;

    private Integer logicId;

    private String resSuperUser;

    private String resSuperPassword;

    private String confAddr;

    private Integer agentPort;

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getResHost() {
        return resHost;
    }

    public void setResHost(String resHost) {
        this.resHost = resHost == null ? null : resHost.trim();
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName == null ? null : resName.trim();
    }

    public Integer getResPort() {
        return resPort;
    }

    public void setResPort(Integer resPort) {
        this.resPort = resPort;
    }

    public String getResInstance() {
        return resInstance;
    }

    public void setResInstance(String resInstance) {
        this.resInstance = resInstance == null ? null : resInstance.trim();
    }

    public String getResUser() {
        return resUser;
    }

    public void setResUser(String resUser) {
        this.resUser = resUser == null ? null : resUser.trim();
    }

    public String getResPassword() {
        return resPassword;
    }

    public void setResPassword(String resPassword) {
        this.resPassword = resPassword == null ? null : resPassword.trim();
    }

    public String getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(String isUsed) {
        this.isUsed = isUsed == null ? null : isUsed.trim();
    }

    public String getResStatus() {
        return resStatus;
    }

    public void setResStatus(String resStatus) {
        this.resStatus = resStatus == null ? null : resStatus.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUsedTime() {
        return usedTime;
    }

    public void setUsedTime(Timestamp usedTime) {
        this.usedTime = usedTime;
    }

    public String getCreateAuthor() {
        return createAuthor;
    }

    public void setCreateAuthor(String createAuthor) {
        this.createAuthor = createAuthor == null ? null : createAuthor.trim();
    }

    public Timestamp getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Timestamp lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getLastModifyAuthor() {
        return lastModifyAuthor;
    }

    public void setLastModifyAuthor(String lastModifyAuthor) {
        this.lastModifyAuthor = lastModifyAuthor == null ? null : lastModifyAuthor.trim();
    }

    public String getMsFlag() {
        return msFlag;
    }

    public void setMsFlag(String msFlag) {
        this.msFlag = msFlag == null ? null : msFlag.trim();
    }

    public String getResUseType() {
        return resUseType;
    }

    public void setResUseType(String resUseType) {
        this.resUseType = resUseType == null ? null : resUseType.trim();
    }

    public Integer getLogicId() {
        return logicId;
    }

    public void setLogicId(Integer logicId) {
        this.logicId = logicId;
    }

    public String getResSuperUser() {
        return resSuperUser;
    }

    public void setResSuperUser(String resSuperUser) {
        this.resSuperUser = resSuperUser == null ? null : resSuperUser.trim();
    }

    public String getResSuperPassword() {
        return resSuperPassword;
    }

    public void setResSuperPassword(String resSuperPassword) {
        this.resSuperPassword = resSuperPassword == null ? null : resSuperPassword.trim();
    }

    public String getConfAddr() {
        return confAddr;
    }

    public void setConfAddr(String confAddr) {
        this.confAddr = confAddr == null ? null : confAddr.trim();
    }

    public Integer getAgentPort() {
        return agentPort;
    }

    public void setAgentPort(Integer agentPort) {
        this.agentPort = agentPort;
    }
}