package com.ai.paas.ipaas.rds.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.JsonSyntaxException;

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
		try {
			return rdsIncMG.create(createApply);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String createslobm(String create) {
		try {
			return rdsIncMG.createslobm(create);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String cancel(String cancelApply) {
		try {
			return rdsIncMG.cancel(cancelApply);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String getFuncList() {
		return rdsIncMG.getFuncList();
	}

	@Override
	public String start(String startApply) {
		try {
			return rdsIncMG.start(startApply);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String stop(String stopApply) {
		try {
			return rdsIncMG.stop(stopApply);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String restart(String restartApply) {
		try {
			return rdsIncMG.restart(restartApply);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String getinstanceinfo(String getinstanceinfo) {
		return rdsIncMG.getinstanceinfo(getinstanceinfo);
	}

	/**
	 * @deprecated
	 */
	@Override
	public String modify(String modify) {
		try {
			return rdsIncMG.modify(modify);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	/**
	 * @deprecated
	 */
	@Override
	public String switchmaster(String switchmaster) {
		try {
			return rdsIncMG.switchmaster(switchmaster);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}

	@Override
	public String changecontainerconfig(String changecontainerconfig) {
		try {
			return rdsIncMG.changecontainerconfig(changecontainerconfig);
		} catch (MyException e) {
			return e.JSONErrorInfo;
		}
	}


}
