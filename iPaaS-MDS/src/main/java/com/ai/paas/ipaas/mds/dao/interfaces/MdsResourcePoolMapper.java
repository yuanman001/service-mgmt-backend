package com.ai.paas.ipaas.mds.dao.interfaces;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MdsResourcePoolMapper {
    int countByExample(MdsResourcePoolCriteria example);

    int deleteByExample(MdsResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer clusterId);

    int insert(MdsResourcePool record);

    int insertSelective(MdsResourcePool record);

    List<MdsResourcePool> selectByExample(MdsResourcePoolCriteria example);

    MdsResourcePool selectByPrimaryKey(Integer clusterId);

    int updateByExampleSelective(@Param("record") MdsResourcePool record, @Param("example") MdsResourcePoolCriteria example);

    int updateByExample(@Param("record") MdsResourcePool record, @Param("example") MdsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(MdsResourcePool record);

    int updateByPrimaryKey(MdsResourcePool record);
}