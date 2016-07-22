package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.vo.ses.SesUserIndexWord;
import com.ai.paas.ipaas.vo.ses.SesUserStopWord;

@Path("/ses/dic")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRPCIKDictionary {

	@Path("save")
	@POST
	public void saveDictonaryWord(List<SesUserIndexWord> indexWordList,
			List<SesUserStopWord> stopWordList, String userId, String serviceId);

	@SuppressWarnings("rawtypes")
	@Path("get")
	@POST
	public Map<String, List> getUserDictionary(String userId, String serviceId);
}
