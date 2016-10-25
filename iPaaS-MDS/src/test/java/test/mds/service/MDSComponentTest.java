package test.mds.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManager;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/mds-client-consumer.xml" , "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class MDSComponentTest {
	
	@Autowired
	private IMsgSrvManager msgSrvManager;
	
	@Test
	public void create() throws PaasException {
		MsgSrvApply apply = new MsgSrvApply();
		apply.setApplyType("create");
		apply.setUserId("6C4F4DBA96294DDCBC5DBBF2CAD442B5");
		apply.setServiceId("MDS123");
		apply.setTopicEnName("TEST_MDS123_1");
		apply.setTopicName("test topic");
		apply.setTopicPartitions(5);
		apply.setMsgReplica(2);
		apply.setMaxSender(5);
		msgSrvManager.createMessageService(apply);
	}
	
}
