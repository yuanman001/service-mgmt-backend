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

    private Timestamp instancecreatetime;

    private Timestamp instancelastupdatetime;

    
    public RdsResourcePool() {
		super();
	}

	public RdsResourcePool(Integer resourceid, String hostip, Integer maxport, Integer minport, Integer currentport,
			Integer cycle, String sshpassword, String sshuser, Integer status, Integer totalmemory, Integer usedmemory,
			String volumnPath, Timestamp instancecreatetime, Timestamp instancelastupdatetime) {
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
		this.instancecreatetime = instancecreatetime;
		this.instancelastupdatetime = instancelastupdatetime;
	}

	public RdsResourcePool(String hostip, Integer maxport, Integer minport, Integer currentport, Integer cycle,
			String sshpassword, String sshuser, Integer status, Integer totalmemory, Integer usedmemory,
			String volumnPath, Timestamp instancecreatetime, Timestamp instancelastupdatetime) {
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
		this.instancecreatetime = instancecreatetime;
		this.instancelastupdatetime = instancelastupdatetime;
	}

	public RdsResourcePool(String hostip, Integer maxport, Integer minport, Integer currentport, Integer cycle,
			String sshpassword, String sshuser, Integer status, Integer totalmemory, Integer usedmemory,
			String volumnPath) {
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