package com.ai.paas.ipaas.des.manage.model;

import java.util.List;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesUserService;

public class GetAllServicesResult extends GeneralResult {

	private List<DesUserService> services;

	public List<DesUserService> getServices() {
		return services;
	}

	public void setServices(List<DesUserService> services) {
		this.services = services;
	}

}
