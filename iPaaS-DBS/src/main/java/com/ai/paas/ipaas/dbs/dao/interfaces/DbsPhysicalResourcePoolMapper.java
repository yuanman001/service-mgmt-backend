package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsPhysicalResourcePoolMapper {
    int countByExample(DbsPhysicalResourcePoolCriteria example);

    int deleteByExample(DbsPhysicalResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer resId);

    int insert(DbsPhysicalResourcePool record);

    int insertSelective(DbsPhysicalResourcePool record);

    List<DbsPhysicalResourcePool> selectByExample(DbsPhysicalResourcePoolCriteria example);

    DbsPhysicalResourcePool selectByPrimaryKey(Integer resId);

    int updateByExampleSelective(@Param("record") DbsPhysicalResourcePool record, @Param("example") DbsPhysicalResourcePoolCriteria example);

    int updateByExample(@Param("record") DbsPhysicalResourcePool record, @Param("example") DbsPhysicalResourcePoolCriteria example);

    int updateByPrimaryKeySelective(DbsPhysicalResourcePool record);

    int updateByPrimaryKey(DbsPhysicalResourcePool record);
}