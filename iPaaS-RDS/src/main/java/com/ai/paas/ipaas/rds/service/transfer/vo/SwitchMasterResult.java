package com.ai.paas.ipaas.rds.service.transfer.vo;
/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月1日 上午10:34:47 
 * @version 
 * @since  
 */
public class SwitchMasterResult extends RDSResult {

	public SwitchMasterResult(){}
	public SwitchMasterResult(int status){
		super.setStatus(status);
	}
	public void setStatus(int status){
		super.setStatus(status);
	}
}
