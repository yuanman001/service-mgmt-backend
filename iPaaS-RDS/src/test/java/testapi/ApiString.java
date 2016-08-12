package testapi;

import java.sql.Timestamp;

import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsIncBase;
import com.ai.paas.ipaas.rds.dao.mapper.bo.RdsResourcePool;
import com.ai.paas.ipaas.rds.service.transfer.vo.CancelRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.CreateRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StartRDS;
import com.ai.paas.ipaas.rds.service.transfer.vo.StopRDS;
import com.google.gson.Gson;

public class ApiString {

	public static void main(String[] args) {
		Gson g = new Gson();
		//add res
		Timestamp time = new Timestamp(System.currentTimeMillis()); 
	//	RdsResourcePool rdsRes = new  RdsResourcePool("192.168.1.125",50000,5000,5001,1,"hbhb123","hbhb123",1,2000000,20,"/home",time,time);
		RdsResourcePool rdsRes = new  RdsResourcePool("10.1.245.226",50000,5000,5001,1,"root","123456",1,2000000,20,"percona_volumn/data");
//		rdsRes.set
		String request = g.toJson(rdsRes);
		System.out.println(request);
		rdsRes = g.fromJson(request, RdsResourcePool.class);
//		System.out.println(rdsRes.getSshpassword());
		
		// delete res
		RdsResourcePool rdsRes1 = new  RdsResourcePool();
		rdsRes1.setResourceid(1);
		String request1 = g.toJson(rdsRes1);
		System.out.println(request1);
		
		// first create rds
		CreateRDS creatObject = new CreateRDS();
		creatObject.createSlaverNum = 1;
		creatObject.createBatmasterNum = 0;
		// user_id对应ccs_user_config中的用户
		creatObject.instanceBase = new RdsIncBase("6C4F4DBA96294DDCBC5DBBF2CAD442B5", 
				"testmysql", "BIU", 5, 100, "","",
				"mysql6", "", 0, 1, "BIU,MYSQL,TEST",
				"BEIJING", 1, "no describe", "/aifs01", 
				"/aifs01/mysqldata","", "192.168.*.*", 
				"root", "root", "containerName",
				"1234", 50000, 2000, 123, 500,time,time);
		String request2 = g.toJson(creatObject);
		System.out.println(request2);
		
		// cancel rds, only master 
		CancelRDS cancelObject = new CancelRDS();
		cancelObject.instanceid = 73;
		String request3 = g.toJson(cancelObject);
		System.out.println(request3);
		
		// stop exist inc
		StopRDS stopObject = new StopRDS();
		stopObject.instanceid = 49;
		String request4 = g.toJson(stopObject);
		System.out.println(request4);
		
		
		// start exist inc
		StartRDS startObject = new StartRDS();
		startObject.instanceid = 49;
		String request5 = g.toJson(startObject);
		System.out.println(request5);
	}

}
