package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskBolt;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormTaskBoltCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpaasStormTaskBoltMapper {
    int countByExample(IpaasStormTaskBoltCriteria example);

    int deleteByExample(IpaasStormTaskBoltCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(IpaasStormTaskBolt record);

    int insertSelective(IpaasStormTaskBolt record);

    List<IpaasStormTaskBolt> selectByExample(IpaasStormTaskBoltCriteria example);

    IpaasStormTaskBolt selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") IpaasStormTaskBolt record, @Param("example") IpaasStormTaskBoltCriteria example);

    int updateByExample(@Param("record") IpaasStormTaskBolt record, @Param("example") IpaasStormTaskBoltCriteria example);

    int updateByPrimaryKeySelective(IpaasStormTaskBolt record);

    int updateByPrimaryKey(IpaasStormTaskBolt record);
}