package com.ai.paas.ipaas.ccs.manage.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;

/**
 * Created by astraea on 2015/5/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/config-client-consumer.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class ConfigCenterServiceManageImplTest {
	
    @Autowired
    private IConfigCenterServiceManageSv iConfigCenterServiceManageSv;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInit() throws Exception {

    }

    @Test
    public void testGetFuncList() throws Exception {

    }

    @Test
    public void testCreate() throws Exception {

    }

    @Test
    public void testCancel() throws Exception {
    	CreateServiceInfo createServiceInfo = new CreateServiceInfo();
        createServiceInfo.setApplyType("cancel");
        createServiceInfo.setServiceId("CCS004");
        createServiceInfo.setTimeOut(2000);
        createServiceInfo.setUserId("1E17ADA7A7F74904A76045000B691956");
        iConfigCenterServiceManageSv.deleteService(createServiceInfo);
    }

    @Test
    public void testModify() throws Exception {

    }

    @Test
    public void testStart() throws Exception {

    }

    @Test
    public void testStop() throws Exception {

    }

    @Test
    public void testRestart() throws Exception {

    }
}