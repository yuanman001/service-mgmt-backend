package com.ai.paas.ipaas.rcs.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;


/**
 * 实时计算服务：RCS (Realtime Calculation Service)<br/>
 * 服务管理接口，具备任务管理、主机管理和集群管理的功能。
 * 
 * @author ygz
 *
 */
@Path("/rcs/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRCSManager extends ISrvManager// extends ISrvManager
{
	/**
	 * 注册计算任务
	 */
	@Path("/registerTask")
	@POST
	public String registerTask(String stormTaskInfoVo);

	/**
	 * 分页查询计算任务
	 */
	@Path("/showList")
	@POST
	public String PagingResult(String pageEntity);

	/**
	 * 编辑计算任务
	 */
	@Path("/editTask")
	@POST
	public String editTask(String stormTaskInfoVo);

	/**
	 * 操作计算任务
	 */
	@Path("/operTask")
	@POST
	public String operTask(String operType);

	/**
	 * 获取计算任务
	 */
	@Path("/getTask")
	@POST
	public String getTask(String id);

	/**
	 * 获取cluster信息
	 */
	@Path("/getCluster")
	@POST
	public String getCluster(String userId);

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
}
