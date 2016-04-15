package com.ai.paas.ipaas.mcs.service.interfaces;

import com.ai.paas.ipaas.PaasException;


public interface IMcsSv {
	//开通服务
	String openMcs(String param) throws PaasException;
	//修改服务
	String modifyMcs(String param) throws PaasException;
	
	//停止服务
	String stopMcs(String param) throws PaasException;
	//启动服务
	String startMcs(String param) throws PaasException;
	//重启服务
	String restartMcs(String param) throws PaasException;
	//注销服务
	String cancelMcs(String param) throws PaasException;
}
