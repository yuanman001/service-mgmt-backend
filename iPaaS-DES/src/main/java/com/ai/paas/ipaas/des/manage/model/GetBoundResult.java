package com.ai.paas.ipaas.des.manage.model;

import java.util.List;

import com.ai.paas.ipaas.des.dao.mapper.bo.DesServiceBind;

public class GetBoundResult extends GeneralResult {

	private List<DesServiceBindResult> bindServices;

	public List<DesServiceBindResult> getBindServices() {
		return bindServices;
	}

	public void setBindServices(List<DesServiceBindResult> bindServices) {
		this.bindServices = bindServices;
	}

	public static class DesServiceBindResult extends DesServiceBind {
		private String[] boundTables;

		public String[] getBoundTables() {
			return boundTables;
		}

		public void setBoundTables(String[] boundTables) {
			this.boundTables = boundTables;
		}
	}

	public static class DesServiceBindTableResult extends GeneralResult {
		private String[] boundTables;
		private String[] unboundTables;

		public String[] getBoundTables() {
			return boundTables;
		}

		public void setBoundTables(String[] boundTables) {
			this.boundTables = boundTables;
		}

		public String[] getUnboundTables() {
			return unboundTables;
		}

		public void setUnboundTables(String[] unboundTables) {
			this.unboundTables = unboundTables;
		}
	}
}
