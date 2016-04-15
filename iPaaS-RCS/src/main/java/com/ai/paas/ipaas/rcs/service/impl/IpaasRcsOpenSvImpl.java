package com.ai.paas.ipaas.rcs.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.rcs.dao.interfaces.RcsClusterInfoMapper;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsClusterInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsClusterInfoCriteria;
import com.ai.paas.ipaas.rcs.service.IIpaasRcsOpenSv;
import com.ai.paas.ipaas.rcs.service.constant.RcsConstants;
import com.ai.paas.ipaas.rcs.util.RcsParamUtil;
import com.ai.paas.ipaas.rcs.vo.PageSelectValue;
import com.ai.paas.ipaas.rcs.vo.StormClusterInfoVo;

@Service
@Transactional
public class IpaasRcsOpenSvImpl implements IIpaasRcsOpenSv {
	
	@Autowired
	private SqlSessionTemplate template;

	@Override
	public String openRcs(String param) throws PaasException {
		// TODO Auto-generated method stub
		Map<String, String> map = RcsParamUtil.getParamMap(param);
		final String userId = map.get(RcsConstants.USER_ID);
		final String clusterType = map.get(RcsConstants.CLUSTER_TYPE);
		final String serviceId  = map.get(RcsConstants.SERVICE_ID);
		
		RcsClusterInfoMapper rcsClusterInfoMapper = ServiceUtil.getMapper(RcsClusterInfoMapper.class);
        //查找出满足这个clusterType并且状态为未使用的所有的集群类型存在一个list里，
        RcsClusterInfoCriteria where=new RcsClusterInfoCriteria();
        where.createCriteria().andClusterTypeEqualTo(Integer.parseInt(clusterType)).andOrderStatusEqualTo(0);
       List<RcsClusterInfo> rcsClusterInfos=new ArrayList<RcsClusterInfo>();
       rcsClusterInfos=rcsClusterInfoMapper.selectByExample(where);
       
		//判断如果list为空提示此种类型已经没有。
		if(rcsClusterInfos.size()<=0){
			return RcsConstants.FAIL_FLAG;
		}
		//如果大于0则把list的第一个的userId更新为传过来的userId然后将状态改为已分配
		else{
			RcsClusterInfo rcsClusterInfo=new RcsClusterInfo();
			rcsClusterInfo=rcsClusterInfos.get(0);
			Float id=rcsClusterInfo.getId();
			int clusterType1=rcsClusterInfo.getClusterType();
			int serverCount=rcsClusterInfo.getServerCount();
			
			RcsClusterInfo rcsClusterInfo1=new RcsClusterInfo();
			rcsClusterInfo1.setOrderUser(userId); 
			rcsClusterInfo1.setOrderStatus(1);
			rcsClusterInfo1.setClusterType(clusterType1);
			rcsClusterInfo1.setServerCount(serverCount);
			rcsClusterInfo1.setServiceId(serviceId);
			
			RcsClusterInfoCriteria where1=new RcsClusterInfoCriteria();
			where1.createCriteria().andIdEqualTo(id);
			
			rcsClusterInfoMapper.updateByExampleSelective(rcsClusterInfo1, where1);
			return RcsConstants.SUCCESS_FLAG;
		}
	}
	
	@Override
	public PageSelectValue<StormClusterInfoVo> searchClusterInfo(String userId) {
		RcsClusterInfoMapper rcsClusterInfoMapper = ServiceUtil.getMapper(RcsClusterInfoMapper.class);
        RcsClusterInfoCriteria where = new RcsClusterInfoCriteria();
        where.createCriteria().andOrderUserEqualTo(userId);
        
        List<StormClusterInfoVo> clusterVos = new ArrayList<StormClusterInfoVo>();
        List<RcsClusterInfo> searchResult = rcsClusterInfoMapper.selectByExample(where);
        if (searchResult != null && searchResult.size() > 0) {
			for (RcsClusterInfo bean: searchResult) {
				StormClusterInfoVo vo = new StormClusterInfoVo();
				vo.setClusterId(bean.getClusterId());
				vo.setClusterType(bean.getClusterType());
				vo.setComments(bean.getComments());
				vo.setOrderStatus(bean.getOrderStatus());
				vo.setOrderUser(bean.getOrderUser());
				clusterVos.add(vo);
			}
		}
        
        PageSelectValue<StormClusterInfoVo> selectValues = new PageSelectValue<StormClusterInfoVo>();
        selectValues.setResultList(clusterVos);
        
        return selectValues;
	}
	
}
