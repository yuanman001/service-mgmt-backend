package com.ai.paas.ipaas.base.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResource;
import com.ai.paas.ipaas.base.dao.mapper.bo.IpaasImageResourceCriteria;

public interface IpaasImageResourceMapper {
	int countByExample(IpaasImageResourceCriteria example);

	int deleteByExample(IpaasImageResourceCriteria example);

	int deleteByPrimaryKey(Integer id);

	int insert(IpaasImageResource record);

	int insertSelective(IpaasImageResource record);

	List<IpaasImageResource> selectByExample(IpaasImageResourceCriteria example);

	IpaasImageResource selectByPrimaryKey(Integer id);

	int updateByExampleSelective(@Param("record") IpaasImageResource record,
			@Param("example") IpaasImageResourceCriteria example);

	int updateByExample(@Param("record") IpaasImageResource record,
			@Param("example") IpaasImageResourceCriteria example);

	int updateByPrimaryKeySelective(IpaasImageResource record);

	int updateByPrimaryKey(IpaasImageResource record);
}