package com.ai.paas.ipaas.agent.util;

import com.ai.paas.ipaas.PaasRuntimeException;

public class AidUtil {
	private AidUtil() {

	}

	private static String aid = null;

	/**
	 * 此方法为临时用法，等整个平台转到跨云或者一个管理端管理多个环境时，需要前端传进来 目前从环境变量中取
	 */
	public static String getAid() {
		if (null == aid) {
			aid = System.getenv("aid");
			if (aid == null) {
				aid = System.getProperty("aid");
			}
			if (null == aid) {
				throw new PaasRuntimeException(
						"Can not get aid, pls. -Daid=xxx");
			}
		}
		return aid;
	}
}
