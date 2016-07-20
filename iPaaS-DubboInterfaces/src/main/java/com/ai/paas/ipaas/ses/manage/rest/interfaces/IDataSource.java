package com.ai.paas.ipaas.ses.manage.rest.interfaces;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;

@Path("/ses/import")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
public interface IDataSource {
	@Path("getAllDS")
	@POST
	public List<SesDataSourceInfo> getIndexDataSources(String userId,
			String serviceId, int groupId);

	@Path("getSQL")
	@POST
	public SesIndexSqlInfo getIndexDataSql(String userId, String serviceId,
			int groupId);

	@Path("saveSQL")
	@POST
	public void saveIndexDataSql(Map<String, String> dbInfo,
			SesDataSourceInfo dbAttr, Map<String, String> userInfo);

	@Path("getDS")
	@POST
	public SesDataSourceInfo getDataSourceInfo(List<SesDataSourceInfo> dataSources,
			Map<String, String> userInfo, Map<String, String> dbInfo);

	@Path("deleteAllDS")
	@POST
	public void deleteDataSource(List<SesDataSourceInfo> dataSources,
			Map<String, String> userInfo);

	@Path("deleteSQL")
	@POST
	public void deleteIndexDataSql(Map<String, String> sqlInfo,
			Map<String, String> userInfo);

	@Path("saveDS")
	@POST
	public void saveDataSource(Map<String, String> userInfo,
			List<SesDataSourceInfo> dataSources);

}
