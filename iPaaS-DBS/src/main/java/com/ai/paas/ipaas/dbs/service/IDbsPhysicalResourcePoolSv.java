package com.ai.paas.ipaas.dbs.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;

public interface IDbsPhysicalResourcePoolSv {

	 /**
	  * 根据主键查询获取DbsPhysicalResourcePool
	  *@param resId 资源ID (RES_ID) 
	  */
	  public DbsPhysicalResourcePool findByPkey(java.lang.Integer resId);
	  /**
	  * 获得列表list
	  * @param DbsPhysicalResourcePool
	  */
	  public List<DbsPhysicalResourcePool> getModels(DbsPhysicalResourcePool dbsPhysicalResourcePool);
	  /**
	  * 新增操作
	  * @param DbsPhysicalResourcePool
	  */
	  public void addModel(DbsPhysicalResourcePool dbsPhysicalResourcePool);
	  /**
	  * 修改
	  * @param DbsPhysicalResourcePool
	  */
	  public void modModel(DbsPhysicalResourcePool dbsPhysicalResourcePool);
	  /**
	  * 删除
	  *@param resId 资源ID (RES_ID) 
	  */
	  public void delModel(java.lang.Integer resId);
	  
	   /**
	   * 分布式数据库服务申请处理
	   * @param paramVo
	   * @throws Exception
	   */
	  public String  applyDistributeDd(OpenResourceParamVo paramVo) throws Exception;
	  
	 
}
