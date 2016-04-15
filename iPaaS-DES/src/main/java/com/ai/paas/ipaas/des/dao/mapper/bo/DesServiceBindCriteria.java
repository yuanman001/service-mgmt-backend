package com.ai.paas.ipaas.des.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class DesServiceBindCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public DesServiceBindCriteria() {
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
        this.limitStart=limitStart;
    }

    public Integer getLimitStart() {
        return limitStart;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd=limitEnd;
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

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andServiceIdIsNull() {
            addCriterion("service_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNotNull() {
            addCriterion("service_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceIdEqualTo(String value) {
            addCriterion("service_id =", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotEqualTo(String value) {
            addCriterion("service_id <>", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThan(String value) {
            addCriterion("service_id >", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_id >=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThan(String value) {
            addCriterion("service_id <", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThanOrEqualTo(String value) {
            addCriterion("service_id <=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLike(String value) {
            addCriterion("service_id like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotLike(String value) {
            addCriterion("service_id not like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIn(List<String> values) {
            addCriterion("service_id in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotIn(List<String> values) {
            addCriterion("service_id not in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdBetween(String value1, String value2) {
            addCriterion("service_id between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotBetween(String value1, String value2) {
            addCriterion("service_id not between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdIsNull() {
            addCriterion("dbs_service_id is null");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdIsNotNull() {
            addCriterion("dbs_service_id is not null");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdEqualTo(String value) {
            addCriterion("dbs_service_id =", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdNotEqualTo(String value) {
            addCriterion("dbs_service_id <>", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdGreaterThan(String value) {
            addCriterion("dbs_service_id >", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("dbs_service_id >=", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdLessThan(String value) {
            addCriterion("dbs_service_id <", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdLessThanOrEqualTo(String value) {
            addCriterion("dbs_service_id <=", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdLike(String value) {
            addCriterion("dbs_service_id like", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdNotLike(String value) {
            addCriterion("dbs_service_id not like", value, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdIn(List<String> values) {
            addCriterion("dbs_service_id in", values, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdNotIn(List<String> values) {
            addCriterion("dbs_service_id not in", values, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdBetween(String value1, String value2) {
            addCriterion("dbs_service_id between", value1, value2, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServiceIdNotBetween(String value1, String value2) {
            addCriterion("dbs_service_id not between", value1, value2, "dbsServiceId");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordIsNull() {
            addCriterion("dbs_service_password is null");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordIsNotNull() {
            addCriterion("dbs_service_password is not null");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordEqualTo(String value) {
            addCriterion("dbs_service_password =", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordNotEqualTo(String value) {
            addCriterion("dbs_service_password <>", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordGreaterThan(String value) {
            addCriterion("dbs_service_password >", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordGreaterThanOrEqualTo(String value) {
            addCriterion("dbs_service_password >=", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordLessThan(String value) {
            addCriterion("dbs_service_password <", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordLessThanOrEqualTo(String value) {
            addCriterion("dbs_service_password <=", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordLike(String value) {
            addCriterion("dbs_service_password like", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordNotLike(String value) {
            addCriterion("dbs_service_password not like", value, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordIn(List<String> values) {
            addCriterion("dbs_service_password in", values, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordNotIn(List<String> values) {
            addCriterion("dbs_service_password not in", values, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordBetween(String value1, String value2) {
            addCriterion("dbs_service_password between", value1, value2, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andDbsServicePasswordNotBetween(String value1, String value2) {
            addCriterion("dbs_service_password not between", value1, value2, "dbsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdIsNull() {
            addCriterion("mds_service_id is null");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdIsNotNull() {
            addCriterion("mds_service_id is not null");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdEqualTo(String value) {
            addCriterion("mds_service_id =", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdNotEqualTo(String value) {
            addCriterion("mds_service_id <>", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdGreaterThan(String value) {
            addCriterion("mds_service_id >", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("mds_service_id >=", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdLessThan(String value) {
            addCriterion("mds_service_id <", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdLessThanOrEqualTo(String value) {
            addCriterion("mds_service_id <=", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdLike(String value) {
            addCriterion("mds_service_id like", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdNotLike(String value) {
            addCriterion("mds_service_id not like", value, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdIn(List<String> values) {
            addCriterion("mds_service_id in", values, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdNotIn(List<String> values) {
            addCriterion("mds_service_id not in", values, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdBetween(String value1, String value2) {
            addCriterion("mds_service_id between", value1, value2, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServiceIdNotBetween(String value1, String value2) {
            addCriterion("mds_service_id not between", value1, value2, "mdsServiceId");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordIsNull() {
            addCriterion("mds_service_password is null");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordIsNotNull() {
            addCriterion("mds_service_password is not null");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordEqualTo(String value) {
            addCriterion("mds_service_password =", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordNotEqualTo(String value) {
            addCriterion("mds_service_password <>", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordGreaterThan(String value) {
            addCriterion("mds_service_password >", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordGreaterThanOrEqualTo(String value) {
            addCriterion("mds_service_password >=", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordLessThan(String value) {
            addCriterion("mds_service_password <", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordLessThanOrEqualTo(String value) {
            addCriterion("mds_service_password <=", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordLike(String value) {
            addCriterion("mds_service_password like", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordNotLike(String value) {
            addCriterion("mds_service_password not like", value, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordIn(List<String> values) {
            addCriterion("mds_service_password in", values, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordNotIn(List<String> values) {
            addCriterion("mds_service_password not in", values, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordBetween(String value1, String value2) {
            addCriterion("mds_service_password between", value1, value2, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsServicePasswordNotBetween(String value1, String value2) {
            addCriterion("mds_service_password not between", value1, value2, "mdsServicePassword");
            return (Criteria) this;
        }

        public Criteria andMdsTopicIsNull() {
            addCriterion("mds_topic is null");
            return (Criteria) this;
        }

        public Criteria andMdsTopicIsNotNull() {
            addCriterion("mds_topic is not null");
            return (Criteria) this;
        }

        public Criteria andMdsTopicEqualTo(String value) {
            addCriterion("mds_topic =", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicNotEqualTo(String value) {
            addCriterion("mds_topic <>", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicGreaterThan(String value) {
            addCriterion("mds_topic >", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicGreaterThanOrEqualTo(String value) {
            addCriterion("mds_topic >=", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicLessThan(String value) {
            addCriterion("mds_topic <", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicLessThanOrEqualTo(String value) {
            addCriterion("mds_topic <=", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicLike(String value) {
            addCriterion("mds_topic like", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicNotLike(String value) {
            addCriterion("mds_topic not like", value, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicIn(List<String> values) {
            addCriterion("mds_topic in", values, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicNotIn(List<String> values) {
            addCriterion("mds_topic not in", values, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicBetween(String value1, String value2) {
            addCriterion("mds_topic between", value1, value2, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsTopicNotBetween(String value1, String value2) {
            addCriterion("mds_topic not between", value1, value2, "mdsTopic");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionIsNull() {
            addCriterion("mds_partition is null");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionIsNotNull() {
            addCriterion("mds_partition is not null");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionEqualTo(Integer value) {
            addCriterion("mds_partition =", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionNotEqualTo(Integer value) {
            addCriterion("mds_partition <>", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionGreaterThan(Integer value) {
            addCriterion("mds_partition >", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionGreaterThanOrEqualTo(Integer value) {
            addCriterion("mds_partition >=", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionLessThan(Integer value) {
            addCriterion("mds_partition <", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionLessThanOrEqualTo(Integer value) {
            addCriterion("mds_partition <=", value, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionIn(List<Integer> values) {
            addCriterion("mds_partition in", values, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionNotIn(List<Integer> values) {
            addCriterion("mds_partition not in", values, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionBetween(Integer value1, Integer value2) {
            addCriterion("mds_partition between", value1, value2, "mdsPartition");
            return (Criteria) this;
        }

        public Criteria andMdsPartitionNotBetween(Integer value1, Integer value2) {
            addCriterion("mds_partition not between", value1, value2, "mdsPartition");
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

        public Criteria andUserNameIsNull() {
            addCriterion("user_name is null");
            return (Criteria) this;
        }

        public Criteria andUserNameIsNotNull() {
            addCriterion("user_name is not null");
            return (Criteria) this;
        }

        public Criteria andUserNameEqualTo(String value) {
            addCriterion("user_name =", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotEqualTo(String value) {
            addCriterion("user_name <>", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThan(String value) {
            addCriterion("user_name >", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("user_name >=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThan(String value) {
            addCriterion("user_name <", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLessThanOrEqualTo(String value) {
            addCriterion("user_name <=", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameLike(String value) {
            addCriterion("user_name like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotLike(String value) {
            addCriterion("user_name not like", value, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameIn(List<String> values) {
            addCriterion("user_name in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotIn(List<String> values) {
            addCriterion("user_name not in", values, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameBetween(String value1, String value2) {
            addCriterion("user_name between", value1, value2, "userName");
            return (Criteria) this;
        }

        public Criteria andUserNameNotBetween(String value1, String value2) {
            addCriterion("user_name not between", value1, value2, "userName");
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

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
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