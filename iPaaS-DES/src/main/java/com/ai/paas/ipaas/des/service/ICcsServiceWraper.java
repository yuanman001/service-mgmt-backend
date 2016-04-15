package com.ai.paas.ipaas.des.service;

import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.PaasException;

/**
 * @author bixy
 * 
 */
public interface ICcsServiceWraper {

	public Map<String, String> getTablePaterns(String userId, String serviceId, List<String> tables) throws PaasException;

	public String[] getUnbindTables(String userId, String serviceId, String[] tables) throws PaasException;
}
