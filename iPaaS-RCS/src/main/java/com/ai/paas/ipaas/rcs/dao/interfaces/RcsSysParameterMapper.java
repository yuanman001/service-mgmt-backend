package com.ai.paas.ipaas.rcs.dao.interfaces;

import java.util.List;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSysParameter;

public interface RcsSysParameterMapper {
	List<RcsSysParameter> selectAllParameter();

	RcsSysParameter selectByPrimaryKey(Integer id);
	
	RcsSysParameter selectByParaValue(String paraValue);
}
