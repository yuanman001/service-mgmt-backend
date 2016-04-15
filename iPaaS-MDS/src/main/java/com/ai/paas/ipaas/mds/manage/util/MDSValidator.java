package com.ai.paas.ipaas.mds.manage.util;

import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;
import com.ai.paas.ipaas.util.StringUtil;

public class MDSValidator {
	public static boolean isNullParam(String createApply) {
		if (StringUtil.isBlank(createApply)) {
			return true;
		}
		return false;
	}

	public static boolean isNullApply(MsgSrvApply apply) {
		if (null == apply || StringUtil.isBlank(apply.getUserId())
				|| StringUtil.isBlank(apply.getServiceId())
				|| StringUtil.isBlank(apply.getApplyType())
				|| StringUtil.isBlank(apply.getTopicEnName())) {
			return true;
		}
		return false;
	}

	public static boolean isNullCanelApply(MsgSrvApply apply) {
		if (null == apply || StringUtil.isBlank(apply.getUserId())
				|| StringUtil.isBlank(apply.getServiceId())
				|| StringUtil.isBlank(apply.getApplyType())) {
			return true;
		}
		return false;
	}

	public static boolean isIllegalApply(MsgSrvApply apply) {
		if (apply.getTopicPartitions() <= 0 || apply.getMsgReplica() < 0) {
			return true;
		}
		return false;
	}
}
