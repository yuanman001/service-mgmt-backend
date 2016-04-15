package com.ai.paas.ipaas.ccs.service.util;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.ccs.zookeeper.impl.ZKPoolFactory;
import com.ai.paas.ipaas.util.ResourceUtil;
import org.springframework.util.Assert;

public class ZookeeperClientUtil {

    public static ZKClient getZkClientFromPool(String zkAddress, String authUserName, String authPasswd ) throws PaasException {
        ZKClient zkClient = null;
        try {
            zkClient = ZKPoolFactory.getZKPool(zkAddress, authUserName, authPasswd).getZkClient(zkAddress, authUserName);
            Assert.notNull(zkClient, ResourceUtil.getMessage(BundleKeyConstants.GET_CONFIG_CLIENT_FAILED));
        } catch (Exception e) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.GET_CONFIG_CLIENT_FAILED), e);
        }
        return zkClient;
    }
    
    public static ZKClient getZkClientFromPool(String zkAddress, String authUserName, String authPasswd ,String serviceId) throws PaasException {
        ZKClient zkClient = null;
        try {
            zkClient = ZKPoolFactory.getZKPool(zkAddress, authUserName, authPasswd ,serviceId).getZkClient(zkAddress, authUserName,serviceId);
            Assert.notNull(zkClient, ResourceUtil.getMessage(BundleKeyConstants.GET_CONFIG_CLIENT_FAILED));
        } catch (Exception e) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.GET_CONFIG_CLIENT_FAILED), e);
        }
        return zkClient;
    }
}