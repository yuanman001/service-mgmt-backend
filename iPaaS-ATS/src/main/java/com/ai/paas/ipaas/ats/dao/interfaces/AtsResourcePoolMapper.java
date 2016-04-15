package com.ai.paas.ipaas.ats.dao.interfaces;

import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsResourcePool;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsResourcePoolCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AtsResourcePoolMapper {
    int countByExample(AtsResourcePoolCriteria example);

    int deleteByExample(AtsResourcePoolCriteria example);

    int deleteByPrimaryKey(int atsResourceId);

    int insert(AtsResourcePool record);

    int insertSelective(AtsResourcePool record);

    List<AtsResourcePool> selectByExample(AtsResourcePoolCriteria example);

    AtsResourcePool selectByPrimaryKey(int atsResourceId);

    int updateByExampleSelective(@Param("record") AtsResourcePool record, @Param("example") AtsResourcePoolCriteria example);

    int updateByExample(@Param("record") AtsResourcePool record, @Param("example") AtsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(AtsResourcePool record);

    int updateByPrimaryKey(AtsResourcePool record);
}