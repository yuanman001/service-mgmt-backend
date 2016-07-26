package com.ai.paas.ipaas.ses.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.ses.manage.rest.interfaces.IRPCDataSource;
import com.ai.paas.ipaas.ses.service.interfaces.IDataSource;
import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;
import com.alibaba.dubbo.config.annotation.Service;

@Service(validation = "true")
public class RPCDataSourceImpl implements IRPCDataSource {
	@Autowired
	IDataSource dsSrv;

	@Override
	public List<SesDataSourceInfo> getIndexDataSources(String userId,
			String serviceId, int groupId) {
		return dsSrv.getIndexDataSources(userId, serviceId, groupId);
	}

	@Override
	public SesIndexSqlInfo getIndexDataSql(String userId, String serviceId,
			int groupId) {
		return dsSrv.getIndexDataSql(userId, serviceId, groupId);
	}

	@Override
	public void saveIndexDataSql(Map<String, String> dbInfo,
			SesDataSourceInfo dbAttr, Map<String, String> userInfo) {
		dsSrv.saveIndexDataSql(dbInfo, dbAttr, userInfo);
	}

	@Override
	public SesDataSourceInfo getDataSourceInfo(
			List<SesDataSourceInfo> dataSources, Map<String, String> userInfo,
			Map<String, String> dbInfo) {
		return dsSrv.getDataSourceInfo(dataSources, userInfo, dbInfo);
	}

	@Override
	public void deleteDataSource(List<SesDataSourceInfo> dataSources,
			Map<String, String> userInfo) {
		dsSrv.deleteDataSource(dataSources, userInfo);
	}

	@Override
	public void deleteIndexDataSql(Map<String, String> sqlInfo,
			Map<String, String> userInfo) {
		dsSrv.deleteIndexDataSql(sqlInfo, userInfo);
	}

	@Override
	public void saveDataSource(Map<String, String> userInfo,
			List<SesDataSourceInfo> dataSources) {
		dsSrv.saveDataSource(userInfo, dataSources);
	}

	@Override
	public String getDataSourceUserPK(String userId, String srvID) {
		return dsSrv.getDataSourceUserPK(userId, srvID);
	}

	@Override
	public List<SesDataSourceInfo> getDataSource(int dataSourceUId,
			String dbAlias, int groupId) {
		return dsSrv.getDataSource(dataSourceUId, dbAlias, groupId);
	}

}
