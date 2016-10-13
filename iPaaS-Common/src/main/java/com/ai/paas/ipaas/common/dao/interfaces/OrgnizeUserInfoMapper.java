package com.ai.paas.ipaas.common.dao.interfaces;

import com.ai.paas.ipaas.common.dao.mapper.bo.OrgnizeUserInfo;
import com.ai.paas.ipaas.common.dao.mapper.bo.OrgnizeUserInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OrgnizeUserInfoMapper {
    int countByExample(OrgnizeUserInfoCriteria example);

    int deleteByExample(OrgnizeUserInfoCriteria example);

    int insert(OrgnizeUserInfo record);

    int insertSelective(OrgnizeUserInfo record);

    List<OrgnizeUserInfo> selectByExample(OrgnizeUserInfoCriteria example);

    int updateByExampleSelective(@Param("record") OrgnizeUserInfo record, @Param("example") OrgnizeUserInfoCriteria example);

    int updateByExample(@Param("record") OrgnizeUserInfo record, @Param("example") OrgnizeUserInfoCriteria example);
}