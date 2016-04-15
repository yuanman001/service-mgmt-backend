package com.ai.paas.ipaas.ccs.service.dto;

import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;

public class CreateServiceInfo {
    private String userId;

    private String applyType;

    private String serviceId;

    private int timeOut;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    @Override
    public String toString() {
        return "CreateServiceInfo{" +
                "userId='" + userId + '\'' +
                ", applyType='" + applyType + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", timeOut=" + timeOut +
                '}';
    }

    public void validate() {
        Assert.notNull(userId, ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
        Assert.notNull(applyType, ResourceUtil.getMessage(BundleKeyConstants.APPLY_TYPE_NOT_NULL));
        Assert.notNull(serviceId, ResourceUtil.getMessage(BundleKeyConstants.SERVICE_ID_NOT_NULL));
        if (timeOut == 0){
            throw new IllegalArgumentException(ResourceUtil.getMessage(BundleKeyConstants.TIMEOUT_NOT_NULL));
        }
    }
}
