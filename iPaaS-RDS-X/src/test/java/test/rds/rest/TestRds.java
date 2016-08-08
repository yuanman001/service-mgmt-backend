package test.rds.rest;

import java.sql.Timestamp;

import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.google.gson.Gson;

public class TestRds {
	static String url = "http://10.1.228.200:10888/services";
	
	public static void main(String[] args){
		TestRds.test();
	}
	
	
	private static void test(){
		Gson g = new Gson();
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
//		CreateRDS creatObject = new CreateRDS();
//		creatObject.createSlaverNum = 1;
//		creatObject.createBatmasterNum = 0;
//		// user_id对应ccs_user_config中的用户
//		creatObject.instanceBase = new RdsIncBase("6C4F4DBA96294DDCBC5DBBF2CAD442B5", "testmysql", "BIU", 5, 100, "","",
//				"mysql6", "", 0, 1, "BIU,MYSQL,TEST","BEIJING", 1, "no describe", "/aifs01", 
//				"/aifs01/mysqldata","", "192.168.*.*", "root", "root", "containerName",
//				"1234", 10000, 2000, 123, 500,time,time);
//		String request = g.toJson(creatObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/create/master",request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
		
		
//		CancelRDS cancelObject = new CancelRDS();
//		cancelObject.instanceid = 83;
//		String request = g.toJson(cancelObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/cancel",request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
		
		
//		StopRDS stopObject = new StopRDS();
//		stopObject.instanceid = 83;
//		String request = g.toJson(stopObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/stop",request);
//		System.out.println(result);
		
		
//		StartRDS startObject = new StartRDS();
//		startObject.instanceid = 83;
//		String request = g.toJson(startObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/start",request);
//		System.out.println(result);
	}
	

}
