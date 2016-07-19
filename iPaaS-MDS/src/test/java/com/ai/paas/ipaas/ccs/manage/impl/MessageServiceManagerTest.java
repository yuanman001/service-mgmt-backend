package com.ai.paas.ipaas.ccs.manage.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.mds.manage.rest.interfaces.IMessageServiceManager;
import com.ai.paas.ipaas.util.UUIDTool;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:dubbo/consumer/mds-client-consumer.xml" })
public class MessageServiceManagerTest {

	@Autowired
	private IMessageServiceManager msgSrvManager;
	private Gson gson = new Gson();

	@Test(expected = IllegalArgumentException.class)
	public void testCreateMessageSrvNullParam() throws Exception {
		String apply = null;
		String result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
	}

	@Test
	public void testCreateMessageSrvIllegalParam() throws Exception {
		String apply = null;
		apply = "";
		String result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'',applyType:'create',serviceId:'MDS001',topicEnName:'FFF49D0D518948D0AB28D7A8EEE25D03_MDS001_"
				+ UUIDTool.genShortId()
				+ "',topicName:'测试队列',topicPartitions:5,msgReplica:2,maxSender:5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'',serviceId:'MDS001',topicEnName:'FFF49D0D518948D0AB28D7A8EEE25D03_MDS001_"
				+ UUIDTool.genShortId()
				+ "',topicName:'测试队列',topicPartitions:5,msgReplica:2,maxSender:5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'create',serviceId:'',topicEnName:'FFF49D0D518948D0AB28D7A8EEE25D03_MDS001_"
				+ UUIDTool.genShortId()
				+ "',topicName:'测试队列',topicPartitions:5,msgReplica:2,maxSender:5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'create',serviceId:'MDS001',topicEnName:'',topicName:'测试队列',topicPartitions:5,msgReplica:2,maxSender:5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'create',serviceId:'MDS001',topicEnName:'1111',topicName:'',topicPartitions:5,msgReplica:2,maxSender:5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
		apply = "{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'create',serviceId:'MDS001',topicEnName:'1111',topicName:'ssss',topicPartitions:-5,msgReplica:-2,maxSender:-5}";
		result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "999999");
	}

	@Test
	public void testCreateMessageSrv() throws Exception {
		String apply = "{userId:'D9DB09D70BFC43CCB5D38A365CB763F1',applyType:'create',serviceId:'MDS102',topicEnName:'TEST_MDS101_1"
				+ "',topicName:'测试队列',topicPartitions:5,msgReplica:2,maxSender:5}";
		String result = msgSrvManager.create(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}
	
	//测试订阅功能
	@Test
	public void testCreateSubscribe() throws Exception {
		String apply = "{userId:'8E7ECAC706994DB9AC2BBB037C18762B',subscribeName:'yinzf789',topicEnName:'8E7ECAC706994DB9AC2BBB037C18762B_MDS002_218700542',userServIpaasId:'MDS002'}";
		String result = msgSrvManager.createSubscribe(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	//测试查询订阅
	@Test
	public void testGetSubscribe() throws Exception {
		String apply = "{subscribeName:'test123---',topicEnName:'8E7ECAC706994DB9AC2BBB037C18762B_MDS002_218700542'}";
		String result = msgSrvManager.getSubscribe(apply);
		System.out.println(result);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}
	
	//测试查询订阅
	@Test
	public void testGetListSubPath() throws Exception {
//		String apply = "{subscribeName:'test123---',topicEnName:'8E7ECAC706994DB9AC2BBB037C18762B_MDS002_218700542'}";
		String apply = "{userId:'8E7ECAC706994DB9AC2BBB037C18762B',applyType:'topicUsage',serviceId:'MDS003',topicEnName:'8E7ECAC706994DB9AC2BBB037C18762B_MDS003_416603763'}";
		String result = msgSrvManager.getListSubPath(apply);
		System.out.println(result);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}
	
	
	
	@Test
	public void testCancelMessageSrv() throws Exception {
		String apply = "{userId:'B9178FB878834E7BA8CD02FB981C7F4D',applyType:'cancel',serviceId:'MDS003'}";
		String result = msgSrvManager.cancel(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testCreateTopic() throws Exception {
		String apply = "{userId:'B9178FB878834E7BA8CD02FB981C7F4D',applyType:'create',serviceId:'MDS003',topicEnName:'TEST_MDS101_2"
				+ "',topicName:'测试队列123',topicPartitions:5,msgReplica:2,maxSender:5}";
		String result = msgSrvManager.createTopic(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testRemoveTopic() throws Exception {
		String apply = "{userId:'B9178FB878834E7BA8CD02FB981C7F4D',applyType:'remove',serviceId:'MDS003',topicEnName:'TEST_MDS101_2"
				+ "',topicName:'测试队列123',topicPartitions:5,msgReplica:2,maxSender:5}";
		String result = msgSrvManager.removeTopic(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testGetTopicUsage() throws Exception {
		String apply = "{userId:'605A3649A52548BA8B023CBA233C4696',applyType:'topicUsage',serviceId:'MDS022',topicEnName:'605A3649A52548BA8B023CBA233C4696_MDS022_1389228533"
				+ "'}";
		String result = msgSrvManager.getTopicUsage(apply);
		System.out.println(result);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testGetTopicMessage() throws Exception {
		String apply = "{userId:'953C42783ABB441A8568471B9FA19EA4',applyType:'getMessage',serviceId:'MDS001',topicEnName:'953C42783ABB441A8568471B9FA19EA4_MDS001_1279924387"
				+ "',partition:1,offset:100}";
		String result = msgSrvManager.getTopicMessage(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testAdjustTopicOffset() throws Exception {
		String apply = "{userId:'B9178FB878834E7BA8CD02FB981C7F4D',applyType:'adjustOffset',serviceId:'MDS006',topicEnName:'B9178FB878834E7BA8CD02FB981C7F4D_MDS006_1179234407"
				+ "',partition:1,offset:423}";
		String result = msgSrvManager.adjustTopicOffset(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}

	@Test
	public void testSendMessage() throws Exception {
		String apply = "{userId:'B9178FB878834E7BA8CD02FB981C7F4D',applyType:'send',serviceId:'MDS006',topicEnName:'B9178FB878834E7BA8CD02FB981C7F4D_MDS006_1179234407"
				+ "',partition:1,message:'This is a test'}";
		String result = msgSrvManager.sendMessage(apply);
		assertEquals(gson.fromJson(result, JsonObject.class).get("resultCode")
				.getAsString(), "000000");
	}
}
