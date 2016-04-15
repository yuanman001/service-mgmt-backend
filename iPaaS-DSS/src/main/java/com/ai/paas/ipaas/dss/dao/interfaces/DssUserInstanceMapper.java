package com.ai.paas.ipaas.dss.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.paas.ipaas.dss.dao.mapper.bo.DssUserInstance;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssUserInstanceCriteria;

public interface DssUserInstanceMapper {
	int countByExample(DssUserInstanceCriteria example);

	int deleteByExample(DssUserInstanceCriteria example);

	int deleteByPrimaryKey(Integer ossId);

	int insert(DssUserInstance record);

	int insertSelective(DssUserInstance record);

	List<DssUserInstance> selectByExample(DssUserInstanceCriteria example);

	DssUserInstance selectByPrimaryKey(Integer ossId);

	int updateByExampleSelective(@Param("record") DssUserInstance record,
			@Param("example") DssUserInstanceCriteria example);

	int updateByExample(@Param("record") DssUserInstance record,
			@Param("example") DssUserInstanceCriteria example);

	int updateByPrimaryKeySelective(DssUserInstance record);

	int updateByPrimaryKey(DssUserInstance record);
}