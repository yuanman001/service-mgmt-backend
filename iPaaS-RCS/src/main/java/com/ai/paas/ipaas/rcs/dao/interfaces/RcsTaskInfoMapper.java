package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RcsTaskInfoMapper {
    int countByExample(RcsTaskInfoCriteria example);

    int deleteByExample(RcsTaskInfoCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(RcsTaskInfo record);

    int insertSelective(RcsTaskInfo record);

    List<RcsTaskInfo> selectByExample(RcsTaskInfoCriteria example);

    RcsTaskInfo selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") RcsTaskInfo record, @Param("example") RcsTaskInfoCriteria example);

    int updateByExample(@Param("record") RcsTaskInfo record, @Param("example") RcsTaskInfoCriteria example);

    int updateByPrimaryKeySelective(RcsTaskInfo record);

    int updateByPrimaryKey(RcsTaskInfo record);
}