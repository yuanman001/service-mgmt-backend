package com.ai.paas.ipaas.mds.dao.interfaces;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserService;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserServiceCriteria;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserServiceKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MdsUserServiceMapper {
    int countByExample(MdsUserServiceCriteria example);

    int deleteByExample(MdsUserServiceCriteria example);

    int deleteByPrimaryKey(MdsUserServiceKey key);

    int insert(MdsUserService record);

    int insertSelective(MdsUserService record);

    List<MdsUserService> selectByExample(MdsUserServiceCriteria example);

    MdsUserService selectByPrimaryKey(MdsUserServiceKey key);

    int updateByExampleSelective(@Param("record") MdsUserService record, @Param("example") MdsUserServiceCriteria example);

    int updateByExample(@Param("record") MdsUserService record, @Param("example") MdsUserServiceCriteria example);

    int updateByPrimaryKeySelective(MdsUserService record);

    int updateByPrimaryKey(MdsUserService record);
}