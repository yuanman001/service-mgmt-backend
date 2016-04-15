package com.ai.paas.ipaas.ccs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class CcsServiceUserConfigCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CcsServiceUserConfigCriteria() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(int value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(int value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(int value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(int value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(int value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(int value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(int value1, int value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(int value1, int value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andZkUserNameIsNull() {
            addCriterion("zk_user_name is null");
            return (Criteria) this;
        }

        public Criteria andZkUserNameIsNotNull() {
            addCriterion("zk_user_name is not null");
            return (Criteria) this;
        }

        public Criteria andZkUserNameEqualTo(String value) {
            addCriterion("zk_user_name =", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameNotEqualTo(String value) {
            addCriterion("zk_user_name <>", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameGreaterThan(String value) {
            addCriterion("zk_user_name >", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameGreaterThanOrEqualTo(String value) {
            addCriterion("zk_user_name >=", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameLessThan(String value) {
            addCriterion("zk_user_name <", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameLessThanOrEqualTo(String value) {
            addCriterion("zk_user_name <=", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameLike(String value) {
            addCriterion("zk_user_name like", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameNotLike(String value) {
            addCriterion("zk_user_name not like", value, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameIn(List<String> values) {
            addCriterion("zk_user_name in", values, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameNotIn(List<String> values) {
            addCriterion("zk_user_name not in", values, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameBetween(String value1, String value2) {
            addCriterion("zk_user_name between", value1, value2, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkUserNameNotBetween(String value1, String value2) {
            addCriterion("zk_user_name not between", value1, value2, "zkUserName");
            return (Criteria) this;
        }

        public Criteria andZkPasswordIsNull() {
            addCriterion("zk_password is null");
            return (Criteria) this;
        }

        public Criteria andZkPasswordIsNotNull() {
            addCriterion("zk_password is not null");
            return (Criteria) this;
        }

        public Criteria andZkPasswordEqualTo(String value) {
            addCriterion("zk_password =", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordNotEqualTo(String value) {
            addCriterion("zk_password <>", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordGreaterThan(String value) {
            addCriterion("zk_password >", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("zk_password >=", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordLessThan(String value) {
            addCriterion("zk_password <", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordLessThanOrEqualTo(String value) {
            addCriterion("zk_password <=", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordLike(String value) {
            addCriterion("zk_password like", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordNotLike(String value) {
            addCriterion("zk_password not like", value, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordIn(List<String> values) {
            addCriterion("zk_password in", values, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordNotIn(List<String> values) {
            addCriterion("zk_password not in", values, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordBetween(String value1, String value2) {
            addCriterion("zk_password between", value1, value2, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkPasswordNotBetween(String value1, String value2) {
            addCriterion("zk_password not between", value1, value2, "zkPassword");
            return (Criteria) this;
        }

        public Criteria andZkAddressIsNull() {
            addCriterion("zk_address is null");
            return (Criteria) this;
        }

        public Criteria andZkAddressIsNotNull() {
            addCriterion("zk_address is not null");
            return (Criteria) this;
        }

        public Criteria andZkAddressEqualTo(String value) {
            addCriterion("zk_address =", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressNotEqualTo(String value) {
            addCriterion("zk_address <>", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressGreaterThan(String value) {
            addCriterion("zk_address >", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressGreaterThanOrEqualTo(String value) {
            addCriterion("zk_address >=", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressLessThan(String value) {
            addCriterion("zk_address <", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressLessThanOrEqualTo(String value) {
            addCriterion("zk_address <=", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressLike(String value) {
            addCriterion("zk_address like", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressNotLike(String value) {
            addCriterion("zk_address not like", value, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressIn(List<String> values) {
            addCriterion("zk_address in", values, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressNotIn(List<String> values) {
            addCriterion("zk_address not in", values, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressBetween(String value1, String value2) {
            addCriterion("zk_address between", value1, value2, "zkAddress");
            return (Criteria) this;
        }

        public Criteria andZkAddressNotBetween(String value1, String value2) {
            addCriterion("zk_address not between", value1, value2, "zkAddress");
            return (Criteria) this;
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

        public Criteria andCcsResourceIdIsNull() {
            addCriterion("ccs_resource_id is null");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdIsNotNull() {
            addCriterion("ccs_resource_id is not null");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdEqualTo(int value) {
            addCriterion("ccs_resource_id =", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdNotEqualTo(int value) {
            addCriterion("ccs_resource_id <>", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdGreaterThan(int value) {
            addCriterion("ccs_resource_id >", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdGreaterThanOrEqualTo(int value) {
            addCriterion("ccs_resource_id >=", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdLessThan(int value) {
            addCriterion("ccs_resource_id <", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdLessThanOrEqualTo(int value) {
            addCriterion("ccs_resource_id <=", value, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdIn(List<Integer> values) {
            addCriterion("ccs_resource_id in", values, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdNotIn(List<Integer> values) {
            addCriterion("ccs_resource_id not in", values, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdBetween(int value1, int value2) {
            addCriterion("ccs_resource_id between", value1, value2, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andCcsResourceIdNotBetween(int value1, int value2) {
            addCriterion("ccs_resource_id not between", value1, value2, "ccsResourceId");
            return (Criteria) this;
        }

        public Criteria andServiceStatusIsNull() {
            addCriterion("service_status is null");
            return (Criteria) this;
        }

        public Criteria andServiceStatusIsNotNull() {
            addCriterion("service_status is not null");
            return (Criteria) this;
        }

        public Criteria andServiceStatusEqualTo(int value) {
            addCriterion("service_status =", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusNotEqualTo(int value) {
            addCriterion("service_status <>", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusGreaterThan(int value) {
            addCriterion("service_status >", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusGreaterThanOrEqualTo(int value) {
            addCriterion("service_status >=", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusLessThan(int value) {
            addCriterion("service_status <", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusLessThanOrEqualTo(int value) {
            addCriterion("service_status <=", value, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusIn(List<Integer> values) {
            addCriterion("service_status in", values, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusNotIn(List<Integer> values) {
            addCriterion("service_status not in", values, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusBetween(int value1, int value2) {
            addCriterion("service_status between", value1, value2, "serviceStatus");
            return (Criteria) this;
        }

        public Criteria andServiceStatusNotBetween(int value1, int value2) {
            addCriterion("service_status not between", value1, value2, "serviceStatus");
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