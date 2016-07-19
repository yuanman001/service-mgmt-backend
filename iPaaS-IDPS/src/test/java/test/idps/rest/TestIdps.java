package test.idps.rest;

import java.util.Map;

import com.ai.paas.ipaas.idps.service.util.IdpsParamUtil;

public class TestIdps {
	static String url = "http://10.1.228.200:10888/services";
	
	public static void main(String[] args){
	
		clean();
		
	}
	
	
	private static void clean() {
		long s = System.currentTimeMillis();
		String param = "{resultMsg:Apply IDPS successfully created!,dssServicePwd:123456,serviceId:IDPS009,dssPId:9A4792E9516B48E28F96AC1DC231708E,nodeNum:2,cpuNum:1,userId:8E7ECAC706994DB9AC2BBB037C18762B,resultCode:000000,mem:512,applyType:create,dssServiceId:DSS007,serviceName:idps_peng_11}";

		
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/clean",param));
		System.out.println("----------删除多台，耗时："+(System.currentTimeMillis()-s));

	}
	
	private static void upgrade(){
		    long s = System.currentTimeMillis();
			String param = "{resultMsg:Apply IDPS successfully created!,dssServicePwd:123456,serviceId:IDPS009,dssPId:9A4792E9516B48E28F96AC1DC231708E,nodeNum:2,cpuNum:1,userId:8E7ECAC706994DB9AC2BBB037C18762B,resultCode:000000,mem:512,applyType:create,dssServiceId:DSS007,serviceName:idps_peng_11}";

			System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/upgrade",param));
			System.out.println("----------删除多台，耗时："+(System.currentTimeMillis()-s));
	}
	
	private static void stop() {
		long s = System.currentTimeMillis();
		
		String param = "{resultMsg:Apply IDPS successfully created!,dssServicePwd:123456,serviceId:IDPS009,dssPId:9A4792E9516B48E28F96AC1DC231708E,nodeNum:2,cpuNum:1,userId:8E7ECAC706994DB9AC2BBB037C18762B,resultCode:000000,mem:512,applyType:create,dssServiceId:DSS007,serviceName:idps_peng_11}";

		
		
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/stop",param));
		System.out.println("----------停止多台，耗时："+(System.currentTimeMillis()-s));

	}
	
	//创建时的参数和其他参数不一样，注意一下。
	private static void create() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"8E7ECAC706994DB9AC2BBB037C18762B\",\"applyType\":\"create\",\"serviceId\":\"IDPS007\",\"nodeNum\":\"2\","
				+ "\"dssPId\":\"9A4792E9516B48E28F96AC1DC231708E\",\"dssServiceId\":\"DSS007\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"idps_zhao_007\"}";
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/create",param));
		System.out.println("----------开通单台，耗时："+(System.currentTimeMillis()-s));

	}
	
	@SuppressWarnings("unused")
	private static void createMany() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"IDPS033\",\"nodeNum\":\"2\","
				+ "\"dssPId\":\"06F638866A564153B2E9EB9EE5BDC6C1\",\"dssServiceId\":\"DSS001\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"many\"}";
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/create",param));
		System.out.println("----------开通多台，耗时："+(System.currentTimeMillis()-s));
	}

}
