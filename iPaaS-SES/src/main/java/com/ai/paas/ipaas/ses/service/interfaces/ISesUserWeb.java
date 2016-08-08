package com.ai.paas.ipaas.ses.service.interfaces;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserWeb;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesWebPool;

public interface ISesUserWeb {
	/**
	 * 获取可用的web端，如果多个，随机返回一个即可
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	public SesWebPool getAvlWeb(String userId, String serviceId);

	/**
	 * 保存用户所使用的Web端
	 * 
	 * @param userWeb
	 */
	public void saveUserWeb(SesUserWeb userWeb);

	/**
	 * 获取用户使用的web端
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	public SesUserWeb getUserWeb(String userId, String serviceId);
}
