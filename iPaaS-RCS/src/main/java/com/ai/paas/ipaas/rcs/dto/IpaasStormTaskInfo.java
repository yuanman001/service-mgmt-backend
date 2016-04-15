/*
	* @(#)IpaasStormTaskInfo.java	2015年03月30日
	*
	* Copyright (c) 2015, COMBAC and/or its affiliates. All rights reserved.
	* COMBANC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package com.ai.paas.ipaas.rcs.dto;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;
/**
* 此类主要是用来处理 "无注释，请添加！" 表的实体类
* 
* @author weichuang
*/
public class IpaasStormTaskInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	public static final String PRO_ID="id";
	/**
	 * 任务名称
	 */
	public static final String PRO_NAME="name";
	/**
	 * 集群ID
	 */
	public static final String PRO_CLUSTERID="clusterId";
	/**
	 * 注册时间
	 */
	public static final String PRO_REGISTERDT="registerDt";
	/**
	 * 注销时间
	 */
	public static final String PRO_CANCELDT="cancelDt";
	/**
	 * 状态
	 */
	public static final String PRO_STATUS="status";
	/**
	 * 说明
	 */
	public static final String PRO_COMMENTS="comments";
	/**
	 * work数量
	 */
	public static final String PRO_NUMWORKERS="numWorkers";
	/**
	 * jar包文件
	 */
	public static final String PRO_JARFILEPATH="jarFilePath";
	/**
	 * 注册用户
	 */
	public static final String PRO_REGISTERUSERID="registerUserId";
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
	private String jarFilePath;
	/**
	 * 注册用户
	 */
	private String registerUserId;
	//默认构造器
	public IpaasStormTaskInfo(){
	}
	//全属性构造器
	public IpaasStormTaskInfo(Long id,String name,Long clusterId,Date registerDt,Date cancelDt,String status,String comments,Integer numWorkers,String jarFilePath){
		this.id = id;
		this.name = name;
		this.clusterId = clusterId;
		this.registerDt = registerDt;
		this.cancelDt = cancelDt;
		this.status = status;
		this.comments = comments;
		this.numWorkers = numWorkers;
		this.jarFilePath = jarFilePath;
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
	/**
	 * 获取(jar包文件)属性
	 */
	public String getJarFilePath() {
		return this.jarFilePath;
	}
	/**
	 * 设置(jar包文件)属性
	 */
	public void setJarFilePath(String jarFilePath) {
		this.jarFilePath=jarFilePath;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result+ ((this.name == null) ? 0 : this.name.hashCode());
		result = prime * result+ ((this.clusterId == null) ? 0 : this.clusterId.hashCode());
		result = prime * result+ ((this.registerDt == null) ? 0 : this.registerDt.hashCode());
		result = prime * result+ ((this.cancelDt == null) ? 0 : this.cancelDt.hashCode());
		result = prime * result+ ((this.status == null) ? 0 : this.status.hashCode());
		result = prime * result+ ((this.comments == null) ? 0 : this.comments.hashCode());
		result = prime * result+ ((this.numWorkers == null) ? 0 : this.numWorkers.hashCode());
		result = prime * result+ ((this.jarFilePath == null) ? 0 : this.jarFilePath.hashCode());
		return result;
		}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)return true;
		if (obj == null)
		return false;
		if (getClass() != obj.getClass())
		return false;
		IpaasStormTaskInfo other = (IpaasStormTaskInfo) obj;
	if (id == null) {
		if (other.id != null)
			return false;
		} else if (!id.equals(other.id))
			return false;
	if (name == null) {
		if (other.name != null)
			return false;
		} else if (!name.equals(other.name))
			return false;
	if (clusterId == null) {
		if (other.clusterId != null)
			return false;
		} else if (!clusterId.equals(other.clusterId))
			return false;
	if (registerDt == null) {
		if (other.registerDt != null)
			return false;
		} else if (!registerDt.equals(other.registerDt))
			return false;
	if (cancelDt == null) {
		if (other.cancelDt != null)
			return false;
		} else if (!cancelDt.equals(other.cancelDt))
			return false;
	if (status == null) {
		if (other.status != null)
			return false;
		} else if (!status.equals(other.status))
			return false;
	if (comments == null) {
		if (other.comments != null)
			return false;
		} else if (!comments.equals(other.comments))
			return false;
	if (numWorkers == null) {
		if (other.numWorkers != null)
			return false;
		} else if (!numWorkers.equals(other.numWorkers))
			return false;
	if (jarFilePath == null) {
		if (other.jarFilePath != null)
			return false;
		} else if (!jarFilePath.equals(other.jarFilePath))
			return false;
		return true;
	}
	public String getRegisterUserId() {
		return registerUserId;
	}
	public void setRegisterUserId(String registerUserId) {
		this.registerUserId = registerUserId;
	}
	
}