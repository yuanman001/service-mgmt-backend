package com.ai.paas.ipaas.ses.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;

public interface IDataSource {
	public List<SesDataSourceInfo> getIndexDataSources(String userId,
			String serviceId, int groupId);

	public SesIndexSqlInfo getIndexDataSql(String userId, String serviceId,
			int groupId);

	public void saveIndexDataSql(Map<String, String> dbInfo,
			SesDataSourceInfo dbAttr, Map<String, String> userInfo);

	public SesDataSourceInfo getDataSourceInfo(
			List<SesDataSourceInfo> dataSources, Map<String, String> userInfo,
			Map<String, String> dbInfo);

	public void deleteDataSource(List<SesDataSourceInfo> dataSources,
			Map<String, String> userInfo);

	public void deleteIndexDataSql(Map<String, String> sqlInfo,
			Map<String, String> userInfo);

	public void saveDataSource(Map<String, String> userInfo,
			List<SesDataSourceInfo> dataSources);

	public String getDataSourceUserPK(String userId, String srvID);

	public List<SesDataSourceInfo> getDataSource(int dataSourceUId,
			String dbAlias, int groupId);
}
