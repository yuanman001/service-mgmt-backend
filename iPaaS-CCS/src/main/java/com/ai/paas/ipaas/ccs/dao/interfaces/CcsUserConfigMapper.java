package com.ai.paas.ipaas.ccs.dao.interfaces;

import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfig;
import com.ai.paas.ipaas.ccs.dao.mapper.bo.CcsUserConfigCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CcsUserConfigMapper {
    int countByExample(CcsUserConfigCriteria example);

    int deleteByExample(CcsUserConfigCriteria example);

    int deleteByPrimaryKey(int id);

    int insert(CcsUserConfig record);

    int insertSelective(CcsUserConfig record);

    List<CcsUserConfig> selectByExample(CcsUserConfigCriteria example);

    CcsUserConfig selectByPrimaryKey(int id);

    int updateByExampleSelective(@Param("record") CcsUserConfig record, @Param("example") CcsUserConfigCriteria example);

    int updateByExample(@Param("record") CcsUserConfig record, @Param("example") CcsUserConfigCriteria example);

    int updateByPrimaryKeySelective(CcsUserConfig record);

    int updateByPrimaryKey(CcsUserConfig record);
}