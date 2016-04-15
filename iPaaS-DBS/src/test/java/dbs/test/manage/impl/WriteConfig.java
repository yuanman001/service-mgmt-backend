package dbs.test.manage.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class WriteConfig {
	
	@Autowired
	private ICCSComponentManageSv componentManageSv;
	
	
	public  Properties readProp() throws IOException {
		
		InputStream inputStream = WriteConfig.class.getClassLoader().getResourceAsStream("config.properties");
		if(inputStream == null) {
			throw new RuntimeException("config file not exists!");
		}
		
		Properties prop = new Properties();
		
		prop.load(inputStream);
		
		return prop;
	}
	
	
	public  void  write() throws PaasException, IOException {
		
		Properties prop = this.readProp();
		
		if(prop == null) {
			return;
		}
		Set<Entry<Object, Object>> set = prop.entrySet();
		
		for(Entry entry  : set ){
			
			String path = (String) entry.getKey();
			
			String data = (String) entry.getValue();
			
			System.out.println("the path is " + path);
			System.out.println("the data is " + data);
			CCSComponentOperationParam  param  = new CCSComponentOperationParam();
			param.setPath(path);
			param.setUserId("FFF49D0D518948D0AB28D7A8EEE25D03");
			param.setPathType(PathType.READONLY);
			componentManageSv.add(param, data);
		}
		
		
	}
	
	
	public void delete() throws PaasException {
		
		CCSComponentOperationParam  param  = new CCSComponentOperationParam();
		String path = "/DBS/DBS001/meta/db";
		param.setPath(path);
		param.setUserId("FFF49D0D518948D0AB28D7A8EEE25D03");
		param.setPathType(PathType.READONLY);
		if(componentManageSv.exists(param)){
		    System.err.println(path + "节点存在，进行清除!");
			componentManageSv.delete(param);
		}else {
			 System.err.println(path + "节点不存在!=========================");
		}
		
	}
	
	@Test
	public void read() throws PaasException {
		
		CCSComponentOperationParam  param  = new CCSComponentOperationParam();
		String path = "/DBS/DBS004/logicdb/db_partition_001";
		param.setPath(path);
		//param.setUserId("FFF49D0D518948D0AB28D7A8EEE25D03");
		param.setUserId("22E750F3714D4A078F5C765CA45CC841");
		param.setPathType(PathType.READONLY);
		//param.setPathType(PathType.WRITABLE);
		if(componentManageSv.exists(param)){
		    System.err.println(path + "节点存在，进行清除!");
		    //componentManageSv.delete(param);
		    //System.err.println(path + "删除成功!");
		    String value = componentManageSv.get(param);
		    System.err.println(path + "节点的值是" + value);
		}else {
			 System.err.println(path + "节点不存在!=========================");
		}
		
	}
	
	
	
	
	
	
	

	
}
