package com.ai.paas.ipaas.ses.dao.mapper.bo;

public class SesResourcePool {
    private Integer id;

    private String hostIp;

    private Integer status;

    private Integer portMin;

    private Integer portMax;

    private Integer memTotal;

    private Integer memUsed;

    private String dataPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp == null ? null : hostIp.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPortMin() {
        return portMin;
    }

    public void setPortMin(Integer portMin) {
        this.portMin = portMin;
    }

    public Integer getPortMax() {
        return portMax;
    }

    public void setPortMax(Integer portMax) {
        this.portMax = portMax;
    }

    public Integer getMemTotal() {
        return memTotal;
    }

    public void setMemTotal(Integer memTotal) {
        this.memTotal = memTotal;
    }

    public Integer getMemUsed() {
        return memUsed;
    }

    public void setMemUsed(Integer memUsed) {
        this.memUsed = memUsed;
    }

    public String getDataPath() {
        return dataPath;
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath == null ? null : dataPath.trim();
    }
}