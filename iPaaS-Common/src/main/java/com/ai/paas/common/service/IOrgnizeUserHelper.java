package com.ai.paas.common.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.common.dao.mapper.bo.OrgnizeUserInfo;

public interface IOrgnizeUserHelper {
	OrgnizeUserInfo getOrgnizeInfo(String userId) throws PaasException;
}
