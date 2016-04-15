package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePool;

public interface IDbsMuiResourcePoolSv {

	/**
	  * 根据主键查询获取DbsUserService
	  *@param  
	  */
	  public DbsMuiResourcePool findByPkey(java.lang.Integer muiId);
	  /**
	  * 获得列表list
	  * @param DbsUserService
	  */
	  public List<DbsMuiResourcePool> getModels(DbsMuiResourcePool dbsMuiResourcePool);
	  /**
	  * 新增操作
	  * @param DbsUserService
	  */
	  public void addModel(DbsMuiResourcePool dbsMuiResourcePool);
	  /**
	  * 修改
	  * @param DbsUserService
	  */
	  public void modModel(DbsMuiResourcePool dbsMuiResourcePool);
	  /**
	  * 删除
	  *@param 
	  */
	  public void delModel(java.lang.Integer muiId);
}
