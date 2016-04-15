package com.ai.paas.ipaas.rcs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 实时计算服务：RCS (Realtime Calculation Service)<br/>
 * 服务操作接口，定义计算任务的启动、停止功能。
 * 
 * @author ygz
 *
 */
@Path("/rcs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRCSOperate //extends ISrvManager 
{
	
	/**
	 * 启动storm拓扑
	 */
	@Path("/start")
	@POST
	public String start(String startApply);

	/**
	 * 停止storm拓扑
	 */
	@Path("/stop")
	@POST
	public String stop(String stopApply);

}
