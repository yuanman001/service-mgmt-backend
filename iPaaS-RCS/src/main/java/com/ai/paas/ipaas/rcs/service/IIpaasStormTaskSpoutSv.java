package com.ai.paas.ipaas.rcs.service;

import java.util.List;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
/**
 * 
 * @author weichuang
 *
 */
public interface IIpaasStormTaskSpoutSv{
	List<RcsSpoutInfo>  searchList(long taskID);
}
