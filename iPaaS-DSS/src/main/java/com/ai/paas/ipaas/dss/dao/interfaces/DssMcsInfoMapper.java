package com.ai.paas.ipaas.dss.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.paas.ipaas.dss.dao.mapper.bo.DssMcsInfo;
import com.ai.paas.ipaas.dss.dao.mapper.bo.DssMcsInfoCriteria;

public interface DssMcsInfoMapper {
	int countByExample(DssMcsInfoCriteria example);

	int deleteByExample(DssMcsInfoCriteria example);

	int deleteByPrimaryKey(Integer id);

	int insert(DssMcsInfo record);

	int insertSelective(DssMcsInfo record);

	List<DssMcsInfo> selectByExample(DssMcsInfoCriteria example);

	DssMcsInfo selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") DssMcsInfo record,
			@Param("example") DssMcsInfoCriteria example);

	int updateByExample(@Param("record") DssMcsInfo record,
			@Param("example") DssMcsInfoCriteria example);

	int updateByPrimaryKeySelective(DssMcsInfo record);

	int updateByPrimaryKey(DssMcsInfo record);
}