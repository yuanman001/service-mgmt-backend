package com.ai.paas.ipaas.ses.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants.PathType;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserMappingMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMapping;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserMappingCriteria;
import com.ai.paas.ipaas.ses.service.constant.SesConstants;
import com.ai.paas.ipaas.ses.service.interfaces.IIndexMapping;
import com.google.gson.JsonObject;

@Service
@Transactional(rollbackFor = Exception.class)
public class IndexMappingImpl implements IIndexMapping {
	private static final transient Logger LOGGER = LoggerFactory
			.getLogger(IndexMappingImpl.class);

	@Autowired
	private ICCSComponentManageSv iCCSComponentManageSv;

	@Override
	public SesUserMapping loadMapping(String userId, String serviceId)
			throws PaasException {
		List<SesUserMapping> resultList;
		try {
			SesUserMappingMapper mappingMapper = ServiceUtil
					.getMapper(SesUserMappingMapper.class);
			SesUserMappingCriteria example = new SesUserMappingCriteria();
			example.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(serviceId);
			resultList = mappingMapper.selectByExample(example);
			if (resultList != null && resultList.size() == 1) {
				return resultList.get(0);
			} else {
				throw new PaasException(SesConstants.ExecResult.FAIL,
						SesConstants.EXPECT_ONE_RECORD_FAIL);
			}
		} catch (Exception e) {
			LOGGER.error(SesConstants.ExecResult.FAIL, e);
			throw new PaasException(SesConstants.ExecResult.FAIL, e);

		}
	}

	@Override
	public void editMapping(SesUserMapping mapping) throws PaasException {
		try {
			SesUserMappingMapper mappingMapper = ServiceUtil
					.getMapper(SesUserMappingMapper.class);
			mappingMapper.updateByPrimaryKeySelective(mapping);
		} catch (Exception e) {
			throw new PaasException(SesConstants.ExecResult.FAIL, e);
		}
	}

	@Override
	public void insertMapping(SesUserMapping mapping) throws PaasException {
		try {
			SesUserMappingMapper mapper = ServiceUtil
					.getMapper(SesUserMappingMapper.class);
			SesUserMappingCriteria sesCriteria = new SesUserMappingCriteria();
			sesCriteria.createCriteria().andUserIdEqualTo(mapping.getUserId())
					.andServiceIdEqualTo(mapping.getServiceId());
			mapper.deleteByExample(sesCriteria);
			mapper.insert(mapping);
			// 这里还得加一个东西，向ZK加入模型主键
			addMappPK2Zk(mapping.getUserId(), mapping.getServiceId(),
					mapping.getPk());
		} catch (Exception e) {
			LOGGER.error(SesConstants.ExecResult.FAIL, e);
			throw new PaasException(SesConstants.ExecResult.FAIL, e);
		}

	}

	private void addMappPK2Zk(String userId, String serviceId, String pk)
			throws PaasException {
		// 在zk中记录申请信息
		CCSComponentOperationParam op = new CCSComponentOperationParam();
		op.setUserId(userId);
		op.setPath(SesConstants.SES_ZK_PATH + serviceId + "/indexPK");
		op.setPathType(PathType.READONLY);

		JsonObject dataJson = new JsonObject();

		dataJson.addProperty("indexPK", pk);
		iCCSComponentManageSv.add(op, dataJson.toString());
		LOGGER.info("write to zk su!");
	}

}
