/**
 * Project Name:iPaaS-SES
 * File Name:HostInfo.java
 * Package Name:com.ai.paas.ipaas.ses.service.vo
 * Date:2015年8月18日下午3:26:11
 * @author jianhua.ma
 */

package com.ai.paas.ipaas.ses.service.vo;

import java.io.Serializable;

import com.ai.paas.ipaas.rest.vo.BaseInfo;

public class SesHostInfo extends BaseInfo implements Serializable {

	private static final long serialVersionUID = -2343553680948165860L;

	/**
	 * 主机ip地址
	 */
	private String ip;
	/**
	 * 主机端口
	 */
	private String port;
	/**
	 * transport.tcp.port
	 */
	private String tcpPort;
	/**
	 * http.port
	 */
	private String httpPort;
	/**
	 * 主机内存资源总量
	 */
	private int memTotal;
	/**
	 * 主机内存资源已使用量
	 */
	private int memUsed;

	/**
	 * 搜索引擎安装跟路径
	 */
	private String binPath;
	/**
	 * 用户数据存放路径
	 */
	private String userPath;
	/**
	 * agnet引擎执行端口
	 */
	private int agentPort;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(String tcpPort) {
		this.tcpPort = tcpPort;
	}

	public String getHttpPort() {
		return httpPort;
	}

	public void setHttpPort(String httpPort) {
		this.httpPort = httpPort;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * memTotal.
	 *
	 * @return the memTotal
	 * @since JDK 1.7
	 */
	public int getMemTotal() {
		return memTotal;
	}

	/**
	 * memTotal.
	 *
	 * @param memTotal
	 *            the memTotal to set
	 * @since JDK 1.7
	 */
	public void setMemTotal(int memTotal) {
		this.memTotal = memTotal;
	}

	/**
	 * memUsed.
	 *
	 * @return the memUsed
	 * @since JDK 1.7
	 */
	public int getMemUsed() {
		return memUsed;
	}

	/**
	 * memUsed.
	 *
	 * @param memUsed
	 *            the memUsed to set
	 * @since JDK 1.7
	 */
	public void setMemUsed(int memUsed) {
		this.memUsed = memUsed;
	}

	/**
	 * binPath.
	 *
	 * @return the binPath
	 * @since JDK 1.7
	 */
	public String getBinPath() {
		return binPath;
	}

	/**
	 * binPath.
	 *
	 * @param binPath
	 *            the binPath to set
	 * @since JDK 1.7
	 */
	public void setBinPath(String binPath) {
		this.binPath = binPath;
	}

	/**
	 * userPath.
	 *
	 * @return the userPath
	 * @since JDK 1.7
	 */
	public String getUserPath() {
		return userPath;
	}

	/**
	 * userPath.
	 *
	 * @param userPath
	 *            the userPath to set
	 * @since JDK 1.7
	 */
	public void setUserPath(String userPath) {
		this.userPath = userPath;
	}

	/**
	 * agentPort.
	 *
	 * @return the agentPort
	 * @since JDK 1.7
	 */
	public int getAgentPort() {
		return agentPort;
	}

	/**
	 * agentPort.
	 *
	 * @param agentPort
	 *            the agentPort to set
	 * @since JDK 1.7
	 */
	public void setAgentPort(int agentPort) {
		this.agentPort = agentPort;
	}

}
