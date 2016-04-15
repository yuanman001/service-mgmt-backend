package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RcsBoltInfoMapper {
    int countByExample(RcsBoltInfoCriteria example);

    int deleteByExample(RcsBoltInfoCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(RcsBoltInfo record);

    int insertSelective(RcsBoltInfo record);

    List<RcsBoltInfo> selectByExample(RcsBoltInfoCriteria example);

    RcsBoltInfo selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") RcsBoltInfo record, @Param("example") RcsBoltInfoCriteria example);

    int updateByExample(@Param("record") RcsBoltInfo record, @Param("example") RcsBoltInfoCriteria example);

    int updateByPrimaryKeySelective(RcsBoltInfo record);

    int updateByPrimaryKey(RcsBoltInfo record);
}