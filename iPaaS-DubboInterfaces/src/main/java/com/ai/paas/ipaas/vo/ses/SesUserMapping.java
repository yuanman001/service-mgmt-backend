package com.ai.paas.ipaas.vo.ses;

import java.sql.Timestamp;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.ai.dubbo.ext.vo.BaseResponse;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SesUserMapping extends BaseResponse{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -5393044294453244783L;

	private Integer id;

    private String userId;

    private String serviceId;

    private String mapping;

    private String indexDisplay;

    private String indexName;

    private String pk;

    private String copyTo;

    private Timestamp updateTime;

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

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping == null ? null : mapping.trim();
    }

    public String getIndexDisplay() {
        return indexDisplay;
    }

    public void setIndexDisplay(String indexDisplay) {
        this.indexDisplay = indexDisplay == null ? null : indexDisplay.trim();
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName == null ? null : indexName.trim();
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk == null ? null : pk.trim();
    }

    public String getCopyTo() {
        return copyTo;
    }

    public void setCopyTo(String copyTo) {
        this.copyTo = copyTo == null ? null : copyTo.trim();
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}