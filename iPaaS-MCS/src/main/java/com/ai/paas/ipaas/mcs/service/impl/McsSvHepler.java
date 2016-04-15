package com.ai.paas.ipaas.mcs.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.mcs.dao.interfaces.McsUserCacheInstanceMapper;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstance;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstanceCriteria;
import com.ai.paas.ipaas.mcs.service.constant.McsConstants;
@Repository
public class McsSvHepler {
	
	public List<McsUserCacheInstance> getMcsUserCacheInstances(
			String serviceId, String userId) {
		McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
		McsUserCacheInstanceCriteria imc = new McsUserCacheInstanceCriteria();
		imc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
		.andUserIdEqualTo(userId).andSerialNumberEqualTo(serviceId);
		return im.selectByExample(imc);
	}

}
