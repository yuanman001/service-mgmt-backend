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
public class CrashHandleTest {
	@Autowired
	IDbsResourceManage service;
	
	public void Crashtest() throws PaasException{
		//DbsResourceManageSv service = new DbsResourceManageSv();
		JSONObject o=new JSONObject();
		o.put("host","10.1.228.202");
		o.put("port",31316);
		o.put("instance", "devrdb21");
		String param=o.toString();
		service.handleCrash(param);
	}
	@Test
	public void recoverTest() throws PaasException{
		JSONObject o=new JSONObject();
		o.put("host","10.1.228.202");
		o.put("port",31316);
		o.put("instance", "devrdb21");
		String param=o.toString();
		service.recoverDistributeDb(param);
	}
	
}
