package com.ai.paas.ipaas.rds.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsIncBaseMapper;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBaseCriteria;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceOperater;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetIncInfo;
import com.ai.paas.ipaas.rds.service.util.GsonSingleton;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:50:48 
 * @version 
 * @since  
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RDSInstanceOperater implements IRDSInstanceOperater {

	@Override
	public String changesinstancebase(String changesinstancebase) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changesinstanceipport(String changesinstanceipport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String changesinstancebaseconfig(String changesinstancebaseconfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createslaver(String createslaver) {
		
		return null;
	}

	@Override
	public String cancelslaver(String cancelslaver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createbatmaster(String createbatmaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelbatmaster(String cancelbatmaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String switchmaster(String switchmaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancebaseinfo(String getinstancebaseinfo) {
		return null;
	}

	@Override
	public String getinstanceipport(String getinstanceipport) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancestatus(String getinstancestatus) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancebaseconfig(String getinstancebaseconfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancespaceinfo(String getinstancespaceinfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstanceslaver(String getinstanceslaver) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancebatmaster(String getinstancebatmaster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getinstancewholeinfo(String getinstancewholeinfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
