package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsUserOptLogMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLog;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLogCriteria;
import com.ai.paas.ipaas.dbs.service.IDbsUserOptLogSv;
import com.ai.paas.ipaas.util.StringUtil;
@Service
@Transactional(rollbackFor = Exception.class)
public class DbsUserOptLogSv implements IDbsUserOptLogSv{

	
	@Override
	public DbsUserOptLog findByPkey(Integer logId) {
		DbsUserOptLogMapper mapper=ServiceUtil.getMapper(DbsUserOptLogMapper.class);
		DbsUserOptLogCriteria dbsCriteria=new DbsUserOptLogCriteria();
		DbsUserOptLogCriteria.Criteria criteria=dbsCriteria.createCriteria();
		criteria.andLogIdEqualTo(logId);
		List<DbsUserOptLog> list=mapper.selectByExample(dbsCriteria);
		return list.get(0);
	}

	@Override
	public List<DbsUserOptLog> getModels(DbsUserOptLog dbsUserOptLog) {
		DbsUserOptLogMapper mapper=ServiceUtil.getMapper(DbsUserOptLogMapper.class);
		DbsUserOptLogCriteria criteria=DbsUserOptLogSv.toCriteria(dbsUserOptLog);
		List<DbsUserOptLog> list=mapper.selectByExample(criteria);
		return list;
	}

	@Override
	public void addModel(DbsUserOptLog dbsUserOptLog) {
		DbsUserOptLogMapper mapper=ServiceUtil.getMapper(DbsUserOptLogMapper.class);
		mapper.insert(dbsUserOptLog);
	}

	@Override
	public void modModel(DbsUserOptLog dbsUserOptLog) {
		DbsUserOptLogMapper mapper=ServiceUtil.getMapper(DbsUserOptLogMapper.class);
		mapper.updateByPrimaryKey(dbsUserOptLog);
	}

	@Override
	public void delModel(Integer logId) {
		DbsUserOptLogMapper mapper=ServiceUtil.getMapper(DbsUserOptLogMapper.class);
		mapper.deleteByPrimaryKey(logId);
	}
	
	public static DbsUserOptLogCriteria toCriteria(DbsUserOptLog dbsUserOptLog){
		DbsUserOptLogCriteria dbsCriteria =new DbsUserOptLogCriteria();
		DbsUserOptLogCriteria.Criteria criteria=dbsCriteria.createCriteria();
		if(dbsUserOptLog.getResId()!=null)
		{
			criteria.andResIdEqualTo(dbsUserOptLog.getResId());
		}
		if(dbsUserOptLog.getLogId()!=null)
		{
			criteria.andLogIdEqualTo(dbsUserOptLog.getLogId());
		}
		
		if(!StringUtil.isBlank(dbsUserOptLog.getUserId()))
		{
			criteria.andUserIdEqualTo(dbsUserOptLog.getUserId());
		}
		if(!StringUtil.isBlank(dbsUserOptLog.getOperType()))
		{
			criteria.andOperTypeEqualTo(dbsUserOptLog.getOperType());
		}
		if(!StringUtil.isBlank(dbsUserOptLog.getOperResult()))
		{
			criteria.andOperResultEqualTo(dbsUserOptLog.getOperResult());
		}
		return dbsCriteria;
	}
}
