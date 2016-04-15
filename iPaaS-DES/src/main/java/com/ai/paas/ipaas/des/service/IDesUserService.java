package com.ai.paas.ipaas.des.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserService;
import com.ai.paas.ipaas.des.manage.model.DesUserServiceParam;

public interface IDesUserService {

	/**
	 * 创建des服务
	 * 
	 * @param desUserServiceParam
	 * @throws PaasException
	 */
	public void create(DesUserServiceParam desUserServiceParam) throws PaasException;

	/**
	 * 列出所有des服务
	 * 
	 * @param desUserServiceParam
	 * @return
	 */
	public List<DesUserService> listAll(DesUserServiceParam desUserServiceParam);

	/**
	 * 更新des服务的绑定状态
	 * 
	 * @param userId
	 * @param serviceId
	 */
	public void bind(String userId, String serviceId);

	/**
	 * 更新des服务的未绑定状态
	 * 
	 * @param userId
	 * @param serviceId
	 */
	public void unbind(String userId, String serviceId);
}
