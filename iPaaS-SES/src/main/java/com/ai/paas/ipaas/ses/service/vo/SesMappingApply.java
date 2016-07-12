package com.ai.paas.ipaas.ses.service.vo;

import com.ai.paas.ipaas.rpc.api.vo.ApplyInfo;

/**
 * 创建mapping入参
 * @author jianhua.ma
 * @version 
 */
public class SesMappingApply extends ApplyInfo{

	private static final long serialVersionUID = -7276753137909397153L;
	/**
	 * 索引名称
	 */
	private String indexName;
	/**
	 * 索引类型
	 */
	private String indexType;
	/**
	 * ses引擎索引mapping定义(数据模型)
	 * 全局为"dynamic":"strict"
	 */
	private String mapping;
	
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public String getIndexType() {
		return indexType;
	}
	public void setIndexType(String indexType) {
		this.indexType = indexType;
	}
	public String getMapping() {
		return mapping;
	}
	public void setMapping(String mapping) {
		this.mapping = mapping;
	}
	

}

