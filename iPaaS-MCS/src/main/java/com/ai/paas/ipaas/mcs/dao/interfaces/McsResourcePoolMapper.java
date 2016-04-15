package com.ai.paas.ipaas.mcs.dao.interfaces;

import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsResourcePool;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface McsResourcePoolMapper {
    int countByExample(McsResourcePoolCriteria example);

    int deleteByExample(McsResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(McsResourcePool record);

    int insertSelective(McsResourcePool record);

    List<McsResourcePool> selectByExample(McsResourcePoolCriteria example);

    McsResourcePool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") McsResourcePool record, @Param("example") McsResourcePoolCriteria example);

    int updateByExample(@Param("record") McsResourcePool record, @Param("example") McsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(McsResourcePool record);

    int updateByPrimaryKey(McsResourcePool record);
}