package com.ai.paas.ipaas.dss.manage.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CancelDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CleanDSSParam;
import com.ai.paas.ipaas.dss.manage.param.DSSResult;
import com.ai.paas.ipaas.dss.manage.param.ModifyParam;
import com.ai.paas.ipaas.dss.manage.param.RecordParam;
import com.ai.paas.ipaas.dss.manage.param.StatusParam;
import com.ai.paas.ipaas.dss.manage.param.UploadParam;
import com.ai.paas.ipaas.dss.manage.rest.interfaces.IDocumentStoreServiceManager;
import com.ai.paas.ipaas.dss.service.IDSSSv;
import com.alibaba.dubbo.config.annotation.Service;

@Service
public class DocumentStoreServiceManagerImpl implements
		IDocumentStoreServiceManager {

	@Autowired
	private IDSSSv dssSv;

	private static final Logger log = LogManager
			.getLogger(DocumentStoreServiceManagerImpl.class.getName());

	@Override
	public String cancel(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		CancelDSSParam cancelObj = DSSHelper.getCancelDSSParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.cancelDSS(cancelObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(cancelObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String create(String param) {
		// 格式化入参
		ApplyDSSParam applyObj = DSSHelper.getRequestParam(param);
		// 创建DSS并初始化出参
		DSSResult dssResult = null;
		try {
			dssResult = dssSv.createDSS(applyObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(applyObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String getFuncList() {
		Object dssResult = null;
		// 获取功能列表
		try {
			dssResult = dssSv.getFuncList();
		} catch (Exception e) {
			// 构建错误信息
			log.error("", e);
			dssResult = DSSHelper.getResult(e.getMessage());
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);

	}

	@Override
	public String modify(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		ModifyParam recordObj = DSSHelper.getModifyParamm(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.modifyDSS(recordObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(recordObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String restart(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cleanAll(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		CleanDSSParam cleanObj = DSSHelper.getCleanDSSParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.cleanDSS(cleanObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(cleanObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String cleanOne(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		CleanDSSParam cleanObj = DSSHelper.getCleanDSSParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.cleanOneDSS(cleanObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(cleanObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String getStatus(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		StatusParam statusObj = DSSHelper.getStatusParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.getStatusDSS(statusObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(statusObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String getRecord(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		RecordParam recordObj = DSSHelper.getRecordParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.getRecordDSS(recordObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(recordObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

	@Override
	public String upload(String param) {
		// 初始化结果
		DSSResult dssResult = null;
		// 格式化入参
		UploadParam recordObj = DSSHelper.getUploadParam(param);
		// 创建DSS并初始化出参
		try {
			dssResult = dssSv.uploadFile(recordObj);
		} catch (Exception e) {
			// 构建错误信息
			dssResult = DSSHelper.getResult(recordObj, e.getMessage());
			log.error("", e);
		}
		// 返回处理结果
		return DSSHelper.getDSSResult(dssResult);
	}

}
