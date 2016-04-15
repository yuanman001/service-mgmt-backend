package com.ai.paas.ipaas.rcs.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.vo.PageSelectValue;
import com.ai.paas.ipaas.rcs.vo.StormClusterInfoVo;

public interface IIpaasRcsOpenSv {

	/**
	 * 开通服务
	 * @param param
	 * @return
	 * @throws PaasException
	 */
	String openRcs(String param) throws PaasException;
	
	/**
	 * 查询用户已开通的storm集群信息。
	 * @param userId
	 * @return
	 */
	public PageSelectValue<StormClusterInfoVo> searchClusterInfo(String userId);
}
