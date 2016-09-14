package com.ai.paas.ipaas.vo.ses;

import java.io.Serializable;
import java.util.List;

/**
 * 配置sql
 *
 */
public class SesIndexSqlInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 933527941994846621L;
	
	/**主键*/
	private int id;
	/**主表*/
	private transient SesIndexPrimarySql primarySql;
	/**辅助表*/
	private transient List<SesIndexFiledSql> filedSqls;
	
	
	public SesIndexPrimarySql getPrimarySql() {
		return primarySql;
	}
	public void setPrimarySql(SesIndexPrimarySql primarySql) {
		this.primarySql = primarySql;
	}
	public List<SesIndexFiledSql> getFiledSqls() {
		return filedSqls;
	}
	public void setFiledSqls(List<SesIndexFiledSql> filedSqls) {
		this.filedSqls = filedSqls;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	

}
