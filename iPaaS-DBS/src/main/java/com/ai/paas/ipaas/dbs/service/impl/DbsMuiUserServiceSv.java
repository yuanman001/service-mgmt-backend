package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsMuiUserServiceMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserServiceCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsMuiUserServiceSv;
import com.ai.paas.ipaas.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsMuiUserServiceSv implements IDbsMuiUserServiceSv{

	@Override
	public com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService findByPkey(
			Integer serviceId) {
		DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
		DbsMuiUserService instance=new DbsMuiUserService();
		instance.setServiceId(serviceId);
		List<DbsMuiUserService> list=mapper.selectByExample(toCriteria(instance));
		return list.get(0);
	}

	@Override
	public List<com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService> getModels(
			com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService dbsUserService) {
		DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
		List<DbsMuiUserService> list=mapper.selectByExample(toCriteria(dbsUserService));
		return list;
	}

	@Override
	public void addModel(
			com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService dbsUserService) {
		DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
		mapper.insert(dbsUserService);
	}

	@Override
	public void modModel(
			com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService dbsUserService) {
		DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
		mapper.updateByPrimaryKey(dbsUserService);
	}

	@Override
	public void delModel(Integer serviceId) {
		DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
		mapper.deleteByPrimaryKey(serviceId);
	}

	private DbsMuiUserServiceCriteria toCriteria(DbsMuiUserService instance){
		DbsMuiUserServiceCriteria dbsCriteria=new DbsMuiUserServiceCriteria();
		DbsMuiUserServiceCriteria.Criteria criteria=dbsCriteria.createCriteria();
		if(instance.getMuiId()!=null)
		{
			criteria.andMuiIdEqualTo(instance.getMuiId());
		}
		if(instance.getServiceId()!=null)
		{
			criteria.andServiceIdEqualTo(instance.getServiceId());
		}
		if(!StringUtil.isBlank(instance.getStatus()))
		{
			criteria.andStatusEqualTo(instance.getStatus());
		}
		if(!StringUtil.isBlank(instance.getUserId()))
		{
			criteria.andUserIdEqualTo(instance.getUserId());
		}
		return dbsCriteria;
	}
}
