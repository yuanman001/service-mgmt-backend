package com.ai.paas.ipaas.dbs.dao.interfaces;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserServiceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DbsMuiUserServiceMapper {
    int countByExample(DbsMuiUserServiceCriteria example);

    int deleteByExample(DbsMuiUserServiceCriteria example);

    int deleteByPrimaryKey(Integer serviceId);

    int insert(DbsMuiUserService record);

    int insertSelective(DbsMuiUserService record);

    List<DbsMuiUserService> selectByExample(DbsMuiUserServiceCriteria example);

    DbsMuiUserService selectByPrimaryKey(Integer serviceId);

    int updateByExampleSelective(@Param("record") DbsMuiUserService record, @Param("example") DbsMuiUserServiceCriteria example);

    int updateByExample(@Param("record") DbsMuiUserService record, @Param("example") DbsMuiUserServiceCriteria example);

    int updateByPrimaryKeySelective(DbsMuiUserService record);

    int updateByPrimaryKey(DbsMuiUserService record);
}