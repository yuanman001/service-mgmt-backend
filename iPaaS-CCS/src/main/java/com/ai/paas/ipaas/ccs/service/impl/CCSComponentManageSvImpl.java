package com.ai.paas.ipaas.ccs.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.log4j.Logger;
import org.apache.zookeeper.data.ACL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.AddMode;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePoolCriteria;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfig;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfigCriteria;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.service.util.ZookeeperClientUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ConfigWatcher;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.ai.paas.ipaas.util.StringUtil;

@Service
@Transactional
public class CCSComponentManageSvImpl implements ICCSComponentManageSv {

    @SuppressWarnings("unused")
	private ZKClient zkClient;
    
    private Logger logger = Logger.getLogger(CCSComponentManageSvImpl.class);

    public void add(CCSComponentOperationParam param, String data) throws PaasException {
        byte[] value = null;
        if (!StringUtil.isBlank(data)) {
            try {
                value = data.getBytes(PaaSConstant.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
            	logger.error(e.getMessage(),e);
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
            }
        }
        add(param, value, AddMode.PERSISTENT);

    }

    @Override
    public void add(CCSComponentOperationParam param, byte[] data) throws PaasException {
        add(param, data, AddMode.PERSISTENT);
    }

    @Override
    public void add(CCSComponentOperationParam param, String data, AddMode addMode) throws PaasException {
        byte[] value = null;
        if (!StringUtil.isBlank(data)) {
            try {
                value = data.getBytes(PaaSConstant.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
            	logger.error(e.getMessage(),e);
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
            }
        }
        add(param, value, addMode);
    }

    @Override
    public void add(CCSComponentOperationParam param, byte[] data, AddMode addMode) throws PaasException {
        if (exists(param)) {
            modify(param, data);
            return;
        }

        String path = ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(), param.getPathType(),
                param.getPath());
        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        List<ACL> acls = createRightACL(param.getPathType(), config);
        ZKClient client = getRightACLClient(param.getPathType(), config);
        try {
        	if(!client.exists(path))
        		client.createNode(path, acls, data, AddMode.convertMode(addMode.getFlag()));
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.ADD_CONFIG_FAILED), e);
        } finally {
            this.zkClient = client;
        }
    }


    @Override
    public boolean exists(CCSComponentOperationParam param) throws PaasException {
        param.validate();

        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
            return client.exists(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(), param.getPathType(),
                    param.getPath()));
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    @Override
    public void modify(CCSComponentOperationParam param, String data) throws PaasException {
        byte[] value = null;
        if (!StringUtil.isBlank(data)) {
            try {
                value = data.getBytes(PaaSConstant.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
            	logger.error(e.getMessage(),e);
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
            }
        }
        modify(param, value);
    }

    @Override
    public void modify(CCSComponentOperationParam param, byte[] data) throws PaasException {
        if (!exists(param)) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
        }

        param.validate();
        String path = ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(), param.getPathType(),
                param.getPath());
        CcsUserConfig config = getUserConfigInfo(param.getUserId());

        ZKClient client = getRightACLClient(param.getPathType(), config);

        try {
            client.setNodeData(path, data);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.MODIFY_CONFIG_FAILED), e);
        }
    }

    @Override
    public String get(CCSComponentOperationParam param) throws PaasException {
        try {
            return new String(readBytes(param, null), PaaSConstant.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
        }
    }

    @Override
    public String get(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException {
        try {
            return new String(readBytes(param, watcher), PaaSConstant.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
        }
    }

    @Override
    public byte[] readBytes(CCSComponentOperationParam param) throws PaasException {
        return readBytes(param, null);
    }

    @Override
    public byte[] readBytes(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException {
        if (!exists(param)) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
        }

        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
            return client.getNodeBytes(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                    param.getPathType(), param.getPath()), watcher);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.GET_CONFIG_FAILED), e);
        }
    }

    @Override
    public void delete(CCSComponentOperationParam param) throws PaasException {
        if (!exists(param))
            return;

        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        ZKClient client = getRightACLClient(param.getPathType(), config);
        try {
            client.deleteNode(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(), param.getPathType(), param.getPath()));
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.DELETE_CONFIG_FAILED), e);
        }
    }


    @Override
    public List<String> listSubPath(CCSComponentOperationParam param) throws PaasException {
        return listSubPath(param, null);
    }

    @Override
    public List<String> listSubPath(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException {
    	if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
	    	if (!exists(param)) {
	            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
	        }
    	}
        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        ZKClient client = null; 
        List<String> paths =  new ArrayList<String>();
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
            if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
            	 paths =  client.getChildren(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                      param.getPathType(), param.getPath()), watcher);
            }else{
            	 paths =  client.getChildren(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                        param.getPathType()), watcher);
            }
            return paths;
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.LIST_SUB_CONFIG_FAILED), e);
        }
    }
    
    @Override
    public List<CCSSubListDTO> listSubPathAndData(CCSComponentOperationParam param) throws PaasException {
        return listSubPathAndData(param, null);
    }

    @Override
    public List<CCSSubListDTO> listSubPathAndData(CCSComponentOperationParam param, ConfigWatcher watcher) throws PaasException {
    	if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
	    	if (!exists(param)) {
	            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
	        }
    	}
        CcsUserConfig config = getUserConfigInfo(param.getUserId());
        ZKClient client = null; 
        List<String> paths =  new ArrayList<String>();
        List<CCSSubListDTO> result = new ArrayList<CCSSubListDTO>();
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
            if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
            	 paths =  client.getChildren(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                      param.getPathType(), param.getPath()), watcher);
            }else{
            	 paths =  client.getChildren(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                        param.getPathType()), watcher);
            }
            for(String path : paths){ 
            	CCSSubListDTO subListInfo = new CCSSubListDTO();
            	String data = "";
            	if(PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
               	 	data = client.getNodeData(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                         param.getPathType(), param.getPath())+path); 
            	}else{
               	 	data = client.getNodeData(ConfigCenterDubboConstants.PathType.convertPath(param.getUserId(),
                         param.getPathType(), param.getPath())+"/"+path); 
            	}        		
            	subListInfo.setPath(path);
            	subListInfo.setData(data==null?"":data);
            	result.add(subListInfo);
        	}
            return result;
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.LIST_SUB_CONFIG_FAILED), e);
        }
    }

    private List<ACL> createRightACL(ConfigCenterDubboConstants.PathType pathType, CcsUserConfig config) throws PaasException {
        List<ACL> acls = null;
        if (ConfigCenterDubboConstants.PathType.READONLY.equals(pathType)) {
            CcsResourcePool pool = selectSuperUserInfo(config.getCcsResourceId());
            try {
                acls = ConfigCenterUtil.createReadOnlyACL(config.getZkUserName(), CiperUtil.decrypt(
                        ConfigCenterConstants.operators, config.getZkPassword()), pool);
            } catch (NoSuchAlgorithmException e) {
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CREATE_ACL_FAILED), e);
            }

        } else {
            try {
                acls = ConfigCenterUtil.createWritableACL(config.getZkUserName(), CiperUtil.decrypt(
                        ConfigCenterConstants.operators, config.getZkPassword()));
            } catch (NoSuchAlgorithmException e) {
            	logger.error(e.getMessage(),e);
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CREATE_ACL_FAILED), e);
            }
        }
        return acls;
    }

    private ZKClient getRightACLClient(ConfigCenterDubboConstants.PathType pathType, CcsUserConfig config) throws PaasException {
        if (ConfigCenterDubboConstants.PathType.READONLY.equals(pathType)) {
            CcsResourcePool pool = selectSuperUserInfo(config.getCcsResourceId());
            return ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), pool.getSuperAuthName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword()));
        } else {
            return ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
        }
    }

    private CcsResourcePool selectSuperUserInfo(int ccsResourceId) {
        CcsResourcePoolMapper poolMapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        return poolMapper.selectByPrimaryKey(ccsResourceId);
    }

    private CcsUserConfig getUserConfigInfo(String userId) throws PaasException {
        CcsUserConfigMapper configMapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        ccsUserConfigCriteria.createCriteria().andUserIdEqualTo(userId);
        List<CcsUserConfig> configs = configMapper.selectByExample(ccsUserConfigCriteria);
        if (configs.size() <= 0 || configs.size() > 1) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.USER_CONFIG_NOT_FOUND));
        }
        return configs.get(0);
    }
    
    @Override
    public InterProcessLock createlock(CCSComponentOperationParam param) throws PaasException{
    	  try {
			if (!exists(param)){
				 throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
			}
			CcsUserConfig config = getUserConfigInfo(param.getUserId());
	        ZKClient client = getRightACLClient(param.getPathType(), config);
	        client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                      CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()));
	        return client.getInterProcessLock(param.getPath());
    	  } catch (PaasException e) {
    		  logger.error(e.getMessage(),e);
			throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CREAT_INTERPROCESSLOCK_FAILED), e);
    	  }
    }

	@Override
	public void insertZK(CcsResourcePool param) throws PaasException {
		CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePool record = new CcsResourcePool();
        record.setSuperAuthName(param.getSuperAuthName());
        record.setSuperAuthPassword(CiperUtil.encrypt("1@3^$aGH;._|$!@#", param.getSuperAuthPassword()));
        record.setZkAddress(param.getZkAddress());
        record.setZkDescription(param.getZkDescription());
        record.setZkTypeCode(param.getZkTypeCode());
        try{
            mapper.insert(record);
        }catch(Exception e){
        	logger.error(e.getMessage(),e);
        }
	}

	@Override
	public CcsResourcePool initUser(int type) {
		CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
		CcsResourcePoolCriteria poolCriteria = new CcsResourcePoolCriteria();
		poolCriteria.createCriteria().andZkTypeCodeEqualTo(type);
        List<CcsResourcePool> resourcePools = mapper.selectByExample(poolCriteria);
        return resourcePools.get(0);
	}
	
}
