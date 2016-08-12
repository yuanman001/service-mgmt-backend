package com.ai.paas.ipaas.rds.dao.mapper.bo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RdsIncBaseCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public RdsIncBaseCriteria() {
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

        public Criteria andDepIdIsNull() {
            addCriterion("dep_id is null");
            return (Criteria) this;
        }

        public Criteria andDepIdIsNotNull() {
            addCriterion("dep_id is not null");
            return (Criteria) this;
        }

        public Criteria andDepIdEqualTo(String value) {
            addCriterion("dep_id =", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdNotEqualTo(String value) {
            addCriterion("dep_id <>", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdGreaterThan(String value) {
            addCriterion("dep_id >", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdGreaterThanOrEqualTo(String value) {
            addCriterion("dep_id >=", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdLessThan(String value) {
            addCriterion("dep_id <", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdLessThanOrEqualTo(String value) {
            addCriterion("dep_id <=", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdLike(String value) {
            addCriterion("dep_id like", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdNotLike(String value) {
            addCriterion("dep_id not like", value, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdIn(List<String> values) {
            addCriterion("dep_id in", values, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdNotIn(List<String> values) {
            addCriterion("dep_id not in", values, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdBetween(String value1, String value2) {
            addCriterion("dep_id between", value1, value2, "depId");
            return (Criteria) this;
        }

        public Criteria andDepIdNotBetween(String value1, String value2) {
            addCriterion("dep_id not between", value1, value2, "depId");
            return (Criteria) this;
        }

        public Criteria andImgIdIsNull() {
            addCriterion("img_id is null");
            return (Criteria) this;
        }

        public Criteria andImgIdIsNotNull() {
            addCriterion("img_id is not null");
            return (Criteria) this;
        }

        public Criteria andImgIdEqualTo(Integer value) {
            addCriterion("img_id =", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotEqualTo(Integer value) {
            addCriterion("img_id <>", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdGreaterThan(Integer value) {
            addCriterion("img_id >", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("img_id >=", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdLessThan(Integer value) {
            addCriterion("img_id <", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdLessThanOrEqualTo(Integer value) {
            addCriterion("img_id <=", value, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdIn(List<Integer> values) {
            addCriterion("img_id in", values, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotIn(List<Integer> values) {
            addCriterion("img_id not in", values, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdBetween(Integer value1, Integer value2) {
            addCriterion("img_id between", value1, value2, "imgId");
            return (Criteria) this;
        }

        public Criteria andImgIdNotBetween(Integer value1, Integer value2) {
            addCriterion("img_id not between", value1, value2, "imgId");
            return (Criteria) this;
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

        public Criteria andBakIdIsNull() {
            addCriterion("bak_id is null");
            return (Criteria) this;
        }

        public Criteria andBakIdIsNotNull() {
            addCriterion("bak_id is not null");
            return (Criteria) this;
        }

        public Criteria andBakIdEqualTo(String value) {
            addCriterion("bak_id =", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdNotEqualTo(String value) {
            addCriterion("bak_id <>", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdGreaterThan(String value) {
            addCriterion("bak_id >", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdGreaterThanOrEqualTo(String value) {
            addCriterion("bak_id >=", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdLessThan(String value) {
            addCriterion("bak_id <", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdLessThanOrEqualTo(String value) {
            addCriterion("bak_id <=", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdLike(String value) {
            addCriterion("bak_id like", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdNotLike(String value) {
            addCriterion("bak_id not like", value, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdIn(List<String> values) {
            addCriterion("bak_id in", values, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdNotIn(List<String> values) {
            addCriterion("bak_id not in", values, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdBetween(String value1, String value2) {
            addCriterion("bak_id between", value1, value2, "bakId");
            return (Criteria) this;
        }

        public Criteria andBakIdNotBetween(String value1, String value2) {
            addCriterion("bak_id not between", value1, value2, "bakId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdIsNull() {
            addCriterion("slaver_id is null");
            return (Criteria) this;
        }

        public Criteria andSlaverIdIsNotNull() {
            addCriterion("slaver_id is not null");
            return (Criteria) this;
        }

        public Criteria andSlaverIdEqualTo(String value) {
            addCriterion("slaver_id =", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdNotEqualTo(String value) {
            addCriterion("slaver_id <>", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdGreaterThan(String value) {
            addCriterion("slaver_id >", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdGreaterThanOrEqualTo(String value) {
            addCriterion("slaver_id >=", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdLessThan(String value) {
            addCriterion("slaver_id <", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdLessThanOrEqualTo(String value) {
            addCriterion("slaver_id <=", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdLike(String value) {
            addCriterion("slaver_id like", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdNotLike(String value) {
            addCriterion("slaver_id not like", value, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdIn(List<String> values) {
            addCriterion("slaver_id in", values, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdNotIn(List<String> values) {
            addCriterion("slaver_id not in", values, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdBetween(String value1, String value2) {
            addCriterion("slaver_id between", value1, value2, "slaverId");
            return (Criteria) this;
        }

        public Criteria andSlaverIdNotBetween(String value1, String value2) {
            addCriterion("slaver_id not between", value1, value2, "slaverId");
            return (Criteria) this;
        }

        public Criteria andIncNameIsNull() {
            addCriterion("inc_name is null");
            return (Criteria) this;
        }

        public Criteria andIncNameIsNotNull() {
            addCriterion("inc_name is not null");
            return (Criteria) this;
        }

        public Criteria andIncNameEqualTo(String value) {
            addCriterion("inc_name =", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameNotEqualTo(String value) {
            addCriterion("inc_name <>", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameGreaterThan(String value) {
            addCriterion("inc_name >", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameGreaterThanOrEqualTo(String value) {
            addCriterion("inc_name >=", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameLessThan(String value) {
            addCriterion("inc_name <", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameLessThanOrEqualTo(String value) {
            addCriterion("inc_name <=", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameLike(String value) {
            addCriterion("inc_name like", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameNotLike(String value) {
            addCriterion("inc_name not like", value, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameIn(List<String> values) {
            addCriterion("inc_name in", values, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameNotIn(List<String> values) {
            addCriterion("inc_name not in", values, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameBetween(String value1, String value2) {
            addCriterion("inc_name between", value1, value2, "incName");
            return (Criteria) this;
        }

        public Criteria andIncNameNotBetween(String value1, String value2) {
            addCriterion("inc_name not between", value1, value2, "incName");
            return (Criteria) this;
        }

        public Criteria andIncIpIsNull() {
            addCriterion("inc_ip is null");
            return (Criteria) this;
        }

        public Criteria andIncIpIsNotNull() {
            addCriterion("inc_ip is not null");
            return (Criteria) this;
        }

        public Criteria andIncIpEqualTo(String value) {
            addCriterion("inc_ip =", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpNotEqualTo(String value) {
            addCriterion("inc_ip <>", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpGreaterThan(String value) {
            addCriterion("inc_ip >", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpGreaterThanOrEqualTo(String value) {
            addCriterion("inc_ip >=", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpLessThan(String value) {
            addCriterion("inc_ip <", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpLessThanOrEqualTo(String value) {
            addCriterion("inc_ip <=", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpLike(String value) {
            addCriterion("inc_ip like", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpNotLike(String value) {
            addCriterion("inc_ip not like", value, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpIn(List<String> values) {
            addCriterion("inc_ip in", values, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpNotIn(List<String> values) {
            addCriterion("inc_ip not in", values, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpBetween(String value1, String value2) {
            addCriterion("inc_ip between", value1, value2, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncIpNotBetween(String value1, String value2) {
            addCriterion("inc_ip not between", value1, value2, "incIp");
            return (Criteria) this;
        }

        public Criteria andIncPortIsNull() {
            addCriterion("inc_port is null");
            return (Criteria) this;
        }

        public Criteria andIncPortIsNotNull() {
            addCriterion("inc_port is not null");
            return (Criteria) this;
        }

        public Criteria andIncPortEqualTo(Integer value) {
            addCriterion("inc_port =", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortNotEqualTo(Integer value) {
            addCriterion("inc_port <>", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortGreaterThan(Integer value) {
            addCriterion("inc_port >", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("inc_port >=", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortLessThan(Integer value) {
            addCriterion("inc_port <", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortLessThanOrEqualTo(Integer value) {
            addCriterion("inc_port <=", value, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortIn(List<Integer> values) {
            addCriterion("inc_port in", values, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortNotIn(List<Integer> values) {
            addCriterion("inc_port not in", values, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortBetween(Integer value1, Integer value2) {
            addCriterion("inc_port between", value1, value2, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncPortNotBetween(Integer value1, Integer value2) {
            addCriterion("inc_port not between", value1, value2, "incPort");
            return (Criteria) this;
        }

        public Criteria andIncTypeIsNull() {
            addCriterion("inc_type is null");
            return (Criteria) this;
        }

        public Criteria andIncTypeIsNotNull() {
            addCriterion("inc_type is not null");
            return (Criteria) this;
        }

        public Criteria andIncTypeEqualTo(Integer value) {
            addCriterion("inc_type =", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeNotEqualTo(Integer value) {
            addCriterion("inc_type <>", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeGreaterThan(Integer value) {
            addCriterion("inc_type >", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("inc_type >=", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeLessThan(Integer value) {
            addCriterion("inc_type <", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeLessThanOrEqualTo(Integer value) {
            addCriterion("inc_type <=", value, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeIn(List<Integer> values) {
            addCriterion("inc_type in", values, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeNotIn(List<Integer> values) {
            addCriterion("inc_type not in", values, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeBetween(Integer value1, Integer value2) {
            addCriterion("inc_type between", value1, value2, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("inc_type not between", value1, value2, "incType");
            return (Criteria) this;
        }

        public Criteria andIncTagIsNull() {
            addCriterion("inc_tag is null");
            return (Criteria) this;
        }

        public Criteria andIncTagIsNotNull() {
            addCriterion("inc_tag is not null");
            return (Criteria) this;
        }

        public Criteria andIncTagEqualTo(String value) {
            addCriterion("inc_tag =", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagNotEqualTo(String value) {
            addCriterion("inc_tag <>", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagGreaterThan(String value) {
            addCriterion("inc_tag >", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagGreaterThanOrEqualTo(String value) {
            addCriterion("inc_tag >=", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagLessThan(String value) {
            addCriterion("inc_tag <", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagLessThanOrEqualTo(String value) {
            addCriterion("inc_tag <=", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagLike(String value) {
            addCriterion("inc_tag like", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagNotLike(String value) {
            addCriterion("inc_tag not like", value, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagIn(List<String> values) {
            addCriterion("inc_tag in", values, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagNotIn(List<String> values) {
            addCriterion("inc_tag not in", values, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagBetween(String value1, String value2) {
            addCriterion("inc_tag between", value1, value2, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncTagNotBetween(String value1, String value2) {
            addCriterion("inc_tag not between", value1, value2, "incTag");
            return (Criteria) this;
        }

        public Criteria andIncLocationIsNull() {
            addCriterion("inc_location is null");
            return (Criteria) this;
        }

        public Criteria andIncLocationIsNotNull() {
            addCriterion("inc_location is not null");
            return (Criteria) this;
        }

        public Criteria andIncLocationEqualTo(String value) {
            addCriterion("inc_location =", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationNotEqualTo(String value) {
            addCriterion("inc_location <>", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationGreaterThan(String value) {
            addCriterion("inc_location >", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationGreaterThanOrEqualTo(String value) {
            addCriterion("inc_location >=", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationLessThan(String value) {
            addCriterion("inc_location <", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationLessThanOrEqualTo(String value) {
            addCriterion("inc_location <=", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationLike(String value) {
            addCriterion("inc_location like", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationNotLike(String value) {
            addCriterion("inc_location not like", value, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationIn(List<String> values) {
            addCriterion("inc_location in", values, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationNotIn(List<String> values) {
            addCriterion("inc_location not in", values, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationBetween(String value1, String value2) {
            addCriterion("inc_location between", value1, value2, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncLocationNotBetween(String value1, String value2) {
            addCriterion("inc_location not between", value1, value2, "incLocation");
            return (Criteria) this;
        }

        public Criteria andIncStatusIsNull() {
            addCriterion("inc_status is null");
            return (Criteria) this;
        }

        public Criteria andIncStatusIsNotNull() {
            addCriterion("inc_status is not null");
            return (Criteria) this;
        }

        public Criteria andIncStatusEqualTo(Integer value) {
            addCriterion("inc_status =", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusNotEqualTo(Integer value) {
            addCriterion("inc_status <>", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusGreaterThan(Integer value) {
            addCriterion("inc_status >", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("inc_status >=", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusLessThan(Integer value) {
            addCriterion("inc_status <", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusLessThanOrEqualTo(Integer value) {
            addCriterion("inc_status <=", value, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusIn(List<Integer> values) {
            addCriterion("inc_status in", values, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusNotIn(List<Integer> values) {
            addCriterion("inc_status not in", values, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusBetween(Integer value1, Integer value2) {
            addCriterion("inc_status between", value1, value2, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("inc_status not between", value1, value2, "incStatus");
            return (Criteria) this;
        }

        public Criteria andIncDescribeIsNull() {
            addCriterion("inc_describe is null");
            return (Criteria) this;
        }

        public Criteria andIncDescribeIsNotNull() {
            addCriterion("inc_describe is not null");
            return (Criteria) this;
        }

        public Criteria andIncDescribeEqualTo(String value) {
            addCriterion("inc_describe =", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeNotEqualTo(String value) {
            addCriterion("inc_describe <>", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeGreaterThan(String value) {
            addCriterion("inc_describe >", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeGreaterThanOrEqualTo(String value) {
            addCriterion("inc_describe >=", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeLessThan(String value) {
            addCriterion("inc_describe <", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeLessThanOrEqualTo(String value) {
            addCriterion("inc_describe <=", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeLike(String value) {
            addCriterion("inc_describe like", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeNotLike(String value) {
            addCriterion("inc_describe not like", value, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeIn(List<String> values) {
            addCriterion("inc_describe in", values, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeNotIn(List<String> values) {
            addCriterion("inc_describe not in", values, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeBetween(String value1, String value2) {
            addCriterion("inc_describe between", value1, value2, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andIncDescribeNotBetween(String value1, String value2) {
            addCriterion("inc_describe not between", value1, value2, "incDescribe");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeIsNull() {
            addCriterion("mysql_home is null");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeIsNotNull() {
            addCriterion("mysql_home is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeEqualTo(String value) {
            addCriterion("mysql_home =", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeNotEqualTo(String value) {
            addCriterion("mysql_home <>", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeGreaterThan(String value) {
            addCriterion("mysql_home >", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeGreaterThanOrEqualTo(String value) {
            addCriterion("mysql_home >=", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeLessThan(String value) {
            addCriterion("mysql_home <", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeLessThanOrEqualTo(String value) {
            addCriterion("mysql_home <=", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeLike(String value) {
            addCriterion("mysql_home like", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeNotLike(String value) {
            addCriterion("mysql_home not like", value, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeIn(List<String> values) {
            addCriterion("mysql_home in", values, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeNotIn(List<String> values) {
            addCriterion("mysql_home not in", values, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeBetween(String value1, String value2) {
            addCriterion("mysql_home between", value1, value2, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlHomeNotBetween(String value1, String value2) {
            addCriterion("mysql_home not between", value1, value2, "mysqlHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeIsNull() {
            addCriterion("mysql_data_home is null");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeIsNotNull() {
            addCriterion("mysql_data_home is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeEqualTo(String value) {
            addCriterion("mysql_data_home =", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeNotEqualTo(String value) {
            addCriterion("mysql_data_home <>", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeGreaterThan(String value) {
            addCriterion("mysql_data_home >", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeGreaterThanOrEqualTo(String value) {
            addCriterion("mysql_data_home >=", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeLessThan(String value) {
            addCriterion("mysql_data_home <", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeLessThanOrEqualTo(String value) {
            addCriterion("mysql_data_home <=", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeLike(String value) {
            addCriterion("mysql_data_home like", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeNotLike(String value) {
            addCriterion("mysql_data_home not like", value, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeIn(List<String> values) {
            addCriterion("mysql_data_home in", values, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeNotIn(List<String> values) {
            addCriterion("mysql_data_home not in", values, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeBetween(String value1, String value2) {
            addCriterion("mysql_data_home between", value1, value2, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlDataHomeNotBetween(String value1, String value2) {
            addCriterion("mysql_data_home not between", value1, value2, "mysqlDataHome");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathIsNull() {
            addCriterion("mysql_volumn_path is null");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathIsNotNull() {
            addCriterion("mysql_volumn_path is not null");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathEqualTo(String value) {
            addCriterion("mysql_volumn_path =", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathNotEqualTo(String value) {
            addCriterion("mysql_volumn_path <>", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathGreaterThan(String value) {
            addCriterion("mysql_volumn_path >", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathGreaterThanOrEqualTo(String value) {
            addCriterion("mysql_volumn_path >=", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathLessThan(String value) {
            addCriterion("mysql_volumn_path <", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathLessThanOrEqualTo(String value) {
            addCriterion("mysql_volumn_path <=", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathLike(String value) {
            addCriterion("mysql_volumn_path like", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathNotLike(String value) {
            addCriterion("mysql_volumn_path not like", value, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathIn(List<String> values) {
            addCriterion("mysql_volumn_path in", values, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathNotIn(List<String> values) {
            addCriterion("mysql_volumn_path not in", values, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathBetween(String value1, String value2) {
            addCriterion("mysql_volumn_path between", value1, value2, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andMysqlVolumnPathNotBetween(String value1, String value2) {
            addCriterion("mysql_volumn_path not between", value1, value2, "mysqlVolumnPath");
            return (Criteria) this;
        }

        public Criteria andWhiteListIsNull() {
            addCriterion("white_list is null");
            return (Criteria) this;
        }

        public Criteria andWhiteListIsNotNull() {
            addCriterion("white_list is not null");
            return (Criteria) this;
        }

        public Criteria andWhiteListEqualTo(String value) {
            addCriterion("white_list =", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListNotEqualTo(String value) {
            addCriterion("white_list <>", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListGreaterThan(String value) {
            addCriterion("white_list >", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListGreaterThanOrEqualTo(String value) {
            addCriterion("white_list >=", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListLessThan(String value) {
            addCriterion("white_list <", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListLessThanOrEqualTo(String value) {
            addCriterion("white_list <=", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListLike(String value) {
            addCriterion("white_list like", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListNotLike(String value) {
            addCriterion("white_list not like", value, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListIn(List<String> values) {
            addCriterion("white_list in", values, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListNotIn(List<String> values) {
            addCriterion("white_list not in", values, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListBetween(String value1, String value2) {
            addCriterion("white_list between", value1, value2, "whiteList");
            return (Criteria) this;
        }

        public Criteria andWhiteListNotBetween(String value1, String value2) {
            addCriterion("white_list not between", value1, value2, "whiteList");
            return (Criteria) this;
        }

        public Criteria andRootNameIsNull() {
            addCriterion("root_name is null");
            return (Criteria) this;
        }

        public Criteria andRootNameIsNotNull() {
            addCriterion("root_name is not null");
            return (Criteria) this;
        }

        public Criteria andRootNameEqualTo(String value) {
            addCriterion("root_name =", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameNotEqualTo(String value) {
            addCriterion("root_name <>", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameGreaterThan(String value) {
            addCriterion("root_name >", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameGreaterThanOrEqualTo(String value) {
            addCriterion("root_name >=", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameLessThan(String value) {
            addCriterion("root_name <", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameLessThanOrEqualTo(String value) {
            addCriterion("root_name <=", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameLike(String value) {
            addCriterion("root_name like", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameNotLike(String value) {
            addCriterion("root_name not like", value, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameIn(List<String> values) {
            addCriterion("root_name in", values, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameNotIn(List<String> values) {
            addCriterion("root_name not in", values, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameBetween(String value1, String value2) {
            addCriterion("root_name between", value1, value2, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootNameNotBetween(String value1, String value2) {
            addCriterion("root_name not between", value1, value2, "rootName");
            return (Criteria) this;
        }

        public Criteria andRootPasswordIsNull() {
            addCriterion("root_password is null");
            return (Criteria) this;
        }

        public Criteria andRootPasswordIsNotNull() {
            addCriterion("root_password is not null");
            return (Criteria) this;
        }

        public Criteria andRootPasswordEqualTo(String value) {
            addCriterion("root_password =", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordNotEqualTo(String value) {
            addCriterion("root_password <>", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordGreaterThan(String value) {
            addCriterion("root_password >", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("root_password >=", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordLessThan(String value) {
            addCriterion("root_password <", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordLessThanOrEqualTo(String value) {
            addCriterion("root_password <=", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordLike(String value) {
            addCriterion("root_password like", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordNotLike(String value) {
            addCriterion("root_password not like", value, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordIn(List<String> values) {
            addCriterion("root_password in", values, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordNotIn(List<String> values) {
            addCriterion("root_password not in", values, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordBetween(String value1, String value2) {
            addCriterion("root_password between", value1, value2, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andRootPasswordNotBetween(String value1, String value2) {
            addCriterion("root_password not between", value1, value2, "rootPassword");
            return (Criteria) this;
        }

        public Criteria andContainerNameIsNull() {
            addCriterion("container_name is null");
            return (Criteria) this;
        }

        public Criteria andContainerNameIsNotNull() {
            addCriterion("container_name is not null");
            return (Criteria) this;
        }

        public Criteria andContainerNameEqualTo(String value) {
            addCriterion("container_name =", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotEqualTo(String value) {
            addCriterion("container_name <>", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameGreaterThan(String value) {
            addCriterion("container_name >", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameGreaterThanOrEqualTo(String value) {
            addCriterion("container_name >=", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLessThan(String value) {
            addCriterion("container_name <", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLessThanOrEqualTo(String value) {
            addCriterion("container_name <=", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLike(String value) {
            addCriterion("container_name like", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotLike(String value) {
            addCriterion("container_name not like", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameIn(List<String> values) {
            addCriterion("container_name in", values, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotIn(List<String> values) {
            addCriterion("container_name not in", values, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameBetween(String value1, String value2) {
            addCriterion("container_name between", value1, value2, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotBetween(String value1, String value2) {
            addCriterion("container_name not between", value1, value2, "containerName");
            return (Criteria) this;
        }

        public Criteria andDbServerIdIsNull() {
            addCriterion("db_server_id is null");
            return (Criteria) this;
        }

        public Criteria andDbServerIdIsNotNull() {
            addCriterion("db_server_id is not null");
            return (Criteria) this;
        }

        public Criteria andDbServerIdEqualTo(String value) {
            addCriterion("db_server_id =", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdNotEqualTo(String value) {
            addCriterion("db_server_id <>", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdGreaterThan(String value) {
            addCriterion("db_server_id >", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdGreaterThanOrEqualTo(String value) {
            addCriterion("db_server_id >=", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdLessThan(String value) {
            addCriterion("db_server_id <", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdLessThanOrEqualTo(String value) {
            addCriterion("db_server_id <=", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdLike(String value) {
            addCriterion("db_server_id like", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdNotLike(String value) {
            addCriterion("db_server_id not like", value, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdIn(List<String> values) {
            addCriterion("db_server_id in", values, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdNotIn(List<String> values) {
            addCriterion("db_server_id not in", values, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdBetween(String value1, String value2) {
            addCriterion("db_server_id between", value1, value2, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbServerIdNotBetween(String value1, String value2) {
            addCriterion("db_server_id not between", value1, value2, "dbServerId");
            return (Criteria) this;
        }

        public Criteria andDbStoreageIsNull() {
            addCriterion("db_storeage is null");
            return (Criteria) this;
        }

        public Criteria andDbStoreageIsNotNull() {
            addCriterion("db_storeage is not null");
            return (Criteria) this;
        }

        public Criteria andDbStoreageEqualTo(Integer value) {
            addCriterion("db_storeage =", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageNotEqualTo(Integer value) {
            addCriterion("db_storeage <>", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageGreaterThan(Integer value) {
            addCriterion("db_storeage >", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageGreaterThanOrEqualTo(Integer value) {
            addCriterion("db_storeage >=", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageLessThan(Integer value) {
            addCriterion("db_storeage <", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageLessThanOrEqualTo(Integer value) {
            addCriterion("db_storeage <=", value, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageIn(List<Integer> values) {
            addCriterion("db_storeage in", values, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageNotIn(List<Integer> values) {
            addCriterion("db_storeage not in", values, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageBetween(Integer value1, Integer value2) {
            addCriterion("db_storeage between", value1, value2, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbStoreageNotBetween(Integer value1, Integer value2) {
            addCriterion("db_storeage not between", value1, value2, "dbStoreage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageIsNull() {
            addCriterion("db_used_storage is null");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageIsNotNull() {
            addCriterion("db_used_storage is not null");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageEqualTo(Integer value) {
            addCriterion("db_used_storage =", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageNotEqualTo(Integer value) {
            addCriterion("db_used_storage <>", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageGreaterThan(Integer value) {
            addCriterion("db_used_storage >", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageGreaterThanOrEqualTo(Integer value) {
            addCriterion("db_used_storage >=", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageLessThan(Integer value) {
            addCriterion("db_used_storage <", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageLessThanOrEqualTo(Integer value) {
            addCriterion("db_used_storage <=", value, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageIn(List<Integer> values) {
            addCriterion("db_used_storage in", values, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageNotIn(List<Integer> values) {
            addCriterion("db_used_storage not in", values, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageBetween(Integer value1, Integer value2) {
            addCriterion("db_used_storage between", value1, value2, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andDbUsedStorageNotBetween(Integer value1, Integer value2) {
            addCriterion("db_used_storage not between", value1, value2, "dbUsedStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageIsNull() {
            addCriterion("int_storage is null");
            return (Criteria) this;
        }

        public Criteria andIntStorageIsNotNull() {
            addCriterion("int_storage is not null");
            return (Criteria) this;
        }

        public Criteria andIntStorageEqualTo(Integer value) {
            addCriterion("int_storage =", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageNotEqualTo(Integer value) {
            addCriterion("int_storage <>", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageGreaterThan(Integer value) {
            addCriterion("int_storage >", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageGreaterThanOrEqualTo(Integer value) {
            addCriterion("int_storage >=", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageLessThan(Integer value) {
            addCriterion("int_storage <", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageLessThanOrEqualTo(Integer value) {
            addCriterion("int_storage <=", value, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageIn(List<Integer> values) {
            addCriterion("int_storage in", values, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageNotIn(List<Integer> values) {
            addCriterion("int_storage not in", values, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageBetween(Integer value1, Integer value2) {
            addCriterion("int_storage between", value1, value2, "intStorage");
            return (Criteria) this;
        }

        public Criteria andIntStorageNotBetween(Integer value1, Integer value2) {
            addCriterion("int_storage not between", value1, value2, "intStorage");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumIsNull() {
            addCriterion("max_connect_num is null");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumIsNotNull() {
            addCriterion("max_connect_num is not null");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumEqualTo(Integer value) {
            addCriterion("max_connect_num =", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumNotEqualTo(Integer value) {
            addCriterion("max_connect_num <>", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumGreaterThan(Integer value) {
            addCriterion("max_connect_num >", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("max_connect_num >=", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumLessThan(Integer value) {
            addCriterion("max_connect_num <", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumLessThanOrEqualTo(Integer value) {
            addCriterion("max_connect_num <=", value, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumIn(List<Integer> values) {
            addCriterion("max_connect_num in", values, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumNotIn(List<Integer> values) {
            addCriterion("max_connect_num not in", values, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumBetween(Integer value1, Integer value2) {
            addCriterion("max_connect_num between", value1, value2, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMaxConnectNumNotBetween(Integer value1, Integer value2) {
            addCriterion("max_connect_num not between", value1, value2, "maxConnectNum");
            return (Criteria) this;
        }

        public Criteria andMasteridIsNull() {
            addCriterion("masterid is null");
            return (Criteria) this;
        }

        public Criteria andMasteridIsNotNull() {
            addCriterion("masterid is not null");
            return (Criteria) this;
        }

        public Criteria andMasteridEqualTo(Integer value) {
            addCriterion("masterid =", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridNotEqualTo(Integer value) {
            addCriterion("masterid <>", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridGreaterThan(Integer value) {
            addCriterion("masterid >", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridGreaterThanOrEqualTo(Integer value) {
            addCriterion("masterid >=", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridLessThan(Integer value) {
            addCriterion("masterid <", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridLessThanOrEqualTo(Integer value) {
            addCriterion("masterid <=", value, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridIn(List<Integer> values) {
            addCriterion("masterid in", values, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridNotIn(List<Integer> values) {
            addCriterion("masterid not in", values, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridBetween(Integer value1, Integer value2) {
            addCriterion("masterid between", value1, value2, "masterid");
            return (Criteria) this;
        }

        public Criteria andMasteridNotBetween(Integer value1, Integer value2) {
            addCriterion("masterid not between", value1, value2, "masterid");
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

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Timestamp value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Timestamp value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Timestamp value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Timestamp value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Timestamp value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Timestamp value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Timestamp> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Timestamp> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Timestamp value1, Timestamp value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Timestamp value1, Timestamp value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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