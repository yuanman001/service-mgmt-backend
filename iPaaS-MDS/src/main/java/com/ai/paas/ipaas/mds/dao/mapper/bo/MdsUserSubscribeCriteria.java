package com.ai.paas.ipaas.mds.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MdsUserSubscribeCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public MdsUserSubscribeCriteria() {
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

        public Criteria andSubscribeIdIsNull() {
            addCriterion("subscribe_id is null");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdIsNotNull() {
            addCriterion("subscribe_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdEqualTo(Integer value) {
            addCriterion("subscribe_id =", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdNotEqualTo(Integer value) {
            addCriterion("subscribe_id <>", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdGreaterThan(Integer value) {
            addCriterion("subscribe_id >", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("subscribe_id >=", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdLessThan(Integer value) {
            addCriterion("subscribe_id <", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdLessThanOrEqualTo(Integer value) {
            addCriterion("subscribe_id <=", value, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdIn(List<Integer> values) {
            addCriterion("subscribe_id in", values, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdNotIn(List<Integer> values) {
            addCriterion("subscribe_id not in", values, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_id between", value1, value2, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("subscribe_id not between", value1, value2, "subscribeId");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameIsNull() {
            addCriterion("subscribe_name is null");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameIsNotNull() {
            addCriterion("subscribe_name is not null");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameEqualTo(String value) {
            addCriterion("subscribe_name =", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameNotEqualTo(String value) {
            addCriterion("subscribe_name <>", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameGreaterThan(String value) {
            addCriterion("subscribe_name >", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameGreaterThanOrEqualTo(String value) {
            addCriterion("subscribe_name >=", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameLessThan(String value) {
            addCriterion("subscribe_name <", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameLessThanOrEqualTo(String value) {
            addCriterion("subscribe_name <=", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameLike(String value) {
            addCriterion("subscribe_name like", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameNotLike(String value) {
            addCriterion("subscribe_name not like", value, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameIn(List<String> values) {
            addCriterion("subscribe_name in", values, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameNotIn(List<String> values) {
            addCriterion("subscribe_name not in", values, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameBetween(String value1, String value2) {
            addCriterion("subscribe_name between", value1, value2, "subscribeName");
            return (Criteria) this;
        }

        public Criteria andSubscribeNameNotBetween(String value1, String value2) {
            addCriterion("subscribe_name not between", value1, value2, "subscribeName");
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

        public Criteria andTopicEnNameIsNull() {
            addCriterion("topic_en_name is null");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameIsNotNull() {
            addCriterion("topic_en_name is not null");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameEqualTo(String value) {
            addCriterion("topic_en_name =", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameNotEqualTo(String value) {
            addCriterion("topic_en_name <>", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameGreaterThan(String value) {
            addCriterion("topic_en_name >", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameGreaterThanOrEqualTo(String value) {
            addCriterion("topic_en_name >=", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameLessThan(String value) {
            addCriterion("topic_en_name <", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameLessThanOrEqualTo(String value) {
            addCriterion("topic_en_name <=", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameLike(String value) {
            addCriterion("topic_en_name like", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameNotLike(String value) {
            addCriterion("topic_en_name not like", value, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameIn(List<String> values) {
            addCriterion("topic_en_name in", values, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameNotIn(List<String> values) {
            addCriterion("topic_en_name not in", values, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameBetween(String value1, String value2) {
            addCriterion("topic_en_name between", value1, value2, "topicEnName");
            return (Criteria) this;
        }

        public Criteria andTopicEnNameNotBetween(String value1, String value2) {
            addCriterion("topic_en_name not between", value1, value2, "topicEnName");
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