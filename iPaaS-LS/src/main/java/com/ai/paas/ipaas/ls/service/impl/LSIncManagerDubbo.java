package com.ai.paas.ipaas.ls.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.ls.manage.rest.interfaces.ILSIncManager;
import com.alibaba.dubbo.config.annotation.Service;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年10月27日 下午4:37:49 
 * @version 
 * @since  
 */
@Service
public class LSIncManagerDubbo implements ILSIncManager {

	@Autowired
	private LSIncManager lsIncManager;
	
	@Override
	public String create(String createApply) {
		// TODO Auto-generated method stub
		return lsIncManager.create(createApply);
	}

	@Override
	public String cancel(String cancelApply) {
		// TODO Auto-generated method stub
		return lsIncManager.cancel(cancelApply);
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return lsIncManager.getFuncList();
	}

	@Override
	public String start(String startApply) {
		// TODO Auto-generated method stub
		return lsIncManager.start(startApply);
	}

	@Override
	public String stop(String stopApply) {
		// TODO Auto-generated method stub
		return lsIncManager.stop(stopApply);
	}

	@Override
	public String restart(String restartApply) {
		// TODO Auto-generated method stub
		return lsIncManager.restart(restartApply);
	}

	@Override
	public String getinstanceinfo(String getinstanceinfo) {
		// TODO Auto-generated method stub
		return lsIncManager.getinstanceinfo(getinstanceinfo);
	}

	@Override
	public String modify(String modify) {
		// TODO Auto-generated method stub
		return lsIncManager.modify(modify);
	}

}
