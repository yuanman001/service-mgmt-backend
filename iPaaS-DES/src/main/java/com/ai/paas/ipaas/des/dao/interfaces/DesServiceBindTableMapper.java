package com.ai.paas.ipaas.des.dao.interfaces;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTable;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTableCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DesServiceBindTableMapper {
    int countByExample(DesServiceBindTableCriteria example);

    int deleteByExample(DesServiceBindTableCriteria example);

    int insert(DesServiceBindTable record);

    int insertSelective(DesServiceBindTable record);

    List<DesServiceBindTable> selectByExample(DesServiceBindTableCriteria example);

    int updateByExampleSelective(@Param("record") DesServiceBindTable record, @Param("example") DesServiceBindTableCriteria example);

    int updateByExample(@Param("record") DesServiceBindTable record, @Param("example") DesServiceBindTableCriteria example);
}