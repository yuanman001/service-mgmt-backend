package com.ai.paas.ipaas.ats.util;

import com.ai.paas.ipaas.mds.manage.vo.MsgSrvApply;

public class MsgSrv extends MsgSrvApply {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5353512725781895189L;
	private int messageType;
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	
}
