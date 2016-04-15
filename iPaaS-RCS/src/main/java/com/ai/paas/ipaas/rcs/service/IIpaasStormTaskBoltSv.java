package com.ai.paas.ipaas.rcs.service;

import java.util.List;

import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
/**
 * 
 * @author weichuang
 *
 */
public interface IIpaasStormTaskBoltSv{
	List<RcsBoltInfo>  searchList(long taskID);
}
