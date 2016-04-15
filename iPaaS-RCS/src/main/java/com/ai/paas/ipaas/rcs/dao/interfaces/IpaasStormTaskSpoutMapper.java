package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskSpout;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskSpoutCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpaasStormTaskSpoutMapper {
    int countByExample(IpaasStormTaskSpoutCriteria example);

    int deleteByExample(IpaasStormTaskSpoutCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(IpaasStormTaskSpout record);

    int insertSelective(IpaasStormTaskSpout record);

    List<IpaasStormTaskSpout> selectByExample(IpaasStormTaskSpoutCriteria example);

    IpaasStormTaskSpout selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") IpaasStormTaskSpout record, @Param("example") IpaasStormTaskSpoutCriteria example);

    int updateByExample(@Param("record") IpaasStormTaskSpout record, @Param("example") IpaasStormTaskSpoutCriteria example);

    int updateByPrimaryKeySelective(IpaasStormTaskSpout record);

    int updateByPrimaryKey(IpaasStormTaskSpout record);
}