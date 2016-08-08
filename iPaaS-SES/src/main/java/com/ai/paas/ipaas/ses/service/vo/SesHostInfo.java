/**
 * Project Name:iPaaS-SES
 * File Name:HostInfo.java
 * Package Name:com.ai.paas.ipaas.ses.service.vo
 * Date:2015年8月18日下午3:26:11
 * @author jianhua.ma
 */

package com.ai.paas.ipaas.ses.service.vo;

import java.io.Serializable;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;

;

public class SesHostInfo extends ApplyInfo implements Serializable {

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
	 * 搜索引擎数据根路径
	 */
	private String dataPath;

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

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

}
