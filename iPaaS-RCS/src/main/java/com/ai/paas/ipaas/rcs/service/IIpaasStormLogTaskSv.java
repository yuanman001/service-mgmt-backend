package com.ai.paas.ipaas.rcs.service;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.rcs.dto.IpaasStormLogTask;

public interface IIpaasStormLogTaskSv {
	void registerLogTask(IpaasStormLogTask logTask) throws PaasException;
}
