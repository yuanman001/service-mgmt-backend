package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsLogicResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsLogicResourcePoolSv;
import com.ai.paas.ipaas.util.StringUtil;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsLogicResourcePoolSv implements IDbsLogicResourcePoolSv{

	
	@Override
	public DbsLogicResourcePool findByPkey(Integer logicId) {
		DbsLogicResourcePoolMapper mapper = ServiceUtil.getMapper(DbsLogicResourcePoolMapper.class);
		DbsLogicResourcePoolCriteria dbsCriteria=new DbsLogicResourcePoolCriteria();
		DbsLogicResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		criteria.andLogicIdEqualTo(logicId);
		List<DbsLogicResourcePool> list=mapper.selectByExample(dbsCriteria);
		return list.get(0);
	}

	@Override
	public List<DbsLogicResourcePool> getModels(
			DbsLogicResourcePool dbsLogicResourcePool) {
		DbsLogicResourcePoolMapper mapper = ServiceUtil.getMapper(DbsLogicResourcePoolMapper.class);
		DbsLogicResourcePoolCriteria dbsCriteria=DbsLogicResourcePoolSv.toCriteria(dbsLogicResourcePool);
		List<DbsLogicResourcePool> list=mapper.selectByExample(dbsCriteria);
		return list;
	}

	@Override
	public void addModel(DbsLogicResourcePool dbsLogicResourcePool) {
		DbsLogicResourcePoolMapper mapper=ServiceUtil.getMapper(DbsLogicResourcePoolMapper.class);
		mapper.insert(dbsLogicResourcePool);
		
	}

	@Override
	public void modModel(DbsLogicResourcePool dbsLogicResourcePool) {
		DbsLogicResourcePoolMapper mapper=ServiceUtil.getMapper(DbsLogicResourcePoolMapper.class);
		mapper.updateByPrimaryKey(dbsLogicResourcePool);
	}

	@Override
	public void delModel(Integer logicId) {
		DbsLogicResourcePoolMapper mapper=ServiceUtil.getMapper(DbsLogicResourcePoolMapper.class);
		DbsLogicResourcePoolCriteria dbsCriteria=new DbsLogicResourcePoolCriteria();
		DbsLogicResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		criteria.andLogicIdEqualTo(logicId);
		mapper.deleteByExample(dbsCriteria);
	}

	public static DbsLogicResourcePoolCriteria toCriteria(DbsLogicResourcePool dbsLogicResourcePool){
		DbsLogicResourcePoolCriteria dbsCriteria=new DbsLogicResourcePoolCriteria();
		DbsLogicResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		if(dbsLogicResourcePool.getLogicId()!=null)
		{
			criteria.andLogicIdEqualTo(dbsLogicResourcePool.getLogicId());
		}
		if(dbsLogicResourcePool.getUsedId()!=null){
			criteria.andUsedIdEqualTo(dbsLogicResourcePool.getUsedId());
		}
		
		if(!StringUtil.isBlank(dbsLogicResourcePool.getLogicName()))
		{
			criteria.andLogicNameEqualTo(dbsLogicResourcePool.getLogicName());
		}
		if(!StringUtil.isBlank(dbsLogicResourcePool.getResUseType()))
		{
			criteria.andResUseTypeEqualTo(dbsLogicResourcePool.getResUseType());
		}
		if(dbsLogicResourcePool.getCreateTime()!=null)
		{
			criteria.andCreateTimeEqualTo(dbsLogicResourcePool.getCreateTime());
		}
		return dbsCriteria;
	}
}
