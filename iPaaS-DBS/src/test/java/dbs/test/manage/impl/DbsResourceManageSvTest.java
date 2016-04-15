package dbs.test.manage.impl;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.dbs.service.IDbsResourceManage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class DbsResourceManageSvTest {

	@Autowired
	IDbsResourceManage service;
	
	@Test
	public void handleCrash() throws PaasException{
		JSONObject json=new JSONObject();
		json.put("host", "10.1.228.202");
		json.put("port", 31306);
		json.put("instance","devrdb11");
		service.handleCrash(json.toString());
	}
}
