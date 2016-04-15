package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsUserServiceMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserServiceCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsUserServiceSv;
import com.ai.paas.ipaas.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsUserServiceSv implements IDbsUserServiceSv{

	
	@Override
	public DbsUserService findByPkey(Integer usedId) {
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		DbsUserServiceCriteria dbscriteria=new DbsUserServiceCriteria();
		DbsUserServiceCriteria.Criteria criteria=dbscriteria.createCriteria();
		criteria.andUsedIdEqualTo(usedId.intValue());
		List<DbsUserService> list=mapper.selectByExample(dbscriteria);
		return list.get(0);
	}

	@Override
	public List<DbsUserService> getModels(
			DbsUserService dbsUserService) {
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		DbsUserServiceCriteria dbscriteria=DbsUserServiceSv.toCriteria(dbsUserService);
		List<DbsUserService> list=mapper.selectByExample(dbscriteria);
		return list;
	}

	@Override
	public void addModel(DbsUserService dbsUserService) {
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		mapper.insert(dbsUserService);
		
	}

	@Override
	public void modModel(DbsUserService dbsUserService) {
		// TODO Auto-generated method stub
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		mapper.updateByPrimaryKey(dbsUserService);
	}

	@Override
	public void delModel(Integer usedId) {
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		mapper.deleteByPrimaryKey(usedId);
		
	}
	
	public static DbsUserServiceCriteria toCriteria(DbsUserService dbsUserService){
		DbsUserServiceCriteria dbscriteria=new DbsUserServiceCriteria();
		DbsUserServiceCriteria.Criteria criteria=dbscriteria.createCriteria();
		
		if(dbsUserService.getUsedId()!=null)
		{
			criteria.andUsedIdEqualTo(dbsUserService.getUsedId());
		}
		if(!StringUtil.isBlank(dbsUserService.getUserId()))
		{
			criteria.andUserIdEqualTo(dbsUserService.getUserId());
		}
		if(!StringUtil.isBlank(dbsUserService.getUserServiceId()))
		{
			criteria.andUserServiceIdEqualTo(dbsUserService.getUserServiceId());
		}
		if(dbsUserService.getCreateTime()!=null)
		{
			criteria.andCreateTimeEqualTo(dbsUserService.getCreateTime());
		}
		return dbscriteria;
	}
}
