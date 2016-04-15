package com.ai.paas.ipaas.txs.component;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.ccs.constants.ConfigCenterDubboConstants;
import com.ai.paas.ipaas.ccs.service.ICCSComponentManageSv;
import com.ai.paas.ipaas.ccs.service.dto.CCSComponentOperationParam;
import com.google.gson.JsonObject;
import kafka.admin.AdminUtils;
import kafka.admin.TopicCommand;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import static com.ai.paas.ipaas.txs.util.Constants.*;

/**
 * Service Util
 * <p/>
 * Created by gaoht on 15/4/28.
 */
@Component
public class TxsComponent {

    private static final Logger logger = LoggerFactory.getLogger(TxsComponent.class);

    @Autowired
    private ICCSComponentManageSv ccs;

    public String[] validateParam(JsonObject paramJson) {
        String applyType = paramJson.get(APPLY_TYPE).getAsString();
        Assert.isTrue(APPLY_TYPE_CREATE.equals(applyType), "applyType should be create ,but actual " + applyType);
        String userId = paramJson.get(USER_ID).getAsString();
        Assert.hasText(userId, "userId should not null ");
        String serviceId = paramJson.get(SERVICE_ID).getAsString();
        Assert.hasText(serviceId, "serviceId should not null ");
        return new String[]{userId, serviceId};
    }

    public void createNode(String userId, String serviceId, String zkPath, String zkData) throws PaasException {
        Assert.hasText(zkPath, "resource pool zk_path should not null");
        if (!zkPath.startsWith("/")) {
            zkPath = "/" + zkPath;
        }
        CCSComponentOperationParam configParam = new CCSComponentOperationParam();
        configParam.setUserId(userId);
        configParam.setPathType(ConfigCenterDubboConstants.PathType.READONLY);
        configParam.setPath("/TXS/" + serviceId + zkPath);
        if (ccs.exists(configParam)) {
            logger.debug("Exists zookeeper node ,config {} ", configParam);
        } else {
            ccs.add(configParam, zkData);
            logger.debug("Created zookeeper node ,config {} ", configParam);
        }
    }


    public void createTopic(String createCommand) throws PaasException {
        String[] opts = createCommand.split(" ");
        TopicCommand.TopicCommandOptions options = new TopicCommand.TopicCommandOptions(opts);
        options.checkArgs();
        String topic = options.options().valuesOf(options.topicOpt()).get(0);
        ZkClient zkClient = new ZkClient(options.options().valuesOf(options.zkConnectOpt()).get(0),
                Integer.MAX_VALUE, Integer.MAX_VALUE, new StringSerializer());
        if (AdminUtils.topicExists(zkClient, topic)) {
            logger.debug("Exists zookeeper node ,command: {} ", createCommand);
        } else {
            AdminUtils.createTopic(zkClient, topic, options.options().valuesOf(options.partitionsOpt()).get(0)
                    , options.options().valuesOf(options.replicationFactorOpt()).get(0), new java.util.Properties());
            logger.debug("Created TXS kafka topic ,command {} ", createCommand);
        }
    }
}
