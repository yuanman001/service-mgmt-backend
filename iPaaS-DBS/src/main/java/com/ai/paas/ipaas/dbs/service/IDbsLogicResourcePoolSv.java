package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;

public interface IDbsLogicResourcePoolSv {

	/**
	  * 根据主键查询获取DbsLogicResourcePool
	  *@param logicId 逻辑库ID (LOGIC_ID) 
	  */
	  public DbsLogicResourcePool findByPkey(java.lang.Integer logicId);
	  /**
	  * 获得列表list
	  * @param DbsLogicResourcePool
	  */
	  public List<DbsLogicResourcePool> getModels(DbsLogicResourcePool dbsLogicResourcePool);
	  /**
	  * 新增操作
	  * @param DbsLogicResourcePool
	  */
	  public void addModel(DbsLogicResourcePool dbsLogicResourcePool);
	  /**
	  * 修改
	  * @param DbsLogicResourcePool
	  */
	  public void modModel(DbsLogicResourcePool dbsLogicResourcePool);
	  /**
	  * 删除
	  *@param logicId 逻辑库ID (LOGIC_ID) 
	  */
	  public void delModel(java.lang.Integer logicId);

}
