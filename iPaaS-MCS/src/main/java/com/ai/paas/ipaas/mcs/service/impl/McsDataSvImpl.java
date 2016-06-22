package com.ai.paas.ipaas.mcs.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.exceptions.JedisMovedDataException;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstance;
import com.ai.paas.ipaas.mcs.service.constant.McsConstants;
import com.ai.paas.ipaas.mcs.service.interfaces.IMcsDataSv;
import com.ai.paas.ipaas.mcs.service.util.McsParamUtil;
import com.ai.paas.ipaas.util.Assert;
import com.google.gson.Gson;

@Service
@Transactional(rollbackFor = Exception.class)
public class McsDataSvImpl implements IMcsDataSv {
	private static transient final Logger log = LoggerFactory.getLogger(McsDataSvImpl.class);
	
	@Autowired
	private McsSvHepler mcsSvHepler;

	@Override
	public String get(String param) throws PaasException {
		//获取服务号配置参数
		Map<String,String> map = McsParamUtil.getParamMap(param);
		final String serviceId=map.get(McsConstants.SERVICE_ID);
		final String userId=map.get(McsConstants.USER_ID);
		final String key=map.get(McsConstants.DDL_KEY);
		final String field = map.get(McsConstants.DDL_FIELD);
		final String selType = map.get(McsConstants.DDL_SEL_TYPE);
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(key, "key为空");
		
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId,userId);
		if(cis==null||cis.isEmpty()) {
			throw new PaasException("该服务未开通过，无法对数据操作！");
		}
		
		String res = "";
		Gson gson = new Gson();
		if(cis.size()==1){
			//单例
			Jedis jedis = initJedis(cis.get(0));
			jedis.auth(cis.get(0).getPwd());
			switch(selType){
				case "String" : res = jedis.get(key);break;
				case "Hash" : res = jedis.hget(key, field);break;
				case "List" : List<String> list = jedis.lrange(key, 0, -1);
								res = gson.toJson(list);break;
				case "Set" : Set<String> set = jedis.smembers(key);
								res = gson.toJson(set);break;
				case "SortedSet" : Set<String> sortedSet = jedis.zrange(key, 0, -1);
									res = gson.toJson(sortedSet);break;
			}
		} else {
			JedisCluster jedis = initJedisCluster(cis);
			switch(selType){
				case "String" : res = jedis.get(key);break;
				case "Hash" : res = jedis.hget(key, field);break;
				case "List" : List<String> list = jedis.lrange(key, 0, -1);
								res = gson.toJson(list);break;
				case "Set" : Set<String> set = jedis.smembers(key);
								res = gson.toJson(set);break;
				case "SortedSet" : Set<String> sortedSet = jedis.zrange(key, 0, -1);
								res = gson.toJson(sortedSet);break;
			}
		}
		return res;
	}

	private JedisCluster initJedisCluster(List<McsUserCacheInstance> cis) {
		Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
		for(int i=0;i<cis.size();i++){
			McsUserCacheInstance ci = cis.get(i);
			jedisClusterNodes.add(new HostAndPort(ci.getCacheHost(), ci.getCachePort()));
		}
		return new JedisCluster(jedisClusterNodes);
	}

	private Jedis initJedis(McsUserCacheInstance mcsUserCacheInstance) {
		return new Jedis(mcsUserCacheInstance.getCacheHost(),mcsUserCacheInstance.getCachePort());
	}

	@Override
	public String del(String param) throws PaasException {
		//获取服务号配置参数
		Map<String,String> map = McsParamUtil.getParamMap(param);
		final String serviceId=map.get(McsConstants.SERVICE_ID);
		final String userId=map.get(McsConstants.USER_ID);
		final String key=map.get(McsConstants.DDL_KEY);
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(key, "key为空");
		
		long res = 0l;
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId,userId);
		if(cis==null||cis.isEmpty())
			throw new PaasException("该服务未开通过，无法对数据操作！");
		if(cis.size()==1){
			//单例
			Jedis jedis = initJedis(cis.get(0));
			jedis.auth(cis.get(0).getPwd());
			res = jedis.del(key.split(","));
		}else{
			//集群
			JedisCluster jedis = initJedisCluster(cis);
			res = jedis.del(key.split(","));
		}
		return res+"";
	}

	@Override
	public String flushDb(String param) throws PaasException {
		//获取服务号配置参数
		Map<String,String> map = McsParamUtil.getParamMap(param);
		final String serviceId=map.get(McsConstants.SERVICE_ID);
		final String userId=map.get(McsConstants.USER_ID);
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		
		String res = "";
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId,userId);
		if(cis==null||cis.isEmpty())
			throw new PaasException("该服务未开通过，无法对数据操作！");
		if(cis.size()==1){
			//单例
			Jedis jedis = initJedis(cis.get(0));
			jedis.auth(cis.get(0).getPwd());
			res = jedis.flushDB();
		}else{
			//集群
			for(int i=0;i<cis.size();i++){
				try {
					McsUserCacheInstance ci = cis.get(i);
					Jedis jedis = initJedis(ci);
					res = jedis.flushDB();
				} catch (JedisMovedDataException e) {
					log.error("可以忽略该异常",e);
				}
			}
		}
		return res;
	}

	@Override
	public String set(String param) throws PaasException {
		Map<String,String> map = McsParamUtil.getParamMap(param);
		final String serviceId=map.get(McsConstants.SERVICE_ID);
		final String userId=map.get(McsConstants.USER_ID);
		final String key=map.get(McsConstants.DDL_KEY);
		final String value=map.get(McsConstants.DDL_VALUE);
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		Assert.notNull(key, "key为空");
		Assert.notNull(value, "value为空");
		
		String res = "";
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId,userId);
		if(cis==null||cis.isEmpty())
			throw new PaasException("该服务未开通过，无法对数据操作！");
		if(cis.size()==1){
			//单例
			Jedis jedis = initJedis(cis.get(0));
			jedis.auth(cis.get(0).getPwd());
			res = jedis.set(key,value);
		}else{
			//集群
			JedisCluster jedis = initJedisCluster(cis);
			res = jedis.set(key,value);
		}
		return res;
	}

	@Override
	public String info(String param) throws PaasException {
		Map<String,String> map = McsParamUtil.getParamMap(param);
		final String serviceId=map.get(McsConstants.SERVICE_ID);
		final String userId=map.get(McsConstants.USER_ID);
		Assert.notNull(serviceId, "serviceId为空");
		Assert.notNull(userId, "userId为空");
		
		String res = "";
		List<McsUserCacheInstance> cis = mcsSvHepler.getMcsUserCacheInstances(serviceId,userId);
		if(cis==null||cis.isEmpty())
			throw new PaasException("该服务未开通过，无法对数据操作！");
		if(cis.size()==1){
			//单例
			Jedis jedis = initJedis(cis.get(0));
			jedis.auth(cis.get(0).getPwd());
			res = jedis.info();
		}else{
			//集群
			for(int i=0;i<cis.size();i++){
				try {
					McsUserCacheInstance ci = cis.get(i);
					Jedis jedis = initJedis(ci);
					res = jedis.info();
				} catch (JedisMovedDataException e) {
					log.error("可以忽略该异常",e);
				}
			}			
		}
		return res;
	}

}
