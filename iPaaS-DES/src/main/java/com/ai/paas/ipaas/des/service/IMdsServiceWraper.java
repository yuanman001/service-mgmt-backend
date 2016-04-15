package com.ai.paas.ipaas.des.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;

/**
 * @author bixy
 * 
 */
public interface IMdsServiceWraper {

	public MdsUserTopic getMdsUserTopic(String userId, String serviceId) throws PaasException;
}
