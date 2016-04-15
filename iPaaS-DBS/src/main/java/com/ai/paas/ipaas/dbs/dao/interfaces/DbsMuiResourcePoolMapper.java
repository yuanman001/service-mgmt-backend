package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsMuiResourcePoolMapper {
    int countByExample(DbsMuiResourcePoolCriteria example);

    int deleteByExample(DbsMuiResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer muiId);

    int insert(DbsMuiResourcePool record);

    int insertSelective(DbsMuiResourcePool record);

    List<DbsMuiResourcePool> selectByExample(DbsMuiResourcePoolCriteria example);

    DbsMuiResourcePool selectByPrimaryKey(Integer muiId);

    int updateByExampleSelective(@Param("record") DbsMuiResourcePool record, @Param("example") DbsMuiResourcePoolCriteria example);

    int updateByExample(@Param("record") DbsMuiResourcePool record, @Param("example") DbsMuiResourcePoolCriteria example);

    int updateByPrimaryKeySelective(DbsMuiResourcePool record);

    int updateByPrimaryKey(DbsMuiResourcePool record);
}