package com.ai.paas.ipaas.dbs.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.agent.client.AgentClient;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.base.manager.ISequenceManageSv;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.IConfigCenterServiceManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ccs.service.dto.ConfigInfo;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsLogicResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsMuiResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsMuiUserServiceMapper;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsPhysicalResourcePoolMapper;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsUserServiceMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsLogicResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsPhysicalResourcePoolCriteria;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserServiceCriteria;
import com.ai.paas.ipaas.dbs.dto.AuthResult;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceResultVo;
import com.ai.paas.ipaas.dbs.service.IDbsLogicResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsMuiResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsMuiUserServiceSv;
import com.ai.paas.ipaas.dbs.service.IDbsPhysicalResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsUserServiceSv;
import com.ai.paas.ipaas.dbs.util.CiperTools;
import com.ai.paas.ipaas.dbs.util.CollectionUtil;
import com.ai.paas.ipaas.dbs.util.DistributeDbConstants;
import com.ai.paas.ipaas.dbs.util.DistributedDBRule;
import com.ai.paas.ipaas.dbs.util.ExceptionCodeConstants;
import com.ai.paas.ipaas.dbs.util.SystemConfig;
import com.ai.paas.ipaas.util.StringUtil;
import com.google.gson.Gson;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

@Service
@Transactional(rollbackFor = Exception.class)
public class DbsPhysicalResourcePoolSv implements IDbsPhysicalResourcePoolSv {

	private static Logger logger = Logger
			.getLogger(DbsPhysicalResourcePoolSv.class);

	@Autowired
	IConfigCenterServiceManageSv configSv;
	@Autowired
	ICCSComponentManageSv manage;
	@Autowired
	ISequenceManageSv iSequenceManageSv;
	@Autowired
	IDbsLogicResourcePoolSv iDbsLogicResourcePoolSv;
	@Autowired
	IDbsUserServiceSv iDbsUserServiceSv;
	@Autowired
	IDbsMuiUserServiceSv iDbsMuiUserServiceSv;
	@Autowired
	IDbsMuiResourcePoolSv iDbsMuiResourcePoolSv;
	
	@Override
	public String applyDistributeDd(OpenResourceParamVo paramVo) throws Exception {
		// 基本的校验
		List<DbsPhysicalResourcePool> modelList = this.validate(paramVo);
		int masterDbCount = paramVo.getMasterNum();
		// 分配库
		Map<String, String> dbMap = allocatDb(paramVo, modelList, masterDbCount);
		
		//对数据库进行主主复制
		//TODO
		//测试环境暂时关闭主主复制功能
		if(paramVo.isAutoSwitch())
		{
			databaseReplication(dbMap);
		}
		
		// 注册配置，logic
		regisDbConfig(paramVo, dbMap);
		//注册DB_Rule_path,like /DBS/DBS1(serviceId)/dbRule={"dbType":"MYSQL","multiTenantMode":false,"allowFullTenantScan":false}
		regisDbRule(paramVo);
		// 注册资源信息到用户节点
		regisDbList(paramVo);
		//分配dbs_mui_resource_pool中的资源，并将url返回
		assignDbsMui(paramVo);
		
		Gson gson=new Gson();
		OpenResourceResultVo resultVo=new OpenResourceResultVo();
		resultVo.setUrl(paramVo.getUrl());
		String result=gson.toJson(resultVo);
		return result;
	}
	//为了实现数据库高可用，将分配后的数据库采用半同步的方式进行主主复制，（前提是mysql的配置文件中bind-address和server-id为不同）
	private void databaseReplication(Map<String,String> dbMap) throws IOException{
		logger.info("主主复制开始 ，信息如下："+dbMap.toString());
		for (Map.Entry<String,String> entry : dbMap.entrySet())
		{
			String value=entry.getValue();
			JSONObject jsonObject=JSONObject.fromObject(value);
			String masterInfo=(String) jsonObject.get("master");
			if(masterInfo!=null)
			{
				String master=jsonObject.getString(masterInfo);
				JSONObject masterIns=JSONObject.fromObject(master);
				String masterUrl=masterIns.getString("url");
				DbsPhysicalResourcePool masterInstance=configMysql(masterUrl,"1");
				JSONArray slaves=jsonObject.getJSONArray("slaves");
				JSONObject slaveInfo=slaves.getJSONObject(0);
				DbsPhysicalResourcePool slaveInstance=new DbsPhysicalResourcePool();
				if(slaveInfo!=null)
				{
					String slaveNum=slaveInfo.getString("slave");
					String slaveIn=jsonObject.getString(slaveNum);
					JSONObject slave=JSONObject.fromObject(slaveIn);
					slaveInstance=configMysql(slave.getString("url"),"2");
				}
				ArrayList<DbsPhysicalResourcePool> resources=new ArrayList<>();
				resources.add(masterInstance);
				resources.add(slaveInstance);
				configReplication(resources);
			}
			
		}
	}
	//替换windows换行符
	public static String replaceIllegalCharacter(String source) {
        if (source == null)
            return source;
        String reg = "[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(source);
        return m.replaceAll("");
    }
	//通过jdbcUrl找到数据库中的root用户及密码，通过agent，对mysql进行重启，并上传shell脚本，对数据库进行更改
	public DbsPhysicalResourcePool configMysql(String jdbcUrl,String serverId) throws IOException{
		logger.info("对数据库进行配置："+jdbcUrl+",serverId 为"+serverId);
		//临时文件上传路径
		String dir="my.cnf";
		String[] tokens=jdbcUrl.split("\\W");
		String ip=tokens[4]+"."+tokens[5]+"."+tokens[6]+"."+tokens[7];
		Integer port=new Integer(tokens[8]);
		String resInstance=tokens[9];
		if(jdbcUrl.startsWith("dt"))
		{
			ip=tokens[6]+"."+tokens[7]+"."+tokens[8]+"."+tokens[9];
			port=new Integer(tokens[10]);
			resInstance=tokens[11];
		}
		logger.info("解析后结果：ip："+ip+",port 为"+port+",resInstance:"+resInstance);
		DbsPhysicalResourcePool instance=new DbsPhysicalResourcePool();
		instance.setResHost(ip);
		instance.setResPort(port);
		instance.setResInstance(resInstance);
		DbsPhysicalResourcePool dbsPhysicalResourcePool=this.getModels(instance).get(0);
		String superUser=dbsPhysicalResourcePool.getResSuperUser();
		String superPassword=dbsPhysicalResourcePool.getResSuperPassword();
		String database=dbsPhysicalResourcePool.getResInstance();
		String bindAddress=dbsPhysicalResourcePool.getResHost();
		int agentPort=dbsPhysicalResourcePool.getAgentPort();
		String confAddress=dbsPhysicalResourcePool.getConfAddr();
		AgentClient agentClient=new AgentClient(ip,agentPort);
		
		ReadableByteChannel readableByteChannel=agentClient.getFile(confAddress+"/my.cnf");
		ByteBuffer byteBuffer = ByteBuffer.allocate(512);
	    try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8");BufferedWriter wr=new BufferedWriter(writer))
		{
	    	while(readableByteChannel.read(byteBuffer) > 0){
	        	byteBuffer.flip();
	        	while(byteBuffer.hasRemaining()){
	                char ch = (char) byteBuffer.get();
	                wr.write(ch);
	            }
	        	byteBuffer.clear();
	        	//
	    	} 	
            wr.flush();
		}
	    
		ArrayList<String> lines=new ArrayList<>();
		try( FileReader reader = new FileReader(dir);BufferedReader br = new BufferedReader(reader))
	    {
			String str = null;
	           
            while((str = br.readLine()) != null) {
            	  if(str.contains("server-id"))
            	  {
            		    lines.add("server-id = "+serverId);
            		    lines.add("bind-address="+bindAddress);
						lines.add("binlog_do_db = "+database);
						lines.add("rpl_semi_sync_master_enabled=1");
						lines.add("rpl_semi_sync_master_timeout=1000");
						lines.add("rpl_semi_sync_slave_enabled=1");
            	  }else{
            		  lines.add(str);
            	  }
                  
            }
	    	
	    }
		try(OutputStream output=new FileOutputStream(dir);Writer writer=new OutputStreamWriter(output,"UTF-8");BufferedWriter wr=new BufferedWriter(writer))
		{
				for(String line:lines)
				{
					writer.write(line);
					writer.write("\n");
				}
	    	   writer.flush();
	            
		}
		try(FileInputStream inputStream = new FileInputStream(dir))
	    {
	    	 agentClient.saveFileStream(confAddress+"/my.cnf",inputStream);
	    }
		agentClient.executeInstruction("mysqladmin -u "+superUser+" --password='"+superPassword+"'  shutdown");
		agentClient.executeInstruction("bash "+confAddress+"/Start_Mysql.sh");
	 
	    //删除临时文件
	    File tempFile=new File(dir);
	    if(tempFile.exists())
	    {
	    	tempFile.delete();
	    }
	    return dbsPhysicalResourcePool;
	}
	//建立主主复制，通过agent执行shell命令
	private void configReplication(ArrayList<DbsPhysicalResourcePool> list) throws IOException{
		for(int i=0;i<list.size();i++)
		{
			DbsPhysicalResourcePool master=list.get(i);
			DbsPhysicalResourcePool slave=new DbsPhysicalResourcePool();
			if(i==0)
			{
				slave=list.get(i+1);
			}else{
				slave=list.get(i-1);
			}
			ArrayList<String> commands=this.confSlave(master, slave);
			AgentClient slaveAgent=new AgentClient(slave.getResHost(),slave.getAgentPort());
			logger.info("主从复制执行语句开始");
			String result=new String();
			for(int j=0;j<commands.size();j++)
			{
				result=slaveAgent.executeInstruction(commands.get(j));
				
				logger.info("执行语句为："+commands.get(j));
				logger.info("agent返回result :"+result);
				if(j==(commands.size()-2))
				{
					if(!result.contains("true"))
					{
						 logger.error("数据库启动失败，主主复制存在问题");
						 throw new PaasRuntimeException("resultMes", "数据库启动失败，主主复制存在问题");
					}
				}
				if(j==(commands.size()-1))
				{
					if(!result.contains("Slave_IO_Running: Yes")&&!result.contains("Slave_SQL_Running: Yes"))
					{
						logger.error("数据库主主复制失败");
						 throw new PaasRuntimeException("resultMes", "数据库主主复制失败");
					}
				}
			}
			
			logger.info("主从复制执行语句结束");
		}
	}
	
	private ArrayList<String> confSlave(DbsPhysicalResourcePool master,DbsPhysicalResourcePool slave)
	{
		
		ArrayList<String> lines=new ArrayList<String>();
		//lines.add("#!/bin/bash");
		//lines.add("mysql -u"+slave.getResUser()+"  -p"+CiperTools.decrypt(slave.getResPassword())+" <<EOF  ");
		String common="mysql -u "+slave.getResSuperUser()+" -p"+CiperTools.decrypt(slave.getResSuperPassword())+" -Bse  ";
		lines.add(common+" \"GRANT REPLICATION SLAVE ON *.* TO '"+master.getResUser()+"'@'%' IDENTIFIED BY '"+CiperTools.decrypt(master.getResPassword())+"'\";");
		lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_master SONAME 'semisync_master.so'\";");
		lines.add(common+" \"INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so'\";");
		/*lines.add(common+"SET GLOBAL rpl_semi_sync_slave_enabled  = 1;");*/
		lines.add(common+" \"stop slave\";");
		lines.add(common+" \"CHANGE MASTER TO MASTER_HOST='"+master.getResHost()+"', MASTER_PORT="+master.getResPort()+",MASTER_USER='"+master.getResUser()+"', MASTER_PASSWORD='"+CiperTools.decrypt(master.getResPassword())+"'\";");
		lines.add(common+" \"start slave\";");
		lines.add("bash "+slave.getConfAddr()+"/Stop_Mysql.sh");
		lines.add("bash "+slave.getConfAddr()+"/Start_Mysql.sh");
		lines.add("bash "+slave.getConfAddr()+"/checkStatus.sh "+slave.getResPort());
		lines.add(common+" \"show slave status \\G \";");
		return lines;
	}
	private boolean isMuiUserServiceExist(OpenResourceParamVo paramVo){
		boolean isexist=false;
		DbsMuiUserService instance=new DbsMuiUserService();
		instance.setUserId(paramVo.getUserId());
		List<DbsMuiUserService> list=iDbsMuiUserServiceSv.getModels(instance);
		if(list!=null&&list.size()!=0)
		{
			DbsMuiResourcePool dbsMuiResourcePool=iDbsMuiResourcePoolSv.findByPkey(list.get(0).getMuiId());
			String url=dbsMuiResourcePool.getMuiUrl();
			paramVo.setUrl(url);
			isexist=true;
		}
		return isexist;
	}
	private void assignDbsMui(OpenResourceParamVo paramVo){
		if(!isMuiUserServiceExist(paramVo))
		{
			DbsMuiUserServiceMapper mapper=ServiceUtil.getMapper(DbsMuiUserServiceMapper.class);
			DbsMuiResourcePoolMapper resourceMapper=ServiceUtil.getMapper(DbsMuiResourcePoolMapper.class);
			DbsMuiResourcePoolCriteria resourceCriteria=new DbsMuiResourcePoolCriteria();
			DbsMuiResourcePoolCriteria.Criteria criteria=resourceCriteria.createCriteria();
			criteria.andStatusEqualTo(DistributeDbConstants.MuiResourceStatus.Free);
			resourceCriteria.setLimitStart(0);
			resourceCriteria.setLimitEnd(1);
			List<DbsMuiResourcePool> list=resourceMapper.selectByExample(resourceCriteria);
			if(list!=null&&list.size()!=0)
			{
				DbsMuiResourcePool instance=list.get(0);
				paramVo.setUrl(instance.getMuiUrl());
				instance.setStatus(DistributeDbConstants.MuiResourceStatus.Used);
				resourceMapper.updateByPrimaryKey(instance);
				int serviceId = iSequenceManageSv.nextVal("service_id");
				DbsMuiUserService dbsMuiUserService=new DbsMuiUserService();
				dbsMuiUserService.setServiceId(serviceId);
				dbsMuiUserService.setMuiId(instance.getMuiId());
				dbsMuiUserService.setUserId(paramVo.getUserId());
				dbsMuiUserService.setStatus(DistributeDbConstants.MuiUserServiceStatus.Effective);
				mapper.insert(dbsMuiUserService);
			}else{
				logger.error("assign dbs mui resource failed");
				throw new PaasRuntimeException("resultMes", "assign dbs mui resource failed");
			}
		}
		
	}
	private void regisDbRule(OpenResourceParamVo paramVo)
	{
		StringBuilder dbRulePathBuilder = new StringBuilder();
		dbRulePathBuilder.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
				.append(DistributeDbConstants.dirSplit)
				.append(paramVo.getServiceId())
				.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DB_RULE_PATH);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("dbType", "MYSQL");
		jsonObject.put("multiTenantMode",Boolean.FALSE);
		jsonObject.put("allowFullTenantScan", Boolean.FALSE);
		
		addConfig(paramVo,jsonObject.toString(),dbRulePathBuilder.toString());
	}
	private void regisDbList(OpenResourceParamVo paramVo) throws PaasException {
		DbsUserService user = this.getUserInfo(paramVo);
		List<DbsLogicResourcePool> logicDbList = getLogicDbList(user);
		String userId = SystemConfig.getString("inner_user_id");
		ConfigInfo config = configSv.getConfigInfo(userId);
		AuthResult authResult = new AuthResult(config.getConfigAddr(),
				config.getConfigUser(), config.getConfigPwd(), userId);
		Map<String, String> configMap = assignSdkUser(paramVo, authResult);
		String dbPattern = SystemConfig.getString("logic_name_pattern");
		assignDbPattern(paramVo, configMap, dbPattern);
		Map<String, String> ipMap = new HashMap<String, String>();
		assignDbListConfig(paramVo, user, logicDbList, configMap, ipMap);
		this.regisDbConfig(paramVo, configMap);
		this.regisDbConfigByIp(authResult, ipMap);
	}

	private void regisDbConfigByIp(AuthResult authResult,
			Map<String, String> ipMap) {

		Set<String> dbPathSet = ipMap.keySet();
		for (String dbPathKey : dbPathSet) {
			String dbConfig = ipMap.get(dbPathKey);
			addConfig(authResult, dbConfig, dbPathKey);
		}

	}

	private void addConfig(AuthResult authResult, String dbConfig, String dbPath) {
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(dbPath);
		param.setUserId(SystemConfig.getString("inner_user_id"));
		param.setPathType(ConfigCenterDubboConstants.PathType.WRITABLE);
		try {
			manage.add(param, dbConfig);
			logger.info("In zk Application information is logged in successfully!");
		} catch (PaasException e) {
			logger.error("In zk Application information is logged in successfully!");
			throw new PaasRuntimeException("resultMes", "In zk application information is not logged!");
		}
	}

	private Map<String, String> assignSdkUser(OpenResourceParamVo paramVo,
			AuthResult authResult) {
		Map<String, String> configMap = new HashMap<String, String>();

		StringBuilder sdkUserPathBuilder = new StringBuilder();
		sdkUserPathBuilder.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
				.append(DistributeDbConstants.dirSplit)
				.append(paramVo.getServiceId());
		sdkUserPathBuilder.append(DistributeDbConstants.dirSplit).append(
				DistributeDbConstants.ConfPath.SDK_USER_INFO);
		JSONObject userObject = new JSONObject();
		userObject.put("configUser", authResult.getConfigUser());
		userObject.put("configPasswd", authResult.getConfigPasswd());
		userObject.put("configAddr", authResult.getConfigAddr());
		userObject.put("userId", authResult.getUserId());
		configMap.put(sdkUserPathBuilder.toString(), userObject.toString());
		return configMap;
	}

	private List<DbsLogicResourcePool> getLogicDbList(DbsUserService user)
			throws PaasException {
		DbsLogicResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsLogicResourcePoolMapper.class);
		DbsLogicResourcePoolCriteria dbsCriteria = new DbsLogicResourcePoolCriteria();
		DbsLogicResourcePoolCriteria.Criteria criteria = dbsCriteria
				.createCriteria();
		criteria.andUsedIdEqualTo(user.getUsedId());
		List<DbsLogicResourcePool> logicDbList = mapper
				.selectByExample(dbsCriteria);

		if (CollectionUtil.isEmpty(logicDbList)) {
			throw new PaasException("", "the user's records for distribute database system is empty"); 
		}
		return logicDbList;
	}

	private DbsUserService getUserInfo(OpenResourceParamVo paramVo)
			throws PaasException {
		DbsUserService user = new DbsUserService();
		// List<DistributeDbUser> userList = distributeDbUserSv.getModels(user);
		DbsUserServiceMapper mapper = ServiceUtil
				.getMapper(DbsUserServiceMapper.class);
		DbsUserServiceCriteria dCriteria = new DbsUserServiceCriteria();
		DbsUserServiceCriteria.Criteria criteria = dCriteria
				.createCriteria();
		criteria.andUserIdEqualTo(paramVo.getUserId());
		criteria.andUserServiceIdEqualTo(paramVo.getServiceId());
		List<DbsUserService> userList = mapper.selectByExample(dCriteria);
		if (CollectionUtil.isEmpty(userList)) {
			throw new PaasException("", "the user's records for distribute database system is empty");
		}
		user = userList.get(0);
		return user;
	}

	private void assignDbListConfig(OpenResourceParamVo paramVo,
			DbsUserService user, List<DbsLogicResourcePool> logicDbList,
			Map<String, String> configMap, Map<String, String> ipMap) {
		StringBuilder realDbPathBuffer = new StringBuilder();
		StringBuilder ipPathBuffer = new StringBuilder();
		StringBuilder monitorPathBuffer=new StringBuilder();
		for (DbsLogicResourcePool distributeLogicDbRes : logicDbList) {
			DbsPhysicalResourcePoolMapper mapper = ServiceUtil
					.getMapper(DbsPhysicalResourcePoolMapper.class);
			DbsPhysicalResourcePoolCriteria dbsPhysicalResourcePoolCriteria = new DbsPhysicalResourcePoolCriteria();
			DbsPhysicalResourcePoolCriteria.Criteria criteria = dbsPhysicalResourcePoolCriteria
					.createCriteria();
			criteria.andLogicIdEqualTo(distributeLogicDbRes.getLogicId());
			criteria.andResUseTypeEqualTo(DistributeDbConstants.ResUseType.BUSI_DB);
			// List<DbsPhysicalResourcePool> distributeDbResList =
			// distributeDbResDao.getModels(distributeDbRes);
			List<DbsPhysicalResourcePool> distributeDbResList = mapper
					.selectByExample(dbsPhysicalResourcePoolCriteria);

			if (!CollectionUtil.isEmpty(distributeDbResList)) {
				for (DbsPhysicalResourcePool distributeDbResDb : distributeDbResList) {
					realDbPathBuffer = new StringBuilder();
					realDbPathBuffer
							.append(DistributeDbConstants.dirSplit)
							.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
							.append(DistributeDbConstants.dirSplit)
							.append(paramVo.getServiceId());
					realDbPathBuffer.append(DistributeDbConstants.dirSplit)
							.append(DistributeDbConstants.ConfPath.REAL_DBS)
							.append(DistributeDbConstants.dirSplit)
							.append(distributeDbResDb.getResHost())
							.append(DistributeDbConstants.underline)
							.append(distributeDbResDb.getResPort())
							.append(DistributeDbConstants.underline)
							.append(distributeDbResDb.getResInstance());

					ipPathBuffer = new StringBuilder();
					ipPathBuffer.append(DistributeDbConstants.dirSplit)
							.append(DistributeDbConstants.ConfPath.DB_LIST)
							.append(DistributeDbConstants.dirSplit)
							.append(distributeDbResDb.getResHost())
							.append(DistributeDbConstants.dirSplit)
							.append(DistributeDbConstants.ConfPath.DB_LIST)
							.append(DistributeDbConstants.dirSplit);
					ipPathBuffer.append(distributeDbResDb.getResHost())
								.append(DistributeDbConstants.underline)
								.append(distributeDbResDb.getResPort())
								.append(DistributeDbConstants.underline)
								.append(distributeDbResDb.getResInstance());

					monitorPathBuffer=new StringBuilder();
					monitorPathBuffer.append(DistributeDbConstants.dirSplit)
								.append(DistributeDbConstants.ConfPath.DB_LIST)
								.append(DistributeDbConstants.dirSplit)
								.append(distributeDbResDb.getResHost())
								.append(DistributeDbConstants.dirSplit)
								.append(DistributeDbConstants.ConfPath.DRDS_DB_MONITOR_PATH)
								.append(DistributeDbConstants.dirSplit);
					monitorPathBuffer.append(distributeDbResDb.getResHost())
								.append(DistributeDbConstants.underline)
								.append(distributeDbResDb.getResPort())
								.append(DistributeDbConstants.underline)
								.append(distributeDbResDb.getResInstance());
							
					JSONObject object = new JSONObject();
					String url = this.getDbConnUrl(distributeDbResDb);
					object.put("url", url);
					object.put("username", distributeDbResDb.getResUser());
					object.put("password", distributeDbResDb.getResPassword());
					object.put("logicDb", distributeLogicDbRes.getLogicName());
					object.put("serviceId", user.getUserServiceId());
					object.put("dbname", distributeDbResDb.getResName());
					object.put("driverClassName","com.mysql.jdbc.Driver");
					String msFlag = distributeDbResDb.getMsFlag();
					if (msFlag != null
							&& DistributeDbConstants.ResMsFlag.SLAVE
									.equals(msFlag)) {
						object.put("isMater", "false");
					} else {
						object.put("isMater", "true");
					}
					configMap.put(realDbPathBuffer.toString(),
							object.toString());
					ipMap.put(ipPathBuffer.toString(), object.toString());
					ipMap.put(monitorPathBuffer.toString(),"");
				}
			}

		}
	}

	private void assignDbPattern(OpenResourceParamVo paramVo,
			Map<String, String> configMap, String dbPattern) {
		StringBuffer patterndBuffer = new StringBuffer();
		patterndBuffer.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
				.append(DistributeDbConstants.dirSplit)
				.append(paramVo.getServiceId());
		patterndBuffer.append(DistributeDbConstants.dirSplit).append(
				DistributeDbConstants.ConfPath.META);
		patterndBuffer.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DB)
				.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DB_PATTERN);

		JSONObject object = new JSONObject();
		object.put("pattern", dbPattern);
		configMap.put(patterndBuffer.toString(), object.toString());
		assignDbTable(paramVo, configMap);
		assignAutoSwitch(paramVo,configMap);
	}
	///meta/db/ha_auto_switch
	private void assignAutoSwitch(OpenResourceParamVo paramVo,Map<String, String> configMap)
	{
		/*Boolean isAutoSwitch=paramVo.isAutoSwitch();
		object.put("autoSwitch",isAutoSwitch.toString());
		*/
		StringBuffer patterndBuffer = new StringBuffer();
		patterndBuffer.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
				.append(DistributeDbConstants.dirSplit)
				.append(paramVo.getServiceId());
		patterndBuffer.append(DistributeDbConstants.dirSplit).append(
				DistributeDbConstants.ConfPath.META);
		patterndBuffer.append(DistributeDbConstants.dirSplit)
		.append(DistributeDbConstants.ConfPath.DB)
		.append(DistributeDbConstants.dirSplit)
		.append(DistributeDbConstants.ConfPath.HA_AUTO_SWITCH);
		
		JSONObject object = new JSONObject();
		Boolean isAutoSwitch=paramVo.isAutoSwitch();
		object.put("ha_auto_switch",isAutoSwitch.toString());
		configMap.put(patterndBuffer.toString(),object.toString());
	}
	
	
	// 在zookeeper上添加一级/meta/table目录，value为空
	private void assignDbTable(OpenResourceParamVo paramVo,
			Map<String, String> configMap) {
		StringBuffer patterndBuffer = new StringBuffer();
		patterndBuffer.append(DistributeDbConstants.dirSplit)
				.append(DistributeDbConstants.ConfPath.DRDS_CONF_PATH)
				.append(DistributeDbConstants.dirSplit)
				.append(paramVo.getServiceId());
		patterndBuffer.append(DistributeDbConstants.dirSplit).append(
				DistributeDbConstants.ConfPath.META);
		patterndBuffer.append(DistributeDbConstants.dirSplit).append(
				DistributeDbConstants.ConfPath.TABLE);
		configMap.put(patterndBuffer.toString(), "");
	}

	private void regisDbConfig(OpenResourceParamVo paramVo,
			Map<String, String> dbMap) {
		Set<String> dbPathSet = dbMap.keySet();
		for (String dbPathKey : dbPathSet) {
			String dbConfig = dbMap.get(dbPathKey);
			addConfig(paramVo, dbConfig, dbPathKey);
		}
	}

	// 待调整，在zk中记录申请信息成功
	private void addConfig(OpenResourceParamVo paramVo, String dbConfig,
			String dbPath) {
		// ICCSComponentManageSv manage=new CCSComponentManageSvImpl();
		CCSComponentOperationParam param = new CCSComponentOperationParam();
		param.setPath(dbPath);
		param.setUserId(paramVo.getUserId());
		param.setPathType(ConfigCenterDubboConstants.PathType.READONLY);
		try {
			manage.add(param, dbConfig);
			logger.info("successed to apply for Information in zookeeper!");
		} catch (PaasException e) {
			logger.error("DBS-ERROR Failed to apply for Information in zookeeper!");
			throw new PaasRuntimeException("resultMes", "Failed to apply for Information in zookeeper!");
		}
	}

	private List<DbsPhysicalResourcePool> validate(OpenResourceParamVo paramVo)
			throws Exception {

		if (paramVo == null) {
			logger.error("DBS-ERROR the parameter of distribute dbs applying service is null");
			throw new PaasException(
					
					ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,
					"the parameter of distribute dbs applying service is null");
		}

		int dbCount = getDbCount(paramVo);

		// 获取未使用物理数据库
		DbsPhysicalResourcePoolMapper dbsPhysicalResourcemapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		DbsPhysicalResourcePoolCriteria dbsPhysicalResourcePoolCriteria = new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria = dbsPhysicalResourcePoolCriteria
				.createCriteria();
		criteria.andIsUsedEqualTo(DistributeDbConstants.ResUseStatus.RES_FREE);
		List<DbsPhysicalResourcePool> modelList = dbsPhysicalResourcemapper
				.selectByExample(dbsPhysicalResourcePoolCriteria);

		if (modelList == null) {
			logger.error("DBS-ERROR Without adequate resources, please apply again later");
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.NO_FREE_RES,
					"Without adequate resources, please apply again later");
		}
		int freeDbSize = modelList.size();
		if (freeDbSize < dbCount) {
			logger.error("DBS-ERROR Without adequate resources, please apply again later");
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.NO_FREE_RES,
					"Without adequate resources, please apply again later");
		}
		return modelList;
	}

	private int getDbCount(OpenResourceParamVo paramVo) {
		int dbCount = paramVo.getMasterNum();
		dbCount = dbCount*2+1;
		return dbCount;
	}

	
	private Map<String, String> allocatDb(OpenResourceParamVo paramVo,
			List<DbsPhysicalResourcePool> modelList, int masterDbCount)
			throws SQLException, PaasException {
		logger.info("=================== DBS-INFO start allocatDb ,"+ paramVo.toString()+"=================================");
		//分配主从库
		Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> map = assignDbsWithMech(
				paramVo, modelList);
		// 分配序列库
		DbsPhysicalResourcePool sequenceDbRes = assignSequenceDb(modelList);

		// 创建序列表
		createSequenceTable(sequenceDbRes);

		// 拼装注册配置
		Map<String, String> dbMap = assembleBusiDbConfig(paramVo, map,sequenceDbRes);

		// 拼装序列库
		assembleSequenceDbConfig(paramVo, sequenceDbRes, dbMap);
		logger.info("=================== DBS-INFO  allocatDb finish ,"+ paramVo.toString()+"=================================");
		return dbMap;

	}

	private void assembleSequenceDbConfig(OpenResourceParamVo paramVo,
			DbsPhysicalResourcePool sequenceDbRes, Map<String, String> dbMap) {
		StringBuilder pathBuffer = getSequenceConfPath(paramVo);
		JSONObject sequenceObject = getSequenceDbConfig(sequenceDbRes);
		dbMap.put(pathBuffer.toString(), sequenceObject.toString());
	}

	private JSONObject getSequenceDbConfig(DbsPhysicalResourcePool sequenceDbRes) {
		JSONObject sequenceObject = new JSONObject();
		sequenceObject.put("driver", "com.mysql.jdbc.Driver");
		sequenceObject.put("username", sequenceDbRes.getResUser());
		sequenceObject.put("password", sequenceDbRes.getResPassword());
		String url = this.getDbConnUrl(sequenceDbRes);
		sequenceObject.put("url", url);
		sequenceObject.put("connectionTimeout",30000);
		sequenceObject.put("idleTimeout",600000);
		sequenceObject.put("maxLifetime",1800000);
		sequenceObject.put("minimumIdle",50);
		sequenceObject.put("maximumPoolSize",200);
		sequenceObject.put("sequenceTable","sequence");
		return sequenceObject;
	}

	private StringBuilder getSequenceConfPath(OpenResourceParamVo paramVo) {
		StringBuilder pathBuffer = new StringBuilder();
		pathBuffer.append(DistributeDbConstants.dirSplit
				+ DistributeDbConstants.ConfPath.DRDS_CONF_PATH
				+ DistributeDbConstants.dirSplit + paramVo.getServiceId());
		pathBuffer.append(DistributeDbConstants.dirSplit
				+ DistributeDbConstants.ConfPath.SEQUENCE_DB_CONF);
		return pathBuffer;
	}

	private void assignMasterDb(DbsPhysicalResourcePool distributeDbRes) {
		distributeDbRes.setMsFlag(DistributeDbConstants.ResMsFlag.MASTER);
		modifyDb(distributeDbRes);
	}

	private void modifyDb(DbsPhysicalResourcePool distributeDbRes) {
		distributeDbRes
				.setCreateTime(new Timestamp(System.currentTimeMillis()));
		distributeDbRes.setIsUsed(DistributeDbConstants.ResUseStatus.RES_USED);
		distributeDbRes.setLastModifyTime(new Timestamp(System
				.currentTimeMillis()));
		distributeDbRes.setResUseType(DistributeDbConstants.ResUseType.BUSI_DB);
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		mapper.updateByPrimaryKey(distributeDbRes);
	}

	private void createSequenceTable(DbsPhysicalResourcePool sequenceDbRes)
			throws PaasException {
		logger.info("=================== DBS-INFO create sequence table =================================");
		
		String url = null;
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			url = this.getDbConnUrl(sequenceDbRes);

			String user = sequenceDbRes.getResUser();

			String password = CiperTools
					.decrypt(sequenceDbRes.getResPassword());

			conn = (Connection) DriverManager
					.getConnection(url, user, password);

			Statement statement = (Statement) conn.createStatement();

			String sql = DistributeDbConstants.sequenceSql;

			statement.execute(sql);
			statement.close();
		} catch (Exception e) {
			logger.error("connect database:" + url + "create table sequence failed,the reason is:" + e.getMessage(), e);
			throw new PaasException("", "The system is busy, please try again later!");

		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
				}
			}
			logger.info("=================== DBS-INFO   sequence table has been created successfully =================================");
		}
	}

	private String getDbConnUrl(DbsPhysicalResourcePool distributeDbRes) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("jdbc:mysql://").append(distributeDbRes.getResHost());
		buffer.append(":").append(distributeDbRes.getResPort());
		buffer.append("/").append(distributeDbRes.getResInstance())
				.append("?autoReconnect=true");
		return buffer.toString();
	}

	private void assignSlaveDb(DbsPhysicalResourcePool slaveDistributeDbRes) {
		slaveDistributeDbRes.setMsFlag(DistributeDbConstants.ResMsFlag.SLAVE);
		modifyDb(slaveDistributeDbRes);
	}

	private DbsPhysicalResourcePool assignSequenceDb(
			List<DbsPhysicalResourcePool> modelList) {
		DbsPhysicalResourcePool sequenceDbRes = modelList
				.get(modelList.size() - 1);
		sequenceDbRes.setMsFlag(DistributeDbConstants.ResMsFlag.MASTER);
		sequenceDbRes.setCreateTime(new Timestamp(System.currentTimeMillis()));
		sequenceDbRes.setIsUsed(DistributeDbConstants.ResUseStatus.RES_USED);
		sequenceDbRes.setLastModifyTime(new Timestamp(System
				.currentTimeMillis()));
		sequenceDbRes
				.setResUseType(DistributeDbConstants.ResUseType.SEQUENCE_DB);
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		mapper.updateByPrimaryKey(sequenceDbRes);
		return sequenceDbRes;
	}

	private Map<String, String> assembleBusiDbConfig(
			OpenResourceParamVo paramVo,
			Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> map,DbsPhysicalResourcePool sequenceDbRes)
			throws SQLException {
		logger.info("==================DBS-INFO assemble json string and log in zookeeper start ======================================");
		List<DbsPhysicalResourcePool> slaveList;
		Map<String, String> dbMap = new HashMap<String, String>();
		Set<DbsPhysicalResourcePool> set = map.keySet();
		int logicDbNum = 1;
		String dbPath = null;
		String logicDbPattern = SystemConfig.getString("logic_name_pattern");
		for (DbsPhysicalResourcePool masterDistributeDbRes : set) {
			String logicDbName = DistributedDBRule.calculatePattern(logicDbNum,
					logicDbPattern);
			slaveList = map.get(masterDistributeDbRes);
			dbPath = getDbPath(paramVo, logicDbName);
			boolean isNeedDistributeTrans = paramVo.isNeedDistributeTrans();
			JSONObject logicDbObject = assembleDbConfig(slaveList,
					masterDistributeDbRes, logicDbName, isNeedDistributeTrans);
			dbMap.put(dbPath, logicDbObject.toString());
			logicDbNum++;
		}

		logicDbNum = 1;
		DbsLogicResourcePool distributeLogicDbRes = null;
		DbsUserServiceMapper mapper = ServiceUtil
				.getMapper(DbsUserServiceMapper.class);
		
		// 新增用户信息
		DbsUserService user = this.getDbUser(paramVo);
		user.setSeqDbId(sequenceDbRes.getResId());
		user.setUsedId(this.getUsedId());
					
		if(paramVo.isNeedDistributeTrans())
		{
				user.setIsTxs(DistributeDbConstants.IsTxs.ISTXS);
				
		}else{
				user.setIsTxs(DistributeDbConstants.IsTxs.WITHOUTTXS);
				
		}

		if(paramVo.isAutoSwitch())
		{
			user.setIsAutoswitch(DistributeDbConstants.IsAutoSwitch.autoSwith);
		}else{
			user.setIsAutoswitch(DistributeDbConstants.IsAutoSwitch.withoutSwitch);
		}
		mapper.insert(user);
		
		for (DbsPhysicalResourcePool masterDistributeDbRes : set) {
			String logicDbName = DistributedDBRule.calculatePattern(logicDbNum,
					logicDbPattern);
			slaveList = map.get(masterDistributeDbRes);
			
			// 新增逻辑库信息
			distributeLogicDbRes = getLogicDb(logicDbName, user);
			DbsLogicResourcePoolMapper dbsLogicResourcePoolmapper = ServiceUtil
					.getMapper(DbsLogicResourcePoolMapper.class);
			dbsLogicResourcePoolmapper.insert(distributeLogicDbRes);
			
			
			// 修改物理库信息
			modifyDbResWithLogicId(masterDistributeDbRes, slaveList,
					distributeLogicDbRes.getLogicId());
			logicDbNum++;
		}
		logger.info("==================DBS-INFO assemble json string and log in zookeeper finished ======================================");
		
		return dbMap;
	}

	private void modifyDbResWithLogicId(
			DbsPhysicalResourcePool masterDistributeDbRes,
			List<DbsPhysicalResourcePool> slaveList, int logicId) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		if (masterDistributeDbRes != null) {
			masterDistributeDbRes.setLogicId(logicId);
			// distributeDbResDao.modModel(masterDistributeDbRes);
			mapper.updateByPrimaryKey(masterDistributeDbRes);
		}

		if (!CollectionUtil.isEmpty(slaveList)) {
			for (DbsPhysicalResourcePool slaveDbRes : slaveList) {
				slaveDbRes.setLogicId(logicId);
				// distributeDbResDao.modModel(slaveDbRes);
				mapper.updateByPrimaryKey(slaveDbRes);
			}
		}
	}

	private DbsLogicResourcePool getLogicDb(String logicDbName,
			DbsUserService user) {
		DbsLogicResourcePool distributeLogicDbRes = new DbsLogicResourcePool();
		distributeLogicDbRes.setCreateTime(new Timestamp(System
				.currentTimeMillis()));
		distributeLogicDbRes.setLogicName(logicDbName);
		distributeLogicDbRes
				.setResUseType(DistributeDbConstants.ResUseType.BUSI_DB);
		distributeLogicDbRes.setUsedId(user.getUsedId());
		distributeLogicDbRes.setLogicId(this.getLogicId());
		return distributeLogicDbRes;
	}

	private DbsUserService getDbUser(OpenResourceParamVo paramVo) {
		DbsUserService dbUser= new DbsUserService();
		dbUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
		dbUser.setUserId(paramVo.getUserId());
		dbUser.setUserServiceId(paramVo.getServiceId());

		return dbUser;
	}

	public  JSONObject assembleDbConfig(
			List<DbsPhysicalResourcePool> slaveList,
			DbsPhysicalResourcePool masterDistributeDbRes, String logicDbName,
			boolean isNeedDistributeTrans) {
		JSONObject logicDbObject = new JSONObject();
		String masterDbName = masterDistributeDbRes.getResName();
		logicDbObject.put("logicDB", logicDbName);
		logicDbObject.put("master", masterDbName);
		JSONObject masterObject = null;
		if (isNeedDistributeTrans) {
			masterObject = this
					.getDbConfigJsonSupportDtm(masterDistributeDbRes);
		} else {
			masterObject = this.getCommonDbConfigJson(masterDistributeDbRes);
		}
		logicDbObject.put(masterDbName, masterObject);
		logicDbObject.put("weight", "30");
		JSONArray array = new JSONArray();
		if (!CollectionUtil.isEmpty(slaveList)) {
			JSONObject slaveDbObject = null;
			String slaveDbName = "";
			for (DbsPhysicalResourcePool slaveDistributeDbRes : slaveList) {
				JSONObject slaveDbArrayObject = assembleSlaveDbConfig(slaveDistributeDbRes);
				array.add(slaveDbArrayObject);
				if (isNeedDistributeTrans) {
					slaveDbObject = this
							.getDbConfigJsonSupportDtm(slaveDistributeDbRes);
				} else {
					slaveDbObject = this
							.getCommonDbConfigJson(slaveDistributeDbRes);
				}
				slaveDbName = slaveDistributeDbRes.getResName();
				logicDbObject.put(slaveDbName, slaveDbObject);
			}
		}
		logicDbObject.put("slaves", array);
		return logicDbObject;
	}

	private JSONObject assembleSlaveDbConfig(
			DbsPhysicalResourcePool slaveDistributeDbRes) {
		JSONObject slaveObject = new JSONObject();
		slaveObject.put("slave", slaveDistributeDbRes.getResName());
		slaveObject.put("slaveMode", "readonly");
		slaveObject.put("weight", "70");
		return slaveObject;
	}

	private JSONObject getCommonDbConfigJson(
			DbsPhysicalResourcePool distributeDbRes) {
		return this.getDbConfigJson(distributeDbRes);
	}

	private JSONObject getDbConfigJsonSupportDtm(
			DbsPhysicalResourcePool distributeDbRes) {
		JSONObject jsonObject = this.getDbConfigJson(distributeDbRes);
		String dtmUrl = this.getDbConnUrlSupportDtm(distributeDbRes);
		jsonObject
				.put("driver",
						"com.ai.paas.ipaas.txs.dtm.jdbc.mysql.DtMySQLDriver");
		jsonObject.put("url", dtmUrl);
		return jsonObject;
	}

	private String getDbConnUrlSupportDtm(
			DbsPhysicalResourcePool distributeDbRes) {
		StringBuilder buffer = new StringBuilder();
		String dbConnUrl = this.getDbConnUrl(distributeDbRes);
		buffer.append("dt@mysql:").append(dbConnUrl);
		return buffer.toString();
	}

	private JSONObject getDbConfigJson(DbsPhysicalResourcePool distributeDbRes) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("driver", "com.mysql.jdbc.Driver");
		jsonObject.put("username", distributeDbRes.getResUser());
		jsonObject.put("password", distributeDbRes.getResPassword());
		String dbConnUrl = this.getDbConnUrl(distributeDbRes);
		jsonObject.put("url", dbConnUrl);
		jsonObject.put("connectionTimeout",30000);
		jsonObject.put("idleTimeout",600000);
		jsonObject.put("maxLifetime",1800000);
		jsonObject.put("minimumIdle",50);
		jsonObject.put("maximumPoolSize",200);
		/*jsonObject.put("maxWait", "10000");
		jsonObject.put("validationQuery", "SELECT 1");
		jsonObject.put("testWhileIdle", "true");*/
		return jsonObject;
	}

	public String getDbPath(OpenResourceParamVo paramVo, String logicDbName) {
		StringBuilder pathBuffer = new StringBuilder();
		pathBuffer.append(DistributeDbConstants.dirSplit
				+ DistributeDbConstants.ConfPath.DRDS_CONF_PATH
				+ DistributeDbConstants.dirSplit + paramVo.getServiceId());
		pathBuffer.append(DistributeDbConstants.dirSplit
				+ DistributeDbConstants.ConfPath.DISTRIBUTEDB_CONF_PATH);
		pathBuffer.append(DistributeDbConstants.dirSplit + logicDbName);
		return pathBuffer.toString();
	}

	@Override
	public DbsPhysicalResourcePool findByPkey(Integer resId) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		DbsPhysicalResourcePoolCriteria dbscriteria = new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria = dbscriteria
				.createCriteria();
		criteria.andResIdEqualTo(resId);
		List<DbsPhysicalResourcePool> list = mapper
				.selectByExample(dbscriteria);
		return list.get(0);
	}

	@Override
	public List<DbsPhysicalResourcePool> getModels(
			DbsPhysicalResourcePool dbsPhysicalResourcePool) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		DbsPhysicalResourcePoolCriteria dbscriteria = DbsPhysicalResourcePoolSv
				.toCriteria(dbsPhysicalResourcePool);
		List<DbsPhysicalResourcePool> list = mapper
				.selectByExample(dbscriteria);
		return list;
	}

	@Override
	public void addModel(DbsPhysicalResourcePool dbsPhysicalResourcePool) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		mapper.insert(dbsPhysicalResourcePool);
	}

	@Override
	public void modModel(DbsPhysicalResourcePool dbsPhysicalResourcePool) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		mapper.updateByPrimaryKey(dbsPhysicalResourcePool);
	}

	@Override
	public void delModel(Integer resId) {
		DbsPhysicalResourcePoolMapper mapper = ServiceUtil
				.getMapper(DbsPhysicalResourcePoolMapper.class);
		mapper.deleteByPrimaryKey(resId);
	}

	public static DbsPhysicalResourcePoolCriteria toCriteria(
			DbsPhysicalResourcePool instance) {
		DbsPhysicalResourcePoolCriteria dbsCriteria = new DbsPhysicalResourcePoolCriteria();
		DbsPhysicalResourcePoolCriteria.Criteria criteria = dbsCriteria
				.createCriteria();
		if(instance.getResId()!=null)
		{
			criteria.andResIdEqualTo(instance.getResId());
		}
		if(instance.getResPort()!=null)
		{
			criteria.andResPortEqualTo(instance.getResPort());
		}
		if(instance.getLogicId()!=null)
		{
			criteria.andLogicIdEqualTo(instance.getLogicId());
		}
		
		if (!StringUtil.isBlank(instance.getCreateAuthor())) {
			criteria.andCreateAuthorEqualTo(instance.getCreateAuthor());
		}
		if (!StringUtil.isBlank(instance.getIsUsed())) {
			criteria.andIsUsedEqualTo(instance.getIsUsed());
		}
		if (!StringUtil.isBlank(instance.getLastModifyAuthor())) {
			criteria.andLastModifyAuthorEqualTo(instance.getLastModifyAuthor());
		}
		if (!StringUtil.isBlank(instance.getMsFlag())) {
			criteria.andMsFlagEqualTo(instance.getMsFlag());
		}
		if (!StringUtil.isBlank(instance.getResHost())) {
			criteria.andResHostEqualTo(instance.getResHost());
		}
		if (!StringUtil.isBlank(instance.getResInstance())) {
			criteria.andResInstanceEqualTo(instance.getResInstance());
		}
		if (!StringUtil.isBlank(instance.getResName())) {
			criteria.andResNameEqualTo(instance.getResName());
		}
		if (!StringUtil.isBlank(instance.getResPassword())) {
			criteria.andResPasswordEqualTo(instance.getResPassword());
		}
		if (!StringUtil.isBlank(instance.getResStatus())) {
			criteria.andResStatusEqualTo(instance.getResStatus());
		}
		if (!StringUtil.isBlank(instance.getResUser())) {
			criteria.andResUserEqualTo(instance.getResUser());
		}
		if (!StringUtil.isBlank(instance.getResUseType())) {
			criteria.andResUseTypeEqualTo(instance.getResUseType());
		}
		if (instance.getCreateTime() != null) {
			criteria.andCreateTimeEqualTo(instance.getCreateTime());
		}
		if (instance.getLastModifyTime() != null) {
			criteria.andLastModifyTimeEqualTo(instance.getLastModifyTime());
		}
		if (instance.getUsedTime() != null) {
			criteria.andUsedTimeEqualTo(instance.getUsedTime());
		}
		return dbsCriteria;
	}

	// 依据主从数据库尽量不在一个物理机的原则分配主从库
	public Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> assignDbsWithMech(
			OpenResourceParamVo paramVo, List<DbsPhysicalResourcePool> modelList)
			throws PaasException {
		if (paramVo == null) {
			logger.error("=================== DBS-ERROR the parameter of Distributed database application services is null=================================");
			
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,
					"the parameter of Distributed database application services is null");
		}

		int dbCount = paramVo.getMasterNum();
		if (modelList == null) {
			logger.error("=================== DBS-ERROR Without adequate resources, please apply again later =================================");
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.NO_FREE_RES,
					"Without adequate resources, please apply again later");
		}
		int freeDbSize = modelList.size();
		if (freeDbSize < dbCount) {
			logger.error("=================== DBS-ERROR Without adequate resources, please apply again later =================================");
			throw new PaasException(
					ExceptionCodeConstants.DubboServiceCode.NO_FREE_RES,
					"Without adequate resources, please apply again later");
		}
		// 从数据库中查找空余机器，组成hashmap<String,list>,其中key 为ip，list为同一ip下的物理库集合
		HashMap<String, ArrayList<DbsPhysicalResourcePool>> model = new HashMap<>();
		for (DbsPhysicalResourcePool instance : modelList) {
			ArrayList<DbsPhysicalResourcePool> list = model.get(instance
					.getResHost());

			if (list != null && !list.contains(instance)) {
				list.add(instance);
			}
			if (model.get(instance.getResHost()) == null) {
				if (list == null) {
					list = new ArrayList<>();
				}
				list.add(instance);
				model.put(instance.getResHost(), list);
			}
		}
		int dbNum = paramVo.getMasterNum();
		List<DbsPhysicalResourcePool> masterdb = new ArrayList<DbsPhysicalResourcePool>();
		Set<String> ipCollection=model.keySet();
		Object[] array=ipCollection.toArray();
		// 分配主库，尽量保证主库的IP不同
		while (dbNum>0) {
			int no=dbNum%array.length;
			String key=(String) array[no];
			ArrayList<DbsPhysicalResourcePool> entry=model.get(key);
			if (entry!= null && entry.size() != 0) {
				DbsPhysicalResourcePool dbsInstance = entry.get(
						0);
				masterdb.add(dbsInstance);
				assignMasterDb(dbsInstance);
				entry.remove(0);
				--dbNum;
			} 
			if(entry.size()==0)
			{
				model.remove(key);
			}
		}
		
		modelList.removeAll(masterdb);
		
		Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> map =new HashMap<>();
		//TODO 新增读写分离需求，如果不需要，不自动分配从库
		if(!paramVo.isMysqlProxy())
		{
			for(DbsPhysicalResourcePool instance:masterdb)
			{
				map.put(instance, new ArrayList<DbsPhysicalResourcePool>());
			}
		}else{
			map = assignSlaveByMech(masterdb, model);
		}
		
		return map;
	}

	// 分配从库
	private Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> assignSlaveByMech(
			List<DbsPhysicalResourcePool> masterdb,
			HashMap<String, ArrayList<DbsPhysicalResourcePool>> model) {
		HashMap<String, ArrayList<DbsPhysicalResourcePool>> slaveIns = removeEmpty(model);

		Map<DbsPhysicalResourcePool, List<DbsPhysicalResourcePool>> map = new HashMap<>();
		// 分配从库，尽量保证从库IP与主库不同
		for (DbsPhysicalResourcePool master : masterdb) {
			DbsPhysicalResourcePool slave = new DbsPhysicalResourcePool();
			if(slaveIns.size()==1)
			{
				Iterator<Entry<String, ArrayList<DbsPhysicalResourcePool>>> iterator = slaveIns
						.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, ArrayList<DbsPhysicalResourcePool>> entry = iterator
							.next();
					ArrayList<DbsPhysicalResourcePool> list = entry.getValue();
					slave = list.get(0);
					slave.setMsFlag(DistributeDbConstants.ResMsFlag.SLAVE);
					assignSlaveDb(slave);
					list.remove(0);
				}
			}else{
				Iterator<Entry<String, ArrayList<DbsPhysicalResourcePool>>> iterator = slaveIns
						.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry<String, ArrayList<DbsPhysicalResourcePool>> entry = iterator
							.next();
					if(!entry.getKey().equals(master.getResHost()))
					{
						ArrayList<DbsPhysicalResourcePool> list = entry.getValue();
						if (list == null || list.size() == 0) {
							continue;
						} else {
							slave = list.get(0);
							slave.setMsFlag(DistributeDbConstants.ResMsFlag.SLAVE);
							assignSlaveDb(slave);
							list.remove(0);
							break;
						}
					}
					

				}
			}
			ArrayList<DbsPhysicalResourcePool> slaveList=new ArrayList<>();
			slaveList.add(slave);
			map.put(master, slaveList);
		}
		return map;
	}

	private HashMap<String, ArrayList<DbsPhysicalResourcePool>> removeEmpty(
			HashMap<String, ArrayList<DbsPhysicalResourcePool>> model) {
		Iterator<Entry<String, ArrayList<DbsPhysicalResourcePool>>> iterator = model
				.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, ArrayList<DbsPhysicalResourcePool>> entry = iterator
					.next();
			if (entry.getValue().size() == 0) {
				iterator.remove();
			}
		}

		return model;
	}



	
	
	private int getLogicId(){
		int nextval = iSequenceManageSv.nextVal("logic_id");
		
		return nextval;
	}
	private int getUsedId(){
		int nextval = iSequenceManageSv.nextVal("used_id");
		
		return nextval;
	}
}
