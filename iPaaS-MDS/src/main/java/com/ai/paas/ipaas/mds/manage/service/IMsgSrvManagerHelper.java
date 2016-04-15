package com.ai.paas.ipaas.mds.manage.service;

import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserService;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;

public interface IMsgSrvManagerHelper {


	/**
	 * 获取一个用户服务
	 * 
	 * @param msgSrvApply
	 * @return
	 */
	public MdsUserService getUserService(MsgSrvApply msgSrvApply);

	/**
	 * 获取用户消息队列定义
	 * 
	 * @param msgSrvApply
	 * @return
	 */
	public MdsUserTopic getUserTopic(MsgSrvApply msgSrvApply);

	/**
	 * 获取正在启用的消息队列定义
	 * @param msgSrvApply
	 * @return
	 */
	public MdsUserTopic getEnabledUserTopic(MsgSrvApply msgSrvApply);

	/**
	 * 新建一个用户服务对象
	 * 
	 * @param msgSrvApply
	 * @return
	 */
	public MdsUserService prepareUserServiceData(MsgSrvApply msgSrvApply);

	/**
	 * 新建一个用户队列对象
	 * 
	 * @param msgSrvApply
	 * @param userService
	 * @return
	 */
	public MdsUserTopic prepareUserTopicData(MsgSrvApply msgSrvApply,
			MdsUserService userService);

	
}
