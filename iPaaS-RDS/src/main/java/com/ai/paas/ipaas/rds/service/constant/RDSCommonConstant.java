package com.ai.paas.ipaas.rds.service.constant;


/**
 * @author 作者 “WTF” E-mail: 1031248990@qq.com
 * @date 创建时间：2016年7月11日 下午4:01:33
 * @version
 * @since
 */
public interface RDSCommonConstant {

	// RDSInstanceStatus
	public int INS_ACTIVATION = 1;
	public int INS_FREEZE = 2;
	public int INS_STARTING = 3;
	public int INS_STARTED = 4;
	public int INS_STOPPING = 5;
	public int INS_STOPPED = 6;

	// ResourceStatus
	public int RES_STATUS_USABLE = 1;
	public int RES_STATUS_UNUSABLE = 2;
	public int RES_CYCLE_USABLE = 1;
	public int RES_CYCLE_UNUSABLE = 2;
	
	/**沉淀在zookeeper的信息*/
	public String RDS_ZK_PATH="/RDS/";
	public String RDS_IMAGE_URL="IMAGEURL";
	
	public int LIMIT_IDLE_USEABLE_EXTERNAL_STORAGE = 5;
}
