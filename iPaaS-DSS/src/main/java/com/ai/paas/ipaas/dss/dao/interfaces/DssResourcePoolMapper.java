package com.ai.paas.ipaas.dss.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.paas.ipaas.dss.dao.mapper.bo.DssResourcePool;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssResourcePoolCriteria;

public interface DssResourcePoolMapper {
	int countByExample(DssResourcePoolCriteria example);

	int deleteByExample(DssResourcePoolCriteria example);

	int deleteByPrimaryKey(Integer hostId);

	int insert(DssResourcePool record);

	int insertSelective(DssResourcePool record);

	List<DssResourcePool> selectByExample(DssResourcePoolCriteria example);

	DssResourcePool selectByPrimaryKey(Integer hostId);

	int updateByExampleSelective(@Param("record") DssResourcePool record,
			@Param("example") DssResourcePoolCriteria example);

	int updateByExample(@Param("record") DssResourcePool record,
			@Param("example") DssResourcePoolCriteria example);

	int updateByPrimaryKeySelective(DssResourcePool record);

	int updateByPrimaryKey(DssResourcePool record);
}