package dbs.test.manage.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.dbs.manage.rest.interfaces.IDistributeDbServiceManager;
import com.alibaba.dubbo.config.annotation.Reference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/dbs-consumer.xml" })
public class MuiCTest {

	@Reference
	IDistributeDbServiceManager service;
	
	@Test
	public void testAgain(){
		String temp="{\"serviceId\":\"DBS003\",\"userId\":\"AAC5A58917984C5CA63311B11E9C57B2\",\"isNeedDistributeTrans\":\"true\",\"masterNum\":\"1\",\"isAutoSwitch\":\"true\",\"applyType\":\"create\",\"isMysqlProxy\":\"true\",\"serviceName\":\"test_dbs\"}";
		String result=service.create(temp);
		System.out.println(result);
		/*AgentClient agent=new AgentClient("10.1.228.202",60004);
		String result=agent.executeInstruction("ls");
		System.out.println(result);*/
	}
	
	
	/*public void test(){
		DbsMuiAuthParamVo paramVo=new DbsMuiAuthParamVo();
		paramVo.setUrl("127.0.0.1:8080");
		paramVo.setUserId("DBS001");
		Gson gson=new Gson();
		String param=gson.toJson(paramVo);
		String result=service.checkMUIAuth(param);
		System.out.println(result);
	}
	

	public void register(){
		OpenResourceParamVo openParamVo=new OpenResourceParamVo();
		openParamVo.setMasterNum(1);
		openParamVo.setNeedDistributeTrans(true);
		openParamVo.setServiceId("DBS004");
		openParamVo.setUserId("3963F1D4CA54471D9852A8A538DE9F93");
		//openParamVo.setUsername(\"FFF49D0D518948D0AB28D7A8EEE25D03\");
		Gson gson=new Gson();
		String param=gson.toJson(openParamVo);
		
		try {
			String result=service.create(param);
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
