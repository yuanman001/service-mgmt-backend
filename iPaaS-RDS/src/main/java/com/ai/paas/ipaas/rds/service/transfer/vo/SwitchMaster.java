package com.ai.paas.ipaas.rds.service.transfer.vo;
/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月1日 上午10:32:04 
 * @version 
 * @since  
 */
public class SwitchMaster {
	private int masterId;

	public SwitchMaster(int masterId) {
		super();
		this.masterId = masterId;
	}

	public int getMasterId() {
		return masterId;
	}

	public void setMasterId(int masterId) {
		this.masterId = masterId;
	}
	
}
