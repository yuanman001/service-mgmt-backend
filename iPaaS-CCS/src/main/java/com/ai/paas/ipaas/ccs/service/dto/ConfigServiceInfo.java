package com.ai.paas.ipaas.ccs.service.dto;

import java.io.Serializable;

public class ConfigServiceInfo implements Serializable {

    private String resultCode;

    private String applyType;

    private String serviceId;

    private String resultMsg;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String toString() {
        return "ConfigServiceInfo{" +
                "resultCode='" + resultCode + '\'' +
                ", applyType='" + applyType + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                '}';
    }
}
