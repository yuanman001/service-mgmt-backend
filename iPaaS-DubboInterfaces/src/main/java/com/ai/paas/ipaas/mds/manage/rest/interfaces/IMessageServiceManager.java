package com.ai.paas.ipaas.mds.manage.rest.interfaces;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.rpc.api.manage.ISrvManager;

@Path("/mds/manage")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IMessageServiceManager extends ISrvManager {
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

	@Path("/topic/create")
	@POST
	public String createTopic(String topicApply);

	@Path("/topic/remove")
	@POST
	public String removeTopic(String topicApply);

	@Path("/topic/usage")
	@POST
	public String getTopicUsage(String topicApply);

	@Path("/topic/message/get")
	@POST
	public String getTopicMessage(String topicApply);

	@Path("/topic/offset/adjust")
	@POST
	public String adjustTopicOffset(String adjustTopicApply);
	
	@Path("/topic/message/send")
	@POST
	public String sendMessage(String topicApply);

	@Path("/subscribe/create")
	@POST
	public String createSubscribe(String subscribeApply);
	
	@Path("/subscribe/get")
	@POST
	public String getSubscribe(String subscribeApply);
	
	@Path("/subscribe/getListSubPath")
	@POST
	public String getListSubPath(String subscribeApply);
}
