package com.ai.paas.ipaas.rcs.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.page.model.PageEntity;
import com.ai.paas.ipaas.page.model.PagingResult;

public interface IBaseSv<T, PK extends Serializable> {
	/**
	 * 查询所有
	 */
	public abstract List<T> searchAll() throws PaasException;
	/**
	 * 查询一个
	 */
	public abstract T searchOneById(PK pk) throws PaasException;
	/**
	 * 查询page
	 */
	public abstract PagingResult<T> searchPage(PageEntity pageEntity) throws PaasException;
	/**
	 * 查询List
	 */
	public abstract List<T> searchList(Map<String, Object> params) throws PaasException;

	/**
	 * 增
	 */
	public abstract void add(T t) throws PaasException;

	/**
	 * 删
	 */
	public abstract void del(PK id) throws PaasException;

	/**
	 * 改
	 */
	public abstract void update(T t) throws PaasException;
}
