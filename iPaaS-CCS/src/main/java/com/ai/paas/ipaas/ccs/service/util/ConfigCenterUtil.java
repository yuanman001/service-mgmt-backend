package com.ai.paas.ipaas.ccs.service.util;

import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.util.CiperUtil;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 配置中心工具类
 * @author Fenggw
 *
 */
public class ConfigCenterUtil {
    public static String appendUserReadOnlyPathPath(String userId) {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_READ_PREFIX;
    }

    public static String appendUserWritablePathPath(String userId) {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_WRITABLE_PREFIX;
    }

    public static String appendUserRootPathPath(String userId) {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId;
    }

    /**
     * 创建只读ACL
     * @param userId
     * @param pwd
     * @param pool
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static List<ACL> createReadOnlyACL(String userId, String pwd, CcsResourcePool pool) throws NoSuchAlgorithmException {
        List<ACL> acls = new ArrayList<ACL>();
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest(appendAdminAuthInfo(pool)));
        ACL adminACL = new ACL(ZooDefs.Perms.ALL, id1);
        acls.add(adminACL);
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest(appendUserAuthInfo(userId, pwd)));
        ACL userACL = new ACL(ZooDefs.Perms.READ, id2);
        acls.add(userACL);
        return acls;
    }

    /**
     * 创建可写ACL
     * @param userId
     * @param pwd
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static List<ACL> createWritableACL(String userId, String pwd) throws NoSuchAlgorithmException {
        List<ACL> acls = new ArrayList<ACL>();
        Id id2 = new Id("digest", DigestAuthenticationProvider.generateDigest(appendUserAuthInfo(userId, pwd)));
        ACL userACL = new ACL(ZooDefs.Perms.ALL, id2);
        acls.add(userACL);
        return acls;
    }


    public static String appendAdminAuthInfo(CcsResourcePool pool) {
        return pool.getSuperAuthName() + ":" + CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword());
    }

    public static String appendUserAuthInfo(String userId, String pwd) {
        return userId + ":" + pwd;
    }
}
