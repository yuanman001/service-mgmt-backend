package com.ai.paas.ipaas.ses.manager.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.ses.manage.rest.interfaces.IRPCDataSource;
import com.ai.paas.ipaas.ses.service.interfaces.IDataSource;
import com.ai.paas.ipaas.vo.ses.RPCDataSource;
import com.ai.paas.ipaas.vo.ses.SesDataSourceInfo;
import com.ai.paas.ipaas.vo.ses.SesIndexSqlInfo;
import com.alibaba.dubbo.config.annotation.Service;

@Service(validation = "true")
public class RPCDataSourceImpl implements IRPCDataSource {
	@Autowired
	IDataSource dsSrv;

	@Override
	public List<SesDataSourceInfo> getIndexDataSources(String userId,
			String serviceId, Integer groupId) {
		return dsSrv.getIndexDataSources(userId, serviceId, groupId);
	}

	@Override
	public SesIndexSqlInfo getIndexDataSql(String userId, String serviceId,
			Integer groupId) {
		return dsSrv.getIndexDataSql(userId, serviceId, groupId);
	}

	@Override
	public void saveIndexDataSql(RPCDataSource rpcDataSource) {
		dsSrv.saveIndexDataSql(rpcDataSource.getDbInfo(),
				rpcDataSource.getDbAttr(), rpcDataSource.getUserInfo());
	}

	@Override
	public SesDataSourceInfo getDataSourceInfo(RPCDataSource rpcDataSource) {
		return dsSrv.getDataSourceInfo(rpcDataSource.getDataSources(),
				rpcDataSource.getUserInfo(), rpcDataSource.getDbInfo());
	}

	@Override
	public void deleteDataSource(RPCDataSource rpcDataSource) {
		dsSrv.deleteDataSource(rpcDataSource.getDataSources(),
				rpcDataSource.getUserInfo());
	}

	@Override
	public void deleteIndexDataSql(RPCDataSource rpcDataSource) {
		dsSrv.deleteIndexDataSql(rpcDataSource.getSqlInfo(),
				rpcDataSource.getUserInfo());
	}

	@Override
	public void saveDataSource(RPCDataSource rpcDataSource) {
		dsSrv.saveDataSource(rpcDataSource.getUserInfo(),
				rpcDataSource.getDataSources());
	}

	@Override
	public String getDataSourceUserPK(String userId, String srvID) {
		return dsSrv.getDataSourceUserPK(userId, srvID);
	}

	@Override
	public List<SesDataSourceInfo> getDataSource(Integer dataSourceUId,
			String dbAlias, Integer groupId) {
		return dsSrv.getDataSource(dataSourceUId, dbAlias, groupId);
	}

}
