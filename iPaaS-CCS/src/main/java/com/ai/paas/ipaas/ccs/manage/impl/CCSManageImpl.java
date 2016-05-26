package com.ai.paas.ipaas.ccs.manage.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.ICCSManage;
import com.ai.paas.ipaas.ccs.service.ICCSManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSResultDTO;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.service.impl.CCSManageSvImpl;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
/**
 * Custom Service
 * @author Fenggw
 *
 */
@Service
public class CCSManageImpl implements ICCSManage {

    @Autowired
    private ICCSManageSv iccsManageSv;

    @Override
    public String add(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        Map<String, String> value = gson.fromJson(jsonParam, Map.class);
        String data = value.get("data");
        CCSResultDTO dto = new CCSResultDTO();
        try {
            iccsManageSv.add(ccsOperationParam, data);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("add Success");
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }

        return new Gson().toJson(dto);
    }

    @Override
    public String modify(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        Map<String, String> value = gson.fromJson(jsonParam, Map.class);
        String data = value.get("data");
        CCSResultDTO dto = new CCSResultDTO();
        try {
            iccsManageSv.modify(ccsOperationParam, data);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("modify Success");
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String get(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        CCSResultDTO dto = new CCSResultDTO();
        try {
            String result = iccsManageSv.get(ccsOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("modify Success");
            dto.setData(result);
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

    @Override
    public String delete(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        CCSResultDTO dto = new CCSResultDTO();
        try {
            iccsManageSv.delete(ccsOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("modify Success");
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }
    
    @Override
    public String deleteBatch(String jsonParam) {
        Gson gson = new Gson();
		List<CCSOperationParam> ccsOperationParams = gson.fromJson(jsonParam,  new TypeToken<List<CCSOperationParam>>(){}.getType());
        CCSResultDTO dto = new CCSResultDTO();
        try {
        	for(CCSOperationParam ccsOperationParam : ccsOperationParams){
        		iccsManageSv.delete(ccsOperationParam);
        	}           
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("delete Batch Success");
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }
    
    @Override
    public String addBatch(String jsonParam) {
        Gson gson = new Gson();   
		List<Map<String, String>> values = gson.fromJson(jsonParam, new TypeToken<List<Map<String, String>>>(){}.getType());
		CCSResultDTO dto = new CCSResultDTO();
		try{
			for(Map<String, String> value : values){
	        	String data = value.get("data");
	        	String userId = value.get("userId");
	        	String path = value.get("path");
	        	String serviceId = value.get("serviceId");
	        	CCSOperationParam ccsOperationParam = new CCSOperationParam();
	        	ccsOperationParam.setPath(path);
	        	ccsOperationParam.setServiceId(serviceId);
	        	ccsOperationParam.setUserId(userId);
	        	iccsManageSv.add(ccsOperationParam, data);      			
	        }
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("add Batch Success");
		}catch(PaasException e){
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
		}		        
        return new Gson().toJson(dto);
    }

    @Override
    public String listSubPath(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        CCSResultDTO dto = new CCSResultDTO();
        try {
            List<String> result = iccsManageSv.listSubPath(ccsOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("listSubPath Success");
            dto.setData(result);
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }
    
    @Override
    public String listAllPath(String rootPath){
    	 Gson gson = new Gson();
         CCSOperationParam ccsOperationParam = gson.fromJson(rootPath, CCSOperationParam.class);
         CCSResultDTO dto = new CCSResultDTO();
         try {
        	 CCSManageSvImpl.result = new HashMap<String, String>();
             Map<String, String> result = iccsManageSv.listAllPath(ccsOperationParam);
             dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
             dto.setResultMessage("listSubPath Success");
             dto.setData(result);
         } catch (PaasException e) {
             dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
             dto.setResultMessage(e.getMessage());
         }
         return new Gson().toJson(dto);
    }
    
    @Override
    public String listSubPathAndData(String jsonParam) {
        Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        CCSResultDTO dto = new CCSResultDTO();
        try {
            List<CCSSubListDTO> result = iccsManageSv.listSubPathAndData(ccsOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("listSubPath Success");
            dto.setData(result);
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
    }

	@Override
	public String getService(String jsonParam) {
		Gson gson = new Gson();
        CCSOperationParam ccsOperationParam = gson.fromJson(jsonParam, CCSOperationParam.class);
        CCSResultDTO dto = new CCSResultDTO();
        try {
            List<String> result = iccsManageSv.getServices(ccsOperationParam);
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
            dto.setResultMessage("getServices Success");
            dto.setData(result);
        } catch (PaasException e) {
            dto.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            dto.setResultMessage(e.getMessage());
        }
        return new Gson().toJson(dto);
	}
       
}
