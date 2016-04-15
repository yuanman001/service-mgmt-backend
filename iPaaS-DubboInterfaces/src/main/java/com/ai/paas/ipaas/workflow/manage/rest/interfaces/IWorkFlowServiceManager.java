package com.ai.paas.ipaas.workflow.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rest.manage.ISrvManager;

/**
 * 工作流：WorkflowService<br/>
 * 服务管理接口
 * 
 */
@Path("/workflow/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IWorkFlowServiceManager extends ISrvManager {

	@Path("/create")
	@POST
	@Override
	public String create(String createApply);

	@Path("/getFuncList")
	@POST
	public String getFuncList();

	@Path("/modify")
	@POST
	@Override
	public String modify(String param);

	@Path("/restart")
	@POST
	@Override
	public String restart(String param);

	@Path("/start")
	@POST
	@Override
	public String start(String param);

	@Path("/stop")
	@POST
	@Override
	public String stop(String param);

	@Path("/cancel")
	@POST
	@Override
	public String cancel(String param);
}
