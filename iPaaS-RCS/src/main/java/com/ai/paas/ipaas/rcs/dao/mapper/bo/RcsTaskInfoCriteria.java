package com.ai.paas.ipaas.rcs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RcsTaskInfoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public RcsTaskInfoCriteria() {
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

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNull() {
            addCriterion("cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(long value) {
            addCriterion("cluster_id =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(long value) {
            addCriterion("cluster_id <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(long value) {
            addCriterion("cluster_id >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(long value) {
            addCriterion("cluster_id >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(long value) {
            addCriterion("cluster_id <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(long value) {
            addCriterion("cluster_id <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<Long> values) {
            addCriterion("cluster_id in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<Long> values) {
            addCriterion("cluster_id not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(long value1, long value2) {
            addCriterion("cluster_id between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(long value1, long value2) {
            addCriterion("cluster_id not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andRegisterDtIsNull() {
            addCriterion("register_dt is null");
            return (Criteria) this;
        }

        public Criteria andRegisterDtIsNotNull() {
            addCriterion("register_dt is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterDtEqualTo(Timestamp value) {
            addCriterion("register_dt =", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtNotEqualTo(Timestamp value) {
            addCriterion("register_dt <>", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtGreaterThan(Timestamp value) {
            addCriterion("register_dt >", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("register_dt >=", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtLessThan(Timestamp value) {
            addCriterion("register_dt <", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtLessThanOrEqualTo(Timestamp value) {
            addCriterion("register_dt <=", value, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtIn(List<Timestamp> values) {
            addCriterion("register_dt in", values, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtNotIn(List<Timestamp> values) {
            addCriterion("register_dt not in", values, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtBetween(Timestamp value1, Timestamp value2) {
            addCriterion("register_dt between", value1, value2, "registerDt");
            return (Criteria) this;
        }

        public Criteria andRegisterDtNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("register_dt not between", value1, value2, "registerDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtIsNull() {
            addCriterion("cancel_dt is null");
            return (Criteria) this;
        }

        public Criteria andCancelDtIsNotNull() {
            addCriterion("cancel_dt is not null");
            return (Criteria) this;
        }

        public Criteria andCancelDtEqualTo(Timestamp value) {
            addCriterion("cancel_dt =", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtNotEqualTo(Timestamp value) {
            addCriterion("cancel_dt <>", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtGreaterThan(Timestamp value) {
            addCriterion("cancel_dt >", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("cancel_dt >=", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtLessThan(Timestamp value) {
            addCriterion("cancel_dt <", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtLessThanOrEqualTo(Timestamp value) {
            addCriterion("cancel_dt <=", value, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtIn(List<Timestamp> values) {
            addCriterion("cancel_dt in", values, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtNotIn(List<Timestamp> values) {
            addCriterion("cancel_dt not in", values, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtBetween(Timestamp value1, Timestamp value2) {
            addCriterion("cancel_dt between", value1, value2, "cancelDt");
            return (Criteria) this;
        }

        public Criteria andCancelDtNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("cancel_dt not between", value1, value2, "cancelDt");
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

        public Criteria andCommentsIsNull() {
            addCriterion("comments is null");
            return (Criteria) this;
        }

        public Criteria andCommentsIsNotNull() {
            addCriterion("comments is not null");
            return (Criteria) this;
        }

        public Criteria andCommentsEqualTo(String value) {
            addCriterion("comments =", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotEqualTo(String value) {
            addCriterion("comments <>", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsGreaterThan(String value) {
            addCriterion("comments >", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsGreaterThanOrEqualTo(String value) {
            addCriterion("comments >=", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLessThan(String value) {
            addCriterion("comments <", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLessThanOrEqualTo(String value) {
            addCriterion("comments <=", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsLike(String value) {
            addCriterion("comments like", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotLike(String value) {
            addCriterion("comments not like", value, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsIn(List<String> values) {
            addCriterion("comments in", values, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotIn(List<String> values) {
            addCriterion("comments not in", values, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsBetween(String value1, String value2) {
            addCriterion("comments between", value1, value2, "comments");
            return (Criteria) this;
        }

        public Criteria andCommentsNotBetween(String value1, String value2) {
            addCriterion("comments not between", value1, value2, "comments");
            return (Criteria) this;
        }

        public Criteria andNumWorkersIsNull() {
            addCriterion("num_workers is null");
            return (Criteria) this;
        }

        public Criteria andNumWorkersIsNotNull() {
            addCriterion("num_workers is not null");
            return (Criteria) this;
        }

        public Criteria andNumWorkersEqualTo(int value) {
            addCriterion("num_workers =", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersNotEqualTo(int value) {
            addCriterion("num_workers <>", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersGreaterThan(int value) {
            addCriterion("num_workers >", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersGreaterThanOrEqualTo(int value) {
            addCriterion("num_workers >=", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersLessThan(int value) {
            addCriterion("num_workers <", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersLessThanOrEqualTo(int value) {
            addCriterion("num_workers <=", value, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersIn(List<Integer> values) {
            addCriterion("num_workers in", values, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersNotIn(List<Integer> values) {
            addCriterion("num_workers not in", values, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersBetween(int value1, int value2) {
            addCriterion("num_workers between", value1, value2, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andNumWorkersNotBetween(int value1, int value2) {
            addCriterion("num_workers not between", value1, value2, "numWorkers");
            return (Criteria) this;
        }

        public Criteria andJarfilepathIsNull() {
            addCriterion("jarFilePath is null");
            return (Criteria) this;
        }

        public Criteria andJarfilepathIsNotNull() {
            addCriterion("jarFilePath is not null");
            return (Criteria) this;
        }

        public Criteria andJarfilepathEqualTo(String value) {
            addCriterion("jarFilePath =", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathNotEqualTo(String value) {
            addCriterion("jarFilePath <>", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathGreaterThan(String value) {
            addCriterion("jarFilePath >", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathGreaterThanOrEqualTo(String value) {
            addCriterion("jarFilePath >=", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathLessThan(String value) {
            addCriterion("jarFilePath <", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathLessThanOrEqualTo(String value) {
            addCriterion("jarFilePath <=", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathLike(String value) {
            addCriterion("jarFilePath like", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathNotLike(String value) {
            addCriterion("jarFilePath not like", value, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathIn(List<String> values) {
            addCriterion("jarFilePath in", values, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathNotIn(List<String> values) {
            addCriterion("jarFilePath not in", values, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathBetween(String value1, String value2) {
            addCriterion("jarFilePath between", value1, value2, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andJarfilepathNotBetween(String value1, String value2) {
            addCriterion("jarFilePath not between", value1, value2, "jarfilepath");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdIsNull() {
            addCriterion("register_user_id is null");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdIsNotNull() {
            addCriterion("register_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdEqualTo(String value) {
            addCriterion("register_user_id =", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdNotEqualTo(String value) {
            addCriterion("register_user_id <>", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdGreaterThan(String value) {
            addCriterion("register_user_id >", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("register_user_id >=", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdLessThan(String value) {
            addCriterion("register_user_id <", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdLessThanOrEqualTo(String value) {
            addCriterion("register_user_id <=", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdLike(String value) {
            addCriterion("register_user_id like", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdNotLike(String value) {
            addCriterion("register_user_id not like", value, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdIn(List<String> values) {
            addCriterion("register_user_id in", values, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdNotIn(List<String> values) {
            addCriterion("register_user_id not in", values, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdBetween(String value1, String value2) {
            addCriterion("register_user_id between", value1, value2, "registerUserId");
            return (Criteria) this;
        }

        public Criteria andRegisterUserIdNotBetween(String value1, String value2) {
            addCriterion("register_user_id not between", value1, value2, "registerUserId");
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