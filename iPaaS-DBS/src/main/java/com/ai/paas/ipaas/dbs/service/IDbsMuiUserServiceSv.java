package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService;

public interface IDbsMuiUserServiceSv {

	/**
	  * 根据主键查询获取DbsUserOptLog
	  *@param logId 日志ID (LOG_ID) 
	  */
	  public DbsMuiUserService findByPkey(java.lang.Integer serviceId);
	  /**
	  * 获得列表list
	  * @param DbsUserOptLog
	  */
	  public List<DbsMuiUserService> getModels(DbsMuiUserService dbsUserService);
	  /**
	  * 新增操作
	  * @param DbsUserOptLog
	  */
	  public void addModel(DbsMuiUserService dbsUserService);
	  /**
	  * 修改
	  * @param DbsUserOptLog
	  */
	  public void modModel(DbsMuiUserService dbsUserService);
	  /**
	  * 删除
	  *@param logId 日志ID (LOG_ID) 
	  */
	  public void delModel(java.lang.Integer serviceId);
}
