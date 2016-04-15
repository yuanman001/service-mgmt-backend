package com.ai.paas.ipaas.txs.dao.interfaces;

import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInst;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInstCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TxsInstMapper {
    int countByExample(TxsInstCriteria example);

    int deleteByExample(TxsInstCriteria example);

    int deleteByPrimaryKey(int txsInstId);

    int insert(TxsInst record);

    int insertSelective(TxsInst record);

    List<TxsInst> selectByExample(TxsInstCriteria example);

    TxsInst selectByPrimaryKey(int txsInstId);

    int updateByExampleSelective(@Param("record") TxsInst record, @Param("example") TxsInstCriteria example);

    int updateByExample(@Param("record") TxsInst record, @Param("example") TxsInstCriteria example);

    int updateByPrimaryKeySelective(TxsInst record);

    int updateByPrimaryKey(TxsInst record);
}