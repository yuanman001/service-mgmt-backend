package com.ai.paas.ipaas.ccs.service.impl;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsServiceUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsServiceUserConfig;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsServiceUserConfigCriteria;
import com.ai.paas.ipaas.ccs.service.ICCSManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.CCSSubListDTO;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.service.util.ZookeeperClientUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.ai.paas.ipaas.util.StringUtil;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by astraea on 2015/5/1.
 */
@Service
@Transactional
public class CCSManageSvImpl implements ICCSManageSv {
	
	private Logger logger = Logger.getLogger(CCSManageSvImpl.class);
	
	public static Map<String,String> result = new HashMap<String,String>();

    @Override
    public void add(CCSOperationParam param, String data) throws PaasException {
        byte[] value = null;
        if (!StringUtil.isBlank(data)) {
            try {
                value = data.getBytes(PaaSConstant.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
            	logger.error(e.getMessage(),e);
                throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
            }
        }
        add(param, value);
    }

    @Override
    public void add(CCSOperationParam param, byte[] data) throws PaasException {
        param.validate();
        if (exists(param)) {
            modify(param, data);
            return;
        }

        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            String path = appendCustomUserRootNode(config.getUserId(), config.getServiceId()) + param.getPath();
            if(!client.exists(path))
	            client.createNode(path,ConfigCenterUtil.createWritableACL(config.getUserId(), 
	            		CiperUtil.decrypt(ConfigCenterConstants.operators,
	                            config.getZkPassword())), data, CreateMode.PERSISTENT);
            
            logger.debug(config.getUserId());
            logger.debug(config.getZkPassword());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    @Override
    public boolean exists(CCSOperationParam param) throws PaasException {
        param.validate();
        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            return client.exists(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                    + param.getPath());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    private CcsServiceUserConfig getCcsServiceUserConfig(String userId, String serviceId) throws PaasException {
        CcsServiceUserConfigMapper mapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfigCriteria configCriteria = new CcsServiceUserConfigCriteria();
        configCriteria.createCriteria().andUserIdEqualTo(userId).andServiceIdEqualTo(serviceId);
        List<CcsServiceUserConfig> serviceUserConfigs = mapper.selectByExample(configCriteria);

        if (serviceUserConfigs.size() < 1) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.USER_CONFIG_NOT_FOUND, userId));
        }

        return serviceUserConfigs.get(0);
    }

    @Override
    public void modify(CCSOperationParam param, String data) throws PaasException {
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
    public void modify(CCSOperationParam param, byte[] data) throws PaasException {
        param.validate();
        if (!exists(param))
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            client.setNodeData(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                    + param.getPath(), data);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    @Override
    public String get(CCSOperationParam param) throws PaasException {
        try {
            return new String(readBytes(param), PaaSConstant.CHARSET_UTF8);
        } catch (UnsupportedEncodingException e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONVERT_DATA_FAILED), e);
        }
    }

    @Override
    public byte[] readBytes(CCSOperationParam param) throws PaasException {
        param.validate();
        if (!exists(param))
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath()));
        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            return client.getNodeBytes(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                    + param.getPath());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    @Override
    public void delete(CCSOperationParam param) throws PaasException {
        param.validate();
        if (!exists(param))
            return;

        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            client.deleteNode(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                    + param.getPath());
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }

    @Override
    public List<String> listSubPath(CCSOperationParam param) throws PaasException {
        if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
        	param.validate();      
	        if (!exists(param))
	            throw new PaasException(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath());
        }
        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
            if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
            	return client.getChildren(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                    + param.getPath());
            }else{
            	return client.getChildren(appendCustomUserRootNode(param.getUserId(), param.getServiceId()));
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }
    
    @Override
    public Map<String,String> listAllPath(CCSOperationParam param) throws PaasException {
        if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
        	param.validate();      
	        if (!exists(param))
	            throw new PaasException(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath());
        }
        CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());

        ZKClient client = null;
        List<String> children = null;
        try {
            client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
			if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())) {
				children = formatPath(
						client.getChildren(appendCustomUserRootNode(
								param.getUserId(), param.getServiceId())
								+ param.getPath()), param);
			} else {
				children = formatPath(
						client.getChildren(appendCustomUserRootNode(
								param.getUserId(), param.getServiceId())),
						param);
			}        
            if(!children.isEmpty()){
            	for(String str : children){
            		String data = client.getNodeData(appendCustomUserRootNode(param.getUserId(), param.getServiceId())+str);
            		if(!StringUtil.isBlank(data)){
            			result.put(str,client.getNodeData(appendCustomUserRootNode(param.getUserId(), param.getServiceId())+str));
            		}
					param.setPath(str);
					listAllPath(param);
				}
            }
            return result;
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
        }
    }
    
    public List<String> formatPath(List<String> children,CCSOperationParam param){
    	List<String> paths = new ArrayList<String>();
    	for(String child : children){
    		if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
    			paths.add(param.getPath()+"/"+child);
    		}else{
    			paths.add(param.getPath()+child);
    		}
    	}
    	return paths;
    }
       
    @Override
	public List<CCSSubListDTO> listSubPathAndData(CCSOperationParam param)throws PaasException {
    	if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
        	param.validate();      
	        if (!exists(param))
	            throw new PaasException(BundleKeyConstants.PATH_NOT_EXISTS, param.getPath());
        }
    	
    	 System.out.println("================="+param.getUserId());
    	 System.out.println("================="+param.getServiceId());
    	 System.out.println("================="+param.getPath());
    	 
    	
    	 CcsServiceUserConfig config = getCcsServiceUserConfig(param.getUserId(), param.getServiceId());
         ZKClient client = null;
         List<String> paths =  new ArrayList<String>();
         List<CCSSubListDTO> result = new ArrayList<CCSSubListDTO>();
         try {
             client = ZookeeperClientUtil.getZkClientFromPool(config.getZkAddress(), config.getZkUserName(),
                     CiperUtil.decrypt(ConfigCenterConstants.operators, config.getZkPassword()),config.getServiceId());
             if (!PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
            	 paths =  client.getChildren(appendCustomUserRootNode(param.getUserId(), param.getServiceId())
                     + param.getPath());
             }else{
            	 paths =  client.getChildren(appendCustomUserRootNode(param.getUserId(), param.getServiceId()));
             }
             for(String path : paths){ 
             	CCSSubListDTO subListInfo = new CCSSubListDTO();
             	String data = "";
             	if(PaaSConstant.UNIX_SEPERATOR.equals(param.getPath())){
             		data = client.getNodeData(appendCustomUserRootNode(param.getUserId(), param.getServiceId())+param.getPath()+path);
             	}else{
             		data = client.getNodeData(appendCustomUserRootNode(param.getUserId(), param.getServiceId())+param.getPath()+"/"+path);
             	}             
             	subListInfo.setPath(path);
             	subListInfo.setData(data==null?"":data);
             	result.add(subListInfo);
         	}
             return result;
         } catch (Exception e) {
        	 logger.error(e.getMessage(),e);
             throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CONFIG_ERROR), e);
         }  
	}
     
	@Override
	public List<String> getServices(CCSOperationParam param) throws PaasException {
		CcsServiceUserConfigMapper mapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfigCriteria configCriteria = new CcsServiceUserConfigCriteria();
        configCriteria.createCriteria().andUserIdEqualTo(param.getUserId());
        List<CcsServiceUserConfig> serviceUserConfigs = mapper.selectByExample(configCriteria);
        List<String> result = new ArrayList<String>();
        if (serviceUserConfigs.size() < 1) {
            return result;
        }
        for(CcsServiceUserConfig serviceConfig : serviceUserConfigs){
        	result.add(serviceConfig.getServiceId());
        }
        return result;
	}

	private String appendCustomUserRootNode(String userId, String serviceId) {
        return ConfigCenterConstants.UserNodePrefix.FOR_PAAS_PLATFORM_PREFIX + ConfigCenterConstants.SEPARATOR + userId
                + ConfigCenterConstants.SEPARATOR + serviceId;
    }

	public Map<String, String> getResult() {
		return result;
	}

	public void setResult(Map<String, String> result) {
		this.result = result;
	}	
}
