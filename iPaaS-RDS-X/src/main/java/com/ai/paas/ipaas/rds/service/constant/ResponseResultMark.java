package com.ai.paas.ipaas.rds.service.constant;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月14日 下午2:17:27 
 * @version 
 * @since  
 */
public interface ResponseResultMark {

	public int SUCCESS = 1;
	public int WARN_INIT_STATUS = 101;
	public int ERROR_ILLEGAL_AUTHORITY = 201;
	public int ERROR_LESS_IMP_PARAM = 202;
	public int ERROR_NOT_EXIST_USEFUL_RESOURCE = 203;
	public int ERROR_LESS_MEMORY_SPACE = 204;
	public int WARNING_INSTANCE_STACK_EMPTY = 205;
	public int ERROR_NOT_MASTER_CANNOT_MODIFY = 206;
	public int ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD = 207;
	public int ERROR_UNKNOW_INSTANCE_TYPE = 208;
	public int ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE = 209;
	public int ERROR_ONLY_CAN_CREATE_MASTER = 210;
	public int ERROR_BAD_CONFIG = 211;
	public int ERROR_BAD_CONFIG_ZK = 212;
	
}
		