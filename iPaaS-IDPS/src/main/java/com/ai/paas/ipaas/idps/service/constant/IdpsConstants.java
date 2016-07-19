package com.ai.paas.ipaas.idps.service.constant;

/**
 * Image dynamic Proccessing Service  常量类
 *
 */
public class IdpsConstants {
	
	/**秘钥*/
	public static final String PWD_KEY = "ImagedynamicProc";
	
	/**接口 输入参数*/
	public static final String USER_ID = "userId";
	public static final String SERVICE_ID = "serviceId";
	public static final String SERVICE_NAME = "serviceName";
	public static final String NODE_NUM = "nodeNum";
	public static final String APPLY_TYPE = "applyType";
	
	
	/**获取镜像厂库、名称时使用到的一级、二级业务CODE*/
	public static final String SERVICE_CODE = "IDPS";
	public static final String GM_IMAGE_CODE = "gm";
	public static final String BALANC_IMAGE_CODE = "nginx";
	
	/**ansible hosts*/
	public static final String CREATE_ANSIBLE_HOSTS = "idps/init_ansible_ssh_hosts.sh {0} {1} {2}";
	/**图片服务器*/
	public static final String DOCKER_4_GM_AND_TOMCAT = "idps/ansible_run_image.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12}";
	/**停用图片容器*/
	public static final String DOCKER_4_STOP_CONTAINER = "idps/ansible_stop_container.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12}";
	/**删除图片容器*/
	public static final String DOCKER_4_DELETE_CONTAINER = "idps/ansible_delete_container.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12}";
	/**启用图片容器*/
	public static final String DOCKER_4_START_CONTAINER = "idps/ansible_start_container.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9} {10} {11} {12}";
	/**图片服务器 负载均衡*/
	public static final String DOCKER_4_BALANCE = "idps/ansible_run_image_balance.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9}";
	/**启用图片容器 负载均衡*/
	public static final String DOCKER_4_BALANCE_START_CONTAINER = "idps/ansible_start_container_balance.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9}";
	/**停用图片容器 负载均衡*/
	public static final String DOCKER_4_BALANCE_STOP_CONTAINER = "idps/ansible_stop_container_balance.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9}";
	/**删除图片容器 负载均衡*/
	public static final String DOCKER_4_BALANCE_DELETE_CONTAINER = "idps/ansible_delete_container_balance.sh {1} {2} "
			+ "{3} {4} {5} {6} {7} {8} {9}";
	
	/**绑定的DSS信息*/
	public static final String DSS_P_ID = "dssPId";
	public static final String DSS_SERVICE_ID = "dssServiceId";
	public static final String DSS_SERVICE_PWD = "dssServicePwd";
	
	
	/**接口 输出参数*/
	public static final String SUCCESS_FLAG = "true";
	public static final String FAIL_FLAG = "false";

	public static final String RESULT_CODE = "resultCode";
	public static final String RESULT_MSG = "resultMsg";
	public static final String SUCCESS_CODE = "000000";
	public static final String FAIL_CODE = "999999";
	public static final String SUCCESS_INFO = "Apply IDPS successfully created!";
	public static final String FAIL_INFO = "Failed to apply the IDPS";
	
	/**接口 输入参数*/
	public static final String APPLY_TYPE_C = "create";
	public static final String APPLY_TYPE_M = "modify";
	public static final String APPLY_TYPE_R = "remove";
	public static final String APPLY_TYPE_START = "start";
	public static final String APPLY_TYPE_STOP = "stop";
	public static final String APPLY_TYPE_RESTART = "restart";
	public static final String FUNCTION_PRE="/idps/manage/";
	
	/**沉淀在zookeeper的信息*/
	public static final String IDPS_ZK_PATH="/IDPS/";
	public static final String IDPS_IMAGE_URL="IMAGEURL";
	//外网服务器地址
	public static final String IDPS_IMAGE_URL_OUT="IMAGEURL_OUT";
	public static final String IDPS_ZK_COMMON_PATH="/IDPS/COMMON";

	/**资源的状态  1是有效*/
	public static final int VALIDATE_STATUS = 1;
	public static final int INVALIDATE_STATUS = 2;

	/**从ipaas_sys_config中获取系统参数 */
	//认证地址
	public static final String AUTH_TABLE_CODE = "AUTH";
	public static final String AUTH_FIELD_CODE = "AUTH_URL";
	
	//图片服务器 项目名称
	public static final String IMAGE_SERVER_NAME_T_CODE = "IDPSWEB";
	public static final String IMAGE_SERVER_NAME_F_CODE = "NAME";
	//图片服务器 jdbc信息
	public static final String IDPS_IMAGE_SERVER="IDPSIMAGESERVER";
	public static final String IMAGE_SERVER_JDBC_URL="JDBC_URL";
	public static final String IMAGE_SERVER_JDBC_USER="JDBC_USER";
	public static final String IMAGE_SERVER_JDBC_PWD="JDBC_PWD";
	
	
	/**图片服务器  负载均衡的节点数量*/
	public static final int IDPS_BALANCE_NUM=2;
	public static final String IDPS_SPLIT="_";
	
	/**图片服务器实例节点类型 1实例节点 2负载节点*/
	public static final int IDPS_INSTANCE_TYPE=1;
	public static final int IDPS_BALANCE_TYPE=2;

	
	/**异常提示*/
	public static final String APPLY_TYPE_ERROR="com.ai.paas.ipaas.idps.apply_type_error";
	public static final String CREATE_FILE_ERROR="com.ai.paas.ipaas.idps.create_file_error";
	public static final String UPLOAD_FILE_ERROR="com.ai.paas.ipaas.idps.upload_file_error";
	public static final String START_ERROR="com.ai.paas.ipaas.idps.start_error";
	public static final String MODIFY_IDPS_RESOURCE_ERROR="com.ai.paas.ipaas.idps.modify_mcs_resource_error";
	public static final String ADD_IDPS_USER_INS_ERROR="com.ai.paas.ipaas.idps.add_idps_user_ins_error";
	
}
