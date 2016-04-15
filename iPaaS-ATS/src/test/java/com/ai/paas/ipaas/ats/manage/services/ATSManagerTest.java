package com.ai.paas.ipaas.ats.manage.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.esotericsoftware.minlog.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * ATSManagerTest
 * Created by gaoht on 15/4/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config-client-consumer.xml", "classpath:applicationContext-mybatis.xml"})
public class ATSManagerTest {

    @Autowired
    private AsynchronousTransactionServiceManager manager;

    @Test
    public void testCreate() {
    	//workflow_service@asiainfo.com
        String ret = manager.create("{userId:'D60E7D1549384A6AB0C4419EA33920CE',applyType:'create',serviceId:'TXS001'}");
        Assert.assertNotNull(ret);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, "000000", json.get("resultCode").getAsString());
    }
    
    @Test
    public void testGetTopicUsage() {
        String ret = manager.getTopicUsage("{messageType:1,userId:'D60E7D1549384A6AB0C4419EA33920CE',applyType:'create',serviceId:'TXS001', topicEnName:'signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f'}");
        Assert.assertNotNull(ret);
        Log.debug("==========" + ret);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, "000000", json.get("resultCode").getAsString());
    }
    
    @Test
    public void getTopicMessage() {
        String ret = manager.getTopicMessage("{messageType:1,userId:'6590776422AD45FA89BB6D39B054E3AD',applyType:'getMessage',serviceId:'TXS001', topicEnName:'signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f',partition:'1',offset:'2'}");
        Assert.assertNotNull(ret);
        Gson gson = new Gson();
        Log.debug("==========" + ret);
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, "000000", json.get("resultCode").getAsString());
    }
    
    @Test
    public void resendTest() {
        String ret = manager.resend("{messageType:1,userId:'6590776422AD45FA89BB6D39B054E3AD',message:'{\"target\":\"signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f\",\"targetClassName\":\"com.ai.paas.ipaas.transaction.dtm.local.message.IDoMessage\",\"targetMethodName\":\"doThing\",\"channelId\":-1974073690715950517,\"mehodArgs\":[{\"arg\":\"\\\"b2\\\"\",\"type\":\"java.lang.String\"}],\"confirmProtocol\":{\"TXID\":\"DTL-BD163480B29548FA8F840271A254FF18\",\"appSignature\":\"APP-4163B3421C5A4A26B04192635D15A8EA\",\"transactionSignature\":\"tx=com.ai.paas.ipaas.ats.producer.TransactionMessageProducer@aa9502d.1\",\"dtStatus\":\"COMMITTED\",\"confirmTime\":\"20151029145229280\",\"paasUserId\":\"7A74B95A05FB4134ADEBC7493CA13761\"}}',serviceId:'TXS001', topicEnName:'signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f',partition:'1'}");
        Assert.assertNotNull(ret);
        Gson gson = new Gson();
        Log.debug("==========" + ret);
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, "000000", json.get("resultCode").getAsString());
    }
    
    @Test
    public void skipTest() {
        String ret = manager.skip("{messageType:'1',userId:'6590776422AD45FA89BB6D39B054E3AD',applyType:'skip',message:'ceshi', topicEnName:'signatureId-16cad52b-1be9-4b77-8366-be2b6e02db9f',partition:'1',offset:'1',serviceId:'TXS001'}");
        Assert.assertNotNull(ret);
        Gson gson = new Gson();
        Log.debug("==========" + ret);
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, "000000", json.get("resultCode").getAsString());
    }
}
