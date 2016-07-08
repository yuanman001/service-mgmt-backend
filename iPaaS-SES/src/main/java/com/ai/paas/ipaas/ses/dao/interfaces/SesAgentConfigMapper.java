package com.ai.paas.ipaas.ses.dao.interfaces;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesAgentConfigCriteria;
import com.ai.paas.ipaas.vo.ses.SesAgentConfig;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface SesAgentConfigMapper {
    int countByExample(SesAgentConfigCriteria example);

    int deleteByExample(SesAgentConfigCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SesAgentConfig record);

    int insertSelective(SesAgentConfig record);

    List<SesAgentConfig> selectByExample(SesAgentConfigCriteria example);

    SesAgentConfig selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SesAgentConfig record, @Param("example") SesAgentConfigCriteria example);

    int updateByExample(@Param("record") SesAgentConfig record, @Param("example") SesAgentConfigCriteria example);

    int updateByPrimaryKeySelective(SesAgentConfig record);

    int updateByPrimaryKey(SesAgentConfig record);
}