package com.ai.paas.ipaas.rds.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceOperater;
import com.alibaba.dubbo.config.annotation.Service;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:50:48 
 * @version 
 * @since  
 */
@Service
public class RDSInstanceOperaterDubbo implements IRDSInstanceOperater {

	@Autowired
	private RDSInstanceOperater rdsIncOperater;

	@Override
	public String changesinstancebase(String changesinstancebase) {
		return rdsIncOperater.changesinstancebase(changesinstancebase);
	}

	@Override
	public String changesinstanceipport(String changesinstanceipport) {
		return rdsIncOperater.changesinstanceipport(changesinstanceipport);
	}

	@Override
	public String changesinstancebaseconfig(String changesinstancebaseconfig) {
		return rdsIncOperater.changesinstancebaseconfig(changesinstancebaseconfig);
	}

	@Override
	public String createslaver(String createslaver) {
		return rdsIncOperater.createslaver(createslaver);
	}

	@Override
	public String cancelslaver(String cancelslaver) {
		return rdsIncOperater.cancelslaver(cancelslaver);
	}

	@Override
	public String createbatmaster(String createbatmaster) {
		return rdsIncOperater.createbatmaster(createbatmaster);
	}

	@Override
	public String cancelbatmaster(String cancelbatmaster) {
		return rdsIncOperater.cancelbatmaster(cancelbatmaster);
	}

	@Override
	public String switchmaster(String switchmaster) {
		return rdsIncOperater.switchmaster(switchmaster);
	}

	@Override
	public String getinstancebaseinfo(String getinstancebaseinfo) {
		return rdsIncOperater.getinstancebaseinfo(getinstancebaseinfo);
	}

	@Override
	public String getinstanceipport(String getinstanceipport) {
		return rdsIncOperater.getinstanceipport(getinstanceipport);
	}

	@Override
	public String getinstancestatus(String getinstancestatus) {
		return rdsIncOperater.getinstancestatus(getinstancestatus);
	}

	@Override
	public String getinstancebaseconfig(String getinstancebaseconfig) {
		return rdsIncOperater.getinstancebaseconfig(getinstancebaseconfig);
	}

	@Override
	public String getinstancespaceinfo(String getinstancespaceinfo) {
		return rdsIncOperater.getinstancespaceinfo(getinstancespaceinfo);
	}

	@Override
	public String getinstanceslaver(String getinstanceslaver) {
		return rdsIncOperater.getinstanceslaver(getinstanceslaver);
	}

	@Override
	public String getinstancebatmaster(String getinstancebatmaster) {
		return rdsIncOperater.getinstancebatmaster(getinstancebatmaster);
	}

	@Override
	public String getinstancewholeinfo(String getinstancewholeinfo) {
		return rdsIncOperater.getinstancewholeinfo(getinstancewholeinfo);
	}
}
