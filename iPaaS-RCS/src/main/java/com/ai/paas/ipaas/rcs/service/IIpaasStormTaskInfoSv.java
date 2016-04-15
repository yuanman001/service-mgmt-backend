package com.ai.paas.ipaas.rcs.service;

import java.util.List;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfo;
import com.ai.paas.ipaas.rcs.vo.PageEntity;
import com.ai.paas.ipaas.rcs.vo.PageResult;
import com.ai.paas.ipaas.rcs.vo.StormTaskInfoVo;

/**
 * @author dongteng
 */
public interface IIpaasStormTaskInfoSv {
	/**
	 * 注册计算任务
	 */
	public String registerTask(RcsTaskInfo rcsTaskInfo,
			List<RcsSpoutInfo> rcsSpoutInfos,
			List<RcsBoltInfo> rcsBoltInfos) throws PaasException;

	/**
	 * 注册计算编辑
	 */
	public void editTask(RcsTaskInfo rcsTaskInfo,
			List<RcsSpoutInfo> rcsSpoutInfos,
			List<RcsBoltInfo> rcsBoltInfos) throws PaasException;

	/**
	 * 分页查询
	 */
	public abstract PageResult<StormTaskInfoVo> searchPage(PageEntity pageEntity);
	
	/**
	 * 通过主键查找
	 */
	public RcsTaskInfo searchOneById(String id);
	
	/**
	 * 更新
	 */
	
	public int update(RcsTaskInfo rcsTaskInfo);
}
