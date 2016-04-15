package com.ai.paas.ipaas.ats.util;

import com.ai.paas.ipaas.util.StringUtil;

public class ATSValidator {

	public static boolean isNullParam(String createApply) {
		if (StringUtil.isBlank(createApply)) {
			return true;
		}
		return false;
	}
}
