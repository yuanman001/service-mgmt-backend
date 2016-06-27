package com.ai.paas.ipaas.mds.manage.service.impl;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsResourcePoolMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserServiceMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserSubscribeMapper;
import com.ai.paas.ipaas.mds.dao.interfaces.MdsUserTopicMapper;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserService;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserServiceCriteria;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopicCriteria;
import com.ai.paas.ipaas.mds.manage.service.IMsgConfigHelper;
import com.ai.paas.ipaas.mds.manage.service.IMsgKafkaHelper;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManager;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManagerHelper;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.util.UUIDTool;

@Transactional(rollbackFor = Exception.class)
@Service
public class MsgSrvManagerImpl implements IMsgSrvManager {

	@Autowired
	IMsgSrvManagerHelper msgSrvManagerHelper;
	@Autowired
	IMsgKafkaHelper msgKafkaHelper;
	@Autowired
	IMsgConfigHelper msgConfigHelper;

	@Override
	public void createMessageService(MsgSrvApply msgSrvApply)
			throws PaasException {
		// 此处不在做输入参数的检查,放在rest层进行检查
		// 为实现调用幂等性，先检查是否存在
		// 分三步走
		// 1.准备数据，包括从资源里面获取kafka集群,要实现获取算法
		MdsUserService userService = null;
		MdsUserTopic userTopic = null;
		userTopic = msgSrvManagerHelper.getEnabledUserTopic(msgSrvApply);
		if (null == userTopic) {
			userService = msgSrvManagerHelper
					.prepareUserServiceData(msgSrvApply);
			userTopic = msgSrvManagerHelper.prepareUserTopicData(msgSrvApply,
					userService);
			// 2.先写消息服务申请数据库
			ServiceUtil.getMapper(MdsUserServiceMapper.class).insert(
					userService);
			ServiceUtil.getMapper(MdsUserTopicMapper.class).insert(userTopic);
		}

		MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
				MdsResourcePoolMapper.class).selectByPrimaryKey(
				userTopic.getMdsClusterId());

		// 3.写消息到ZK中
		msgConfigHelper.createConfigInfo(userTopic);
		// 4.创建队列，如果这里出错可以不建下面节点
		if (!msgKafkaHelper.isTopicExist(kafkaCluster,
				userTopic.getTopicEnName()))
			msgKafkaHelper.createTopic(userTopic, kafkaCluster);

	}

	@Override
	public void createTopic(MsgSrvApply topicApply) throws PaasException {

		MdsUserTopic userTopic = null;
		userTopic = msgSrvManagerHelper.getEnabledUserTopic(topicApply);
		if (null == userTopic) {
			MdsUserService userService = msgSrvManagerHelper
					.getUserService(topicApply);
			if (null == userService) {
				throw new PaasException("The userid:" + topicApply.getUserId()
						+ "'s service id:" + topicApply.getServiceId()
						+ " has not opened!");
			}
			userTopic = msgSrvManagerHelper.prepareUserTopicData(topicApply,
					userService);
			ServiceUtil.getMapper(MdsUserTopicMapper.class).insert(userTopic);
		}

		MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
				MdsResourcePoolMapper.class).selectByPrimaryKey(
				userTopic.getMdsClusterId());

		// 3.写消息到ZK中
		msgConfigHelper.createConfigInfo(userTopic);
		// 4.创建队列，如果这里出错可以不建下面节点
		if (!msgKafkaHelper.isTopicExist(kafkaCluster,
				userTopic.getTopicEnName()))
			msgKafkaHelper.createTopic(userTopic, kafkaCluster);
	}

	@Override
	public void removeTopic(MsgSrvApply topicApply) throws PaasException {
		MdsUserTopic userTopic = null;
		userTopic = msgSrvManagerHelper.getUserTopic(topicApply);
		if (null == userTopic) {
			throw new PaasException("The userid:" + topicApply.getUserId()
					+ "'s service id:" + topicApply.getServiceId() + ", topic:"
					+ topicApply.getTopicEnName() + " does not exist!");
		}
		userTopic.setState(MDSConstant.USER_TOPIC_STATE_DISABLE);
		ServiceUtil.getMapper(MdsUserTopicMapper.class).updateByPrimaryKey(
				userTopic);
		MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
				MdsResourcePoolMapper.class).selectByPrimaryKey(
				userTopic.getMdsClusterId());

		// 3.删除ZK节点
		msgConfigHelper.removeConfigInfo(userTopic);
		// 4.创建队列，如果这里出错可以不建下面节点
		if (msgKafkaHelper.isTopicExist(kafkaCluster,
				userTopic.getTopicEnName()))
			msgKafkaHelper
					.deleteTopic(kafkaCluster, userTopic.getTopicEnName());
	}

	@Override
	public void cancelMessageService(MsgSrvApply msgSrvApply)
			throws PaasException {
		// 删除服务，更新服务
		MdsUserService userService = new MdsUserService();
		userService.setState(MDSConstant.MESSAGE_SERVICE_STATE_DISABLE);
		MdsUserServiceCriteria userServiceCriteria = new MdsUserServiceCriteria();
		userServiceCriteria.createCriteria()
				.andUserIdEqualTo(msgSrvApply.getUserId())
				.andUserSrvIdEqualTo(msgSrvApply.getServiceId());
		ServiceUtil.getMapper(MdsUserServiceMapper.class)
				.updateByExampleSelective(userService, userServiceCriteria);
		// 更新所有的topic
		MdsUserTopicCriteria userTopicCriteria = new MdsUserTopicCriteria();
		userTopicCriteria.createCriteria()
				.andUserSrvIdEqualTo(msgSrvApply.getServiceId())
				.andUserIdEqualTo(msgSrvApply.getUserId());
		MdsUserTopic userTopic = new MdsUserTopic();
		userTopic.setState(MDSConstant.USER_TOPIC_STATE_DISABLE);
		// 不关心更新了多少
		ServiceUtil.getMapper(MdsUserTopicMapper.class)
				.updateByExampleSelective(userTopic, userTopicCriteria);

		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicCriteria);
		if (null != userTopics) {
			// 删除zk信息
			// 删除真正队列
			for (MdsUserTopic topicEnity : userTopics) {
				msgConfigHelper.removeConfigInfo(topicEnity);
				MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
						MdsResourcePoolMapper.class).selectByPrimaryKey(
						topicEnity.getMdsClusterId());
				msgKafkaHelper.deleteTopic(kafkaCluster,
						topicEnity.getTopicEnName());
			}
		}

	}

	@Override
	public List<MsgSrvUsageApplyResult> getTopicUsage(MsgSrvApply msgTopicApply)
			throws PaasException {
		MdsUserTopicCriteria userTopicCriteria = new MdsUserTopicCriteria();
		userTopicCriteria.createCriteria()
				.andUserSrvIdEqualTo(msgTopicApply.getServiceId())
				.andUserIdEqualTo(msgTopicApply.getUserId())
				.andTopicEnNameEqualTo(msgTopicApply.getTopicEnName())
				.andStateEqualTo(MDSConstant.USER_TOPIC_STATE_ENABLE);
		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicCriteria);
		if (null == userTopics || userTopics.size() <= 0) {
			throw new PaasRuntimeException(
					"Can not find the user's topic, userId:"
							+ msgTopicApply.getUserId() + ",srvId:"
							+ msgTopicApply.getServiceId() + ",topic:"
							+ msgTopicApply.getTopicEnName());
		}
		// 获取cluster
		MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
				MdsResourcePoolMapper.class).selectByPrimaryKey(
				userTopics.get(0).getMdsClusterId());
		if (null == kafkaCluster) {
			throw new PaasRuntimeException(
					"Can not find the user's cluster, userId:"
							+ msgTopicApply.getUserId() + ",srvId:"
							+ msgTopicApply.getServiceId() + ",topic:"
							+ msgTopicApply.getTopicEnName());
		}
		return msgKafkaHelper.getTopicOffsets(msgTopicApply.getUserId(),
				msgTopicApply.getServiceId(), msgTopicApply.getTopicEnName(),msgTopicApply.getSubscribeName(),
				kafkaCluster);
	}
	
	@Override
	public List<String> getListSubPath(MsgSrvApply msgTopicApply)
			throws PaasException {
		return msgKafkaHelper.getListSubPath(msgTopicApply.getUserId(),
				msgTopicApply.getServiceId(), msgTopicApply.getTopicEnName());
	}
	

	@Override
	public String getTopicMessage(MsgSrvApply msgTopicApply) {
		MdsUserTopicCriteria userTopicCriteria = new MdsUserTopicCriteria();
		userTopicCriteria.createCriteria()
				.andUserSrvIdEqualTo(msgTopicApply.getServiceId())
				.andUserIdEqualTo(msgTopicApply.getUserId())
				.andTopicEnNameEqualTo(msgTopicApply.getTopicEnName())
				.andStateEqualTo(MDSConstant.USER_TOPIC_STATE_ENABLE);
		List<MdsUserTopic> userTopics = ServiceUtil.getMapper(
				MdsUserTopicMapper.class).selectByExample(userTopicCriteria);
		if (null == userTopics || userTopics.size() <= 0) {
			throw new PaasRuntimeException(
					"Can not find the user's topic, userId:"
							+ msgTopicApply.getUserId() + ",srvId:"
							+ msgTopicApply.getServiceId() + ",topic:"
							+ msgTopicApply.getTopicEnName());
		}
		// 获取cluster
		MdsResourcePool kafkaCluster = ServiceUtil.getMapper(
				MdsResourcePoolMapper.class).selectByPrimaryKey(
				userTopics.get(0).getMdsClusterId());
		if (null == kafkaCluster) {
			throw new PaasRuntimeException(
					"Can not find the user's cluster, userId:"
							+ msgTopicApply.getUserId() + ",srvId:"
							+ msgTopicApply.getServiceId() + ",topic:"
							+ msgTopicApply.getTopicEnName());
		}
		return msgKafkaHelper.getTopicMessage(kafkaCluster,
				msgTopicApply.getTopicEnName(), msgTopicApply.getPartition(),
				msgTopicApply.getOffset());
	}

	@Override
	public void adjustTopicOffset(MsgSrvApply msgTopicApply) {
		// 得到路径
		String offsetPath = MDSConstant.CONSUMER_ROOT_PATH
				+ msgTopicApply.getServiceId() + PaaSConstant.UNIX_SEPERATOR
				+ msgTopicApply.getTopicEnName() + PaaSConstant.UNIX_SEPERATOR
				+ "consumer" + PaaSConstant.UNIX_SEPERATOR + "offsets"
				+ PaaSConstant.UNIX_SEPERATOR + "partition_"
				+ msgTopicApply.getPartition() + PaaSConstant.UNIX_SEPERATOR
				+ "adjusted_offset";
		try {
			msgConfigHelper.adjustMsgOffSets(offsetPath,
					msgTopicApply.getUserId(), msgTopicApply.getOffset());
		} catch (PaasException ex) {
			throw new PaasRuntimeException("Adjust Topic:"
					+ msgTopicApply.getTopicEnName() + ",partition:"
					+ msgTopicApply.getPartition() + " Offset: "
					+ msgTopicApply.getOffset());
		}
	}

	@Override
	public void sendMessage(MsgSrvApply msgTopicApply) {
		// 需要获取这个用户的send消息
		String sendConf;
		try {
			sendConf = msgConfigHelper.getTopicSendConf(
					msgTopicApply.getUserId(), msgTopicApply.getServiceId(),
					msgTopicApply.getTopicEnName());
		} catch (PaasException e) {
			throw new PaasRuntimeException("Topic:"
					+ msgTopicApply.getTopicEnName() + ",partition:"
					+ msgTopicApply.getPartition() + " Message: "
					+ msgTopicApply.getMessage() + ", get send config error!",
					e);
		}
		String msg = msgTopicApply.getMessage();
		if (StringUtil.isBlank(msg)) {
			throw new PaasRuntimeException("Adjust Topic:"
					+ msgTopicApply.getTopicEnName() + ",partition:"
					+ msgTopicApply.getPartition() + " Offset: "
					+ msgTopicApply.getOffset());
		}
		byte[] bytes;
		try {
			bytes = msg.getBytes(MDSConstant.CHARSET_UTF8);
		} catch (UnsupportedEncodingException e) {
			throw new PaasRuntimeException("Topic:"
					+ msgTopicApply.getTopicEnName() + ",partition:"
					+ msgTopicApply.getPartition() + " Message: "
					+ msgTopicApply.getMessage()
					+ ", can not convert to utf-8 bytes!", e);
		}
		msgKafkaHelper.sendMessage(msgTopicApply.getTopicEnName(),
				msgTopicApply.getPartition(), sendConf, bytes);
	}

	@Override
	public void createSubscribe(MdsUserSubscribe subscribeApply) throws PaasException {
		// TODO Auto-generated method stub
		if(null != subscribeApply){
			//设置主键
			subscribeApply.setSubscribeId(UUIDTool.genShortId());
			//设置时间
			Timestamp time = new Timestamp(System.currentTimeMillis());
			subscribeApply.setCreateTime(time);
			ServiceUtil.getMapper(MdsUserSubscribeMapper.class).insert(subscribeApply);
			// 写消息到ZK中（路径：/MDS/MDS002/8E7ECAC706994DB9AC2BBB037C18762B_MDS002_218700542/consumers/subscribe1）
			msgConfigHelper.createConsums(subscribeApply);
		}
	}

	@Override
	public List<MdsUserSubscribe> getSubscribe(MdsUserSubscribe subscribeApply) throws PaasException {
		List<MdsUserSubscribe> subList = null;
		if(null != subscribeApply){
			subList = ServiceUtil.getMapper(MdsUserSubscribeMapper.class).selectBySubscribe(subscribeApply);
			System.out.println("查询成功------订阅表---yinzf");
		}
		return subList;
	}
}
