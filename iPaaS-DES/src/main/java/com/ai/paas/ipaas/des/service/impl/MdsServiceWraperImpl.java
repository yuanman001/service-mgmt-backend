package com.ai.paas.ipaas.des.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.des.service.IMdsServiceWraper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserTopicMapper;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopicCriteria;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopicCriteria.Criteria;

@Service
@Transactional(rollbackFor = Exception.class)
public class MdsServiceWraperImpl implements IMdsServiceWraper {

	@Override
	public MdsUserTopic getMdsUserTopic(String userId, String serviceId) throws PaasException {
		MdsUserTopicMapper mapper = ServiceUtil.getMapper(MdsUserTopicMapper.class);
		MdsUserTopicCriteria mdsUserTopicCriteria = new MdsUserTopicCriteria();
		Criteria criteria = mdsUserTopicCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andUserSrvIdEqualTo(serviceId);
		List<MdsUserTopic> list = mapper.selectByExample(mdsUserTopicCriteria);
		if (CollectionUtils.isNotEmpty(list))
			return list.get(0);
		else
			throw new PaasException("mds does not exist");
	}

}
