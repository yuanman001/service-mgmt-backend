package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpaasStormTaskInfoMapper {
    int countByExample(IpaasStormTaskInfoCriteria example);

    int deleteByExample(IpaasStormTaskInfoCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(IpaasStormTaskInfo record);

    int insertSelective(IpaasStormTaskInfo record);

    List<IpaasStormTaskInfo> selectByExample(IpaasStormTaskInfoCriteria example);

    IpaasStormTaskInfo selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") IpaasStormTaskInfo record, @Param("example") IpaasStormTaskInfoCriteria example);

    int updateByExample(@Param("record") IpaasStormTaskInfo record, @Param("example") IpaasStormTaskInfoCriteria example);

    int updateByPrimaryKeySelective(IpaasStormTaskInfo record);

    int updateByPrimaryKey(IpaasStormTaskInfo record);
}