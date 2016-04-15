package com.ai.paas.ipaas.ccs.service.dto;

import java.io.Serializable;

public class ConfigInfo implements Serializable {

    private String resultCode;

    private String configAddr;

    private String configUser;

    private String configPwd;

    private String resultDescription;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getConfigAddr() {
        return configAddr;
    }

    public void setConfigAddr(String configAddr) {
        this.configAddr = configAddr;
    }

    public String getConfigUser() {
        return configUser;
    }

    public void setConfigUser(String configUser) {
        this.configUser = configUser;
    }

    public String getConfigPwd() {
        return configPwd;
    }

    public void setConfigPwd(String configPwd) {
        this.configPwd = configPwd;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    @Override
    public String toString() {
        return "ConfigInfo{" +
                "resultCode='" + resultCode + '\'' +
                ", configAddr='" + configAddr + '\'' +
                ", configUser='" + configUser + '\'' +
                ", configPwd='" + configPwd + '\'' +
                ", resultDescription='" + resultDescription + '\'' +
                '}';
    }
}
