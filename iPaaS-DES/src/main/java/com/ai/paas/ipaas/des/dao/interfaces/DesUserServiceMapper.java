package com.ai.paas.ipaas.des.dao.interfaces;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserService;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserServiceCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DesUserServiceMapper {
    int countByExample(DesUserServiceCriteria example);

    int deleteByExample(DesUserServiceCriteria example);

    int insert(DesUserService record);

    int insertSelective(DesUserService record);

    List<DesUserService> selectByExample(DesUserServiceCriteria example);

    int updateByExampleSelective(@Param("record") DesUserService record, @Param("example") DesUserServiceCriteria example);

    int updateByExample(@Param("record") DesUserService record, @Param("example") DesUserServiceCriteria example);
}