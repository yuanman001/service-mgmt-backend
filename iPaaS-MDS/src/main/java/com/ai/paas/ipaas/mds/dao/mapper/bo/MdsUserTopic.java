package com.ai.paas.ipaas.mds.dao.mapper.bo;

import java.sql.Timestamp;

public class MdsUserTopic {
    private Integer topicInstId;

    private Integer srvInstId;

    private String topicDisplayName;

    private String topicEnName;

    private String userId;

    private String userSrvId;

    private Integer mdsClusterId;

    private String producerConfigPath;

    private String producerConfig;

    private String consumerConfigPath;

    private String consumerConfig;

    private Integer topicPartitions;

    private Integer msgReplicas;

    private String reamrk;

    private Integer state;

    private String operatorId;

    private Timestamp createdTime;

    private Timestamp modifiedTime;

    public Integer getTopicInstId() {
        return topicInstId;
    }

    public void setTopicInstId(Integer topicInstId) {
        this.topicInstId = topicInstId;
    }

    public Integer getSrvInstId() {
        return srvInstId;
    }

    public void setSrvInstId(Integer srvInstId) {
        this.srvInstId = srvInstId;
    }

    public String getTopicDisplayName() {
        return topicDisplayName;
    }

    public void setTopicDisplayName(String topicDisplayName) {
        this.topicDisplayName = topicDisplayName == null ? null : topicDisplayName.trim();
    }

    public String getTopicEnName() {
        return topicEnName;
    }

    public void setTopicEnName(String topicEnName) {
        this.topicEnName = topicEnName == null ? null : topicEnName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserSrvId() {
        return userSrvId;
    }

    public void setUserSrvId(String userSrvId) {
        this.userSrvId = userSrvId == null ? null : userSrvId.trim();
    }

    public Integer getMdsClusterId() {
        return mdsClusterId;
    }

    public void setMdsClusterId(Integer mdsClusterId) {
        this.mdsClusterId = mdsClusterId;
    }

    public String getProducerConfigPath() {
        return producerConfigPath;
    }

    public void setProducerConfigPath(String producerConfigPath) {
        this.producerConfigPath = producerConfigPath == null ? null : producerConfigPath.trim();
    }

    public String getProducerConfig() {
        return producerConfig;
    }

    public void setProducerConfig(String producerConfig) {
        this.producerConfig = producerConfig == null ? null : producerConfig.trim();
    }

    public String getConsumerConfigPath() {
        return consumerConfigPath;
    }

    public void setConsumerConfigPath(String consumerConfigPath) {
        this.consumerConfigPath = consumerConfigPath == null ? null : consumerConfigPath.trim();
    }

    public String getConsumerConfig() {
        return consumerConfig;
    }

    public void setConsumerConfig(String consumerConfig) {
        this.consumerConfig = consumerConfig == null ? null : consumerConfig.trim();
    }

    public Integer getTopicPartitions() {
        return topicPartitions;
    }

    public void setTopicPartitions(Integer topicPartitions) {
        this.topicPartitions = topicPartitions;
    }

    public Integer getMsgReplicas() {
        return msgReplicas;
    }

    public void setMsgReplicas(Integer msgReplicas) {
        this.msgReplicas = msgReplicas;
    }

    public String getReamrk() {
        return reamrk;
    }

    public void setReamrk(String reamrk) {
        this.reamrk = reamrk == null ? null : reamrk.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId == null ? null : operatorId.trim();
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Timestamp modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}