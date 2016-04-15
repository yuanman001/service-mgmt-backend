package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbsUserServiceCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public DbsUserServiceCriteria() {
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

        public Criteria andUserServiceIdIsNull() {
            addCriterion("user_service_id is null");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdIsNotNull() {
            addCriterion("user_service_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdEqualTo(String value) {
            addCriterion("user_service_id =", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdNotEqualTo(String value) {
            addCriterion("user_service_id <>", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdGreaterThan(String value) {
            addCriterion("user_service_id >", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_service_id >=", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdLessThan(String value) {
            addCriterion("user_service_id <", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdLessThanOrEqualTo(String value) {
            addCriterion("user_service_id <=", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdLike(String value) {
            addCriterion("user_service_id like", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdNotLike(String value) {
            addCriterion("user_service_id not like", value, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdIn(List<String> values) {
            addCriterion("user_service_id in", values, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdNotIn(List<String> values) {
            addCriterion("user_service_id not in", values, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdBetween(String value1, String value2) {
            addCriterion("user_service_id between", value1, value2, "userServiceId");
            return (Criteria) this;
        }

        public Criteria andUserServiceIdNotBetween(String value1, String value2) {
            addCriterion("user_service_id not between", value1, value2, "userServiceId");
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

        public Criteria andIsTxsIsNull() {
            addCriterion("is_txs is null");
            return (Criteria) this;
        }

        public Criteria andIsTxsIsNotNull() {
            addCriterion("is_txs is not null");
            return (Criteria) this;
        }

        public Criteria andIsTxsEqualTo(String value) {
            addCriterion("is_txs =", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsNotEqualTo(String value) {
            addCriterion("is_txs <>", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsGreaterThan(String value) {
            addCriterion("is_txs >", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsGreaterThanOrEqualTo(String value) {
            addCriterion("is_txs >=", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsLessThan(String value) {
            addCriterion("is_txs <", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsLessThanOrEqualTo(String value) {
            addCriterion("is_txs <=", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsLike(String value) {
            addCriterion("is_txs like", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsNotLike(String value) {
            addCriterion("is_txs not like", value, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsIn(List<String> values) {
            addCriterion("is_txs in", values, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsNotIn(List<String> values) {
            addCriterion("is_txs not in", values, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsBetween(String value1, String value2) {
            addCriterion("is_txs between", value1, value2, "isTxs");
            return (Criteria) this;
        }

        public Criteria andIsTxsNotBetween(String value1, String value2) {
            addCriterion("is_txs not between", value1, value2, "isTxs");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdIsNull() {
            addCriterion("seq_db_id is null");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdIsNotNull() {
            addCriterion("seq_db_id is not null");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdEqualTo(Integer value) {
            addCriterion("seq_db_id =", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdNotEqualTo(Integer value) {
            addCriterion("seq_db_id <>", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdGreaterThan(Integer value) {
            addCriterion("seq_db_id >", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq_db_id >=", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdLessThan(Integer value) {
            addCriterion("seq_db_id <", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdLessThanOrEqualTo(Integer value) {
            addCriterion("seq_db_id <=", value, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdIn(List<Integer> values) {
            addCriterion("seq_db_id in", values, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdNotIn(List<Integer> values) {
            addCriterion("seq_db_id not in", values, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdBetween(Integer value1, Integer value2) {
            addCriterion("seq_db_id between", value1, value2, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andSeqDbIdNotBetween(Integer value1, Integer value2) {
            addCriterion("seq_db_id not between", value1, value2, "seqDbId");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyIsNull() {
            addCriterion("is_mysql_proxy is null");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyIsNotNull() {
            addCriterion("is_mysql_proxy is not null");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyEqualTo(String value) {
            addCriterion("is_mysql_proxy =", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyNotEqualTo(String value) {
            addCriterion("is_mysql_proxy <>", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyGreaterThan(String value) {
            addCriterion("is_mysql_proxy >", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyGreaterThanOrEqualTo(String value) {
            addCriterion("is_mysql_proxy >=", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyLessThan(String value) {
            addCriterion("is_mysql_proxy <", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyLessThanOrEqualTo(String value) {
            addCriterion("is_mysql_proxy <=", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyLike(String value) {
            addCriterion("is_mysql_proxy like", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyNotLike(String value) {
            addCriterion("is_mysql_proxy not like", value, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyIn(List<String> values) {
            addCriterion("is_mysql_proxy in", values, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyNotIn(List<String> values) {
            addCriterion("is_mysql_proxy not in", values, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyBetween(String value1, String value2) {
            addCriterion("is_mysql_proxy between", value1, value2, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsMysqlProxyNotBetween(String value1, String value2) {
            addCriterion("is_mysql_proxy not between", value1, value2, "isMysqlProxy");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchIsNull() {
            addCriterion("is_autoswitch is null");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchIsNotNull() {
            addCriterion("is_autoswitch is not null");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchEqualTo(String value) {
            addCriterion("is_autoswitch =", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchNotEqualTo(String value) {
            addCriterion("is_autoswitch <>", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchGreaterThan(String value) {
            addCriterion("is_autoswitch >", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchGreaterThanOrEqualTo(String value) {
            addCriterion("is_autoswitch >=", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchLessThan(String value) {
            addCriterion("is_autoswitch <", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchLessThanOrEqualTo(String value) {
            addCriterion("is_autoswitch <=", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchLike(String value) {
            addCriterion("is_autoswitch like", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchNotLike(String value) {
            addCriterion("is_autoswitch not like", value, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchIn(List<String> values) {
            addCriterion("is_autoswitch in", values, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchNotIn(List<String> values) {
            addCriterion("is_autoswitch not in", values, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchBetween(String value1, String value2) {
            addCriterion("is_autoswitch between", value1, value2, "isAutoswitch");
            return (Criteria) this;
        }

        public Criteria andIsAutoswitchNotBetween(String value1, String value2) {
            addCriterion("is_autoswitch not between", value1, value2, "isAutoswitch");
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