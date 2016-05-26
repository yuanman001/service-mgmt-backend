package com.ai.paas.ipaas.ccs.manage.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.manage.rest.interfaces.IConfigCenterServiceManager;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSFuncList;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.service.dto.ConfigServiceInfo;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;
import com.ai.paas.ipaas.constants.CommonConstants;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.ai.paas.ipaas.util.StringUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
/**
 * 门户Service
 * @author Fenggw
 *
 */
@Service
public class ConfigCenterServiceManageImpl implements IConfigCenterServiceManager {

	private Logger logger = Logger.getLogger(ConfigCenterServiceManageImpl.class);

    private static final String USER_ID = "userId";

    @Autowired
    private IConfigCenterServiceManageSv configCenterSv;

    @Override
    public String init(String userJson) {
        ConfigInfo configInfo = new ConfigInfo();
        String userId = getUserFromJson(userJson);
        if (StringUtil.isBlank(userId)) {
            configInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            configInfo.setResultDescription(ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
        } else {
            try {
                configInfo = configCenterSv.createUserNode(userId);
            } catch (PaasException e) {
                configInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
                configInfo.setResultDescription(e.getMessage());
            }
        }
        return new Gson().toJson(configInfo);
    }

    private String getUserFromJson(String userJson) {
        Gson gson = new Gson();
        Map<String, String> userJsonMap = gson.fromJson(userJson, Map.class);
        return userJsonMap.get(USER_ID);
    }

    @Override
    public String getFuncList() {  
    	List<CCSFuncList> ccsFuncLists = new ArrayList<CCSFuncList>();
        CCSFuncList ccsFuncList1 = new CCSFuncList();
        ccsFuncList1.setApplyType("create");
        ccsFuncList1.setDescription("create services");
        ccsFuncList1.setUrl(CommonConstants.CCS_URL_HEAD+CommonConstants.CCS_URL_CREATE);
        
        CCSFuncList ccsFuncList2 = new CCSFuncList();
        ccsFuncList2.setApplyType("cancel");
        ccsFuncList2.setDescription("cancel services");
        ccsFuncList2.setUrl(CommonConstants.CCS_URL_HEAD+CommonConstants.CCS_URL_CANCEL);
        
        ccsFuncLists.add(ccsFuncList1);
        ccsFuncLists.add(ccsFuncList2);
        Gson gson = new Gson();
        
        return gson.toJson(ccsFuncLists);
    }

    @Override
    public String create(String s) {
        ConfigServiceInfo configServiceInfo = new ConfigServiceInfo();
        Gson gson = new Gson();
        CreateServiceInfo createServiceInfo = gson.fromJson(s, CreateServiceInfo.class);
        try {
            configServiceInfo = configCenterSv.createService(createServiceInfo);
        } catch (PaasException e) {
        	logger.error(e.getMessage(),e);
            configServiceInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
            configServiceInfo.setResultMsg(e.getMessage());
        }
        return new Gson().toJson(configServiceInfo);
    }

    @Override
    public String cancel(String s) {
    	 ConfigServiceInfo configServiceInfo = new ConfigServiceInfo();
         Gson gson = new Gson();
         CreateServiceInfo createServiceInfo = gson.fromJson(s, CreateServiceInfo.class);
         createServiceInfo.setTimeOut(2000);
         try {
             configServiceInfo = configCenterSv.deleteService(createServiceInfo);
         } catch (PaasException e) {
         	 logger.error(e.getMessage(),e);
             configServiceInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
             configServiceInfo.setResultMsg(e.getMessage());
         }
         return new Gson().toJson(configServiceInfo);
    }

    @Override
    public String modify(String s) {
        return null;
    }

    @Override
    public String start(String s) {
        return null;
    }

    @Override
    public String stop(String s) {
        return null;
    }

    @Override
    public String restart(String s) {
        return null;
    }

	@Override
	public String getConfigInfo(String userJson) {

        ConfigInfo configInfo = new ConfigInfo();
        try {
        	String userId = getUserFromJson(userJson);
        	if (StringUtil.isBlank(userId)) {
        		configInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
        		configInfo.setResultDescription(ResourceUtil.getMessage(BundleKeyConstants.USER_ID_NOT_NULL));
        	} else {
                configInfo = configCenterSv.getConfigInfo(userId);
                configInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
        	}
        } catch (PaasException e) {
        	configInfo.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
        	configInfo.setResultDescription(e.getMessage());
        }
        return new Gson().toJson(configInfo);
	}
}
