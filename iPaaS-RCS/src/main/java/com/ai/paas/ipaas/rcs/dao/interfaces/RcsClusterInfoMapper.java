package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsClusterInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsClusterInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RcsClusterInfoMapper {
    int countByExample(RcsClusterInfoCriteria example);

    int deleteByExample(RcsClusterInfoCriteria example);

    int deleteByPrimaryKey(Float id);

    int insert(RcsClusterInfo record);

    int insertSelective(RcsClusterInfo record);

    List<RcsClusterInfo> selectByExample(RcsClusterInfoCriteria example);

    RcsClusterInfo selectByPrimaryKey(Float id);

    int updateByExampleSelective(@Param("record") RcsClusterInfo record, @Param("example") RcsClusterInfoCriteria example);

    int updateByExample(@Param("record") RcsClusterInfo record, @Param("example") RcsClusterInfoCriteria example);

    int updateByPrimaryKeySelective(RcsClusterInfo record);

    int updateByPrimaryKey(RcsClusterInfo record);
}