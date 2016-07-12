/**
 * Project Name:iPaaS-SES
 * File Name:SesSrvApply.java
 * Package Name:com.ai.paas.ipaas.ses.service.vo
 * Date:2015年8月18日下午3:37:49
 * @author jianhua.ma
 */

package com.ai.paas.ipaas.ses.service.vo;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;


public class SesSrvApply extends ApplyInfo{

	private static final long serialVersionUID = -7276753137909397160L;
	/**
	 * 用户申请搜索服务集群节点的个数
	 */
	private Integer clusterNum;
	/**
	 * 搜索服务索引分片数量
	 */
	private Integer shardNum;
	/**
	 * 搜索服务单片申请内存
	 */
	private Integer sesMem;
	/**
	 * 搜索服务复制个数，理论上越大越靠谱
	 */
	private Integer replicasNum;
	/**
	 * 申请搜索服务的服务名称
	 */
	private String serviceName;
	
	public Integer getClusterNum() {
		return clusterNum;
	}
	public void setClusterNum(Integer clusterNum) {
		this.clusterNum = clusterNum;
	}
	public Integer getShardNum() {
		return shardNum;
	}
	public void setShardNum(Integer shardNum) {
		this.shardNum = shardNum;
	}
	public Integer getReplicasNum() {
		return replicasNum;
	}
	public void setReplicasNum(Integer replicasNum) {
		this.replicasNum = replicasNum;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	/**
	 * sesMem.
	 *
	 * @return  the sesMem
	 * @since   JDK 1.7
	 */
	public Integer getSesMem() {
		return sesMem;
	}
	/**
	 * sesMem.
	 *
	 * @param   sesMem    the sesMem to set
	 * @since   JDK 1.7
	 */
	public void setSesMem(Integer sesMem) {
		this.sesMem = sesMem;
	}

}

