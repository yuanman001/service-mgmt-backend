package com.ai.paas.ipaas.ccs.service.dto;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstant;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;

import java.io.Serializable;

public class CCSOperationParam implements Serializable {
    private String userId;

    private String path;

    private String serviceId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String toString() {
        return "CCSOperationParam{" +
                "userId='" + userId + '\'' +
                ", path='" + path + '\'' +
                ", serviceId='" + serviceId + '\'' +
                '}';
    }

    public void validate() {
        Assert.notNull(getPath(), ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_NULL));

        if (!path.startsWith(PaaSConstant.UNIX_SEPERATOR)&&path.endsWith(PaaSConstant.UNIX_SEPERATOR) || path.length() < 2)
            throw new IllegalArgumentException(ResourceUtil.getMessage(BundleKeyConstant.PATH_ILL));

        Assert.notNull(getUserId(), ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
    }
}
