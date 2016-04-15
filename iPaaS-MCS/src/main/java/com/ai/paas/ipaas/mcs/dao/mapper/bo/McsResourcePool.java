package com.ai.paas.ipaas.mcs.dao.mapper.bo;

public class McsResourcePool {
    private Integer id;

    private String cacheHostIp;

    private Integer cacheMemory;

    private Integer cacheMemoryUsed;

    private Integer status;

    private Integer cachePort;

    private Integer minPort;

    private Integer maxPort;

    private Integer cycle;

    private String agentCmd;

    private String agentFile;

    private String cachePath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCacheHostIp() {
        return cacheHostIp;
    }

    public void setCacheHostIp(String cacheHostIp) {
        this.cacheHostIp = cacheHostIp == null ? null : cacheHostIp.trim();
    }

    public Integer getCacheMemory() {
        return cacheMemory;
    }

    public void setCacheMemory(Integer cacheMemory) {
        this.cacheMemory = cacheMemory;
    }

    public Integer getCacheMemoryUsed() {
        return cacheMemoryUsed;
    }

    public void setCacheMemoryUsed(Integer cacheMemoryUsed) {
        this.cacheMemoryUsed = cacheMemoryUsed;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCachePort() {
        return cachePort;
    }

    public void setCachePort(Integer cachePort) {
        this.cachePort = cachePort;
    }

    public Integer getMinPort() {
        return minPort;
    }

    public void setMinPort(Integer minPort) {
        this.minPort = minPort;
    }

    public Integer getMaxPort() {
        return maxPort;
    }

    public void setMaxPort(Integer maxPort) {
        this.maxPort = maxPort;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getAgentCmd() {
        return agentCmd;
    }

    public void setAgentCmd(String agentCmd) {
        this.agentCmd = agentCmd == null ? null : agentCmd.trim();
    }

    public String getAgentFile() {
        return agentFile;
    }

    public void setAgentFile(String agentFile) {
        this.agentFile = agentFile == null ? null : agentFile.trim();
    }

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath == null ? null : cachePath.trim();
    }
}