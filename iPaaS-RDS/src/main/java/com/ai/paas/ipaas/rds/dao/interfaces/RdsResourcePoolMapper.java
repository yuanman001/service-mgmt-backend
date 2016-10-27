package com.ai.paas.ipaas.rds.dao.interfaces;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RdsResourcePoolMapper {
    int countByExample(RdsResourcePoolCriteria example);

    int deleteByExample(RdsResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer resourceid);

    int insert(RdsResourcePool record);

    int insertSelective(RdsResourcePool record);

    List<RdsResourcePool> selectByExample(RdsResourcePoolCriteria example);

    RdsResourcePool selectByPrimaryKey(Integer resourceid);

    int updateByExampleSelective(@Param("record") RdsResourcePool record, @Param("example") RdsResourcePoolCriteria example);

    int updateByExample(@Param("record") RdsResourcePool record, @Param("example") RdsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(RdsResourcePool record);

    int updateByPrimaryKey(RdsResourcePool record);
}