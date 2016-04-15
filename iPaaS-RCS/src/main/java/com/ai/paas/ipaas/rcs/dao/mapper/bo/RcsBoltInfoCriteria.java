package com.ai.paas.ipaas.rcs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class RcsBoltInfoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public RcsBoltInfoCriteria() {
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

        public Criteria andIdEqualTo(long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(long value1, long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(long value1, long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("task_id is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("task_id is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(long value) {
            addCriterion("task_id =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(long value) {
            addCriterion("task_id <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(long value) {
            addCriterion("task_id >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(long value) {
            addCriterion("task_id >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(long value) {
            addCriterion("task_id <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(long value) {
            addCriterion("task_id <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(List<Long> values) {
            addCriterion("task_id in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(List<Long> values) {
            addCriterion("task_id not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(long value1, long value2) {
            addCriterion("task_id between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(long value1, long value2) {
            addCriterion("task_id not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andBoltNameIsNull() {
            addCriterion("bolt_name is null");
            return (Criteria) this;
        }

        public Criteria andBoltNameIsNotNull() {
            addCriterion("bolt_name is not null");
            return (Criteria) this;
        }

        public Criteria andBoltNameEqualTo(String value) {
            addCriterion("bolt_name =", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameNotEqualTo(String value) {
            addCriterion("bolt_name <>", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameGreaterThan(String value) {
            addCriterion("bolt_name >", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameGreaterThanOrEqualTo(String value) {
            addCriterion("bolt_name >=", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameLessThan(String value) {
            addCriterion("bolt_name <", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameLessThanOrEqualTo(String value) {
            addCriterion("bolt_name <=", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameLike(String value) {
            addCriterion("bolt_name like", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameNotLike(String value) {
            addCriterion("bolt_name not like", value, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameIn(List<String> values) {
            addCriterion("bolt_name in", values, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameNotIn(List<String> values) {
            addCriterion("bolt_name not in", values, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameBetween(String value1, String value2) {
            addCriterion("bolt_name between", value1, value2, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltNameNotBetween(String value1, String value2) {
            addCriterion("bolt_name not between", value1, value2, "boltName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameIsNull() {
            addCriterion("bolt_class_name is null");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameIsNotNull() {
            addCriterion("bolt_class_name is not null");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameEqualTo(String value) {
            addCriterion("bolt_class_name =", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameNotEqualTo(String value) {
            addCriterion("bolt_class_name <>", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameGreaterThan(String value) {
            addCriterion("bolt_class_name >", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameGreaterThanOrEqualTo(String value) {
            addCriterion("bolt_class_name >=", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameLessThan(String value) {
            addCriterion("bolt_class_name <", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameLessThanOrEqualTo(String value) {
            addCriterion("bolt_class_name <=", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameLike(String value) {
            addCriterion("bolt_class_name like", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameNotLike(String value) {
            addCriterion("bolt_class_name not like", value, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameIn(List<String> values) {
            addCriterion("bolt_class_name in", values, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameNotIn(List<String> values) {
            addCriterion("bolt_class_name not in", values, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameBetween(String value1, String value2) {
            addCriterion("bolt_class_name between", value1, value2, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andBoltClassNameNotBetween(String value1, String value2) {
            addCriterion("bolt_class_name not between", value1, value2, "boltClassName");
            return (Criteria) this;
        }

        public Criteria andThreadsIsNull() {
            addCriterion("threads is null");
            return (Criteria) this;
        }

        public Criteria andThreadsIsNotNull() {
            addCriterion("threads is not null");
            return (Criteria) this;
        }

        public Criteria andThreadsEqualTo(int value) {
            addCriterion("threads =", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsNotEqualTo(int value) {
            addCriterion("threads <>", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsGreaterThan(int value) {
            addCriterion("threads >", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsGreaterThanOrEqualTo(int value) {
            addCriterion("threads >=", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsLessThan(int value) {
            addCriterion("threads <", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsLessThanOrEqualTo(int value) {
            addCriterion("threads <=", value, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsIn(List<Integer> values) {
            addCriterion("threads in", values, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsNotIn(List<Integer> values) {
            addCriterion("threads not in", values, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsBetween(int value1, int value2) {
            addCriterion("threads between", value1, value2, "threads");
            return (Criteria) this;
        }

        public Criteria andThreadsNotBetween(int value1, int value2) {
            addCriterion("threads not between", value1, value2, "threads");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesIsNull() {
            addCriterion("grouping_types is null");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesIsNotNull() {
            addCriterion("grouping_types is not null");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesEqualTo(String value) {
            addCriterion("grouping_types =", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesNotEqualTo(String value) {
            addCriterion("grouping_types <>", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesGreaterThan(String value) {
            addCriterion("grouping_types >", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesGreaterThanOrEqualTo(String value) {
            addCriterion("grouping_types >=", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesLessThan(String value) {
            addCriterion("grouping_types <", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesLessThanOrEqualTo(String value) {
            addCriterion("grouping_types <=", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesLike(String value) {
            addCriterion("grouping_types like", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesNotLike(String value) {
            addCriterion("grouping_types not like", value, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesIn(List<String> values) {
            addCriterion("grouping_types in", values, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesNotIn(List<String> values) {
            addCriterion("grouping_types not in", values, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesBetween(String value1, String value2) {
            addCriterion("grouping_types between", value1, value2, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingTypesNotBetween(String value1, String value2) {
            addCriterion("grouping_types not between", value1, value2, "groupingTypes");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsIsNull() {
            addCriterion("grouping_spout_or_blots is null");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsIsNotNull() {
            addCriterion("grouping_spout_or_blots is not null");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsEqualTo(String value) {
            addCriterion("grouping_spout_or_blots =", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsNotEqualTo(String value) {
            addCriterion("grouping_spout_or_blots <>", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsGreaterThan(String value) {
            addCriterion("grouping_spout_or_blots >", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsGreaterThanOrEqualTo(String value) {
            addCriterion("grouping_spout_or_blots >=", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsLessThan(String value) {
            addCriterion("grouping_spout_or_blots <", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsLessThanOrEqualTo(String value) {
            addCriterion("grouping_spout_or_blots <=", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsLike(String value) {
            addCriterion("grouping_spout_or_blots like", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsNotLike(String value) {
            addCriterion("grouping_spout_or_blots not like", value, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsIn(List<String> values) {
            addCriterion("grouping_spout_or_blots in", values, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsNotIn(List<String> values) {
            addCriterion("grouping_spout_or_blots not in", values, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsBetween(String value1, String value2) {
            addCriterion("grouping_spout_or_blots between", value1, value2, "groupingSpoutOrBlots");
            return (Criteria) this;
        }

        public Criteria andGroupingSpoutOrBlotsNotBetween(String value1, String value2) {
            addCriterion("grouping_spout_or_blots not between", value1, value2, "groupingSpoutOrBlots");
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