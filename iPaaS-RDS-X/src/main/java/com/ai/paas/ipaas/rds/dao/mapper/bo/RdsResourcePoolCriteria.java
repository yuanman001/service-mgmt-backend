package com.ai.paas.ipaas.rds.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RdsResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public RdsResourcePoolCriteria() {
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

        public Criteria andResourceidIsNull() {
            addCriterion("resourceid is null");
            return (Criteria) this;
        }

        public Criteria andResourceidIsNotNull() {
            addCriterion("resourceid is not null");
            return (Criteria) this;
        }

        public Criteria andResourceidEqualTo(Integer value) {
            addCriterion("resourceid =", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidNotEqualTo(Integer value) {
            addCriterion("resourceid <>", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidGreaterThan(Integer value) {
            addCriterion("resourceid >", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidGreaterThanOrEqualTo(Integer value) {
            addCriterion("resourceid >=", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidLessThan(Integer value) {
            addCriterion("resourceid <", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidLessThanOrEqualTo(Integer value) {
            addCriterion("resourceid <=", value, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidIn(List<Integer> values) {
            addCriterion("resourceid in", values, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidNotIn(List<Integer> values) {
            addCriterion("resourceid not in", values, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidBetween(Integer value1, Integer value2) {
            addCriterion("resourceid between", value1, value2, "resourceid");
            return (Criteria) this;
        }

        public Criteria andResourceidNotBetween(Integer value1, Integer value2) {
            addCriterion("resourceid not between", value1, value2, "resourceid");
            return (Criteria) this;
        }

        public Criteria andHostipIsNull() {
            addCriterion("hostip is null");
            return (Criteria) this;
        }

        public Criteria andHostipIsNotNull() {
            addCriterion("hostip is not null");
            return (Criteria) this;
        }

        public Criteria andHostipEqualTo(String value) {
            addCriterion("hostip =", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipNotEqualTo(String value) {
            addCriterion("hostip <>", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipGreaterThan(String value) {
            addCriterion("hostip >", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipGreaterThanOrEqualTo(String value) {
            addCriterion("hostip >=", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipLessThan(String value) {
            addCriterion("hostip <", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipLessThanOrEqualTo(String value) {
            addCriterion("hostip <=", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipLike(String value) {
            addCriterion("hostip like", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipNotLike(String value) {
            addCriterion("hostip not like", value, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipIn(List<String> values) {
            addCriterion("hostip in", values, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipNotIn(List<String> values) {
            addCriterion("hostip not in", values, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipBetween(String value1, String value2) {
            addCriterion("hostip between", value1, value2, "hostip");
            return (Criteria) this;
        }

        public Criteria andHostipNotBetween(String value1, String value2) {
            addCriterion("hostip not between", value1, value2, "hostip");
            return (Criteria) this;
        }

        public Criteria andMaxportIsNull() {
            addCriterion("maxport is null");
            return (Criteria) this;
        }

        public Criteria andMaxportIsNotNull() {
            addCriterion("maxport is not null");
            return (Criteria) this;
        }

        public Criteria andMaxportEqualTo(Integer value) {
            addCriterion("maxport =", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportNotEqualTo(Integer value) {
            addCriterion("maxport <>", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportGreaterThan(Integer value) {
            addCriterion("maxport >", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportGreaterThanOrEqualTo(Integer value) {
            addCriterion("maxport >=", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportLessThan(Integer value) {
            addCriterion("maxport <", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportLessThanOrEqualTo(Integer value) {
            addCriterion("maxport <=", value, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportIn(List<Integer> values) {
            addCriterion("maxport in", values, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportNotIn(List<Integer> values) {
            addCriterion("maxport not in", values, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportBetween(Integer value1, Integer value2) {
            addCriterion("maxport between", value1, value2, "maxport");
            return (Criteria) this;
        }

        public Criteria andMaxportNotBetween(Integer value1, Integer value2) {
            addCriterion("maxport not between", value1, value2, "maxport");
            return (Criteria) this;
        }

        public Criteria andMinportIsNull() {
            addCriterion("minport is null");
            return (Criteria) this;
        }

        public Criteria andMinportIsNotNull() {
            addCriterion("minport is not null");
            return (Criteria) this;
        }

        public Criteria andMinportEqualTo(Integer value) {
            addCriterion("minport =", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportNotEqualTo(Integer value) {
            addCriterion("minport <>", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportGreaterThan(Integer value) {
            addCriterion("minport >", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportGreaterThanOrEqualTo(Integer value) {
            addCriterion("minport >=", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportLessThan(Integer value) {
            addCriterion("minport <", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportLessThanOrEqualTo(Integer value) {
            addCriterion("minport <=", value, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportIn(List<Integer> values) {
            addCriterion("minport in", values, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportNotIn(List<Integer> values) {
            addCriterion("minport not in", values, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportBetween(Integer value1, Integer value2) {
            addCriterion("minport between", value1, value2, "minport");
            return (Criteria) this;
        }

        public Criteria andMinportNotBetween(Integer value1, Integer value2) {
            addCriterion("minport not between", value1, value2, "minport");
            return (Criteria) this;
        }

        public Criteria andCurrentportIsNull() {
            addCriterion("currentport is null");
            return (Criteria) this;
        }

        public Criteria andCurrentportIsNotNull() {
            addCriterion("currentport is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentportEqualTo(Integer value) {
            addCriterion("currentport =", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportNotEqualTo(Integer value) {
            addCriterion("currentport <>", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportGreaterThan(Integer value) {
            addCriterion("currentport >", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportGreaterThanOrEqualTo(Integer value) {
            addCriterion("currentport >=", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportLessThan(Integer value) {
            addCriterion("currentport <", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportLessThanOrEqualTo(Integer value) {
            addCriterion("currentport <=", value, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportIn(List<Integer> values) {
            addCriterion("currentport in", values, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportNotIn(List<Integer> values) {
            addCriterion("currentport not in", values, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportBetween(Integer value1, Integer value2) {
            addCriterion("currentport between", value1, value2, "currentport");
            return (Criteria) this;
        }

        public Criteria andCurrentportNotBetween(Integer value1, Integer value2) {
            addCriterion("currentport not between", value1, value2, "currentport");
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

        public Criteria andSshpasswordIsNull() {
            addCriterion("sshpassword is null");
            return (Criteria) this;
        }

        public Criteria andSshpasswordIsNotNull() {
            addCriterion("sshpassword is not null");
            return (Criteria) this;
        }

        public Criteria andSshpasswordEqualTo(String value) {
            addCriterion("sshpassword =", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordNotEqualTo(String value) {
            addCriterion("sshpassword <>", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordGreaterThan(String value) {
            addCriterion("sshpassword >", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordGreaterThanOrEqualTo(String value) {
            addCriterion("sshpassword >=", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordLessThan(String value) {
            addCriterion("sshpassword <", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordLessThanOrEqualTo(String value) {
            addCriterion("sshpassword <=", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordLike(String value) {
            addCriterion("sshpassword like", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordNotLike(String value) {
            addCriterion("sshpassword not like", value, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordIn(List<String> values) {
            addCriterion("sshpassword in", values, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordNotIn(List<String> values) {
            addCriterion("sshpassword not in", values, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordBetween(String value1, String value2) {
            addCriterion("sshpassword between", value1, value2, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshpasswordNotBetween(String value1, String value2) {
            addCriterion("sshpassword not between", value1, value2, "sshpassword");
            return (Criteria) this;
        }

        public Criteria andSshuserIsNull() {
            addCriterion("sshuser is null");
            return (Criteria) this;
        }

        public Criteria andSshuserIsNotNull() {
            addCriterion("sshuser is not null");
            return (Criteria) this;
        }

        public Criteria andSshuserEqualTo(String value) {
            addCriterion("sshuser =", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserNotEqualTo(String value) {
            addCriterion("sshuser <>", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserGreaterThan(String value) {
            addCriterion("sshuser >", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserGreaterThanOrEqualTo(String value) {
            addCriterion("sshuser >=", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserLessThan(String value) {
            addCriterion("sshuser <", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserLessThanOrEqualTo(String value) {
            addCriterion("sshuser <=", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserLike(String value) {
            addCriterion("sshuser like", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserNotLike(String value) {
            addCriterion("sshuser not like", value, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserIn(List<String> values) {
            addCriterion("sshuser in", values, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserNotIn(List<String> values) {
            addCriterion("sshuser not in", values, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserBetween(String value1, String value2) {
            addCriterion("sshuser between", value1, value2, "sshuser");
            return (Criteria) this;
        }

        public Criteria andSshuserNotBetween(String value1, String value2) {
            addCriterion("sshuser not between", value1, value2, "sshuser");
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

        public Criteria andTotalmemoryIsNull() {
            addCriterion("totalmemory is null");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryIsNotNull() {
            addCriterion("totalmemory is not null");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryEqualTo(Integer value) {
            addCriterion("totalmemory =", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryNotEqualTo(Integer value) {
            addCriterion("totalmemory <>", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryGreaterThan(Integer value) {
            addCriterion("totalmemory >", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("totalmemory >=", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryLessThan(Integer value) {
            addCriterion("totalmemory <", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryLessThanOrEqualTo(Integer value) {
            addCriterion("totalmemory <=", value, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryIn(List<Integer> values) {
            addCriterion("totalmemory in", values, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryNotIn(List<Integer> values) {
            addCriterion("totalmemory not in", values, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryBetween(Integer value1, Integer value2) {
            addCriterion("totalmemory between", value1, value2, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andTotalmemoryNotBetween(Integer value1, Integer value2) {
            addCriterion("totalmemory not between", value1, value2, "totalmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryIsNull() {
            addCriterion("usedmemory is null");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryIsNotNull() {
            addCriterion("usedmemory is not null");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryEqualTo(Integer value) {
            addCriterion("usedmemory =", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryNotEqualTo(Integer value) {
            addCriterion("usedmemory <>", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryGreaterThan(Integer value) {
            addCriterion("usedmemory >", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryGreaterThanOrEqualTo(Integer value) {
            addCriterion("usedmemory >=", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryLessThan(Integer value) {
            addCriterion("usedmemory <", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryLessThanOrEqualTo(Integer value) {
            addCriterion("usedmemory <=", value, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryIn(List<Integer> values) {
            addCriterion("usedmemory in", values, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryNotIn(List<Integer> values) {
            addCriterion("usedmemory not in", values, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryBetween(Integer value1, Integer value2) {
            addCriterion("usedmemory between", value1, value2, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andUsedmemoryNotBetween(Integer value1, Integer value2) {
            addCriterion("usedmemory not between", value1, value2, "usedmemory");
            return (Criteria) this;
        }

        public Criteria andVolumnPathIsNull() {
            addCriterion("volumn_path is null");
            return (Criteria) this;
        }

        public Criteria andVolumnPathIsNotNull() {
            addCriterion("volumn_path is not null");
            return (Criteria) this;
        }

        public Criteria andVolumnPathEqualTo(String value) {
            addCriterion("volumn_path =", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathNotEqualTo(String value) {
            addCriterion("volumn_path <>", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathGreaterThan(String value) {
            addCriterion("volumn_path >", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathGreaterThanOrEqualTo(String value) {
            addCriterion("volumn_path >=", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathLessThan(String value) {
            addCriterion("volumn_path <", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathLessThanOrEqualTo(String value) {
            addCriterion("volumn_path <=", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathLike(String value) {
            addCriterion("volumn_path like", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathNotLike(String value) {
            addCriterion("volumn_path not like", value, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathIn(List<String> values) {
            addCriterion("volumn_path in", values, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathNotIn(List<String> values) {
            addCriterion("volumn_path not in", values, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathBetween(String value1, String value2) {
            addCriterion("volumn_path between", value1, value2, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andVolumnPathNotBetween(String value1, String value2) {
            addCriterion("volumn_path not between", value1, value2, "volumnPath");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeIsNull() {
            addCriterion("instancecreatetime is null");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeIsNotNull() {
            addCriterion("instancecreatetime is not null");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeEqualTo(Timestamp value) {
            addCriterion("instancecreatetime =", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeNotEqualTo(Timestamp value) {
            addCriterion("instancecreatetime <>", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeGreaterThan(Timestamp value) {
            addCriterion("instancecreatetime >", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("instancecreatetime >=", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeLessThan(Timestamp value) {
            addCriterion("instancecreatetime <", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("instancecreatetime <=", value, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeIn(List<Timestamp> values) {
            addCriterion("instancecreatetime in", values, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeNotIn(List<Timestamp> values) {
            addCriterion("instancecreatetime not in", values, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("instancecreatetime between", value1, value2, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancecreatetimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("instancecreatetime not between", value1, value2, "instancecreatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeIsNull() {
            addCriterion("instancelastupdatetime is null");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeIsNotNull() {
            addCriterion("instancelastupdatetime is not null");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeEqualTo(Timestamp value) {
            addCriterion("instancelastupdatetime =", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeNotEqualTo(Timestamp value) {
            addCriterion("instancelastupdatetime <>", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeGreaterThan(Timestamp value) {
            addCriterion("instancelastupdatetime >", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("instancelastupdatetime >=", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeLessThan(Timestamp value) {
            addCriterion("instancelastupdatetime <", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("instancelastupdatetime <=", value, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeIn(List<Timestamp> values) {
            addCriterion("instancelastupdatetime in", values, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeNotIn(List<Timestamp> values) {
            addCriterion("instancelastupdatetime not in", values, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("instancelastupdatetime between", value1, value2, "instancelastupdatetime");
            return (Criteria) this;
        }

        public Criteria andInstancelastupdatetimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("instancelastupdatetime not between", value1, value2, "instancelastupdatetime");
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