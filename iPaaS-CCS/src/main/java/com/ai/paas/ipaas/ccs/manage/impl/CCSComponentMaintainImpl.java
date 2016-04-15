package com.ai.paas.ipaas.ccs.manage.impl;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.ICCSComponentMaintain;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSResultDTO;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.service.dto.UserModel;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.ccs.zookeeper.impl.ZKPoolFactory;
import com.ai.paas.ipaas.util.CiperUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;
/**
 * Inner Service
 * @author Fenggw
 *
 */
@Service
public class CCSComponentMaintainImpl implements ICCSComponentMaintain {

    @Autowired
    private ICCSComponentManageSv iccsComponentManageSv;

    @Override
    public String add(String jsonParam) {
        CCSResultDTO dto = new CCSResultDTO();
        try {
            Gson gson = new Gson();
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);
            Map<String, String> value = gson.fromJson(jsonParam, Map.class);
            String data = value.get("data");

            iccsComponentManageSv.add(ccsComponentOperationParam, data);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("add Success");
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String modify(String jsonParam) {
        CCSResultDTO dto = new CCSResultDTO();
        try {
            Gson gson = new Gson();
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);
            Map<String, String> value = gson.fromJson(jsonParam, Map.class);
            String data = value.get("data");

            iccsComponentManageSv.modify(ccsComponentOperationParam, data);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("modify Success");
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String get(String jsonParam) {
        CCSResultDTO dto = new CCSResultDTO();
        Gson gson = new Gson();
        try {
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);

            String result = iccsComponentManageSv.get(ccsComponentOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("get config Success");
            dto.setData(result);
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String delete(String jsonParam) {
        Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);
            iccsComponentManageSv.delete(ccsComponentOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("delete Success");
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String listSubPath(String jsonParam) {
        Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);
            List<String> result = iccsComponentManageSv.listSubPath(ccsComponentOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("list sub path Success");
            dto.setData(result);
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }
    
	@Override
	public String listSubPathAndData(String jsonParam) {
		Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(jsonParam, CCSComponentOperationParam.class);
            List<CCSSubListDTO> result = iccsComponentManageSv.listSubPathAndData(ccsComponentOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("list sub path Success");
            dto.setData(result);
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
	}

    @Override
    public String exists(String param) {
        Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
            CCSComponentOperationParam ccsComponentOperationParam = gson.fromJson(param, CCSComponentOperationParam.class);

            boolean data = iccsComponentManageSv.exists(ccsComponentOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("list sub path Success");
            dto.setData(data);
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

	@Override
	public String insertZK(String param) {
		Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
        	CcsResourcePool ccsResourcePool = gson.fromJson(param, CcsResourcePool.class);
            iccsComponentManageSv.insertZK(ccsResourcePool);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("list sub path Success");
        } catch (Throwable e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
	}

	@Override
	public String initUser(String param) {
		Gson gson = new Gson();
        CCSResultDTO dto = new CCSResultDTO();
        try {
        	CcsResourcePool ccsResourcePool = iccsComponentManageSv.initUser(1);
        	String configAddr = ccsResourcePool.getZkAddress();
        	String adminName = ccsResourcePool.getSuperAuthName();
        	String adminPwd = CiperUtil.decrypt("1@3^$aGH;._|$!@#", ccsResourcePool.getSuperAuthPassword());
        	UserModel user = gson.fromJson(param, UserModel.class);
        	String userName = user.getUserName();
        	String userPwd = user.getUserPwd();
        	ZKClient client = ZKPoolFactory.getZKPool(configAddr, adminName, adminPwd).getZkClient(configAddr, adminName);
        	if(!client.exists(ConfigCenterUtil.appendUserReadOnlyPathPath(userName))){
            	client.createNode(ConfigCenterUtil.appendUserReadOnlyPathPath(userName), ConfigCenterUtil.createReadOnlyACL(userName,
                        String.valueOf(userPwd), ccsResourcePool), "", CreateMode.PERSISTENT);

                client.createNode(ConfigCenterUtil.appendUserWritablePathPath(userName), ConfigCenterUtil.createWritableACL(userName,
                        String.valueOf(userPwd)), "", CreateMode.PERSISTENT);
        		dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
        		dto.setResultMessage("init user Success");
        	}else{  
                dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
           		dto.setResultMessage("用户节点已经存在");
        	}            
        } catch (Throwable e) {
        	e.printStackTrace();
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage("init user failed");
        }
        return new Gson().toJson(dto);
	}
}
