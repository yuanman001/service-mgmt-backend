package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbsLogicResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public DbsLogicResourcePoolCriteria() {
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

        public Criteria andLogicIdIsNull() {
            addCriterion("logic_id is null");
            return (Criteria) this;
        }

        public Criteria andLogicIdIsNotNull() {
            addCriterion("logic_id is not null");
            return (Criteria) this;
        }

        public Criteria andLogicIdEqualTo(Integer value) {
            addCriterion("logic_id =", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotEqualTo(Integer value) {
            addCriterion("logic_id <>", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdGreaterThan(Integer value) {
            addCriterion("logic_id >", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("logic_id >=", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdLessThan(Integer value) {
            addCriterion("logic_id <", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdLessThanOrEqualTo(Integer value) {
            addCriterion("logic_id <=", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdIn(List<Integer> values) {
            addCriterion("logic_id in", values, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotIn(List<Integer> values) {
            addCriterion("logic_id not in", values, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdBetween(Integer value1, Integer value2) {
            addCriterion("logic_id between", value1, value2, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotBetween(Integer value1, Integer value2) {
            addCriterion("logic_id not between", value1, value2, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicNameIsNull() {
            addCriterion("logic_name is null");
            return (Criteria) this;
        }

        public Criteria andLogicNameIsNotNull() {
            addCriterion("logic_name is not null");
            return (Criteria) this;
        }

        public Criteria andLogicNameEqualTo(String value) {
            addCriterion("logic_name =", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameNotEqualTo(String value) {
            addCriterion("logic_name <>", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameGreaterThan(String value) {
            addCriterion("logic_name >", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameGreaterThanOrEqualTo(String value) {
            addCriterion("logic_name >=", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameLessThan(String value) {
            addCriterion("logic_name <", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameLessThanOrEqualTo(String value) {
            addCriterion("logic_name <=", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameLike(String value) {
            addCriterion("logic_name like", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameNotLike(String value) {
            addCriterion("logic_name not like", value, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameIn(List<String> values) {
            addCriterion("logic_name in", values, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameNotIn(List<String> values) {
            addCriterion("logic_name not in", values, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameBetween(String value1, String value2) {
            addCriterion("logic_name between", value1, value2, "logicName");
            return (Criteria) this;
        }

        public Criteria andLogicNameNotBetween(String value1, String value2) {
            addCriterion("logic_name not between", value1, value2, "logicName");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Timestamp value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Timestamp value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Timestamp value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Timestamp value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Timestamp> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Timestamp> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIsNull() {
            addCriterion("res_use_type is null");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIsNotNull() {
            addCriterion("res_use_type is not null");
            return (Criteria) this;
        }

        public Criteria andResUseTypeEqualTo(String value) {
            addCriterion("res_use_type =", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotEqualTo(String value) {
            addCriterion("res_use_type <>", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeGreaterThan(String value) {
            addCriterion("res_use_type >", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeGreaterThanOrEqualTo(String value) {
            addCriterion("res_use_type >=", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLessThan(String value) {
            addCriterion("res_use_type <", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLessThanOrEqualTo(String value) {
            addCriterion("res_use_type <=", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLike(String value) {
            addCriterion("res_use_type like", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotLike(String value) {
            addCriterion("res_use_type not like", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIn(List<String> values) {
            addCriterion("res_use_type in", values, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotIn(List<String> values) {
            addCriterion("res_use_type not in", values, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeBetween(String value1, String value2) {
            addCriterion("res_use_type between", value1, value2, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotBetween(String value1, String value2) {
            addCriterion("res_use_type not between", value1, value2, "resUseType");
            return (Criteria) this;
        }

        public Criteria andUsedIdIsNull() {
            addCriterion("used_id is null");
            return (Criteria) this;
        }

        public Criteria andUsedIdIsNotNull() {
            addCriterion("used_id is not null");
            return (Criteria) this;
        }

        public Criteria andUsedIdEqualTo(Integer value) {
            addCriterion("used_id =", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdNotEqualTo(Integer value) {
            addCriterion("used_id <>", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdGreaterThan(Integer value) {
            addCriterion("used_id >", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("used_id >=", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdLessThan(Integer value) {
            addCriterion("used_id <", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdLessThanOrEqualTo(Integer value) {
            addCriterion("used_id <=", value, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdIn(List<Integer> values) {
            addCriterion("used_id in", values, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdNotIn(List<Integer> values) {
            addCriterion("used_id not in", values, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdBetween(Integer value1, Integer value2) {
            addCriterion("used_id between", value1, value2, "usedId");
            return (Criteria) this;
        }

        public Criteria andUsedIdNotBetween(Integer value1, Integer value2) {
            addCriterion("used_id not between", value1, value2, "usedId");
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