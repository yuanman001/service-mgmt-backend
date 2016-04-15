package com.ai.paas.ipaas.ccs.service.impl;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.BundleKeyConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterConstants;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsResourcePoolMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsServiceUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.interfaces.CcsUserConfigMapper;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.*;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsServiceUserConfigCriteria.Criteria;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.ccs.service.dto.ConfigServiceInfo;
import com.ai.paas.ipaas.ccs.service.dto.CreateServiceInfo;
import com.ai.paas.ipaas.ccs.service.util.ConfigCenterUtil;
import com.ai.paas.ipaas.ccs.service.util.ZookeeperClientUtil;
import com.ai.paas.ipaas.ccs.zookeeper.ZKClient;
import com.ai.paas.ipaas.util.Assert;
import com.ai.paas.ipaas.util.CiperUtil;
import com.ai.paas.ipaas.util.ResourceUtil;
import com.ai.paas.ipaas.util.UUIDTool;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.ACL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigCenterServiceManageSvImpl implements IConfigCenterServiceManageSv {
	
	private Logger logger = Logger.getLogger(ConfigCenterServiceManageSvImpl.class);

    @Override
    public ConfigInfo createUserNode(String userId) throws PaasException {
        ConfigInfo result = new ConfigInfo();
        try {
            //选择随机的Zookeeper机器，并创建用户节点
            CcsResourcePool pool = selectRandomZkMachine(userId, ConfigCenterDubboConstants.ZKTypeCode.INNER);
            String passwd = createUserNodeToZk(userId, pool);

            //沉淀用户信息
            insertUserZkData(userId, pool, passwd);

            // 拼接返回结果
            result.setConfigUser(userId);
            result.setConfigPwd(CiperUtil.encrypt(ConfigCenterConstants.operators, String.valueOf(passwd)));
            result.setResultCode("000000");
            result.setConfigAddr(pool.getZkAddress());
        } catch (Exception e) {
        	logger.error("createUserNode",e);
            throw new PaasException("创建用户节点失败", e);
        }
        return result;
    }

    @Override
    public ConfigInfo getConfigInfo(String userId) throws PaasException {
        CcsUserConfig config = selectUserConfigByUserId(userId);
        ConfigInfo configInfo = new ConfigInfo();

        if (config == null) {
            configInfo.setResultCode("000000");
            return configInfo;
        }

        configInfo.setConfigAddr(config.getZkAddress());
        configInfo.setConfigUser(config.getZkUserName());
        configInfo.setConfigPwd(config.getZkPassword());
        return configInfo;
    }

    private CcsUserConfig selectUserConfigByUserId(String userId) {
        CcsUserConfigMapper configMapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfigCriteria ccsUserConfigCriteria = new CcsUserConfigCriteria();
        ccsUserConfigCriteria.createCriteria().andUserIdEqualTo(userId);
        List<CcsUserConfig> configs = configMapper.selectByExample(ccsUserConfigCriteria);
        if (configs.size() < 1) {
            return null;
        }
        return configs.get(0);
    }

    @Override
    public ConfigServiceInfo createService(CreateServiceInfo createServiceInfo) throws PaasException {
        createServiceInfo.validate();
        if (!"create".equals(createServiceInfo.getApplyType())) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.APPLY_TYPE_ERROR));
        }

        CcsUserConfig ccsUserConfig = selectUserConfigByUserId(createServiceInfo.getUserId());
        Assert.notNull(ccsUserConfig, ResourceUtil.getMessage(BundleKeyConstants.USER_CONFIG_NOT_FOUND,
                createServiceInfo.getUserId()));
        CcsResourcePool pool = selectRandomZkMachine(createServiceInfo.getUserId(), ConfigCenterDubboConstants.ZKTypeCode.CUSTOM);
        String passwd = String.valueOf(UUIDTool.genShortId());

        try {
            createUserServiceRootPath(createServiceInfo.getUserId(), pool, passwd, createServiceInfo.getServiceId());
        } catch (Exception e) {
        	logger.error("createUserServiceRootPath:"+e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CREATE_USER_NODE_ERROR), e);
        }

        StringBuffer data = appendConfigServiceData(createServiceInfo, pool, passwd);

        CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePool resourcePool = mapper.selectByPrimaryKey(ccsUserConfig.getCcsResourceId());

        try {
            ZKClient client = ZookeeperClientUtil.getZkClientFromPool(ccsUserConfig.getZkAddress(), resourcePool.getSuperAuthName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, resourcePool.getSuperAuthPassword()));
            List<ACL> acls = ConfigCenterUtil.createReadOnlyACL(ccsUserConfig.getZkUserName(), CiperUtil.decrypt(ConfigCenterConstants.operators, ccsUserConfig.getZkPassword()), resourcePool);
            if(!client.exists(appendCCSServicePath(createServiceInfo)))
            	client.createNode(appendCCSServicePath(createServiceInfo), acls, data.toString(), CreateMode.PERSISTENT);
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CREATE_CONFIG_SERVICE_ERROR), e);
        }

        insertConfigzkData(createServiceInfo, pool, passwd);

        ConfigServiceInfo configServiceInfo = new ConfigServiceInfo();
        configServiceInfo.setResultCode("000000");
        configServiceInfo.setServiceId(createServiceInfo.getServiceId());
        configServiceInfo.setResultMsg(ResourceUtil.getMessage(BundleKeyConstants.APPLY_SERVICE_SUCCESS));
        return configServiceInfo;
    }
    
    
    @Override
	public ConfigServiceInfo deleteService(CreateServiceInfo createServiceInfo)
			throws PaasException {
    	createServiceInfo.validate();
        if (!"cancel".equals(createServiceInfo.getApplyType())) {
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.APPLY_TYPE_ERROR));
        }
        CcsUserConfig ccsUserConfig = selectUserConfigByUserId(createServiceInfo.getUserId());
        Assert.notNull(ccsUserConfig, ResourceUtil.getMessage(BundleKeyConstants.USER_CONFIG_NOT_FOUND,
                createServiceInfo.getUserId()));
        CcsResourcePool pool = selectRandomZkMachine(createServiceInfo.getUserId(), ConfigCenterDubboConstants.ZKTypeCode.CUSTOM);
        CcsServiceUserConfig serviceConfig = qryCcsUserConfig(createServiceInfo.getUserId(),createServiceInfo.getServiceId());
        try {
            deleteUserServicePath(pool,serviceConfig );
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CANCEL_USER_NODE_ERROR), e);
        }

        CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePool resourcePool = mapper.selectByPrimaryKey(ccsUserConfig.getCcsResourceId());

        try {
            ZKClient client = ZookeeperClientUtil.getZkClientFromPool(ccsUserConfig.getZkAddress(), resourcePool.getSuperAuthName(),
                    CiperUtil.decrypt(ConfigCenterConstants.operators, resourcePool.getSuperAuthPassword()));
            client.deleteNode(appendCCSServicePath(createServiceInfo));
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CANCEL_CONFIG_SERVICE_ERROR), e);
        }
        
        updateConfigzkData(createServiceInfo);

        ConfigServiceInfo configServiceInfo = new ConfigServiceInfo();
        configServiceInfo.setResultCode("000000");
        configServiceInfo.setServiceId(createServiceInfo.getServiceId());
        configServiceInfo.setResultMsg(ResourceUtil.getMessage(BundleKeyConstants.CANCEL_SERVICE_SUCCESS));
        return configServiceInfo;
	}

	private String appendCCSServicePath(CreateServiceInfo createServiceInfo) {
        return ConfigCenterUtil.appendUserReadOnlyPathPath(createServiceInfo.getUserId()) +
                ConfigCenterConstants.CONFIG_SERVICE_NODE_NAME + ConfigCenterConstants.SEPARATOR
                + createServiceInfo.getServiceId();
    }

    private StringBuffer appendConfigServiceData(CreateServiceInfo createServiceInfo, CcsResourcePool pool, String passwd) {
        //{"zkAddr":"","zkUser":"","zkPwd":"","timOut":10, "serviceId":""}
        StringBuffer data = new StringBuffer();
        data.append("{\"zkAddr\":\"");
        data.append(pool.getZkAddress());
        data.append("\",\"zkUser\":\"");
        data.append(createServiceInfo.getUserId());
        data.append("\",\"zkPwd\":\"");
        data.append(CiperUtil.encrypt(ConfigCenterConstants.operators, passwd));
        data.append("\",\"timOut\":");
        data.append(createServiceInfo.getTimeOut());
        data.append(", \"serviceId\":\"");
        data.append(createServiceInfo.getServiceId());
        data.append("\"}");
        return data;
    }

    private void insertConfigzkData(CreateServiceInfo createServiceInfo, CcsResourcePool pool, String passwd) {
        CcsServiceUserConfigMapper ccsServiceUserConfigMapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfig config = new CcsServiceUserConfig();
        config.setUserId(createServiceInfo.getUserId());
        config.setCcsResourceId(pool.getId());
        config.setServiceId(createServiceInfo.getServiceId());
        config.setZkAddress(pool.getZkAddress());
        config.setZkUserName(createServiceInfo.getUserId());
        config.setZkPassword(CiperUtil.encrypt(ConfigCenterConstants.operators, passwd));
        ccsServiceUserConfigMapper.insert(config);
    }
    
    public void updateConfigzkData(CreateServiceInfo createServiceInfo) throws PaasException {
        CcsServiceUserConfigMapper ccsServiceUserConfigMapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
        CcsServiceUserConfig config = new CcsServiceUserConfig();
        CcsServiceUserConfigCriteria ccsServiceUserConfigCriteria = new CcsServiceUserConfigCriteria();
        Criteria criteria = ccsServiceUserConfigCriteria.createCriteria();
        criteria.andUserIdEqualTo(createServiceInfo.getUserId()).andServiceIdEqualTo(createServiceInfo.getServiceId());
        List<CcsServiceUserConfig> ccsServiceUserConfig = ccsServiceUserConfigMapper.selectByExample(ccsServiceUserConfigCriteria);
        System.out.println("******************"+ccsServiceUserConfig.size());
        System.out.println("######################"+ccsServiceUserConfig);
        if(!ccsServiceUserConfig.isEmpty()&&ccsServiceUserConfig.size()==1){
        	config = ccsServiceUserConfig.get(0);
        	config.setServiceStatus(1);
        	ccsServiceUserConfigMapper.updateByPrimaryKey(config);
        }else{
        	throw new PaasException(ResourceUtil.getMessage(BundleKeyConstants.CANCEL_USER_NODE_ERROR));    
        }   	
    }

    private void insertUserZkData(String userId, CcsResourcePool pool, String passwd) {
        CcsUserConfigMapper configMapper = ServiceUtil.getMapper(CcsUserConfigMapper.class);
        CcsUserConfig config = new CcsUserConfig();
        config.setUpdateTime(new Timestamp(new Date().getTime()));
        config.setUserId(userId);
        config.setZkAddress(pool.getZkAddress());
        config.setZkUserName(userId);
        config.setCcsResourceId(pool.getId());
        config.setZkPassword(CiperUtil.encrypt(ConfigCenterConstants.operators, passwd));
        configMapper.insert(config);
    }
    
    private CcsServiceUserConfig qryCcsUserConfig(String userId, String serviceId) throws PaasException {
    	CcsServiceUserConfigMapper configMapper = ServiceUtil.getMapper(CcsServiceUserConfigMapper.class);
    	CcsServiceUserConfigCriteria criteria = new CcsServiceUserConfigCriteria();
        criteria.createCriteria().andUserIdEqualTo(userId).andServiceIdEqualTo(serviceId);
        List<CcsServiceUserConfig> ccsServiceConfig = configMapper.selectByExample(criteria);
        if(null!=ccsServiceConfig){
        	return ccsServiceConfig.get(0);
        }else{
        	throw new PaasException("用户数据不正确");
        }        
    }
    
    /**
     * 随机选择zookpeer地址
     * @param userId
     * @param type
     * @return
     */
    private CcsResourcePool selectRandomZkMachine(String userId, ConfigCenterDubboConstants.ZKTypeCode type) {
        CcsResourcePoolMapper mapper = ServiceUtil.getMapper(CcsResourcePoolMapper.class);
        CcsResourcePoolCriteria ccsResourcePoolCriteria = new CcsResourcePoolCriteria();
        ccsResourcePoolCriteria.createCriteria().andZkTypeCodeEqualTo(type.getFlag());
        List<CcsResourcePool> pools = mapper.selectByExample(ccsResourcePoolCriteria);
        int result1 = (userId.hashCode() % pools.size());
        return pools.get(result1);
    }
    
    /**
     * 在zookeeper上创建用户节点
     * @param userId
     * @param pool
     * @return
     * @throws Exception
     */
    private String createUserNodeToZk(String userId, CcsResourcePool pool) throws Exception {
        String passwd = String.valueOf(UUIDTool.genShortId());
        createUserRootPath(userId, pool, passwd);
        createUserReadOnlyPath(userId, pool, passwd);
        createUserWriteablePath(userId, pool, passwd);
        return String.valueOf(passwd);
    }
    
    /**
     * 创建用户根节点
     * @param userId
     * @param pool
     * @param pwd
     * @throws Exception
     */
    private void createUserRootPath(String userId, CcsResourcePool pool, String pwd) throws Exception {
        ZKClient client = null;
        client = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), pool.getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword()));
        List<ACL> acls = ConfigCenterUtil.createReadOnlyACL(userId, pwd, pool);
        if(!client.exists(ConfigCenterUtil.appendUserRootPathPath(userId)))
        	client.createNode(ConfigCenterUtil.appendUserRootPathPath(userId), acls, "client", CreateMode.PERSISTENT);
    }

    private void createUserServiceRootPath(String userId, CcsResourcePool pool, String pwd, String serviceId) throws Exception {
        ZKClient client = null;
        client = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), pool.getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword()));
        List<ACL> acls = ConfigCenterUtil.createWritableACL(userId, pwd);
        logger.info("----------------"+userId+":"+pwd);
        if(!client.exists(ConfigCenterUtil.appendUserRootPathPath(userId)))
	        client.createNode(ConfigCenterUtil.appendUserRootPathPath(userId) + ConfigCenterConstants.SEPARATOR +
	                serviceId, acls, "client", CreateMode.PERSISTENT);
    }
    
    private void deleteUserServicePath(CcsResourcePool pool, CcsServiceUserConfig serviceConfig) throws Exception {
        ZKClient client = null;
        String userId = serviceConfig.getUserId();
        String serviceId = serviceConfig.getServiceId();
        String paasword = serviceConfig.getZkPassword();
        client = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), userId,
                CiperUtil.decrypt(ConfigCenterConstants.operators, paasword),serviceId);
        client.deleteNode(ConfigCenterUtil.appendUserRootPathPath(userId) + ConfigCenterConstants.SEPARATOR +serviceId);
    }
    
    /**
     * 创建用户写节点
     * @param userId
     * @param pool
     * @param pwd
     * @throws Exception
     */
    private void createUserWriteablePath(String userId, CcsResourcePool pool, String pwd) throws Exception {
        ZKClient client = null;
        client = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), pool.getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword()));
        List<ACL> acls = ConfigCenterUtil.createWritableACL(userId, pwd);
        if(!client.exists(ConfigCenterUtil.appendUserWritablePathPath(userId)))
        	client.createNode(ConfigCenterUtil.appendUserWritablePathPath(userId), acls, "client", CreateMode.PERSISTENT);
    }
    
    /**
     * 创建用户只读节点
     * @param userId
     * @param pool
     * @param pwd
     * @throws Exception
     */
    private void createUserReadOnlyPath(String userId, CcsResourcePool pool, String pwd) throws Exception {
        ZKClient client = null;
        client = ZookeeperClientUtil.getZkClientFromPool(pool.getZkAddress(), pool.getSuperAuthName(),
                CiperUtil.decrypt(ConfigCenterConstants.operators, pool.getSuperAuthPassword()));
        List<ACL> acls = ConfigCenterUtil.createReadOnlyACL(userId, pwd, pool);
        
        if(!client.exists(ConfigCenterUtil.appendUserReadOnlyPathPath(userId)))
        	client.createNode(ConfigCenterUtil.appendUserReadOnlyPathPath(userId), acls, "client", CreateMode.PERSISTENT);
    }


}
