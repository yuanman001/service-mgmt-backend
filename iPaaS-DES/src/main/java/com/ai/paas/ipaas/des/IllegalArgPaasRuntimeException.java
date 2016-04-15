package com.ai.paas.ipaas.des;

import com.ai.paas.ipaas.PaaSConstant;
import com.ai.paas.ipaas.PaasRuntimeException;

public class IllegalArgPaasRuntimeException extends PaasRuntimeException {

	public IllegalArgPaasRuntimeException() {
		super(PaaSConstant.ExceptionCode.PARAM_IS_NULL, "argument can't be null");
	}

	public IllegalArgPaasRuntimeException(String message) {
		super(PaaSConstant.ExceptionCode.PARAM_IS_NULL, message);
	}
}
