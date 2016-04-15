package com.ai.paas.ipaas.rcs.service.impl;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.rcs.dao.interfaces.RcsSpoutInfoMapper;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfoCriteria;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskSpoutSv;

//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.ai.paas.ipaas.rcs.dto.IpaasStormTaskSpout;
//import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskSpoutSv;
// 
//import com.ai.paas.ipaas.rcs.util.BaseSvImpl;
//
///**
// * 
// * @author weichuang
// *
// */
@Service
@Transactional
// public class IpaasStormTaskSpoutSvImpl extends
// BaseSvImpl<IpaasStormTaskSpout, Long> implements IIpaasStormTaskSpoutSv
public class IpaasStormTaskSpoutSvImpl implements IIpaasStormTaskSpoutSv {
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public List<RcsSpoutInfo> searchList(long taskID) {
		RcsSpoutInfoCriteria rcsSpoutInfoCriteria = new RcsSpoutInfoCriteria();
		RcsSpoutInfoCriteria.Criteria criteria = rcsSpoutInfoCriteria
				.createCriteria();
		RcsSpoutInfoMapper rcsSpoutInfoMapper = template
				.getMapper(RcsSpoutInfoMapper.class);
		List<RcsSpoutInfo> listSpout = rcsSpoutInfoMapper
				.selectByExample(rcsSpoutInfoCriteria);
		return listSpout;
	}

}
// {
// @SuppressWarnings("unused")
// private IpaasStormTaskSpout ipaasStormTaskSpoutDao;
//
// @Autowired
// public void setIpaasStormTaskSpoutDao(IpaasStormTaskSpout
// ipaasStormTaskSpoutDao){
// this.ipaasStormTaskSpoutDao = ipaasStormTaskSpoutDao;
// // super.setBaseDao(ipaasStormTaskSpoutDao);
// }
