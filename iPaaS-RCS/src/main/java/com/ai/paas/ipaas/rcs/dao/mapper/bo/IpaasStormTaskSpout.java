package com.ai.paas.ipaas.rcs.dao.mapper.bo;

public class IpaasStormTaskSpout {
    private long id;

    private long taskId;

    private String spoutName;

    private String spoutClassName;

    private int threads;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
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

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}