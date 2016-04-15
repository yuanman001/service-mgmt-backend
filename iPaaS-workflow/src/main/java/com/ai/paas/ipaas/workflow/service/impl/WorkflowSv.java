package com.ai.paas.ipaas.workflow.service.impl;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.workflow.constant.WorkflowConstants;
import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowParamVo;
import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowResultVo;
import com.ai.paas.ipaas.workflow.manager.vo.WorkflowUrlVo;
import com.ai.paas.ipaas.workflow.service.interfaces.IWorkflowSv;
import com.google.gson.Gson;

@Service
public class WorkflowSv implements IWorkflowSv{
	private static Logger logger = Logger.getLogger(WorkflowSv.class);

	@Autowired
	ICCSComponentManageSv manage;
	
	@Override
	public String applyWorkflow(OpenWorkflowParamVo paramVo) throws Exception {
		// 基本的校验
		// TODO 分配工作流
		// 注册配置
		regisWorkflowConfig(paramVo);
		//注册Workflow_Rule_path,like /workflow/workflow001(serviceId)
		Gson gson=new Gson();
		OpenWorkflowResultVo resultVo=new OpenWorkflowResultVo();
		//TODO 获取分配的工作流服务相关地址，暂时固定写死
		ArrayList resultMsg = this.getWorkflowUrl();
		resultVo.setResultContent(resultMsg);
		resultVo.setUserId(paramVo.getUserId());
		resultVo.setServiceId(paramVo.getServiceId());
		String result=gson.toJson(resultVo);
		return result;
	}

	//工作流申请配置信息保存
	private void regisWorkflowConfig(OpenWorkflowParamVo paramVo) {
			String workflowConfigPath = this.getConfigPath(paramVo);
			addConfig(workflowConfigPath, paramVo);
	}

	//在zk中记录申请信息
	private void addConfig(String workflowConfigPath, OpenWorkflowParamVo paramVo) {

		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(workflowConfigPath);
		param.setUserId(paramVo.getUserId());
		param.setPathType(ConfigCenterDubboConstants.PathType.READONLY);
		logger.info("workflwConfigPath=" + workflowConfigPath + "  userId=" + paramVo.getUserId() + "  serviceId=" + paramVo.getServiceId()); 
		try {
			manage.add(param, paramVo.getServiceId());
			logger.info("successed to apply for Information in zookeeper!");
		} catch (PaasException e) {
			logger.error("Workflow-ERROR Failed to apply for Information in zookeeper!");
			throw new PaasRuntimeException("resultMes", "Failed to apply for Information in zookeeper!");
		}
	}
	
	//获取workflow根目录
	public String getConfigPath( OpenWorkflowParamVo paramVo) {
		StringBuilder pathBuffer = new StringBuilder();
		pathBuffer.append(WorkflowConstants.dirSplit
				+ WorkflowConstants.ConfPath.WF_CONF_PATH
				+ WorkflowConstants.dirSplit + paramVo.getServiceId());
		return pathBuffer.toString();
	}
	
	//获取工作流返回信息
	private ArrayList getWorkflowUrl() {
		ArrayList list = new ArrayList<WorkflowUrlVo>();
		WorkflowUrlVo workflowUrlVo = new WorkflowUrlVo();
		//接口服务调用地址
		workflowUrlVo.setUrl(WorkflowConstants.ConfAppAddr.WF_CONF_INTERFACE_ADDR);
		workflowUrlVo.setUrlDesc(WorkflowConstants.ConfAppAddr.WF_CONF_INTERFACE_ADDR_DESC);
		list.add(workflowUrlVo);
		//流程建模的地址
		workflowUrlVo.setUrl(WorkflowConstants.ConfAppAddr.WF_CONF_MODELING_ADDR);
		workflowUrlVo.setUrlDesc(WorkflowConstants.ConfAppAddr.WF_CONF_MODELING_ADDR_DESC);
		list.add(workflowUrlVo);
		return list;
	}

}
