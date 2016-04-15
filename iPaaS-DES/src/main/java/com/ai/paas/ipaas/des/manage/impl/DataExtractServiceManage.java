package com.ai.paas.ipaas.des.manage.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.agent.client.AgentClient;
import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaaSMgmtConstant;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.util.CiperTools;
import com.ai.paas.ipaas.des.IllegalArgPaasRuntimeException;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBind;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTable;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserService;
import com.ai.paas.ipaas.des.manage.model.DesFilterTableParam;
import com.ai.paas.ipaas.des.manage.model.DesServiceBindParam;
import com.ai.paas.ipaas.des.manage.model.DesUserServiceParam;
import com.ai.paas.ipaas.des.manage.model.GeneralResult;
import com.ai.paas.ipaas.des.manage.model.GetAllServicesResult;
import com.ai.paas.ipaas.des.manage.model.GetBoundResult;
import com.ai.paas.ipaas.des.manage.model.GetBoundResult.DesServiceBindResult;
import com.ai.paas.ipaas.des.manage.model.GetBoundResult.DesServiceBindTableResult;
import com.ai.paas.ipaas.des.manage.rest.interfaces.IDataExtractServiceManager;
import com.ai.paas.ipaas.des.service.ICcsServiceWraper;
import com.ai.paas.ipaas.des.service.IDbsServiceWraper;
import com.ai.paas.ipaas.des.service.IDesServiceBind;
import com.ai.paas.ipaas.des.service.IDesServiceBindTable;
import com.ai.paas.ipaas.des.service.IDesUserService;
import com.ai.paas.ipaas.des.service.IMdsServiceWraper;
import com.ai.paas.ipaas.des.util.DesContants;
import com.ai.paas.ipaas.des.util.InstancePropertiesGenerater;
import com.ai.paas.ipaas.des.util.RegexUtil;
import com.ai.paas.ipaas.mds.dao.mapper.bo.MdsUserTopic;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

@Service
public class DataExtractServiceManage implements IDataExtractServiceManager {
	private static Logger logger = Logger.getLogger(DataExtractServiceManage.class);

	@Autowired
	private IDesUserService desUserService;

	@Autowired
	private IDesServiceBind desServiceBindService;

	@Autowired
	private IDesServiceBindTable desServiceBindTable;

	@Autowired
	private IDbsServiceWraper dbsServiceWraper;

	@Autowired
	private ICcsServiceWraper ccsServiceWraper;

	@Autowired
	private IMdsServiceWraper mdsServiceWraper;

	@Override
	public String create(String param) {
		GeneralResult result = new GeneralResult();
		Gson gson = new Gson();
		try {
			DesUserServiceParam desUserServiceParam = gson.fromJson(param, DesUserServiceParam.class);
			desUserServiceParam.setState(DesContants.DES_STATE_UNBIND);
			desUserService.create(desUserServiceParam);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			result.setResultMsg(DesContants.DES_REST_SUCCESS);
		} catch (PaasException e) {
			logger.error("create data extract service got problem :" + e.getMessage(), e);
			result.setResultCode(e.getErrCode());
			result.setResultMsg(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	@Override
	public String getAllServices(String param) {
		GetAllServicesResult result = new GetAllServicesResult();
		Gson gson = new Gson();
		try {
			DesUserServiceParam desUserServiceParam = gson.fromJson(param, DesUserServiceParam.class);
			List<DesUserService> list = desUserService.listAll(desUserServiceParam);
			if (CollectionUtils.isEmpty(list)) {
				result.setResultCode(PaaSConstant.ExceptionCode.NO_RESULT);
			} else {
				result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
				result.setServices(list);
			}
			result.setResultMsg(DesContants.DES_REST_SUCCESS);
			return (new Gson()).toJson(result);
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	@Override
	public String bind(String param) {
		GeneralResult result = new GeneralResult();
		Gson gson = new Gson();
		DesServiceBindParam desServiceBindParam = gson.fromJson(param, DesServiceBindParam.class);
		try {
			if (desServiceBindService.checkDbsServiceBound(desServiceBindParam.getUserId(), desServiceBindParam.getDbsServiceId())) {
				result.setResultCode(DesContants.SERVICE_ALLREADY_BOUND);
				result.setResultMsg("dbs service allready bound");
			} else if (desServiceBindService.checkMdsServiceBound(desServiceBindParam.getUserId(), desServiceBindParam.getMdsServiceId())) {
				result.setResultCode(DesContants.SERVICE_ALLREADY_BOUND);
				result.setResultMsg("mds service allready bound");
			} else {
				MdsUserTopic mdsUserTopic = mdsServiceWraper.getMdsUserTopic(desServiceBindParam.getUserId(), desServiceBindParam.getMdsServiceId());
				desServiceBindService.bind(desServiceBindParam, mdsUserTopic.getTopicEnName(), mdsUserTopic.getTopicPartitions());
				desUserService.bind(desServiceBindParam.getUserId(), desServiceBindParam.getServiceId());
				result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
				result.setResultMsg(DesContants.DES_REST_SUCCESS);

				// 发送配置到远程服务器
				List<DbsPhysicalResourcePool> prs = dbsServiceWraper.getSlavePhysicalResources(desServiceBindParam.getUserId(), desServiceBindParam.getDbsServiceId());
				for (DbsPhysicalResourcePool dbsPhysicalResourcePool : prs) {
					AgentClient agentClient = new AgentClient(dbsPhysicalResourcePool.getResHost(), dbsPhysicalResourcePool.getAgentPort());
					sendInstanceConfig(desServiceBindParam, mdsUserTopic, agentClient, dbsPhysicalResourcePool.getResHost() + ":" + dbsPhysicalResourcePool.getResPort(),
							dbsPhysicalResourcePool.getResUser(), dbsPhysicalResourcePool.getResPassword(), dbsPhysicalResourcePool.getResInstance());
				}
			}
		} catch (IllegalArgPaasRuntimeException e) {
			logger.error("bind des got problem :" + e.getMessage(), e);
			result.setResultCode(e.getErrCode());
			result.setResultMsg(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	private void sendInstanceConfig(DesServiceBindParam desServiceBindParam, MdsUserTopic mdsUserTopic, AgentClient agentClient, String host, String username, String password, String dbName)
			throws IOException {
		agentClient.executeInstruction(DesContants.CANAL_CONFIG_INSTANCES_PATH, "mkdir " + host);
		Properties properties = InstancePropertiesGenerater.generateProperties(desServiceBindParam, mdsUserTopic, host, username, CiperTools.decrypt(password), dbName);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		properties.store(bos, "");
		agentClient.saveFile(DesContants.CANAL_CONFIG_INSTANCES_PATH + host + "/instance.properties", bos.toString());
	}

	@Override
	public String getBound(String param) {
		GetBoundResult result = new GetBoundResult();
		Gson gson = new Gson();
		DesUserServiceParam desUserServiceParam = gson.fromJson(param, DesUserServiceParam.class);
		try {
			List<DesServiceBind> list = desServiceBindService.getBound(desUserServiceParam.getUserId());
			List<DesServiceBindResult> resultList = new ArrayList<>();
			for (DesServiceBind desServiceBind : list) {
				DesServiceBindResult desServiceBindResult = new DesServiceBindResult();
				desServiceBindResult.setDbsServiceId(desServiceBind.getDbsServiceId());
				desServiceBindResult.setMdsServiceId(desServiceBind.getMdsServiceId());
				desServiceBindResult.setMdsServicePassword(desServiceBind.getMdsServicePassword());
				desServiceBindResult.setServiceId(desServiceBind.getServiceId());
				desServiceBindResult.setUserId(desServiceBind.getUserId());
				List<DesServiceBindTable> bindTableList = desServiceBindTable.getBindTables(desServiceBind.getUserId(), desServiceBind.getServiceId());
				String[] bindTables = getBindTables(bindTableList);
				desServiceBindResult.setBoundTables(bindTables);
				resultList.add(desServiceBindResult);
			}
			result.setBindServices(resultList);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			result.setResultMsg(DesContants.DES_REST_SUCCESS);
		} catch (IllegalArgPaasRuntimeException e) {
			logger.error("get bound des got problem :" + e.getMessage(), e);
			result.setResultCode(e.getErrCode());
			result.setResultMsg(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	@Override
	public String getBoundTableInfo(String param) {
		DesServiceBindTableResult result = new DesServiceBindTableResult();
		Gson gson = new Gson();
		DesUserServiceParam desUserServiceParam = gson.fromJson(param, DesUserServiceParam.class);
		try {
			DesServiceBind desServiceBind = desServiceBindService.getBound(desUserServiceParam.getUserId(), desUserServiceParam.getServiceId());
			if (desServiceBind != null) {
				List<DesServiceBindTable> bindTableList = desServiceBindTable.getBindTables(desUserServiceParam.getUserId(), desUserServiceParam.getServiceId());
				String[] bindTables = getBindTables(bindTableList);
				result.setBoundTables(bindTables);
				result.setUnboundTables(ccsServiceWraper.getUnbindTables(desUserServiceParam.getUserId(), desServiceBind.getDbsServiceId(), bindTables));
				result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
				result.setResultMsg(DesContants.DES_REST_SUCCESS);
			} else {
				result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
				result.setResultMsg(DesContants.DES_REST_ERROR);
			}
		} catch (IllegalArgPaasRuntimeException e) {
			logger.error("get bound des got problem :" + e.getMessage(), e);
			result.setResultCode(e.getErrCode());
			result.setResultMsg(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	private String[] getBindTables(List<DesServiceBindTable> tables) {
		String[] result = new String[tables.size()];
		for (int i = 0; i < tables.size(); i++) {
			result[i] = tables.get(i).getTableName();
		}
		return result;
	}

	@Override
	public String unbind(String param) {
		GeneralResult result = new GeneralResult();
		Gson gson = new Gson();
		DesServiceBindParam desServiceBindParam = gson.fromJson(param, DesServiceBindParam.class);
		try {
			desServiceBindService.unbind(desServiceBindParam.getUserId(), desServiceBindParam.getServiceId());
			desUserService.unbind(desServiceBindParam.getUserId(), desServiceBindParam.getServiceId());
			desServiceBindTable.deleteBindTables(desServiceBindParam.getUserId(), desServiceBindParam.getServiceId());
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			result.setResultMsg(DesContants.DES_REST_SUCCESS);

			// 停止扫描实例，删除远程服务器上的配置
			List<DbsPhysicalResourcePool> prs = dbsServiceWraper.getSlavePhysicalResources(desServiceBindParam.getUserId(), desServiceBindParam.getDbsServiceId());
			for (DbsPhysicalResourcePool dbsPhysicalResourcePool : prs) {
				AgentClient agentClient = new AgentClient(dbsPhysicalResourcePool.getResHost(), dbsPhysicalResourcePool.getAgentPort());
				agentClient.executeInstruction(DesContants.CANAL_CONFIG_INSTANCES_PATH, "rm -rf " + dbsPhysicalResourcePool.getResHost() + ":" + dbsPhysicalResourcePool.getResPort());
			}
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			result.setResultMsg(DesContants.DES_REST_SUCCESS);
		} catch (IllegalArgPaasRuntimeException e) {
			logger.error("unbind des got problem :" + e.getMessage(), e);
			result.setResultCode(e.getErrCode());
			result.setResultMsg(e.getMessage());
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	@Override
	public String filterTable(String param) {
		GeneralResult result = new GeneralResult();
		Gson gson = new Gson();
		DesFilterTableParam desFilterTableParam = gson.fromJson(param, DesFilterTableParam.class);

		try {
			desServiceBindTable.insertBindTable(desFilterTableParam.getTables(), desFilterTableParam.getUserId(), desFilterTableParam.getServiceId());
			// 修改配置到远端服务器
			List<DbsPhysicalResourcePool> prs = dbsServiceWraper.getSlavePhysicalResources(desFilterTableParam.getUserId(), desFilterTableParam.getDbsServiceId());
			Map<String, String> paternMap = ccsServiceWraper.getTablePaterns(desFilterTableParam.getUserId(), desFilterTableParam.getDbsServiceId(), Arrays.asList(desFilterTableParam.getTables()));
			for (DbsPhysicalResourcePool dbsPhysicalResourcePool : prs) {
				AgentClient agentClient = new AgentClient(dbsPhysicalResourcePool.getResHost(), dbsPhysicalResourcePool.getAgentPort());
				String filePath = DesContants.CANAL_CONFIG_INSTANCES_PATH + dbsPhysicalResourcePool.getResHost() + ":" + dbsPhysicalResourcePool.getResPort() + "/"
						+ DesContants.CANAL_CONFIG_INSTANCE_FILE;
				ReadableByteChannel channel = agentClient.getFile(filePath);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int readresult = channel.read(buffer);
				while (readresult != -1) {
					bos.write(buffer.array(), 0, readresult);
					bos.flush();
					buffer.clear();
					readresult = channel.read(buffer);
				}
				Properties properties = new Properties();
				properties.load(new ByteArrayInputStream(bos.toByteArray()));
				bos.close();
				String regex = RegexUtil.generateRegex(dbsPhysicalResourcePool.getResInstance(), paternMap.values());
				if (regex.length() > 0) {
					properties.put(DesContants.CANAL_CONFIG_FILTER, regex);
					properties.put(DesContants.CANAL_CONFIG_BLACK_FILTER, "");
					properties.put(DesContants.PAAS_MDS_TABLE_FILTER_RULE, RegexUtil.generateTableRule(paternMap));
				} else {
					properties.put(DesContants.CANAL_CONFIG_FILTER, "");
					properties.put(DesContants.PAAS_MDS_TABLE_FILTER_RULE, "");
					properties.put(DesContants.CANAL_CONFIG_BLACK_FILTER, DesContants.CANAL_CONFIG_FILTER_BLANK);
				}
				bos = new ByteArrayOutputStream();
				properties.store(bos, "");
				agentClient.saveFile(filePath, bos.toString());
				bos.close();
			}
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_SUCCESS);
			result.setResultMsg(DesContants.DES_REST_SUCCESS);
		} catch (Exception e) {
			logger.error(e);
			result.setResultCode(PaaSMgmtConstant.REST_SERVICE_RESULT_FAIL);
			result.setResultMsg(DesContants.DES_REST_ERROR);
		}
		return gson.toJson(result);
	}

	@Override
	public String cancel(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getFuncList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String modify(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String restart(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String start(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String stop(String arg0) {
		throw new UnsupportedOperationException();
	}

	public void setDesUserService(IDesUserService desUserService) {
		this.desUserService = desUserService;
	}

	public void setDesServiceBindService(IDesServiceBind desServiceBindService) {
		this.desServiceBindService = desServiceBindService;
	}

}
