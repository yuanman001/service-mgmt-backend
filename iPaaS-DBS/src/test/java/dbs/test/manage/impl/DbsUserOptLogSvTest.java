package dbs.test.manage.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLog;
import com.ai.paas.ipaas.dbs.service.IDbsUserOptLogSv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class DbsUserOptLogSvTest {

	@Autowired
	IDbsUserOptLogSv sv;
	@Test
	public void testAdd(){
		DbsUserOptLog log=new DbsUserOptLog();
		sv.addModel(log);
	}
	
}
