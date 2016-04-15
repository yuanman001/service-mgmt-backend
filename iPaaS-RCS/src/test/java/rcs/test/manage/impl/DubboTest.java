package rcs.test.manage.impl;


import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//import com.ai.paas.ipaas.rcs.dubbo.interfaces.IStormDemoDubboSV;
//import com.ai.paas.ipaas.rcs.dubbo.interfaces.IStormTaskDubboSV;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormDemoVo;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskBoltVo;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskInfoVo;
//import com.ai.paas.ipaas.rcs.dubbo.vo.StormTaskSpoutVo;
import com.ai.paas.ipaas.rcs.manage.rest.interfaces.IRCSManager;
import com.alibaba.dubbo.config.annotation.Reference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "/dubbo/consumer/rcs-consumer.xml" })
public class DubboTest 
{
//	@com.alibaba.dubbo.config.annotation.Reference
//	private IStormDemoDubboSV iStormDemoDubboSV;
	
//	@com.alibaba.dubbo.config.annotation.Reference
//	private IStormTaskDubboSV iStormTaskDubboSV;
	@Reference
	private IRCSManager iRCSManager;
	
	@org.junit.Test
	public void create() {
		String param ="{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"1\",\"prodCluster\":\"1\"}";
		System.out.println("----------@@@@@@@@@@@@@@@@@@@@@@@@@create-res:\n"+iRCSManager.create(param));

	}// prodCluster clusterType

	
//	@org.junit.Test
//	public void showList() {
//		String param ="{\"currentPage\":\"1\",\"PageSize\":\"10\",\"name\":\"test\"}";
//		System.out.println("----------@@@@@@@@@@@@@@@@@@@@@@@@@pageList-res:\n"+iRCSManager.PagingResult(param));
//
//	}
	
//	@org.junit.Test
//	public void registTask() {
//		String param ="{\"name\":\"test051801\",\"clusterId\":\"1\",\"numWorkers\":\"2\",\"comments\":\"测试DTTT\",\"jarfilepath\":\"/aifs01/devusers/devusr02/stormJars/201504/bbe296aa6a9a48fc8cd94b6300229619.jar\"}";
//		System.out.println("----------@@@@@@@@@@@@@@@@@@@@@@@@@pageList-res:\n"+iRCSManager.registerTask(param));
//
//	}
	/*
	
	@org.junit.Test
	public void getBestCacheResource() 
	{
		///111dddddddd
//		List<StormDemoVo> stormDemos = iStormDemoDubboSV.searchAll();
//		System.out.println("demo:" + stormDemos);
//		StormDemoVo stormDemoVo = new StormDemoVo();
//		stormDemoVo.setDemoName("aaaaaaaaaaaaa");
//		iStormDemoDubboSV.add(stormDemoVo);
//		//
//		iStormDemoDubboSV.del(3);
//		//
//		PageEntity pageEntity = new PageEntity();
//		pageEntity.setPage(1);
//		pageEntity.setSize(10);
//		PagingResult<StormDemoVo> pagingResult = iStormDemoDubboSV.searchPage(pageEntity);
//		System.out.println(pagingResult);
	}
	@org.junit.Test
	public void testStormTaskDubboSV() {
//		StormTaskInfoVo stormTaskInfoVo = new StormTaskInfoVo();
//		stormTaskInfoVo.setName("test");
//		stormTaskInfoVo.setClusterId(1L);
//		stormTaskInfoVo.setNumWorkers(11);
//		stormTaskInfoVo.setRegisterDt(new Date());
//		stormTaskInfoVo.setCancelDt(new Date());
//		stormTaskInfoVo.setStatus("0");
//		stormTaskInfoVo.setJarFilePath("D:\\stormJars\\201503\\47e2375d023140acb79ac0c0c9637692.jar");
//		stormTaskInfoVo.setComments("");
//		
//		List<StormTaskSpoutVo> stormTaskSpoutVos = new ArrayList<StormTaskSpoutVo>();
//		List<StormTaskBoltVo> stormTaskBoltVos = new ArrayList<StormTaskBoltVo>();
//		
//		stormTaskInfoVo.setStormTaskSpoutVos(stormTaskSpoutVos);
//		stormTaskInfoVo.setStormTaskBoltVos(stormTaskBoltVos);
//		iStormTaskDubboSV.registerTask(stormTaskInfoVo);
	}
	
	@org.junit.Test
	public void testStormTaskDubboSV2() 
	{
//		iStormDemoDubboSV.del(63);
	}
*/
}