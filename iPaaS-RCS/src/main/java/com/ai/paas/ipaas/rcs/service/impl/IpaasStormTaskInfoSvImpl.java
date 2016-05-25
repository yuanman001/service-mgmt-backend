package com.ai.paas.ipaas.rcs.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl2.UnifiedJEXL.Exception;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.rcs.dao.interfaces.RcsBoltInfoMapper;
import com.ai.paas.ipaas.rcs.dao.interfaces.RcsSpoutInfoMapper;
import com.ai.paas.ipaas.rcs.dao.interfaces.RcsTaskInfoMapper;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfoCriteria;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskBoltSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskInfoSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskSpoutSv;
import com.ai.paas.ipaas.rcs.service.constant.RcsConstants;
import com.ai.paas.ipaas.rcs.util.PageUtils;
import com.ai.paas.ipaas.rcs.vo.PageEntity;
import com.ai.paas.ipaas.rcs.vo.PageResult;
import com.ai.paas.ipaas.rcs.vo.StormTaskInfoVo;
import com.ai.paas.ipaas.seq.service.ISequenceSv;

@Service
@Transactional
public class IpaasStormTaskInfoSvImpl implements IIpaasStormTaskInfoSv {

	// @Override
	// public List<IpaasStormTaskInfo> searchAll() throws PaasException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public IpaasStormTaskInfo searchOneById(Long pk) throws PaasException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public PagingResult<IpaasStormTaskInfo> searchPage(PageEntity pageEntity)
	// throws PaasException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public List<IpaasStormTaskInfo> searchList(Map<String, Object> params)
	// throws PaasException {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public void add(IpaasStormTaskInfo t) throws PaasException {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void del(Long id) throws PaasException {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void update(IpaasStormTaskInfo t) throws PaasException {
	// // TODO Auto-generated method stub
	//
	// }
	@Autowired
	ISequenceSv iSequenceManageSv;
	
	@Autowired
	private SqlSessionTemplate template;

	
	@Autowired
	private IIpaasStormTaskSpoutSv iIpaasStormTaskSpoutSv;

	@Autowired
	private IIpaasStormTaskBoltSv iIpaasStormTaskBoltSv;

	 /**
	 * 注册计算任务
	 */
	
	@Override
	public String registerTask(RcsTaskInfo rcsTaskInfo,
			List<RcsSpoutInfo> rcsSpoutInfos,
			List<RcsBoltInfo> rcsBoltInfos) throws PaasException {
		//判断计算任务名是否存在
		RcsTaskInfoCriteria rcsTaskInfoCriteria = new RcsTaskInfoCriteria();
		RcsTaskInfoCriteria.Criteria criteria = rcsTaskInfoCriteria
				.createCriteria();
		criteria.andNameEqualTo(rcsTaskInfo.getName());	
		
		RcsTaskInfoMapper rcsTaskInfoMapper = ServiceUtil.getMapper(RcsTaskInfoMapper.class);
		RcsSpoutInfoMapper rcsSpoutInfoMapper = ServiceUtil.getMapper(RcsSpoutInfoMapper.class);
		RcsBoltInfoMapper rcsBoltInfoMapper = ServiceUtil.getMapper(RcsBoltInfoMapper.class);
		//若计算任务存在，则返回
		List<RcsTaskInfo> rcsTaskInfos = rcsTaskInfoMapper
				.selectByExample(rcsTaskInfoCriteria);
		if(rcsTaskInfos.size()>0){
			return RcsConstants.FAIL_FLAG;
		}
		//序列生成主键
		long nextval = iSequenceManageSv.nextVal("seq_rcs_task_info");
		rcsTaskInfo.setId(nextval);
		rcsTaskInfoMapper.insert(rcsTaskInfo);
		for(RcsSpoutInfo rcsSpoutInfo : rcsSpoutInfos){
			rcsSpoutInfo.setTaskId(nextval);
			rcsSpoutInfoMapper.insert(rcsSpoutInfo);
		}
		
		for(RcsBoltInfo rcsBoltInfo : rcsBoltInfos){
			rcsBoltInfo.setTaskId(nextval);
			rcsBoltInfoMapper.insert(rcsBoltInfo);
		}
		return RcsConstants.SUCCESS_FLAG;
	}

	@Override
	public void editTask(RcsTaskInfo rcsTaskInfo,
			List<RcsSpoutInfo> rcsSpoutInfos,
			List<RcsBoltInfo> rcsBoltInfos) throws PaasException {
		// TODO Auto-generated method stub

	}

	public RcsTaskInfo searchOneById(String id){
		RcsTaskInfoCriteria rcsTaskInfoCriteria = new RcsTaskInfoCriteria();
		RcsTaskInfoCriteria.Criteria criteria = rcsTaskInfoCriteria
				.createCriteria();
		RcsTaskInfoMapper rcsTaskInfoMapper = template
				.getMapper(RcsTaskInfoMapper.class);
		RcsTaskInfo rcsTaskInfo = rcsTaskInfoMapper.selectByPrimaryKey(Long.parseLong(id));
		return rcsTaskInfo;
	}
	
	@Override
	public PageResult<StormTaskInfoVo> searchPage(PageEntity pageEntity) {
		// PageEntity pageEntity=request.getPageEntity();
		RcsTaskInfoCriteria rcsTaskInfoCriteria = new RcsTaskInfoCriteria();
		RcsTaskInfoCriteria.Criteria criteria = rcsTaskInfoCriteria
				.createCriteria();
		String name=(String) pageEntity.getName();
		String userID=(String)pageEntity.getUserID();//20150611
		criteria.andNameLike("%"+name+"%");// pageEntity
		criteria.andRegisterUserIdEqualTo(userID);
		RcsTaskInfoMapper rcsTaskInfoMapper = template
				.getMapper(RcsTaskInfoMapper.class);
		int totalCount = rcsTaskInfoMapper.countByExample(rcsTaskInfoCriteria);
		//当前页面
		rcsTaskInfoCriteria.setLimitStart(pageEntity.getLimitStart());
		rcsTaskInfoCriteria.setLimitEnd(pageEntity.getLimitEnd());
		List<RcsTaskInfo> rcsTaskInfos = rcsTaskInfoMapper
				.selectByExample(rcsTaskInfoCriteria);
		List<StormTaskInfoVo> rcsTaskInfoVos = new ArrayList<StormTaskInfoVo>();
		if (rcsTaskInfos != null && rcsTaskInfos.size() > 0) {
			for (int i = 0; i < rcsTaskInfos.size(); i++) {
				StormTaskInfoVo stormTaskInfoVo = new StormTaskInfoVo();
				BeanUtils.copyProperties(rcsTaskInfos.get(i), stormTaskInfoVo);
				rcsTaskInfoVos.add(stormTaskInfoVo);
			}
		}
		PageResult<StormTaskInfoVo> pageResult = new PageResult<StormTaskInfoVo>();
		pageResult.setResultList(rcsTaskInfoVos);
		pageResult.setTotalPages(PageUtils.getTotalPages(totalCount,
				pageEntity.getPageSize()));
		pageResult.setTotalCount(totalCount);
		pageResult.setCurrentPage(pageEntity.getCurrentPage());
		return pageResult;
	}
	
	@Override
	public int update(RcsTaskInfo rcsTaskInfo){
		int count=0;
		RcsTaskInfoCriteria rcsTaskInfoCriteria = new RcsTaskInfoCriteria();
		RcsTaskInfoCriteria.Criteria criteria = rcsTaskInfoCriteria
				.createCriteria();
		RcsTaskInfoMapper rcsTaskInfoMapper = template
				.getMapper(RcsTaskInfoMapper.class);
		try{
			count = rcsTaskInfoMapper.updateByPrimaryKey(rcsTaskInfo);
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	// @SuppressWarnings("unused")
	// private IpaasStormTaskInfo ipaasStormTaskInfoDao;
	//
	// @Autowired
	// public void setIpaasStormTaskInfoDao(IpaasStormTaskInfo
	// ipaasStormTaskInfoDao){
	// this.ipaasStormTaskInfoDao = ipaasStormTaskInfoDao;
	// // super.setBaseDao(ipaasStormTaskInfoDao);
	// }
	//
//	 @Autowired
//	 private IIpaasStormTaskSpoutSv iIpaasStormTaskSpoutSv;
//	
//	 @Autowired
//	 private IIpaasStormTaskBoltSv iIpaasStormTaskBoltSv;
//	
//	 /**
//	 * 注册计算任务
//	 */
//	 public void registerTask(IpaasStormTaskInfo ipaasStormTaskInfo,
//	 List<IpaasStormTaskSpout> ipaasStormTaskSpouts, List<IpaasStormTaskBolt>
//	 ipaasStormTaskBolts)
//	 throws PaasException{
//	 //
//	 add(ipaasStormTaskInfo);
//	 //
//	 for (IpaasStormTaskSpout ipaasStormTaskSpout : ipaasStormTaskSpouts) {
//	 ipaasStormTaskSpout.setTaskId(ipaasStormTaskInfo.getId());
//	 iIpaasStormTaskSpoutSv.add(ipaasStormTaskSpout);
//	 }
//	 //
//	 for (IpaasStormTaskBolt ipaasStormTaskBolt : ipaasStormTaskBolts) {
//	 ipaasStormTaskBolt.setTaskId(ipaasStormTaskInfo.getId());
//	 iIpaasStormTaskBoltSv.add(ipaasStormTaskBolt);
//	 }
//	 }
	// /**
	// * 编辑计算任务
	// */
	// public void editTask(IpaasStormTaskInfo ipaasStormTaskInfo,
	// List<IpaasStormTaskSpout> ipaasStormTaskSpouts, List<IpaasStormTaskBolt>
	// ipaasStormTaskBolts)
	// throws PaasException{
	// try {
	// //
	// update(ipaasStormTaskInfo);
	// //
	// Map<String, Object> params = new HashMap<String, Object>();
	// params.put(IpaasStormTaskSpout.PRO_TASKID, ipaasStormTaskInfo.getId());
	// List<IpaasStormTaskSpout> _ipaasStormTaskSpouts =
	// iIpaasStormTaskSpoutSv.searchList(params);
	// for (IpaasStormTaskSpout ipaasStormTaskSpout : _ipaasStormTaskSpouts) {
	// iIpaasStormTaskSpoutSv.del(ipaasStormTaskSpout.getId());
	// }
	// params.clear();
	// params.put(IpaasStormTaskBolt.PRO_TASKID, ipaasStormTaskInfo.getId());
	// List<IpaasStormTaskBolt> _ipaasStormTaskBolts =
	// iIpaasStormTaskBoltSv.searchList(params);
	// for (IpaasStormTaskBolt ipaasStormTaskBolt : _ipaasStormTaskBolts) {
	// iIpaasStormTaskBoltSv.del(ipaasStormTaskBolt.getId());
	// }
	// //
	// for (IpaasStormTaskSpout ipaasStormTaskSpout : ipaasStormTaskSpouts) {
	// ipaasStormTaskSpout.setTaskId(ipaasStormTaskInfo.getId());
	// iIpaasStormTaskSpoutSv.add(ipaasStormTaskSpout);
	// }
	// //
	// for (IpaasStormTaskBolt ipaasStormTaskBolt : ipaasStormTaskBolts) {
	// ipaasStormTaskBolt.setTaskId(ipaasStormTaskInfo.getId());
	// iIpaasStormTaskBoltSv.add(ipaasStormTaskBolt);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
}
