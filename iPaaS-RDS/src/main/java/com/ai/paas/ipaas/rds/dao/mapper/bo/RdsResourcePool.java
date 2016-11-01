package com.ai.paas.ipaas.rds.dao.mapper.bo;

import java.sql.Timestamp;

public class RdsResourcePool {
    private Integer resourceid;

    private String hostip;

    private Integer maxport;

    private Integer minport;

    private Integer currentport;

    private Integer cycle;

    private String sshpassword;

    private String sshuser;

    private Integer status;

    private Integer totalmemory;

    private Integer usedmemory;

    private String volumnPath;

    private Integer totIntStorage;

    private Integer usedIntStorage;

    private String cpu;

    private Integer netBandwidth;

    private Integer usedNetBandwidth;

    private Integer orgId;

    private Integer sshPort;

    private Timestamp instancecreatetime;

    private Timestamp instancelastupdatetime;

    
    
    public RdsResourcePool(Integer resourceid, String hostip, Integer maxport, Integer minport, Integer currentport,
			Integer cycle, String sshpassword, String sshuser, Integer status, Integer totalmemory, Integer usedmemory,
			String volumnPath, Integer totIntStorage, Integer usedIntStorage, String cpu, Integer netBandwidth,
			Integer usedNetBandwidth, Integer orgId, Integer sshPort, Timestamp instancecreatetime,
			Timestamp instancelastupdatetime) {
		super();
		this.resourceid = resourceid;
		this.hostip = hostip;
		this.maxport = maxport;
		this.minport = minport;
		this.currentport = currentport;
		this.cycle = cycle;
		this.sshpassword = sshpassword;
		this.sshuser = sshuser;
		this.status = status;
		this.totalmemory = totalmemory;
		this.usedmemory = usedmemory;
		this.volumnPath = volumnPath;
		this.totIntStorage = totIntStorage;
		this.usedIntStorage = usedIntStorage;
		this.cpu = cpu;
		this.netBandwidth = netBandwidth;
		this.usedNetBandwidth = usedNetBandwidth;
		this.orgId = orgId;
		this.sshPort = sshPort;
		this.instancecreatetime = instancecreatetime;
		this.instancelastupdatetime = instancelastupdatetime;
	}

	public RdsResourcePool() {
		super();
	}

	public RdsResourcePool(String hostip, Integer maxport, Integer minport, Integer currentport, Integer cycle,
			String sshpassword, String sshuser, Integer status, Integer totalmemory, Integer usedmemory,
			String volumnPath, Integer totIntStorage, Integer usedIntStorage, String cpu, Integer netBandwidth,
			Integer usedNetBandwidth, Integer orgId, Integer sshPort) {
		super();
		this.hostip = hostip;
		this.maxport = maxport;
		this.minport = minport;
		this.currentport = currentport;
		this.cycle = cycle;
		this.sshpassword = sshpassword;
		this.sshuser = sshuser;
		this.status = status;
		this.totalmemory = totalmemory;
		this.usedmemory = usedmemory;
		this.volumnPath = volumnPath;
		this.totIntStorage = totIntStorage;
		this.usedIntStorage = usedIntStorage;
		this.cpu = cpu;
		this.netBandwidth = netBandwidth;
		this.usedNetBandwidth = usedNetBandwidth;
		this.orgId = orgId;
		this.sshPort = sshPort;
	}

	public Integer getResourceid() {
        return resourceid;
    }

    public void setResourceid(Integer resourceid) {
        this.resourceid = resourceid;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip == null ? null : hostip.trim();
    }

    public Integer getMaxport() {
        return maxport;
    }

    public void setMaxport(Integer maxport) {
        this.maxport = maxport;
    }

    public Integer getMinport() {
        return minport;
    }

    public void setMinport(Integer minport) {
        this.minport = minport;
    }

    public Integer getCurrentport() {
        return currentport;
    }

    public void setCurrentport(Integer currentport) {
        this.currentport = currentport;
    }

    public Integer getCycle() {
        return cycle;
    }

    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    public String getSshpassword() {
        return sshpassword;
    }

    public void setSshpassword(String sshpassword) {
        this.sshpassword = sshpassword == null ? null : sshpassword.trim();
    }

    public String getSshuser() {
        return sshuser;
    }

    public void setSshuser(String sshuser) {
        this.sshuser = sshuser == null ? null : sshuser.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTotalmemory() {
        return totalmemory;
    }

    public void setTotalmemory(Integer totalmemory) {
        this.totalmemory = totalmemory;
    }

    public Integer getUsedmemory() {
        return usedmemory;
    }

    public void setUsedmemory(Integer usedmemory) {
        this.usedmemory = usedmemory;
    }

    public String getVolumnPath() {
        return volumnPath;
    }

    public void setVolumnPath(String volumnPath) {
        this.volumnPath = volumnPath == null ? null : volumnPath.trim();
    }

    public Integer getTotIntStorage() {
        return totIntStorage;
    }

    public void setTotIntStorage(Integer totIntStorage) {
        this.totIntStorage = totIntStorage;
    }

    public Integer getUsedIntStorage() {
        return usedIntStorage;
    }

    public void setUsedIntStorage(Integer usedIntStorage) {
        this.usedIntStorage = usedIntStorage;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu == null ? null : cpu.trim();
    }

    public Integer getNetBandwidth() {
        return netBandwidth;
    }

    public void setNetBandwidth(Integer netBandwidth) {
        this.netBandwidth = netBandwidth;
    }

    public Integer getUsedNetBandwidth() {
        return usedNetBandwidth;
    }

    public void setUsedNetBandwidth(Integer usedNetBandwidth) {
        this.usedNetBandwidth = usedNetBandwidth;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public Integer getSshPort() {
        return sshPort;
    }

    public void setSshPort(Integer sshPort) {
        this.sshPort = sshPort;
    }

    public Timestamp getInstancecreatetime() {
        return instancecreatetime;
    }

    public void setInstancecreatetime(Timestamp instancecreatetime) {
        this.instancecreatetime = instancecreatetime;
    }

    public Timestamp getInstancelastupdatetime() {
        return instancelastupdatetime;
    }

    public void setInstancelastupdatetime(Timestamp instancelastupdatetime) {
        this.instancelastupdatetime = instancelastupdatetime;
    }
}