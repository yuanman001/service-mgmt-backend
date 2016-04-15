package com.ai.paas.ipaas.ccs.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.service.dto.CCSOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;

import java.util.List;
import java.util.Map;

/**
 * 用于配置服务的管理的Service层
 */
public interface ICCSManageSv {
    void add(CCSOperationParam param, String data) throws PaasException;

    void add(CCSOperationParam param, byte[] data) throws PaasException;

    boolean exists(CCSOperationParam param) throws PaasException;

    void modify(CCSOperationParam param, String data) throws PaasException;

    void modify(CCSOperationParam param, byte[] data) throws PaasException;

    String get(CCSOperationParam param) throws PaasException;

    byte[] readBytes(CCSOperationParam param) throws PaasException;

    void delete(CCSOperationParam param) throws PaasException;

    List<String> listSubPath(CCSOperationParam param) throws PaasException;
    
    List<CCSSubListDTO> listSubPathAndData(CCSOperationParam param) throws PaasException;
    
    List<String> getServices(CCSOperationParam param) throws PaasException;

	Map<String,String> listAllPath(CCSOperationParam param) throws PaasException;
}
