/*
	* @(#)IpaasStormTaskInfo.java	2015年03月25日
	*
	* Copyright (c) 2015, COMBAC and/or its affiliates. All rights reserved.
	* COMBANC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package com.ai.paas.ipaas.rcs.vo;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;
import java.util.List;
/**
* @author weichuang
*/
public class StormTaskInfoVo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 任务名称
	 */
	private String name;
	/**
	 * 集群ID
	 */
	private Long clusterId;
	/**
	 * 集群ID名称
	 */
	private String clusterName;
	/**
	 * 注册时间
	 */
	private Date registerDt;
	/**
	 * 注销时间
	 */
	private Date cancelDt;
	/**
	 * 状态
	 */
	private String status;
	/**
	 * 说明
	 */
	private String comments;
	/**
	 * work数量
	 */
	private Integer numWorkers;
	/**
	 * jar包文件
	 */
	private String jarfilepath;
	/**
	 * 注册用户
	 */
	private String registerUserId;
	
	private List<StormTaskSpoutVo> stormTaskSpoutVos;
	private List<StormTaskBoltVo> stormTaskBoltVos;
	//默认构造器
	public StormTaskInfoVo(){
	}
	/**
	 * 获取(ID)属性
	 */
	public Long getId() {
		return this.id;
	}
	/**
	 * 设置(ID)属性
	 */
	public void setId(Long id) {
		this.id=id;
	}
	/**
	 * 获取(任务名称)属性
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * 设置(任务名称)属性
	 */
	public void setName(String name) {
		this.name=name;
	}
	/**
	 * 获取(集群ID)属性
	 */
	public Long getClusterId() {
		return this.clusterId;
	}
	/**
	 * 设置(集群ID)属性
	 */
	public void setClusterId(Long clusterId) {
		this.clusterId=clusterId;
	}
	/**
	 * 获取(集群ID名称)属性
	 */
	public String getClusterName() {
		return this.clusterName;
	}
	/**
	 * 设置(集群ID名称)属性
	 */
	public void setClusterName(String clusterName) {
		this.clusterName=clusterName;
	}
	/**
	 * 获取(注册时间)属性
	 */
	public Date getRegisterDt() {
		return this.registerDt;
	}
	/**
	 * 设置(注册时间)属性
	 */
	public void setRegisterDt(Date registerDt) {
		this.registerDt=registerDt;
	}
	/**
	 * 获取(注销时间)属性
	 */
	public Date getCancelDt() {
		return this.cancelDt;
	}
	/**
	 * 设置(注销时间)属性
	 */
	public void setCancelDt(Date cancelDt) {
		this.cancelDt=cancelDt;
	}
	/**
	 * 获取(状态)属性
	 */
	public String getStatus() {
		return this.status;
	}
	/**
	 * 设置(状态)属性
	 */
	public void setStatus(String status) {
		this.status=status;
	}
	/**
	 * 获取(说明)属性
	 */
	public String getComments() {
		return this.comments;
	}
	/**
	 * 设置(说明)属性
	 */
	public void setComments(String comments) {
		this.comments=comments;
	}
	/**
	 * 获取(work数量)属性
	 */
	public Integer getNumWorkers() {
		return this.numWorkers;
	}
	/**
	 * 设置(work数量)属性
	 */
	public void setNumWorkers(Integer numWorkers) {
		this.numWorkers=numWorkers;
	}
	public List<StormTaskSpoutVo> getStormTaskSpoutVos() {
		return stormTaskSpoutVos;
	}
	public void setStormTaskSpoutVos(List<StormTaskSpoutVo> stormTaskSpoutVos) {
		this.stormTaskSpoutVos = stormTaskSpoutVos;
	}
	public List<StormTaskBoltVo> getStormTaskBoltVos() {
		return stormTaskBoltVos;
	}
	public void setStormTaskBoltVos(List<StormTaskBoltVo> stormTaskBoltVos) {
		this.stormTaskBoltVos = stormTaskBoltVos;
	}
	public String getjarfilepath() {
		return jarfilepath;
	}
	public void setjarfilepath(String jarfilepath) {
		this.jarfilepath = jarfilepath;
	}
	public String getRegisterUserId() {
		return registerUserId;
	}
	public void setRegisterUserId(String registerUserId) {
		this.registerUserId = registerUserId;
	}
	
}