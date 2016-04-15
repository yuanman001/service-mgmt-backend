package com.ai.paas.ipaas.mds.manage.util;

import java.util.HashMap;
import java.util.Map;

import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.google.gson.Gson;

public class ConfigUtil {
	public static String genSenderConfig(MsgSrvApply msgSrvApply,
			MdsResourcePool kafkaCluster) {
		Map<String, String> props = new HashMap<String, String>();
		props.put("metadata.broker.list", kafkaCluster.getKafkaAddress());
		props.put("serializer.class", MDSConstant.SENDER_SERIALIZER_CLASS);
		props.put("key.serializer.class",
				MDSConstant.SENDER_KEY_SERIALIZER_CLASS);
		props.put("partitioner.class", MDSConstant.SENDER_PARTITIONER_CLASS);
		props.put("request.required.acks",
				MDSConstant.SENDER_REQUEST_REQUIRED_ACKS);
		props.put("queue.buffering.max.messages",
				MDSConstant.SENDER_QUEUE_MAX_MESSAGES);
		props.put("producer.type", MDSConstant.SENDER_PRODUCER_TYPE);
		props.put("message.send.max.retries",
				MDSConstant.SENDER_MESSAGE_SEND_MAX_RETRIES);
		props.put("compression.codec", MDSConstant.SENDER_COMPRESSION_CODEC);
		props.put("request.timeout.ms", MDSConstant.SENDER_REQUEST_TIMEOUT_MS);
		props.put("batch.num.messages", MDSConstant.SENDER_BATCH_NUM_MESSAGES);
		props.put("send.buffer.bytes", MDSConstant.SENDER_SEND_BUFFER_BYTES);
		if (msgSrvApply.getMaxSender() <= 0) {
			props.put("maxProducer", MDSConstant.SENDER_MAX_PRODUCER);
		}
		props.put("maxProducer", "" + msgSrvApply.getMaxSender());
		Gson gson = new Gson();
		return gson.toJson(props);
	}

	public static String genConsumerConfig(MsgSrvApply msgSrvApply,
			MdsResourcePool kafkaCluster) {
		Map<String, String> props = new HashMap<String, String>();
		props.put("kafka.zookeeper.hosts", kafkaCluster.getZkAddress());
		props.put("kafka.zookeeper.broker.path",
				MDSConstant.CONSUMER_KAFKA_ZOOKEEPER_BROKER_PATH);
		props.put("kafka.zookeeper.user", kafkaCluster.getZkAuthUser());
		props.put("kafka.zookeeper.user.passwd", kafkaCluster.getZkAuthPasswd());
		props.put("kafka.consumer.id", msgSrvApply.getUserId());
		Gson gson = new Gson();
		return gson.toJson(props);
	}
	
	
}
