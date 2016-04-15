package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormProd;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.IpaasStormProdCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface IpaasStormProdMapper {
    int countByExample(IpaasStormProdCriteria example);

    int deleteByExample(IpaasStormProdCriteria example);

    int deleteByPrimaryKey(int prodCluster);

    int insert(IpaasStormProd record);

    int insertSelective(IpaasStormProd record);

    List<IpaasStormProd> selectByExample(IpaasStormProdCriteria example);

    IpaasStormProd selectByPrimaryKey(int prodCluster);

    int updateByExampleSelective(@Param("record") IpaasStormProd record, @Param("example") IpaasStormProdCriteria example);

    int updateByExample(@Param("record") IpaasStormProd record, @Param("example") IpaasStormProdCriteria example);

    int updateByPrimaryKeySelective(IpaasStormProd record);

    int updateByPrimaryKey(IpaasStormProd record);
}