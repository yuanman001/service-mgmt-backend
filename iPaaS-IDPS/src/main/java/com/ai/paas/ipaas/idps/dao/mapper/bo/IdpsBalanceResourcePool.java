package com.ai.paas.ipaas.idps.dao.mapper.bo;

public class IdpsBalanceResourcePool {
    private Integer id;

    private String idpsBalanceHostIp;

    private Integer status;

    private Integer idpsBalancePort;

    private Integer minPort;

    private Integer maxPort;

    private Integer cycle;

    private String sshUser;

    private String sshPassword;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdpsBalanceHostIp() {
        return idpsBalanceHostIp;
    }

    public void setIdpsBalanceHostIp(String idpsBalanceHostIp) {
        this.idpsBalanceHostIp = idpsBalanceHostIp == null ? null : idpsBalanceHostIp.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIdpsBalancePort() {
        return idpsBalancePort;
    }

    public void setIdpsBalancePort(Integer idpsBalancePort) {
        this.idpsBalancePort = idpsBalancePort;
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

    public String getSshUser() {
        return sshUser;
    }

    public void setSshUser(String sshUser) {
        this.sshUser = sshUser == null ? null : sshUser.trim();
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword == null ? null : sshPassword.trim();
    }
}