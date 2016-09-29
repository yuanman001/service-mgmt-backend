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
	public String resultCode = String.valueOf(ResponseResultMark.WARN_INIT_STATUS);
	public String resultMsg;
	public String content;
	public String ExceptionTrace;
	public RDSResult(int status) {
		super();
		
		this.resultCode = String.valueOf(status);
		switch(status){
		case ResponseResultMark.SUCCESS:
			resultMsg = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			resultMsg = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			resultMsg = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			resultMsg = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			resultMsg = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			resultMsg = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			resultMsg = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			resultMsg = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			resultMsg = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			resultMsg = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			resultMsg = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			resultMsg = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			resultMsg = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			resultMsg = "ERROR_BAD_CONFIG_ZK";
			break;
		case ResponseResultMark.ERROR_INSTANCE_GROUP_CANNOT_GET_NULL:
			resultMsg = "ERROR_INSTANCE_GROUP_CANNOT_GET_NULL";
			break;
		default:
			resultMsg = "STATUS_UNKNOW";
		}
	}
	public RDSResult(int status, String discribe, String content, String exceptionTrace) {
		super();
		this.resultCode = String.valueOf(status);
		this.resultMsg = discribe;
		this.content = content;
		ExceptionTrace = exceptionTrace;
		if(discribe != null)
		switch(status){
		case ResponseResultMark.SUCCESS:
			resultMsg = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			resultMsg = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			resultMsg = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			resultMsg = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			resultMsg = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			resultMsg = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			resultMsg = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			resultMsg = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			resultMsg = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			resultMsg = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			resultMsg = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			resultMsg = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			resultMsg = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			resultMsg = "ERROR_BAD_CONFIG_ZK";
			break;
		case ResponseResultMark.ERROR_INSTANCE_GROUP_CANNOT_GET_NULL:
			resultMsg = "ERROR_INSTANCE_GROUP_CANNOT_GET_NULL";
			break;
		default:
			resultMsg = "STATUS_UNKNOW";
		}
	}
	public void setStatus(int status){
		this.resultCode = String.valueOf(status);
		switch(status){
		case ResponseResultMark.SUCCESS:
			resultMsg = "SUCCESS";
			break;
		case ResponseResultMark.WARN_INIT_STATUS:
			resultMsg = "WARN_INIT_STATUS";
			break;
		case ResponseResultMark.ERROR_ILLEGAL_AUTHORITY:
			resultMsg = "ERROR_ILLEGAL_AUTHORITY";
			break;
		case ResponseResultMark.ERROR_LESS_IMP_PARAM:
			resultMsg = "ERROR_LESS_IMP_PARAM";
			break;
		case ResponseResultMark.ERROR_NOT_EXIST_USEFUL_RESOURCE:
			resultMsg = "ERROR_NOT_EXIST_USEFUL_RESOURCE";
			break;
		case ResponseResultMark.ERROR_LESS_MEMORY_SPACE:
			resultMsg = "ERROR_LESS_MEMORY_SPACE";
			break;
		case ResponseResultMark.WARNING_INSTANCE_STACK_EMPTY:
			resultMsg = "WARNING_INSTANCE_STACK_EMPTY";
			break;
		case ResponseResultMark.ERROR_NOT_MASTER_CANNOT_MODIFY:
			resultMsg = "ERROR_NOT_MASTER_CANNOT_MODIFY";
			break;
		case ResponseResultMark.ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD:
			resultMsg = "ERROR_CANNOT__CREATE_MASTER_IN_THIS_METHOD";
			break;
		case ResponseResultMark.ERROR_UNKNOW_INSTANCE_TYPE:
			resultMsg = "ERROR_UNKNOW_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE:
			resultMsg = "ERROR_CANNOT_START_OPERA_THIS_INSTANCE_TYPE";
			break;
		case ResponseResultMark.ERROR_ONLY_CAN_CREATE_MASTER:
			resultMsg = "ERROR_ONLY_CAN_CREATE_MASTER";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG:
			resultMsg = "ERROR_BAD_CONFIG";
			break;
		case ResponseResultMark.ERROR_BAD_CONFIG_ZK:
			resultMsg = "ERROR_BAD_CONFIG_ZK";
			break;
		case ResponseResultMark.ERROR_INSTANCE_GROUP_CANNOT_GET_NULL:
			resultMsg = "ERROR_INSTANCE_GROUP_CANNOT_GET_NULL";
			break;
		default:
			resultMsg = "STATUS_UNKNOW";
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