package com.ai.paas.ipaas.rcs.dao.mapper.bo;

public class RcsSpoutInfo {
    private Long id;

    private Long taskId;

    private String spoutName;

    private String spoutClassName;

    private Integer threads;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getSpoutName() {
        return spoutName;
    }

    public void setSpoutName(String spoutName) {
        this.spoutName = spoutName == null ? null : spoutName.trim();
    }

    public String getSpoutClassName() {
        return spoutClassName;
    }

    public void setSpoutClassName(String spoutClassName) {
        this.spoutClassName = spoutClassName == null ? null : spoutClassName.trim();
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }
}