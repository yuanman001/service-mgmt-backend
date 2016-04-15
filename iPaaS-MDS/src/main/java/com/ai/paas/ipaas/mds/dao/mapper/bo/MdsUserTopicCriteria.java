package com.ai.paas.ipaas.mds.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MdsUserTopicCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public MdsUserTopicCriteria() {
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

        public Criteria andTopicInstIdIsNull() {
            addCriterion("topic_inst_id is null");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdIsNotNull() {
            addCriterion("topic_inst_id is not null");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdEqualTo(Integer value) {
            addCriterion("topic_inst_id =", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdNotEqualTo(Integer value) {
            addCriterion("topic_inst_id <>", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdGreaterThan(Integer value) {
            addCriterion("topic_inst_id >", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("topic_inst_id >=", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdLessThan(Integer value) {
            addCriterion("topic_inst_id <", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdLessThanOrEqualTo(Integer value) {
            addCriterion("topic_inst_id <=", value, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdIn(List<Integer> values) {
            addCriterion("topic_inst_id in", values, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdNotIn(List<Integer> values) {
            addCriterion("topic_inst_id not in", values, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdBetween(Integer value1, Integer value2) {
            addCriterion("topic_inst_id between", value1, value2, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andTopicInstIdNotBetween(Integer value1, Integer value2) {
            addCriterion("topic_inst_id not between", value1, value2, "topicInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdIsNull() {
            addCriterion("srv_inst_id is null");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdIsNotNull() {
            addCriterion("srv_inst_id is not null");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdEqualTo(Integer value) {
            addCriterion("srv_inst_id =", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdNotEqualTo(Integer value) {
            addCriterion("srv_inst_id <>", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdGreaterThan(Integer value) {
            addCriterion("srv_inst_id >", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("srv_inst_id >=", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdLessThan(Integer value) {
            addCriterion("srv_inst_id <", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdLessThanOrEqualTo(Integer value) {
            addCriterion("srv_inst_id <=", value, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdIn(List<Integer> values) {
            addCriterion("srv_inst_id in", values, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdNotIn(List<Integer> values) {
            addCriterion("srv_inst_id not in", values, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdBetween(Integer value1, Integer value2) {
            addCriterion("srv_inst_id between", value1, value2, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andSrvInstIdNotBetween(Integer value1, Integer value2) {
            addCriterion("srv_inst_id not between", value1, value2, "srvInstId");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameIsNull() {
            addCriterion("topic_display_name is null");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameIsNotNull() {
            addCriterion("topic_display_name is not null");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameEqualTo(String value) {
            addCriterion("topic_display_name =", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameNotEqualTo(String value) {
            addCriterion("topic_display_name <>", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameGreaterThan(String value) {
            addCriterion("topic_display_name >", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameGreaterThanOrEqualTo(String value) {
            addCriterion("topic_display_name >=", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameLessThan(String value) {
            addCriterion("topic_display_name <", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameLessThanOrEqualTo(String value) {
            addCriterion("topic_display_name <=", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameLike(String value) {
            addCriterion("topic_display_name like", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameNotLike(String value) {
            addCriterion("topic_display_name not like", value, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameIn(List<String> values) {
            addCriterion("topic_display_name in", values, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameNotIn(List<String> values) {
            addCriterion("topic_display_name not in", values, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameBetween(String value1, String value2) {
            addCriterion("topic_display_name between", value1, value2, "topicDisplayName");
            return (Criteria) this;
        }

        public Criteria andTopicDisplayNameNotBetween(String value1, String value2) {
            addCriterion("topic_display_name not between", value1, value2, "topicDisplayName");
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

        public Criteria andUserSrvIdIsNull() {
            addCriterion("user_srv_id is null");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdIsNotNull() {
            addCriterion("user_srv_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdEqualTo(String value) {
            addCriterion("user_srv_id =", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdNotEqualTo(String value) {
            addCriterion("user_srv_id <>", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdGreaterThan(String value) {
            addCriterion("user_srv_id >", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_srv_id >=", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdLessThan(String value) {
            addCriterion("user_srv_id <", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdLessThanOrEqualTo(String value) {
            addCriterion("user_srv_id <=", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdLike(String value) {
            addCriterion("user_srv_id like", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdNotLike(String value) {
            addCriterion("user_srv_id not like", value, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdIn(List<String> values) {
            addCriterion("user_srv_id in", values, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdNotIn(List<String> values) {
            addCriterion("user_srv_id not in", values, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdBetween(String value1, String value2) {
            addCriterion("user_srv_id between", value1, value2, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andUserSrvIdNotBetween(String value1, String value2) {
            addCriterion("user_srv_id not between", value1, value2, "userSrvId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdIsNull() {
            addCriterion("mds_cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdIsNotNull() {
            addCriterion("mds_cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdEqualTo(Integer value) {
            addCriterion("mds_cluster_id =", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdNotEqualTo(Integer value) {
            addCriterion("mds_cluster_id <>", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdGreaterThan(Integer value) {
            addCriterion("mds_cluster_id >", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("mds_cluster_id >=", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdLessThan(Integer value) {
            addCriterion("mds_cluster_id <", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdLessThanOrEqualTo(Integer value) {
            addCriterion("mds_cluster_id <=", value, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdIn(List<Integer> values) {
            addCriterion("mds_cluster_id in", values, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdNotIn(List<Integer> values) {
            addCriterion("mds_cluster_id not in", values, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdBetween(Integer value1, Integer value2) {
            addCriterion("mds_cluster_id between", value1, value2, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andMdsClusterIdNotBetween(Integer value1, Integer value2) {
            addCriterion("mds_cluster_id not between", value1, value2, "mdsClusterId");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathIsNull() {
            addCriterion("producer_config_path is null");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathIsNotNull() {
            addCriterion("producer_config_path is not null");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathEqualTo(String value) {
            addCriterion("producer_config_path =", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathNotEqualTo(String value) {
            addCriterion("producer_config_path <>", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathGreaterThan(String value) {
            addCriterion("producer_config_path >", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathGreaterThanOrEqualTo(String value) {
            addCriterion("producer_config_path >=", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathLessThan(String value) {
            addCriterion("producer_config_path <", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathLessThanOrEqualTo(String value) {
            addCriterion("producer_config_path <=", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathLike(String value) {
            addCriterion("producer_config_path like", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathNotLike(String value) {
            addCriterion("producer_config_path not like", value, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathIn(List<String> values) {
            addCriterion("producer_config_path in", values, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathNotIn(List<String> values) {
            addCriterion("producer_config_path not in", values, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathBetween(String value1, String value2) {
            addCriterion("producer_config_path between", value1, value2, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigPathNotBetween(String value1, String value2) {
            addCriterion("producer_config_path not between", value1, value2, "producerConfigPath");
            return (Criteria) this;
        }

        public Criteria andProducerConfigIsNull() {
            addCriterion("producer_config is null");
            return (Criteria) this;
        }

        public Criteria andProducerConfigIsNotNull() {
            addCriterion("producer_config is not null");
            return (Criteria) this;
        }

        public Criteria andProducerConfigEqualTo(String value) {
            addCriterion("producer_config =", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigNotEqualTo(String value) {
            addCriterion("producer_config <>", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigGreaterThan(String value) {
            addCriterion("producer_config >", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigGreaterThanOrEqualTo(String value) {
            addCriterion("producer_config >=", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigLessThan(String value) {
            addCriterion("producer_config <", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigLessThanOrEqualTo(String value) {
            addCriterion("producer_config <=", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigLike(String value) {
            addCriterion("producer_config like", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigNotLike(String value) {
            addCriterion("producer_config not like", value, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigIn(List<String> values) {
            addCriterion("producer_config in", values, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigNotIn(List<String> values) {
            addCriterion("producer_config not in", values, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigBetween(String value1, String value2) {
            addCriterion("producer_config between", value1, value2, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andProducerConfigNotBetween(String value1, String value2) {
            addCriterion("producer_config not between", value1, value2, "producerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathIsNull() {
            addCriterion("consumer_config_path is null");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathIsNotNull() {
            addCriterion("consumer_config_path is not null");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathEqualTo(String value) {
            addCriterion("consumer_config_path =", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathNotEqualTo(String value) {
            addCriterion("consumer_config_path <>", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathGreaterThan(String value) {
            addCriterion("consumer_config_path >", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathGreaterThanOrEqualTo(String value) {
            addCriterion("consumer_config_path >=", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathLessThan(String value) {
            addCriterion("consumer_config_path <", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathLessThanOrEqualTo(String value) {
            addCriterion("consumer_config_path <=", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathLike(String value) {
            addCriterion("consumer_config_path like", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathNotLike(String value) {
            addCriterion("consumer_config_path not like", value, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathIn(List<String> values) {
            addCriterion("consumer_config_path in", values, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathNotIn(List<String> values) {
            addCriterion("consumer_config_path not in", values, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathBetween(String value1, String value2) {
            addCriterion("consumer_config_path between", value1, value2, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigPathNotBetween(String value1, String value2) {
            addCriterion("consumer_config_path not between", value1, value2, "consumerConfigPath");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigIsNull() {
            addCriterion("consumer_config is null");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigIsNotNull() {
            addCriterion("consumer_config is not null");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigEqualTo(String value) {
            addCriterion("consumer_config =", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigNotEqualTo(String value) {
            addCriterion("consumer_config <>", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigGreaterThan(String value) {
            addCriterion("consumer_config >", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigGreaterThanOrEqualTo(String value) {
            addCriterion("consumer_config >=", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigLessThan(String value) {
            addCriterion("consumer_config <", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigLessThanOrEqualTo(String value) {
            addCriterion("consumer_config <=", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigLike(String value) {
            addCriterion("consumer_config like", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigNotLike(String value) {
            addCriterion("consumer_config not like", value, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigIn(List<String> values) {
            addCriterion("consumer_config in", values, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigNotIn(List<String> values) {
            addCriterion("consumer_config not in", values, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigBetween(String value1, String value2) {
            addCriterion("consumer_config between", value1, value2, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andConsumerConfigNotBetween(String value1, String value2) {
            addCriterion("consumer_config not between", value1, value2, "consumerConfig");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsIsNull() {
            addCriterion("topic_partitions is null");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsIsNotNull() {
            addCriterion("topic_partitions is not null");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsEqualTo(Integer value) {
            addCriterion("topic_partitions =", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsNotEqualTo(Integer value) {
            addCriterion("topic_partitions <>", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsGreaterThan(Integer value) {
            addCriterion("topic_partitions >", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsGreaterThanOrEqualTo(Integer value) {
            addCriterion("topic_partitions >=", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsLessThan(Integer value) {
            addCriterion("topic_partitions <", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsLessThanOrEqualTo(Integer value) {
            addCriterion("topic_partitions <=", value, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsIn(List<Integer> values) {
            addCriterion("topic_partitions in", values, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsNotIn(List<Integer> values) {
            addCriterion("topic_partitions not in", values, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsBetween(Integer value1, Integer value2) {
            addCriterion("topic_partitions between", value1, value2, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andTopicPartitionsNotBetween(Integer value1, Integer value2) {
            addCriterion("topic_partitions not between", value1, value2, "topicPartitions");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasIsNull() {
            addCriterion("msg_replicas is null");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasIsNotNull() {
            addCriterion("msg_replicas is not null");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasEqualTo(Integer value) {
            addCriterion("msg_replicas =", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasNotEqualTo(Integer value) {
            addCriterion("msg_replicas <>", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasGreaterThan(Integer value) {
            addCriterion("msg_replicas >", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasGreaterThanOrEqualTo(Integer value) {
            addCriterion("msg_replicas >=", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasLessThan(Integer value) {
            addCriterion("msg_replicas <", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasLessThanOrEqualTo(Integer value) {
            addCriterion("msg_replicas <=", value, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasIn(List<Integer> values) {
            addCriterion("msg_replicas in", values, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasNotIn(List<Integer> values) {
            addCriterion("msg_replicas not in", values, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasBetween(Integer value1, Integer value2) {
            addCriterion("msg_replicas between", value1, value2, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andMsgReplicasNotBetween(Integer value1, Integer value2) {
            addCriterion("msg_replicas not between", value1, value2, "msgReplicas");
            return (Criteria) this;
        }

        public Criteria andReamrkIsNull() {
            addCriterion("reamrk is null");
            return (Criteria) this;
        }

        public Criteria andReamrkIsNotNull() {
            addCriterion("reamrk is not null");
            return (Criteria) this;
        }

        public Criteria andReamrkEqualTo(String value) {
            addCriterion("reamrk =", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkNotEqualTo(String value) {
            addCriterion("reamrk <>", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkGreaterThan(String value) {
            addCriterion("reamrk >", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkGreaterThanOrEqualTo(String value) {
            addCriterion("reamrk >=", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkLessThan(String value) {
            addCriterion("reamrk <", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkLessThanOrEqualTo(String value) {
            addCriterion("reamrk <=", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkLike(String value) {
            addCriterion("reamrk like", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkNotLike(String value) {
            addCriterion("reamrk not like", value, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkIn(List<String> values) {
            addCriterion("reamrk in", values, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkNotIn(List<String> values) {
            addCriterion("reamrk not in", values, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkBetween(String value1, String value2) {
            addCriterion("reamrk between", value1, value2, "reamrk");
            return (Criteria) this;
        }

        public Criteria andReamrkNotBetween(String value1, String value2) {
            addCriterion("reamrk not between", value1, value2, "reamrk");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(Integer value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(Integer value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(Integer value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(Integer value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(Integer value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<Integer> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<Integer> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(Integer value1, Integer value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(Integer value1, Integer value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNull() {
            addCriterion("operator_id is null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIsNotNull() {
            addCriterion("operator_id is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorIdEqualTo(String value) {
            addCriterion("operator_id =", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotEqualTo(String value) {
            addCriterion("operator_id <>", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThan(String value) {
            addCriterion("operator_id >", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdGreaterThanOrEqualTo(String value) {
            addCriterion("operator_id >=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThan(String value) {
            addCriterion("operator_id <", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLessThanOrEqualTo(String value) {
            addCriterion("operator_id <=", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdLike(String value) {
            addCriterion("operator_id like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotLike(String value) {
            addCriterion("operator_id not like", value, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdIn(List<String> values) {
            addCriterion("operator_id in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotIn(List<String> values) {
            addCriterion("operator_id not in", values, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdBetween(String value1, String value2) {
            addCriterion("operator_id between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andOperatorIdNotBetween(String value1, String value2) {
            addCriterion("operator_id not between", value1, value2, "operatorId");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNull() {
            addCriterion("created_time is null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIsNotNull() {
            addCriterion("created_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeEqualTo(Timestamp value) {
            addCriterion("created_time =", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotEqualTo(Timestamp value) {
            addCriterion("created_time <>", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThan(Timestamp value) {
            addCriterion("created_time >", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("created_time >=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThan(Timestamp value) {
            addCriterion("created_time <", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("created_time <=", value, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeIn(List<Timestamp> values) {
            addCriterion("created_time in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotIn(List<Timestamp> values) {
            addCriterion("created_time not in", values, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("created_time between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andCreatedTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("created_time not between", value1, value2, "createdTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIsNull() {
            addCriterion("modified_time is null");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIsNotNull() {
            addCriterion("modified_time is not null");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeEqualTo(Timestamp value) {
            addCriterion("modified_time =", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotEqualTo(Timestamp value) {
            addCriterion("modified_time <>", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeGreaterThan(Timestamp value) {
            addCriterion("modified_time >", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("modified_time >=", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeLessThan(Timestamp value) {
            addCriterion("modified_time <", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("modified_time <=", value, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeIn(List<Timestamp> values) {
            addCriterion("modified_time in", values, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotIn(List<Timestamp> values) {
            addCriterion("modified_time not in", values, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("modified_time between", value1, value2, "modifiedTime");
            return (Criteria) this;
        }

        public Criteria andModifiedTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("modified_time not between", value1, value2, "modifiedTime");
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