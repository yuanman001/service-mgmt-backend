package com.ai.paas.ipaas.txs.manage.services;

import com.ai.paas.ipaas.txs.manage.rest.interfaces.ITansactionServiceManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Transaction Service Manager Test
 * Created by gaoht on 15/4/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:config-client-consumer.xml", "classpath:applicationContext-mybatis.xml"})
public class TSManagerTest {
    @Autowired
    private TransactionServiceManager manager;

    @Test
    public void testCreate() {
        String ret = manager.create("{userId:'FFF49D0D518948D0AB28D7A8EEE25D03',applyType:'create',serviceId:'TXS1'}");
        Assert.assertNotNull(ret);
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(ret, JsonObject.class);
        Assert.assertNotNull(json);
        Assert.assertEquals(ret, 0, json.get("resultCode").getAsInt());
    }
}
