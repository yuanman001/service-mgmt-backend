package com.ai.paas.ipaas.idps.manager.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.idps.manage.rest.interfaces.ImageDynProcServiceManager;
import com.ai.paas.ipaas.idps.service.constant.IdpsConstants;
import com.ai.paas.ipaas.idps.service.interfaces.IIdpsSv;
import com.ai.paas.ipaas.idps.service.util.IdpsParamUtil;
import com.alibaba.dubbo.config.annotation.Service;
/**
 *  Image dynamic Proccessing Service
 *
 */
@Service
public class ImageDynProcServiceManagerImpl implements ImageDynProcServiceManager {
	private static final Logger LOG = LogManager
			.getLogger(ImageDynProcServiceManagerImpl.class.getName());
	@Autowired
	private IIdpsSv iIdpsSv;
	@Override
	public String create(String createApply) {
		String res = null;
		String isUpgrade = "no";
		try {
			res = iIdpsSv.open(createApply,isUpgrade);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(createApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(createApply, res,null);
	}

	@Override
	public String cancel(String cancelApply) {
		String res = null;
		try {
			res = iIdpsSv.cancel(cancelApply);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(cancelApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(cancelApply, res,"cancel successfully!");
	}

	@Override
	public String modify(String modifyApply) {
		String res = null;
		try {
			res = iIdpsSv.modify(modifyApply);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(modifyApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(modifyApply, res,"modify successfully!");
	}

	@Override
	public String start(String startApply) {
		String res = null;
		try {
			res = iIdpsSv.start(startApply);
			startApply = startApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		} catch (Exception e) {
			startApply = startApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(startApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(startApply, res,"start successfully!");
	}

	@Override
	public String stop(String stopApply) {
		String res = null;
		try {
			res = iIdpsSv.stop(stopApply);
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
			return IdpsParamUtil.getReturn(stopApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(stopApply, res,"stop successfully!");
	}

	@Override
	public String restart(String restartApply) {
		String res = null;
		try {
			res = iIdpsSv.restart(restartApply);
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(restartApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(restartApply, res,"restart successfully!");
	}

	@Override
	public String getFuncList() {
		return IdpsParamUtil.getFuncList();
	}

	@Override
	public String clean(String stopApply) {
		String destroy = "no";
		String res = null;
		try {
			res = iIdpsSv.clean(stopApply,destroy);
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		} catch (Exception e) {
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(stopApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(stopApply, res,"stop successfully!");
	}

	@Override
	public String upgrade(String createApply) {
		String res = null;
		String isUpgrade = "yes";
		try {
			res = iIdpsSv.open(createApply,isUpgrade);
			createApply = createApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		} catch (Exception e) {
			createApply = createApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(createApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(createApply, res,"upgrade successfully!");
	}

	@Override
	public String destroy(String stopApply) {
		String destroy = "yes";
		String res = null;
		try {
			res = iIdpsSv.clean(stopApply,destroy);
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
		} catch (Exception e) {
			stopApply = stopApply.replaceAll("[{]", "{\"").replaceAll("[:]", "\":\"").replaceAll("[,]", "\",\"").replaceAll("[}]", "\"}");
			LOG.error(e.getMessage(),e);
			return IdpsParamUtil.getReturn(stopApply, IdpsConstants.FAIL_FLAG,e.getMessage());
		}
		return IdpsParamUtil.getReturn(stopApply, res,"stop successfully!");
	}
	
	
	

	
}
	