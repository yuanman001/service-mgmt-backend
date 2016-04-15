package com.ai.paas.ipaas.txs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TxsInstCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public TxsInstCriteria() {
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

        public Criteria andTxsInstIdIsNull() {
            addCriterion("txs_inst_id is null");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdIsNotNull() {
            addCriterion("txs_inst_id is not null");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdEqualTo(int value) {
            addCriterion("txs_inst_id =", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdNotEqualTo(int value) {
            addCriterion("txs_inst_id <>", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdGreaterThan(int value) {
            addCriterion("txs_inst_id >", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdGreaterThanOrEqualTo(int value) {
            addCriterion("txs_inst_id >=", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdLessThan(int value) {
            addCriterion("txs_inst_id <", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdLessThanOrEqualTo(int value) {
            addCriterion("txs_inst_id <=", value, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdIn(List<Integer> values) {
            addCriterion("txs_inst_id in", values, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdNotIn(List<Integer> values) {
            addCriterion("txs_inst_id not in", values, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdBetween(int value1, int value2) {
            addCriterion("txs_inst_id between", value1, value2, "txsInstId");
            return (Criteria) this;
        }

        public Criteria andTxsInstIdNotBetween(int value1, int value2) {
            addCriterion("txs_inst_id not between", value1, value2, "txsInstId");
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

        public Criteria andTxsResourceIdIsNull() {
            addCriterion("txs_resource_id is null");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdIsNotNull() {
            addCriterion("txs_resource_id is not null");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdEqualTo(int value) {
            addCriterion("txs_resource_id =", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdNotEqualTo(int value) {
            addCriterion("txs_resource_id <>", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdGreaterThan(int value) {
            addCriterion("txs_resource_id >", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdGreaterThanOrEqualTo(int value) {
            addCriterion("txs_resource_id >=", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdLessThan(int value) {
            addCriterion("txs_resource_id <", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdLessThanOrEqualTo(int value) {
            addCriterion("txs_resource_id <=", value, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdIn(List<Integer> values) {
            addCriterion("txs_resource_id in", values, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdNotIn(List<Integer> values) {
            addCriterion("txs_resource_id not in", values, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdBetween(int value1, int value2) {
            addCriterion("txs_resource_id between", value1, value2, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andTxsResourceIdNotBetween(int value1, int value2) {
            addCriterion("txs_resource_id not between", value1, value2, "txsResourceId");
            return (Criteria) this;
        }

        public Criteria andZkPathIsNull() {
            addCriterion("zk_path is null");
            return (Criteria) this;
        }

        public Criteria andZkPathIsNotNull() {
            addCriterion("zk_path is not null");
            return (Criteria) this;
        }

        public Criteria andZkPathEqualTo(String value) {
            addCriterion("zk_path =", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathNotEqualTo(String value) {
            addCriterion("zk_path <>", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathGreaterThan(String value) {
            addCriterion("zk_path >", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathGreaterThanOrEqualTo(String value) {
            addCriterion("zk_path >=", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathLessThan(String value) {
            addCriterion("zk_path <", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathLessThanOrEqualTo(String value) {
            addCriterion("zk_path <=", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathLike(String value) {
            addCriterion("zk_path like", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathNotLike(String value) {
            addCriterion("zk_path not like", value, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathIn(List<String> values) {
            addCriterion("zk_path in", values, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathNotIn(List<String> values) {
            addCriterion("zk_path not in", values, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathBetween(String value1, String value2) {
            addCriterion("zk_path between", value1, value2, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkPathNotBetween(String value1, String value2) {
            addCriterion("zk_path not between", value1, value2, "zkPath");
            return (Criteria) this;
        }

        public Criteria andZkNodeIsNull() {
            addCriterion("zk_node is null");
            return (Criteria) this;
        }

        public Criteria andZkNodeIsNotNull() {
            addCriterion("zk_node is not null");
            return (Criteria) this;
        }

        public Criteria andZkNodeEqualTo(String value) {
            addCriterion("zk_node =", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeNotEqualTo(String value) {
            addCriterion("zk_node <>", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeGreaterThan(String value) {
            addCriterion("zk_node >", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeGreaterThanOrEqualTo(String value) {
            addCriterion("zk_node >=", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeLessThan(String value) {
            addCriterion("zk_node <", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeLessThanOrEqualTo(String value) {
            addCriterion("zk_node <=", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeLike(String value) {
            addCriterion("zk_node like", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeNotLike(String value) {
            addCriterion("zk_node not like", value, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeIn(List<String> values) {
            addCriterion("zk_node in", values, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeNotIn(List<String> values) {
            addCriterion("zk_node not in", values, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeBetween(String value1, String value2) {
            addCriterion("zk_node between", value1, value2, "zkNode");
            return (Criteria) this;
        }

        public Criteria andZkNodeNotBetween(String value1, String value2) {
            addCriterion("zk_node not between", value1, value2, "zkNode");
            return (Criteria) this;
        }

        public Criteria andInstStateIsNull() {
            addCriterion("inst_state is null");
            return (Criteria) this;
        }

        public Criteria andInstStateIsNotNull() {
            addCriterion("inst_state is not null");
            return (Criteria) this;
        }

        public Criteria andInstStateEqualTo(int value) {
            addCriterion("inst_state =", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateNotEqualTo(int value) {
            addCriterion("inst_state <>", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateGreaterThan(int value) {
            addCriterion("inst_state >", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateGreaterThanOrEqualTo(int value) {
            addCriterion("inst_state >=", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateLessThan(int value) {
            addCriterion("inst_state <", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateLessThanOrEqualTo(int value) {
            addCriterion("inst_state <=", value, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateIn(List<Integer> values) {
            addCriterion("inst_state in", values, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateNotIn(List<Integer> values) {
            addCriterion("inst_state not in", values, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateBetween(int value1, int value2) {
            addCriterion("inst_state between", value1, value2, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateNotBetween(int value1, int value2) {
            addCriterion("inst_state not between", value1, value2, "instState");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeIsNull() {
            addCriterion("inst_state_time is null");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeIsNotNull() {
            addCriterion("inst_state_time is not null");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeEqualTo(Timestamp value) {
            addCriterion("inst_state_time =", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeNotEqualTo(Timestamp value) {
            addCriterion("inst_state_time <>", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeGreaterThan(Timestamp value) {
            addCriterion("inst_state_time >", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("inst_state_time >=", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeLessThan(Timestamp value) {
            addCriterion("inst_state_time <", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("inst_state_time <=", value, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeIn(List<Timestamp> values) {
            addCriterion("inst_state_time in", values, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeNotIn(List<Timestamp> values) {
            addCriterion("inst_state_time not in", values, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("inst_state_time between", value1, value2, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andInstStateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("inst_state_time not between", value1, value2, "instStateTime");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNull() {
            addCriterion("remarks is null");
            return (Criteria) this;
        }

        public Criteria andRemarksIsNotNull() {
            addCriterion("remarks is not null");
            return (Criteria) this;
        }

        public Criteria andRemarksEqualTo(String value) {
            addCriterion("remarks =", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotEqualTo(String value) {
            addCriterion("remarks <>", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThan(String value) {
            addCriterion("remarks >", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksGreaterThanOrEqualTo(String value) {
            addCriterion("remarks >=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThan(String value) {
            addCriterion("remarks <", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLessThanOrEqualTo(String value) {
            addCriterion("remarks <=", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksLike(String value) {
            addCriterion("remarks like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotLike(String value) {
            addCriterion("remarks not like", value, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksIn(List<String> values) {
            addCriterion("remarks in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotIn(List<String> values) {
            addCriterion("remarks not in", values, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksBetween(String value1, String value2) {
            addCriterion("remarks between", value1, value2, "remarks");
            return (Criteria) this;
        }

        public Criteria andRemarksNotBetween(String value1, String value2) {
            addCriterion("remarks not between", value1, value2, "remarks");
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