package com.ai.paas.ipaas.dss.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DssUserInstanceCriteria {
	protected String orderByClause;

	protected boolean distinct;

	protected List<Criteria> oredCriteria;

	protected Integer limitStart;

	protected Integer limitEnd;

	public DssUserInstanceCriteria() {
		oredCriteria = new ArrayList<Criteria>();
	}

	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	public String getOrderByClause() {
		return orderByClause;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	public void setLimitStart(Integer limitStart) {
		this.limitStart = limitStart;
	}

	public Integer getLimitStart() {
		return limitStart;
	}

	public void setLimitEnd(Integer limitEnd) {
		this.limitEnd = limitEnd;
	}

	public Integer getLimitEnd() {
		return limitEnd;
	}

	protected abstract static class GeneratedCriteria {
		protected List<Criterion> criteria;

		protected GeneratedCriteria() {
			super();
			criteria = new ArrayList<Criterion>();
		}

		public boolean isValid() {
			return criteria.size() > 0;
		}

		public List<Criterion> getAllCriteria() {
			return criteria;
		}

		public List<Criterion> getCriteria() {
			return criteria;
		}

		protected void addCriterion(String condition) {
			if (condition == null) {
				throw new RuntimeException("Value for condition cannot be null");
			}
			criteria.add(new Criterion(condition));
		}

		protected void addCriterion(String condition, Object value,
				String property) {
			if (value == null) {
				throw new RuntimeException("Value for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value));
		}

		protected void addCriterion(String condition, Object value1,
				Object value2, String property) {
			if (value1 == null || value2 == null) {
				throw new RuntimeException("Between values for " + property
						+ " cannot be null");
			}
			criteria.add(new Criterion(condition, value1, value2));
		}

		public Criteria andOssIdIsNull() {
			addCriterion("oss_id is null");
			return (Criteria) this;
		}

		public Criteria andOssIdIsNotNull() {
			addCriterion("oss_id is not null");
			return (Criteria) this;
		}

		public Criteria andOssIdEqualTo(Integer value) {
			addCriterion("oss_id =", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdNotEqualTo(Integer value) {
			addCriterion("oss_id <>", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdGreaterThan(Integer value) {
			addCriterion("oss_id >", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("oss_id >=", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdLessThan(Integer value) {
			addCriterion("oss_id <", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdLessThanOrEqualTo(Integer value) {
			addCriterion("oss_id <=", value, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdIn(List<Integer> values) {
			addCriterion("oss_id in", values, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdNotIn(List<Integer> values) {
			addCriterion("oss_id not in", values, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdBetween(Integer value1, Integer value2) {
			addCriterion("oss_id between", value1, value2, "ossId");
			return (Criteria) this;
		}

		public Criteria andOssIdNotBetween(Integer value1, Integer value2) {
			addCriterion("oss_id not between", value1, value2, "ossId");
			return (Criteria) this;
		}

		public Criteria andUserIdIsNull() {
			addCriterion("user_id is null");
			return (Criteria) this;
		}

		public Criteria andUserIdIsNotNull() {
			addCriterion("user_id is not null");
			return (Criteria) this;
		}

		public Criteria andUserIdEqualTo(String value) {
			addCriterion("user_id =", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotEqualTo(String value) {
			addCriterion("user_id <>", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdGreaterThan(String value) {
			addCriterion("user_id >", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdGreaterThanOrEqualTo(String value) {
			addCriterion("user_id >=", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLessThan(String value) {
			addCriterion("user_id <", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLessThanOrEqualTo(String value) {
			addCriterion("user_id <=", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdLike(String value) {
			addCriterion("user_id like", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotLike(String value) {
			addCriterion("user_id not like", value, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdIn(List<String> values) {
			addCriterion("user_id in", values, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotIn(List<String> values) {
			addCriterion("user_id not in", values, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdBetween(String value1, String value2) {
			addCriterion("user_id between", value1, value2, "userId");
			return (Criteria) this;
		}

		public Criteria andUserIdNotBetween(String value1, String value2) {
			addCriterion("user_id not between", value1, value2, "userId");
			return (Criteria) this;
		}

		public Criteria andDbNameIsNull() {
			addCriterion("db_name is null");
			return (Criteria) this;
		}

		public Criteria andDbNameIsNotNull() {
			addCriterion("db_name is not null");
			return (Criteria) this;
		}

		public Criteria andDbNameEqualTo(String value) {
			addCriterion("db_name =", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameNotEqualTo(String value) {
			addCriterion("db_name <>", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameGreaterThan(String value) {
			addCriterion("db_name >", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameGreaterThanOrEqualTo(String value) {
			addCriterion("db_name >=", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameLessThan(String value) {
			addCriterion("db_name <", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameLessThanOrEqualTo(String value) {
			addCriterion("db_name <=", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameLike(String value) {
			addCriterion("db_name like", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameNotLike(String value) {
			addCriterion("db_name not like", value, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameIn(List<String> values) {
			addCriterion("db_name in", values, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameNotIn(List<String> values) {
			addCriterion("db_name not in", values, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameBetween(String value1, String value2) {
			addCriterion("db_name between", value1, value2, "dbName");
			return (Criteria) this;
		}

		public Criteria andDbNameNotBetween(String value1, String value2) {
			addCriterion("db_name not between", value1, value2, "dbName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameIsNull() {
			addCriterion("collection_name is null");
			return (Criteria) this;
		}

		public Criteria andCollectionNameIsNotNull() {
			addCriterion("collection_name is not null");
			return (Criteria) this;
		}

		public Criteria andCollectionNameEqualTo(String value) {
			addCriterion("collection_name =", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameNotEqualTo(String value) {
			addCriterion("collection_name <>", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameGreaterThan(String value) {
			addCriterion("collection_name >", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameGreaterThanOrEqualTo(String value) {
			addCriterion("collection_name >=", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameLessThan(String value) {
			addCriterion("collection_name <", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameLessThanOrEqualTo(String value) {
			addCriterion("collection_name <=", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameLike(String value) {
			addCriterion("collection_name like", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameNotLike(String value) {
			addCriterion("collection_name not like", value, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameIn(List<String> values) {
			addCriterion("collection_name in", values, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameNotIn(List<String> values) {
			addCriterion("collection_name not in", values, "collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameBetween(String value1, String value2) {
			addCriterion("collection_name between", value1, value2,
					"collectionName");
			return (Criteria) this;
		}

		public Criteria andCollectionNameNotBetween(String value1, String value2) {
			addCriterion("collection_name not between", value1, value2,
					"collectionName");
			return (Criteria) this;
		}

		public Criteria andOssSizeIsNull() {
			addCriterion("oss_size is null");
			return (Criteria) this;
		}

		public Criteria andOssSizeIsNotNull() {
			addCriterion("oss_size is not null");
			return (Criteria) this;
		}

		public Criteria andOssSizeEqualTo(Double value) {
			addCriterion("oss_size =", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeNotEqualTo(Double value) {
			addCriterion("oss_size <>", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeGreaterThan(Double value) {
			addCriterion("oss_size >", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeGreaterThanOrEqualTo(Double value) {
			addCriterion("oss_size >=", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeLessThan(Double value) {
			addCriterion("oss_size <", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeLessThanOrEqualTo(Double value) {
			addCriterion("oss_size <=", value, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeIn(List<Double> values) {
			addCriterion("oss_size in", values, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeNotIn(List<Double> values) {
			addCriterion("oss_size not in", values, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeBetween(Double value1, Double value2) {
			addCriterion("oss_size between", value1, value2, "ossSize");
			return (Criteria) this;
		}

		public Criteria andOssSizeNotBetween(Double value1, Double value2) {
			addCriterion("oss_size not between", value1, value2, "ossSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeIsNull() {
			addCriterion("file_limit_size is null");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeIsNotNull() {
			addCriterion("file_limit_size is not null");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeEqualTo(Double value) {
			addCriterion("file_limit_size =", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeNotEqualTo(Double value) {
			addCriterion("file_limit_size <>", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeGreaterThan(Double value) {
			addCriterion("file_limit_size >", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeGreaterThanOrEqualTo(Double value) {
			addCriterion("file_limit_size >=", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeLessThan(Double value) {
			addCriterion("file_limit_size <", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeLessThanOrEqualTo(Double value) {
			addCriterion("file_limit_size <=", value, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeIn(List<Double> values) {
			addCriterion("file_limit_size in", values, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeNotIn(List<Double> values) {
			addCriterion("file_limit_size not in", values, "fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeBetween(Double value1, Double value2) {
			addCriterion("file_limit_size between", value1, value2,
					"fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andFileLimitSizeNotBetween(Double value1, Double value2) {
			addCriterion("file_limit_size not between", value1, value2,
					"fileLimitSize");
			return (Criteria) this;
		}

		public Criteria andStartDateIsNull() {
			addCriterion("start_date is null");
			return (Criteria) this;
		}

		public Criteria andStartDateIsNotNull() {
			addCriterion("start_date is not null");
			return (Criteria) this;
		}

		public Criteria andStartDateEqualTo(Timestamp value) {
			addCriterion("start_date =", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateNotEqualTo(Timestamp value) {
			addCriterion("start_date <>", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateGreaterThan(Timestamp value) {
			addCriterion("start_date >", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateGreaterThanOrEqualTo(Timestamp value) {
			addCriterion("start_date >=", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateLessThan(Timestamp value) {
			addCriterion("start_date <", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateLessThanOrEqualTo(Timestamp value) {
			addCriterion("start_date <=", value, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateIn(List<Timestamp> values) {
			addCriterion("start_date in", values, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateNotIn(List<Timestamp> values) {
			addCriterion("start_date not in", values, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateBetween(Timestamp value1, Timestamp value2) {
			addCriterion("start_date between", value1, value2, "startDate");
			return (Criteria) this;
		}

		public Criteria andStartDateNotBetween(Timestamp value1,
				Timestamp value2) {
			addCriterion("start_date not between", value1, value2, "startDate");
			return (Criteria) this;
		}

		public Criteria andEndDateIsNull() {
			addCriterion("end_date is null");
			return (Criteria) this;
		}

		public Criteria andEndDateIsNotNull() {
			addCriterion("end_date is not null");
			return (Criteria) this;
		}

		public Criteria andEndDateEqualTo(Timestamp value) {
			addCriterion("end_date =", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateNotEqualTo(Timestamp value) {
			addCriterion("end_date <>", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateGreaterThan(Timestamp value) {
			addCriterion("end_date >", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateGreaterThanOrEqualTo(Timestamp value) {
			addCriterion("end_date >=", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateLessThan(Timestamp value) {
			addCriterion("end_date <", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateLessThanOrEqualTo(Timestamp value) {
			addCriterion("end_date <=", value, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateIn(List<Timestamp> values) {
			addCriterion("end_date in", values, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateNotIn(List<Timestamp> values) {
			addCriterion("end_date not in", values, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateBetween(Timestamp value1, Timestamp value2) {
			addCriterion("end_date between", value1, value2, "endDate");
			return (Criteria) this;
		}

		public Criteria andEndDateNotBetween(Timestamp value1, Timestamp value2) {
			addCriterion("end_date not between", value1, value2, "endDate");
			return (Criteria) this;
		}

		public Criteria andGroupIdIsNull() {
			addCriterion("group_id is null");
			return (Criteria) this;
		}

		public Criteria andGroupIdIsNotNull() {
			addCriterion("group_id is not null");
			return (Criteria) this;
		}

		public Criteria andGroupIdEqualTo(Integer value) {
			addCriterion("group_id =", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdNotEqualTo(Integer value) {
			addCriterion("group_id <>", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdGreaterThan(Integer value) {
			addCriterion("group_id >", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("group_id >=", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdLessThan(Integer value) {
			addCriterion("group_id <", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdLessThanOrEqualTo(Integer value) {
			addCriterion("group_id <=", value, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdIn(List<Integer> values) {
			addCriterion("group_id in", values, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdNotIn(List<Integer> values) {
			addCriterion("group_id not in", values, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdBetween(Integer value1, Integer value2) {
			addCriterion("group_id between", value1, value2, "groupId");
			return (Criteria) this;
		}

		public Criteria andGroupIdNotBetween(Integer value1, Integer value2) {
			addCriterion("group_id not between", value1, value2, "groupId");
			return (Criteria) this;
		}

		public Criteria andRedisIdIsNull() {
			addCriterion("redis_id is null");
			return (Criteria) this;
		}

		public Criteria andRedisIdIsNotNull() {
			addCriterion("redis_id is not null");
			return (Criteria) this;
		}

		public Criteria andRedisIdEqualTo(Integer value) {
			addCriterion("redis_id =", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdNotEqualTo(Integer value) {
			addCriterion("redis_id <>", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdGreaterThan(Integer value) {
			addCriterion("redis_id >", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("redis_id >=", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdLessThan(Integer value) {
			addCriterion("redis_id <", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdLessThanOrEqualTo(Integer value) {
			addCriterion("redis_id <=", value, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdIn(List<Integer> values) {
			addCriterion("redis_id in", values, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdNotIn(List<Integer> values) {
			addCriterion("redis_id not in", values, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdBetween(Integer value1, Integer value2) {
			addCriterion("redis_id between", value1, value2, "redisId");
			return (Criteria) this;
		}

		public Criteria andRedisIdNotBetween(Integer value1, Integer value2) {
			addCriterion("redis_id not between", value1, value2, "redisId");
			return (Criteria) this;
		}

		public Criteria andServiceNameIsNull() {
			addCriterion("service_name is null");
			return (Criteria) this;
		}

		public Criteria andServiceNameIsNotNull() {
			addCriterion("service_name is not null");
			return (Criteria) this;
		}

		public Criteria andServiceNameEqualTo(String value) {
			addCriterion("service_name =", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameNotEqualTo(String value) {
			addCriterion("service_name <>", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameGreaterThan(String value) {
			addCriterion("service_name >", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameGreaterThanOrEqualTo(String value) {
			addCriterion("service_name >=", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameLessThan(String value) {
			addCriterion("service_name <", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameLessThanOrEqualTo(String value) {
			addCriterion("service_name <=", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameLike(String value) {
			addCriterion("service_name like", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameNotLike(String value) {
			addCriterion("service_name not like", value, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameIn(List<String> values) {
			addCriterion("service_name in", values, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameNotIn(List<String> values) {
			addCriterion("service_name not in", values, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameBetween(String value1, String value2) {
			addCriterion("service_name between", value1, value2, "serviceName");
			return (Criteria) this;
		}

		public Criteria andServiceNameNotBetween(String value1, String value2) {
			addCriterion("service_name not between", value1, value2,
					"serviceName");
			return (Criteria) this;
		}
	}

	public static class Criteria extends GeneratedCriteria {

		protected Criteria() {
			super();
		}
	}

	public static class Criterion {
		private String condition;

		private Object value;

		private Object secondValue;

		private boolean noValue;

		private boolean singleValue;

		private boolean betweenValue;

		private boolean listValue;

		private String typeHandler;

		public String getCondition() {
			return condition;
		}

		public Object getValue() {
			return value;
		}

		public Object getSecondValue() {
			return secondValue;
		}

		public boolean isNoValue() {
			return noValue;
		}

		public boolean isSingleValue() {
			return singleValue;
		}

		public boolean isBetweenValue() {
			return betweenValue;
		}

		public boolean isListValue() {
			return listValue;
		}

		public String getTypeHandler() {
			return typeHandler;
		}

		protected Criterion(String condition) {
			super();
			this.condition = condition;
			this.typeHandler = null;
			this.noValue = true;
		}

		protected Criterion(String condition, Object value, String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.typeHandler = typeHandler;
			if (value instanceof List<?>) {
				this.listValue = true;
			} else {
				this.singleValue = true;
			}
		}

		protected Criterion(String condition, Object value) {
			this(condition, value, null);
		}

		protected Criterion(String condition, Object value, Object secondValue,
				String typeHandler) {
			super();
			this.condition = condition;
			this.value = value;
			this.secondValue = secondValue;
			this.typeHandler = typeHandler;
			this.betweenValue = true;
		}

		protected Criterion(String condition, Object value, Object secondValue) {
			this(condition, value, secondValue, null);
		}
	}
}