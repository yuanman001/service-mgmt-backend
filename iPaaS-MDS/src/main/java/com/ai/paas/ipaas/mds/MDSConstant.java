package com.ai.paas.ipaas.mds;

import com.ai.paas.ipaas.PaaSConstant;

public class MDSConstant extends PaaSConstant {
	public static final String MDS_RESOURCE_POOL_SEQNAME = "seq_mds_resource_pool";

	public static final String ZK_AUTH_SCHEMA = "digest";

	/**
	 * 用户消息服务状态-1：启用
	 */
	public static final int MESSAGE_SERVICE_STATE_ENABLE = 1;
	/**
	 * 用户消息服务状态-0：无效
	 */
	public static final int MESSAGE_SERVICE_STATE_DISABLE = 0;

	/**
	 * 用户消息服务状态-1：启用
	 */
	public static final int KAFKA_CLUSTER_STATE_ENABLE = 1;
	/**
	 * 用户消息服务状态-0：无效
	 */
	public static final int KAFKA_CLUSTER_STATE_DISABLE = 0;

	/**
	 * 用户消息服务状态-1：启用
	 */
	public static final int USER_TOPIC_STATE_ENABLE = 1;
	/**
	 * 用户消息服务状态-0：无效
	 */
	public static final int USER_TOPIC_STATE_DISABLE = 0;

	public static final String SENDER_ROOT_PATH = "/MDS/";

	public static final String SENDER_SERIALIZER_CLASS = "kafka.serializer.DefaultEncoder";

	public static final String SENDER_KEY_SERIALIZER_CLASS = "kafka.serializer.StringEncoder";

	public static final String SENDER_PARTITIONER_CLASS = "com.ai.paas.ipaas.mds.impl.sender.ModPartitioner";

	public static final String SENDER_REQUEST_REQUIRED_ACKS = "1";

	public static final String SENDER_QUEUE_MAX_MESSAGES = "1048576";

	public static final String SENDER_PRODUCER_TYPE = "sync";

	public static final String SENDER_MESSAGE_SEND_MAX_RETRIES = "3";

	public static final String SENDER_COMPRESSION_CODEC = "none";

	public static final String SENDER_REQUEST_TIMEOUT_MS = "20000";

	public static final String SENDER_BATCH_NUM_MESSAGES = "200";

	public static final String SENDER_SEND_BUFFER_BYTES = "67108864";

	public static final String SENDER_MAX_PRODUCER = "2";

	public static final String CONSUMER_ROOT_PATH = "/MDS/";

	public static final String CONSUMER_KAFKA_ZOOKEEPER_BROKER_PATH = "/brokers";

	public static final String CONSUMER_KAFKA_ZOOKEEPER_USER = "";

	public static final String CONSUMER_KAFKA_ZOOKEEPER_USER_PASSWD = "";

	/**
	 * 消费端未消费时偏移位置
	 */
	public static final long CONSUMER_NONE_OFFSET = -1;

}
