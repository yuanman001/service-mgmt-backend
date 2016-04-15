package com.ai.paas.ipaas.des.test;

import java.util.Arrays;
import java.util.Map.Entry;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.des.service.ICcsServiceWraper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/applicationContext.xml", "classpath:dubbo/consumer/applicationContext-mybatis.xml" })
public class ICcsServiceWraperTest {

	@Autowired
	private ICcsServiceWraper ccsServiceWraper;

	@Test
	public void getTableInfoMapTest() throws PaasException {
		System.out.println("************************************");
		for (Entry<String, String> entry : ccsServiceWraper.getTablePaterns("FA5AA5D44D7042C891E471E50D323015", "DBS001", Arrays.asList("cust_bixy")).entrySet())
			System.out.println(entry.getKey() + "------------------------" + entry.getValue());
		System.out.println("************************************");
	}
}
