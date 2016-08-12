package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceConfig;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:48:12 
 * @version 
 * @since  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSInstanceConfig implements IRDSInstanceConfig {

	@Override
	public String getmysqlparam(String getmysqlparam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modifymysqlparam(String modifymysqlparam) {
		// TODO Auto-generated method stub
		return null;
	}

}
