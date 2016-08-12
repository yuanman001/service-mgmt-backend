package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSAccountManager;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:46:02 
 * @version 
 * @since  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSAccountManager implements IRDSAccountManager {

	@Override
	public String createmysqlroot(String createmysqlroot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelmysqlroot(String cancelmysqlroot) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getipwritelist(String getipwritelist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modifyipwritelist(String modifyipwritelist) {
		// TODO Auto-generated method stub
		return null;
	}

}
