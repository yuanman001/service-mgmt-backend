package test.idps.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.idps.service.interfaces.IIdpsSv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/idps-consumer.xml",
		"classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class IDPSComponentTest {
	@Autowired
	private IIdpsSv idpsSrv;
	
	@Test
	public void create() throws Exception {
		String param ="{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"create\","
				+ "\"serviceId\":\"IDPS123\",\"nodeNum\":\"1\",\"dssPId\":\"0A8111DB280044528DF309D501DFFF6A\","
				+ "\"dssServiceId\":\"DSS001\",\"dssServicePwd\":\"123456\",\"serviceName\":\"first\"}";
		idpsSrv.open(param, "no");
	}
	
	@Test
	public void createMany() throws Exception {
		String param ="{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"create\","
				+ "\"serviceId\":\"IDPS234\",\"nodeNum\":\"2\",\"dssPId\":\"0A8111DB280044528DF309D501DFFF6A\","
				+ "\"dssServiceId\":\"DSS001\",\"dssServicePwd\":\"123456\",\"serviceName\":\"many\"}";
		idpsSrv.open(param, "no");
	}
	
}
