package com.ai.paas.ipaas.ccs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class CcsResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public CcsResourcePoolCriteria() {
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

        public Criteria andZkTypeCodeIsNull() {
            addCriterion("zk_type_code is null");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeIsNotNull() {
            addCriterion("zk_type_code is not null");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeEqualTo(int value) {
            addCriterion("zk_type_code =", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeNotEqualTo(int value) {
            addCriterion("zk_type_code <>", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeGreaterThan(int value) {
            addCriterion("zk_type_code >", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeGreaterThanOrEqualTo(int value) {
            addCriterion("zk_type_code >=", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeLessThan(int value) {
            addCriterion("zk_type_code <", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeLessThanOrEqualTo(int value) {
            addCriterion("zk_type_code <=", value, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeIn(List<Integer> values) {
            addCriterion("zk_type_code in", values, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeNotIn(List<Integer> values) {
            addCriterion("zk_type_code not in", values, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeBetween(int value1, int value2) {
            addCriterion("zk_type_code between", value1, value2, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkTypeCodeNotBetween(int value1, int value2) {
            addCriterion("zk_type_code not between", value1, value2, "zkTypeCode");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionIsNull() {
            addCriterion("zk_description is null");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionIsNotNull() {
            addCriterion("zk_description is not null");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionEqualTo(String value) {
            addCriterion("zk_description =", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionNotEqualTo(String value) {
            addCriterion("zk_description <>", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionGreaterThan(String value) {
            addCriterion("zk_description >", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("zk_description >=", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionLessThan(String value) {
            addCriterion("zk_description <", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionLessThanOrEqualTo(String value) {
            addCriterion("zk_description <=", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionLike(String value) {
            addCriterion("zk_description like", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionNotLike(String value) {
            addCriterion("zk_description not like", value, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionIn(List<String> values) {
            addCriterion("zk_description in", values, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionNotIn(List<String> values) {
            addCriterion("zk_description not in", values, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionBetween(String value1, String value2) {
            addCriterion("zk_description between", value1, value2, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andZkDescriptionNotBetween(String value1, String value2) {
            addCriterion("zk_description not between", value1, value2, "zkDescription");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameIsNull() {
            addCriterion("super_auth_name is null");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameIsNotNull() {
            addCriterion("super_auth_name is not null");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameEqualTo(String value) {
            addCriterion("super_auth_name =", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameNotEqualTo(String value) {
            addCriterion("super_auth_name <>", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameGreaterThan(String value) {
            addCriterion("super_auth_name >", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameGreaterThanOrEqualTo(String value) {
            addCriterion("super_auth_name >=", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameLessThan(String value) {
            addCriterion("super_auth_name <", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameLessThanOrEqualTo(String value) {
            addCriterion("super_auth_name <=", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameLike(String value) {
            addCriterion("super_auth_name like", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameNotLike(String value) {
            addCriterion("super_auth_name not like", value, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameIn(List<String> values) {
            addCriterion("super_auth_name in", values, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameNotIn(List<String> values) {
            addCriterion("super_auth_name not in", values, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameBetween(String value1, String value2) {
            addCriterion("super_auth_name between", value1, value2, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthNameNotBetween(String value1, String value2) {
            addCriterion("super_auth_name not between", value1, value2, "superAuthName");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordIsNull() {
            addCriterion("super_auth_password is null");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordIsNotNull() {
            addCriterion("super_auth_password is not null");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordEqualTo(String value) {
            addCriterion("super_auth_password =", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordNotEqualTo(String value) {
            addCriterion("super_auth_password <>", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordGreaterThan(String value) {
            addCriterion("super_auth_password >", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("super_auth_password >=", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordLessThan(String value) {
            addCriterion("super_auth_password <", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordLessThanOrEqualTo(String value) {
            addCriterion("super_auth_password <=", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordLike(String value) {
            addCriterion("super_auth_password like", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordNotLike(String value) {
            addCriterion("super_auth_password not like", value, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordIn(List<String> values) {
            addCriterion("super_auth_password in", values, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordNotIn(List<String> values) {
            addCriterion("super_auth_password not in", values, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordBetween(String value1, String value2) {
            addCriterion("super_auth_password between", value1, value2, "superAuthPassword");
            return (Criteria) this;
        }

        public Criteria andSuperAuthPasswordNotBetween(String value1, String value2) {
            addCriterion("super_auth_password not between", value1, value2, "superAuthPassword");
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