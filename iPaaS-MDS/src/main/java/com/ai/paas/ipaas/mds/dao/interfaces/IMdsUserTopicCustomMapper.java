package com.ai.paas.ipaas.mds.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsKafkaLoad;

public interface IMdsUserTopicCustomMapper {

	@Select("SELECT MDS_CLUSTER_ID as clusterId,COUNT(USER_ID) AS userNum FROM"
			+ " MDS_USER_TOPIC WHERE STATE=1 GROUP BY MDS_CLUSTER_ID")
	List<MdsKafkaLoad> getClusterLoad();

}