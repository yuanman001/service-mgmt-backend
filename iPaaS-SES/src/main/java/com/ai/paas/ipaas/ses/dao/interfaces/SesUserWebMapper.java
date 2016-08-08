package com.ai.paas.ipaas.ses.dao.interfaces;

import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserWeb;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserWebCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SesUserWebMapper {
    int countByExample(SesUserWebCriteria example);

    int deleteByExample(SesUserWebCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(SesUserWeb record);

    int insertSelective(SesUserWeb record);

    List<SesUserWeb> selectByExample(SesUserWebCriteria example);

    SesUserWeb selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") SesUserWeb record, @Param("example") SesUserWebCriteria example);

    int updateByExample(@Param("record") SesUserWeb record, @Param("example") SesUserWebCriteria example);

    int updateByPrimaryKeySelective(SesUserWeb record);

    int updateByPrimaryKey(SesUserWeb record);
}