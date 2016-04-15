package com.ai.paas.ipaas.ccs.constants;

import com.ai.paas.ipaas.util.ResourceUtil;

/**
 * Created by astraea on 2015/4/30.
 */
public class BundleKeyConstants {
	
	/**
	 * 路径已经存在
	 */
    public static java.lang.String PATH_EXISTS = "com.ai.paas.ipaas.ccs.path_exists";
    /**
     * 路径不存在
     */
    public static java.lang.String PATH_NOT_EXISTS = "com.ai.paas.ipaas.ccs.path_not_exists";
    /**
     * 获取配置客户端失败
     */
    public static java.lang.String GET_CONFIG_CLIENT_FAILED = "com.ai.paas.ipaas.ccs.get_config_client_failed";
    /**
     * 返回客户端资源失败
     */
    public static java.lang.String RETURN_RESOURCE_FAILED = "com.ai.paas.ipaas.ccs.return_resource_failed";
    /**
     * 增加配置失败
     */
    public static java.lang.String ADD_CONFIG_FAILED = "com.ai.paas.ipaas.ccs.add_config_failed";
    /**
     * 修改配置失败
     */
    public static java.lang.String MODIFY_CONFIG_FAILED = "com.ai.paas.ipaas.ccs.modify_config_failed";
    /**
     * 获取配置失败
     */
    public static java.lang.String GET_CONFIG_FAILED = "com.ai.paas.ipaas.ccs.get_config_failed";
    /**
     * 删除配置失败
     */
    public static java.lang.String DELETE_CONFIG_FAILED = "com.ai.paas.ipaas.ccs.delete_config_failed";
    /**
     * 列出子路径失败
     */
    public static java.lang.String LIST_SUB_CONFIG_FAILED = "com.ai.paas.ipaas.ccs.list_sub_config_failed";
    /**
     * 创建ACL失败
     */
    public static java.lang.String CREATE_ACL_FAILED = "com.ai.paas.ipaas.ccs.create_acl_failed";
    /**
     * 申请类型不能为空
     */
    public static java.lang.String APPLY_TYPE_NOT_NULL = "com.ai.paas.ipaas.ccs.apply_type_not_null";
    /**
     * 服务ID不能为空
     */
    public static java.lang.String SERVICE_ID_NOT_NULL = "com.ai.paas.ipaas.ccs.service_id_not_null";
    /**
     * 超时不能为空
     */
    public static java.lang.String TIMEOUT_NOT_NULL = "com.ai.paas.ipaas.ccs.timeout_not_null";
    /**
     * 用户配置没有找到
     */
    public static java.lang.String USER_CONFIG_NOT_FOUND = "com.ai.paas.ipaas.ccs.user_config_not_found";
    /**
     * 申请服务成功
     */
    public static java.lang.String APPLY_SERVICE_SUCCESS = "com.ai.paas.ipaas.ccs.apply_service_success";
    /**
     * 注销服务成功
     */
    public static java.lang.String CANCEL_SERVICE_SUCCESS = "com.ai.paas.ipaas.ccs.cancel_service_success";
    /**
     * 申请服务类型错误
     */
    public static java.lang.String APPLY_TYPE_ERROR = "com.ai.paas.ipaas.ccs.apply_type_error";
    /**
     * 删除用户节点失败
     */
    public static java.lang.String CANCEL_USER_NODE_ERROR = "com.ai.paas.ipaas.ccs.cancel_user_node_error";
    /**
     * 创建用户节点失败
     */
    public static java.lang.String CREATE_USER_NODE_ERROR = "com.ai.paas.ipaas.ccs.create_user_node_error";
    /**
     * 创建配置服务失败
     */
    public static java.lang.String CREATE_CONFIG_SERVICE_ERROR = "com.ai.paas.ipaas.ccs.create_config_service_error";
    /**
     * 注销配置服务失败
     */
    public static java.lang.String CANCEL_CONFIG_SERVICE_ERROR = "com.ai.paas.ipaas.ccs.cancel_config_service_error";
    /**
     * 没有支持的特性
     */
    public static java.lang.String NO_SUPPORT_FEATURE = "com.ai.paas.ipaas.ccs.not_support_feature";
    /**
     * 路径不能为空
     */
    public static final String PATH_NOT_NULL = "com.ai.paas.ipaas.ccs.path_not_null";
    /**
     * 用户ID不能为空
     */
    public static final String USER_ID_NOT_NULL = "com.ai.paas.ipaas.ccs.user_id_not_null";
    /**
     * 路径类型不能为空
     */
    public static final String PATH_TYPE_NOT_NULL = "com.ai.paas.ipaas.ccs.path_type_not_null";
    /**
     * 配置错误
     */
    public static final String  CONFIG_ERROR ="com.ai.paas.ipaas.ccs.config_error";
    /**
     * 转换数据失败
     */
    public static final String CONVERT_DATA_FAILED ="com.ai.paas.ipaas.ccs.convert_data_failed";
    
    /**
     * 创建互斥锁失败
     */
    public static final String CREAT_INTERPROCESSLOCK_FAILED ="com.ai.paas.ipaas.ccs.create_lock_failed";
    
    static {
        ResourceUtil.addBundle("com.ai.paas.ipaas.ccs.ipaas-ccs");
    }
}
