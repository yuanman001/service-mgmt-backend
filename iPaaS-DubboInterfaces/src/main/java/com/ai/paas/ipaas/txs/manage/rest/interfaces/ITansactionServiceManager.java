package com.ai.paas.ipaas.txs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rest.manage.ISrvManager;


/**
 * 事务保障服务：TXS (Tansaction Service)<br/>
 * 服务管理接口
 * 
 * @author wusheng
 *
 */
@Path("/txs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface ITansactionServiceManager extends ISrvManager {
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
}
