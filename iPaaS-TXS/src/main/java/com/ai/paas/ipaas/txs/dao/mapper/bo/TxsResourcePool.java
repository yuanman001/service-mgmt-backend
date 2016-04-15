package com.ai.paas.ipaas.txs.dao.mapper.bo;

import java.sql.Timestamp;

public class TxsResourcePool {
    private int txsResourceId;

    private String zkPath;

    private String zkNode;

    private int resourceState;

    private Timestamp resourceTime;

    private String remarks;

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

    public int getResourceState() {
        return resourceState;
    }

    public void setResourceState(int resourceState) {
        this.resourceState = resourceState;
    }

    public Timestamp getResourceTime() {
        return resourceTime;
    }

    public void setResourceTime(Timestamp resourceTime) {
        this.resourceTime = resourceTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}