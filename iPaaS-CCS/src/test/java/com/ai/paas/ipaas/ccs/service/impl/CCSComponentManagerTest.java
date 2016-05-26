package com.ai.paas.ipaas.ccs.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.AddMode;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfigCriteria;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.ccs.zookeeper.impl.ZKPoolFactory;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.UUIDTool;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/applicationContext.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CCSComponentManagerTest {

    private final String testReadOnlyPathValue = "test readonly path value";

    private final String testWritablePathValue = "test writable path value";

    @Autowired
    private ICCSComponentManageSv manageSv;

    @Autowired
    private IConfigCenterServiceManageSv configCenterSv;

    private static String userId;

    private static ConfigInfo configInfo;

    private static String adminUser = "admin";

    private static String adminPasswd = CiperUtil.decrypt(ConfigCenterConstants.operators, "ec4c9e0e78f76a69");

    private String parentPath = "/test";
    private String readOnlyPath = parentPath + "/writables";
    private final String wrongPath = readOnlyPath + "BBBBBBBBBBBBBBBB";

    @Before
    public void setUp() throws Exception {
        userId = UUIDTool.genId32();
        configInfo = configCenterSv.createUserNode(userId);
        testAddReadOnlyPath();
        testAddWritablePath();
    }

    @After
    public void tearDown() throws Exception {
        ZKClient zkClient = ZKPoolFactory.getZKPool(configInfo.getConfigAddr(), configInfo.getConfigUser(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, configInfo.getConfigPwd())).getZkClient(configInfo.getConfigAddr(),
                configInfo.getConfigUser()).addAuth(ConfigCenterConstants.ZKAuthSchema.DIGEST, adminUser + ":" + adminPasswd);
        zkClient.deleteNode(appendUserRootNode());
        assertFalse(zkClient.exists(appendUserRootNode()));

        CcsUserConfigMapper mapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        CcsUserConfigCriteria.Criteria criteria = ccsUserConfigCriteria.createCriteria();
        criteria.andUserIdEqualTo(userId);
        mapper.deleteByExample(ccsUserConfigCriteria);
    }

    public void testAddReadOnlyPath() throws Exception {
        CCSComponentOperationParam param = createReadOnlyParam();
        manageSv.add(param, testReadOnlyPathValue);
        assertTrue(manageSv.exists(param));
        assertEquals(testReadOnlyPathValue, manageSv.get(param));
    }

    private CCSComponentOperationParam createReadOnlyParam() {
        CCSComponentOperationParam param = createConfigOperationParam(readOnlyPath, ConfigCenterDubboConstants.PathType.READONLY);
        return param;
    }

    public void testAddWritablePath() throws Exception {
        CCSComponentOperationParam param = createWritableParam();
        manageSv.add(param, testReadOnlyPathValue);
        assertTrue(manageSv.exists(param));
        assertEquals(testReadOnlyPathValue, manageSv.get(param));
    }

    public void testaddWritablePath() throws Exception {
        CCSComponentOperationParam param = createWritableParam();
        manageSv.add(param, testReadOnlyPathValue);
        assertTrue(manageSv.exists(param));
        assertEquals(testReadOnlyPathValue, manageSv.get(param));
    }

    @Test(expected = PaasException.class)
    public void testaddExistReadOnlyPath() throws Exception {
        CCSComponentOperationParam param = createReadOnlyParam();
        manageSv.add(param, testReadOnlyPathValue);
    }

    @Test
    public void testNotExists() throws Exception {
        CCSComponentOperationParam param = createConfigOperationParam(wrongPath, ConfigCenterDubboConstants.PathType.READONLY);
        assertFalse(manageSv.exists(param));
    }


    @Test
    public void testModifyReadOnlyPath() throws Exception {
        CCSComponentOperationParam param = createReadOnlyParam();
        manageSv.modify(param, testWritablePathValue);
        assertTrue(manageSv.exists(param));
        assertEquals(testWritablePathValue, manageSv.get(param));
    }

    @Test
    public void testModifyWritablePath() throws Exception {
        CCSComponentOperationParam param = createWritableParam();
        manageSv.modify(param, testWritablePathValue);
        assertTrue(manageSv.exists(param));
        assertEquals(testWritablePathValue, manageSv.get(param));
    }

    private CCSComponentOperationParam createWritableParam() {
        CCSComponentOperationParam param = createConfigOperationParam(readOnlyPath, ConfigCenterDubboConstants.PathType.WRITABLE);
        return param;
    }

    @Test(expected = PaasException.class)
    public void testModifyNoExistPath() throws Exception {
        CCSComponentOperationParam param = createConfigOperationParam(wrongPath, ConfigCenterDubboConstants.PathType.WRITABLE);
        manageSv.modify(param, testWritablePathValue);
    }

    @Test(expected = PaasException.class)
    public void testGetNoExistNode() throws Exception {
        CCSComponentOperationParam param = createConfigOperationParam(wrongPath, ConfigCenterDubboConstants.PathType.READONLY);
        manageSv.get(param);
    }

    private CCSComponentOperationParam createConfigOperationParam(String path, ConfigCenterDubboConstants.PathType readOnly) {
        CCSComponentOperationParam param = new CCSComponentOperationParam();
        param.setPath(path);
        param.setUserId(userId);
        param.setPathType(readOnly);
        return param;
    }

    @Test
    public void testDeleteWritablePath() throws Exception {
        CCSComponentOperationParam param = createWritableParam();
        manageSv.delete(param);
        assertFalse(manageSv.exists(param));
    }

    @Test
    public void testDeleteReadOnlyPath() throws Exception {
        CCSComponentOperationParam param = createReadOnlyParam();
        manageSv.delete(param);
        assertFalse(manageSv.exists(param));
    }

    @Test
    public void testListSubPath() throws Exception {
        CCSComponentOperationParam param = createConfigOperationParam(parentPath, ConfigCenterDubboConstants.PathType.READONLY);
        List<String> subsChildren = manageSv.listSubPath(param);
        assertEquals(1, subsChildren.size());
    }

    @Test(expected = PaasException.class)
    public void testListNotSubPath() throws Exception {
        CCSComponentOperationParam param = createConfigOperationParam(wrongPath, ConfigCenterDubboConstants.PathType.WRITABLE);
        List<String> subsChildren = manageSv.listSubPath(param);
    }

    private String appendUserRootNode() {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId;
    }

    @Test
    public void testAddMode() throws PaasException, IOException, InterruptedException {
        CCSComponentOperationParam param = createConfigOperationParam(readOnlyPath + "BBBBBBBBB", ConfigCenterDubboConstants.PathType.WRITABLE);
        assertFalse(manageSv.exists(param));
        manageSv.add(param, testReadOnlyPathValue, AddMode.EPHEMERAL);
        assertTrue(manageSv.exists(param));
    }
}