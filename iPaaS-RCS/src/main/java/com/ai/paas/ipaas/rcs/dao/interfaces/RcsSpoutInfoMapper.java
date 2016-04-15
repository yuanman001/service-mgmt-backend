package com.ai.paas.ipaas.rcs.dao.interfaces;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfoCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RcsSpoutInfoMapper {
    int countByExample(RcsSpoutInfoCriteria example);

    int deleteByExample(RcsSpoutInfoCriteria example);

    int deleteByPrimaryKey(long id);

    int insert(RcsSpoutInfo record);

    int insertSelective(RcsSpoutInfo record);

    List<RcsSpoutInfo> selectByExample(RcsSpoutInfoCriteria example);

    RcsSpoutInfo selectByPrimaryKey(long id);

    int updateByExampleSelective(@Param("record") RcsSpoutInfo record, @Param("example") RcsSpoutInfoCriteria example);

    int updateByExample(@Param("record") RcsSpoutInfo record, @Param("example") RcsSpoutInfoCriteria example);

    int updateByPrimaryKeySelective(RcsSpoutInfo record);

    int updateByPrimaryKey(RcsSpoutInfo record);
}