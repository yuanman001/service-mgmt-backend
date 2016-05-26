package dbs.test.manage.impl;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsPhysicalResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsLogicResourcePoolSv;
import com.ai.paas.ipaas.dbs.util.DistributeDbConstants;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})

public class DbsLogicResourcePoolSvTest {

	@Autowired
	IDbsLogicResourcePoolSv isv;
	
	@Test
	public void modTest(){
		
		DbsPhysicalResourcePoolMapper dbsPhysicalResourcemapper=ServiceUtil.getMapper(DbsPhysicalResourcePoolMapper.class);
		DbsPhysicalResourcePoolCriteria dbsPhysicalResourcePoolCriteria=new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria=dbsPhysicalResourcePoolCriteria.createCriteria();
		criteria.andIsUsedEqualTo(DistributeDbConstants.ResUseStatus.RES_FREE);
		List<DbsPhysicalResourcePool> modelList  = dbsPhysicalResourcemapper.selectByExample(dbsPhysicalResourcePoolCriteria);
		System.out.println("++++++++++"+modelList.size()+"++++++++++++++++++++++++");
	}
	
	
}
