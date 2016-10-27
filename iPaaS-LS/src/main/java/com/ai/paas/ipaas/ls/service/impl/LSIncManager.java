package com.ai.paas.ipaas.ls.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.common.service.IOrgnizeUserHelper;
import com.ai.paas.ipaas.ls.manage.rest.interfaces.ILSIncManager;
import com.ai.paas.ipaas.ls.service.util.GsonSingleton;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年10月27日 下午4:42:04 
 * @version 
 * @since  
 */
@Service
@Transactional(rollbackFor = Exception.class) // 事务可以正常使用
public class LSIncManager implements ILSIncManager {
	
	private static transient final Logger LOG = LoggerFactory.getLogger(LSIncManager.class);

	@Autowired
	GsonSingleton g;

	@Autowired
	ICCSComponentManageSv iCCSComponentManageSv;
	
	@Autowired
	private IOrgnizeUserHelper orgnizeUserHelper;

	@Override
	public String create(String createApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancel(String cancelApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String startApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String stopApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String restart(String restartApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstanceinfo(String getinstanceinfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String modify) {
		// TODO Auto-generated method stub
		return null;
	}
}
