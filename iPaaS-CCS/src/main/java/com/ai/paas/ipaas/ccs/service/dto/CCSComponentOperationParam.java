package com.ai.paas.ipaas.ccs.service.dto;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstant;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.ResourceUtil;

import java.io.Serializable;
/**
 * 操作参数
 * @author Fenggw
 *
 */
public class CCSComponentOperationParam implements Serializable {
    private String userId;

    private String path;

    public ConfigCenterDubboConstants.PathType getPathType() {
        return pathType;
    }

    public void setPathType(ConfigCenterDubboConstants.PathType pathType) {
        this.pathType = pathType;
    }

    private ConfigCenterDubboConstants.PathType pathType;

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

    @Override
    public String toString() {
        return "ConfigOperationParam{" +
                "userId='" + userId + '\'' +
                ", path='" + path + '\'' +
                ", pathType=" + pathType.getDescription() +
                '}';
    }

    public void validate() {
        Assert.notNull(getPath(), ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_NULL));

        if (!path.startsWith(PaaSConstant.UNIX_SEPERATOR) || path.length() < 2)
            throw new IllegalArgumentException(ResourceUtil.getMessage(BundleKeyConstant.PATH_ILL));

        Assert.notNull(getPathType(), ResourceUtil.getMessage(BundleKeyConstants.PATH_TYPE_NOT_NULL));
        Assert.notNull(getUserId(), ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
    }
}
