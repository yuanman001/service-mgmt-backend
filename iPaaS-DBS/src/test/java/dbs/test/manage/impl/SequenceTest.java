package dbs.test.manage.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.base.manager.ISequenceManageSv;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class SequenceTest {
	
	@Autowired
	ISequenceManageSv iSequenceManageSv;
	
	
	
	@Test
	public void getNextVal(){
		long nextval = iSequenceManageSv.nextVal("test_sequence");
		
		
		System.out.println(nextval);
	}
	

}
