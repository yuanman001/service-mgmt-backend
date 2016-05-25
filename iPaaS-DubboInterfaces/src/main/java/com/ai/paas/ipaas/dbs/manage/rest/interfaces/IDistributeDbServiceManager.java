package com.ai.paas.ipaas.dbs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;



@Path("/dbs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IDistributeDbServiceManager extends ISrvManager {

	@Path("/cancel")
	@POST
	@Override
	public String cancel(String param);

	@Path("/create")
	@POST
	@Override
	public String create(String param);

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
	
	@Path("/getFuncList")
    @POST
    public String getFuncList();
	
	@Path("/checkMUIAuth")
	@POST
	public String checkMUIAuth(String param);
	
	
	@Path("/isTxs")
	@POST
	public String isTxs(String param);
}
