package test.idps.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.idps.manage.rest.interfaces.ImageDynProcServiceManager;
import com.alibaba.dubbo.config.annotation.Reference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/idps-consumer.xml" })

public class TestIdpsDubbo {
	@Reference
	private ImageDynProcServiceManager imageDynProcServiceManager;
	
	
	@Test
	public void getFuncList() {
		String fl = imageDynProcServiceManager.getFuncList();
		System.out.println(fl);
	}
	
	@Test
	public void create() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"BF512D2696004534BE9EB66161E52EF7\",\"applyType\":\"create\",\"serviceId\":\"IDPS034\",\"nodeNum\":\"1\","
				+ "\"dssPId\":\"0A8111DB280044528DF309D501DFFF6A\",\"dssServiceId\":\"DSS001\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"first\"}";
		System.out.println("----------create-res:\n"+imageDynProcServiceManager.create(param));
		System.out.println("----------开通单台，耗时："+(System.currentTimeMillis()-s));

	}
	
	@Test
	public void createMany() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"BF512D2696004534BE9EB66161E52EF7\",\"applyType\":\"create\",\"serviceId\":\"IDPS033\",\"nodeNum\":\"2\","
				+ "\"dssPId\":\"0A8111DB280044528DF309D501DFFF6A\",\"dssServiceId\":\"DSS001\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"many\"}";
		System.out.println("----------create-res:\n"+imageDynProcServiceManager.create(param));
		System.out.println("----------开通多台，耗时："+(System.currentTimeMillis()-s));
	}
	@Test
	public void modify() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"modify\",\"serviceId\":\"104\",\"capacity\":\"152\","
				+ "\"haMode\":\"single\",\"serviceName\":\"test\"}";
		System.out.println("----------modify-res:\n"+imageDynProcServiceManager.modify(param));
		System.out.println("----------修改单例，耗时："+(System.currentTimeMillis()-s));

	}
	

}
