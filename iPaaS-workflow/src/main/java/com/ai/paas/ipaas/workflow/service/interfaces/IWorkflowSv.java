package com.ai.paas.ipaas.workflow.service.interfaces;

import com.ai.paas.ipaas.workflow.manager.vo.OpenWorkflowParamVo;

public interface IWorkflowSv {
	   /**
		   * 工作流服务申请处理
		   * @param paramVo
		   * @throws Exception
		   */
		  public String  applyWorkflow(OpenWorkflowParamVo paramVo) throws Exception;
		  
}
