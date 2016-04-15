package com.ai.paas.ipaas.rcs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.Date;

public class RcsTaskInfo {
    private Long id;

    private String name;

    private Long clusterId;

    private Date registerDt;

    private Date cancelDt;

    private String status;

    private String comments;

    private Integer numWorkers;

	private String jarfilepath;

    private String registerUserId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Date getRegisterDt() {
        return registerDt;
    }

    public void setRegisterDt(Date registerDt) {
        this.registerDt = registerDt;
    }

    public Date getCancelDt() {
        return cancelDt;
    }

    public void setCancelDt(Date cancelDt) {
        this.cancelDt = cancelDt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments == null ? null : comments.trim();
    }

    public Integer getNumWorkers() {
        return numWorkers;
    }

    public void setNumWorkers(Integer numWorkers) {
        this.numWorkers = numWorkers;
    }

    public String getjarfilepath() {
        return jarfilepath;
    }

    public void setjarfilepath(String jarfilepath) {
        this.jarfilepath = jarfilepath == null ? null : jarfilepath.trim();
    }

    public String getRegisterUserId() {
        return registerUserId;
    }

    public void setRegisterUserId(String registerUserId) {
        this.registerUserId = registerUserId == null ? null : registerUserId.trim();
    }
}