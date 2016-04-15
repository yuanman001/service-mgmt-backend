package com.ai.paas.ipaas.ats.manage.services;

import static com.ai.paas.ipaas.txs.util.Constants.ERROR_CODE;
import static com.ai.paas.ipaas.txs.util.Constants.ERROR_TOPIC;
import static com.ai.paas.ipaas.txs.util.Constants.RESULT_CODE;
import static com.ai.paas.ipaas.txs.util.Constants.RESULT_MSG;
import static com.ai.paas.ipaas.txs.util.Constants.SIGNATURE_ID;
import static com.ai.paas.ipaas.txs.util.Constants.SUCCESS_CODE;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ats.dao.interfaces.AtsInstMapper;
import com.ai.paas.ipaas.ats.dao.interfaces.AtsResourcePoolMapper;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsInst;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsInstCriteria;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsResourcePool;
import com.ai.paas.ipaas.ats.dao.mapper.bo.AtsResourcePoolCriteria;
import com.ai.paas.ipaas.ats.manage.rest.interfaces.IAsynchronousTansactionServiceManager;
import com.ai.paas.ipaas.ats.util.ATSResultWrapper;
import com.ai.paas.ipaas.ats.util.ATSValidator;
import com.ai.paas.ipaas.ats.util.MsgSrv;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.mds.MDSConstant;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsResourcePool;
import com.ai.paas.ipaas.mds.manage.service.IMsgConfigHelper;
import com.ai.paas.ipaas.mds.manage.service.IMsgKafkaHelper;
import com.ai.paas.ipaas.mds.manage.util.MDSResultWrapper;
import com.ai.paas.ipaas.mds.manage.util.MDSValidator;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.mds.manage.vo.MsgSrvUsageApplyResult;
import com.ai.paas.ipaas.txs.component.TxsComponent;
import com.ai.paas.ipaas.txs.dao.interfaces.TxsInstMapper;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInst;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInstCriteria;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class AsynchronousTransactionServiceManager implements IAsynchronousTansactionServiceManager {
	private static final Logger logger = LoggerFactory.getLogger(AsynchronousTransactionServiceManager.class);

	@Autowired
	private SqlSessionTemplate template;

	@Autowired
	private TxsComponent txsComponent;

	@Autowired
	IMsgKafkaHelper msgKafkaHelper;

	@Autowired
	IMsgConfigHelper msgConfigHelper;

	@Autowired
	ICCSComponentManageSv configSv;
	
	@Override
	public String cancel(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(String param) {
		Gson gson = new Gson();
		String code = SUCCESS_CODE;
		String message = "";
		JsonObject paramJson = new JsonObject();
		try {
			logger.info("ATS create param:{}", param);

			paramJson = gson.fromJson(param, JsonObject.class);
			Assert.notNull(paramJson, "Param :" + param + " is malformed");
			paramJson.addProperty(SIGNATURE_ID, create(paramJson));
		} catch (RuntimeException e) {
			logger.info("ATS runtime exception param:{}", param, e);
			code = ERROR_CODE;
			message = e.getMessage();
		} catch (Throwable a) {
			logger.error("ATS exception create param:{}", param, a);
			code = ERROR_CODE;
			message = a.getMessage();
		}
		paramJson.addProperty(RESULT_CODE, code);
		paramJson.addProperty(RESULT_MSG, message);
		return gson.toJson(paramJson);
	}

	private String create(JsonObject paramJson) throws Throwable {
		String[] identify = txsComponent.validateParam(paramJson);
		String userId = identify[0];
		String serviceId = identify[1];

		TxsInstMapper instMapper = template.getMapper(TxsInstMapper.class);
		TxsInstCriteria txsInstCriteria = new TxsInstCriteria();
		txsInstCriteria.createCriteria().andUserIdEqualTo(userId).andServiceIdEqualTo(serviceId).andInstStateEqualTo(0);
		List<TxsInst> txsInstList = instMapper.selectByExample(txsInstCriteria);
		if (txsInstList == null || txsInstList.size() < 1) {
			throw new RuntimeException("Please first create txs service");
		}

		AtsResourcePoolMapper mapper = template.getMapper(AtsResourcePoolMapper.class);
		AtsResourcePoolCriteria apc = new AtsResourcePoolCriteria();
		apc.createCriteria().andTxsResourceIdEqualTo(txsInstList.get(0).getTxsResourceId()).andResourceStateEqualTo(0);
		List<AtsResourcePool> atsResourcePoolList = mapper.selectByExample(apc);
		if (atsResourcePoolList == null || atsResourcePoolList.size() < 1) {
			throw new RuntimeException("can't fetch ats_resource_pool");
		}

		AtsResourcePool resource = atsResourcePoolList.get(0);

		String topic = SIGNATURE_ID + "-" + UUID.randomUUID().toString();

		String errorTopic = ERROR_TOPIC + "-" + topic;

		String topicPath = resource.getZkTopicPath() + "/" + topic;
		txsComponent.createNode(userId, serviceId, resource.getZkProducerPath(), resource.getZkProducerNode());
		txsComponent.createNode(userId, serviceId, resource.getZkConsumerPath(), resource.getZkConsumerNode());
		txsComponent.createNode(userId, serviceId, topicPath, topic);
		String command = resource.getKafkaCreateCommand() + " --topic " + topic;
		txsComponent.createTopic(command);

		/** 创建异常队列 */
		String command2 = resource.getKafkaCreateCommand() + " --topic " + errorTopic;
		txsComponent.createTopic(command2);

		AtsInstMapper atsInstMapper = template.getMapper(AtsInstMapper.class);
		AtsInst atsInst = new AtsInst();
		BeanUtils.copyProperties(atsInst, resource);
		atsInst.setKafkaCreateCommand(command);
		atsInst.setUserId(userId);
		atsInst.setServiceId(serviceId);
		atsInst.setZkTopicPath(topicPath);
		atsInst.setInstState(0);
		atsInstMapper.insertSelective(atsInst);
		logger.debug("Created txs inst,user_id: {} service_id：{}", userId, serviceId);

		/** 异常队列入库 */
		AtsInst atsInst2 = new AtsInst();
		BeanUtils.copyProperties(atsInst2, atsInst);
		atsInst2.setKafkaCreateCommand(command2);
		// 前台通过数据库不能查看
		atsInst2.setInstState(1);
		atsInstMapper.insertSelective(atsInst2);
		logger.debug("Created error_txs inst,user_id: {} service_id：{}", userId, serviceId);

		return topic;
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String param) {
		// TODO Auto-generated method stub
		return null;
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
	public String getTopicUsage(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply paramter is null!", null);
		}
		MsgSrv apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrv.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply paramter is illegel json!", null);
		}
		changeToErrorMessage(apply);
		
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply paramter has null values!", apply);
		}
		List<MsgSrvUsageApplyResult> usages = null;
		try {
			usages = this.getTopicUsage(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager topic usage error!", e);
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Message topic usage apply failed!" + e.getMessage(), apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Message topic usage apply success!", apply, usages);
	}

	public void changeToErrorMessage(MsgSrv apply){
		if(apply.getMessageType()==1)
		{
			StringBuffer topicEnName=new StringBuffer();
			topicEnName.append("error-");
			topicEnName.append(apply.getTopicEnName());
			apply.setTopicEnName(topicEnName.toString());
		}
	}
	
	private List<MsgSrvUsageApplyResult> getTopicUsage(MsgSrvApply apply) throws PaasException {
		AtsInstMapper instMapper = template.getMapper(AtsInstMapper.class);
		AtsInstCriteria atxInstCriteria = new AtsInstCriteria();
		atxInstCriteria.createCriteria().andUserIdEqualTo(apply.getUserId()).andServiceIdEqualTo(apply.getServiceId())
				.andKafkaCreateCommandLike("%" + apply.getTopicEnName() + "%");
		List<AtsInst> atsInstList = instMapper.selectByExample(atxInstCriteria);
		if (atsInstList == null || atsInstList.size() < 1) {
			throw new RuntimeException("Please first create ats service");
		}

		AtsInst atsIns = atsInstList.get(0);

		// kafka address
		JsonObject kafkaJson = new Gson().fromJson(atsIns.getZkProducerNode(), JsonObject.class);
		JsonObject zkJson = new Gson().fromJson(atsIns.getZkConsumerNode(), JsonObject.class);

		String kafkaAddress = null;
		if (kafkaJson.has("metadata.broker.list")) {
			kafkaAddress = kafkaJson.get("metadata.broker.list").getAsString();
		} else {
			throw new RuntimeException("Please config metadata.broker.list value");
		}

		String zkAddress = null;
		if (zkJson.has("kafka.zookeeper.hosts")) {
			zkAddress = zkJson.get("kafka.zookeeper.hosts").getAsString();
		} else {
			throw new RuntimeException("Please config kafka.zookeeper.hosts value");
		}

		// 生成kafka信息
		MdsResourcePool kafkaCluster = new MdsResourcePool();
		kafkaCluster.setKafkaAddress(kafkaAddress);
		kafkaCluster.setZkAddress(zkAddress);

		return msgKafkaHelper.getTopicOffsets(apply.getUserId(), apply.getServiceId(), apply.getTopicEnName(),
				kafkaCluster);
	}

	@Override
	public String getTopicMessage(String topicApply) {
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(topicApply)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply paramter is null!", null);
		}
		MsgSrv apply = null;
		try {
			apply = gson.fromJson(topicApply, MsgSrv.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message  apply paramter is illegel json!", null);
		}
		changeToErrorMessage(apply);
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply paramter has null values!", apply);
		}
		String message = null;
		try {
			message = this.getTopicMessage(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager get topic message error!", e);
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply failed!" + e.getMessage(), apply);
		}
		if (null == message) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Get topic message apply failed!", apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"Get topic message apply success!", apply, message);
	}

	private String getTopicMessage(MsgSrvApply apply) throws PaasException {
		AtsInstMapper instMapper = template.getMapper(AtsInstMapper.class);
		AtsInstCriteria atxInstCriteria = new AtsInstCriteria();
		atxInstCriteria.createCriteria().andUserIdEqualTo(apply.getUserId()).andServiceIdEqualTo(apply.getServiceId())
				.andKafkaCreateCommandLike("%" + apply.getTopicEnName() + "%");
		List<AtsInst> atsInstList = instMapper.selectByExample(atxInstCriteria);
		if (atsInstList == null || atsInstList.size() < 1) {
			throw new RuntimeException("Please first create ats service");
		}

		AtsInst atsIns = atsInstList.get(0);

		// kafka address
		JsonObject kafkaJson = new Gson().fromJson(atsIns.getZkProducerNode(), JsonObject.class);
		JsonObject zkJson = new Gson().fromJson(atsIns.getZkConsumerNode(), JsonObject.class);

		String kafkaAddress = null;
		if (kafkaJson.has("metadata.broker.list")) {
			kafkaAddress = kafkaJson.get("metadata.broker.list").getAsString();
		} else {
			throw new RuntimeException("Please config metadata.broker.list value");
		}

		String zkAddress = null;
		if (zkJson.has("kafka.zookeeper.hosts")) {
			zkAddress = zkJson.get("kafka.zookeeper.hosts").getAsString();
		} else {
			throw new RuntimeException("Please config kafka.zookeeper.hosts value");
		}

		// 生成kafka信息
		MdsResourcePool kafkaCluster = new MdsResourcePool();
		kafkaCluster.setKafkaAddress(kafkaAddress);
		kafkaCluster.setZkAddress(zkAddress);

		return msgKafkaHelper.getTopicMessage(kafkaCluster, apply.getTopicEnName(), apply.getPartition(),
				apply.getOffset());
	}

	@Override
	public String skip(String param) {
		logger.info("ATS skip method,param is :"+param);
		Gson gson = new Gson();
		if (MDSValidator.isNullParam(param)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply paramter is null!", null);
		}
		MsgSrv apply = null;
		try {
			apply = gson.fromJson(param, MsgSrv.class);
		} catch (Exception e) {
			// 转换有错误
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset  apply paramter is illegel json!", null);
		}
		if(apply.getMessageType()==0)
		{
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"normal message can not be skip!", null);
		}
		changeToErrorMessage(apply);
		// apply 验证
		if (MDSValidator.isNullApply(apply)) {
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply paramter has null values!", apply);
		}
		if(apply.getMessageType()==0)
		{
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"cannot skip normal topic", apply);
		}
		
		try {
			adjustTopicOffset(apply);
		} catch (Exception e) {
			logger.error("MessageServiceManager adjust topic offset error!", e);
			return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"Adjust topic offset apply failed!" + e.getMessage(), apply);
		}
		// 成功了
		return MDSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"skip topic message  success!", apply);
	}

	public Long adjustMsgOffSets(String path, String userId, long newOffSet)
			throws PaasException {
		// T// 写发送方节点
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(path);
		param.setPathType(PathType.WRITABLE);
		param.setUserId(userId);
		Long instance=null;
		if(configSv.exists(param))
		{
			String value=configSv.get(param);
			instance=new Long(value);
			instance+=newOffSet;
		}else{
			instance=new Long(0L);
		}
		return instance;
	}
	
	public void adjustTopicOffset(MsgSrvApply msgTopicApply) {
		// 得到路径
		String offsetPath = MDSConstant.CONSUMER_ROOT_PATH + msgTopicApply.getServiceId() + PaaSConstant.UNIX_SEPERATOR
				+ msgTopicApply.getTopicEnName() + PaaSConstant.UNIX_SEPERATOR + "consumer"
				+ PaaSConstant.UNIX_SEPERATOR + "offsets" + PaaSConstant.UNIX_SEPERATOR + "partition_"
				+ msgTopicApply.getPartition() + PaaSConstant.UNIX_SEPERATOR + "adjusted_offset";
		try {
			Long value=adjustMsgOffSets(offsetPath, msgTopicApply.getUserId(), msgTopicApply.getOffset());
			List<MsgSrvUsageApplyResult> usages =  this.getTopicUsage(msgTopicApply);
			int partition=msgTopicApply.getPartition();
			for(MsgSrvUsageApplyResult instance:usages)
			{
				if(instance.getPartitionId()==partition)
				{
					long consumedOffset=instance.getConsumedOffset()+1;
					if(consumedOffset>value)
					{
						value=consumedOffset;
					}
					if(value>instance.getTotalOffset())
					{
						throw new PaasRuntimeException("skip value is larger than total offset");
					}
				}
			}
			msgConfigHelper.adjustMsgOffSets(offsetPath, msgTopicApply.getUserId(),value);
		} catch (PaasException ex) {
			throw new PaasRuntimeException("Adjust Topic:" + msgTopicApply.getTopicEnName() + ",partition:"
					+ msgTopicApply.getPartition() + " Offset: " + msgTopicApply.getOffset());
		}
	}

	@Override
	public String resend(String param) {
		logger.info("message:resend,param :"+param);
		Gson gson = new Gson();
		if (ATSValidator.isNullParam(param)) {
			return ATSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL, "apply param is null");
		}
		Properties props = gson.fromJson(param, Properties.class);
		Integer instState =new Integer((String) props.get("messageType"));
		if(instState==0)
		{
			return ATSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"cannot resend normal topic" );
		}
		String topicEnName = props.getProperty("topicEnName");
		StringBuilder path = new StringBuilder();
		path.append("/dtm/local/message-customer/topic/");
		path.append(topicEnName);
		AtsInstMapper instMapper = template.getMapper(AtsInstMapper.class);
		AtsInstCriteria atxInstCriteria = new AtsInstCriteria();
		atxInstCriteria.createCriteria().andUserIdEqualTo(props.getProperty("userId"))
				.andZkTopicPathEqualTo(path.toString()).andInstStateEqualTo(1);
		List<AtsInst> atsInstList = instMapper.selectByExample(atxInstCriteria);
		if (atsInstList == null || atsInstList.size() < 1) {
			throw new RuntimeException("Please first create ats service");
		}
		AtsInst atsIns = atsInstList.get(0);
		String zkProducerNode = atsIns.getZkProducerNode();
		Integer partition = new Integer(props.getProperty("partition"));
		byte[] message = props.getProperty("message").getBytes();

		try {
			StringBuffer topic=new StringBuffer();
			topic.append("error-");
			topic.append(topicEnName);
			msgKafkaHelper.sendMessage(topic.toString(), partition.intValue(), zkProducerNode, message);
		} catch (Exception e) {
			logger.error("resend topic message   error!", e);
			return ATSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL,
					"resend topic message   failed!" + e.getMessage());
		}
		// 成功了
		return ATSResultWrapper.wrapRestfulResult(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS,
				"resend topic message  success!");

	}
}
