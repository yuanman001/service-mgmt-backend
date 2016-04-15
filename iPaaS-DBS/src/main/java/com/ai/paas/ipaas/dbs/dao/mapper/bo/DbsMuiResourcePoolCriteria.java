package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class DbsMuiResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public DbsMuiResourcePoolCriteria() {
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

        public Criteria andMuiIdIsNull() {
            addCriterion("mui_id is null");
            return (Criteria) this;
        }

        public Criteria andMuiIdIsNotNull() {
            addCriterion("mui_id is not null");
            return (Criteria) this;
        }

        public Criteria andMuiIdEqualTo(Integer value) {
            addCriterion("mui_id =", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdNotEqualTo(Integer value) {
            addCriterion("mui_id <>", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdGreaterThan(Integer value) {
            addCriterion("mui_id >", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mui_id >=", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdLessThan(Integer value) {
            addCriterion("mui_id <", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdLessThanOrEqualTo(Integer value) {
            addCriterion("mui_id <=", value, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdIn(List<Integer> values) {
            addCriterion("mui_id in", values, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdNotIn(List<Integer> values) {
            addCriterion("mui_id not in", values, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdBetween(Integer value1, Integer value2) {
            addCriterion("mui_id between", value1, value2, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mui_id not between", value1, value2, "muiId");
            return (Criteria) this;
        }

        public Criteria andMuiUrlIsNull() {
            addCriterion("mui_url is null");
            return (Criteria) this;
        }

        public Criteria andMuiUrlIsNotNull() {
            addCriterion("mui_url is not null");
            return (Criteria) this;
        }

        public Criteria andMuiUrlEqualTo(String value) {
            addCriterion("mui_url =", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlNotEqualTo(String value) {
            addCriterion("mui_url <>", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlGreaterThan(String value) {
            addCriterion("mui_url >", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlGreaterThanOrEqualTo(String value) {
            addCriterion("mui_url >=", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlLessThan(String value) {
            addCriterion("mui_url <", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlLessThanOrEqualTo(String value) {
            addCriterion("mui_url <=", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlLike(String value) {
            addCriterion("mui_url like", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlNotLike(String value) {
            addCriterion("mui_url not like", value, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlIn(List<String> values) {
            addCriterion("mui_url in", values, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlNotIn(List<String> values) {
            addCriterion("mui_url not in", values, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlBetween(String value1, String value2) {
            addCriterion("mui_url between", value1, value2, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andMuiUrlNotBetween(String value1, String value2) {
            addCriterion("mui_url not between", value1, value2, "muiUrl");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
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