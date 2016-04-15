package dbs.test.manage.impl;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;
import com.ai.paas.ipaas.dbs.service.IDbsResourceManage;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext-mybatis.xml"})
public class DistributeDbServiceManageTest {
	@Autowired
	IDbsResourceManage service;
	@Autowired
	private ICCSComponentManageSv componentManageSv;
	@Test
	public void test(){
		String temp="{\"serviceId\":\"DBS002\",\"userId\":\"22E750F3714D4A078F5C765CA45CC841\",\"isNeedDistributeTrans\":\"false\",\"masterNum\":\"5\",\"applyType\":\"create\",\"serviceName\":\"12\"}";
		OpenResourceParamVo openParamVo=new OpenResourceParamVo();
		openParamVo.setMasterNum(1);
		openParamVo.setNeedDistributeTrans(true);
		openParamVo.setServiceId("DBS004");
		openParamVo.setUserId("3963F1D4CA54471D9852A8A538DE9F93");
		//openParamVo.setUsername(\"FFF49D0D518948D0AB28D7A8EEE25D03\");
		
		
		try {
			service.applyDistributeDd(openParamVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void read() throws PaasException{
		CCSComponentOperationParam  param  = new CCSComponentOperationParam();
		String path = "/DBS/DBS006/meta/db/db_pattern";
		param.setPath(path);
		param.setUserId("3963F1D4CA54471D9852A8A538DE9F93");
		param.setPathType(PathType.WRITABLE);
		if(componentManageSv.exists(param)){
			System.out.println(componentManageSv.get(param));
		}else {
			 System.err.println(path + "节点不存在!=========================");
		}
		
	}
	
	public void handle() throws PaasException{
		JSONObject json=new JSONObject();
		json.put("host", "10.1.228.202");
		json.put("port", 31306);
		json.put("instance","devrdb11");
		service.handleCrash(json.toString());
	}
}
