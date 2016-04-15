package com.ai.paas.ipaas.ccs.dao.interfaces;

import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsServiceUserConfig;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsServiceUserConfigCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CcsServiceUserConfigMapper {
    int countByExample(CcsServiceUserConfigCriteria example);

    int deleteByExample(CcsServiceUserConfigCriteria example);

    int deleteByPrimaryKey(int id);

    int insert(CcsServiceUserConfig record);

    int insertSelective(CcsServiceUserConfig record);

    List<CcsServiceUserConfig> selectByExample(CcsServiceUserConfigCriteria example);

    CcsServiceUserConfig selectByPrimaryKey(int id);

    int updateByExampleSelective(@Param("record") CcsServiceUserConfig record, @Param("example") CcsServiceUserConfigCriteria example);

    int updateByExample(@Param("record") CcsServiceUserConfig record, @Param("example") CcsServiceUserConfigCriteria example);

    int updateByPrimaryKeySelective(CcsServiceUserConfig record);

    int updateByPrimaryKey(CcsServiceUserConfig record);
}