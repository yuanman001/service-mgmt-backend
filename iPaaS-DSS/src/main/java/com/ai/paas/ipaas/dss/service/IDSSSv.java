package com.ai.paas.ipaas.dss.service;

import java.util.List;

import com.ai.paas.ipaas.dss.manage.param.ApplyDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CancelDSSParam;
import com.ai.paas.ipaas.dss.manage.param.CleanDSSParam;
import com.ai.paas.ipaas.dss.manage.param.DSSResult;
import com.ai.paas.ipaas.dss.manage.param.Func;
import com.ai.paas.ipaas.dss.manage.param.ModifyParam;
import com.ai.paas.ipaas.dss.manage.param.RecordParam;
import com.ai.paas.ipaas.dss.manage.param.RecordResult;
import com.ai.paas.ipaas.dss.manage.param.StatusParam;
import com.ai.paas.ipaas.dss.manage.param.StatusResult;
import com.ai.paas.ipaas.dss.manage.param.UploadParam;

public interface IDSSSv {

	/**
	 * 开通DSS服务
	 * 
	 * @param applyObj
	 * @return 开通结果
	 */
	public abstract DSSResult createDSS(ApplyDSSParam applyObj)
			throws Exception;

	/**
	 * 获取功能列表
	 * 
	 * @return
	 */
	public abstract List<Func> getFuncList() throws Exception;

	/**
	 * 注销DSS服务
	 * 
	 * @param applyObj
	 * @return 注销结果
	 */
	public abstract DSSResult cancelDSS(CancelDSSParam applyObj)
			throws Exception;

	/**
	 * 清理该service id 对应的 DSS所有文件服务
	 * 
	 * @param applyObj
	 * @return 清理结果
	 */
	public abstract DSSResult cleanDSS(CleanDSSParam applyObj) throws Exception;

	/**
	 * 清理该service id 对应的 DSS key所对应文件服务
	 * 
	 * @param applyObj
	 * @return 清理结果
	 */
	public abstract DSSResult cleanOneDSS(CleanDSSParam applyObj)
			throws Exception;

	/**
	 * 查询使用量及总容量
	 * 
	 * @param applyObj
	 * @return 状态信息
	 * @throws Exception
	 */
	public abstract StatusResult getStatusDSS(StatusParam applyObj)
			throws Exception;

	/**
	 * 根据KEY查询记录
	 * 
	 * @param applyObj
	 * @return 记录信息
	 * @throws Exception
	 */
	public abstract RecordResult getRecordDSS(RecordParam applyObj)
			throws Exception;

	/**
	 * 修改容量、文件限制大小
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 */
	public abstract DSSResult modifyDSS(ModifyParam applyObj) throws Exception;

	/**
	 * 上传文件
	 * 
	 * @param applyObj
	 * @return
	 * @throws Exception
	 */
	public abstract DSSResult uploadFile(UploadParam applyObj) throws Exception;
}
