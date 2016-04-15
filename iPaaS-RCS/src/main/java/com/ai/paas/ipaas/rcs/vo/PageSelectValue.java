package com.ai.paas.ipaas.rcs.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageSelectValue<T> implements Serializable {
	private static final long serialVersionUID = 231081797378780346L;

	private List<T> resultList = new ArrayList<T>();

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}
