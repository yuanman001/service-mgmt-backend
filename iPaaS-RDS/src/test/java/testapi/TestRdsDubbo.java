package testapi;


import java.sql.Timestamp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSInstanceManager;
import com.ai.paas.ipaas.rds.manage.rest.interfaces.IRDSResourcePool;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.ChangeContainerConfig;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDSResult;
import com.ai.paas.ipaas.rds.service.transfer.vo.GetIncInfo;
import com.ai.paas.ipaas.rds.service.transfer.vo.ModifyRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.SwitchMaster;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/rds-consumer.xml" })

public class TestRdsDubbo {
	@Reference
	private IRDSInstanceManager incManager;
	@Reference
	private IRDSResourcePool resMananger;
	
	Gson g = new Gson();
	
//	@Test
//	public void switchmaster(){
//		String result = incOperater.switchmaster(g.toJson(new SwitchMaster(114)));
//		System.out.println(result);
//	}
	
	/**
	 * passed
	 */
//	@Test
//	public void getIncList() {
//		GetIncInfo getIncInfo = new GetIncInfo();
//		getIncInfo.getAll = 1;
//		String resquest = g.toJson(getIncInfo);
//		String result = incManager.getinstanceinfo(resquest);
//		System.out.println(result);
//	}
	
	
	/**
	 * passed
	 */
//	@Test
//	public void getFuncList() {
//		String funcList = incManager.getFuncList();
//		System.out.println(funcList);
//	}
	
	/**
	 * passed
	 * 解析方法:不用解析，为字符串
	 */
//	@Test
//	public void addRes(){
//		
//		Timestamp time = new Timestamp(System.currentTimeMillis()); 
////		RdsResourcePool rdsRes = new  RdsResourcePool("192.168.1.125",50000,5000,5001,1,"hbhb123","hbhb123",1,2000000,20,"/home",time,time);
//		RdsResourcePool rdsRes = new  RdsResourcePool("10.1.245.226",50000,5000,5001,1,"root","123456",1,2000000,20,"percona_volumn/data",time,time);
//		String request = g.toJson(rdsRes);
//		System.out.println(request);
//		String result = resMananger.add(request);
//		System.out.println(result);
//	}

	/**
	 * passed
	 * 解析方法:不用解析，为字符串
	 */
//	@Test
//	public void deleteRes(){
//		
//		RdsResourcePool rdsRes = new  RdsResourcePool();
//		rdsRes.setResourceid(1);
//		String request = g.toJson(rdsRes);
//		System.out.println(request);
//		String result = resMananger.delete(request);
//		System.out.println(result);
//	}

	/**
	 * 博士说这里不用做备用服务器，0个备用
	 * 
	 * ["{\"instanceBase\":{\"incName\":\"实例名称5\",\"serviceId\":\"RDS005\",\"incDescribe\":\"实例描述\",\"userId\":\"8E7ECAC706994DB9AC2BBB037C18762B\",\"maxConnectNum\":\"300\",\"dbStoreage\":\"10000\",\"incType\":\"1\",\"incTag\":\"实例标签5\",\"incLocation\":\"实例位置55\",\"depId\":\"部门名称5\"},\"createBatmasterNum\":0,\"createSlaverNum\":1}"]
	 * passed
	 * 解析方法:CreateRDSResult ct = g.fromGson(obj,CreateRDSResult.class)
	 * 主要是status值有用
	 */
//	@Test
//	public void create(){
//		Timestamp time = new Timestamp(System.currentTimeMillis()); 
//		CreateRDS creatObject = new CreateRDS();
//		creatObject.createSlaverNum = 1;
//		creatObject.createBatmasterNum = 0;
//		creatObject.instanceBase = new RdsIncBase(
//				"6C4F4DBA96294DDCBC5DBBF2CAD442B5", //UserID
//				"test_res_lim_mysql", //serviceId 
//				"BIU",// depId 部门
//				5, // imgId
//				0, // resId
//				"", // bakId 无用
//				"", // slaverId 无用
//				"mysql6", // incName
//				"", // incIp
//				0,  // incPort
//				1, // incType
//				"BIU,MYSQL,TEST", // incTag
//				"BEIJING", // incLocation
//				1, // incStatus
//				"no describe", // incDescribe
//				"/aifs01", // mysqlHome 无用，服务器提供固定值
//				"/aifs01/mysqldata", // mysqlDataHome 无用，服务器会生成固定值
//				"", // mysqlVolumnPath 无用，服务器提供固定值
//				"192.168.*.*,10.1.*.*,localhost,%.%.%.%", // whiteList
//				"rootusr", // rootName
//				"123456", // rootPassword
//				"containerName", // containerName
//				"1234", // dbServerId
//				10000, // dbStoreage
//				2000, // dbUsedStorage
//				2, // intStorage
//				500, // maxConnectNum
//				0, // masterid 无用
//				"1",// cpu属于可分配资源 不对应cpuInfo，这里代表需要cpu数量
//				5, // netBandwidth
//				"on", // sqlAudit （on，off）
//				"semisynchronous", // syncStrategy（分为半同步semisynchronous，异步asynchronous）
//				time,time);
//		String request = g.toJson(creatObject);
//		System.out.println(request);
//		String result = incManager.create(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		CreateRDSResult ssss = g.fromJson(result, CreateRDSResult.class);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//	}
	
//	@Test
//	public void switchMaster(){
//		SwitchMaster sm = new SwitchMaster(152);
//		String request = g.toJson(sm);
//		System.out.println(request);
//		String result = incManager.switchmaster(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		CreateRDSResult ssss = g.fromJson(result, CreateRDSResult.class);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//	}
	
	/**
	 * passed
	 */
//	@Test
//	public void createByJSONString(){
//		String request = "{\"instanceBase\": {\"userId\": \"6C4F4DBA96294DDCBC5DBBF2CAD442B5\","
//				+ "\"serviceId\": \"testmysql\",\"depId\": \"BIU\",\"incName\": \"mysql6\","
//				+ "\"incType\": 1,\"incTag\": \"BIU\",\"incLocation\": \"BEIJING\","
//				+ "\"incDescribe\": \"nodescribe\",\"dbServerId\": \"1234\",\"dbStoreage\": 50000,"
//				+ "\"maxConnectNum\": 500},\"createBatmasterNum\": 0,\"createSlaverNum\": 1}";
//		System.out.println(request);
//		String result = incManager.create(request);
//		System.out.println(result);
//	}
	
	/**
	 * 
	 */
//	@Test
//	public void createslobm(){
//		Timestamp time = new Timestamp(System.currentTimeMillis());
//		CreateSRDS createObject = new CreateSRDS();
//		createObject.masterinstanceid = 58;
//		createObject.thisInstanceType = 2;
//		String request = g.toJson(createObject);
//		System.out.println(request);
//		String result = incManager.createslobm(request);
//		System.out.println(result);
//	}

	/**
	 * passed
	 * 解析方法:CancelRDSResult ct = g.fromGson(obj,CancelRDSResult.class)
	 * 主要是status值有用
	 */
//	@Test
//	public void cancel(){
//		CancelRDS cancelObject = new CancelRDS();
//		cancelObject.instanceid = 218;
//		String request = g.toJson(cancelObject);
//		System.out.println(request);
//		String result = incManager.cancel(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//	}
	
	/**
	 * passed
	 * 解析方法:StopRDSResult ct = g.fromGson(obj,StopRDSResult.class)
	 * 主要是status值有用
	 */
//	@Test
//	public void stop(){
//		StopRDS stopObject = new StopRDS();
//		stopObject.instanceid = 218;
//		String request = g.toJson(stopObject);
//		System.out.println(request);
//		String result = incManager.stop(request);
//		System.out.println(result);
//	}
	
//	@Test
//	public void restart(){
//		StopRDS stopObject = new StopRDS();
//		stopObject.instanceid = 218;
//		String request = g.toJson(stopObject);
//		System.out.println(request);
//		String result = incManager.stop(request);
//		System.out.println(result);
//		StartRDS startObject = new StartRDS();
//		startObject.instanceid = 218;
//		String request1 = g.toJson(startObject);
//		System.out.println(request1);
//		String result1 = incManager.start(request1);
//		System.out.println(result1);
//	}
	
	/**
	 * passed
	 * 解析方法:StartRDSResult ct = g.fromGson(obj,StartRDSResult.class)
	 * 主要是status值有用
	 */
//	@Test
//	public void start(){
//		StartRDS startObject = new StartRDS();
//		startObject.instanceid = 165;
//		String request = g.toJson(startObject);
//		System.out.println(request);
//		String result = incManager.start(request);
//		System.out.println(result);
//	}
	
//	@Test 
//	public void changecontainerconfig(){
//		ChangeContainerConfig ccc = new ChangeContainerConfig();
//		ccc.cpu = "1";
//		ccc.ExtStorage = 20000;
//		ccc.groupMasterId =165;
//		ccc.IntStorage = 1;
////		ccc.instanceid = 165;
//		String request = g.toJson(ccc);
//		System.out.println(request);
//		String result = incManager.start(request);
//		System.out.println(result);
//	}
	
	/**
	 * passed
	 */
//	@Test
//	public void modify(){
//		ModifyRDS modifyObject = new ModifyRDS();
//		modifyObject.groupMasterId = 218;
//		modifyObject.IntStorage = 10;
//		modifyObject.cpu = "1"; //cpu数量
//		modifyObject.ExtStorage = 10000;
//		modifyObject.NetBandwidth = 10;
//		String request = g.toJson(modifyObject);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$request$$$$$$$$$$$$$$$$$$$");
//		System.out.println(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$request$$$$$$$$$$$$$$$$$$$");
//		String result = incManager.modify(request);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//		System.out.println(result);
//		System.out.println("$$$$$$$$$$$$$$$$$$$$result$$$$$$$$$$$$$$$$$$$");
//	}
}
