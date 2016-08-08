package com.ai.paas.ipaas.ses.dao.interfaces;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesWebPool;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesWebPoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SesWebPoolMapper {
    int countByExample(SesWebPoolCriteria example);

    int deleteByExample(SesWebPoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SesWebPool record);

    int insertSelective(SesWebPool record);

    List<SesWebPool> selectByExample(SesWebPoolCriteria example);

    SesWebPool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SesWebPool record, @Param("example") SesWebPoolCriteria example);

    int updateByExample(@Param("record") SesWebPool record, @Param("example") SesWebPoolCriteria example);

    int updateByPrimaryKeySelective(SesWebPool record);

    int updateByPrimaryKey(SesWebPool record);
}