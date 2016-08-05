package com.ai.paas.ipaas.rds.service.transfer.vo;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;

/** 
 * 必须字段-Create
 * token/
 * instanceName/
 * user_id/
 * serial_number/
 * instancenetworktype/
 * instancespaceinfo/
 * instancebaseconfig/
 * instanceimagebelonger/
 * instancerootaccount/
 * 可选字段
 * instanceslaver/
 * instancebatmaster/
 * 生成字段-RDSResourcePlan
 * instanceipport/
 * instancestatus/
 * instanceresourcebelonger/
 * 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月12日 下午2:53:14 
 * @version 
 * @since  
 */
public class CreateRDS extends Object{
	public RdsIncBase instanceBase;
	public int createBatmasterNum;// max-1
	public int createSlaverNum;   // max-n
	
}
