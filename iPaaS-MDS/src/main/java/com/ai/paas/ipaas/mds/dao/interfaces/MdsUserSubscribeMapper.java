package com.ai.paas.ipaas.mds.dao.interfaces;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribeCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MdsUserSubscribeMapper {
    int countByExample(MdsUserSubscribeCriteria example);

    int deleteByExample(MdsUserSubscribeCriteria example);

    int insert(MdsUserSubscribe record);

    int insertSelective(MdsUserSubscribe record);

    List<MdsUserSubscribe> selectByExample(MdsUserSubscribeCriteria example);
    
    List<MdsUserSubscribe> selectBySubscribe(MdsUserSubscribe example);

    int updateByExampleSelective(@Param("record") MdsUserSubscribe record, @Param("example") MdsUserSubscribeCriteria example);

    int updateByExample(@Param("record") MdsUserSubscribe record, @Param("example") MdsUserSubscribeCriteria example);
}