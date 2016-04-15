package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLog;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserOptLogCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsUserOptLogMapper {
    int countByExample(DbsUserOptLogCriteria example);

    int deleteByExample(DbsUserOptLogCriteria example);

    int deleteByPrimaryKey(Integer logId);

    int insert(DbsUserOptLog record);

    int insertSelective(DbsUserOptLog record);

    List<DbsUserOptLog> selectByExample(DbsUserOptLogCriteria example);

    DbsUserOptLog selectByPrimaryKey(Integer logId);

    int updateByExampleSelective(@Param("record") DbsUserOptLog record, @Param("example") DbsUserOptLogCriteria example);

    int updateByExample(@Param("record") DbsUserOptLog record, @Param("example") DbsUserOptLogCriteria example);

    int updateByPrimaryKeySelective(DbsUserOptLog record);

    int updateByPrimaryKey(DbsUserOptLog record);
}