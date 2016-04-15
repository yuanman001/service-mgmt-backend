package com.ai.paas.ipaas.rcs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 实时计算服务：RCS (Realtime Calculation Service)<br/>
 * 服务的监控接口，定义计算任务、集群、主机的监控信息的获取功能
 * 
 * @author ygz
 *
 */
@Path("/rcs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRCSMonitor //extends ISrvManager 
{

	/**
	 * 获取特定拓扑的日志文件
	 */
	@Path("/getLogFile")
	@POST
	public String getLogFile();


}
