package ccs.test.manage.impl;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePoolCriteria;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfigCriteria;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.IConfigCenterServiceManager;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.ccs.zookeeper.impl.ZKPoolFactory;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.UUIDTool;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import org.apache.zookeeper.KeeperException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/config-client-consumer.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CreateUserNodeTest {

    private static transient final Logger logger = LoggerFactory.getLogger(CreateUserNodeTest.class);

    private static String userId;

    private static ConfigInfo configInfo;

    @Reference
    private IConfigCenterServiceManager configCenterManager;

    @Before
    public void testCreateUserNode() throws Exception {
        userId = UUIDTool.genId32();
        logger.info("[create user ] : " + userId);
        String result = configCenterManager.init("{'userId':'" + userId + "'}");
        Gson gson = new Gson();
        configInfo = gson.fromJson(result, ConfigInfo.class);
    }

    @Test
    public void testUserConfigIsInDB() {
        CcsUserConfigMapper mapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        CcsUserConfigCriteria.Criteria criteria = ccsUserConfigCriteria.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andZkAddressEqualTo(configInfo.getConfigAddr());
        criteria.andZkUserNameEqualTo(configInfo.getConfigUser());
        criteria.andZkPasswordEqualTo(configInfo.getConfigPwd());
        assertEquals(1, mapper.countByExample(ccsUserConfigCriteria));
    }

    @Test
    public void testUserPathIsExists() throws Exception {
        ZKClient zkClient = ZKPoolFactory.getZKPool(configInfo.getConfigAddr(), configInfo.getConfigUser(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, configInfo.getConfigPwd())).
                getZkClient(configInfo.getConfigAddr(), configInfo.getConfigUser());
        assertTrue(zkClient.exists(ConfigCenterUtil.appendUserWritablePathPath(userId)));
        assertTrue(zkClient.exists(ConfigCenterUtil.appendUserReadOnlyPathPath(userId)));
    }


    @Test
    public void testWritablePathAuth() throws Exception {
        ZKClient zkClient = ZKPoolFactory.getZKPool(configInfo.getConfigAddr(), configInfo.getConfigUser(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, configInfo.getConfigPwd())).
                getZkClient(configInfo.getConfigAddr(), configInfo.getConfigUser());
        zkClient.setNodeData(ConfigCenterUtil.appendUserWritablePathPath(userId), userId);
        assertEquals(userId, zkClient.getNodeData(ConfigCenterUtil.appendUserWritablePathPath(userId), false));
    }

    @Test(expected = KeeperException.NoAuthException.class)
    public void testReadOnlyPathAuth() throws Exception {
        ZKClient zkClient = ZKPoolFactory.getZKPool(configInfo.getConfigAddr(), configInfo.getConfigUser(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, configInfo.getConfigPwd())).
                getZkClient(configInfo.getConfigAddr(), configInfo.getConfigUser());
        zkClient.setNodeData(ConfigCenterUtil.appendUserReadOnlyPathPath(userId), userId);
    }

    @After
    public void clearData() throws Exception {
        CcsResourcePoolMapper mapper1 = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePoolCriteria ccsResourcePoolCriteria = new CcsResourcePoolCriteria();
        ccsResourcePoolCriteria.createCriteria().andZkTypeCodeEqualTo(ConfigCenterDubboConstants.ZKTypeCode.INNER.getFlag());
        List<CcsResourcePool> pools = mapper1.selectByExample(ccsResourcePoolCriteria);
        int result1 = (userId.hashCode() % pools.size());

        ZKClient zkClient = ZKPoolFactory.getZKPool(configInfo.getConfigAddr(), pools.get(result1).getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants
                        .operators, pools.get(result1).getSuperAuthPassword())).getZkClient(configInfo.getConfigAddr(),
                pools.get(result1).getSuperAuthName()).addAuth(ConfigCenterConstants.
                ZKAuthSchema.DIGEST, configInfo.getConfigUser() + ":" + CiperUtil.decrypt(ConfigCenterConstants
                .operators, configInfo.getConfigPwd()));
        zkClient.deleteNode(appendUserRootNode());
        assertFalse(zkClient.exists(appendUserRootNode()));

        CcsUserConfigMapper mapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        CcsUserConfigCriteria.Criteria criteria = ccsUserConfigCriteria.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andZkAddressEqualTo(configInfo.getConfigAddr());
        criteria.andZkUserNameEqualTo(configInfo.getConfigUser());
        criteria.andZkPasswordEqualTo(configInfo.getConfigPwd());
        mapper.deleteByExample(ccsUserConfigCriteria);
    }

    private String appendUserRootNode() {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId;
    }
}