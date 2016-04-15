package com.ai.paas.ipaas.rcs.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.dao.interfaces.RcsSysParameterMapper;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSysParameter;
import com.ai.paas.ipaas.rcs.service.IRcsSysParamSv;

@Service
@Transactional
public class RcsSysParamSvImpl implements IRcsSysParamSv {
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public RcsSysParameter selectByPrimaryKey(int id) {
		RcsSysParameterMapper mapper = template.getMapper(RcsSysParameterMapper.class);
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public RcsSysParameter selectByParaValue(String paraValue) {
		RcsSysParameterMapper mapper = template.getMapper(RcsSysParameterMapper.class);
		return mapper.selectByParaValue(paraValue);
	}

	@Override
	public List<RcsSysParameter> getAllParameter() throws PaasException {
		RcsSysParameterMapper mapper = template.getMapper(RcsSysParameterMapper.class);
		List<RcsSysParameter> allParameter = mapper.selectAllParameter();
		return allParameter;
	}
}
