package com.ai.paas.ipaas.mds.manage.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.nio.channels.UnresolvedAddressException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.admin.AdminUtils;
import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetRequest;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.javaapi.producer.Producer;
import kafka.message.MessageAndOffset;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.ZkUtils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.manage.service.IMsgKafkaHelper;
import com.ai.paas.ipaas.mds.manage.util.ZKStringSerializer;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Component
public class MsgKafkaHelper implements IMsgKafkaHelper {
	private static transient final Logger log = LoggerFactory
			.getLogger(MsgKafkaHelper.class);

	@Autowired
	private ICCSComponentManageSv configSv;
	private List<String> m_replicaBrokers = new ArrayList<String>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ai.paas.ipaas.mds.manage.service.impl.IMsgKafkaHelper#getTopicOffsets
	 * (java.lang.String, java.lang.String, java.lang.String,
	 * com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool)
	 */
	@Override
	public List<MsgSrvUsageApplyResult> getTopicOffsets(String userId,
			String userSrvId, String topic,String subscribeName,  MdsResourcePool kafkaCluster) {
		List<MsgSrvUsageApplyResult> usages = new ArrayList<>();
		List<String> seedBrokers = new ArrayList<>();
		String clusterAddr = kafkaCluster.getKafkaAddress();
		List<Integer> ports = new ArrayList<>();
		String[] addrs = clusterAddr.split(",");
		for (String addr : addrs) {
			String[] splits = addr.split("\\:");
			seedBrokers.add(splits[0]);
			ports.add(Integer.parseInt(splits[1]));
		}
		List<Integer> topicPartitions = getTopicPartitions(topic, seedBrokers,
				ports);
		for (Integer partition : topicPartitions) {
			MsgSrvUsageApplyResult usage = getTopicOffset(topic, partition,
					seedBrokers, ports);
			// 获取消费记录数
			usage.setConsumedOffset(getTopicPartitionConsumeOffset(userId,
					userSrvId, topic,subscribeName, partition));
			usages.add(usage);
		}
		return usages;
	}

	private List<Integer> getTopicPartitions(String topic,
			List<String> seedBrokers, List<Integer> ports) {
		List<Integer> partitions = new ArrayList<>();
		int i = 0;
		for (String seed : seedBrokers) {
			SimpleConsumer consumer = null;
			try {
				consumer = new SimpleConsumer(seed, ports.get(i++), 100000,
						64 * 1024, "leaderLookup");
				List<String> topics = Collections.singletonList(topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						if (!partitions.contains(part.partitionId()))
							partitions.add(part.partitionId());
					}
				}
			} catch (Exception e) {
				log.error("Error communicating with Broker [" + seed
						+ "] to find Leader for [" + topic + "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}

		return partitions;
	}

	public MsgSrvUsageApplyResult getTopicOffset(String topic, int partition,
			List<String> seedBrokers, List<Integer> ports) {
		// find the meta data about the topic and partition we are interested in
		//
		MsgSrvUsageApplyResult usage = new MsgSrvUsageApplyResult();
		usage.setPartitionId(partition);
		usage.setTopicEnName(topic);
		PartitionMetadata metadata = findLeader(seedBrokers, ports, topic,
				partition);
		if (metadata == null) {
			log.error("Can't find metadata for Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
			return usage;
		}
		if (metadata.leader() == null) {
			log.error("Can't find Leader for  Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
			return usage;
		}
		String leadBroker = metadata.leader().host();
		String clientName = "Client_" + topic + "_" + partition;

		SimpleConsumer consumer = new SimpleConsumer(leadBroker, metadata
				.leader().port(), 100000, 64 * 1024, clientName);
		long readOffset = getLastOffset(consumer, topic, partition,
				kafka.api.OffsetRequest.EarliestTime(), clientName);
		usage.setAvalOffset(readOffset);
		// 获取了可用位置和最大位置
		long totalOffset = getLastOffset(consumer, topic, partition,
				kafka.api.OffsetRequest.LatestTime(), clientName);
		// 这里需要判断，如果二者相等
		// if (readOffset == totalOffset && readOffset > 0) {
		// usage.setAvalOffset(readOffset - 1);
		// }
		usage.setTotalOffset(totalOffset);
		if (consumer != null)
			consumer.close();
		return usage;
	}

	private long getLastOffset(SimpleConsumer consumer, String topic,
			int partition, long whichTime, String clientName) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
				partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
				whichTime, 1));
		OffsetRequest request = new OffsetRequest(requestInfo,
				kafka.api.OffsetRequest.CurrentVersion(), clientName);
		OffsetResponse response = consumer.getOffsetsBefore(request);
		if (response.hasError()) {
			log.error("Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topic, partition));
			return 0;
		}
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}

	private PartitionMetadata findLeader(List<String> seedBrokers,
			List<Integer> ports, String topic, int partition) {

		PartitionMetadata returnMetaData = null;
		int i = 0;
		loop: for (String seed : seedBrokers) {
			SimpleConsumer consumer = null;
			try {
				consumer = new SimpleConsumer(seed, ports.get(i++), 100000,
						64 * 1024, "leaderLookup");
				List<String> topics = Collections.singletonList(topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						if (part.partitionId() == partition) {
							returnMetaData = part;
							break loop;
						}
					}
				}
			} catch (Exception e) {
				log.error("Error communicating with Broker [" + seed
						+ "] to find Leader for [" + topic + ", " + partition
						+ "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}
		if (returnMetaData != null) {
			m_replicaBrokers.clear();
			for (kafka.cluster.BrokerEndPoint replica : returnMetaData
					.replicas()) {
				m_replicaBrokers.add(replica.host());
			}
		}
		return returnMetaData;
	}

	private long getTopicPartitionConsumeOffset(String userId,
			String userSrvId, String topic, String subscribeName, int partition) {
		String consumerPath = MDSConstant.CONSUMER_ROOT_PATH + userSrvId
				+ PaaSConstant.UNIX_SEPERATOR + topic
				+ PaaSConstant.UNIX_SEPERATOR + (("".equals(subscribeName) || subscribeName ==null)?"consumer":subscribeName)
				+ PaaSConstant.UNIX_SEPERATOR + "offsets"
				+ PaaSConstant.UNIX_SEPERATOR + "partition_" + partition;
		log.info("=========================getTopicPartitionConsumeOffset:consumerPath==============================");
		log.info(consumerPath);
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(consumerPath);
		param.setPathType(PathType.WRITABLE);
		param.setUserId(userId);
		String content;
		try {
			content = configSv.get(param);
			log.info("=========================getTopicPartitionConsumeOffset:content==============================");
			log.info(content);
			if (!StringUtil.isBlank(content)) {
				Gson gson = new Gson();
				JsonObject json = gson.fromJson(content, JsonObject.class);
				return json.get("offset").getAsLong();
			} else {
				return MDSConstant.CONSUMER_NONE_OFFSET;
			}
		} catch (PaasException e) {
			log.error("", e);
			return MDSConstant.CONSUMER_NONE_OFFSET;
		}
	}
	
	public List<String> getListSubPath(String userId,
			String userSrvId, String topic) {
		String consumerPath = MDSConstant.CONSUMER_ROOT_PATH + userSrvId
				+ PaaSConstant.UNIX_SEPERATOR + topic;
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(consumerPath);
		param.setPathType(PathType.WRITABLE);
		param.setUserId(userId);
		List<String> listSubPath =null;
		try {
			listSubPath = configSv.listSubPath(param);
			
		} catch (PaasException e) {
			log.error("", e);
		}
		return listSubPath;
	}
	

	@Override
	public boolean isTopicExist(MdsResourcePool kafkaCluster, String topicName) {
		int sessionTimeoutMs = 10000;
		int connectionTimeoutMs = 10000;
		return AdminUtils.topicExists(
				createZKClient(kafkaCluster.getZkAddress(), sessionTimeoutMs,
						connectionTimeoutMs, kafkaCluster.getZkAuthUser(),
						kafkaCluster.getZkAuthPasswd()), topicName);
	}

	private ZkUtils createZKClient(String zkAddress, int sessionTimeoutMs,
			int connectionTimeoutMs, String authUser, String authPasswd) {
		ZkSerializer zkSerializer = new ZKStringSerializer();
		ZkClient zkClient = new ZkClient(zkAddress, sessionTimeoutMs,
				connectionTimeoutMs, zkSerializer);
		if (!StringUtil.isBlank(authUser) && StringUtil.isBlank(authPasswd)) {
			try {
				zkClient.addAuthInfo(MDSConstant.ZK_AUTH_SCHEMA, (authUser
						+ ":" + authPasswd).getBytes(MDSConstant.CHARSET_UTF8));
			} catch (UnsupportedEncodingException ex) {
				log.error("ZK auth info error!", ex);
			}
		}
		return ZkUtils.apply(zkClient, false);
	}

	@Override
	public void createTopic(MdsUserTopic userTopic, MdsResourcePool kafkaCluster) {
		int sessionTimeoutMs = 10000;
		int connectionTimeoutMs = 10000;
		Properties topicConfig = new Properties();
		AdminUtils.createTopic(
				createZKClient(kafkaCluster.getZkAddress(), sessionTimeoutMs,
						connectionTimeoutMs, kafkaCluster.getZkAuthUser(),
						kafkaCluster.getZkAuthPasswd()), userTopic
						.getTopicEnName(), userTopic.getTopicPartitions(),
				userTopic.getMsgReplicas(), topicConfig);
	}

	public void deleteTopic(MdsResourcePool kafkaCluster, String topic) {
		int sessionTimeoutMs = 10000;
		int connectionTimeoutMs = 10000;
		if (AdminUtils.topicExists(
				createZKClient(kafkaCluster.getZkAddress(), sessionTimeoutMs,
						connectionTimeoutMs, kafkaCluster.getZkAuthUser(),
						kafkaCluster.getZkAuthPasswd()), topic))
			AdminUtils.deleteTopic(
					createZKClient(kafkaCluster.getZkAddress(),
							sessionTimeoutMs, connectionTimeoutMs,
							kafkaCluster.getZkAuthUser(),
							kafkaCluster.getZkAuthPasswd()), topic);
	}

	public static void main(String[] args) {
		// 还必须获得该分区的主机地址，连接不对不行
		MsgKafkaHelper kafkaHelper = new MsgKafkaHelper();
		List<String> brokers = new ArrayList<>();
		brokers.add("10.1.228.198");
		brokers.add("10.1.228.199");
		brokers.add("10.1.228.202");
		List<Integer> ports = new ArrayList<>();
		ports.add(39091);
		ports.add(39091);
		ports.add(39091);
		MsgSrvUsageApplyResult result = kafkaHelper.getTopicOffset(
				"signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f", 0, brokers,
				ports);
		System.out.println(result.getAvalOffset() + "==="
				+ result.getTotalOffset());
	}

	@Override
	public String getTopicMessage(MdsResourcePool kafkaCluster, String topic,
			int partition, long offset) {
		String messageCnt = null;
		List<String> seedBrokers = new ArrayList<>();
		String clusterAddr = kafkaCluster.getKafkaAddress();
		List<Integer> ports = new ArrayList<>();
		String[] addrs = clusterAddr.split(",");
		for (String addr : addrs) {
			String[] splits = addr.split("\\:");
			seedBrokers.add(splits[0]);
			ports.add(Integer.parseInt(splits[1]));
		}
		PartitionMetadata metadata = findLeader(seedBrokers, ports, topic,
				partition);
		if (metadata == null) {
			log.error("Can't find metadata for Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
			throw new RuntimeException("Can't find metadata for Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
		}
		if (metadata.leader() == null) {
			log.error("Can't find Leader for  Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
			throw new RuntimeException("Can't find Leader for  Topic:" + topic
					+ " and Partition:" + partition + ". Exiting!");
		}
		String leadBroker = metadata.leader().host();
		String clientName = "Client_" + topic + "_" + partition;

		SimpleConsumer consumer = new SimpleConsumer(leadBroker, metadata
				.leader().port(), 100000, 1024 * 1024, clientName);
		FetchResponse fetchResponse = fetchMessages(consumer, topic, partition,
				offset);
		if (fetchResponse.hasError()) {
			String message = "Error fetching data from [" + partition
					+ "] for topic [" + topic + "]: [" + topic + "]";
			log.error(message);
			throw new PaasRuntimeException(message);
		}

		ByteBufferMessageSet msgs = fetchResponse.messageSet(topic, partition);
		for (MessageAndOffset msg : msgs) {
			if (msg.message() != null && msg.offset() == offset) {
				byte[] payload = new byte[msg.message().payload().remaining()];
				try {
					msg.message().payload().get(payload);
					messageCnt = new String(payload, PaaSConstant.CHARSET_UTF8);
				} catch (UnsupportedEncodingException e) {
					log.warn("UnsupportedEncodingException:", e);
				}
				break;
			}
		}
		return messageCnt;
	}

	private FetchResponse fetchMessages(SimpleConsumer consumer, String topic,
			int partition, long offset) {

		FetchRequestBuilder builder = new FetchRequestBuilder();
		FetchRequest fetchRequest = builder
				.addFetch(topic, partition, offset, 1024 * 1024)
				.clientId("Single_" + topic + "_" + partition).build();
		FetchResponse fetchResponse;
		try {
			fetchResponse = consumer.fetch(fetchRequest);
		} catch (Exception e) {
			if (e instanceof ConnectException
					|| e instanceof SocketTimeoutException
					|| e instanceof IOException
					|| e instanceof UnresolvedAddressException) {

				log.warn("Network error when fetching messages:", e);
				throw new PaasRuntimeException(
						"Network error when fetching messages:", e);
			} else {
				throw new RuntimeException(e);
			}
		}
		return fetchResponse;
	}

	@Override
	public void sendMessage(String topic, int parition, String senderConf,
			byte[] message) {
		// 向队列发送消息
		// 准备配置信息
		ProducerConfig cfg = null;
		Gson gson = new Gson();
		Properties props = gson.fromJson(senderConf, Properties.class);
		cfg = new ProducerConfig(props);
		Producer<String, byte[]> producer = new Producer<>(cfg);
		try {
			KeyedMessage<String, byte[]> km = new KeyedMessage<String, byte[]>(
					topic, String.valueOf(parition), message);
			producer.send(km);
		} catch (Exception e) {
			log.error("", e);
			throw new RuntimeException(e);
		} finally {
			if (null != producer) {
				try {
					producer.close();
				} catch (Exception e) {
					log.error("", e);
				}
			}
		}
	}
}
