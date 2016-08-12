package com.ai.paas.ipaas.rds.service.transfer.vo;


import java.util.LinkedList;
import java.util.List;

import com.ai.paas.ipaas.rds.dao.wo.InstanceBase;


/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月14日 下午2:28:11 
 * @version 
 * @since  
 */
public class CreateRDSResult extends RDSResult{

	public boolean isInstanceConfig;
	public boolean isInstanceRun;
	public InstanceBase Instance;
	public List<InstanceBaseSimple> incSimList = new LinkedList<InstanceBaseSimple>(); 
//	public boolean isExistBatMaster;
//	public boolean isBatMasterConfig;
//	public boolean isBatMasterRun;
//	public InstanceBase BatMasterInstance;
//	public boolean isExistGroupSlaver;
//	public boolean isSlaverGroupConfig;
//	public boolean isSlaverGroupRun;
//	public List<InstanceBase> slaverGroupInstance;
	
	public CreateRDSResult(int status) {
		super(status);
		// TODO Auto-generated constructor stub
	}
	public CreateRDSResult(int status, String discribe, String content, String exceptionTrace) {
		super(status,discribe,content,exceptionTrace);
		// TODO Auto-generated constructor stub
	}
	public void setStatus(int status){
		super.setStatus(status);
	}

}
