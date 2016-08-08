package com.ai.paas.ipaas.ses.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.agent.util.AidUtil;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserWebMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesWebPoolMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserWeb;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserWebCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesWebPool;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesWebPoolCriteria;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.ISesUserWeb;

@Service
@Transactional(rollbackFor = Exception.class)
public class SesUserWebImpl implements ISesUserWeb {

	@Override
	public SesWebPool getAvlWeb(String userId, String serviceId) {
		SesWebPoolMapper sesWebPoolMapper = ServiceUtil
				.getMapper(SesWebPoolMapper.class);
		SesWebPoolCriteria criteria = new SesWebPoolCriteria();
		criteria.createCriteria()
				.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
				.andTenantIdEqualTo(AidUtil.getAid());
		List<SesWebPool> list = sesWebPoolMapper.selectByExample(criteria);
		if (null == list || list.size() == 0)
			return null;
		else if (list.size() == 1)
			// 就一个
			return list.get(0);
		else {
			// 策略是找个使用最少的
			SesUserWebMapper sesUserWebMapper = ServiceUtil
					.getMapper(SesUserWebMapper.class);
			SesUserWebCriteria webCriteria = new SesUserWebCriteria();
			List<String> values = new ArrayList<>();
			for (SesWebPool sesWebPool : list) {
				values.add("" + sesWebPool.getId());
			}
			webCriteria.createCriteria()
					.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
					.andWebIdIn(values);
			List<SesUserWeb> userWebs = sesUserWebMapper
					.selectByExample(webCriteria);
			if (null == userWebs || userWebs.size() == 0)
				return list.get(0);
			else {
				// 自己计数
				Map<String, Integer> count = new HashMap<>();
				for (SesUserWeb userWeb : userWebs) {
					String webId = userWeb.getWebId().trim();
					if (null != count.get(webId)) {
						int old = count.get(webId);
						old++;
						count.put(webId, old);
					} else {
						count.put(webId, 1);
					}
				}
				// 找到最少的
				int min = Integer.MAX_VALUE;
				String key = null;
				for (Map.Entry<String, Integer> entry : count.entrySet()) {
					if (entry.getValue() < min) {
						min = entry.getValue();
						key = entry.getKey();
					}
				}
				if (key == null) {
					return list.get(0);
				} else {
					boolean found = false;
					SesWebPool webPool = null;
					for (SesWebPool sesWebPool : list) {
						if (key.equals(sesWebPool.getId())) {
							found = true;
							webPool = sesWebPool;
							break;
						}

					}
					if (!found)
						return list.get(0);
					else
						return webPool;
				}
			}
		}
	}

	@Override
	public void saveUserWeb(SesUserWeb userWeb) {
		SesUserWebMapper sesUserWebMapper = ServiceUtil
				.getMapper(SesUserWebMapper.class);
		sesUserWebMapper.insert(userWeb);
	}

	@Override
	public SesUserWeb getUserWeb(String userId, String serviceId) {
		SesUserWebMapper sesUserWebMapper = ServiceUtil
				.getMapper(SesUserWebMapper.class);
		SesUserWebCriteria webCriteria = new SesUserWebCriteria();
		webCriteria.createCriteria()
				.andStatusEqualTo(SesConstants.VALIDATE_STATUS)
				.andUserIdEqualTo(userId).andServiceIdEqualTo(serviceId);
		List<SesUserWeb> userWebs = sesUserWebMapper
				.selectByExample(webCriteria);
		if (null == userWebs || userWebs.size() == 0)
			return null;
		else
			return userWebs.get(0);
	}

}
