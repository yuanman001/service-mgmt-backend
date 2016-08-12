package com.ai.paas.ipaas.rds.service.transfer.vo;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;

public class InstanceBaseSimple {
	private String id;
    private String userId;
    private String serviceId;
    private String incName;
    private String incType;
    private String incIp;
    private String incPort;
    private String rootName;
    private String rootPassword;
    
    public InstanceBaseSimple(RdsIncBase incBase) {
    	this.id = incBase.getId() + "";
    	this.userId = incBase.getUserId();
    	this.serviceId = incBase.getServiceId();
    	this.incName = incBase.getIncName();
    	this.incType = incBase.getIncType() + "";
    	this.incIp = incBase.getIncIp();
    	this.incPort = incBase.getIncPort() + "";
    	this.rootName = incBase.getRootName();
    	this.rootPassword = incBase.getRootPassword();
	}

	public InstanceBaseSimple() {
		super();
	}

	public void setByIncBase(RdsIncBase incBase){
    	this.id = incBase.getId() + "";
    	this.userId = incBase.getUserId();
    	this.serviceId = incBase.getServiceId();
    	this.incName = incBase.getIncName();
    	this.incType = incBase.getIncType() + "";
    	this.incIp = incBase.getIncIp();
    	this.incPort = incBase.getIncPort() + "";
    	this.rootName = incBase.getRootName();
    	this.rootPassword = incBase.getRootPassword();
    }
}
