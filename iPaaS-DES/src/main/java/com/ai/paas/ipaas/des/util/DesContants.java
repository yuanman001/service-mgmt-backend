package com.ai.paas.ipaas.des.util;

public class DesContants {
	public static final String SERVICE_ALLREADY_BOUND = "910030";

	public static final int DES_STATE_UNBIND = 0;
	public static final int DES_STATE_BIND = 1;
	public static final int DES_STATE_CANCEL = 2;

	public static final String DES_REST_SUCCESS = "success";
	public static final String DES_REST_ERROR = "error";

	public static final String CANAL_CONFIG_INSTANCES_PATH = "/aifs01/users/devmys01/canal/config/instances/";
	public static final String CANAL_CONFIG_INSTANCE_FILE = "instance.properties";
	public static final String CANAL_CONFIG_FILTER = "canal.instance.filter.regex";
	public static final String CANAL_CONFIG_BLACK_FILTER = "canal.instance.filter.black.regex";
	public static final String PAAS_MDS_TABLE_FILTER_RULE = "paas.mds.table.filter.rule";
	public static final String CANAL_CONFIG_FILTER_BLANK = ".*\\..*"; // 可以过滤所有表的通配符

	public static final String DBS_TABLE_PATERN_LEFT = "{";
	public static final String DBS_TABLE_PATER_RIGHT = "}";

	private DesContants() {
	}
}
