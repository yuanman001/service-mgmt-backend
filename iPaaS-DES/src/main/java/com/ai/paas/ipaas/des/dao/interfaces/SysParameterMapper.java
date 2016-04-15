package com.ai.paas.ipaas.des.dao.interfaces;

import com.ai.paas.ipaas.des.dao.mapper.bo.SysParameter;
import com.ai.paas.ipaas.des.dao.mapper.bo.SysParameterCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SysParameterMapper {
    int countByExample(SysParameterCriteria example);

    int deleteByExample(SysParameterCriteria example);

    int insert(SysParameter record);

    int insertSelective(SysParameter record);

    List<SysParameter> selectByExample(SysParameterCriteria example);

    int updateByExampleSelective(@Param("record") SysParameter record, @Param("example") SysParameterCriteria example);

    int updateByExample(@Param("record") SysParameter record, @Param("example") SysParameterCriteria example);
}