package com.ai.paas.ipaas.ats.dao.mapper.bo;

import java.sql.Timestamp;

public class AtsInst {
    private int atsInstId;

    private int atsResourceId;

    private String userId;

    private String serviceId;

    private String zkProducerPath;

    private String zkProducerNode;

    private String zkConsumerPath;

    private String zkConsumerNode;

    private String zkTopicPath;

    private String kafkaCreateCommand;

    private int instState;

    private Timestamp instTime;

    private String remarks;

    public int getAtsInstId() {
        return atsInstId;
    }

    public void setAtsInstId(int atsInstId) {
        this.atsInstId = atsInstId;
    }

    public int getAtsResourceId() {
        return atsResourceId;
    }

    public void setAtsResourceId(int atsResourceId) {
        this.atsResourceId = atsResourceId;
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

    public int getInstState() {
        return instState;
    }

    public void setInstState(int instState) {
        this.instState = instState;
    }

    public Timestamp getInstTime() {
        return instTime;
    }

    public void setInstTime(Timestamp instTime) {
        this.instTime = instTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }
}