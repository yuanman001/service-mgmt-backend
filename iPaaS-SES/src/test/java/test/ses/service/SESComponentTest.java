package test.ses.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.ses.service.interfaces.ISesManage;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApply;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/ses-consumer.xml",
		"classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class SESComponentTest {
	
	@Autowired
	private ISesManage sesSrv;
	
	@Test
	public void create() throws Exception {
		SesSrvApply apply = new SesSrvApply();
		apply.setApplyType("create");
    	apply.setClusterNum(3);
    	apply.setReplicasNum(1);
    	apply.setServiceId("SES123");
    	apply.setServiceName("ses-test");
    	apply.setShardNum(2);
    	apply.setUserId("6C4F4DBA96294DDCBC5DBBF2CAD442B5");
    	apply.setSesMem(64);
	    sesSrv.createSesService(apply);
	}
}
