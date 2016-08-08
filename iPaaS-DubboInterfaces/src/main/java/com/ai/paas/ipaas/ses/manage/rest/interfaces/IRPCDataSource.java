package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.vo.ses.RPCDataSource;
import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;

@Path("/ses/import")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IRPCDataSource {
	@Path("getAllDS")
	@POST
	public List<SesDataSourceInfo> getIndexDataSources(
			@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId,
			@FormParam("groupId") Integer groupId);

	@Path("getSQL")
	@POST
	public SesIndexSqlInfo getIndexDataSql(@FormParam("userId") String userId,
			@FormParam("serviceId") String serviceId,
			@FormParam("groupId") Integer groupId);

	@Path("saveSQL")
	@POST
	public void saveIndexDataSql(RPCDataSource rpcDataSource);

	@Path("getDS")
	@POST
	public SesDataSourceInfo getDataSourceInfo(RPCDataSource rpcDataSource);

	@Path("deleteAllDS")
	@POST
	public void deleteDataSource(RPCDataSource rpcDataSource);

	@Path("deleteSQL")
	@POST
	public void deleteIndexDataSql(RPCDataSource rpcDataSource);

	@Path("saveDS")
	@POST
	public void saveDataSource(RPCDataSource rpcDataSource);

	/**
	 * @param userId
	 * @param srvID
	 * @return
	 */
	@Path("getDBUserPK")
	@POST
	public String getDataSourceUserPK(@FormParam("userId") String userId,
			@FormParam("srvId") String srvId);

	@Path("getDSByDuid")
	@POST
	public List<SesDataSourceInfo> getDataSource(
			@FormParam("dataSourceUId") Integer dataSourceUId,
			@FormParam("dbAlias") String dbAlias,
			@FormParam("groupId") Integer groupId);
}
