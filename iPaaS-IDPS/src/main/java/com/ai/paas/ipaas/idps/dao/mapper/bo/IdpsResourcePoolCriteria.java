package com.ai.paas.ipaas.idps.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class IdpsResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public IdpsResourcePoolCriteria() {
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

        public Criteria andIdpsHostIpIsNull() {
            addCriterion("idps_host_ip is null");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpIsNotNull() {
            addCriterion("idps_host_ip is not null");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpEqualTo(String value) {
            addCriterion("idps_host_ip =", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpNotEqualTo(String value) {
            addCriterion("idps_host_ip <>", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpGreaterThan(String value) {
            addCriterion("idps_host_ip >", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpGreaterThanOrEqualTo(String value) {
            addCriterion("idps_host_ip >=", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpLessThan(String value) {
            addCriterion("idps_host_ip <", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpLessThanOrEqualTo(String value) {
            addCriterion("idps_host_ip <=", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpLike(String value) {
            addCriterion("idps_host_ip like", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpNotLike(String value) {
            addCriterion("idps_host_ip not like", value, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpIn(List<String> values) {
            addCriterion("idps_host_ip in", values, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpNotIn(List<String> values) {
            addCriterion("idps_host_ip not in", values, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpBetween(String value1, String value2) {
            addCriterion("idps_host_ip between", value1, value2, "idpsHostIp");
            return (Criteria) this;
        }

        public Criteria andIdpsHostIpNotBetween(String value1, String value2) {
            addCriterion("idps_host_ip not between", value1, value2, "idpsHostIp");
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

        public Criteria andIdpsPortIsNull() {
            addCriterion("idps_port is null");
            return (Criteria) this;
        }

        public Criteria andIdpsPortIsNotNull() {
            addCriterion("idps_port is not null");
            return (Criteria) this;
        }

        public Criteria andIdpsPortEqualTo(Integer value) {
            addCriterion("idps_port =", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortNotEqualTo(Integer value) {
            addCriterion("idps_port <>", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortGreaterThan(Integer value) {
            addCriterion("idps_port >", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("idps_port >=", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortLessThan(Integer value) {
            addCriterion("idps_port <", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortLessThanOrEqualTo(Integer value) {
            addCriterion("idps_port <=", value, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortIn(List<Integer> values) {
            addCriterion("idps_port in", values, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortNotIn(List<Integer> values) {
            addCriterion("idps_port not in", values, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortBetween(Integer value1, Integer value2) {
            addCriterion("idps_port between", value1, value2, "idpsPort");
            return (Criteria) this;
        }

        public Criteria andIdpsPortNotBetween(Integer value1, Integer value2) {
            addCriterion("idps_port not between", value1, value2, "idpsPort");
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

        public Criteria andSshUserIsNull() {
            addCriterion("ssh_user is null");
            return (Criteria) this;
        }

        public Criteria andSshUserIsNotNull() {
            addCriterion("ssh_user is not null");
            return (Criteria) this;
        }

        public Criteria andSshUserEqualTo(String value) {
            addCriterion("ssh_user =", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotEqualTo(String value) {
            addCriterion("ssh_user <>", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserGreaterThan(String value) {
            addCriterion("ssh_user >", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserGreaterThanOrEqualTo(String value) {
            addCriterion("ssh_user >=", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLessThan(String value) {
            addCriterion("ssh_user <", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLessThanOrEqualTo(String value) {
            addCriterion("ssh_user <=", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserLike(String value) {
            addCriterion("ssh_user like", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotLike(String value) {
            addCriterion("ssh_user not like", value, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserIn(List<String> values) {
            addCriterion("ssh_user in", values, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotIn(List<String> values) {
            addCriterion("ssh_user not in", values, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserBetween(String value1, String value2) {
            addCriterion("ssh_user between", value1, value2, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshUserNotBetween(String value1, String value2) {
            addCriterion("ssh_user not between", value1, value2, "sshUser");
            return (Criteria) this;
        }

        public Criteria andSshPasswordIsNull() {
            addCriterion("ssh_password is null");
            return (Criteria) this;
        }

        public Criteria andSshPasswordIsNotNull() {
            addCriterion("ssh_password is not null");
            return (Criteria) this;
        }

        public Criteria andSshPasswordEqualTo(String value) {
            addCriterion("ssh_password =", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordNotEqualTo(String value) {
            addCriterion("ssh_password <>", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordGreaterThan(String value) {
            addCriterion("ssh_password >", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("ssh_password >=", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordLessThan(String value) {
            addCriterion("ssh_password <", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordLessThanOrEqualTo(String value) {
            addCriterion("ssh_password <=", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordLike(String value) {
            addCriterion("ssh_password like", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordNotLike(String value) {
            addCriterion("ssh_password not like", value, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordIn(List<String> values) {
            addCriterion("ssh_password in", values, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordNotIn(List<String> values) {
            addCriterion("ssh_password not in", values, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordBetween(String value1, String value2) {
            addCriterion("ssh_password between", value1, value2, "sshPassword");
            return (Criteria) this;
        }

        public Criteria andSshPasswordNotBetween(String value1, String value2) {
            addCriterion("ssh_password not between", value1, value2, "sshPassword");
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