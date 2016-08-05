package com.ai.paas.ipaas.rds.service.transfer.vo;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午3:40:34 
 * @version 
 * @since  
 */
public class CancelRDSResult  extends RDSResult{

	public CancelRDSResult(int status) {
		super(status);
		// TODO Auto-generated constructor stub
	}
	public CancelRDSResult(int status, String discribe, String content, String exceptionTrace) {
		super(status,discribe,content,exceptionTrace);
		// TODO Auto-generated constructor stub
	}
	public CancelRDSResult() {
		super();
	}
}
