package com.ai.paas.ipaas.dbs.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DbsPhysicalResourcePoolCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public DbsPhysicalResourcePoolCriteria() {
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

        public Criteria andResIdIsNull() {
            addCriterion("res_id is null");
            return (Criteria) this;
        }

        public Criteria andResIdIsNotNull() {
            addCriterion("res_id is not null");
            return (Criteria) this;
        }

        public Criteria andResIdEqualTo(Integer value) {
            addCriterion("res_id =", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdNotEqualTo(Integer value) {
            addCriterion("res_id <>", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdGreaterThan(Integer value) {
            addCriterion("res_id >", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("res_id >=", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdLessThan(Integer value) {
            addCriterion("res_id <", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdLessThanOrEqualTo(Integer value) {
            addCriterion("res_id <=", value, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdIn(List<Integer> values) {
            addCriterion("res_id in", values, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdNotIn(List<Integer> values) {
            addCriterion("res_id not in", values, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdBetween(Integer value1, Integer value2) {
            addCriterion("res_id between", value1, value2, "resId");
            return (Criteria) this;
        }

        public Criteria andResIdNotBetween(Integer value1, Integer value2) {
            addCriterion("res_id not between", value1, value2, "resId");
            return (Criteria) this;
        }

        public Criteria andResHostIsNull() {
            addCriterion("res_host is null");
            return (Criteria) this;
        }

        public Criteria andResHostIsNotNull() {
            addCriterion("res_host is not null");
            return (Criteria) this;
        }

        public Criteria andResHostEqualTo(String value) {
            addCriterion("res_host =", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostNotEqualTo(String value) {
            addCriterion("res_host <>", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostGreaterThan(String value) {
            addCriterion("res_host >", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostGreaterThanOrEqualTo(String value) {
            addCriterion("res_host >=", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostLessThan(String value) {
            addCriterion("res_host <", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostLessThanOrEqualTo(String value) {
            addCriterion("res_host <=", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostLike(String value) {
            addCriterion("res_host like", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostNotLike(String value) {
            addCriterion("res_host not like", value, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostIn(List<String> values) {
            addCriterion("res_host in", values, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostNotIn(List<String> values) {
            addCriterion("res_host not in", values, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostBetween(String value1, String value2) {
            addCriterion("res_host between", value1, value2, "resHost");
            return (Criteria) this;
        }

        public Criteria andResHostNotBetween(String value1, String value2) {
            addCriterion("res_host not between", value1, value2, "resHost");
            return (Criteria) this;
        }

        public Criteria andResNameIsNull() {
            addCriterion("res_name is null");
            return (Criteria) this;
        }

        public Criteria andResNameIsNotNull() {
            addCriterion("res_name is not null");
            return (Criteria) this;
        }

        public Criteria andResNameEqualTo(String value) {
            addCriterion("res_name =", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameNotEqualTo(String value) {
            addCriterion("res_name <>", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameGreaterThan(String value) {
            addCriterion("res_name >", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameGreaterThanOrEqualTo(String value) {
            addCriterion("res_name >=", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameLessThan(String value) {
            addCriterion("res_name <", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameLessThanOrEqualTo(String value) {
            addCriterion("res_name <=", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameLike(String value) {
            addCriterion("res_name like", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameNotLike(String value) {
            addCriterion("res_name not like", value, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameIn(List<String> values) {
            addCriterion("res_name in", values, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameNotIn(List<String> values) {
            addCriterion("res_name not in", values, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameBetween(String value1, String value2) {
            addCriterion("res_name between", value1, value2, "resName");
            return (Criteria) this;
        }

        public Criteria andResNameNotBetween(String value1, String value2) {
            addCriterion("res_name not between", value1, value2, "resName");
            return (Criteria) this;
        }

        public Criteria andResPortIsNull() {
            addCriterion("res_port is null");
            return (Criteria) this;
        }

        public Criteria andResPortIsNotNull() {
            addCriterion("res_port is not null");
            return (Criteria) this;
        }

        public Criteria andResPortEqualTo(Integer value) {
            addCriterion("res_port =", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortNotEqualTo(Integer value) {
            addCriterion("res_port <>", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortGreaterThan(Integer value) {
            addCriterion("res_port >", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("res_port >=", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortLessThan(Integer value) {
            addCriterion("res_port <", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortLessThanOrEqualTo(Integer value) {
            addCriterion("res_port <=", value, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortIn(List<Integer> values) {
            addCriterion("res_port in", values, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortNotIn(List<Integer> values) {
            addCriterion("res_port not in", values, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortBetween(Integer value1, Integer value2) {
            addCriterion("res_port between", value1, value2, "resPort");
            return (Criteria) this;
        }

        public Criteria andResPortNotBetween(Integer value1, Integer value2) {
            addCriterion("res_port not between", value1, value2, "resPort");
            return (Criteria) this;
        }

        public Criteria andResInstanceIsNull() {
            addCriterion("res_instance is null");
            return (Criteria) this;
        }

        public Criteria andResInstanceIsNotNull() {
            addCriterion("res_instance is not null");
            return (Criteria) this;
        }

        public Criteria andResInstanceEqualTo(String value) {
            addCriterion("res_instance =", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceNotEqualTo(String value) {
            addCriterion("res_instance <>", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceGreaterThan(String value) {
            addCriterion("res_instance >", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceGreaterThanOrEqualTo(String value) {
            addCriterion("res_instance >=", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceLessThan(String value) {
            addCriterion("res_instance <", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceLessThanOrEqualTo(String value) {
            addCriterion("res_instance <=", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceLike(String value) {
            addCriterion("res_instance like", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceNotLike(String value) {
            addCriterion("res_instance not like", value, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceIn(List<String> values) {
            addCriterion("res_instance in", values, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceNotIn(List<String> values) {
            addCriterion("res_instance not in", values, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceBetween(String value1, String value2) {
            addCriterion("res_instance between", value1, value2, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResInstanceNotBetween(String value1, String value2) {
            addCriterion("res_instance not between", value1, value2, "resInstance");
            return (Criteria) this;
        }

        public Criteria andResUserIsNull() {
            addCriterion("res_user is null");
            return (Criteria) this;
        }

        public Criteria andResUserIsNotNull() {
            addCriterion("res_user is not null");
            return (Criteria) this;
        }

        public Criteria andResUserEqualTo(String value) {
            addCriterion("res_user =", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserNotEqualTo(String value) {
            addCriterion("res_user <>", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserGreaterThan(String value) {
            addCriterion("res_user >", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserGreaterThanOrEqualTo(String value) {
            addCriterion("res_user >=", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserLessThan(String value) {
            addCriterion("res_user <", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserLessThanOrEqualTo(String value) {
            addCriterion("res_user <=", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserLike(String value) {
            addCriterion("res_user like", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserNotLike(String value) {
            addCriterion("res_user not like", value, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserIn(List<String> values) {
            addCriterion("res_user in", values, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserNotIn(List<String> values) {
            addCriterion("res_user not in", values, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserBetween(String value1, String value2) {
            addCriterion("res_user between", value1, value2, "resUser");
            return (Criteria) this;
        }

        public Criteria andResUserNotBetween(String value1, String value2) {
            addCriterion("res_user not between", value1, value2, "resUser");
            return (Criteria) this;
        }

        public Criteria andResPasswordIsNull() {
            addCriterion("res_password is null");
            return (Criteria) this;
        }

        public Criteria andResPasswordIsNotNull() {
            addCriterion("res_password is not null");
            return (Criteria) this;
        }

        public Criteria andResPasswordEqualTo(String value) {
            addCriterion("res_password =", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordNotEqualTo(String value) {
            addCriterion("res_password <>", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordGreaterThan(String value) {
            addCriterion("res_password >", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("res_password >=", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordLessThan(String value) {
            addCriterion("res_password <", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordLessThanOrEqualTo(String value) {
            addCriterion("res_password <=", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordLike(String value) {
            addCriterion("res_password like", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordNotLike(String value) {
            addCriterion("res_password not like", value, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordIn(List<String> values) {
            addCriterion("res_password in", values, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordNotIn(List<String> values) {
            addCriterion("res_password not in", values, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordBetween(String value1, String value2) {
            addCriterion("res_password between", value1, value2, "resPassword");
            return (Criteria) this;
        }

        public Criteria andResPasswordNotBetween(String value1, String value2) {
            addCriterion("res_password not between", value1, value2, "resPassword");
            return (Criteria) this;
        }

        public Criteria andIsUsedIsNull() {
            addCriterion("is_used is null");
            return (Criteria) this;
        }

        public Criteria andIsUsedIsNotNull() {
            addCriterion("is_used is not null");
            return (Criteria) this;
        }

        public Criteria andIsUsedEqualTo(String value) {
            addCriterion("is_used =", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotEqualTo(String value) {
            addCriterion("is_used <>", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedGreaterThan(String value) {
            addCriterion("is_used >", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedGreaterThanOrEqualTo(String value) {
            addCriterion("is_used >=", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedLessThan(String value) {
            addCriterion("is_used <", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedLessThanOrEqualTo(String value) {
            addCriterion("is_used <=", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedLike(String value) {
            addCriterion("is_used like", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotLike(String value) {
            addCriterion("is_used not like", value, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedIn(List<String> values) {
            addCriterion("is_used in", values, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotIn(List<String> values) {
            addCriterion("is_used not in", values, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedBetween(String value1, String value2) {
            addCriterion("is_used between", value1, value2, "isUsed");
            return (Criteria) this;
        }

        public Criteria andIsUsedNotBetween(String value1, String value2) {
            addCriterion("is_used not between", value1, value2, "isUsed");
            return (Criteria) this;
        }

        public Criteria andResStatusIsNull() {
            addCriterion("res_status is null");
            return (Criteria) this;
        }

        public Criteria andResStatusIsNotNull() {
            addCriterion("res_status is not null");
            return (Criteria) this;
        }

        public Criteria andResStatusEqualTo(String value) {
            addCriterion("res_status =", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusNotEqualTo(String value) {
            addCriterion("res_status <>", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusGreaterThan(String value) {
            addCriterion("res_status >", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusGreaterThanOrEqualTo(String value) {
            addCriterion("res_status >=", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusLessThan(String value) {
            addCriterion("res_status <", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusLessThanOrEqualTo(String value) {
            addCriterion("res_status <=", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusLike(String value) {
            addCriterion("res_status like", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusNotLike(String value) {
            addCriterion("res_status not like", value, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusIn(List<String> values) {
            addCriterion("res_status in", values, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusNotIn(List<String> values) {
            addCriterion("res_status not in", values, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusBetween(String value1, String value2) {
            addCriterion("res_status between", value1, value2, "resStatus");
            return (Criteria) this;
        }

        public Criteria andResStatusNotBetween(String value1, String value2) {
            addCriterion("res_status not between", value1, value2, "resStatus");
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

        public Criteria andUsedTimeIsNull() {
            addCriterion("used_time is null");
            return (Criteria) this;
        }

        public Criteria andUsedTimeIsNotNull() {
            addCriterion("used_time is not null");
            return (Criteria) this;
        }

        public Criteria andUsedTimeEqualTo(Timestamp value) {
            addCriterion("used_time =", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeNotEqualTo(Timestamp value) {
            addCriterion("used_time <>", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeGreaterThan(Timestamp value) {
            addCriterion("used_time >", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("used_time >=", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeLessThan(Timestamp value) {
            addCriterion("used_time <", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("used_time <=", value, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeIn(List<Timestamp> values) {
            addCriterion("used_time in", values, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeNotIn(List<Timestamp> values) {
            addCriterion("used_time not in", values, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("used_time between", value1, value2, "usedTime");
            return (Criteria) this;
        }

        public Criteria andUsedTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("used_time not between", value1, value2, "usedTime");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorIsNull() {
            addCriterion("create_author is null");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorIsNotNull() {
            addCriterion("create_author is not null");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorEqualTo(String value) {
            addCriterion("create_author =", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorNotEqualTo(String value) {
            addCriterion("create_author <>", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorGreaterThan(String value) {
            addCriterion("create_author >", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorGreaterThanOrEqualTo(String value) {
            addCriterion("create_author >=", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorLessThan(String value) {
            addCriterion("create_author <", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorLessThanOrEqualTo(String value) {
            addCriterion("create_author <=", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorLike(String value) {
            addCriterion("create_author like", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorNotLike(String value) {
            addCriterion("create_author not like", value, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorIn(List<String> values) {
            addCriterion("create_author in", values, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorNotIn(List<String> values) {
            addCriterion("create_author not in", values, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorBetween(String value1, String value2) {
            addCriterion("create_author between", value1, value2, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andCreateAuthorNotBetween(String value1, String value2) {
            addCriterion("create_author not between", value1, value2, "createAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeIsNull() {
            addCriterion("last_modify_time is null");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeIsNotNull() {
            addCriterion("last_modify_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeEqualTo(Timestamp value) {
            addCriterion("last_modify_time =", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeNotEqualTo(Timestamp value) {
            addCriterion("last_modify_time <>", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeGreaterThan(Timestamp value) {
            addCriterion("last_modify_time >", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("last_modify_time >=", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeLessThan(Timestamp value) {
            addCriterion("last_modify_time <", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("last_modify_time <=", value, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeIn(List<Timestamp> values) {
            addCriterion("last_modify_time in", values, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeNotIn(List<Timestamp> values) {
            addCriterion("last_modify_time not in", values, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("last_modify_time between", value1, value2, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("last_modify_time not between", value1, value2, "lastModifyTime");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorIsNull() {
            addCriterion("last_modify_author is null");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorIsNotNull() {
            addCriterion("last_modify_author is not null");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorEqualTo(String value) {
            addCriterion("last_modify_author =", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorNotEqualTo(String value) {
            addCriterion("last_modify_author <>", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorGreaterThan(String value) {
            addCriterion("last_modify_author >", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorGreaterThanOrEqualTo(String value) {
            addCriterion("last_modify_author >=", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorLessThan(String value) {
            addCriterion("last_modify_author <", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorLessThanOrEqualTo(String value) {
            addCriterion("last_modify_author <=", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorLike(String value) {
            addCriterion("last_modify_author like", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorNotLike(String value) {
            addCriterion("last_modify_author not like", value, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorIn(List<String> values) {
            addCriterion("last_modify_author in", values, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorNotIn(List<String> values) {
            addCriterion("last_modify_author not in", values, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorBetween(String value1, String value2) {
            addCriterion("last_modify_author between", value1, value2, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andLastModifyAuthorNotBetween(String value1, String value2) {
            addCriterion("last_modify_author not between", value1, value2, "lastModifyAuthor");
            return (Criteria) this;
        }

        public Criteria andMsFlagIsNull() {
            addCriterion("ms_flag is null");
            return (Criteria) this;
        }

        public Criteria andMsFlagIsNotNull() {
            addCriterion("ms_flag is not null");
            return (Criteria) this;
        }

        public Criteria andMsFlagEqualTo(String value) {
            addCriterion("ms_flag =", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagNotEqualTo(String value) {
            addCriterion("ms_flag <>", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagGreaterThan(String value) {
            addCriterion("ms_flag >", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagGreaterThanOrEqualTo(String value) {
            addCriterion("ms_flag >=", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagLessThan(String value) {
            addCriterion("ms_flag <", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagLessThanOrEqualTo(String value) {
            addCriterion("ms_flag <=", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagLike(String value) {
            addCriterion("ms_flag like", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagNotLike(String value) {
            addCriterion("ms_flag not like", value, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagIn(List<String> values) {
            addCriterion("ms_flag in", values, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagNotIn(List<String> values) {
            addCriterion("ms_flag not in", values, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagBetween(String value1, String value2) {
            addCriterion("ms_flag between", value1, value2, "msFlag");
            return (Criteria) this;
        }

        public Criteria andMsFlagNotBetween(String value1, String value2) {
            addCriterion("ms_flag not between", value1, value2, "msFlag");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIsNull() {
            addCriterion("res_use_type is null");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIsNotNull() {
            addCriterion("res_use_type is not null");
            return (Criteria) this;
        }

        public Criteria andResUseTypeEqualTo(String value) {
            addCriterion("res_use_type =", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotEqualTo(String value) {
            addCriterion("res_use_type <>", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeGreaterThan(String value) {
            addCriterion("res_use_type >", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeGreaterThanOrEqualTo(String value) {
            addCriterion("res_use_type >=", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLessThan(String value) {
            addCriterion("res_use_type <", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLessThanOrEqualTo(String value) {
            addCriterion("res_use_type <=", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeLike(String value) {
            addCriterion("res_use_type like", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotLike(String value) {
            addCriterion("res_use_type not like", value, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeIn(List<String> values) {
            addCriterion("res_use_type in", values, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotIn(List<String> values) {
            addCriterion("res_use_type not in", values, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeBetween(String value1, String value2) {
            addCriterion("res_use_type between", value1, value2, "resUseType");
            return (Criteria) this;
        }

        public Criteria andResUseTypeNotBetween(String value1, String value2) {
            addCriterion("res_use_type not between", value1, value2, "resUseType");
            return (Criteria) this;
        }

        public Criteria andLogicIdIsNull() {
            addCriterion("logic_id is null");
            return (Criteria) this;
        }

        public Criteria andLogicIdIsNotNull() {
            addCriterion("logic_id is not null");
            return (Criteria) this;
        }

        public Criteria andLogicIdEqualTo(Integer value) {
            addCriterion("logic_id =", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotEqualTo(Integer value) {
            addCriterion("logic_id <>", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdGreaterThan(Integer value) {
            addCriterion("logic_id >", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("logic_id >=", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdLessThan(Integer value) {
            addCriterion("logic_id <", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdLessThanOrEqualTo(Integer value) {
            addCriterion("logic_id <=", value, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdIn(List<Integer> values) {
            addCriterion("logic_id in", values, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotIn(List<Integer> values) {
            addCriterion("logic_id not in", values, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdBetween(Integer value1, Integer value2) {
            addCriterion("logic_id between", value1, value2, "logicId");
            return (Criteria) this;
        }

        public Criteria andLogicIdNotBetween(Integer value1, Integer value2) {
            addCriterion("logic_id not between", value1, value2, "logicId");
            return (Criteria) this;
        }

        public Criteria andResSuperUserIsNull() {
            addCriterion("res_super_user is null");
            return (Criteria) this;
        }

        public Criteria andResSuperUserIsNotNull() {
            addCriterion("res_super_user is not null");
            return (Criteria) this;
        }

        public Criteria andResSuperUserEqualTo(String value) {
            addCriterion("res_super_user =", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserNotEqualTo(String value) {
            addCriterion("res_super_user <>", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserGreaterThan(String value) {
            addCriterion("res_super_user >", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserGreaterThanOrEqualTo(String value) {
            addCriterion("res_super_user >=", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserLessThan(String value) {
            addCriterion("res_super_user <", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserLessThanOrEqualTo(String value) {
            addCriterion("res_super_user <=", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserLike(String value) {
            addCriterion("res_super_user like", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserNotLike(String value) {
            addCriterion("res_super_user not like", value, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserIn(List<String> values) {
            addCriterion("res_super_user in", values, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserNotIn(List<String> values) {
            addCriterion("res_super_user not in", values, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserBetween(String value1, String value2) {
            addCriterion("res_super_user between", value1, value2, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperUserNotBetween(String value1, String value2) {
            addCriterion("res_super_user not between", value1, value2, "resSuperUser");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordIsNull() {
            addCriterion("res_super_password is null");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordIsNotNull() {
            addCriterion("res_super_password is not null");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordEqualTo(String value) {
            addCriterion("res_super_password =", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordNotEqualTo(String value) {
            addCriterion("res_super_password <>", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordGreaterThan(String value) {
            addCriterion("res_super_password >", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("res_super_password >=", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordLessThan(String value) {
            addCriterion("res_super_password <", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordLessThanOrEqualTo(String value) {
            addCriterion("res_super_password <=", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordLike(String value) {
            addCriterion("res_super_password like", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordNotLike(String value) {
            addCriterion("res_super_password not like", value, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordIn(List<String> values) {
            addCriterion("res_super_password in", values, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordNotIn(List<String> values) {
            addCriterion("res_super_password not in", values, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordBetween(String value1, String value2) {
            addCriterion("res_super_password between", value1, value2, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andResSuperPasswordNotBetween(String value1, String value2) {
            addCriterion("res_super_password not between", value1, value2, "resSuperPassword");
            return (Criteria) this;
        }

        public Criteria andConfAddrIsNull() {
            addCriterion("conf_addr is null");
            return (Criteria) this;
        }

        public Criteria andConfAddrIsNotNull() {
            addCriterion("conf_addr is not null");
            return (Criteria) this;
        }

        public Criteria andConfAddrEqualTo(String value) {
            addCriterion("conf_addr =", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrNotEqualTo(String value) {
            addCriterion("conf_addr <>", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrGreaterThan(String value) {
            addCriterion("conf_addr >", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrGreaterThanOrEqualTo(String value) {
            addCriterion("conf_addr >=", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrLessThan(String value) {
            addCriterion("conf_addr <", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrLessThanOrEqualTo(String value) {
            addCriterion("conf_addr <=", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrLike(String value) {
            addCriterion("conf_addr like", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrNotLike(String value) {
            addCriterion("conf_addr not like", value, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrIn(List<String> values) {
            addCriterion("conf_addr in", values, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrNotIn(List<String> values) {
            addCriterion("conf_addr not in", values, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrBetween(String value1, String value2) {
            addCriterion("conf_addr between", value1, value2, "confAddr");
            return (Criteria) this;
        }

        public Criteria andConfAddrNotBetween(String value1, String value2) {
            addCriterion("conf_addr not between", value1, value2, "confAddr");
            return (Criteria) this;
        }

        public Criteria andAgentPortIsNull() {
            addCriterion("agent_port is null");
            return (Criteria) this;
        }

        public Criteria andAgentPortIsNotNull() {
            addCriterion("agent_port is not null");
            return (Criteria) this;
        }

        public Criteria andAgentPortEqualTo(Integer value) {
            addCriterion("agent_port =", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotEqualTo(Integer value) {
            addCriterion("agent_port <>", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortGreaterThan(Integer value) {
            addCriterion("agent_port >", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("agent_port >=", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortLessThan(Integer value) {
            addCriterion("agent_port <", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortLessThanOrEqualTo(Integer value) {
            addCriterion("agent_port <=", value, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortIn(List<Integer> values) {
            addCriterion("agent_port in", values, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotIn(List<Integer> values) {
            addCriterion("agent_port not in", values, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortBetween(Integer value1, Integer value2) {
            addCriterion("agent_port between", value1, value2, "agentPort");
            return (Criteria) this;
        }

        public Criteria andAgentPortNotBetween(Integer value1, Integer value2) {
            addCriterion("agent_port not between", value1, value2, "agentPort");
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