package com.ai.paas.ipaas.des.service;

import java.util.List;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;

/**
 * 包装dbs服务相关操作
 * 
 * @author bixy
 * 
 */
public interface IDbsServiceWraper {

	/**
	 * 根据用户id和用户服务id获取用户涉及的物理数据源信息
	 * @param userId
	 * @param userServiceId
	 * @return
	 */
	public List<DbsPhysicalResourcePool> getSlavePhysicalResources(String userId, String userServiceId);
}
