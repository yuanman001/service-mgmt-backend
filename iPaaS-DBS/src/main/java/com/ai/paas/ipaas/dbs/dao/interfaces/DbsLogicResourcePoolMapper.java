package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsLogicResourcePoolMapper {
    int countByExample(DbsLogicResourcePoolCriteria example);

    int deleteByExample(DbsLogicResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer logicId);

    int insert(DbsLogicResourcePool record);

    int insertSelective(DbsLogicResourcePool record);

    List<DbsLogicResourcePool> selectByExample(DbsLogicResourcePoolCriteria example);

    DbsLogicResourcePool selectByPrimaryKey(Integer logicId);

    int updateByExampleSelective(@Param("record") DbsLogicResourcePool record, @Param("example") DbsLogicResourcePoolCriteria example);

    int updateByExample(@Param("record") DbsLogicResourcePool record, @Param("example") DbsLogicResourcePoolCriteria example);

    int updateByPrimaryKeySelective(DbsLogicResourcePool record);

    int updateByPrimaryKey(DbsLogicResourcePool record);
}