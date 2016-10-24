package com.ai.paas.ipaas.common.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.common.dao.interfaces.OrgnizeUserInfoMapper;
import com.ai.paas.ipaas.common.dao.mapper.bo.OrgnizeUserInfo;
import com.ai.paas.ipaas.common.dao.mapper.bo.OrgnizeUserInfoCriteria;
import com.ai.paas.ipaas.common.service.IOrgnizeUserHelper;
import com.alibaba.dubbo.config.annotation.Service;

@Service
@Transactional
public class OrgnizeUserHelper implements IOrgnizeUserHelper{
	public OrgnizeUserInfo getOrgnizeInfo(String userId) throws PaasException {
		OrgnizeUserInfoMapper mapper = ServiceUtil.getMapper(OrgnizeUserInfoMapper.class);
		OrgnizeUserInfoCriteria condition = new OrgnizeUserInfoCriteria();
		condition.createCriteria().andUserIdEqualTo(userId);
		List<OrgnizeUserInfo> beans = mapper.selectByExample(condition);
		if(beans.size()!=1){ 
			new PaasException("get orgnize info error!");
		}
		
		return beans.get(0);
	}
}
