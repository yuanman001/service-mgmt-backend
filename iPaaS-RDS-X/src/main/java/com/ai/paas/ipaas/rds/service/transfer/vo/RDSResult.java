package com.ai.paas.ipaas.rds.service.transfer.vo;

import com.ai.paas.ipaas.rds.service.constant.ResponseResultMark;

/** 
 * 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午3:09:36 
 * @version 
 * @since  
 */
public class RDSResult implements RDSResultInterface{
	public int status = ResponseResultMark.WARN_INIT_STATUS;
	public String discribe;
	public String content;
	public String ExceptionTrace;
	public RDSResult(int status) {
		super();
		this.status = status;
		switch(status){
		case ResponseResultMark.SUCCESS:
			discribe = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			discribe = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			discribe = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			discribe = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			discribe = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			discribe = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			discribe = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			discribe = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			discribe = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			discribe = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			discribe = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			discribe = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			discribe = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			discribe = "ERROR_BAD_CONFIG_ZK";
			break;
		default:
			discribe = "STATUS_UNKNOW";
		}
	}
	public RDSResult(int status, String discribe, String content, String exceptionTrace) {
		super();
		this.status = status;
		this.discribe = discribe;
		this.content = content;
		ExceptionTrace = exceptionTrace;
		switch(status){
		case ResponseResultMark.SUCCESS:
			discribe = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			discribe = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			discribe = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			discribe = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			discribe = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			discribe = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			discribe = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			discribe = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			discribe = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			discribe = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			discribe = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			discribe = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			discribe = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			discribe = "ERROR_BAD_CONFIG_ZK";
			break;
		default:
			discribe = "STATUS_UNKNOW";
		}
	}
	public void setStatus(int status){
		this.status = status;
		switch(status){
		case ResponseResultMark.SUCCESS:
			discribe = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			discribe = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			discribe = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			discribe = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			discribe = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			discribe = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			discribe = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			discribe = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			discribe = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			discribe = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			discribe = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			discribe = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			discribe = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			discribe = "ERROR_BAD_CONFIG_ZK";
			break;
		default:
			discribe = "STATUS_UNKNOW";
		}
	}
	public RDSResult() {
		super();
	}

}


//switch(status){
//case ResponseResultMark
//case ResponseResultMark.SUCCESS
//case ResponseResultMark.WARN_INIT_STATUS
//case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY
//case ResponseResultMark.ERROR_LESS_IMP_PARAM
//case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE
//case ResponseResultMark.ERROR_LESS_MEMORY_SPACE
//case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY
//case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY
//case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD
//case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE
//case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE
//case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER
//case ResponseResultMark.ERROR_BAD_CONFIG
//case ResponseResultMark.ERROR_BAD_CONFIG_ZK
//}