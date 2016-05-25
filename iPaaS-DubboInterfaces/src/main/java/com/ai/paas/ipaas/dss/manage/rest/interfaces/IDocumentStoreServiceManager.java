package com.ai.paas.ipaas.dss.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;


/**
 * 存储中心： DSS（Document store service）<br/>
 * 服务管理接口
 * 
 * @author chenym6
 *
 */
@Path("/dss/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IDocumentStoreServiceManager extends ISrvManager {
	@Path("/cancel")
	@POST
	@Override
	public String cancel(String param);

	@Path("/create")
	@POST
	@Override
	public String create(String param);

	@Path("/getFuncList")
	@POST
	@Override
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

	@Path("/cleanAll")
	@POST
	public String cleanAll(String param);

	@Path("/cleanOne")
	@POST
	public String cleanOne(String param);

	@Path("/getStatus")
	@POST
	public String getStatus(String param);

	@Path("/getRecord")
	@POST
	public String getRecord(String param);
	
	@Path("/upload")
	@POST
	public String upload(String param);
}
