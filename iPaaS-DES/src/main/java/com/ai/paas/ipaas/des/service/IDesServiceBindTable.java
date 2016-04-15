package com.ai.paas.ipaas.des.service;

import java.util.List;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTable;

public interface IDesServiceBindTable {

	public void insertBindTable(String[] tables, String userId, String serviceId);

	public List<DesServiceBindTable> getBindTables(String userId, String serviceId);
	
	public void deleteBindTables(String userId, String serviceId);
}
