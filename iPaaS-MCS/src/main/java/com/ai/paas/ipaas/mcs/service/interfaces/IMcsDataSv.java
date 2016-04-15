package com.ai.paas.ipaas.mcs.service.interfaces;

import com.ai.paas.ipaas.PaasException;

/**
 * 缓存的数据  服务
 *
 */
public interface IMcsDataSv {
	//查询，根据key查询
	String get(String param) throws PaasException;
	//删除，根据key
	String del(String param) throws PaasException;
	//删除所有key
	String flushDb(String param) throws PaasException;
	
	//保存数据
	String set(String param) throws PaasException;
	//获得缓存使用状态
	String info(String param) throws PaasException;
}
