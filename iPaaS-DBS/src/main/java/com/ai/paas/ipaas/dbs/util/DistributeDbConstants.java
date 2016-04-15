package com.ai.paas.ipaas.dbs.util;

public class DistributeDbConstants {
	
	public static  final String  dirSplit = "/";
	
	public static  final String underline ="_";
 	
	public static class ConfPath{
		
		public final static String  CACHE_CONF_PATH = "cache";
		
		public final static String  TABLE_CONF_PATH = "tableRules";
		
		public final static String  DB_RULE_PATH = "dbRule";
		
		public final static String DRDS_CONF_PATH = "DBS";
		
		
		
		public final static String DISTRIBUTEDB_CONF_PATH = "logicdb";
		
		public final static String SEQUENCE_DB_CONF = "sequenceDb";
		
		public final static String  SEQUENCE_SERVICE_CONF_PATH = "sequences";
		
		public final static String  SDK_USER_INFO = "sdkuser";
		
		public final static String  REAL_DBS = "realDbs";
		
		public final static String  DB_LIST = "dblist";
		
		public final static String DRDS_DB_MONITOR_PATH="dbmonitor";
		
		public final static String META = "meta";
		
		public final static String TABLE = "table";
		
		public final static String  DB = "db";
		
		public final static String  DB_PATTERN = "db_pattern";
		
		public final static String  HA_AUTO_SWITCH="ha_auto_switch";

	}
	
	public  final static String   sequenceSql = "CREATE TABLE if not exists `sequence` (`name` varchar(32) NOT NULL,`value` bigint(20) DEFAULT NULL,PRIMARY KEY (`name`)) ENGINE=InnoDB DEFAULT CHARSET=utf8";
	
    public static class  ResUseStatus  {
    	
    	//资源空闲
    	public final static String  RES_FREE = "0";
    	//资源使用
    	public final static String  RES_USED = "1";
    	
    	
    }
    //主从标示
    public static class  ResMsFlag{
    	
    	public final static String  MASTER = "1";
    	
    	public final static String  SLAVE = "0";
    }
    
    
    public static class ResUseType {
    	
    	public final static String  BUSI_DB = "10";
    	
    	public final static String  SEQUENCE_DB = "11";
    	
    }

    public static class ResStatus {
    	
    	public final static String  NORMAL = "10";
    	
    	public final static String  CRASH = "11";
    	
    }
    
    public static class IsTxs{
    	public final static String  ISTXS = "1";
    	
    	public final static String   WITHOUTTXS= "0";
    }
    
    public static class MuiResourceStatus{
    	public final static String Free="0";
    	
    	public final static String Used="1";
    }
    
    public static class MuiUserServiceStatus{
    	public final static String Effective="0";
    	
    	public final static String Invalid="1";
    }
    
    //dbs_user_service isMysqlProxy 是否读写分离
    
    public static class IsMysqlProxy{
    	public final static String useMysqlProxy="0";
    	
    	public final static String withoutProxy="1";
    }
    
    //dbs_user_service isAutoSwitch 是否自动切换（只有读写分离的情况下才能自动切换）
    
    public static class IsAutoSwitch{
    	
    	public final static String autoSwith="0";
    	
    	public final static String withoutSwitch="1";
    }
}
