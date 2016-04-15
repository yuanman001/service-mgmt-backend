package com.ai.paas.ipaas.mcs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class McsResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public McsResourcePoolCriteria() {
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
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

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpIsNull() {
            addCriterion("cache_host_ip is null");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpIsNotNull() {
            addCriterion("cache_host_ip is not null");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpEqualTo(String value) {
            addCriterion("cache_host_ip =", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpNotEqualTo(String value) {
            addCriterion("cache_host_ip <>", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpGreaterThan(String value) {
            addCriterion("cache_host_ip >", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpGreaterThanOrEqualTo(String value) {
            addCriterion("cache_host_ip >=", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpLessThan(String value) {
            addCriterion("cache_host_ip <", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpLessThanOrEqualTo(String value) {
            addCriterion("cache_host_ip <=", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpLike(String value) {
            addCriterion("cache_host_ip like", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpNotLike(String value) {
            addCriterion("cache_host_ip not like", value, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpIn(List<String> values) {
            addCriterion("cache_host_ip in", values, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpNotIn(List<String> values) {
            addCriterion("cache_host_ip not in", values, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpBetween(String value1, String value2) {
            addCriterion("cache_host_ip between", value1, value2, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheHostIpNotBetween(String value1, String value2) {
            addCriterion("cache_host_ip not between", value1, value2, "cacheHostIp");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryIsNull() {
            addCriterion("cache_memory is null");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryIsNotNull() {
            addCriterion("cache_memory is not null");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryEqualTo(Integer value) {
            addCriterion("cache_memory =", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryNotEqualTo(Integer value) {
            addCriterion("cache_memory <>", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryGreaterThan(Integer value) {
            addCriterion("cache_memory >", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("cache_memory >=", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryLessThan(Integer value) {
            addCriterion("cache_memory <", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryLessThanOrEqualTo(Integer value) {
            addCriterion("cache_memory <=", value, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryIn(List<Integer> values) {
            addCriterion("cache_memory in", values, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryNotIn(List<Integer> values) {
            addCriterion("cache_memory not in", values, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryBetween(Integer value1, Integer value2) {
            addCriterion("cache_memory between", value1, value2, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryNotBetween(Integer value1, Integer value2) {
            addCriterion("cache_memory not between", value1, value2, "cacheMemory");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedIsNull() {
            addCriterion("cache_memory_used is null");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedIsNotNull() {
            addCriterion("cache_memory_used is not null");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedEqualTo(Integer value) {
            addCriterion("cache_memory_used =", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedNotEqualTo(Integer value) {
            addCriterion("cache_memory_used <>", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedGreaterThan(Integer value) {
            addCriterion("cache_memory_used >", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedGreaterThanOrEqualTo(Integer value) {
            addCriterion("cache_memory_used >=", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedLessThan(Integer value) {
            addCriterion("cache_memory_used <", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedLessThanOrEqualTo(Integer value) {
            addCriterion("cache_memory_used <=", value, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedIn(List<Integer> values) {
            addCriterion("cache_memory_used in", values, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedNotIn(List<Integer> values) {
            addCriterion("cache_memory_used not in", values, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedBetween(Integer value1, Integer value2) {
            addCriterion("cache_memory_used between", value1, value2, "cacheMemoryUsed");
            return (Criteria) this;
        }

        public Criteria andCacheMemoryUsedNotBetween(Integer value1, Integer value2) {
            addCriterion("cache_memory_used not between", value1, value2, "cacheMemoryUsed");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andCachePortIsNull() {
            addCriterion("cache_port is null");
            return (Criteria) this;
        }

        public Criteria andCachePortIsNotNull() {
            addCriterion("cache_port is not null");
            return (Criteria) this;
        }

        public Criteria andCachePortEqualTo(Integer value) {
            addCriterion("cache_port =", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortNotEqualTo(Integer value) {
            addCriterion("cache_port <>", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortGreaterThan(Integer value) {
            addCriterion("cache_port >", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortGreaterThanOrEqualTo(Integer value) {
            addCriterion("cache_port >=", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortLessThan(Integer value) {
            addCriterion("cache_port <", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortLessThanOrEqualTo(Integer value) {
            addCriterion("cache_port <=", value, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortIn(List<Integer> values) {
            addCriterion("cache_port in", values, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortNotIn(List<Integer> values) {
            addCriterion("cache_port not in", values, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortBetween(Integer value1, Integer value2) {
            addCriterion("cache_port between", value1, value2, "cachePort");
            return (Criteria) this;
        }

        public Criteria andCachePortNotBetween(Integer value1, Integer value2) {
            addCriterion("cache_port not between", value1, value2, "cachePort");
            return (Criteria) this;
        }

        public Criteria andMinPortIsNull() {
            addCriterion("min_port is null");
            return (Criteria) this;
        }

        public Criteria andMinPortIsNotNull() {
            addCriterion("min_port is not null");
            return (Criteria) this;
        }

        public Criteria andMinPortEqualTo(Integer value) {
            addCriterion("min_port =", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortNotEqualTo(Integer value) {
            addCriterion("min_port <>", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortGreaterThan(Integer value) {
            addCriterion("min_port >", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("min_port >=", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortLessThan(Integer value) {
            addCriterion("min_port <", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortLessThanOrEqualTo(Integer value) {
            addCriterion("min_port <=", value, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortIn(List<Integer> values) {
            addCriterion("min_port in", values, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortNotIn(List<Integer> values) {
            addCriterion("min_port not in", values, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortBetween(Integer value1, Integer value2) {
            addCriterion("min_port between", value1, value2, "minPort");
            return (Criteria) this;
        }

        public Criteria andMinPortNotBetween(Integer value1, Integer value2) {
            addCriterion("min_port not between", value1, value2, "minPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortIsNull() {
            addCriterion("max_port is null");
            return (Criteria) this;
        }

        public Criteria andMaxPortIsNotNull() {
            addCriterion("max_port is not null");
            return (Criteria) this;
        }

        public Criteria andMaxPortEqualTo(Integer value) {
            addCriterion("max_port =", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortNotEqualTo(Integer value) {
            addCriterion("max_port <>", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortGreaterThan(Integer value) {
            addCriterion("max_port >", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_port >=", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortLessThan(Integer value) {
            addCriterion("max_port <", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortLessThanOrEqualTo(Integer value) {
            addCriterion("max_port <=", value, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortIn(List<Integer> values) {
            addCriterion("max_port in", values, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortNotIn(List<Integer> values) {
            addCriterion("max_port not in", values, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortBetween(Integer value1, Integer value2) {
            addCriterion("max_port between", value1, value2, "maxPort");
            return (Criteria) this;
        }

        public Criteria andMaxPortNotBetween(Integer value1, Integer value2) {
            addCriterion("max_port not between", value1, value2, "maxPort");
            return (Criteria) this;
        }

        public Criteria andCycleIsNull() {
            addCriterion("cycle is null");
            return (Criteria) this;
        }

        public Criteria andCycleIsNotNull() {
            addCriterion("cycle is not null");
            return (Criteria) this;
        }

        public Criteria andCycleEqualTo(Integer value) {
            addCriterion("cycle =", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotEqualTo(Integer value) {
            addCriterion("cycle <>", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleGreaterThan(Integer value) {
            addCriterion("cycle >", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleGreaterThanOrEqualTo(Integer value) {
            addCriterion("cycle >=", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleLessThan(Integer value) {
            addCriterion("cycle <", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleLessThanOrEqualTo(Integer value) {
            addCriterion("cycle <=", value, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleIn(List<Integer> values) {
            addCriterion("cycle in", values, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotIn(List<Integer> values) {
            addCriterion("cycle not in", values, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleBetween(Integer value1, Integer value2) {
            addCriterion("cycle between", value1, value2, "cycle");
            return (Criteria) this;
        }

        public Criteria andCycleNotBetween(Integer value1, Integer value2) {
            addCriterion("cycle not between", value1, value2, "cycle");
            return (Criteria) this;
        }

        public Criteria andAgentCmdIsNull() {
            addCriterion("agent_cmd is null");
            return (Criteria) this;
        }

        public Criteria andAgentCmdIsNotNull() {
            addCriterion("agent_cmd is not null");
            return (Criteria) this;
        }

        public Criteria andAgentCmdEqualTo(String value) {
            addCriterion("agent_cmd =", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdNotEqualTo(String value) {
            addCriterion("agent_cmd <>", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdGreaterThan(String value) {
            addCriterion("agent_cmd >", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdGreaterThanOrEqualTo(String value) {
            addCriterion("agent_cmd >=", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdLessThan(String value) {
            addCriterion("agent_cmd <", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdLessThanOrEqualTo(String value) {
            addCriterion("agent_cmd <=", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdLike(String value) {
            addCriterion("agent_cmd like", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdNotLike(String value) {
            addCriterion("agent_cmd not like", value, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdIn(List<String> values) {
            addCriterion("agent_cmd in", values, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdNotIn(List<String> values) {
            addCriterion("agent_cmd not in", values, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdBetween(String value1, String value2) {
            addCriterion("agent_cmd between", value1, value2, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentCmdNotBetween(String value1, String value2) {
            addCriterion("agent_cmd not between", value1, value2, "agentCmd");
            return (Criteria) this;
        }

        public Criteria andAgentFileIsNull() {
            addCriterion("agent_file is null");
            return (Criteria) this;
        }

        public Criteria andAgentFileIsNotNull() {
            addCriterion("agent_file is not null");
            return (Criteria) this;
        }

        public Criteria andAgentFileEqualTo(String value) {
            addCriterion("agent_file =", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileNotEqualTo(String value) {
            addCriterion("agent_file <>", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileGreaterThan(String value) {
            addCriterion("agent_file >", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileGreaterThanOrEqualTo(String value) {
            addCriterion("agent_file >=", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileLessThan(String value) {
            addCriterion("agent_file <", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileLessThanOrEqualTo(String value) {
            addCriterion("agent_file <=", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileLike(String value) {
            addCriterion("agent_file like", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileNotLike(String value) {
            addCriterion("agent_file not like", value, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileIn(List<String> values) {
            addCriterion("agent_file in", values, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileNotIn(List<String> values) {
            addCriterion("agent_file not in", values, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileBetween(String value1, String value2) {
            addCriterion("agent_file between", value1, value2, "agentFile");
            return (Criteria) this;
        }

        public Criteria andAgentFileNotBetween(String value1, String value2) {
            addCriterion("agent_file not between", value1, value2, "agentFile");
            return (Criteria) this;
        }

        public Criteria andCachePathIsNull() {
            addCriterion("cache_path is null");
            return (Criteria) this;
        }

        public Criteria andCachePathIsNotNull() {
            addCriterion("cache_path is not null");
            return (Criteria) this;
        }

        public Criteria andCachePathEqualTo(String value) {
            addCriterion("cache_path =", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathNotEqualTo(String value) {
            addCriterion("cache_path <>", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathGreaterThan(String value) {
            addCriterion("cache_path >", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathGreaterThanOrEqualTo(String value) {
            addCriterion("cache_path >=", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathLessThan(String value) {
            addCriterion("cache_path <", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathLessThanOrEqualTo(String value) {
            addCriterion("cache_path <=", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathLike(String value) {
            addCriterion("cache_path like", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathNotLike(String value) {
            addCriterion("cache_path not like", value, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathIn(List<String> values) {
            addCriterion("cache_path in", values, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathNotIn(List<String> values) {
            addCriterion("cache_path not in", values, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathBetween(String value1, String value2) {
            addCriterion("cache_path between", value1, value2, "cachePath");
            return (Criteria) this;
        }

        public Criteria andCachePathNotBetween(String value1, String value2) {
            addCriterion("cache_path not between", value1, value2, "cachePath");
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