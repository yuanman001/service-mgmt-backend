package com.ai.paas.ipaas.rcs.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSysParameter;

public interface IRcsSysParamSv {
	public RcsSysParameter selectByPrimaryKey(int id);
	
	public RcsSysParameter selectByParaValue(String paraValue);

	public List<RcsSysParameter> getAllParameter() throws PaasException;
}
