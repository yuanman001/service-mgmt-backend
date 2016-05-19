package com.ai.paas.ipaas.mcs.service.impl;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Repository;

import com.ai.paas.agent.client.AgentClient;
import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.mcs.dao.interfaces.McsUserCacheInstanceMapper;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstance;
import com.ai.paas.ipaas.mcs.dao.mapper.bo.McsUserCacheInstanceCriteria;
import com.ai.paas.ipaas.mcs.service.constant.McsConstants;

@Repository
public class McsSvHepler {
	
	public List<McsUserCacheInstance> getMcsUserCacheInstances(
			String serviceId, String userId) {
		McsUserCacheInstanceMapper im = ServiceUtil.getMapper(McsUserCacheInstanceMapper.class);
		McsUserCacheInstanceCriteria imc = new McsUserCacheInstanceCriteria();
		imc.createCriteria().andStatusEqualTo(McsConstants.VALIDATE_STATUS)
		.andUserIdEqualTo(userId).andSerialNumberEqualTo(serviceId);
		return im.selectByExample(imc);
	}
	
	public String getRandomKey() {
		Random rand = new Random();
		int i = rand.nextInt(900000) + 100000;
		return i+"";
	}
	
	/**
	 * upload file to remote server
	 * @param ac
	 * @param fileName
	 * @param content
	 * @throws Exception
	 */
	public void uploadFile(AgentClient ac, String fileName, String content) throws Exception {
		ac.saveFile(fileName, content);
	}
	
	/**
	 * excute command
	 * @param ac
	 * @param command
	 * @throws Exception
	 */
	public void excuteCommand(AgentClient ac, String command) throws Exception {
		ac.executeInstruction(command);
	}

}
