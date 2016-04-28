package com.ai.paas.ipaas.idps.dao.interfaces;

import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsBalanceResourcePool;
import com.ai.paas.ipaas.idps.dao.mapper.bo.IdpsBalanceResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IdpsBalanceResourcePoolMapper {
    int countByExample(IdpsBalanceResourcePoolCriteria example);

    int deleteByExample(IdpsBalanceResourcePoolCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(IdpsBalanceResourcePool record);

    int insertSelective(IdpsBalanceResourcePool record);

    List<IdpsBalanceResourcePool> selectByExample(IdpsBalanceResourcePoolCriteria example);

    IdpsBalanceResourcePool selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") IdpsBalanceResourcePool record, @Param("example") IdpsBalanceResourcePoolCriteria example);

    int updateByExample(@Param("record") IdpsBalanceResourcePool record, @Param("example") IdpsBalanceResourcePoolCriteria example);

    int updateByPrimaryKeySelective(IdpsBalanceResourcePool record);

    int updateByPrimaryKey(IdpsBalanceResourcePool record);
}