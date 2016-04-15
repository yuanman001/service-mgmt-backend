package com.ai.paas.ipaas.ccs.service.impl;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsServiceUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.*;
import com.ai.paas.ipaas.ccs.service.ICCSManageSv;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.service.util.ZookeeperClientUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.UUIDTool;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/applicationContext.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CCSManageSvImplTest {

    private String userId = UUIDTool.genId32();

    private String serviceId = UUIDTool.genId32();

    @Autowired
    private ICCSManageSv manageSv;

    @Autowired
    private IConfigCenterServiceManageSv iConfigCenterServiceManageSv;

    @Before
    public void setUp() throws Exception {
        iConfigCenterServiceManageSv.createUserNode(userId);
        CreateServiceInfo createServiceInfo = new CreateServiceInfo();
        createServiceInfo.setServiceId(serviceId);
        createServiceInfo.setApplyType("create");
        createServiceInfo.setTimeOut(1000);
        createServiceInfo.setUserId(userId);
        iConfigCenterServiceManageSv.createService(createServiceInfo);

        CCSOperationParam ccsOperationParam = new CCSOperationParam();
        ccsOperationParam.setUserId(userId);
        ccsOperationParam.setServiceId(serviceId);
        ccsOperationParam.setPath("/test/testPath");
        manageSv.add(ccsOperationParam, "Test data");
        assertTrue(manageSv.exists(ccsOperationParam));
        assertEquals("Test data", manageSv.get(ccsOperationParam));
    }

    @After
    public void tearDown() throws Exception {
        CcsServiceUserConfigMapper mapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfigCriteria criteria = new CcsServiceUserConfigCriteria();
        criteria.createCriteria().andServiceIdEqualTo(serviceId).andUserIdEqualTo(userId);
        List<CcsServiceUserConfig> configs = mapper.selectByExample(criteria);
        assertEquals(1, configs.size());

        CcsServiceUserConfig config = configs.get(0);
        ZKClient client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
        client.deleteNode(ConfigCenterUtil.appendUserRootPathPath(userId));

        mapper.deleteByPrimaryKey(config.getId());

        CcsUserConfigMapper ccsUserConfigMapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria userConfigCriteria = new CcsUserConfigCriteria();
        userConfigCriteria.createCriteria().andUserIdEqualTo(userId);
        List<CcsUserConfig> userConfigs = ccsUserConfigMapper.selectByExample(userConfigCriteria);
        assertEquals(1, userConfigs.size());
        CcsUserConfig userConfig = userConfigs.get(0);

        CcsResourcePoolMapper poolMapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePool pool = poolMapper.selectByPrimaryKey(userConfig.getCcsResourceId());
        assertNotNull(pool);

        ZKClient client2 = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), pool.getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword())).addAuth("digest",
                userConfig.getZkUserName() + ":" + CiperUtil.decrypt(ConfigCenterConstants.operators, userConfig.getZkPassword()));

        client2.deleteNode(ConfigCenterUtil.appendUserRootPathPath(userId));
        ccsUserConfigMapper.deleteByPrimaryKey(userConfig.getId());
    }

    @Test
    public void testModify() throws Exception {
        CCSOperationParam ccsOperationParam = new CCSOperationParam();
        ccsOperationParam.setUserId(userId);
        ccsOperationParam.setServiceId(serviceId);
        ccsOperationParam.setPath("/test/testPath");
        manageSv.modify(ccsOperationParam, "modify Value1");
        assertTrue(manageSv.exists(ccsOperationParam));
        assertEquals("modify Value1", manageSv.get(ccsOperationParam));
    }

    @Test
    public void testDelete() throws Exception {
        CCSOperationParam ccsOperationParam = new CCSOperationParam();
        ccsOperationParam.setUserId(userId);
        ccsOperationParam.setServiceId(serviceId);
        ccsOperationParam.setPath("/test/testPath");
        manageSv.delete(ccsOperationParam);
        assertFalse(manageSv.exists(ccsOperationParam));
    }

    @Test
    public void testListSubPath() throws Exception {
        CCSOperationParam ccsOperationParam = new CCSOperationParam();
        ccsOperationParam.setUserId(userId);
        ccsOperationParam.setServiceId(serviceId);
        ccsOperationParam.setPath("/test");

        List<String> children = manageSv.listSubPath(ccsOperationParam);
        assertEquals(1, children.size());
    }
}