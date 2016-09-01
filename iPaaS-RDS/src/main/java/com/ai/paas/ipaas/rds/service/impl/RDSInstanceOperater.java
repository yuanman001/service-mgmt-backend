package com.ai.paas.ipaas.rds.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsIncBaseMapper;
import com.ai.paas.ipaas.rds.dao.interfaces.RdsResourcePoolMapper;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceOperater;
import com.ai.paas.ipaas.rds.service.constant.InstanceType;
import com.ai.paas.ipaas.rds.service.constant.ResponseResultMark;
import com.ai.paas.ipaas.rds.service.transfer.vo.SwitchMaster;
import com.ai.paas.ipaas.rds.service.transfer.vo.SwitchMasterResult;
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

	@Autowired
	GsonSingleton g;

	@Autowired
	ICCSComponentManageSv iCCSComponentManageSv;
	
	private RdsIncBaseMapper incMapper = ServiceUtil.getMapper(RdsIncBaseMapper.class);
	private RdsResourcePoolMapper resMapper = ServiceUtil.getMapper(RdsResourcePoolMapper.class);
	
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
		SwitchMaster sm = g.getGson().fromJson(switchmaster, SwitchMaster.class);
		SwitchMasterResult smr = new SwitchMasterResult();
		Stack<RdsIncBase> rdsIncStack = getInstanceStack(sm.getMasterId());
		RdsIncBase masterInc = null;
		RdsIncBase bakInc = null;
		List<RdsIncBase> slaverIncList = new LinkedList<RdsIncBase>();
		for(RdsIncBase inc : rdsIncStack){
			if(inc.getIncType() == InstanceType.MASTER){
				masterInc = inc;
			}
			if(inc.getIncType() == InstanceType.SLAVER){
				bakInc = inc;
			}
			if(inc.getIncType() == InstanceType.BATMASTER){
				slaverIncList.add(inc);
			}
		}
		// 修改数据库
		if(masterInc == null || bakInc == null){
			RdsIncBase rdsInc = new RdsIncBase();
			rdsInc.setBakId(masterInc.getBakId());
			rdsInc.setSlaverId(masterInc.getSlaverId());
			if(masterInc.getIncName().contains("-staybakmaster"))
			{	masterInc.setIncName(masterInc.getIncName().substring(0, masterInc.getIncName().length()-14));}
			else
			{	masterInc.setIncName(masterInc.getIncName() + "-staybakmaster");}
			masterInc.setIncType(InstanceType.BATMASTER);
			masterInc.setMasterid(bakInc.getId());
			masterInc.setBakId("");
			masterInc.setSlaverId("");
			
			if(bakInc.getIncName().contains("-staybakmaster"))
			{	bakInc.setIncName(bakInc.getIncName().substring(0, bakInc.getIncName().length()-14));}
			else
			{	bakInc.setIncName(bakInc.getIncName() + "-staybakmaster");}
			bakInc.setIncType(InstanceType.MASTER);
			bakInc.setMasterid(0);
			bakInc.setBakId(rdsInc.getBakId());
			bakInc.setSlaverId(rdsInc.getSlaverId());
			
			if(slaverIncList.size() > 0){
				for(int i = 0; i < slaverIncList.size(); i++){
					slaverIncList.get(i).setMasterid(bakInc.getId());
				}
			}
			
			incMapper.updateByPrimaryKey(bakInc);
			incMapper.updateByPrimaryKey(masterInc);
			if(slaverIncList.size() > 0){
				for(int i = 0; i < slaverIncList.size(); i++){
					incMapper.updateByPrimaryKey(slaverIncList.get(i));
				}
			}
		} else {
			smr.setStatus(ResponseResultMark.ERROR_INSTANCE_GROUP_CANNOT_GET_NULL);
		}
		
		// 修改配置
		switchConfig(masterInc,bakInc,slaverIncList);
		
		return g.getGson().toJson(smr);
	}


	private void switchConfig(RdsIncBase masterInc, RdsIncBase bakInc, List<RdsIncBase> slaverIncList) {
		// TODO Auto-generated method stub
		
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
	
	/**
	 * 通过一个实例id查询到实例和实例的主备、主从实例
	 * @param instanceid
	 * @return
	 */
	public Stack<RdsIncBase> getInstanceStack(int instanceid) {
		Stack<RdsIncBase> instanceStack = new Stack<RdsIncBase>();
//		RdsIncBaseMapper ibm = ServiceUtil.getMapper(RdsIncBaseMapper.class);
		RdsIncBase instanceInfo = incMapper.selectByPrimaryKey(instanceid);
		
		if(null != instanceInfo){
			instanceStack.push(instanceInfo);
		}else {
			return instanceStack;
		}
		if(InstanceType.MASTER == instanceInfo.getIncType()){
//			System.out.println("$$$$$$$$$$$$$$$$$$$$"+instanceInfo.getBakId());
			if(null != instanceInfo.getBakId() && !instanceInfo.getBakId().equals("")){
				RdsIncBase rib = incMapper.selectByPrimaryKey(getIdArrayFromString(instanceInfo.getBakId()).get(0));
				instanceStack.push(rib);
			}
			if(null != instanceInfo.getSlaverId() && !instanceInfo.getSlaverId().equals("")){
				for(Integer is : getIdArrayFromString(instanceInfo.getSlaverId())){
					RdsIncBase ribs = incMapper.selectByPrimaryKey(is);
					instanceStack.push(ribs);
				}
			}
		}
		
		return instanceStack;
	}
	private List<Integer> getIdArrayFromString(String bakId) {
		// 如果使用split作为分隔符，则
		String[] idArray = bakId.split("\\|");
		ArrayList<Integer> idList = new ArrayList<Integer>();
		for(int i = 0; i < idArray.length; i++){
			if(idArray[i] != null && !idArray[i].isEmpty() && !idArray[i].equals(""))
				idList.add(Integer.valueOf(idArray[i]));
		}
		return idList;
	}

}
