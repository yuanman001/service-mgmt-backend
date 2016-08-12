package com.ai.paas.ipaas.rds.service.transfer.vo;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午3:37:17 
 * @version 
 * @since  
 */
public class StopRDSResult extends RDSResult {

	public StopRDSResult(int warningInstanceStackEmpty) {
		super.setStatus(warningInstanceStackEmpty);
	}
	

}
