package com.ai.paas.ipaas.txs.dao.mapper.bo;

import java.sql.Timestamp;

public class TxsInst {
    private int txsInstId;

    private String userId;

    private String serviceId;

    private int txsResourceId;

    private String zkPath;

    private String zkNode;

    private int instState;

    private Timestamp instStateTime;

    private String remarks;

    public int getTxsInstId() {
        return txsInstId;
    }

    public void setTxsInstId(int txsInstId) {
        this.txsInstId = txsInstId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public int getTxsResourceId() {
        return txsResourceId;
    }

    public void setTxsResourceId(int txsResourceId) {
        this.txsResourceId = txsResourceId;
    }

    public String getZkPath() {
        return zkPath;
    }

    public void setZkPath(String zkPath) {
        this.zkPath = zkPath == null ? null : zkPath.trim();
    }

    public String getZkNode() {
        return zkNode;
    }

    public void setZkNode(String zkNode) {
        this.zkNode = zkNode == null ? null : zkNode.trim();
    }

    public int getInstState() {
        return instState;
    }

    public void setInstState(int instState) {
        this.instState = instState;
    }

    public Timestamp getInstStateTime() {
        return instStateTime;
    }

    public void setInstStateTime(Timestamp instStateTime) {
        this.instStateTime = instStateTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}