package com.ai.paas.ipaas.ccs.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.service.dto.ConfigServiceInfo;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;

public interface IConfigCenterServiceManageSv {
    ConfigInfo createUserNode(String userId) throws PaasException;

    ConfigInfo getConfigInfo(String userId) throws PaasException;

    ConfigServiceInfo createService(CreateServiceInfo createServiceInfo) throws PaasException;
    
    ConfigServiceInfo deleteService(CreateServiceInfo createServiceInfo) throws PaasException;
}
