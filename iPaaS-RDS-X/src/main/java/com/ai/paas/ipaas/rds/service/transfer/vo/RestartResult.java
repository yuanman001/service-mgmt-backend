package com.ai.paas.ipaas.rds.service.transfer.vo;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月20日 上午10:18:51 
 * @version 
 * @since  
 */
public class RestartResult extends RDSResult{

	public RestartResult(int warningInstanceStackEmpty) {
		super.setStatus(warningInstanceStackEmpty);
	}

	
}
