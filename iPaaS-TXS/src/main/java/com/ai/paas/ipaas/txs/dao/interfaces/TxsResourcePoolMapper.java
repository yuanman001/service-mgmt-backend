package com.ai.paas.ipaas.txs.dao.interfaces;

import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsResourcePool;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsResourcePoolCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TxsResourcePoolMapper {
    int countByExample(TxsResourcePoolCriteria example);

    int deleteByExample(TxsResourcePoolCriteria example);

    int deleteByPrimaryKey(int txsResourceId);

    int insert(TxsResourcePool record);

    int insertSelective(TxsResourcePool record);

    List<TxsResourcePool> selectByExample(TxsResourcePoolCriteria example);

    TxsResourcePool selectByPrimaryKey(int txsResourceId);

    int updateByExampleSelective(@Param("record") TxsResourcePool record, @Param("example") TxsResourcePoolCriteria example);

    int updateByExample(@Param("record") TxsResourcePool record, @Param("example") TxsResourcePoolCriteria example);

    int updateByPrimaryKeySelective(TxsResourcePool record);

    int updateByPrimaryKey(TxsResourcePool record);
}