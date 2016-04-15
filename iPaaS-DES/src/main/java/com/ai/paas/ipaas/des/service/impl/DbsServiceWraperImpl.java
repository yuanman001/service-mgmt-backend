package com.ai.paas.ipaas.des.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.service.IDbsLogicResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsPhysicalResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsUserServiceSv;
import com.ai.paas.ipaas.des.service.IDbsServiceWraper;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsServiceWraperImpl implements IDbsServiceWraper {

	@Autowired
	private IDbsUserServiceSv dbsUserServiceSv;

	@Autowired
	private IDbsLogicResourcePoolSv dbsLogicResourcePoolSv;

	@Autowired
	private IDbsPhysicalResourcePoolSv dbsPhysicalResourcePoolSv;

	@Override
	public List<DbsPhysicalResourcePool> getSlavePhysicalResources(String userId, String userServiceId) {
		List<DbsPhysicalResourcePool> result = new ArrayList<DbsPhysicalResourcePool>();
		DbsUserService criteria = new DbsUserService();
		criteria.setUserId(userId);
		criteria.setUserServiceId(userServiceId);
		List<DbsUserService> dbsUserServiceList = dbsUserServiceSv.getModels(criteria);
		if (CollectionUtils.isEmpty(dbsUserServiceList))
			return result;
		for (DbsUserService dbsUserService : dbsUserServiceList) {
			DbsLogicResourcePool logicResourcePoolCriteria = new DbsLogicResourcePool();
			logicResourcePoolCriteria.setUsedId(dbsUserService.getUsedId());
			List<DbsLogicResourcePool> DbsLogicResourcePoolList = dbsLogicResourcePoolSv.getModels(logicResourcePoolCriteria);

			if (CollectionUtils.isEmpty(DbsLogicResourcePoolList))
				continue;
			for (DbsLogicResourcePool dbsLogicResourcePool : DbsLogicResourcePoolList) {
				DbsPhysicalResourcePool physicalResourcePoolCriteria = new DbsPhysicalResourcePool();
				physicalResourcePoolCriteria.setLogicId(dbsLogicResourcePool.getLogicId());
				physicalResourcePoolCriteria.setMsFlag("1");
				List<DbsPhysicalResourcePool> list = dbsPhysicalResourcePoolSv.getModels(physicalResourcePoolCriteria);
				if (CollectionUtils.isNotEmpty(list)) {
					result.addAll(list);
				}
			}
		}
		return result;
	}
}
