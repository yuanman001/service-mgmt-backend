package com.ai.paas.ipaas.dbs.manage.vo;

import java.io.Serializable;

public class OpenResourceParamVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String userId;   //用户ID
	
	
	
	private String serviceId;  //服务ID
	

	
	private int masterNum;      //主库数
	
	private boolean isMysqlProxy	;	//是否读写分离，即是否要从库
	
	public boolean isMysqlProxy() {
		return isMysqlProxy;
	}

	public void setMysqlProxy(boolean isMysqlProxy) {
		this.isMysqlProxy = isMysqlProxy;
	}

	public boolean isAutoSwitch() {
		return isAutoSwitch;
	}

	public void setAutoSwitch(boolean isAutoSwitch) {
		this.isAutoSwitch = isAutoSwitch;
	}

	private boolean isAutoSwitch;	//主从是否自动切换
	
	private String url;
    
    public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	private boolean isNeedDistributeTrans;  //是否需要分布式事务服务

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}


	public int getMasterNum() {
		return masterNum;
	}

	public void setMasterNum(int masterNum) {
		this.masterNum = masterNum;
	}



	public boolean isNeedDistributeTrans() {
		return isNeedDistributeTrans;
	}

	public void setNeedDistributeTrans(boolean isNeedDistributeTrans) {
		this.isNeedDistributeTrans = isNeedDistributeTrans;
	}
    
}
