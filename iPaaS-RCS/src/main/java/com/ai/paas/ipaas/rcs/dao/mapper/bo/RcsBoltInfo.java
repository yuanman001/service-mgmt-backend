package com.ai.paas.ipaas.rcs.dao.mapper.bo;

public class RcsBoltInfo {
    private Long id;

    private Long taskId;

    private String boltName;

    private String boltClassName;

    private Integer threads;

    private String groupingTypes;

    private String groupingSpoutOrBlots;

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

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
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