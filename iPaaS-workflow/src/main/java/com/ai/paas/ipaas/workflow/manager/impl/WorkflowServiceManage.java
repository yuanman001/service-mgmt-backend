package com.ai.paas.ipaas.workflow.manager.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.util.StringUtil;
import com.ai.paas.ipaas.workflow.constant.ExceptionCodeConstants;
import com.ai.paas.ipaas.workflow.manage.rest.interfaces.IWorkFlowServiceManager;
import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowParamVo;
import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowResultVo;
import com.ai.paas.ipaas.workflow.service.interfaces.IWorkflowSv;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

@Service
public class WorkflowServiceManage implements IWorkFlowServiceManager{

	private static Logger logger = Logger.getLogger(WorkflowServiceManage.class);
	@Autowired
	private IWorkflowSv iWorkflowSv;
	
	@Override
	public String create(String param) {
		logger.info("WrokflowDubboSvImpl -> applyWorkflowService ========================");
		Gson gson=new Gson();
		OpenWorkflowParamVo  openWorkflowParamVo=gson.fromJson(param, OpenWorkflowParamVo.class);
		OpenWorkflowResultVo  openWorkflowResultVo = new OpenWorkflowResultVo();
		try{
			if(param == null){
				throw new PaasException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter for appllying workflow is null");
			}
		    this.valParamVo(openWorkflowParamVo);
		    String result=iWorkflowSv.applyWorkflow(openWorkflowParamVo);
		    openWorkflowResultVo=gson.fromJson(result, OpenWorkflowResultVo.class);
		    openWorkflowResultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SUCCESS_CODE);
		    openWorkflowResultVo.setResultDesc(ExceptionCodeConstants.DubboServiceCode.SUCCESS_MESSAGE);
		}catch(PaasException e) {
			logger.error("there occurs a problem when applied for workflow,the reason is :" + e.getMessage());
			openWorkflowResultVo.setResultCode(e.getErrCode());
			openWorkflowResultVo.setResultDesc(e.getMessage());
		}catch(Exception e) {
			logger.error(logger,e);
			openWorkflowResultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE);
			openWorkflowResultVo.setResultDesc(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_MESSAGE);
		}
		
		return gson.toJson(openWorkflowResultVo);
	}
	
private void valParamVo(OpenWorkflowParamVo paramVo) throws Exception{
		
		if(StringUtil.isBlank(paramVo.getUserId())) {
			throw new PaasRuntimeException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter of user Id can not be empty");
		}
		
		if(StringUtil.isBlank(paramVo.getServiceId())){
			throw new PaasRuntimeException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter of service Id can not be empty");
		}
	}

	@Override
	public String cancel(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String restart(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

}
