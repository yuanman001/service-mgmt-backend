package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceOperater;
import com.alibaba.dubbo.config.annotation.Service;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:50:48 
 * @version 
 * @since  
 */
@Service
public class RDSInstanceOperaterDubbo implements IRDSInstanceOperater {

	@Autowired
	private RDSInstanceOperater rdsIncOperater;

	
	@Override
	public String switchmaster(String switchmaster) {
		return rdsIncOperater.switchmaster(switchmaster);
	}
	
	@Override
	public String changecontainerconfig(String changecontainerconfig) {
		// TODO Auto-generated method stub
		return rdsIncOperater.changecontainerconfig(changecontainerconfig);
	}
	
	
}
