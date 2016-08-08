package com.ai.paas.ipaas.vo.ses;

import java.util.List;
import java.util.Map;

import com.ai.dubbo.ext.vo.BaseInfo;

public class RPCDataSource extends BaseInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1567361641097036324L;
	private List<SesDataSourceInfo> dataSources;
	private Map<String, String> dbInfo;
	private SesDataSourceInfo dbAttr;
	private Map<String, String> userInfo;
	private Map<String, String> sqlInfo;

	public List<SesDataSourceInfo> getDataSources() {
		return dataSources;
	}

	public void setDataSources(List<SesDataSourceInfo> dataSources) {
		this.dataSources = dataSources;
	}

	public Map<String, String> getDbInfo() {
		return dbInfo;
	}

	public void setDbInfo(Map<String, String> dbInfo) {
		this.dbInfo = dbInfo;
	}

	public SesDataSourceInfo getDbAttr() {
		return dbAttr;
	}

	public void setDbAttr(SesDataSourceInfo dbAttr) {
		this.dbAttr = dbAttr;
	}

	public Map<String, String> getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(Map<String, String> userInfo) {
		this.userInfo = userInfo;
	}

	public Map<String, String> getSqlInfo() {
		return sqlInfo;
	}

	public void setSqlInfo(Map<String, String> sqlInfo) {
		this.sqlInfo = sqlInfo;
	}

}
