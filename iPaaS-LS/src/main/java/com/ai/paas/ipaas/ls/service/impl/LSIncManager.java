package com.ai.paas.ipaas.ls.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.common.service.IOrgnizeUserHelper;
import com.ai.paas.ipaas.ls.manage.rest.interfaces.ILSIncManager;
import com.ai.paas.ipaas.ls.service.constant.ResponseResultMark;
import com.ai.paas.ipaas.ls.service.transfer.vo.CancelLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.CancelLSResult;
import com.ai.paas.ipaas.ls.service.transfer.vo.CreateLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.CreateLSResult;
import com.ai.paas.ipaas.ls.service.transfer.vo.ModifyLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.ModifyLSResult;
import com.ai.paas.ipaas.ls.service.transfer.vo.RestartLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.RestartLSResult;
import com.ai.paas.ipaas.ls.service.transfer.vo.StartLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.StartLSResult;
import com.ai.paas.ipaas.ls.service.transfer.vo.StopLS;
import com.ai.paas.ipaas.ls.service.transfer.vo.StopLSResult;
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
		CreateLS createObj = g.getGson().fromJson(createApply, CreateLS.class);
		
		
		CreateLSResult createResult = new CreateLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(createResult);
	}

	@Override
	public String cancel(String cancelApply) {
		CancelLS cancelObj = g.getGson().fromJson(cancelApply, CancelLS.class);
		
		
		CancelLSResult cancelResult = new CancelLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(cancelResult);
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String startApply) {
		StartLS startObj = g.getGson().fromJson(startApply, StartLS.class);
		
		
		StartLSResult startResult = new StartLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(startResult);
	}

	@Override
	public String stop(String stopApply) {
		StopLS stopObj = g.getGson().fromJson(stopApply, StopLS.class);
		
		
		StopLSResult stopResult = new StopLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(stopResult);
	}

	@Override
	public String restart(String restartApply) {
		RestartLS restartObj = g.getGson().fromJson(restartApply, RestartLS.class);
		
		
		RestartLSResult restartResult = new RestartLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(restartResult);
	}

	@Override
	public String getinstanceinfo(String getinstanceinfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String modify) {
		ModifyLS modifyObj = g.getGson().fromJson(modify, ModifyLS.class);
		
		
		ModifyLSResult modifyResult = new ModifyLSResult(ResponseResultMark.WARN_INIT_STATUS);
		return g.getGson().toJson(modifyResult);
	}
}
