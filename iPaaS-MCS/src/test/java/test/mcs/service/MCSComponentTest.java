package test.mcs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.mcs.service.interfaces.IMcsSv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/mcs-consumer.xml",
		"classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class MCSComponentTest {
	
	@Autowired
	private IMcsSv mcsSrv;
	
	@Test
	public void create() throws Exception {
	    String param = "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"create\","
	    		+ "\"serviceId\":\"MCS123\",\"capacity\":\"128\",\"haMode\":\"single\",\"serviceName\":\"test123\"}";
		mcsSrv.openMcs(param);
	}
}
