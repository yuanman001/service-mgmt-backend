package com.ai.paas.ipaas.des.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.des.dao.interfaces.DesServiceBindTableMapper;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTable;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTableCriteria;
import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBindTableCriteria.Criteria;
import com.ai.paas.ipaas.des.service.IDesServiceBindTable;

@Service
@Transactional(rollbackFor = Exception.class)
public class DesServiceBindTableImpl implements IDesServiceBindTable {

	@Override
	public void insertBindTable(String[] tables, String userId, String serviceId) {
		deleteBindTables(userId, serviceId);
		// 插入新的数据
		DesServiceBindTableMapper mapper = ServiceUtil.getMapper(DesServiceBindTableMapper.class);
		DesServiceBindTableCriteria desServiceBindTableCriteria = new DesServiceBindTableCriteria();
		Criteria criteria = desServiceBindTableCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		for (String table : tables) {
			DesServiceBindTable record = new DesServiceBindTable();
			record.setServiceId(serviceId);
			record.setUserId(userId);
			record.setTableName(table);
			mapper.insert(record);
		}
	}

	@Override
	public List<DesServiceBindTable> getBindTables(String userId, String serviceId) {
		DesServiceBindTableMapper mapper = ServiceUtil.getMapper(DesServiceBindTableMapper.class);
		DesServiceBindTableCriteria desServiceBindTableCriteria = new DesServiceBindTableCriteria();
		Criteria criteria = desServiceBindTableCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		return mapper.selectByExample(desServiceBindTableCriteria);
	}

	@Override
	public void deleteBindTables(String userId, String serviceId) {
		// 删除原来的数据
		DesServiceBindTableMapper mapper = ServiceUtil.getMapper(DesServiceBindTableMapper.class);
		DesServiceBindTableCriteria desServiceBindTableCriteria = new DesServiceBindTableCriteria();
		Criteria criteria = desServiceBindTableCriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andServiceIdEqualTo(serviceId);
		mapper.deleteByExample(desServiceBindTableCriteria);
	}
}
