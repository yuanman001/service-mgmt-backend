package com.ai.paas.ipaas.des.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;



@Path("/des/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IDataExtractServiceManager extends ISrvManager {
	
	@Path("/create")
	@POST
	public String create(String param);
	
	@Path("/getAll")
	@POST
	public String getAllServices(String param);
	
	@Path("/bind")
	@POST
	public String bind(String param);
	
	@Path("/getBound")
	@POST
	public String getBound(String param);
	
	@Path("/getBoundTableInfo")
	@POST
	public String getBoundTableInfo(String param);
	
	@Path("/unbind")
	@POST
	public String unbind(String param);
	
	@Path("/filterTable")
	@POST
	public String filterTable(String param);
}
