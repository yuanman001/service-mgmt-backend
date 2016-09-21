package com.ai.paas.ipaas.rds.dao.wo;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.service.constant.InstanceType;

/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年9月21日 上午10:48:33 
 * @version 
 * @since  
 */
public class InstanceGroup {

	RdsIncBase master;
	RdsIncBase bakMaster;
	List<RdsIncBase> slaveList;
	public static InstanceGroup getGroupFromInstanceStack(Stack<RdsIncBase> instanceStack) {
		InstanceGroup group = new InstanceGroup();
		group.slaveList = new LinkedList<RdsIncBase>();
		for (RdsIncBase inc : instanceStack){
			if(inc.getIncType() == InstanceType.MASTER){
				group.master = inc;
			}
			if(inc.getIncType() == InstanceType.BATMASTER){
				group.bakMaster = inc;
			}
			if(inc.getIncType() == InstanceType.SLAVER){
				group.slaveList.add(inc);
			}
		}
		return group;
	}
}
