package com.ai.paas.ipaas.des.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.des.manage.model.DesFilterTableParam;
import com.ai.paas.ipaas.des.manage.model.DesServiceBindParam;
import com.ai.paas.ipaas.des.manage.model.DesUserServiceParam;
import com.ai.paas.ipaas.des.manage.rest.interfaces.IDataExtractServiceManager;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/des-client-consumer.xml", "classpath:dubbo/consumer/applicationContext.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class DataExtractServiceManageTest {

	@Autowired
	private IDataExtractServiceManager dataExtractServiceManager;

	@Test
	public void testCreate() {
		String param = "{\"userId\":\"FA5AA5D44D7042C891E471E50D323015\",\"serviceId\":\"DES002\"}";
		dataExtractServiceManager.create(param);
	}

	@Test
	public void testGetAllServices() {
		DesUserServiceParam desUserServiceParam = new DesUserServiceParam();
		desUserServiceParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		String result = dataExtractServiceManager.getAllServices(new Gson().toJson(desUserServiceParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
	
	@Test
	public void testBind() {
		DesServiceBindParam desServiceBindParam = new DesServiceBindParam();
		desServiceBindParam.setDbsServiceId("DBS001");
		desServiceBindParam.setServiceId("DES001");
		desServiceBindParam.setMdsServiceId("MDS002");
		desServiceBindParam.setMdsServicePassword("111111");
		desServiceBindParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		desServiceBindParam.setUserName("491676539@qq.com");
		String result = dataExtractServiceManager.bind(new Gson().toJson(desServiceBindParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
	
	@Test
	public void testGetBound(){
		DesUserServiceParam desUserServiceParam = new DesUserServiceParam();
		desUserServiceParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		String result =  dataExtractServiceManager.getBound(new Gson().toJson(desUserServiceParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
	
	@Test
	public void testGetBoundTables(){
		DesUserServiceParam desUserServiceParam = new DesUserServiceParam();
		desUserServiceParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		desUserServiceParam.setServiceId("DES001");
		String result = dataExtractServiceManager.getBoundTableInfo(new Gson().toJson(desUserServiceParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
	
	@Test
	public void testunBind(){
		DesServiceBindParam desServiceBindParam = new DesServiceBindParam();
		desServiceBindParam.setDbsServiceId("DBS001");
		desServiceBindParam.setMdsServiceId("MDS002");
		desServiceBindParam.setServiceId("DES001");
		desServiceBindParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		String result =  dataExtractServiceManager.unbind(new Gson().toJson(desServiceBindParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
	
	@Test
	public void testFilterTable(){
		DesFilterTableParam desFilterTableParam = new DesFilterTableParam();
		desFilterTableParam.setDbsServiceId("DBS001");
		desFilterTableParam.setServiceId("DES001");
		desFilterTableParam.setUserId("FA5AA5D44D7042C891E471E50D323015");
		desFilterTableParam.setTables(new String[]{"cust_bixy22","cust_bixy"});
		String result = dataExtractServiceManager.filterTable(new Gson().toJson(desFilterTableParam));
		System.out.println("****************************************");
		System.out.println(result);
		System.out.println("****************************************");
	}
}
