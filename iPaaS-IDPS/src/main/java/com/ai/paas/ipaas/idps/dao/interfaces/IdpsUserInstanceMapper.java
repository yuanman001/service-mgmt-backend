package com.ai.paas.ipaas.idps.dao.interfaces;

import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsUserInstance;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsUserInstanceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdpsUserInstanceMapper {
    int countByExample(IdpsUserInstanceCriteria example);

    int deleteByExample(IdpsUserInstanceCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IdpsUserInstance record);

    int insertSelective(IdpsUserInstance record);

    List<IdpsUserInstance> selectByExample(IdpsUserInstanceCriteria example);

    IdpsUserInstance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IdpsUserInstance record, @Param("example") IdpsUserInstanceCriteria example);

    int updateByExample(@Param("record") IdpsUserInstance record, @Param("example") IdpsUserInstanceCriteria example);

    int updateByPrimaryKeySelective(IdpsUserInstance record);

    int updateByPrimaryKey(IdpsUserInstance record);
}