package com.ai.paas.ipaas.des.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.des.dao.interfaces.DesUserServiceMapper;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserService;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserServiceCriteria;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserServiceCriteria.Criteria;
import com.ai.paas.ipaas.des.manage.model.DesUserServiceParam;
import com.ai.paas.ipaas.des.service.IDesUserService;
import com.ai.paas.ipaas.des.util.DesContants;

@Service
@Transactional(rollbackFor = Exception.class)
public class DesUserServiceImpl implements IDesUserService {
	private static final String SEQ_ID = "srv_apply_id";

	@Override
	public void create(DesUserServiceParam desUserServiceParam) throws PaasException {
		if (desUserServiceParam.getServiceId() == null) {
			PaasException exception = new PaasException(PaaSConstant.ExceptionCode.PARAM_IS_NULL, "need param service id ");
			throw exception;
		}
		if (desUserServiceParam.getUserId() == null) {
			PaasException exception = new PaasException(PaaSConstant.ExceptionCode.PARAM_IS_NULL, "need param user id ");
			throw exception;
		}

		DesUserServiceMapper mapper = ServiceUtil.getMapper(DesUserServiceMapper.class);
		DesUserService desUserService = new DesUserService();
		desUserService.setSrvApplyId(Integer.parseInt(""+ServiceUtil.nextVal(SEQ_ID)));
		desUserService.setServiceId(desUserServiceParam.getServiceId());
		desUserService.setState(DesContants.DES_STATE_UNBIND);
		desUserService.setUserId(desUserServiceParam.getUserId());
		mapper.insert(desUserService);
	}

	@Override
	public List<DesUserService> listAll(DesUserServiceParam desUserServiceParam) {
		DesUserServiceMapper mapper = ServiceUtil.getMapper(DesUserServiceMapper.class);
		DesUserServiceCriteria desUserServiceCriteria = new DesUserServiceCriteria();
		Criteria criteria = desUserServiceCriteria.createCriteria();
		criteria.andUserIdEqualTo(desUserServiceParam.getUserId());
		return mapper.selectByExample(desUserServiceCriteria);
	}

	@Override
	public void bind(String userId, String serviceId) {
		DesUserServiceMapper mapper = ServiceUtil.getMapper(DesUserServiceMapper.class);
		DesUserServiceCriteria desUserServiceCriteria = new DesUserServiceCriteria();
		Criteria criteria = desUserServiceCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		List<DesUserService> desUserServiceList = mapper.selectByExample(desUserServiceCriteria);
		DesUserService desUserService = desUserServiceList.get(0);
		desUserService.setState(DesContants.DES_STATE_BIND);
		mapper.updateByExample(desUserService, desUserServiceCriteria);
	}

	@Override
	public void unbind(String userId, String serviceId) {
		DesUserServiceMapper mapper = ServiceUtil.getMapper(DesUserServiceMapper.class);
		DesUserServiceCriteria desUserServiceCriteria = new DesUserServiceCriteria();
		Criteria criteria = desUserServiceCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		List<DesUserService> desUserServiceList = mapper.selectByExample(desUserServiceCriteria);
		DesUserService desUserService = desUserServiceList.get(0);
		desUserService.setState(DesContants.DES_STATE_UNBIND);
		mapper.updateByExampleSelective(desUserService, desUserServiceCriteria);
	}

}
