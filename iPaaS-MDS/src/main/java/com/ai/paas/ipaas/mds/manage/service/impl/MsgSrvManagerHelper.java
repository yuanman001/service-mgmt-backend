package com.ai.paas.ipaas.mds.manage.service.impl;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.interfaces.IMdsUserTopicCustomMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsResourcePoolMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserServiceMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserTopicMapper;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsKafkaLoad;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePoolCriteria;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserService;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserServiceCriteria;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopicCriteria;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManagerHelper;
import com.ai.paas.ipaas.mds.manage.util.ConfigUtil;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.util.UUIDTool;

@Component
public class MsgSrvManagerHelper implements IMsgSrvManagerHelper {
	private static transient final Logger logger = LoggerFactory
			.getLogger(MsgSrvManagerHelper.class);

	@Override
	public MdsUserService prepareUserServiceData(MsgSrvApply msgSrvApply) {
		MdsUserService userService = new MdsUserService();
		// 生成序列
		userService.setSrvInstId(Integer.parseInt(""+ServiceUtil
				.nextVal(MDSConstant.MDS_RESOURCE_POOL_SEQNAME)));
		userService.setUserId(msgSrvApply.getUserId());
		userService.setUserSrvId(msgSrvApply.getServiceId());
		Timestamp time = new Timestamp(System.currentTimeMillis());
		userService.setCreatedTime(time);
		userService.setModifiedTime(time);
		userService.setState(MDSConstant.MESSAGE_SERVICE_STATE_ENABLE);
		userService.setOperatorId(msgSrvApply.getUserId());
		return userService;
	}

	@Override
	public MdsUserTopic prepareUserTopicData(MsgSrvApply msgSrvApply,
			MdsUserService userService) {
		MdsUserTopic userTopic = new MdsUserTopic();
		// 分几步
		MdsResourcePool kafkaCluster = null;
		// 选择合适的cluster,如果以前用户在这个cluster里面有，则继续使用这个cluster
		MdsUserTopicCriteria userTopicExample = new MdsUserTopicCriteria();
		userTopicExample.createCriteria()
				.andUserIdEqualTo(msgSrvApply.getUserId())
				.andStateEqualTo(MDSConstant.USER_TOPIC_STATE_ENABLE);
		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicExample);
		if (null != userTopics && userTopics.size() > 0) {
			// 即使有多个，也使用第一个，总保持用第一个
			kafkaCluster = ServiceUtil.getMapper(MdsResourcePoolMapper.class)
					.selectByPrimaryKey(userTopics.get(0).getMdsClusterId());
		}
		if (null == kafkaCluster) {
			// 此时含了没有查到，或者根本没有情况
			// 如果没有使用过cluster，则找一个用户最少的cluster出来,如果有多个cluster，则选择第一个
			// 准备发送和消费的配置信息
			List<MdsKafkaLoad> clusterLoads = ServiceUtil.getMapper(
					IMdsUserTopicCustomMapper.class).getClusterLoad();
			MdsResourcePoolCriteria clusterExample = new MdsResourcePoolCriteria();
			clusterExample.createCriteria().andClusterStateEqualTo(
					MDSConstant.KAFKA_CLUSTER_STATE_ENABLE);
			List<MdsResourcePool> clusters = ServiceUtil.getMapper(
					MdsResourcePoolMapper.class)
					.selectByExample(clusterExample);
			// 这里还可能是空的
			if (null == clusterLoads || clusterLoads.size() <= 0) {
				//
				if (null == clusters || clusters.size() <= 0) {
					logger.error("Can not get available kafka cluster!");
					throw new PaasRuntimeException(
							"Can not get available kafka cluster!");
				}
				// 这里hash取模
				int mod = Math.abs(msgSrvApply.getUserId().hashCode())
						% clusters.size();
				kafkaCluster = clusters.get(mod);
			} else {
				// 获取所有可以用的资源列表,判断是否有没有用，有
				MdsResourcePool avalCluster = null;
				for (MdsResourcePool cluster : clusters) {
					avalCluster = cluster;
					for (MdsKafkaLoad clusterLoad : clusterLoads) {

						if (cluster.getClusterId() == clusterLoad
								.getClusterId()) {
							// 已用，可用为null
							avalCluster = null;
							break;
						}
					}
					if (null != avalCluster) {
						break;
					}
				}
				if (null == avalCluster) {
					// 排序
					Collections.sort(clusterLoads);
					// 获取第一个clusterId
					kafkaCluster = ServiceUtil.getMapper(
							MdsResourcePoolMapper.class).selectByPrimaryKey(
							clusterLoads.get(0).getClusterId());
				} else {
					kafkaCluster = avalCluster;
				}
			}
		}
		userTopic.setTopicInstId(UUIDTool.genShortId());
		userTopic.setTopicDisplayName(msgSrvApply.getTopicName());
		userTopic.setTopicEnName(msgSrvApply.getTopicEnName());
		userTopic.setUserId(msgSrvApply.getUserId());
		userTopic.setUserSrvId(msgSrvApply.getServiceId());
		userTopic.setMdsClusterId(kafkaCluster.getClusterId());
		userTopic.setSrvInstId(userService.getSrvInstId());
		// 准备生产端配置
		userTopic.setProducerConfigPath(MDSConstant.SENDER_ROOT_PATH
				+ msgSrvApply.getServiceId() + PaaSConstant.UNIX_SEPERATOR
				+ msgSrvApply.getTopicEnName() + "/sender");
		userTopic.setProducerConfig(ConfigUtil.genSenderConfig(msgSrvApply,
				kafkaCluster));
		// 准备消费端配置
		userTopic.setConsumerConfigPath(MDSConstant.CONSUMER_ROOT_PATH
				+ msgSrvApply.getServiceId() + PaaSConstant.UNIX_SEPERATOR
				+ msgSrvApply.getTopicEnName() + "/consumer");
		userTopic.setConsumerConfig(ConfigUtil.genConsumerConfig(msgSrvApply,
				kafkaCluster));
		userTopic.setTopicPartitions(msgSrvApply.getTopicPartitions());
		userTopic.setMsgReplicas(msgSrvApply.getMsgReplica());
		userTopic.setState(MDSConstant.USER_TOPIC_STATE_ENABLE);
		//
		Timestamp time = new Timestamp(System.currentTimeMillis());
		userTopic.setCreatedTime(time);
		userTopic.setModifiedTime(time);
		userTopic.setOperatorId(msgSrvApply.getUserId());
		return userTopic;
	}

	@Override
	public MdsUserService getUserService(MsgSrvApply msgSrvApply) {
		MdsUserServiceCriteria userServiceCriteria = new MdsUserServiceCriteria();
		userServiceCriteria.createCriteria()
				.andUserSrvIdEqualTo(msgSrvApply.getServiceId())
				.andUserIdEqualTo(msgSrvApply.getUserId())
				.andStateEqualTo(MDSConstant.MESSAGE_SERVICE_STATE_ENABLE);
		List<MdsUserService> userServices = ServiceUtil.getMapper(
				MdsUserServiceMapper.class)
				.selectByExample(userServiceCriteria);
		if (null == userServices || userServices.size() <= 0)
			return null;
		else
			return userServices.get(0);
	}

	@Override
	public MdsUserTopic getEnabledUserTopic(MsgSrvApply msgSrvApply) {
		MdsUserTopicCriteria userTopicExample = new MdsUserTopicCriteria();
		userTopicExample.createCriteria()
				.andUserIdEqualTo(msgSrvApply.getUserId())
				.andUserSrvIdEqualTo(msgSrvApply.getServiceId())
				.andTopicEnNameEqualTo(msgSrvApply.getTopicEnName())
				.andStateEqualTo(MDSConstant.USER_TOPIC_STATE_ENABLE);
		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicExample);
		if (null == userTopics || userTopics.size() <= 0)
			return null;
		else
			return userTopics.get(0);
	}

	@Override
	public MdsUserTopic getUserTopic(MsgSrvApply msgSrvApply) {
		MdsUserTopicCriteria userTopicExample = new MdsUserTopicCriteria();
		userTopicExample.createCriteria()
				.andUserIdEqualTo(msgSrvApply.getUserId())
				.andUserSrvIdEqualTo(msgSrvApply.getServiceId())
				.andTopicEnNameEqualTo(msgSrvApply.getTopicEnName());
		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicExample);
		if (null == userTopics || userTopics.size() <= 0)
			return null;
		else
			return userTopics.get(0);
	}

}
