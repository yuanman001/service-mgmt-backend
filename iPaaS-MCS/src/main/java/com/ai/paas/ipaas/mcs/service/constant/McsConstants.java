package com.ai.paas.ipaas.mcs.service.constant;

public class McsConstants {

  public static final String SERVICE_CODE = "MCS";
  public static final String REDIS_IMAGE_CODE = "redis";
  public static final String REDIS_CLUSTER_IMAGE_CODE = "redis_cluster";
  
  public static final String SSH_USER_CODE = "mcs_ssh_user";
  public static final String SSH_USER_PWD_CODE = "mcs_ssh_user_pwd";
	
  public static final String DOCKER_COMMAND_START = "start";
  public static final String DOCKER_COMMAND_STOP = "stop";
  public static final String DOCKER_COMMAND_REMOVE = "rm";

  public static final String MODE_SINGLE = "single";
  public static final String MODE_CLUSTER = "cluster";
  public static final String MODE_REPLICATION = "replication";
  public static final String MODE_SENTINEL = "sentinel";

  public static final String PLAYBOOK_MCS_PATH = "/playbook/mcs/";
  public static final String PLAYBOOK_CFG_PATH  = "/mcs/ansible_ssh/";
  public static final String PLAYBOOK_HOST_CFG = "mcs_host.cfg";
  public static final String PLAYBOOK_SINGLE_YML = "create_single.yml";
  public static final String PLAYBOOK_CLUSTER_YML = "create_cluster.yml";
  public static final String PLAYBOOK_REPLICATION_YML = "create_replication.yml";
  public static final String PLAYBOOK_SENTINEL_YML = "create_sentinel.yml";
  public static final String PLAYBOOK_OPERATE_YML = "operate_mcs.yml";

  public static final String FILE_PATH = "/commonconfig/";
  public static final String LOG_PATH = "/redis/log/";   //modified by yuanman in 2016-05-21

  public static final String CLUSTER_FILE_PATH = "/redis/cluster/";
  public static final String AGENT_URL_BASE = "http://";

  public static final String PWD_KEY = "BaryTukyTukyBary";

  public static final String USER_ID = "userId";
  public static final String SERVICE_ID = "serviceId";
  public static final String SERVICE_NAME = "serviceName";
  public static final String APPLY_TYPE = "applyType";
  public static final String CAPACITY = "capacity";
  public static final String HA_MODE = "haMode";

  public static final String SUCCESS_FLAG = "true";
  public static final String FAIL_FLAG = "false";

  public static final String RESULT_CODE = "resultCode";
  public static final String RESULT_MSG = "resultMsg";
  public static final String SUCCESS_CODE = "000000";
  public static final String FAIL_CODE = "999999";
  public static final String SUCCESS_INFO = "Apply mcs successfully created!";
  public static final String FAIL_INFO = "Failed to apply the cache";

  public static final String APPLY_TYPE_C = "create";
  public static final String APPLY_TYPE_M = "modify";
  public static final String APPLY_TYPE_R = "remove";
  public static final String APPLY_TYPE_START = "start";
  public static final String APPLY_TYPE_STOP = "stop";
  public static final String APPLY_TYPE_RESTART = "restart";
  public static final String FUNCTION_PRE = "/mcs/manage/";

  public static final String MCS_ZK_PATH = "/MCS/";
  public static final String MCS_ZK_COMMON_PATH = "/MCS/COMMON";
  public static final String MCS_ZK_COMMON =
      "{'maxActive':'500','maxIdle':'100','maxWait':'10000',"
          + "'testOnBorrow':'false','testOnReturn':'true'}";

  public static final int CLUSTER_CACHE_NUM = 6;
  public static final int VALIDATE_STATUS = 1;
  public static final int INVALIDATE_STATUS = 2;
  public static final int SENTINEL_NUM = 6;

  public static final String DDL_KEY = "key";
  public static final String DDL_VALUE = "value";
  public static final String DDL_FIELD = "field";
  public static final String DDL_SEL_TYPE = "selectType";

  public static final String APPLY_TYPE_ERROR = "com.ai.paas.ipaas.mcs.apply_type_error";
  public static final String CREATE_FILE_ERROR = "com.ai.paas.ipaas.mcs.create_file_error";
  public static final String UPLOAD_FILE_ERROR = "com.ai.paas.ipaas.mcs.upload_file_error";
  public static final String START_ERROR = "com.ai.paas.ipaas.mcs.start_error";
  public static final String CLUSTER_START_ERROR = "com.ai.paas.ipaas.mcs.cluster_start_error";
  public static final String MODIFY_MCS_RESOURCE_ERROR =
      "com.ai.paas.ipaas.mcs.modify_mcs_resource_error";
  public static final String ADD_MCS_USER_INS_ERROR =
      "com.ai.paas.ipaas.mcs.add_mcs_user_ins_error";
}
