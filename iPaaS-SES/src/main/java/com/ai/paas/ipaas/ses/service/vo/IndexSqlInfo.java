package com.ai.paas.ipaas.ses.service.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 配置sql
 *
 */
public class IndexSqlInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933527941994846621L;
	
	/**主键*/
	private int id;
	/**主表*/
	private transient IndexPrimarySql primarySql;
	/**辅助表*/
	private transient List<IndexFiledSql> filedSqls;
	
	
	public IndexPrimarySql getPrimarySql() {
		return primarySql;
	}
	public void setPrimarySql(IndexPrimarySql primarySql) {
		this.primarySql = primarySql;
	}
	public List<IndexFiledSql> getFiledSqls() {
		return filedSqls;
	}
	public void setFiledSqls(List<IndexFiledSql> filedSqls) {
		this.filedSqls = filedSqls;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	

}
