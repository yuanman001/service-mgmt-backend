package com.ai.paas.ipaas.rds.dao.mapper.bo;

import java.sql.Timestamp;

public class RdsIncBase {
    private Integer id;

    private String userId;

    private String serviceId;

    private String depId;

    private Integer imgId;

    private Integer resId;

    private String bakId;

    private String slaverId;

    private String incName;

    private String incIp;

    private Integer incPort;

    private Integer incType;

    private String incTag;

    private String incLocation;

    private Integer incStatus;

    private String incDescribe;

    private String mysqlHome;

    private String mysqlDataHome;

    private String mysqlVolumnPath;

    private String whiteList;

    private String rootName;

    private String rootPassword;

    private String containerName;

    private String dbServerId;

    private Integer dbStoreage;

    private Integer dbUsedStorage;

    private Integer intStorage;

    private Integer maxConnectNum;

    private Integer masterid;

    private Timestamp createTime;

    private Timestamp updateTime;

    
    public RdsIncBase(String userId, String serviceId, String depId, Integer imgId, Integer resId, String bakId,
			String slaverId, String incName, String incIp, Integer incPort, Integer incType, String incTag,
			String incLocation, Integer incStatus, String incDescribe, String mysqlHome, String mysqlDataHome,
			String mysqlVolumnPath, String whiteList, String rootName, String rootPassword, String containerName,
			String dbServerId, Integer dbStoreage, Integer dbUsedStorage, Integer intStorage, Integer maxConnectNum,
			Timestamp createTime, Timestamp updateTime) {
		super();
		this.userId = userId;
		this.serviceId = serviceId;
		this.depId = depId;
		this.imgId = imgId;
		this.resId = resId;
		this.bakId = bakId;
		this.slaverId = slaverId;
		this.incName = incName;
		this.incIp = incIp;
		this.incPort = incPort;
		this.incType = incType;
		this.incTag = incTag;
		this.incLocation = incLocation;
		this.incStatus = incStatus;
		this.incDescribe = incDescribe;
		this.mysqlHome = mysqlHome;
		this.mysqlDataHome = mysqlDataHome;
		this.mysqlVolumnPath = mysqlVolumnPath;
		this.whiteList = whiteList;
		this.rootName = rootName;
		this.rootPassword = rootPassword;
		this.containerName = containerName;
		this.dbServerId = dbServerId;
		this.dbStoreage = dbStoreage;
		this.dbUsedStorage = dbUsedStorage;
		this.intStorage = intStorage;
		this.maxConnectNum = maxConnectNum;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public RdsIncBase(String userId, String serviceId, String depId, Integer imgId, Integer resId, String bakId,
			String slaverId, String incName, String incIp, Integer incPort, Integer incType, String incTag,
			String incLocation, Integer incStatus, String incDescribe, String mysqlHome, String mysqlDataHome,
			String mysqlVolumnPath, String whiteList, String rootName, String rootPassword, String containerName,
			String dbServerId, Integer dbStoreage, Integer dbUsedStorage, Integer intStorage, Integer maxConnectNum,
			Integer masterid, Timestamp createTime, Timestamp updateTime) {
		super();
		this.userId = userId;
		this.serviceId = serviceId;
		this.depId = depId;
		this.imgId = imgId;
		this.resId = resId;
		this.bakId = bakId;
		this.slaverId = slaverId;
		this.incName = incName;
		this.incIp = incIp;
		this.incPort = incPort;
		this.incType = incType;
		this.incTag = incTag;
		this.incLocation = incLocation;
		this.incStatus = incStatus;
		this.incDescribe = incDescribe;
		this.mysqlHome = mysqlHome;
		this.mysqlDataHome = mysqlDataHome;
		this.mysqlVolumnPath = mysqlVolumnPath;
		this.whiteList = whiteList;
		this.rootName = rootName;
		this.rootPassword = rootPassword;
		this.containerName = containerName;
		this.dbServerId = dbServerId;
		this.dbStoreage = dbStoreage;
		this.dbUsedStorage = dbUsedStorage;
		this.intStorage = intStorage;
		this.maxConnectNum = maxConnectNum;
		this.masterid = masterid;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public RdsIncBase() {
		super();
	}

	public RdsIncBase(Integer id, String userId, String serviceId, String depId, Integer imgId, Integer resId,
			String bakId, String slaverId, String incName, String incIp, Integer incPort, Integer incType,
			String incTag, String incLocation, Integer incStatus, String incDescribe, String mysqlHome,
			String mysqlDataHome, String mysqlVolumnPath, String whiteList, String rootName, String rootPassword,
			String containerName, String dbServerId, Integer dbStoreage, Integer dbUsedStorage, Integer intStorage,
			Integer maxConnectNum, Integer masterid, Timestamp createTime, Timestamp updateTime) {
		super();
		this.id = id;
		this.userId = userId;
		this.serviceId = serviceId;
		this.depId = depId;
		this.imgId = imgId;
		this.resId = resId;
		this.bakId = bakId;
		this.slaverId = slaverId;
		this.incName = incName;
		this.incIp = incIp;
		this.incPort = incPort;
		this.incType = incType;
		this.incTag = incTag;
		this.incLocation = incLocation;
		this.incStatus = incStatus;
		this.incDescribe = incDescribe;
		this.mysqlHome = mysqlHome;
		this.mysqlDataHome = mysqlDataHome;
		this.mysqlVolumnPath = mysqlVolumnPath;
		this.whiteList = whiteList;
		this.rootName = rootName;
		this.rootPassword = rootPassword;
		this.containerName = containerName;
		this.dbServerId = dbServerId;
		this.dbStoreage = dbStoreage;
		this.dbUsedStorage = dbUsedStorage;
		this.intStorage = intStorage;
		this.maxConnectNum = maxConnectNum;
		this.masterid = masterid;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public RdsIncBase clone(){
		RdsIncBase incBaseClone = new RdsIncBase();
		incBaseClone.id = this.id;
		incBaseClone.userId = this.userId;
		incBaseClone.serviceId = this.serviceId;
		incBaseClone.depId = this.depId;
		incBaseClone.imgId = this.imgId;
		incBaseClone.resId = this.resId;
		incBaseClone.bakId = this.bakId;
		incBaseClone.slaverId = this.slaverId;
		incBaseClone.incName = this.incName;
		incBaseClone.incIp = this.incIp;
		incBaseClone.incPort = this.incPort;
		incBaseClone.incType = this.incType;
		incBaseClone.incTag = this.incTag;
		incBaseClone.incLocation = this.incLocation;
		incBaseClone.incStatus = this.incStatus;
		incBaseClone.incDescribe = this.incDescribe;
		incBaseClone.mysqlHome = this.mysqlHome;
		incBaseClone.mysqlDataHome = this.mysqlDataHome;
		incBaseClone.mysqlVolumnPath = this.mysqlVolumnPath;
		incBaseClone.whiteList = this.whiteList;
		incBaseClone.rootName = this.rootName;
		incBaseClone.rootPassword = this.rootPassword;
		incBaseClone.containerName = this.containerName;
		incBaseClone.dbServerId = this.dbServerId;
		incBaseClone.dbStoreage = this.dbStoreage;
		incBaseClone.dbUsedStorage = this.dbUsedStorage;
		incBaseClone.intStorage = this.intStorage;
		incBaseClone.maxConnectNum = this.maxConnectNum;
		incBaseClone.masterid = this.masterid;
		incBaseClone.createTime = this.createTime;
		incBaseClone.updateTime = this.updateTime;
		return incBaseClone;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public String getDepId() {
        return depId;
    }

    public void setDepId(String depId) {
        this.depId = depId == null ? null : depId.trim();
    }

    public Integer getImgId() {
        return imgId;
    }

    public void setImgId(Integer imgId) {
        this.imgId = imgId;
    }

    public Integer getResId() {
        return resId;
    }

    public void setResId(Integer resId) {
        this.resId = resId;
    }

    public String getBakId() {
        return bakId;
    }

    public void setBakId(String bakId) {
        this.bakId = bakId == null ? null : bakId.trim();
    }

    public String getSlaverId() {
        return slaverId;
    }

    public void setSlaverId(String slaverId) {
        this.slaverId = slaverId == null ? null : slaverId.trim();
    }

    public String getIncName() {
        return incName;
    }

    public void setIncName(String incName) {
        this.incName = incName == null ? null : incName.trim();
    }

    public String getIncIp() {
        return incIp;
    }

    public void setIncIp(String incIp) {
        this.incIp = incIp == null ? null : incIp.trim();
    }

    public Integer getIncPort() {
        return incPort;
    }

    public void setIncPort(Integer incPort) {
        this.incPort = incPort;
    }

    public Integer getIncType() {
        return incType;
    }

    public void setIncType(Integer incType) {
        this.incType = incType;
    }

    public String getIncTag() {
        return incTag;
    }

    public void setIncTag(String incTag) {
        this.incTag = incTag == null ? null : incTag.trim();
    }

    public String getIncLocation() {
        return incLocation;
    }

    public void setIncLocation(String incLocation) {
        this.incLocation = incLocation == null ? null : incLocation.trim();
    }

    public Integer getIncStatus() {
        return incStatus;
    }

    public void setIncStatus(Integer incStatus) {
        this.incStatus = incStatus;
    }

    public String getIncDescribe() {
        return incDescribe;
    }

    public void setIncDescribe(String incDescribe) {
        this.incDescribe = incDescribe == null ? null : incDescribe.trim();
    }

    public String getMysqlHome() {
        return mysqlHome;
    }

    public void setMysqlHome(String mysqlHome) {
        this.mysqlHome = mysqlHome == null ? null : mysqlHome.trim();
    }

    public String getMysqlDataHome() {
        return mysqlDataHome;
    }

    public void setMysqlDataHome(String mysqlDataHome) {
        this.mysqlDataHome = mysqlDataHome == null ? null : mysqlDataHome.trim();
    }

    public String getMysqlVolumnPath() {
        return mysqlVolumnPath;
    }

    public void setMysqlVolumnPath(String mysqlVolumnPath) {
        this.mysqlVolumnPath = mysqlVolumnPath == null ? null : mysqlVolumnPath.trim();
    }

    public String getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(String whiteList) {
        this.whiteList = whiteList == null ? null : whiteList.trim();
    }

    public String getRootName() {
        return rootName;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName == null ? null : rootName.trim();
    }

    public String getRootPassword() {
        return rootPassword;
    }

    public void setRootPassword(String rootPassword) {
        this.rootPassword = rootPassword == null ? null : rootPassword.trim();
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName == null ? null : containerName.trim();
    }

    public String getDbServerId() {
        return dbServerId;
    }

    public void setDbServerId(String dbServerId) {
        this.dbServerId = dbServerId == null ? null : dbServerId.trim();
    }

    public Integer getDbStoreage() {
        return dbStoreage;
    }

    public void setDbStoreage(Integer dbStoreage) {
        this.dbStoreage = dbStoreage;
    }

    public Integer getDbUsedStorage() {
        return dbUsedStorage;
    }

    public void setDbUsedStorage(Integer dbUsedStorage) {
        this.dbUsedStorage = dbUsedStorage;
    }

    public Integer getIntStorage() {
        return intStorage;
    }

    public void setIntStorage(Integer intStorage) {
        this.intStorage = intStorage;
    }

    public Integer getMaxConnectNum() {
        return maxConnectNum;
    }

    public void setMaxConnectNum(Integer maxConnectNum) {
        this.maxConnectNum = maxConnectNum;
    }

    public Integer getMasterid() {
        return masterid;
    }

    public void setMasterid(Integer masterid) {
        this.masterid = masterid;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}