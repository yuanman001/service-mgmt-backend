package com.ai.paas.ipaas.idps.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;


/**
 * 图片动态处理服务：IDPS (Image dynamic Proccessing Service )<br/>
 * 服务管理接口
 * 
 * @author liwx3
 *
 */
@Path("/idps/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface ImageDynProcServiceManager extends ISrvManager {
	
	@Path("/getFuncList")
	@POST
	public String getFuncList();

	@Path("/create")
	@POST
	public String create(String createApply);

	@Path("/cancel")
	@POST
	public String cancel(String cancelApply);

	@Path("/modify")
	@POST
	public String modify(String modifyApply);

	@Path("/start")
	@POST
	public String start(String startApply);

	@Path("/stop")
	@POST
	public String stop(String stopApply);

	@Path("/restart")
	@POST
	public String restart(String restartApply);	
	
	@Path("/clean")
	@POST
	public String clean(String startApply);
	
	@Path("/upgrade")
	@POST
	public String upgrade(String startApply);
	
	@Path("/destroy")
	@POST
	public String destroy(String startApply);
	
	
}
