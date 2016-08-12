package com.ai.paas.ipaas.rds.dao.wo;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;

public class InstanceBase {
	public RdsIncBase rdsIncBase;

	public InstanceBase(RdsIncBase instanceInfo) {
		this.rdsIncBase = instanceInfo;
	}

	public RdsIncBase getRdsIncBase() {
		return rdsIncBase;
	}

	public void setRdsIncBase(RdsIncBase rdsIncBase) {
		this.rdsIncBase = rdsIncBase;
	}
	
}
