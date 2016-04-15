package com.ai.paas.ipaas.ats.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AtsResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public AtsResourcePoolCriteria() {
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

        public Criteria andAtsResourceIdIsNull() {
            addCriterion("ats_resource_id is null");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdIsNotNull() {
            addCriterion("ats_resource_id is not null");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdEqualTo(int value) {
            addCriterion("ats_resource_id =", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdNotEqualTo(int value) {
            addCriterion("ats_resource_id <>", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdGreaterThan(int value) {
            addCriterion("ats_resource_id >", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdGreaterThanOrEqualTo(int value) {
            addCriterion("ats_resource_id >=", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdLessThan(int value) {
            addCriterion("ats_resource_id <", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdLessThanOrEqualTo(int value) {
            addCriterion("ats_resource_id <=", value, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdIn(List<Integer> values) {
            addCriterion("ats_resource_id in", values, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdNotIn(List<Integer> values) {
            addCriterion("ats_resource_id not in", values, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdBetween(int value1, int value2) {
            addCriterion("ats_resource_id between", value1, value2, "atsResourceId");
            return (Criteria) this;
        }

        public Criteria andAtsResourceIdNotBetween(int value1, int value2) {
            addCriterion("ats_resource_id not between", value1, value2, "atsResourceId");
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

        public Criteria andZkProducerPathIsNull() {
            addCriterion("zk_producer_path is null");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathIsNotNull() {
            addCriterion("zk_producer_path is not null");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathEqualTo(String value) {
            addCriterion("zk_producer_path =", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathNotEqualTo(String value) {
            addCriterion("zk_producer_path <>", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathGreaterThan(String value) {
            addCriterion("zk_producer_path >", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathGreaterThanOrEqualTo(String value) {
            addCriterion("zk_producer_path >=", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathLessThan(String value) {
            addCriterion("zk_producer_path <", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathLessThanOrEqualTo(String value) {
            addCriterion("zk_producer_path <=", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathLike(String value) {
            addCriterion("zk_producer_path like", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathNotLike(String value) {
            addCriterion("zk_producer_path not like", value, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathIn(List<String> values) {
            addCriterion("zk_producer_path in", values, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathNotIn(List<String> values) {
            addCriterion("zk_producer_path not in", values, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathBetween(String value1, String value2) {
            addCriterion("zk_producer_path between", value1, value2, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerPathNotBetween(String value1, String value2) {
            addCriterion("zk_producer_path not between", value1, value2, "zkProducerPath");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeIsNull() {
            addCriterion("zk_producer_node is null");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeIsNotNull() {
            addCriterion("zk_producer_node is not null");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeEqualTo(String value) {
            addCriterion("zk_producer_node =", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeNotEqualTo(String value) {
            addCriterion("zk_producer_node <>", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeGreaterThan(String value) {
            addCriterion("zk_producer_node >", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeGreaterThanOrEqualTo(String value) {
            addCriterion("zk_producer_node >=", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeLessThan(String value) {
            addCriterion("zk_producer_node <", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeLessThanOrEqualTo(String value) {
            addCriterion("zk_producer_node <=", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeLike(String value) {
            addCriterion("zk_producer_node like", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeNotLike(String value) {
            addCriterion("zk_producer_node not like", value, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeIn(List<String> values) {
            addCriterion("zk_producer_node in", values, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeNotIn(List<String> values) {
            addCriterion("zk_producer_node not in", values, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeBetween(String value1, String value2) {
            addCriterion("zk_producer_node between", value1, value2, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkProducerNodeNotBetween(String value1, String value2) {
            addCriterion("zk_producer_node not between", value1, value2, "zkProducerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathIsNull() {
            addCriterion("zk_consumer_path is null");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathIsNotNull() {
            addCriterion("zk_consumer_path is not null");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathEqualTo(String value) {
            addCriterion("zk_consumer_path =", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathNotEqualTo(String value) {
            addCriterion("zk_consumer_path <>", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathGreaterThan(String value) {
            addCriterion("zk_consumer_path >", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathGreaterThanOrEqualTo(String value) {
            addCriterion("zk_consumer_path >=", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathLessThan(String value) {
            addCriterion("zk_consumer_path <", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathLessThanOrEqualTo(String value) {
            addCriterion("zk_consumer_path <=", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathLike(String value) {
            addCriterion("zk_consumer_path like", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathNotLike(String value) {
            addCriterion("zk_consumer_path not like", value, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathIn(List<String> values) {
            addCriterion("zk_consumer_path in", values, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathNotIn(List<String> values) {
            addCriterion("zk_consumer_path not in", values, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathBetween(String value1, String value2) {
            addCriterion("zk_consumer_path between", value1, value2, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerPathNotBetween(String value1, String value2) {
            addCriterion("zk_consumer_path not between", value1, value2, "zkConsumerPath");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeIsNull() {
            addCriterion("zk_consumer_node is null");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeIsNotNull() {
            addCriterion("zk_consumer_node is not null");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeEqualTo(String value) {
            addCriterion("zk_consumer_node =", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeNotEqualTo(String value) {
            addCriterion("zk_consumer_node <>", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeGreaterThan(String value) {
            addCriterion("zk_consumer_node >", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeGreaterThanOrEqualTo(String value) {
            addCriterion("zk_consumer_node >=", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeLessThan(String value) {
            addCriterion("zk_consumer_node <", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeLessThanOrEqualTo(String value) {
            addCriterion("zk_consumer_node <=", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeLike(String value) {
            addCriterion("zk_consumer_node like", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeNotLike(String value) {
            addCriterion("zk_consumer_node not like", value, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeIn(List<String> values) {
            addCriterion("zk_consumer_node in", values, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeNotIn(List<String> values) {
            addCriterion("zk_consumer_node not in", values, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeBetween(String value1, String value2) {
            addCriterion("zk_consumer_node between", value1, value2, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkConsumerNodeNotBetween(String value1, String value2) {
            addCriterion("zk_consumer_node not between", value1, value2, "zkConsumerNode");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathIsNull() {
            addCriterion("zk_topic_path is null");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathIsNotNull() {
            addCriterion("zk_topic_path is not null");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathEqualTo(String value) {
            addCriterion("zk_topic_path =", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathNotEqualTo(String value) {
            addCriterion("zk_topic_path <>", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathGreaterThan(String value) {
            addCriterion("zk_topic_path >", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathGreaterThanOrEqualTo(String value) {
            addCriterion("zk_topic_path >=", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathLessThan(String value) {
            addCriterion("zk_topic_path <", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathLessThanOrEqualTo(String value) {
            addCriterion("zk_topic_path <=", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathLike(String value) {
            addCriterion("zk_topic_path like", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathNotLike(String value) {
            addCriterion("zk_topic_path not like", value, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathIn(List<String> values) {
            addCriterion("zk_topic_path in", values, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathNotIn(List<String> values) {
            addCriterion("zk_topic_path not in", values, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathBetween(String value1, String value2) {
            addCriterion("zk_topic_path between", value1, value2, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andZkTopicPathNotBetween(String value1, String value2) {
            addCriterion("zk_topic_path not between", value1, value2, "zkTopicPath");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandIsNull() {
            addCriterion("kafka_create_command is null");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandIsNotNull() {
            addCriterion("kafka_create_command is not null");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandEqualTo(String value) {
            addCriterion("kafka_create_command =", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandNotEqualTo(String value) {
            addCriterion("kafka_create_command <>", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandGreaterThan(String value) {
            addCriterion("kafka_create_command >", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandGreaterThanOrEqualTo(String value) {
            addCriterion("kafka_create_command >=", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandLessThan(String value) {
            addCriterion("kafka_create_command <", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandLessThanOrEqualTo(String value) {
            addCriterion("kafka_create_command <=", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandLike(String value) {
            addCriterion("kafka_create_command like", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandNotLike(String value) {
            addCriterion("kafka_create_command not like", value, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandIn(List<String> values) {
            addCriterion("kafka_create_command in", values, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandNotIn(List<String> values) {
            addCriterion("kafka_create_command not in", values, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandBetween(String value1, String value2) {
            addCriterion("kafka_create_command between", value1, value2, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andKafkaCreateCommandNotBetween(String value1, String value2) {
            addCriterion("kafka_create_command not between", value1, value2, "kafkaCreateCommand");
            return (Criteria) this;
        }

        public Criteria andResourceStateIsNull() {
            addCriterion("resource_state is null");
            return (Criteria) this;
        }

        public Criteria andResourceStateIsNotNull() {
            addCriterion("resource_state is not null");
            return (Criteria) this;
        }

        public Criteria andResourceStateEqualTo(int value) {
            addCriterion("resource_state =", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateNotEqualTo(int value) {
            addCriterion("resource_state <>", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateGreaterThan(int value) {
            addCriterion("resource_state >", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateGreaterThanOrEqualTo(int value) {
            addCriterion("resource_state >=", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateLessThan(int value) {
            addCriterion("resource_state <", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateLessThanOrEqualTo(int value) {
            addCriterion("resource_state <=", value, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateIn(List<Integer> values) {
            addCriterion("resource_state in", values, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateNotIn(List<Integer> values) {
            addCriterion("resource_state not in", values, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateBetween(int value1, int value2) {
            addCriterion("resource_state between", value1, value2, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceStateNotBetween(int value1, int value2) {
            addCriterion("resource_state not between", value1, value2, "resourceState");
            return (Criteria) this;
        }

        public Criteria andResourceTimeIsNull() {
            addCriterion("resource_time is null");
            return (Criteria) this;
        }

        public Criteria andResourceTimeIsNotNull() {
            addCriterion("resource_time is not null");
            return (Criteria) this;
        }

        public Criteria andResourceTimeEqualTo(Timestamp value) {
            addCriterion("resource_time =", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeNotEqualTo(Timestamp value) {
            addCriterion("resource_time <>", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeGreaterThan(Timestamp value) {
            addCriterion("resource_time >", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("resource_time >=", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeLessThan(Timestamp value) {
            addCriterion("resource_time <", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("resource_time <=", value, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeIn(List<Timestamp> values) {
            addCriterion("resource_time in", values, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeNotIn(List<Timestamp> values) {
            addCriterion("resource_time not in", values, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("resource_time between", value1, value2, "resourceTime");
            return (Criteria) this;
        }

        public Criteria andResourceTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("resource_time not between", value1, value2, "resourceTime");
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