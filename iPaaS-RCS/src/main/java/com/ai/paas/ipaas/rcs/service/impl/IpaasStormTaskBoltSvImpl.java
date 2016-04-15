package com.ai.paas.ipaas.rcs.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.rcs.dao.interfaces.RcsBoltInfoMapper;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfoCriteria;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfoCriteria;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskBoltSv;

//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.paas.ipaas.rcs.dto.IpaasStormTaskBolt;
//import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskBoltSv;
//import com.ai.paas.ipaas.rcs.dao.interfaces.IpaasStormTaskBoltMapper;
//import com.ai.paas.ipaas.rcs.util.BaseSvImpl;
//
///**
// * 
// * @author weichuang
// *
// */
@Service
@Transactional
// public class IpaasStormTaskBoltSvImpl extends BaseSvImpl<IpaasStormTaskBolt,
// Long> implements IIpaasStormTaskBoltSv {
public class IpaasStormTaskBoltSvImpl implements IIpaasStormTaskBoltSv {
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public List<RcsBoltInfo> searchList(long taskID) {
		RcsBoltInfoCriteria rcsboltInfoCriteria = new RcsBoltInfoCriteria();
		RcsBoltInfoCriteria.Criteria criteria = rcsboltInfoCriteria
				.createCriteria();
		RcsBoltInfoMapper rcsBoltInfoMapper = template
				.getMapper(RcsBoltInfoMapper.class);

		List<RcsBoltInfo> listBolt = rcsBoltInfoMapper
				.selectByExample(rcsboltInfoCriteria);
		return listBolt;
	}
	// @SuppressWarnings("unused")
	// private IpaasStormTaskBoltMapper ipaasStormTaskBoltDao;
	//
	// @Autowired
	// public void setIpaasStormTaskBoltDao(IpaasStormTaskBoltMapper
	// ipaasStormTaskBoltDao){
	// this.ipaasStormTaskBoltDao = ipaasStormTaskBoltDao;
	// // super.setBaseDao(ipaasStormTaskBoltDao);
	// }
}
