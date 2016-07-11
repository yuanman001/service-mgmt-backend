package com.ai.paas.ipaas.ses.service.interfaces;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.ses.service.vo.DataSourceInfo;
import com.ai.paas.ipaas.ses.service.vo.IndexSqlInfo;

public interface IDataSource {
	public List<DataSourceInfo> getIndexDataSources(String userId,
			String serviceId, int groupId);

	public IndexSqlInfo getIndexDataSql(String userId, String serviceId,
			int groupId);

	public void saveIndexDataSql(Map<String, String> dbInfo,
			DataSourceInfo dbAttr, Map<String, String> userInfo);

	public DataSourceInfo getDataSourceInfo(List<DataSourceInfo> dataSources,
			Map<String, String> userInfo, Map<String, String> dbInfo);

	public void deleteDataSource(List<DataSourceInfo> dataSources,
			Map<String, String> userInfo);

	public void deleteIndexDataSql(Map<String, String> sqlInfo,
			Map<String, String> userInfo);

	public void saveDataSource(Map<String, String> userInfo,
			List<DataSourceInfo> dataSources);

}
