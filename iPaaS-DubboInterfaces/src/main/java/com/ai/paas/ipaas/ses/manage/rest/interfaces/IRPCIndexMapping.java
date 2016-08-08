package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.dubbo.ext.vo.BaseResponse;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.vo.ses.SesUserMapping;

@Path("/ses/mapping")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRPCIndexMapping {
	/**
	 * 加载mapping
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 * @throws PaasException
	 */
	@Path("load")
	@POST
	public SesUserMapping loadMapping(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId) throws PaasException;

	/**
	 * 
	 * 编辑mapping
	 *
	 * @author jianhua.ma
	 * @param mapping
	 */
	@Path("update")
	@POST
	public BaseResponse editMapping(SesUserMapping mapping) throws PaasException;

	/**
	 * 
	 * 沉淀mapping
	 *
	 * @author jianhua.ma
	 * @param mapping
	 */
	@Path("add")
	@POST
	public BaseResponse insertMapping(SesUserMapping mapping) throws PaasException;

}
