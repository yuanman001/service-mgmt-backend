package com.ai.paas.ipaas.ses.service.interfaces;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping;

public interface IIndexMapping {
	/**
	 * 加载mapping
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 * @throws PaasException
	 */
	public SesUserMapping loadMapping(String userId, String serviceId)
			throws PaasException;

	/**
	 * 
	 * 编辑mapping
	 *
	 * @author jianhua.ma
	 * @param mapping
	 */
	void editMapping(SesUserMapping mapping) throws PaasException;

	/**
	 * 
	 * 沉淀mapping
	 *
	 * @author jianhua.ma
	 * @param mapping
	 */
	void insertMapping(SesUserMapping mapping) throws PaasException;

}
