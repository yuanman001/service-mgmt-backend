/*
	* @(#)IpaasStormTaskSpout.java	2015年03月30日
	*
	* Copyright (c) 2015, COMBAC and/or its affiliates. All rights reserved.
	* COMBANC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package com.ai.paas.ipaas.rcs.dto;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
/**
* 此类主要是用来处理 "无注释，请添加！" 表的实体类
* 
* @author weichuang
*/
public class IpaasStormTaskSpout implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * ID
	 */
	public static final String PRO_ID="id";
	/**
	 * task
	 */
	public static final String PRO_TASKID="taskId";
	/**
	 * spout名称
	 */
	public static final String PRO_SPOUTNAME="spoutName";
	/**
	 * spout类名
	 */
	public static final String PRO_SPOUTCLASSNAME="spoutClassName";
	/**
	 * 线程数量
	 */
	public static final String PRO_THREADS="threads";
	/**
	 * ID
	 */
	private Long id;
	/**
	 * task
	 */
	private Long taskId;
	/**
	 * spout名称
	 */
	private String spoutName;
	/**
	 * spout类名
	 */
	private String spoutClassName;
	/**
	 * 线程数量
	 */
	private Integer threads;
	//默认构造器
	public IpaasStormTaskSpout(){
	}
	//全属性构造器
	public IpaasStormTaskSpout(Long id,Long taskId,String spoutName,String spoutClassName,Integer threads){
		this.id = id;
		this.taskId = taskId;
		this.spoutName = spoutName;
		this.spoutClassName = spoutClassName;
		this.threads = threads;
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
	 * 获取(task)属性
	 */
	public Long getTaskId() {
		return this.taskId;
	}
	/**
	 * 设置(task)属性
	 */
	public void setTaskId(Long taskId) {
		this.taskId=taskId;
	}
	/**
	 * 获取(spout名称)属性
	 */
	public String getSpoutName() {
		return this.spoutName;
	}
	/**
	 * 设置(spout名称)属性
	 */
	public void setSpoutName(String spoutName) {
		this.spoutName=spoutName;
	}
	/**
	 * 获取(spout类名)属性
	 */
	public String getSpoutClassName() {
		return this.spoutClassName;
	}
	/**
	 * 设置(spout类名)属性
	 */
	public void setSpoutClassName(String spoutClassName) {
		this.spoutClassName=spoutClassName;
	}
	/**
	 * 获取(线程数量)属性
	 */
	public Integer getThreads() {
		return this.threads;
	}
	/**
	 * 设置(线程数量)属性
	 */
	public void setThreads(Integer threads) {
		this.threads=threads;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result+ ((this.taskId == null) ? 0 : this.taskId.hashCode());
		result = prime * result+ ((this.spoutName == null) ? 0 : this.spoutName.hashCode());
		result = prime * result+ ((this.spoutClassName == null) ? 0 : this.spoutClassName.hashCode());
		result = prime * result+ ((this.threads == null) ? 0 : this.threads.hashCode());
		return result;
		}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)return true;
		if (obj == null)
		return false;
		if (getClass() != obj.getClass())
		return false;
		IpaasStormTaskSpout other = (IpaasStormTaskSpout) obj;
	if (id == null) {
		if (other.id != null)
			return false;
		} else if (!id.equals(other.id))
			return false;
	if (taskId == null) {
		if (other.taskId != null)
			return false;
		} else if (!taskId.equals(other.taskId))
			return false;
	if (spoutName == null) {
		if (other.spoutName != null)
			return false;
		} else if (!spoutName.equals(other.spoutName))
			return false;
	if (spoutClassName == null) {
		if (other.spoutClassName != null)
			return false;
		} else if (!spoutClassName.equals(other.spoutClassName))
			return false;
	if (threads == null) {
		if (other.threads != null)
			return false;
		} else if (!threads.equals(other.threads))
			return false;
		return true;
	}
}