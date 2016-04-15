package com.ai.paas.ipaas.dbs.service.impl;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsPhysicalResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;
import com.ai.paas.ipaas.dbs.service.IDbsLogicResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsPhysicalResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsResourceManage;
import com.ai.paas.ipaas.dbs.service.IDbsUserServiceSv;
import com.ai.paas.ipaas.dbs.util.DistributeDbConstants;
import com.ai.paas.ipaas.dbs.util.ExceptionCodeConstants;


@Service
@Transactional(rollbackFor = Exception.class)
public class DbsResourceManageSv implements IDbsResourceManage{
	@Autowired
	IDbsPhysicalResourcePoolSv service;
	@Autowired
	IDbsLogicResourcePoolSv iDbsLogicResourcePoolSv;
	@Autowired
	IDbsUserServiceSv iDbsUserServiceSv;
	@Autowired
	ICCSComponentManageSv manage;
	
	private static final Logger log = LogManager
			.getLogger(DbsResourceManageSv.class.getName());
	
	@Override
	public void applyDistributeDd(OpenResourceParamVo paramVo) throws Exception {
		service.applyDistributeDd(paramVo);
		
	}
	@Override
	public void handleCrash(String param) throws PaasException {
		log.info("dbs manage handlecrash invoked");
		JSONObject object = JSONObject.fromObject(param);
		DbsPhysicalResourcePoolMapper mapper=ServiceUtil.getMapper(DbsPhysicalResourcePoolMapper.class);
		
		DbsPhysicalResourcePoolCriteria dbsCriteria=new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		criteria.andResHostEqualTo((String) object.get("host"));
		criteria.andResPortEqualTo((int) object.get("port"));
		criteria.andResInstanceEqualTo((String) object.get("instance"));
		List<DbsPhysicalResourcePool> listSearch =mapper.selectByExample(dbsCriteria);
		if (listSearch.get(0) == null&&listSearch.size()!=1) {
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE,
					"the related instance does not exist or exists more than one instance");
			
		}
		DbsPhysicalResourcePool dbs = listSearch.get(0);
		dbs.setResStatus(DistributeDbConstants.ResStatus.CRASH);
		mapper.updateByPrimaryKey(dbs);
		int logicId=dbs.getLogicId();
		DbsUserService dbsUserService= getDbsUserService(logicId);
		if(dbsUserService.getIsAutoswitch().equalsIgnoreCase(DistributeDbConstants.IsAutoSwitch.autoSwith))
		{
			DbsLogicResourcePool dbsLogicResourcePool=iDbsLogicResourcePoolSv.findByPkey(logicId);
			String logicName=dbsLogicResourcePool.getLogicName();
			OpenResourceParamVo paramVo=new OpenResourceParamVo();
			paramVo.setServiceId(dbsUserService.getUserServiceId());
			DbsPhysicalResourcePoolSv sv=new DbsPhysicalResourcePoolSv();
			String dbPath=sv.getDbPath(paramVo, logicName);
			CCSComponentOperationParam cccomponent=new CCSComponentOperationParam();
			cccomponent.setPath(dbPath);
			cccomponent.setUserId(dbsUserService.getUserId());
			cccomponent.setPathType(ConfigCenterDubboConstants.PathType.READONLY);
			if(manage.exists(cccomponent)){
				try {
					JSONObject jsonObject = JSONObject.fromObject(manage.get(cccomponent));
					JSONArray array = new JSONArray();
					jsonObject.remove("slaves");
					if(dbs.getMsFlag().equals(DistributeDbConstants.ResMsFlag.MASTER))
					{
						jsonObject.remove("master");
						DbsPhysicalResourcePool slave=new DbsPhysicalResourcePool();
						slave.setLogicId(dbs.getLogicId());
						slave.setResStatus(DistributeDbConstants.ResStatus.NORMAL);
						List<DbsPhysicalResourcePool> list=service.getModels(slave);
						if(list!=null&&list.size()==1)
						{
							jsonObject.put("master", list.get(0).getResName());
							jsonObject.put("slaves", array);
							manage.modify(cccomponent, jsonObject.toString());
							//更新主从关系
							dbs.setMsFlag(DistributeDbConstants.ResMsFlag.SLAVE);
							list.get(0).setMsFlag(DistributeDbConstants.ResMsFlag.MASTER);
							mapper.updateByPrimaryKey(dbs);
							mapper.updateByPrimaryKey(list.get(0));
						}
					}else{
						jsonObject.put("slaves", array);
						manage.modify(cccomponent, jsonObject.toString());
					}
					log.info("In zk Application information is modified in successfully! and the path is :"+dbPath);
				} catch (PaasException e) {
					log.error( "In zk application information is not modified and the path is :"+dbPath, e);
					throw new PaasRuntimeException("resultMes", "In zk application information is not modified and the path is :"+dbPath);
				}
			}
		}
	}
	
	private DbsUserService getDbsUserService(int logicId){
		DbsLogicResourcePool dbsLogicResourcePool=iDbsLogicResourcePoolSv.findByPkey(logicId);
		DbsUserService userService=iDbsUserServiceSv.findByPkey(dbsLogicResourcePool.getUsedId());
		
		return userService;
	}
	@Override
	public void recoverDistributeDb(String param) throws PaasException {
		log.info("dbs manage recoverDistributeDb invoked");
		JSONObject object = JSONObject.fromObject(param);
		DbsPhysicalResourcePoolMapper mapper=ServiceUtil.getMapper(DbsPhysicalResourcePoolMapper.class);
		
		DbsPhysicalResourcePoolCriteria dbsCriteria=new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria=dbsCriteria.createCriteria();
		criteria.andResHostEqualTo((String) object.get("host"));
		criteria.andResPortEqualTo((int) object.get("port"));
		criteria.andResInstanceEqualTo((String) object.get("instance"));
		List<DbsPhysicalResourcePool> listSearch =mapper.selectByExample(dbsCriteria);
		if (listSearch.get(0) == null&&listSearch.size()!=1) {
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE,
					"the related instance does not exist or exists more than one instance ");
			
		}
		DbsPhysicalResourcePool dbs = listSearch.get(0);
		dbs.setResStatus(DistributeDbConstants.ResStatus.NORMAL);
		mapper.updateByPrimaryKey(dbs);
		int logicId=dbs.getLogicId();
		
		//查询是否需要分布式事务
		DbsLogicResourcePool dbsLogicResourcePool=iDbsLogicResourcePoolSv.findByPkey(logicId);
		String logicName=dbsLogicResourcePool.getLogicName();
		DbsUserService dbsUserService= getDbsUserService(logicId);
		DbsPhysicalResourcePoolSv sv=new DbsPhysicalResourcePoolSv();
		
		OpenResourceParamVo paramVo=new OpenResourceParamVo();
		paramVo.setServiceId(dbsUserService.getUserServiceId());
		String dbPath=sv.getDbPath(paramVo, logicName);
		CCSComponentOperationParam cccomponent=new CCSComponentOperationParam();
		cccomponent.setPath(dbPath);
		cccomponent.setUserId(dbsUserService.getUserId());
		cccomponent.setPathType(ConfigCenterDubboConstants.PathType.READONLY);
		if(manage.exists(cccomponent)){
			try {
				JSONObject jsonObject = JSONObject.fromObject(manage.get(cccomponent));  
				
				JSONArray array = new JSONArray();
				JSONObject slaveObject = new JSONObject();
				slaveObject.put("slave", dbs.getResName());
				slaveObject.put("slaveMode", "readonly");
				slaveObject.put("weight", "70");
				array.add(slaveObject);
				
				jsonObject.remove("slaves");
				jsonObject.put("slaves", array);
				manage.modify(cccomponent, jsonObject.toString());
				log.info("In zk Application information is modified in successfully! and the path is :"+dbPath);
			} catch (PaasException e) {
				log.error( "In zk application information is not modified and the path is :"+dbPath, e);
				throw new PaasRuntimeException("resultMes", "In zk application information is not modified and the path is :"+dbPath);
			}
			
		}
	}
	/*private JSONObject getDbConfigJson(DbsPhysicalResourcePool distributeDbRes) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("driver", "com.mysql.jdbc.Driver");
		jsonObject.put("username", distributeDbRes.getResUser());
		jsonObject.put("password", distributeDbRes.getResPassword());
		String dbConnUrl = this.getDbConnUrl(distributeDbRes);
		jsonObject.put("url", dbConnUrl);
		jsonObject.put("initSize", "2");
		jsonObject.put("maxActive", "30");
		jsonObject.put("maxIdle", "4");
		jsonObject.put("maxWait", "10000");
		jsonObject.put("validationQuery", "SELECT 1");
		jsonObject.put("testWhileIdle", "true");
		return jsonObject;
	}*/
	
	/*private String getDbConnUrl(DbsPhysicalResourcePool distributeDbRes) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("jdbc:mysql://").append(distributeDbRes.getResHost());
		buffer.append(":").append(distributeDbRes.getResPort());
		buffer.append("/").append(distributeDbRes.getResInstance())
				.append("?autoReconnect=true");
		return buffer.toString();
	}
*/
	
}
