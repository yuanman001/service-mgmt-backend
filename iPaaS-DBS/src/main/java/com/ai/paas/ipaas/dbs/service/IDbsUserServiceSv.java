package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;

public interface IDbsUserServiceSv {
	/**
	  * 根据主键查询获取DbsUserService
	  *@param usedId 用户使用ID (USED_ID) 
	  */
	  public DbsUserService findByPkey(java.lang.Integer usedId);
	  /**
	  * 获得列表list
	  * @param DbsUserService
	  */
	  public List<DbsUserService> getModels(DbsUserService dbsUserService);
	  /**
	  * 新增操作
	  * @param DbsUserService
	  */
	  public void addModel(DbsUserService dbsUserService);
	  /**
	  * 修改
	  * @param DbsUserService
	  */
	  public void modModel(DbsUserService dbsUserService);
	  /**
	  * 删除
	  *@param usedId 用户使用ID (USED_ID) 
	  */
	  public void delModel(java.lang.Integer usedId);
}
