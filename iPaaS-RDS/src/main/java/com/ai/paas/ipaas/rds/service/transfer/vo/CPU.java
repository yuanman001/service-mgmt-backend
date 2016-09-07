package com.ai.paas.ipaas.rds.service.transfer.vo;
/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月6日 下午3:21:49 
 * @version 
 * @since  
 */
public class CPU {

	public CPU() {
		super();
	}
	public CPU(String name, boolean usable) {
		super();
		this.name = name;
		this.usable = usable;
	}
	public String name;
	public boolean usable;
}
