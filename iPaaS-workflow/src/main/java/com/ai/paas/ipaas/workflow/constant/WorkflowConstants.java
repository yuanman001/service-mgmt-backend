package com.ai.paas.ipaas.workflow.constant;

public class WorkflowConstants {
	
	public static  final String  dirSplit = "/";
 	
	public static class ConfPath{
		public final static String WF_CONF_PATH = "workflow";
		
	}
	
	public static class ConfAppAddr{
		public final static String WF_CONF_INTERFACE_ADDR = "http://10.1.228.198:14831/workflow-rest";
		public final static String WF_CONF_INTERFACE_ADDR_DESC = "接口服务地址";
		public final static String WF_CONF_MODELING_ADDR = "http://10.1.228.198:14831/activiti-webapp/";
		public final static String WF_CONF_MODELING_ADDR_DESC = "流程建模地址（用户名/密码：kermit/kermit）";
		
	}
}
