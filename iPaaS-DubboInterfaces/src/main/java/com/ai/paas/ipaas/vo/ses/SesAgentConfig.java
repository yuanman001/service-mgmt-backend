package com.ai.paas.ipaas.vo.ses;

public class SesAgentConfig {
    private Integer id;

    private String agentCmd;

    private String agentFile;

    private String userPath;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getUserPath() {
        return userPath;
    }

    public void setUserPath(String userPath) {
        this.userPath = userPath == null ? null : userPath.trim();
    }
}