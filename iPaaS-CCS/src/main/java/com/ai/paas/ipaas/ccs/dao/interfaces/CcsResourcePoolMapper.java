package com.ai.paas.ipaas.ccs.dao.interfaces;

import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePool;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CcsResourcePoolMapper {
    int countByExample(CcsResourcePoolCriteria example);

    int deleteByExample(CcsResourcePoolCriteria example);

    int deleteByPrimaryKey(int id);

    int insert(CcsResourcePool record);

    int insertSelective(CcsResourcePool record);

    List<CcsResourcePool> selectByExample(CcsResourcePoolCriteria example);

    CcsResourcePool selectByPrimaryKey(int id);

    int updateByExampleSelective(@Param("record") CcsResourcePool record, @Param("example") CcsResourcePoolCriteria example);

    int updateByExample(@Param("record") CcsResourcePool record, @Param("example") CcsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(CcsResourcePool record);

    int updateByPrimaryKey(CcsResourcePool record);
}