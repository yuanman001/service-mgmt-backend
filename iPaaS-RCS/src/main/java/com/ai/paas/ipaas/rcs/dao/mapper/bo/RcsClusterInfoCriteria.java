package com.ai.paas.ipaas.rcs.dao.mapper.bo;

import java.util.ArrayList;
import java.util.List;

public class RcsClusterInfoCriteria {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected Integer limitStart;

    protected Integer limitEnd;

    public RcsClusterInfoCriteria() {
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
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Float value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Float value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Float value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Float value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Float value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Float value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Float> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Float> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Float value1, Float value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Float value1, Float value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNull() {
            addCriterion("Cluster_ID is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("Cluster_ID is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(String value) {
            addCriterion("Cluster_ID =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(String value) {
            addCriterion("Cluster_ID <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(String value) {
            addCriterion("Cluster_ID >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(String value) {
            addCriterion("Cluster_ID >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(String value) {
            addCriterion("Cluster_ID <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(String value) {
            addCriterion("Cluster_ID <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLike(String value) {
            addCriterion("Cluster_ID like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotLike(String value) {
            addCriterion("Cluster_ID not like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<String> values) {
            addCriterion("Cluster_ID in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<String> values) {
            addCriterion("Cluster_ID not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(String value1, String value2) {
            addCriterion("Cluster_ID between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(String value1, String value2) {
            addCriterion("Cluster_ID not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIsNull() {
            addCriterion("Cluster_Type is null");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIsNotNull() {
            addCriterion("Cluster_Type is not null");
            return (Criteria) this;
        }

        public Criteria andClusterTypeEqualTo(int value) {
            addCriterion("Cluster_Type =", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotEqualTo(int value) {
            addCriterion("Cluster_Type <>", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeGreaterThan(int value) {
            addCriterion("Cluster_Type >", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeGreaterThanOrEqualTo(int value) {
            addCriterion("Cluster_Type >=", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeLessThan(int value) {
            addCriterion("Cluster_Type <", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeLessThanOrEqualTo(int value) {
            addCriterion("Cluster_Type <=", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIn(List<Integer> values) {
            addCriterion("Cluster_Type in", values, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotIn(List<Integer> values) {
            addCriterion("Cluster_Type not in", values, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeBetween(int value1, int value2) {
            addCriterion("Cluster_Type between", value1, value2, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotBetween(int value1, int value2) {
            addCriterion("Cluster_Type not between", value1, value2, "clusterType");
            return (Criteria) this;
        }

        public Criteria andServerCountIsNull() {
            addCriterion("Server_count is null");
            return (Criteria) this;
        }

        public Criteria andServerCountIsNotNull() {
            addCriterion("Server_count is not null");
            return (Criteria) this;
        }

        public Criteria andServerCountEqualTo(int value) {
            addCriterion("Server_count =", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountNotEqualTo(int value) {
            addCriterion("Server_count <>", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountGreaterThan(int value) {
            addCriterion("Server_count >", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountGreaterThanOrEqualTo(int value) {
            addCriterion("Server_count >=", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountLessThan(int value) {
            addCriterion("Server_count <", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountLessThanOrEqualTo(int value) {
            addCriterion("Server_count <=", value, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountIn(List<Integer> values) {
            addCriterion("Server_count in", values, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountNotIn(List<Integer> values) {
            addCriterion("Server_count not in", values, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountBetween(int value1, int value2) {
            addCriterion("Server_count between", value1, value2, "serverCount");
            return (Criteria) this;
        }

        public Criteria andServerCountNotBetween(int value1, int value2) {
            addCriterion("Server_count not between", value1, value2, "serverCount");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNull() {
            addCriterion("bandwidth is null");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNotNull() {
            addCriterion("bandwidth is not null");
            return (Criteria) this;
        }

        public Criteria andBandwidthEqualTo(Float value) {
            addCriterion("bandwidth =", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotEqualTo(Float value) {
            addCriterion("bandwidth <>", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThan(Float value) {
            addCriterion("bandwidth >", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThanOrEqualTo(Float value) {
            addCriterion("bandwidth >=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThan(Float value) {
            addCriterion("bandwidth <", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThanOrEqualTo(Float value) {
            addCriterion("bandwidth <=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthIn(List<Float> values) {
            addCriterion("bandwidth in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotIn(List<Float> values) {
            addCriterion("bandwidth not in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthBetween(Float value1, Float value2) {
            addCriterion("bandwidth between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotBetween(Float value1, Float value2) {
            addCriterion("bandwidth not between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
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

        public Criteria andHostCpuIsNull() {
            addCriterion("Host_CPU is null");
            return (Criteria) this;
        }

        public Criteria andHostCpuIsNotNull() {
            addCriterion("Host_CPU is not null");
            return (Criteria) this;
        }

        public Criteria andHostCpuEqualTo(String value) {
            addCriterion("Host_CPU =", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuNotEqualTo(String value) {
            addCriterion("Host_CPU <>", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuGreaterThan(String value) {
            addCriterion("Host_CPU >", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuGreaterThanOrEqualTo(String value) {
            addCriterion("Host_CPU >=", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuLessThan(String value) {
            addCriterion("Host_CPU <", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuLessThanOrEqualTo(String value) {
            addCriterion("Host_CPU <=", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuLike(String value) {
            addCriterion("Host_CPU like", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuNotLike(String value) {
            addCriterion("Host_CPU not like", value, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuIn(List<String> values) {
            addCriterion("Host_CPU in", values, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuNotIn(List<String> values) {
            addCriterion("Host_CPU not in", values, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuBetween(String value1, String value2) {
            addCriterion("Host_CPU between", value1, value2, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostCpuNotBetween(String value1, String value2) {
            addCriterion("Host_CPU not between", value1, value2, "hostCpu");
            return (Criteria) this;
        }

        public Criteria andHostMemoryIsNull() {
            addCriterion("HOST_memory is null");
            return (Criteria) this;
        }

        public Criteria andHostMemoryIsNotNull() {
            addCriterion("HOST_memory is not null");
            return (Criteria) this;
        }

        public Criteria andHostMemoryEqualTo(String value) {
            addCriterion("HOST_memory =", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryNotEqualTo(String value) {
            addCriterion("HOST_memory <>", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryGreaterThan(String value) {
            addCriterion("HOST_memory >", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryGreaterThanOrEqualTo(String value) {
            addCriterion("HOST_memory >=", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryLessThan(String value) {
            addCriterion("HOST_memory <", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryLessThanOrEqualTo(String value) {
            addCriterion("HOST_memory <=", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryLike(String value) {
            addCriterion("HOST_memory like", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryNotLike(String value) {
            addCriterion("HOST_memory not like", value, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryIn(List<String> values) {
            addCriterion("HOST_memory in", values, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryNotIn(List<String> values) {
            addCriterion("HOST_memory not in", values, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryBetween(String value1, String value2) {
            addCriterion("HOST_memory between", value1, value2, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostMemoryNotBetween(String value1, String value2) {
            addCriterion("HOST_memory not between", value1, value2, "hostMemory");
            return (Criteria) this;
        }

        public Criteria andHostDiskIsNull() {
            addCriterion("HOST_disk is null");
            return (Criteria) this;
        }

        public Criteria andHostDiskIsNotNull() {
            addCriterion("HOST_disk is not null");
            return (Criteria) this;
        }

        public Criteria andHostDiskEqualTo(String value) {
            addCriterion("HOST_disk =", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskNotEqualTo(String value) {
            addCriterion("HOST_disk <>", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskGreaterThan(String value) {
            addCriterion("HOST_disk >", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskGreaterThanOrEqualTo(String value) {
            addCriterion("HOST_disk >=", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskLessThan(String value) {
            addCriterion("HOST_disk <", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskLessThanOrEqualTo(String value) {
            addCriterion("HOST_disk <=", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskLike(String value) {
            addCriterion("HOST_disk like", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskNotLike(String value) {
            addCriterion("HOST_disk not like", value, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskIn(List<String> values) {
            addCriterion("HOST_disk in", values, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskNotIn(List<String> values) {
            addCriterion("HOST_disk not in", values, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskBetween(String value1, String value2) {
            addCriterion("HOST_disk between", value1, value2, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andHostDiskNotBetween(String value1, String value2) {
            addCriterion("HOST_disk not between", value1, value2, "hostDisk");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNull() {
            addCriterion("Order_status is null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIsNotNull() {
            addCriterion("Order_status is not null");
            return (Criteria) this;
        }

        public Criteria andOrderStatusEqualTo(int value) {
            addCriterion("Order_status =", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotEqualTo(int value) {
            addCriterion("Order_status <>", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThan(int value) {
            addCriterion("Order_status >", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusGreaterThanOrEqualTo(int value) {
            addCriterion("Order_status >=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThan(int value) {
            addCriterion("Order_status <", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusLessThanOrEqualTo(int value) {
            addCriterion("Order_status <=", value, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusIn(List<Integer> values) {
            addCriterion("Order_status in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotIn(List<Integer> values) {
            addCriterion("Order_status not in", values, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusBetween(int value1, int value2) {
            addCriterion("Order_status between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderStatusNotBetween(int value1, int value2) {
            addCriterion("Order_status not between", value1, value2, "orderStatus");
            return (Criteria) this;
        }

        public Criteria andOrderUserIsNull() {
            addCriterion("Order_user is null");
            return (Criteria) this;
        }

        public Criteria andOrderUserIsNotNull() {
            addCriterion("Order_user is not null");
            return (Criteria) this;
        }

        public Criteria andOrderUserEqualTo(String value) {
            addCriterion("Order_user =", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserNotEqualTo(String value) {
            addCriterion("Order_user <>", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserGreaterThan(String value) {
            addCriterion("Order_user >", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserGreaterThanOrEqualTo(String value) {
            addCriterion("Order_user >=", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserLessThan(String value) {
            addCriterion("Order_user <", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserLessThanOrEqualTo(String value) {
            addCriterion("Order_user <=", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserLike(String value) {
            addCriterion("Order_user like", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserNotLike(String value) {
            addCriterion("Order_user not like", value, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserIn(List<String> values) {
            addCriterion("Order_user in", values, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserNotIn(List<String> values) {
            addCriterion("Order_user not in", values, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserBetween(String value1, String value2) {
            addCriterion("Order_user between", value1, value2, "orderUser");
            return (Criteria) this;
        }

        public Criteria andOrderUserNotBetween(String value1, String value2) {
            addCriterion("Order_user not between", value1, value2, "orderUser");
            return (Criteria) this;
        }

        public Criteria andZkIpIsNull() {
            addCriterion("Zk_IP is null");
            return (Criteria) this;
        }

        public Criteria andZkIpIsNotNull() {
            addCriterion("Zk_IP is not null");
            return (Criteria) this;
        }

        public Criteria andZkIpEqualTo(String value) {
            addCriterion("Zk_IP =", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpNotEqualTo(String value) {
            addCriterion("Zk_IP <>", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpGreaterThan(String value) {
            addCriterion("Zk_IP >", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpGreaterThanOrEqualTo(String value) {
            addCriterion("Zk_IP >=", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpLessThan(String value) {
            addCriterion("Zk_IP <", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpLessThanOrEqualTo(String value) {
            addCriterion("Zk_IP <=", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpLike(String value) {
            addCriterion("Zk_IP like", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpNotLike(String value) {
            addCriterion("Zk_IP not like", value, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpIn(List<String> values) {
            addCriterion("Zk_IP in", values, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpNotIn(List<String> values) {
            addCriterion("Zk_IP not in", values, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpBetween(String value1, String value2) {
            addCriterion("Zk_IP between", value1, value2, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkIpNotBetween(String value1, String value2) {
            addCriterion("Zk_IP not between", value1, value2, "zkIp");
            return (Criteria) this;
        }

        public Criteria andZkPortIsNull() {
            addCriterion("Zk_port is null");
            return (Criteria) this;
        }

        public Criteria andZkPortIsNotNull() {
            addCriterion("Zk_port is not null");
            return (Criteria) this;
        }

        public Criteria andZkPortEqualTo(String value) {
            addCriterion("Zk_port =", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortNotEqualTo(String value) {
            addCriterion("Zk_port <>", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortGreaterThan(String value) {
            addCriterion("Zk_port >", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortGreaterThanOrEqualTo(String value) {
            addCriterion("Zk_port >=", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortLessThan(String value) {
            addCriterion("Zk_port <", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortLessThanOrEqualTo(String value) {
            addCriterion("Zk_port <=", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortLike(String value) {
            addCriterion("Zk_port like", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortNotLike(String value) {
            addCriterion("Zk_port not like", value, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortIn(List<String> values) {
            addCriterion("Zk_port in", values, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortNotIn(List<String> values) {
            addCriterion("Zk_port not in", values, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortBetween(String value1, String value2) {
            addCriterion("Zk_port between", value1, value2, "zkPort");
            return (Criteria) this;
        }

        public Criteria andZkPortNotBetween(String value1, String value2) {
            addCriterion("Zk_port not between", value1, value2, "zkPort");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNull() {
            addCriterion("Service_Id is null");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNotNull() {
            addCriterion("Service_Id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceIdEqualTo(String value) {
            addCriterion("Service_Id =", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotEqualTo(String value) {
            addCriterion("Service_Id <>", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThan(String value) {
            addCriterion("Service_Id >", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("Service_Id >=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThan(String value) {
            addCriterion("Service_Id <", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThanOrEqualTo(String value) {
            addCriterion("Service_Id <=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLike(String value) {
            addCriterion("Service_Id like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotLike(String value) {
            addCriterion("Service_Id not like", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIn(List<String> values) {
            addCriterion("Service_Id in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotIn(List<String> values) {
            addCriterion("Service_Id not in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdBetween(String value1, String value2) {
            addCriterion("Service_Id between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotBetween(String value1, String value2) {
            addCriterion("Service_Id not between", value1, value2, "serviceId");
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