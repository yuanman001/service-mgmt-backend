package com.ai.paas.ipaas.dbs.service;


/**
 *数据库宕机处理
 */
public interface IDbMonitorResult {
	/**
	 * 数据库宕机处理：获得宕机列表，触发处理流程
	 * 
	 */
	void crash();
}
