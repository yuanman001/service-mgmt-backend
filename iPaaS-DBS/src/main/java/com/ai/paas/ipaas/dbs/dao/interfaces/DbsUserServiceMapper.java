package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserServiceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsUserServiceMapper {
    int countByExample(DbsUserServiceCriteria example);

    int deleteByExample(DbsUserServiceCriteria example);

    int deleteByPrimaryKey(Integer usedId);

    int insert(DbsUserService record);

    int insertSelective(DbsUserService record);

    List<DbsUserService> selectByExample(DbsUserServiceCriteria example);

    DbsUserService selectByPrimaryKey(Integer usedId);

    int updateByExampleSelective(@Param("record") DbsUserService record, @Param("example") DbsUserServiceCriteria example);

    int updateByExample(@Param("record") DbsUserService record, @Param("example") DbsUserServiceCriteria example);

    int updateByPrimaryKeySelective(DbsUserService record);

    int updateByPrimaryKey(DbsUserService record);
}