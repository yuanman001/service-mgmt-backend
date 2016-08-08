package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.vo.ses.RPCDictionay;

@Path("/ses/dic")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRPCIKDictionary {

	@Path("save")
	@POST
	public void saveDictonaryWord(RPCDictionay rpcDict);

	@SuppressWarnings("rawtypes")
	@Path("get")
	@POST
	public Map<String, List> getUserDictionary(
			@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId,
			@FormParam("start") Integer start, @FormParam("rows") Integer rows);

	@Path("getUserIndexWords")
	@POST
	public String getUserIndexWords(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId,
			@FormParam("start") Integer start, @FormParam("rows") Integer rows);

	@Path("getUserStopWords")
	@POST
	public String getUserStopWords(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId,
			@FormParam("start") Integer start, @FormParam("rows") Integer rows);

	@Path("saveAllStopWords")
	@POST
	public String saveAllStopWords(String allStopWords);

	@Path("saveAllIndexWords")
	@POST
	public String saveAllIndexWords(String allIndexWords);

	@Path("clearAllIndexWords")
	@POST
	public void clearAllIndexWords(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId);
	
	@Path("clearAllStopWords")
	@POST
	public void clearAllStopWords(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId);
}
