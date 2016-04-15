package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsMuiResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsMuiResourcePoolSv;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsMuiResourcePoolSv implements IDbsMuiResourcePoolSv{

	@Override
	public DbsMuiResourcePool findByPkey(Integer muiId) {
		DbsMuiResourcePoolMapper mapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
		DbsMuiResourcePool dbsMuiResourcePool=mapper.selectByPrimaryKey(muiId);
		return dbsMuiResourcePool;
	}

	@Override
	public List<DbsMuiResourcePool> getModels(
			DbsMuiResourcePool dbsMuiResourcePool) {
		DbsMuiResourcePoolMapper mapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
		List<DbsMuiResourcePool> list=mapper.selectByExample(toCriteria(dbsMuiResourcePool));
		return list;
	}

	@Override
	public void addModel(DbsMuiResourcePool dbsMuiResourcePool) {
		DbsMuiResourcePoolMapper mapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
		mapper.insert(dbsMuiResourcePool);
	}

	@Override
	public void modModel(DbsMuiResourcePool dbsMuiResourcePool) {
		DbsMuiResourcePoolMapper mapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
		mapper.updateByPrimaryKey(dbsMuiResourcePool);
		
	}

	@Override
	public void delModel(Integer muiId) {
		DbsMuiResourcePoolMapper mapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
		mapper.deleteByPrimaryKey(muiId);
	}

	private DbsMuiResourcePoolCriteria toCriteria(DbsMuiResourcePool instance){
		DbsMuiResourcePoolCriteria dbsCriteria=new DbsMuiResourcePoolCriteria();
		DbsMuiResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		if(instance.getMuiId()!=null)
		{
			criteria.andMuiIdEqualTo(instance.getMuiId());
		}
		if(instance.getMuiUrl()!=null)
		{
			criteria.andMuiUrlEqualTo(instance.getMuiUrl());
		}
		if(instance.getStatus()!=null)
		{
			criteria.andStatusEqualTo(instance.getStatus());
		}
		return dbsCriteria;
	}
}
