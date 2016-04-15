package ccs.test.manage.impl;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsServiceUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.*;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.IConfigCenterServiceManager;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.service.util.ZookeeperClientUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.UUIDTool;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/config-client-consumer.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CreateServiceTest {
    @Reference
    private IConfigCenterServiceManager configCenterManager;

    private static CcsServiceUserConfig configServiceInfo;

    private static String userId;

    private String serviceId;

    private static ConfigInfo configInfo;

    @Before
    public void setUp() throws Exception {
        userId = UUIDTool.genId32();
        serviceId = UUIDTool.genId32();

        String result = configCenterManager.init("{'userId':'" + userId + "'}");
        Gson gson = new Gson();
        configInfo = gson.fromJson(result, ConfigInfo.class);

        String var1 = "{'userId':'" + userId + "','applyType':'create','serviceId':'" + serviceId + "', 'timeOut':2000}";
        configCenterManager.create(var1);
    }

    @Test
    public void testCCSNode() throws Exception {
        List<CcsServiceUserConfig> configs = getCcsServiceUserConfigs();
        assertEquals(1, configs.size());

        ZKClient client = ZookeeperClientUtil.getZkClientFromPool(configInfo.getConfigAddr(), configInfo.getConfigUser()
                , CiperUtil.decrypt(ConfigCenterConstants.operators, configInfo.getConfigPwd()));
        assertTrue(client.exists(appendUserRootNode() + "/CCS-SERVICE/" + serviceId));
        assertEquals("{\"zkAddr\":\"" + configs.get(0).getZkAddress() + "\",\"zkUser\":\"" + userId + "\",\"zkPwd\":\""
                        + configs.get(0).getZkPassword() + "\",\"timOut\":2000, \"serviceId\":\"" + serviceId + "\"}",
                client.getNodeData(appendUserRootNode() + "/CCS-SERVICE/" + serviceId));
    }

    @Test
    public void testUserServiceConfigNode() throws Exception {
        List<CcsServiceUserConfig> configs = getCcsServiceUserConfigs();
        assertEquals(1, configs.size());
        CcsServiceUserConfig config = configs.get(0);
        ZKClient client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
        assertTrue(client.exists(appendCustomUserRootNode()));
    }

    private List<CcsServiceUserConfig> getCcsServiceUserConfigs() {
        CcsServiceUserConfigMapper mapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfigCriteria configCriteria = new CcsServiceUserConfigCriteria();
        configCriteria.createCriteria().andUserIdEqualTo(userId).andServiceIdEqualTo(serviceId);
        return mapper.selectByExample(configCriteria);
    }


    @After
    public void tearDown() throws Exception {
        ZKClient zkClient = null;
        ZKClient zkClient1 = null;
        CcsServiceUserConfigMapper mapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfigCriteria configCriteria = new CcsServiceUserConfigCriteria();
        configCriteria.createCriteria().andUserIdEqualTo(userId);
        List<CcsServiceUserConfig> configs = mapper.selectByExample(configCriteria);
        assertEquals(1, configs.size());
        configServiceInfo = configs.get(0);

        zkClient = clearCCSComponent();
        zkClient1 = clearConfigServiceInfo();

        mapper.deleteByPrimaryKey(configServiceInfo.getId());
        clearData();
    }

    private void clearData() {
        CcsUserConfigMapper mapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        CcsUserConfigCriteria.Criteria criteria = ccsUserConfigCriteria.createCriteria();
        criteria.andUserIdEqualTo(userId);
        criteria.andZkAddressEqualTo(configInfo.getConfigAddr());
        criteria.andZkUserNameEqualTo(configInfo.getConfigUser());
        criteria.andZkPasswordEqualTo(configInfo.getConfigPwd());
        mapper.deleteByExample(ccsUserConfigCriteria);
    }

    private ZKClient clearCCSComponent() throws Exception {
        CcsResourcePool pool = selectRandomZkMachine(userId, ConfigCenterDubboConstants.ZKTypeCode.INNER);

        ZKClient zkClient1 = ZookeeperClientUtil.getZkClientFromPool(configInfo.getConfigAddr(),
                pool.getSuperAuthName(), CiperUtil.decrypt(ConfigCenterConstants
                        .operators, pool.getSuperAuthPassword())).addAuth(ConfigCenterConstants.
                ZKAuthSchema.DIGEST, configInfo.getConfigUser() + ":" + CiperUtil.decrypt(ConfigCenterConstants
                .operators, configInfo.getConfigPwd()));
        zkClient1.deleteNode(appendUserRootNode());
        assertFalse(zkClient1.exists(appendUserRootNode()));
        return zkClient1;
    }

    private ZKClient clearConfigServiceInfo() throws Exception {
        CcsResourcePool pool = selectRandomZkMachine(userId, ConfigCenterDubboConstants.ZKTypeCode.CUSTOM);

        ZKClient zkClient = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(),
                pool.getSuperAuthName(), CiperUtil.decrypt(ConfigCenterConstants
                        .operators, pool.getSuperAuthPassword()));
        zkClient.deleteNode(appendCustomUserRootNode());
        assertFalse(zkClient.exists(appendCustomUserRootNode()));
        return zkClient;
    }

    private String appendUserRootNode() {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR 
        		+ userId + ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_HAS_READ_PREFIX
                ;
    }

    private String appendCustomUserRootNode() {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId
                + ConfigCenterConstants.SEPARATOR + serviceId;
    }

    private CcsResourcePool selectRandomZkMachine(String userId, ConfigCenterDubboConstants.ZKTypeCode type) {
        CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePoolCriteria ccsResourcePoolCriteria = new CcsResourcePoolCriteria();
        ccsResourcePoolCriteria.createCriteria().andZkTypeCodeEqualTo(type.getFlag());
        List<CcsResourcePool> pools = mapper.selectByExample(ccsResourcePoolCriteria);
        int result1 = (userId.hashCode() % pools.size());
        return pools.get(result1);
    }
}
