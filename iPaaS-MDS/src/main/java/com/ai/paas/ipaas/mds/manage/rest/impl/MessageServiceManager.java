package com.ai.paas.ipaas.mds.manage.rest.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserSubscribe;
import com.ai.paas.ipaas.mds.manage.rest.interfaces.IMessageServiceManager;
import com.ai.paas.ipaas.mds.manage.service.IMsgSrvManager;
import com.ai.paas.ipaas.mds.manage.util.MDSResultWrapper;
import com.ai.paas.ipaas.mds.manage.util.MDSValidator;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

@Service
public class MessageServiceManager implements IMessageServiceManager {
	private static transient final Logger logger = LoggerFactory
			.getLogger(MessageServiceManager.class);

	@Autowired
	private IMsgSrvManager msgSrvManager;

	@Override
	public String getFuncList() {
		return null;
	}

	@Override
	public String create(String createApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(createApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create service apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(createApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create service apply paramter is illegel json!",
					null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create service apply paramter has null values!",
					apply);
		}
		if (MDSValidator.isIllegalApply(apply)) {
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message create service apply paramter value is illegel,partition or replica must be great than 0!",
							apply);
		}
		try {
			msgSrvManager.createMessageService(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager create error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create service apply failed!" + e.getMessage(),
					apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message create service apply success!", apply);
	}

	@Override
	public String cancel(String cancelApply) {
		// 注销服务，资源回收
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(cancelApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message cancel service apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(cancelApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message cancel service apply paramter is illegel json!",
					null);
		}
		// apply 验证
		if (MDSValidator.isNullCanelApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message cancel service apply paramter has null values!",
					apply);
		}
		try {
			msgSrvManager.cancelMessageService(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager cancel error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message cancel service apply failed!" + e.getMessage(),
					apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message cancel service apply success!", apply);
	}

	@Override
	public String modify(String modifyApply) {
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
				"modify function is not surported!", null);
	}

	@Override
	public String start(String startApply) {
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
				"start function is not surported!", null);
	}

	@Override
	public String stop(String stopApply) {
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
				"stop function is not surported!", null);
	}

	@Override
	public String restart(String restartApply) {
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
				"restart function is not surported!", null);
	}

	@Override
	public String createTopic(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic create apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic create apply paramter is illegel json!",
					null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic create apply paramter has null values!",
					apply);
		}
		if (MDSValidator.isIllegalApply(apply)) {
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message topic create apply paramter value is illegel,partition or replica must be great than 0!",
							apply);
		}

		try {
			msgSrvManager.createTopic(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager create topic error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic create apply failed!" + e.getMessage(),
					apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message topic create apply success!", apply);
	}

	@Override
	public String removeTopic(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic remove apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic remove apply paramter is illegel json!",
					null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic remove apply paramter has null values!",
					apply);
		}

		try {
			msgSrvManager.removeTopic(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager create topic error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic remove apply failed!" + e.getMessage(),
					apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message topic remove apply success!", apply);
	}

	@Override
	public String getTopicUsage(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message topic usage apply paramter is illegel json!",
							null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply paramter has null values!",
					apply);
		}
		List<MsgSrvUsageApplyResult> usages = null;
		try {
			usages = msgSrvManager.getTopicUsage(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager topic usage error!", e);
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message topic usage apply failed!"
									+ e.getMessage(), apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message topic usage apply success!", apply, usages);
	}

	@Override
	public String getTopicMessage(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message  apply paramter is illegel json!", null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply paramter has null values!", apply);
		}
		String message = null;
		try {
			message = msgSrvManager.getTopicMessage(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager get topic message error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply failed!" + e.getMessage(), apply);
		}
		if (null == message) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply failed!", apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Get topic message apply success!", apply, message);
	}

	@Override
	public String adjustTopicOffset(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset  apply paramter is illegel json!",
					null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply paramter has null values!",
					apply);
		}
		try {
			msgSrvManager.adjustTopicOffset(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager adjust topic offset error!", e);
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Adjust topic offset apply failed!"
									+ e.getMessage(), apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Get topic message apply success!", apply);
	}

	@Override
	public String sendMessage(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Send topic message apply paramter is null!", null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Send topic message  apply paramter is illegel json!",
							null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper
					.wrapRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Send topic message apply paramter has null values!",
							apply);
		}
		String message = null;
		try {
			msgSrvManager.sendMessage(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager send topic message error!", e);
			return MDSResultWrapper.wrapRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Send topic message apply failed!" + e.getMessage(), apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Send topic message apply success!", apply, message);
	}

	//创建订阅信息
	@Override
	public String createSubscribe(String subscribeApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(subscribeApply)) {
			return MDSResultWrapper.wrapSubRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create subscribe apply paramter is null!", null);
		}
		MdsUserSubscribe apply = null;
		try {
			apply = gson.fromJson(subscribeApply, MdsUserSubscribe.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper
					.wrapSubRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Send subscribe message  apply paramter is illegel json!",
							apply);
		}
		try {
			msgSrvManager.createSubscribe(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager create error!", e);
			return MDSResultWrapper.wrapSubRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message create subscribe apply failed!" + e.getMessage(),
					apply);
		}
		// 成功了
		return MDSResultWrapper.wrapSubRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message create subscribe apply success!", null);
	}

	//查询订阅信息（1.订阅者不能是consumer。2.同一个topic下不能有相同的订阅者）
	@Override
	public String getSubscribe(String subscribeApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(subscribeApply)) {
			return MDSResultWrapper.wrapSubRestfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message subscribe usage apply paramter is null!", null);
		}
		MdsUserSubscribe apply = null;
		try {
			apply = gson.fromJson(subscribeApply, MdsUserSubscribe.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper
					.wrapSubRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message subscribe usage apply paramter is illegel json!",
							apply);
		}
		List<MdsUserSubscribe> subs = null;
		String isExis = null;
		try {
			subs = msgSrvManager.getSubscribe(apply);
			if(null == subs || subs.isEmpty()){
				isExis = "no";
			}else{
				isExis = "yes";
			}
		} catch (Exception e) {
			logger.error("MessageServiceManager topic usage error!", e);
			return MDSResultWrapper
					.wrapSubRestfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message subscribe usage apply failed!"
									+ e.getMessage(), apply);
		}
		
		// 成功了
		return MDSResultWrapper.wrapSubRestfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message subscribe usage apply success!", null, isExis);
	}
	
	//获得消息队列下所有的消费者
	@Override
	public String getListSubPath(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wraplistSubPathfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message getListSubPath apply paramter is null!", null,null);
		}
		MsgSrvApply apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrvApply.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper
					.wraplistSubPathfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message getListSubPath apply paramter is illegel json!",
							null,null);
		}
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wraplistSubPathfulResult(
					PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message getListSubPath apply paramter has null values!",
					apply,null);
		}
		List<String> listSubPath = null;
		try {
			listSubPath = msgSrvManager.getListSubPath(apply);
		} catch (Exception e) {
			logger.error("Message getListSubPath apply error!", e);
			return MDSResultWrapper
					.wraplistSubPathfulResult(
							PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
							"Message getListSubPath apply failed!"
									+ e.getMessage(), apply,null);
		}
		// 成功了
		return MDSResultWrapper.wraplistSubPathfulResult(
				PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message getListSubPath apply success!", apply, listSubPath);
	}

}
