package ccs.test.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.service.ICCSManageSv;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;

/**
 * Created by astraea on 2015/5/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:dubbo/consumer/config-client-consumer.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class CCSComponentTest {

    @Autowired
    private IConfigCenterServiceManageSv iConfigCenterServiceManageSv;

    @Autowired
    private ICCSManageSv iccsManageSv;

    @Test
    public void testAdd() throws PaasException {
        CreateServiceInfo createServiceInfo = new CreateServiceInfo();
        createServiceInfo.setApplyType("create");
        createServiceInfo.setServiceId("CCS003");
        createServiceInfo.setTimeOut(2000);
        createServiceInfo.setUserId("6C4F4DBA96294DDCBC5DBBF2CAD442B5");
        iConfigCenterServiceManageSv.createService(createServiceInfo);
    }

//    @Test
//    public void testCiperUtil() throws  PaasException{
//        System.out.println(CiperUtil.decrypt(ConfigCenterConstants.operators, "c4f9c1f00f342f066f33354354bc139d"));
//    }
//
//    @Test
//    public void testAddConfig() throws PaasException{
//        CCSOperationParam param = new CCSOperationParam();
//        param.setServiceId("CCS005");
//        param.setUserId("528FFA8BC5CA4B72805443B368994FCB");
//        param.setPath("/TESTTEST6");
//        iccsManageSv.add(param, "");        
//        
//        param.setServiceId("CCS006");
//        param.setUserId("528FFA8BC5CA4B72805443B368994FCB");
//        param.setPath("/TESTTEST6");
//        iccsManageSv.add(param, "");
//    }
//    
//    @Test
//    public void testGetServices() throws PaasException{
//        CCSOperationParam param = new CCSOperationParam();
//        param.setUserId("FFF49D0D518948D0AB28D7A8EEE25D03");
//        List<String> result = iccsManageSv.getServices(param);
//        for(String s : result){
//        	System.out.println(s);
//        }
//    }
}
