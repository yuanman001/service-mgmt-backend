package com.ai.paas.ipaas.txs.manage.services;

import com.ai.paas.ipaas.txs.component.TxsComponent;
import com.ai.paas.ipaas.txs.dao.interfaces.TxsInstMapper;
import com.ai.paas.ipaas.txs.dao.interfaces.TxsResourcePoolMapper;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInst;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsInstCriteria;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsResourcePool;
import com.ai.paas.ipaas.txs.dao.mapper.bo.TxsResourcePoolCriteria;
import com.ai.paas.ipaas.txs.manage.rest.interfaces.ITansactionServiceManager;
import com.ai.paas.ipaas.txs.util.PoolSelect;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.beanutils.BeanUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.ai.paas.ipaas.txs.util.Constants.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class TransactionServiceManager implements ITansactionServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceManager.class);

    @Autowired
    private SqlSessionTemplate template;


    @Autowired
    private TxsComponent txsComponent;

    private PoolSelect<TxsResourcePool> poolSelect = new PoolSelect<>();

    @Override
    public String cancel(String param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String create(String param) {
        Gson gson = new Gson();
        JsonObject ret = new JsonObject();
        String code = SUCCESS_CODE;
        String message = "";
        try {
            logger.info("TXS create param:{}", param);
            JsonObject paramJson = gson.fromJson(param, JsonObject.class);
            Assert.notNull(paramJson, "Param :" + param + " is malformed");
            create(paramJson);
        } catch (RuntimeException e) {
            logger.info("TXS runtime exception param:{}", param, e);
            code = ERROR_CODE;
            message = e.getMessage();
        } catch (Throwable a) {
            logger.error("TXS exception create param:{}", param, a);
            code = ERROR_CODE;
            message = a.getMessage();
        }
        ret.addProperty(RESULT_CODE, code);
        ret.addProperty(RESULT_MSG, message);
        return gson.toJson(ret);
    }

    private void create(JsonObject paramJson) throws Throwable {
        String[] identify = txsComponent.validateParam(paramJson);
        String userId = identify[0];
        String serviceId = identify[1];

        TxsResourcePoolMapper mapper = template.getMapper(TxsResourcePoolMapper.class);
        TxsResourcePoolCriteria tpc = new TxsResourcePoolCriteria();
        tpc.createCriteria().andResourceStateEqualTo(0);
        TxsResourcePool resource = poolSelect.next(mapper.selectByExample(tpc));
        if (resource == null) {
            throw new RuntimeException("can't fetch txs_resource_pool");
        }

        txsComponent.createNode(userId, serviceId, resource.getZkPath(), resource.getZkNode());

        TxsInstMapper instMapper = template.getMapper(TxsInstMapper.class);
        TxsInstCriteria txsInstCriteria = new TxsInstCriteria();
        txsInstCriteria.createCriteria().andUserIdEqualTo(userId).andInstStateEqualTo(0);
        List<TxsInst> txsInstList = instMapper.selectByExample(txsInstCriteria);
        if (txsInstList == null || txsInstList.size() < 1) {
            TxsInst txsInst = new TxsInst();
            BeanUtils.copyProperties(txsInst, resource);
            txsInst.setInstState(0);
            txsInst.setUserId(userId);
            txsInst.setServiceId(serviceId);
            instMapper.insertSelective(txsInst);
            logger.debug("Created txs inst,resource_id: {} ", resource.getTxsResourceId());
        } else {
            if (!serviceId.equals(txsInstList.get(0).getServiceId())) {
                throw new RuntimeException("User(userId:" + userId + ") has txs service(serviceId:" + txsInstList.get(0).getServiceId()
                        + "). You can't create another txs service with serviceId:" + serviceId);
            }
            logger.debug("Exists txs inst,resource_id: {} ", resource.getTxsResourceId());
        }
    }

    @Override
    public String getFuncList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String modify(String param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String restart(String param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String start(String param) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String stop(String param) {
        // TODO Auto-generated method stub
        return null;
    }
}
