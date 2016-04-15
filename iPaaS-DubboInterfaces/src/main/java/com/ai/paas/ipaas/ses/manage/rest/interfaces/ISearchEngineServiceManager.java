package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 缓存服务：SES (Search Engine Service)<br/>
 * 服务管理接口
 * 
 *
 */
@Path("/ses/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface ISearchEngineServiceManager{
	
	@Path("/getFuncList")
	@POST
	public String getFuncList();

	@Path("/create")
	@POST
	public String create(String createApply);
	
	@Path("/mapping")
	@POST
	public String mapping(String mappingApply);
	
	@Path("/index")
	@POST
	public String index(String mappingApply);
	
	@Path("/start")
	@POST
	public String start(String operateApply);
	
	@Path("/stop")
	@POST
	public String stop(String operateApply);
	
	@Path("/recycle")
	@POST
	public String recycle(String operateApply);

}
