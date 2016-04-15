package com.ai.paas.ipaas.mcs.dao.interfaces;

import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstance;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstanceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface McsUserCacheInstanceMapper {
    int countByExample(McsUserCacheInstanceCriteria example);

    int deleteByExample(McsUserCacheInstanceCriteria example);

    int deleteByPrimaryKey(Integer id);

    int insert(McsUserCacheInstance record);

    int insertSelective(McsUserCacheInstance record);

    List<McsUserCacheInstance> selectByExample(McsUserCacheInstanceCriteria example);

    McsUserCacheInstance selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") McsUserCacheInstance record, @Param("example") McsUserCacheInstanceCriteria example);

    int updateByExample(@Param("record") McsUserCacheInstance record, @Param("example") McsUserCacheInstanceCriteria example);

    int updateByPrimaryKeySelective(McsUserCacheInstance record);

    int updateByPrimaryKey(McsUserCacheInstance record);
}