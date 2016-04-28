package com.ai.paas.ipaas.idps.dao.interfaces;

import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsResourcePool;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdpsResourcePoolMapper {
    int countByExample(IdpsResourcePoolCriteria example);

    int deleteByExample(IdpsResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IdpsResourcePool record);

    int insertSelective(IdpsResourcePool record);

    List<IdpsResourcePool> selectByExample(IdpsResourcePoolCriteria example);

    IdpsResourcePool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IdpsResourcePool record, @Param("example") IdpsResourcePoolCriteria example);

    int updateByExample(@Param("record") IdpsResourcePool record, @Param("example") IdpsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(IdpsResourcePool record);

    int updateByPrimaryKey(IdpsResourcePool record);
}