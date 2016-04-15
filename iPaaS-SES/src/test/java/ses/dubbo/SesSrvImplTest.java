package ses.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.rest.vo.BaseInfo;
import com.ai.paas.ipaas.ses.manage.rest.interfaces.ISearchEngineServiceManager;
import com.ai.paas.ipaas.ses.service.vo.SesMappingApply;
import com.ai.paas.ipaas.ses.service.vo.SesSrvApply;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/ses-consumer.xml" })

public class SesSrvImplTest {
	@Reference
	private ISearchEngineServiceManager iSearchEngineServiceManager;
	

    private String userId = "83647DAF5C9C460892E8E8B4125BF8DA";

    private String serviceId = "SES003";




    @Test
    public void testCreateSesService() throws Exception {
    	SesSrvApply apply = new SesSrvApply();
    	apply.setApplyType("create");
    	apply.setClusterNum(2);
    	apply.setReplicasNum(1);
    	apply.setServiceId(serviceId);
    	apply.setServiceName("sb service");
    	apply.setShardNum(2);
    	apply.setUserId(userId);
    	apply.setSesMem(64);
//    	System.out.println(new Gson().toJson(apply));
    	String res = iSearchEngineServiceManager.create(new Gson().toJson(apply));
    	System.out.print(res);
    	
    	
    }
    @Test
    public void start() throws Exception {
    	
		 BaseInfo mappingApply = new BaseInfo();
		 mappingApply.setUserId(userId);
		 mappingApply.setServiceId(serviceId);
		 iSearchEngineServiceManager.start(new Gson().toJson(mappingApply));
    }
    @Test
    public void stop() throws Exception {
    	
    	BaseInfo mappingApply = new BaseInfo();
    	mappingApply.setUserId(userId);
    	mappingApply.setServiceId(serviceId);
    	iSearchEngineServiceManager.stop(new Gson().toJson(mappingApply));
    }
    @Test
    public void recycle() throws Exception {
    	
    	BaseInfo mappingApply = new BaseInfo();
    	mappingApply.setUserId(userId);
    	mappingApply.setServiceId(serviceId);
    	iSearchEngineServiceManager.recycle(new Gson().toJson(mappingApply));
    }
    @Test
    public void testCreateIndex() throws Exception {
    	
    	SesMappingApply mappingApply = new SesMappingApply();
    	mappingApply.setUserId("B9178FB878834E7BA8CD02FB981C7F4D");
    	mappingApply.setServiceId(serviceId);
    	mappingApply.setMapping("{\"457119323\":{\"dynamic\":\"strict\",\"_id\" : {\"path\" : \"acct_id\"},\"properties\":{\"acct_id\" : {\"type\" : \"string\", \"store\" : true },\"attr_code\" : {\"type\" : \"string\", \"store\" : true },\"attr_value\" : {\"type\" : \"string\"},\"city_code\" : {\"type\" : \"string\" },\"cust_id\" : {\"type\" : \"string\" },\"cust_name\" : {\"type\" : \"string\", \"store\" : true , \"indexAnalyzer\":\"ik\" ,\"searchAnalyzer\": \"ik\"},\"pay_name\" : {\"type\" : \"string\"}}}}");
    	
//		 System.out.println(new Gson().toJson(mappingApply));
    	iSearchEngineServiceManager.mapping(new Gson().toJson(mappingApply));
//		 iSearchEngineServiceManager.index(new Gson().toJson(mappingApply));
    }
    

}