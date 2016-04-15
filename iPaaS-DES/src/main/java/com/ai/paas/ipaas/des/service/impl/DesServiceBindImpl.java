package com.ai.paas.ipaas.des.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.des.dao.interfaces.DesServiceBindMapper;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBind;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindCriteria;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindCriteria.Criteria;
import com.ai.paas.ipaas.des.manage.model.DesServiceBindParam;
import com.ai.paas.ipaas.des.service.IDesServiceBind;
import com.ai.paas.ipaas.util.Assert;

@Service
@Transactional(rollbackFor = Exception.class)
public class DesServiceBindImpl implements IDesServiceBind {
	private static final String SERVICEID = "serviceId";
	private static final String USERID = "userId";

	@Override
	public void bind(DesServiceBindParam desServiceBindParam, String mdsTopic, int mdsPartition) {
		Assert.notNull(desServiceBindParam.getServiceId(), SERVICEID);
		Assert.notNull(desServiceBindParam.getUserId(), USERID);
		Assert.notNull(desServiceBindParam.getDbsServiceId(), "dbsServiceId");
		Assert.notNull(desServiceBindParam.getMdsServiceId(), "mdsServiceId");
		Assert.notNull(desServiceBindParam.getMdsServicePassword(), "mdsServicePassword");
		Assert.notNull(mdsTopic, "mdsTopic");
		Assert.notNull(mdsPartition, "mdsPartition");

		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBind desServiceBind = new DesServiceBind();
		desServiceBind.setServiceId(desServiceBindParam.getServiceId());
		desServiceBind.setDbsServiceId(desServiceBindParam.getDbsServiceId());
		desServiceBind.setDbsServicePassword(desServiceBindParam.getDbsServicePassword());
		desServiceBind.setMdsServiceId(desServiceBindParam.getMdsServiceId());
		desServiceBind.setMdsServicePassword(desServiceBindParam.getMdsServicePassword());
		desServiceBind.setUserId(desServiceBindParam.getUserId());
		desServiceBind.setUserName(desServiceBindParam.getUserName());
		desServiceBind.setMdsPartition(mdsPartition);
		desServiceBind.setMdsTopic(mdsTopic);
		mapper.insert(desServiceBind);
	}

	@Override
	public void unbind(String userId, String serviceId) {
		Assert.notNull(serviceId, SERVICEID);
		Assert.notNull(userId, USERID);
		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBindCriteria desServiceBindCriteria = new DesServiceBindCriteria();
		Criteria criteria = desServiceBindCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		mapper.deleteByExample(desServiceBindCriteria);
	}

	@Override
	public List<DesServiceBind> getBound(String userId) {
		Assert.notNull(userId, USERID);
		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBindCriteria desServiceBindCriteria = new DesServiceBindCriteria();
		Criteria criteria = desServiceBindCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		return mapper.selectByExample(desServiceBindCriteria);
	}

	@Override
	public DesServiceBind getBound(String userId, String serviceId) {
		Assert.notNull(userId, USERID);
		Assert.notNull(serviceId, SERVICEID);
		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBindCriteria desServiceBindCriteria = new DesServiceBindCriteria();
		Criteria criteria = desServiceBindCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		List<DesServiceBind> list = mapper.selectByExample(desServiceBindCriteria);
		if (CollectionUtils.isNotEmpty(list))
			return list.get(0);
		else
			return null;
	}

	@Override
	public boolean checkDbsServiceBound(String userId, String serviceId) {
		Assert.notNull(serviceId, SERVICEID);
		Assert.notNull(userId, USERID);
		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBindCriteria desServiceBindCriteria = new DesServiceBindCriteria();
		Criteria criteria = desServiceBindCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andDbsServiceIdEqualTo(serviceId);
		List<DesServiceBind> list = mapper.selectByExample(desServiceBindCriteria);
		if (CollectionUtils.isNotEmpty(list))
			return true;
		else
			return false;
	}

	@Override
	public boolean checkMdsServiceBound(String userId, String serviceId) {
		Assert.notNull(serviceId, SERVICEID);
		Assert.notNull(userId, USERID);
		DesServiceBindMapper mapper = ServiceUtil.getMapper(DesServiceBindMapper.class);
		DesServiceBindCriteria desServiceBindCriteria = new DesServiceBindCriteria();
		Criteria criteria = desServiceBindCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andMdsServiceIdEqualTo(serviceId);
		List<DesServiceBind> list = mapper.selectByExample(desServiceBindCriteria);
		if (CollectionUtils.isNotEmpty(list))
			return true;
		else
			return false;
	}

}
