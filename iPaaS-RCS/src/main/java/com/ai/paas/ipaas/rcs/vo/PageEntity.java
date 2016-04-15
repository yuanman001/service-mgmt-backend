package com.ai.paas.ipaas.rcs.vo;

import java.io.Serializable;
import java.util.Map;

public class PageEntity implements Serializable {
	private static final long serialVersionUID = 4933771255759680537L;
	
	private int currentPage; // 请求页数
	
	private int PageSize; // 每页大小
	
	private int limitStart ;
	
	private int limitEnd;
	
	private String name;
	
	private String userID; //20150611
	
	private Map<String,String> params; // 传入的参数

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		countLimit();
	}

	public int getPageSize() {
		return PageSize;
	}

	public void setPageSize(int pageSize) {
		PageSize = pageSize;
		countLimit();
	}
	
	private void countLimit(){
		if(this.PageSize != 0 && this.currentPage != 0){
			 this.limitStart = this.PageSize  * (this.currentPage -1);
			 limitEnd = this.PageSize;
		}
	}

	public int getLimitStart() {
		return limitStart;
	}

	public int getLimitEnd() {
		return limitEnd;
	}

	public Map<String,String> getParams() {
		return params;
	}

	public void setParams(Map<String,String> params) {
		this.params = params;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
		
	public String getUserID(){
		return userID;
	}
	
	public void setUserID(String userID){
		this.userID=userID;
	}
}
