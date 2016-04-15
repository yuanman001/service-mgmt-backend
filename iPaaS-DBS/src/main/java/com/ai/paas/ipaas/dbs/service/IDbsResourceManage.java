package com.ai.paas.ipaas.dbs.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;

public interface IDbsResourceManage {

	   /**
	   * 分布式数据库服务申请处理
	   * @param paramVo
	   * @throws Exception
	   */
	  public void  applyDistributeDd(OpenResourceParamVo paramVo) throws Exception;
	  /**
	   * 数据库宕机后处理程序
	   * @param paramVo
	   * @throws Exception
	   */
	  public void handleCrash(String param) throws PaasException;
	  /**
	   * 数据库恢复后处理程序
	   * @param paramVo
	 * @throws PaasException 
	   * @throws Exception
	   */
	  public void recoverDistributeDb(String param) throws PaasException;
}
