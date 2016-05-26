package workflow.test.manage.impl;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.workflow.manage.rest.interfaces.IWorkFlowServiceManager;
import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowParamVo;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/config-client-consumer.xml" })
public class CreateServiceTest {

	private static Logger logger = Logger.getLogger(CreateServiceTest.class);
	@Reference
	private IWorkFlowServiceManager iWorkFlowServiceManager;

	@Test
	public void testUserServiceConfigNode() throws Exception {

		OpenWorkflowParamVo result = new OpenWorkflowParamVo();
		result.setServiceId("workflow001");
		result.setUserId("AAC5A58917984C5CA63311B11E9C57B2"); 
		Gson gson = new Gson();
		String createApply = gson.toJson(result);
		logger.debug(createApply);
		String resultMsg = iWorkFlowServiceManager.create(createApply);
		logger.debug("resultMsg=" + resultMsg);
//		iWorkflowSv.applyWorkflow(result);
	}
}
