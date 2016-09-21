package com.ai.paas.ipaas.rds.service.impl;
/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月20日 下午3:28:35 
 * @version 
 * @since  
 */
public class MyException extends Exception {

	public String JSONErrorInfo;

	public MyException(String jSONErrorInfo) {
		super();
		JSONErrorInfo = jSONErrorInfo;
	}
}
