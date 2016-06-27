package com.ai.paas.ipaas.mds.manage.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.ai.paas.ipaas.mds.manage.service.IMsgConfigHelper;

@Component
public class MsgConfigHelper implements IMsgConfigHelper {
	@Autowired
	private ICCSComponentManageSv configSv;

	@Override
	public void createConfigInfo(MdsUserTopic userTopic) throws PaasException {
		// 写发送方节点
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(userTopic.getProducerConfigPath());
		param.setPathType(PathType.READONLY);
		param.setUserId(userTopic.getUserId());
		if (!configSv.exists(param)) {
			configSv.add(param, userTopic.getProducerConfig());
		}
		param.setPath(userTopic.getConsumerConfigPath());
		param.setPathType(PathType.READONLY);
		param.setUserId(userTopic.getUserId());
		// 写接收方节点
		if (!configSv.exists(param)) {
			configSv.add(param, userTopic.getConsumerConfig());
		}
	}
	
	@Override
	public void createConsums(MdsUserSubscribe subscribe) throws PaasException {
		// 设置订阅者节点
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		String consumersPath = "/MDS/" + subscribe.getUserServIpaasId() + "/" +subscribe.getTopicEnName() + "/" 
		+ "consumers" + "/" + subscribe.getSubscribeName();
		param.setPath(consumersPath);
		param.setPathType(PathType.READONLY);
		param.setUserId(subscribe.getUserId());
		if (!configSv.exists(param)) {
			configSv.add(param, "");
		}
	}

	@Override
	public void removeConfigInfo(MdsUserTopic userTopic) throws PaasException {
		// 写发送方节点
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(userTopic.getProducerConfigPath());
		param.setPathType(PathType.READONLY);
		param.setUserId(userTopic.getUserId());
		if (configSv.exists(param)) {
			configSv.delete(param);
		}
		param.setPath(userTopic.getConsumerConfigPath());
		param.setPathType(PathType.READONLY);
		param.setUserId(userTopic.getUserId());
		// 写接收方节点
		if (configSv.exists(param)) {
			configSv.delete(param);
		}
		// 删除父节点
		String parentPath = userTopic.getProducerConfigPath();
		parentPath = parentPath.substring(0,
				parentPath.lastIndexOf(MDSConstant.UNIX_SEPERATOR));
		param.setPath(parentPath);
		param.setPathType(PathType.READONLY);
		param.setUserId(userTopic.getUserId());
		// 写接收方节点
		if (configSv.exists(param)) {
			configSv.delete(param);
		}
	}

	@Override
	public void adjustMsgOffSets(String path, String userId, long newOffSet)
			throws PaasException {
		// T// 写发送方节点
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(path);
		param.setPathType(PathType.WRITABLE);
		param.setUserId(userId);
		configSv.add(param, newOffSet + "");
	}

	@Override
	public String getTopicSendConf(String userId, String srvId, String topic)
			throws PaasException {
		String path = MDSConstant.SENDER_ROOT_PATH + srvId
				+ PaaSConstant.UNIX_SEPERATOR + topic + "/sender";
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(path);
		param.setPathType(PathType.READONLY);
		param.setUserId(userId);
		return configSv.get(param);
	}
}
