/*package com.ai.paas.ipaas.rcs.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ai.paas.ipaas.PaasException;
import com.ai.paas.ipaas.base.orm.IGenericDao;
import com.ai.paas.ipaas.page.model.PageEntity;
import com.ai.paas.ipaas.page.model.PagingResult;

public class BaseSvImpl<T, PK extends Serializable> implements IBaseSv<T, PK> {
	private IGenericDao<T, PK> baseDao;

	public void setBaseDao(IGenericDao<T, PK> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public List<T> searchAll() throws PaasException {
		return baseDao.getAll();
	}

	@Override
	public void add(T t) throws PaasException {
		baseDao.insert(t);
	}

	@Override
	public void del(PK id) throws PaasException {
		if (baseDao.delete(id) != 1) {
			throw new PaasException("999999", "删除失败【"+id+"】");
		}
	}

	@Override
	public void update(T t) throws PaasException {
		if (baseDao.update(t) != 1) {
			throw new PaasException("999999", "update失败【"+t+"】");
		}
	}

	@Override
	public T searchOneById(PK pk) throws PaasException {
		return baseDao.get(pk);
	}

	@Override
	public PagingResult<T> searchPage(PageEntity pageEntity) throws PaasException {
		return baseDao.selectPagination(pageEntity);
	}

	@Override
	public List<T> searchList(Map<String, Object> params) throws PaasException {
		return baseDao.getAllByParam(params);
	}


	
}
*/