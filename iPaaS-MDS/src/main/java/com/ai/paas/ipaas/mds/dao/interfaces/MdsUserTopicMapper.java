package com.ai.paas.ipaas.mds.dao.interfaces;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopicCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface MdsUserTopicMapper {
    int countByExample(MdsUserTopicCriteria example);

    int deleteByExample(MdsUserTopicCriteria example);

    int deleteByPrimaryKey(Integer topicInstId);

    int insert(MdsUserTopic record);

    int insertSelective(MdsUserTopic record);

    List<MdsUserTopic> selectByExample(MdsUserTopicCriteria example);

    MdsUserTopic selectByPrimaryKey(Integer topicInstId);

    int updateByExampleSelective(@Param("record") MdsUserTopic record, @Param("example") MdsUserTopicCriteria example);

    int updateByExample(@Param("record") MdsUserTopic record, @Param("example") MdsUserTopicCriteria example);

    int updateByPrimaryKeySelective(MdsUserTopic record);

    int updateByPrimaryKey(MdsUserTopic record);
}