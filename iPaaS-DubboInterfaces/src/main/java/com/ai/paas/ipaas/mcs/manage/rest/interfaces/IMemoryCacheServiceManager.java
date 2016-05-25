package com.ai.paas.ipaas.mcs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;


/**
 * 缓存服务：MCS (Memory Cache Service)<br/>
 * 服务管理接口
 * 
 * @author cym
 *
 */
@Path("/mcs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IMemoryCacheServiceManager extends ISrvManager {
	
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
	
	
	
	
	@Path("/get")
	@POST
	public String get(String getParam);	
	
	@Path("/del")
	@POST
	public String del(String delparam);	
	
	@Path("/clean")
	@POST
	public String clean(String cleanParam);	
	
	@Path("/set")
	@POST
	public String set(String getParam);	
	
	@Path("/info")
	@POST
	public String info(String getParam);	
}
