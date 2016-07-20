package com.ai.paas.ipaas.ses.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ses.manage.rest.interfaces.IIndexMapping;
import com.ai.paas.ipaas.util.CloneTool;
import com.ai.paas.ipaas.vo.ses.SesUserMapping;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class IndexMappingImpl implements IIndexMapping {
	@Autowired
	com.ai.paas.ipaas.ses.service.interfaces.IIndexMapping mappingSRV;

	@Override
	public SesUserMapping loadMapping(String userId, String serviceId)
			throws PaasException {
		com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping mapping = mappingSRV
				.loadMapping(userId, serviceId);
		return CloneTool.clone(mapping, SesUserMapping.class);
	}

	@Override
	public void editMapping(SesUserMapping mapping) throws PaasException {
		mappingSRV
				.editMapping((com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping) CloneTool
						.clone(mapping,
								com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping.class));
	}

	@Override
	public void insertMapping(SesUserMapping mapping) throws PaasException {
		mappingSRV
				.insertMapping((com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping) CloneTool
						.clone(mapping,
								com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping.class));
	}

}
