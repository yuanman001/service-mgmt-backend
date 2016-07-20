package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.ses.SesUserInstance;

@Path("/ses/userInst")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface ISesUserInst {
	/**
	 * 根据userid srvId查询主机实例
	 * 
	 * @param userId
	 * @param srvId
	 * @return
	 * @throws PaasException
	 */
	@Path("queryInst")
	@POST
	public SesUserInstance queryInst(String userId, String srvId)
			throws PaasException;

}
