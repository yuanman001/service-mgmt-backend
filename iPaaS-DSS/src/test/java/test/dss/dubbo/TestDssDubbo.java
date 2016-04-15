package test.dss.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.dss.manage.rest.interfaces.IDocumentStoreServiceManager;
import com.alibaba.dubbo.config.annotation.Reference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/dss-consumer.xml",
		"classpath*:context/applicationContext-mybatis.xml" })
public class TestDssDubbo {
	@Reference
	private IDocumentStoreServiceManager documentStoreServiceManagerImpl;

	@Test
	public void getFuncList() {
		String fl = documentStoreServiceManagerImpl.getFuncList();
		System.out.println(fl);
	}

	@Test
	public void create() {
		System.out
				.println(documentStoreServiceManagerImpl
						.create("{'userId':'67969406E7394E218ADE8FCB9FFEE516','applyType':'create','serviceId':'DSS001','capacity':'999','singleFileSize':'3','serviceName':'TEST'}"));
	}

	@Test
	public void cleanAll() {
		System.out
				.println(documentStoreServiceManagerImpl
						.cleanAll("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'cleanAll','serviceId':'DSS001'}"));

	}

	@Test
	public void cleanOne() {
		System.out
				.println(documentStoreServiceManagerImpl
						.cleanOne("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'cleanOne','serviceId':'DSS001','key':'55753f1c1fda9a39b47396f8'}"));
	}

	@Test
	public void cancel() {
		System.out
				.println(documentStoreServiceManagerImpl
						.cancel("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'cancel','serviceId':'DSS002'}"));
	}

	@Test
	public void getStatus() {
		System.out.println(documentStoreServiceManagerImpl
						.getStatus("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'getStatus','serviceId':'DSS001'}"));
	}

	@Test
	public void getRecord() {
		System.out
				.println(documentStoreServiceManagerImpl
						.getRecord("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'getRecord','serviceId':'DSS004','key':'557673c21fda9a35b8339a7f'}"));
	}

	@Test
	public void modify() {
		System.out
				.println(documentStoreServiceManagerImpl
						.modify("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'modify','serviceId':'DSS001','size':'1062','limitFileSize':'2'}"));

	}

	@Test
	public void upload() {
		System.out
				.println(documentStoreServiceManagerImpl
						.upload("{'userId':'22E750F3714D4A078F5C765CA45CC841','applyType':'upload','serviceId':'DSS004','fileName':'t','fileType':'txt','bytes':[-27,-109,-120,-27,-106,-67,-26,-120,-111,-25,-102,-124],'remark':'test'}"));
	}

}
