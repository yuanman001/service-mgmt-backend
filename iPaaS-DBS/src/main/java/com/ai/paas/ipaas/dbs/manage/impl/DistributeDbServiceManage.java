package com.ai.paas.ipaas.dbs.manage.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.PaasRuntimeException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.dbs.dao.interfaces.DbsUserServiceMapper;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiResourcePool;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsMuiUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserService;
import com.ai.paas.ipaas.dbs.dao.mapper.bo.DbsUserServiceCriteria;
import com.ai.paas.ipaas.dbs.manage.rest.interfaces.IDistributeDbServiceManager;
import com.ai.paas.ipaas.dbs.manage.vo.DbsMuiAuthParamVo;
import com.ai.paas.ipaas.dbs.manage.vo.DbsMuiAuthResultVo;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceParamVo;
import com.ai.paas.ipaas.dbs.manage.vo.OpenResourceResultVo;
import com.ai.paas.ipaas.dbs.service.IDbsMuiResourcePoolSv;
import com.ai.paas.ipaas.dbs.service.IDbsMuiUserServiceSv;
import com.ai.paas.ipaas.dbs.service.IDbsPhysicalResourcePoolSv;
import com.ai.paas.ipaas.dbs.util.DistributeDbConstants;
import com.ai.paas.ipaas.dbs.util.ExceptionCodeConstants;
import com.ai.paas.ipaas.util.StringUtil;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
@Service
public class DistributeDbServiceManage implements IDistributeDbServiceManager {

	private static Logger logger = Logger.getLogger(DistributeDbServiceManage.class);
	@Autowired
	private IDbsPhysicalResourcePoolSv iDbsPhysicalResourcePoolSv;
	@Autowired
	private IDbsMuiUserServiceSv iDbsMuiUserServiceSv;
	@Autowired
	private IDbsMuiResourcePoolSv iDbsMuiResourcePoolSv;
	@Override
	public String cancel(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(String param) {
		logger.info("DistributeDbResDubboSvImpl -> applyDistributeDb ========================");
		Gson gson=new Gson();
		OpenResourceParamVo  openResourceParamVo=gson.fromJson(param, OpenResourceParamVo.class);
		OpenResourceResultVo  openResourceResultVo = new OpenResourceResultVo();
		try{
			if(param == null){
				throw new PaasException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter for appllying database is null");
			}
		    this.valParamVo(openResourceParamVo);
		    String result=iDbsPhysicalResourcePoolSv.applyDistributeDd(openResourceParamVo);
		    openResourceResultVo=gson.fromJson(result, OpenResourceResultVo.class);
		    openResourceResultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SUCCESS_CODE);
		    openResourceResultVo.setResultMsg(ExceptionCodeConstants.DubboServiceCode.SUCCESS_MESSAGE);
		}catch(PaasException e) {
			logger.error("there occurs a problem when applied for distribute database,the reason is :" + e.getMessage());
			openResourceResultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE);
			openResourceResultVo.setResultMsg(e.getMessage());
		}catch(Exception e) {
			logger.error(logger,e);
			openResourceResultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE);
			openResourceResultVo.setResultMsg(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_MESSAGE);
		}
		
		return gson.toJson(openResourceResultVo);
	}

	@Override
	public String modify(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String restart(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String param) {
		// TODO Auto-generated method stub
		return null;
	}

	private void valParamVo(OpenResourceParamVo paramVo) throws Exception{
		
		if(StringUtil.isBlank(paramVo.getUserId())) {
			throw new PaasRuntimeException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter of user Id can not be empty");
		}
		
		if(paramVo.getMasterNum() <= 0) {
			throw new PaasRuntimeException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter of master number can not be empty");
		}
		
		if(StringUtil.isBlank(paramVo.getServiceId())){
			throw new PaasRuntimeException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter of service Id can not be empty");
		}
		
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	//验证 
	@Override
	public String checkMUIAuth(String param) {
		System.out.println("DistributeDbResDubboSvImpl -> checkMUIAuth ========================");
		Gson gson=new Gson();
		
		DbsMuiAuthResultVo resultVo=new DbsMuiAuthResultVo();
		DbsMuiAuthParamVo dbsMuiAuthParamVo=null;
		//返回状态
		boolean status=false;
		try{
			if(param == null){
				throw new PaasException(ExceptionCodeConstants.DubboServiceCode.PARAM_IS_NULL,"the parameter for check mui auth is null");
			}
		    dbsMuiAuthParamVo=gson.fromJson(param, DbsMuiAuthParamVo.class);
		    String userId=dbsMuiAuthParamVo.getUserId();
		    String url=dbsMuiAuthParamVo.getUrl();
		    DbsMuiUserService dbsMuiUserService=new DbsMuiUserService();
		    dbsMuiUserService.setUserId(userId);
		    List<DbsMuiUserService> list=iDbsMuiUserServiceSv.getModels(dbsMuiUserService);
		    if(list!=null&&list.size()==1)
		    {
		    	DbsMuiUserService dbsInstance=list.get(0);
		    	DbsMuiResourcePool dbsMuiResourcePool=iDbsMuiResourcePoolSv.findByPkey(dbsInstance.getMuiId());
		    	if(url.equals(dbsMuiResourcePool.getMuiUrl())&&dbsInstance.getStatus().equals(DistributeDbConstants.MuiUserServiceStatus.Effective))
		    	{
		    		status=true;
		    		 resultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SUCCESS_CODE);
		 		    resultVo.setResultMsg(ExceptionCodeConstants.DubboServiceCode.SUCCESS_MESSAGE);
		    	}
		    }else{
		    	resultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE);
				resultVo.setResultMsg(ExceptionCodeConstants.DubboServiceCode.SYSTEM_QUERY_FAILED);
		    }
		    resultVo.setStatus(status);
		   
		}catch(PaasException e) {
			logger.error("there occurs a problem when check dbs mui auth,the reason is :" + e.getMessage(),e);
			resultVo.setResultCode(e.getErrCode());
			resultVo.setResultMsg(e.getMessage());
		}catch(Exception e) {
			logger.error("there occurs a problem when check dbs mui auth,the reason is :" + e.getMessage(),e);
			resultVo.setResultCode(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_CODE);
			resultVo.setResultMsg(ExceptionCodeConstants.DubboServiceCode.SYSTEM_ERROR_MESSAGE);
		}
		
		return gson.toJson(resultVo);
	}
	
	@Override
	public String isTxs(String param) {
		Gson gson=new Gson();
		JsonObject instance=gson.fromJson(param, JsonObject.class);
		String userId=instance.get("userId").getAsString();
		String serviceId=instance.get("serviceId").getAsString();
		DbsUserServiceMapper mapper=ServiceUtil.getMapper(DbsUserServiceMapper.class);
		DbsUserServiceCriteria dbscriteria=new DbsUserServiceCriteria();
		DbsUserServiceCriteria.Criteria criteria=dbscriteria.createCriteria();
		criteria.andUserIdEqualTo(userId);
		criteria.andUserServiceIdEqualTo(serviceId);
		List<DbsUserService> list=mapper.selectByExample(dbscriteria);
		JsonObject value=new JsonObject();
		if(list!=null&&list.size()!=0)
		{
			Integer isTxs=new Integer(list.get(0).getIsTxs());
			boolean flag=false;
			if(isTxs.intValue()==1)
			{
				flag=true;
			}
			value.addProperty("res",flag);
		}
		return value.toString();
	}
	
}
