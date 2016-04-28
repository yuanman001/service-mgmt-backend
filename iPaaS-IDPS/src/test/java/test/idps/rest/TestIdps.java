package test.idps.rest;

public class TestIdps {
	static String url = "http://192.168.1.125:20880/ipaas";
	
	public static void main(String[] args){
	
		create();
//		createMany();
	}
	
	
	
	private static void create() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"IDPS024\",\"nodeNum\":\"1\","
				+ "\"dssPId\":\"06F638866A564153B2E9EB9EE5BDC6C1\",\"dssServiceId\":\"DSS001\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"first\"}";
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/create",param));
		System.out.println("----------开通单台，耗时："+(System.currentTimeMillis()-s));

	}
	
	private static void createMany() {
		long s = System.currentTimeMillis();
		String param ="{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"IDPS033\",\"nodeNum\":\"2\","
				+ "\"dssPId\":\"06F638866A564153B2E9EB9EE5BDC6C1\",\"dssServiceId\":\"DSS001\","
				+ "\"dssServicePwd\":\"123456\",\"serviceName\":\"many\"}";
		System.out.println("----------create-res:\n"+HttpClientUtil.send(url+"/idps/manage/create",param));
		System.out.println("----------开通多台，耗时："+(System.currentTimeMillis()-s));
	}

}
