package com.ai.paas.ipaas.rcs.dao.mapper.bo;

public class IpaasStormTaskBolt {
    private long id;

    private long taskId;

    private String boltName;

    private String boltClassName;

    private int threads;

    private String groupingTypes;

    private String groupingSpoutOrBlots;

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

    public String getBoltName() {
        return boltName;
    }

    public void setBoltName(String boltName) {
        this.boltName = boltName == null ? null : boltName.trim();
    }

    public String getBoltClassName() {
        return boltClassName;
    }

    public void setBoltClassName(String boltClassName) {
        this.boltClassName = boltClassName == null ? null : boltClassName.trim();
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public String getGroupingTypes() {
        return groupingTypes;
    }

    public void setGroupingTypes(String groupingTypes) {
        this.groupingTypes = groupingTypes == null ? null : groupingTypes.trim();
    }

    public String getGroupingSpoutOrBlots() {
        return groupingSpoutOrBlots;
    }

    public void setGroupingSpoutOrBlots(String groupingSpoutOrBlots) {
        this.groupingSpoutOrBlots = groupingSpoutOrBlots == null ? null : groupingSpoutOrBlots.trim();
    }
}