package test.rds.rest;

import java.sql.Timestamp;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDS;
import com.google.gson.Gson;

public class TestRds {
	static String url = "http://10.1.228.200:10888/services";
	
	public static void main(String[] args){
		TestRds.test();
	}
	
	
	private static void test(){
		Gson g = new Gson();
		
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
		CreateRDS creatObject = new CreateRDS();
		creatObject.createSlaverNum = 1;
		creatObject.createBatmasterNum = 0;
		creatObject.instanceBase = new RdsIncBase(
				"6C4F4DBA96294DDCBC5DBBF2CAD442B5", //UserID
				"test_res_lim_mysql", //serviceId 
				"BIU", // depId 部门
				5, // imgId
				0, // resId
				"", // bakId 无用
				"", // slaverId 无用
				"mysql6", // incName
				"", // incIp
				0,  // incPort
				1, // incType
				"BIU,MYSQL,TEST", // incTag
				"BEIJING", // incLocation
				1, // incStatus
				"no describe", // incDescribe
				"/aifs01", // mysqlHome 无用，服务器提供固定值
				"/aifs01/mysqldata", // mysqlDataHome 无用，服务器会生成固定值
				"", // mysqlVolumnPath 无用，服务器提供固定值
				"192.168.*.*,10.1.*.*,localhost,%.%.%.%", // whiteList
				"rootusr", // rootName
				"123456", // rootPassword
				"containerName", // containerName
				"1234", // dbServerId
				10000, // dbStoreage
				2000, // dbUsedStorage
				2, // intStorage
				500, // maxConnectNum
				0, // masterid 无用
				"1",// cpu属于可分配资源 不对应cpuInfo，这里代表需要cpu数量
				5, // netBandwidth
				"on", // sqlAudit （on，off）
				"semisynchronous" // syncStrategy（分为半同步semisynchronous，异步asynchronous）
				);
		String request = g.toJson(creatObject);
		System.out.println(request);
		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/create/master",request);
		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
		System.out.println(result);
		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
		
		
//		CancelRDS cancelObject = new CancelRDS();
//		cancelObject.instanceid = 220;
//		String request = g.toJson(cancelObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/cancel",request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
		
		
//		StopRDS stopObject = new StopRDS();
//		stopObject.instanceid = 220;
//		String request = g.toJson(stopObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/stop",request);
//		System.out.println(url+"/rds/mysql/manager/stop");
//		System.out.println(result);
		
		
//		StartRDS startObject = new StartRDS();
//		startObject.instanceid = 220;
//		String request = g.toJson(startObject);
//		System.out.println(request);
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/start",request);
//		System.out.println(result);
		
		
//		ModifyRDS modifyObject = new ModifyRDS();
//		modifyObject.groupMasterId = 220;
//		modifyObject.IntStorage = 1;
//		modifyObject.cpu = "1"; //cpu数量
//		modifyObject.ExtStorage = 10012;
//		modifyObject.NetBandwidth = 3;
//		String request = g.toJson(modifyObject);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$request$$$$$$$$$$$$$$$$$$$");
//		System.out.println(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$request$$$$$$$$$$$$$$$$$$$");
//		String result = RdsHttpClientUtil.send(url+"/rds/mysql/manager/modify",request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
	}
	

}
