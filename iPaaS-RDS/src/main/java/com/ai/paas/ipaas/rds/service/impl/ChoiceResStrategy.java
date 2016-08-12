package com.ai.paas.ipaas.rds.service.impl;

import java.util.List;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;


/** 
 * @author  作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月13日 下午2:23:50 
 * @version 
 * @since  
 */
public class ChoiceResStrategy {

	private ChoiceRes choiceRes;

	public ChoiceResStrategy(ChoiceRes choiceRes) {
		this.choiceRes = choiceRes;
	}
	
	public RdsResourcePool makeDecision(List<RdsResourcePool> canUseResList){
		return choiceRes.choiceOne(canUseResList);
	}

	public RdsResourcePool makeDecision(List<RdsResourcePool> canUseResList, List<RdsResourcePool> exceptList) {
		// TODO Auto-generated method stub
		return choiceRes.choiceOne(canUseResList,exceptList);
	}
}
