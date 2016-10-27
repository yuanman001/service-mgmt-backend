package testapi;


import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/ls-consumer.xml" , "classpath:dubbo/consumer/applicationContext-mybatis.xml"})
public class TestLsSpring {
//	@Autowired
//	private RDSInstanceManager incManager;

	
	Gson g = new Gson();
	
}
