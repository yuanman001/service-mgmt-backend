package com.ai.paas.ipaas.rds.service.impl;

import java.sql.Time;
import java.sql.Timestamp;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsResourcePoolMapper;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSResourcePool;
import com.ai.paas.ipaas.rds.service.util.GsonSingleton;
import com.alibaba.dubbo.config.annotation.Service;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午2:43:47 
 * @version 
 * @since  
 */
//@Service("RDSResourcePool")
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSResourcePool implements IRDSResourcePool {

	@Autowired
	GsonSingleton g;

	
	public void printLog(String a){
		Logger log = Logger.getLogger(RDSResourcePool.class.getName());
		log.info(a);
	}
	@Override
	public String add(String getParam) {
		Timestamp time = new Timestamp(System.currentTimeMillis());
		RdsResourcePool rp = g.getGson().fromJson(getParam, RdsResourcePool.class);
		rp.setStatus(1);
		rp.setCycle(1);
		rp.setInstancecreatetime(time);
		rp.setInstancelastupdatetime(time);
		RdsResourcePoolMapper rdsResMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		rdsResMapper.insert(rp);
		return "success";
	}

	@Override
	public String delete(String getParam) {
		RdsResourcePool rp = g.getGson().fromJson(getParam, RdsResourcePool.class);
		RdsResourcePoolMapper rdsResMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
		rdsResMapper.deleteByPrimaryKey(rp.getResourceid());
		return "success";
	}

	@Override
	public String get(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String addImage(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteImage(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getImage(String getParam) {
		// TODO Auto-generated method stub
		return null;
	}

}
