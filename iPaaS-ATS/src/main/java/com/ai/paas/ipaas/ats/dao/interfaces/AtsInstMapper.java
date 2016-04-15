package com.ai.paas.ipaas.ats.dao.interfaces;

import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsInst;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsInstCriteria;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AtsInstMapper {
    int countByExample(AtsInstCriteria example);

    int deleteByExample(AtsInstCriteria example);

    int deleteByPrimaryKey(int atsInstId);

    int insert(AtsInst record);

    int insertSelective(AtsInst record);

    List<AtsInst> selectByExample(AtsInstCriteria example);

    AtsInst selectByPrimaryKey(int atsInstId);

    int updateByExampleSelective(@Param("record") AtsInst record, @Param("example") AtsInstCriteria example);

    int updateByExample(@Param("record") AtsInst record, @Param("example") AtsInstCriteria example);

    int updateByPrimaryKeySelective(AtsInst record);

    int updateByPrimaryKey(AtsInst record);
}