package com.ai.paas.ipaas.ats.dao.mapper.bo;

import java.sql.Timestamp;

public class AtsResourcePool {
    private int atsResourceId;

    private int txsResourceId;

    private String zkProducerPath;

    private String zkProducerNode;

    private String zkConsumerPath;

    private String zkConsumerNode;

    private String zkTopicPath;

    private String kafkaCreateCommand;

    private int resourceState;

    private Timestamp resourceTime;

    private String remarks;

    public int getAtsResourceId() {
        return atsResourceId;
    }

    public void setAtsResourceId(int atsResourceId) {
        this.atsResourceId = atsResourceId;
    }

    public int getTxsResourceId() {
        return txsResourceId;
    }

    public void setTxsResourceId(int txsResourceId) {
        this.txsResourceId = txsResourceId;
    }

    public String getZkProducerPath() {
        return zkProducerPath;
    }

    public void setZkProducerPath(String zkProducerPath) {
        this.zkProducerPath = zkProducerPath == null ? null : zkProducerPath.trim();
    }

    public String getZkProducerNode() {
        return zkProducerNode;
    }

    public void setZkProducerNode(String zkProducerNode) {
        this.zkProducerNode = zkProducerNode == null ? null : zkProducerNode.trim();
    }

    public String getZkConsumerPath() {
        return zkConsumerPath;
    }

    public void setZkConsumerPath(String zkConsumerPath) {
        this.zkConsumerPath = zkConsumerPath == null ? null : zkConsumerPath.trim();
    }

    public String getZkConsumerNode() {
        return zkConsumerNode;
    }

    public void setZkConsumerNode(String zkConsumerNode) {
        this.zkConsumerNode = zkConsumerNode == null ? null : zkConsumerNode.trim();
    }

    public String getZkTopicPath() {
        return zkTopicPath;
    }

    public void setZkTopicPath(String zkTopicPath) {
        this.zkTopicPath = zkTopicPath == null ? null : zkTopicPath.trim();
    }

    public String getKafkaCreateCommand() {
        return kafkaCreateCommand;
    }

    public void setKafkaCreateCommand(String kafkaCreateCommand) {
        this.kafkaCreateCommand = kafkaCreateCommand == null ? null : kafkaCreateCommand.trim();
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