/*
	* @(#)IpaasStormTaskBolt.java	2015年03月25日
	*
	* Copyright (c) 2015, COMBAC and/or its affiliates. All rights reserved.
	* COMBANC PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
*/
package com.ai.paas.ipaas.rcs.vo;

import java.lang.Long;
import java.lang.String;
import java.lang.Integer;
/**
* 此类主要是用来处理 "无注释，请添加！" 表的实体类
* 
* @author weichuang
*/
public class StormTaskBoltVo implements java.io.Serializable{
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
	 * bolt名称
	 */
	public static final String PRO_BOLTNAME="boltName";
	/**
	 * bolt类名
	 */
	public static final String PRO_BOLTCLASSNAME="boltClassName";
	/**
	 * 线程数量
	 */
	public static final String PRO_THREADS="threads";
	/**
	 * grouping类型，多个，逗号分隔
	 */
	public static final String PRO_GROUPINGTYPES="groupingTypes";
	/**
	 * grouping源头，多个，逗号分隔
	 */
	public static final String PRO_GROUPINGSPOUTORBLOTS="groupingSpoutOrBlots";
	/**
	 * ID
	 */
	private Long id;
	/**
	 * task
	 */
	private Long taskId;
	/**
	 * bolt名称
	 */
	private String boltName;
	/**
	 * bolt类名
	 */
	private String boltClassName;
	/**
	 * 线程数量
	 */
	private Integer threads;
	/**
	 * grouping类型，多个，逗号分隔
	 */
	private String groupingTypes;
	/**
	 * grouping源头，多个，逗号分隔
	 */
	private String groupingSpoutOrBlots;
	//默认构造器
	public StormTaskBoltVo(){
	}
	//全属性构造器
	public StormTaskBoltVo(Long id,Long taskId,String boltName,String boltClassName,Integer threads,String groupingTypes,String groupingSpoutOrBlots){
		this.id = id;
		this.taskId = taskId;
		this.boltName = boltName;
		this.boltClassName = boltClassName;
		this.threads = threads;
		this.groupingTypes = groupingTypes;
		this.groupingSpoutOrBlots = groupingSpoutOrBlots;
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
	 * 获取(bolt名称)属性
	 */
	public String getBoltName() {
		return this.boltName;
	}
	/**
	 * 设置(bolt名称)属性
	 */
	public void setBoltName(String boltName) {
		this.boltName=boltName;
	}
	/**
	 * 获取(bolt类名)属性
	 */
	public String getBoltClassName() {
		return this.boltClassName;
	}
	/**
	 * 设置(bolt类名)属性
	 */
	public void setBoltClassName(String boltClassName) {
		this.boltClassName=boltClassName;
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
	/**
	 * 获取(grouping类型，多个，逗号分隔)属性
	 */
	public String getGroupingTypes() {
		return this.groupingTypes;
	}
	/**
	 * 设置(grouping类型，多个，逗号分隔)属性
	 */
	public void setGroupingTypes(String groupingTypes) {
		this.groupingTypes=groupingTypes;
	}
	/**
	 * 获取(grouping源头，多个，逗号分隔)属性
	 */
	public String getGroupingSpoutOrBlots() {
		return this.groupingSpoutOrBlots;
	}
	/**
	 * 设置(grouping源头，多个，逗号分隔)属性
	 */
	public void setGroupingSpoutOrBlots(String groupingSpoutOrBlots) {
		this.groupingSpoutOrBlots=groupingSpoutOrBlots;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result+ ((this.id == null) ? 0 : this.id.hashCode());
		result = prime * result+ ((this.taskId == null) ? 0 : this.taskId.hashCode());
		result = prime * result+ ((this.boltName == null) ? 0 : this.boltName.hashCode());
		result = prime * result+ ((this.boltClassName == null) ? 0 : this.boltClassName.hashCode());
		result = prime * result+ ((this.threads == null) ? 0 : this.threads.hashCode());
		result = prime * result+ ((this.groupingTypes == null) ? 0 : this.groupingTypes.hashCode());
		result = prime * result+ ((this.groupingSpoutOrBlots == null) ? 0 : this.groupingSpoutOrBlots.hashCode());
		return result;
		}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)return true;
		if (obj == null)
		return false;
		if (getClass() != obj.getClass())
		return false;
		StormTaskBoltVo other = (StormTaskBoltVo) obj;
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
	if (boltName == null) {
		if (other.boltName != null)
			return false;
		} else if (!boltName.equals(other.boltName))
			return false;
	if (boltClassName == null) {
		if (other.boltClassName != null)
			return false;
		} else if (!boltClassName.equals(other.boltClassName))
			return false;
	if (threads == null) {
		if (other.threads != null)
			return false;
		} else if (!threads.equals(other.threads))
			return false;
	if (groupingTypes == null) {
		if (other.groupingTypes != null)
			return false;
		} else if (!groupingTypes.equals(other.groupingTypes))
			return false;
	if (groupingSpoutOrBlots == null) {
		if (other.groupingSpoutOrBlots != null)
			return false;
		} else if (!groupingSpoutOrBlots.equals(other.groupingSpoutOrBlots))
			return false;
		return true;
	}
}