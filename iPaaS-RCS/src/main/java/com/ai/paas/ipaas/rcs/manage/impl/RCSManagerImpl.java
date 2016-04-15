package com.ai.paas.ipaas.rcs.manage.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.constants.Constants;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsBoltInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSpoutInfo;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsSysParameter;
import com.ai.paas.ipaas.rcs.dao.mapper.bo.RcsTaskInfo;
import com.ai.paas.ipaas.rcs.dto.IpaasStormLogTask;
//import com.ai.paas.ipaas.rcs.dubbo.interfaces.IStormTaskDubboSV;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskBoltVo;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskInfoVo;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskSpoutVo;
import com.ai.paas.ipaas.rcs.manage.rest.interfaces.IRCSManager;
import com.ai.paas.ipaas.rcs.service.IIpaasRcsOpenSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormLogTaskSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskBoltSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskInfoSv;
import com.ai.paas.ipaas.rcs.service.IIpaasStormTaskSpoutSv;
import com.ai.paas.ipaas.rcs.service.IRcsSysParamSv;
import com.ai.paas.ipaas.rcs.util.RcsParamUtil;
import com.ai.paas.ipaas.rcs.vo.OperTaskType;
import com.ai.paas.ipaas.rcs.vo.PageEntity;
import com.ai.paas.ipaas.rcs.vo.PageResult;
import com.ai.paas.ipaas.rcs.vo.PageSelectValue;
import com.ai.paas.ipaas.rcs.vo.StormClusterInfoVo;
import com.ai.paas.ipaas.rcs.vo.StormTaskBoltVo;
import com.ai.paas.ipaas.rcs.vo.StormTaskInfoVo;
import com.ai.paas.ipaas.rcs.vo.StormTaskSpoutVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;

@Service
public class RCSManagerImpl implements IRCSManager {

	@Autowired
	private IIpaasStormTaskInfoSv iIpaasStormTaskInfoSv;

	@Autowired
	private IIpaasStormTaskSpoutSv iIpaasStormTaskSpoutSv;

	@Autowired
	private IIpaasStormTaskBoltSv iIpaasStormTaskBoltSv;

	@Autowired
	private IIpaasStormLogTaskSv iIpaasStormLogTaskSv;

	@Autowired
	private IIpaasRcsOpenSv iIpaasRcsOpenSv;

	@Autowired
	private IRcsSysParamSv iSysParaSv;
	
	/**
	 * 计算任务注册
	 */
	@Override
	public String registerTask(String rcsInfoVo) {
		Gson gson = new Gson();
		StormTaskInfoVo stormTaskInfoVo = new StormTaskInfoVo();
		stormTaskInfoVo = gson.fromJson(rcsInfoVo, StormTaskInfoVo.class);

		RcsTaskInfo rcsTaskInfo = new RcsTaskInfo();
		List<RcsSpoutInfo> rcsSpoutInfos = new ArrayList<RcsSpoutInfo>();
		List<RcsBoltInfo> rcsBoltInfos = new ArrayList<RcsBoltInfo>();
		BeanUtils.copyProperties(stormTaskInfoVo, rcsTaskInfo);

		rcsTaskInfo.setRegisterDt(new Date());
		rcsTaskInfo.setStatus("0");

		if (stormTaskInfoVo.getStormTaskSpoutVos() != null) {
			for (StormTaskSpoutVo stormTaskSpoutVo : stormTaskInfoVo
					.getStormTaskSpoutVos()) {
				RcsSpoutInfo rcsSpoutInfo = new RcsSpoutInfo();
				BeanUtils.copyProperties(stormTaskSpoutVo, rcsSpoutInfo);
				rcsSpoutInfos.add(rcsSpoutInfo);
			}
		}

		if (stormTaskInfoVo.getStormTaskBoltVos() != null) {
			for (StormTaskBoltVo stormTaskSpoutVo : stormTaskInfoVo
					.getStormTaskBoltVos()) {
				RcsBoltInfo rcsBoltInfo = new RcsBoltInfo();
				BeanUtils.copyProperties(stormTaskSpoutVo, rcsBoltInfo);
				rcsBoltInfos.add(rcsBoltInfo);
			}
		}

		String res = null;
		try {
			res = this.iIpaasStormTaskInfoSv.registerTask(rcsTaskInfo,
					rcsSpoutInfos, rcsBoltInfos);
			//若重复则返回
			if(res=="false"){
				return res;
			}
			
			IpaasStormLogTask ipaasStormLogTask = new IpaasStormLogTask();
			ipaasStormLogTask.setLoggerName(stormTaskInfoVo.getName());
			iIpaasStormLogTaskSv.registerLogTask(ipaasStormLogTask);
		} catch (PaasException e) {
			e.printStackTrace();
		}

		return RcsParamUtil.getReturn(rcsInfoVo, res);
	}

	@Override
	public String PagingResult(String params) {
		Gson gson = new Gson();
		PageEntity pageEntity = new PageEntity();

		pageEntity = gson.fromJson(params, PageEntity.class);
		PageResult<StormTaskInfoVo> pageResult = new PageResult<StormTaskInfoVo>();
        
		//私有方法countList调用
		pageEntity.setCurrentPage(pageEntity.getCurrentPage());
		pageResult = iIpaasStormTaskInfoSv.searchPage(pageEntity);
		
		List<StormTaskInfoVo> stormTaskInfoVos = new ArrayList<StormTaskInfoVo>();
		
		List<StormTaskInfoVo> stormTaskInfos = pageResult.getResultList();
		for (StormTaskInfoVo vo : stormTaskInfos) {
			StormTaskInfoVo stormTaskInfoVo = new StormTaskInfoVo();
			BeanUtils.copyProperties(vo, stormTaskInfoVo);

			RcsSysParameter bean = iSysParaSv.selectByParaValue(vo.getClusterId() + "");
			stormTaskInfoVo.setClusterName(bean.getParaComment());
			stormTaskInfoVos.add(stormTaskInfoVo);
		}
		pageResult.setResultList(stormTaskInfoVos);
		
		return gson.toJson(pageResult);
	}

	@Override
	public String getCluster(String userId) {
		PageSelectValue<StormClusterInfoVo> selectValues = new PageSelectValue<StormClusterInfoVo>();
		selectValues = iIpaasRcsOpenSv.searchClusterInfo(userId);
		
        Gson gson = new Gson();
        return gson.toJson(selectValues);
	}
	
	@Override
	public String editTask(String stormTaskInfoVo) {
		return null;
	}

	@Override
	public String operTask(String params) {
		int count = 0;
		Gson gson = new Gson();
		OperTaskType opertask = gson.fromJson(params, OperTaskType.class);
		String operType = opertask.getOperType();
		String id = opertask.getID();
		RcsTaskInfo rcsTaskInfo = null;
		try {
			rcsTaskInfo = iIpaasStormTaskInfoSv.searchOneById(id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (operType.equalsIgnoreCase("start")) {
			rcsTaskInfo.setStatus(Constants.STATUS_TASK_INFO.RUNNING);
		} else if (operType.equalsIgnoreCase("cancel")) {
			rcsTaskInfo.setCancelDt(new Date());
			rcsTaskInfo.setStatus(Constants.STATUS_TASK_INFO.DELETED);
		} else if (operType.equalsIgnoreCase("stop")) {
			rcsTaskInfo.setStatus(Constants.STATUS_TASK_INFO.STOPED);
		} else {
			// throw new PaasException("999999", "operType有误");
		}

		try {
			count = iIpaasStormTaskInfoSv.update(rcsTaskInfo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count + "";
	}

	@Override
	public String getTask(String id) {
		Gson gson = new Gson();
		RcsTaskInfo rcsTaskInfo = iIpaasStormTaskInfoSv.searchOneById(id);
		List<RcsSpoutInfo> ipaasStormTaskSpouts = iIpaasStormTaskSpoutSv
				.searchList(rcsTaskInfo.getId());
		List<RcsBoltInfo> ipaasStormTaskBolts = iIpaasStormTaskBoltSv
				.searchList(rcsTaskInfo.getId());
		StormTaskInfoVo stormTaskInfoVo = new StormTaskInfoVo();
		BeanUtils.copyProperties(rcsTaskInfo, stormTaskInfoVo);
		List<StormTaskSpoutVo> stormTaskSpoutVos = new ArrayList<StormTaskSpoutVo>();
		for (RcsSpoutInfo rcsSpoutInfo : ipaasStormTaskSpouts) {
			StormTaskSpoutVo stormTaskSpoutVo = new StormTaskSpoutVo();
			BeanUtils.copyProperties(rcsSpoutInfo, stormTaskSpoutVo);
			stormTaskSpoutVos.add(stormTaskSpoutVo);
		}
		stormTaskInfoVo.setStormTaskSpoutVos(stormTaskSpoutVos);
		List<StormTaskBoltVo> stormTaskBoltVos = new ArrayList<StormTaskBoltVo>();
		for (RcsBoltInfo rcsBoltInfo : ipaasStormTaskBolts) {
			StormTaskBoltVo stormTaskBoltVo = new StormTaskBoltVo();
			BeanUtils.copyProperties(rcsBoltInfo, stormTaskBoltVo);
			stormTaskBoltVos.add(stormTaskBoltVo);
		}
		stormTaskInfoVo.setStormTaskBoltVos(stormTaskBoltVos);
		
		return gson.toJson(stormTaskInfoVo);
	}

	@Override
	public String create(String createApply) {
		// TODO Auto-generated method stub

		String res = null;
		try {
			res = iIpaasRcsOpenSv.openRcs(createApply);
		} catch (PaasException e) {
			e.printStackTrace();
		}
		return RcsParamUtil.getReturn(createApply, res);
	}

	@Override
	public String getFuncList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancel(String cancelApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String modifyApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String start(String startApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String stop(String stopApply) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String restart(String restartApply) {
		// TODO Auto-generated method stub
		return null;
	}
}
