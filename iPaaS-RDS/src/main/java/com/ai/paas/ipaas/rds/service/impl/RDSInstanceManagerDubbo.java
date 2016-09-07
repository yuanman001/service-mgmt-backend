package com.ai.paas.ipaas.rds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * 传输对象命名规则就是对应方法名
 * 拓扑结构可以随意更改 
 * 空间是通过修改mysql配置和扩充磁盘阵列实现 
 * @author 作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:48:55
 * @version
 * @since
 */
@Service
public class RDSInstanceManagerDubbo implements IRDSInstanceManager {
	
	private static transient final Logger LOG = LoggerFactory.getLogger(RDSInstanceManagerDubbo.class);

	@Autowired
	private RDSInstanceManager rdsIncMG;
	
	@Override
	public String create(String createApply) {
		return rdsIncMG.create(createApply);
	}

	@Override
	public String createslobm(String create) {
		return rdsIncMG.createslobm(create);
	}

	@Override
	public String cancel(String cancelApply) {
		return rdsIncMG.cancel(cancelApply);
	}

	@Override
	public String getFuncList() {
		return rdsIncMG.getFuncList();
	}

	@Override
	public String start(String startApply) {
		return rdsIncMG.start(startApply);
	}

	@Override
	public String stop(String stopApply) {
		return rdsIncMG.stop(stopApply);
	}

	@Override
	public String restart(String restartApply) {
		return rdsIncMG.restart(restartApply);
	}

	@Override
	public String getinstanceinfo(String getinstanceinfo) {
		return rdsIncMG.getinstanceinfo(getinstanceinfo);
	}

	@Override
	public String modify(String modify) {
		return rdsIncMG.modify(modify);
	}

	@Override
	public String switchmaster(String switchmaster) {
		// TODO Auto-generated method stub
		return rdsIncMG.switchmaster(switchmaster);
	}

	@Override
	public String changecontainerconfig(String changecontainerconfig) {
		// TODO Auto-generated method stub
		return rdsIncMG.changecontainerconfig(changecontainerconfig);
	}


}
