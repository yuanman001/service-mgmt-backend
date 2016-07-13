package com.ai.paas.ipaas.idps.service.interfaces;


public interface IIdpsSv {
	//开通服务
	String open(String param,String isUpgrade) throws Exception;
	//修改服务
	String modify(String param) throws Exception;
	
	//停止服务
	String stop(String param) throws Exception;
	//启动服务
	String start(String param) throws Exception;
	//重启服务
	String restart(String param) throws Exception;
	//注销服务
	String cancel(String param) throws Exception;
	//删除服务
	String clean(String param,String destroy) throws Exception;
}
