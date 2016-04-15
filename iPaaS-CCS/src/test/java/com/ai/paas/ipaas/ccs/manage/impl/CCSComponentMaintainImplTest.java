package com.ai.paas.ipaas.ccs.manage.impl;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePoolCriteria;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfigCriteria;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.ICCSComponentMaintain;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.IConfigCenterServiceManager;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSResultDTO;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.ccs.zookeeper.impl.ZKPoolFactory;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.UUIDTool;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/config-client-consumer.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CCSComponentMaintainImplTest {

    private static String userId = UUIDTool.genId32();

    private static ConfigInfo configInfo;

    //@Reference
    private IConfigCenterServiceManager configCenterManager;

    // @Reference
    private ICCSComponentMaintain iccsComponentMaintain;

    @Autowired
    private ICCSComponentManageSv manageSv;


    //@Before
    public void setUp() throws Exception {
        userId = UUIDTool.genId32();
        String result = configCenterManager.init("{'userId':'" + userId + "'}");
        Gson gson = new Gson();
        configInfo = gson.fromJson(result, ConfigInfo.class);

        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath("/test/tttttt");
        param.setPathType(ConfigCenterDubboConstants.PathType.WRITABLE);
        manageSv.add(param, "Test Value");
    }

    // @After
    public void tearDown() throws Exception {
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


    @Test
    public void testGet() throws Exception {
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setPathType(ConfigCenterDubboConstants.PathType.WRITABLE);
        param.setPath("/test/tttttt");
        param.setUserId(userId);
        System.out.println(iccsComponentMaintain.get(new Gson().toJson(param)));
    }

    private String appendUserRootNode() {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId;
    }

    @Test
    public void test() {
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setUserId(userId);
        param.setPath("/test/tttttt");
        param.setPathType(ConfigCenterDubboConstants.PathType.WRITABLE);
        Gson gson = new Gson();
        String value = gson.toJson(param);
        System.out.println(gson.toJson(param));

        CCSComponentOperationParam param1 = gson.fromJson(value, CCSComponentOperationParam.class);
        assertEquals(ConfigCenterDubboConstants.PathType.READONLY, param1.getPathType());
    }

    @Test
    public void test1() {
        CCSResultDTO dto = new CCSResultDTO();
        List<String> data = new ArrayList<String>();
        data.add("1");
        data.add("2");
        data.add("3");
        dto.setData(data);
        dto.setResultMessage("list success");
        dto.setResultCode("000000");

        Gson gson = new Gson();
        System.out.println(gson.toJson(dto));
    }
}