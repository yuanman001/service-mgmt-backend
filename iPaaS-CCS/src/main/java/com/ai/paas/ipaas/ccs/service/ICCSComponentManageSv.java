package com.ai.paas.ipaas.ccs.service;

import java.util.List;

import org.apache.curator.framework.recipes.locks.InterProcessLock;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.AddMode;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher;

/**
 * 用于内部管理配置的Service层
 */
public interface ICCSComponentManageSv{

    void add(CCSComponentOperationParam param, String data) throws PaasException;

    void add(CCSComponentOperationParam param, byte[] data) throws PaasException;

    void add(CCSComponentOperationParam param, String data, AddMode addMode) throws PaasException;

    void add(CCSComponentOperationParam param, byte[] data, AddMode addMode) throws PaasException;

    boolean exists(CCSComponentOperationParam param) throws PaasException;

    void modify(CCSComponentOperationParam param, String data) throws PaasException;

    void modify(CCSComponentOperationParam param, byte[] data) throws PaasException;

    String get(CCSComponentOperationParam param) throws PaasException;

    String get(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException;

    byte[] readBytes(CCSComponentOperationParam param) throws PaasException;

    byte[] readBytes(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException;

    void delete(CCSComponentOperationParam param) throws PaasException;
    
    List<String> listSubPath(CCSComponentOperationParam param) throws PaasException;

    List<String> listSubPath(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException;

    List<CCSSubListDTO> listSubPathAndData(CCSComponentOperationParam param) throws PaasException;

    List<CCSSubListDTO> listSubPathAndData(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException;

	InterProcessLock createlock(CCSComponentOperationParam param) throws PaasException;
	
	void insertZK(CcsResourcePool param) throws PaasException;

	CcsResourcePool initUser(int type);
}
