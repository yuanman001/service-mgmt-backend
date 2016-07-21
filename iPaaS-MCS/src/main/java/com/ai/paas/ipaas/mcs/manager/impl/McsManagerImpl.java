package com.ai.paas.ipaas.mcs.manager.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.mcs.manage.rest.interfaces.IMemoryCacheServiceManager;
import com.ai.paas.ipaas.mcs.service.constant.McsConstants;
import com.ai.paas.ipaas.mcs.service.interfaces.IMcsDataSv;
import com.ai.paas.ipaas.mcs.service.interfaces.IMcsSv;
import com.ai.paas.ipaas.mcs.service.util.McsParamUtil;
import com.alibaba.dubbo.config.annotation.Service;


@Service
public class McsManagerImpl implements IMemoryCacheServiceManager {
	private static final Logger logger = LogManager.getLogger(McsManagerImpl.class
			.getName());
	@Autowired
	private IMcsSv iMcsSv;
	@Autowired
	private IMcsDataSv iMcsDataSv;

	@Override
	public String create(String createApply) {
		String res = null;
		try {
			res = iMcsSv.openMcs(createApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(createApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(createApply, res, null);
	}

	@Override
	public String cancel(String cancelApply) {
		String res = null;
		try {
			res = iMcsSv.cancelMcs(cancelApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(cancelApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(cancelApply, res, "cancel successfully!");
	}

	@Override
	public String modify(String modifyApply) {
		String res = null;
		try {
			res = iMcsSv.modifyMcs(modifyApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(modifyApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(modifyApply, res, "modify successfully!");
	}

	@Override
	public String start(String startApply) {
		String res = null;
		try {
			res = iMcsSv.startMcs(startApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(startApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(startApply, res, "start successfully!");
	}

	@Override
	public String stop(String stopApply) {
		String res = null;
		try {
			res = iMcsSv.stopMcs(stopApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(stopApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(stopApply, res, "stop successfully!");
	}

	@Override
	public String restart(String restartApply) {
		String res = null;
		try {
			res = iMcsSv.restartMcs(restartApply);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getReturn(restartApply, McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getReturn(restartApply, res,
				"restart successfully!");
	}

	@Override
	public String getFuncList() {
		return McsParamUtil.getFuncList();
	}

	public String get(String param) {
		String res = null;
		try {
			res = iMcsDataSv.get(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getDataReturn(McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getDataReturn(McsConstants.SUCCESS_FLAG, res);
	}

	public String del(String param) {
		String res = null;
		try {
			res = iMcsDataSv.del(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getDataReturn(McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getDataReturn(McsConstants.SUCCESS_FLAG, res);
	}

	public String clean(String param) {
		String res = null;
		try {
			res = iMcsDataSv.flushDb(param);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return McsParamUtil.getDataReturn(McsConstants.FAIL_FLAG,
					e.getMessage());
		}
		return McsParamUtil.getDataReturn(McsConstants.SUCCESS_FLAG, res);
	}

	@Override
	public String set(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String info(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
