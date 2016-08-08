package com.ai.paas.ipaas.ses.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.dubbo.ext.vo.BaseResponse;
import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ses.manage.rest.interfaces.IRPCIndexMapping;
import com.ai.paas.ipaas.ses.service.interfaces.IIndexMapping;
import com.ai.paas.ipaas.util.CloneTool;
import com.ai.paas.ipaas.vo.ses.SesUserMapping;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class RPCIndexMappingImpl implements IRPCIndexMapping {
	@Autowired
	IIndexMapping mappingSRV;

	@Override
	public SesUserMapping loadMapping(String userId, String serviceId)
			throws PaasException {
		com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping mapping = mappingSRV
				.loadMapping(userId, serviceId);
		return CloneTool.clone(mapping, SesUserMapping.class);
	}

	@Override
	public BaseResponse editMapping(SesUserMapping mapping)
			throws PaasException {
		mappingSRV
				.editMapping((com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping) CloneTool
						.clone(mapping,
								com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping.class));
		BaseResponse response = new BaseResponse();
		response.setSuccess(true);
		response.setResultCode(PaaSConstant.RPC_CALL_OK);
		return response;
	}

	@Override
	public BaseResponse insertMapping(SesUserMapping mapping)
			throws PaasException {
		mappingSRV
				.insertMapping((com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping) CloneTool
						.clone(mapping,
								com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping.class));
		BaseResponse response = new BaseResponse();
		response.setSuccess(true);
		response.setResultCode(PaaSConstant.RPC_CALL_OK);
		return response;
	}

}
