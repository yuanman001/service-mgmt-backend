package test.dss.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.service.IDSSSv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/dss-client-consumer.xml",
		"classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class DSSComponentTest {
	
	@Autowired
	private IDSSSv dssSrv;
	
	@Test
	public void create() throws Exception {
		ApplyDSSParam apply = new ApplyDSSParam();
		apply.setApplyType("create");
		apply.setUserId("6C4F4DBA96294DDCBC5DBBF2CAD442B5");
		apply.setServiceId("DSS123");
		dssSrv.createDSS(apply);
	}
	
}
