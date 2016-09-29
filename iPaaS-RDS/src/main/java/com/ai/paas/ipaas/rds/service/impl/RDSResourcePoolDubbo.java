package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSResourcePool;
import com.alibaba.dubbo.config.annotation.Service;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午2:43:47 
 * @version 
 * @since  
 */
@Service
public class RDSResourcePoolDubbo implements IRDSResourcePool {

	@Autowired
	private RDSResourcePool rdsRes;
	@Override
	public String add(String getParam) {
		return rdsRes.add(getParam);
	}

	@Override
	public String delete(String getParam) {
		return rdsRes.delete(getParam);
	}

	@Override
	public String get(String getParam) {
		return rdsRes.get(getParam);
	}

	@Override
	public String addImage(String getParam) {
		return rdsRes.addImage(getParam);
	}

	@Override
	public String deleteImage(String getParam) {
		return rdsRes.deleteImage(getParam);
	}

	@Override
	public String getImage(String getParam) {
		return rdsRes.getImage(getParam);
	}


}
