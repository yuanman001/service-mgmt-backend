package com.ai.paas.ipaas.rds.dao.interfaces;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBaseCriteria;

public interface RdsIncBaseMapper {
    int countByExample(RdsIncBaseCriteria example);

    int deleteByExample(RdsIncBaseCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(RdsIncBase record);

    int insertSelective(RdsIncBase record);

    List<RdsIncBase> selectByExample(RdsIncBaseCriteria example);

    RdsIncBase selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") RdsIncBase record, @Param("example") RdsIncBaseCriteria example);

    int updateByExample(@Param("record") RdsIncBase record, @Param("example") RdsIncBaseCriteria example);

    int updateByPrimaryKeySelective(RdsIncBase record);

    int updateByPrimaryKey(RdsIncBase record);
}