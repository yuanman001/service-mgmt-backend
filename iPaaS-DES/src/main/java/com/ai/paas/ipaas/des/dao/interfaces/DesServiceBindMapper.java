package com.ai.paas.ipaas.des.dao.interfaces;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBind;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DesServiceBindMapper {
    int countByExample(DesServiceBindCriteria example);

    int deleteByExample(DesServiceBindCriteria example);

    int insert(DesServiceBind record);

    int insertSelective(DesServiceBind record);

    List<DesServiceBind> selectByExample(DesServiceBindCriteria example);

    int updateByExampleSelective(@Param("record") DesServiceBind record, @Param("example") DesServiceBindCriteria example);

    int updateByExample(@Param("record") DesServiceBind record, @Param("example") DesServiceBindCriteria example);
}