package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLog;

public interface IDbsUserOptLogSv {

	/**
	  * 根据主键查询获取DbsUserOptLog
	  *@param logId 日志ID (LOG_ID) 
	  */
	  public DbsUserOptLog findByPkey(java.lang.Integer logId);
	  /**
	  * 获得列表list
	  * @param DbsUserOptLog
	  */
	  public List<DbsUserOptLog> getModels(DbsUserOptLog dbsUserOptLog);
	  /**
	  * 新增操作
	  * @param DbsUserOptLog
	  */
	  public void addModel(DbsUserOptLog dbsUserOptLog);
	  /**
	  * 修改
	  * @param DbsUserOptLog
	  */
	  public void modModel(DbsUserOptLog dbsUserOptLog);
	  /**
	  * 删除
	  *@param logId 日志ID (LOG_ID) 
	  */
	  public void delModel(java.lang.Integer logId);
}
