package com.ai.paas.ipaas.des.service;

import java.util.List;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBind;
import com.ai.paas.ipaas.des.manage.model.DesServiceBindParam;

/**
 * DES 绑定服务
 * 
 * @author bixy
 * 
 */
public interface IDesServiceBind {

	/**
	 * 绑定des服务
	 * 
	 * @param desServiceBindParam
	 */
	public void bind(DesServiceBindParam desServiceBindParam, String mdsTopic, int mdsPartition);

	/**
	 * 解绑des服务
	 * 
	 * @param userId
	 * @param serviceId
	 */
	public void unbind(String userId, String serviceId);

	/**
	 * 获取已经绑定的服务
	 * 
	 * @param userId
	 * @return
	 */
	public List<DesServiceBind> getBound(String userId);

	/**
	 * 获取某个已经绑定的服务
	 * 
	 * @param userId 用户的id
	 * @param serviceId des服务的id
	 * @return
	 */
	public DesServiceBind getBound(String userId, String serviceId);

	/**
	 * 检查某个dbs服务是否已经绑定
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	public boolean checkDbsServiceBound(String userId, String serviceId);

	/**
	 * 检查某个mbs服务是否已经绑定
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	public boolean checkMdsServiceBound(String userId, String serviceId);
}
