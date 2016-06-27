package com.ai.paas.ipaas.mds.dao.mapper.bo;

import java.sql.Timestamp;

public class MdsUserSubscribe {
    private Integer subscribeId;

    private String subscribeName;

    private String userId;

    private String topicEnName;
    
    private String userServIpaasId;

    private Timestamp createTime;

    public Integer getSubscribeId() {
        return subscribeId;
    }

    public void setSubscribeId(Integer subscribeId) {
        this.subscribeId = subscribeId;
    }

    public String getSubscribeName() {
        return subscribeName;
    }

    public void setSubscribeName(String subscribeName) {
        this.subscribeName = subscribeName == null ? null : subscribeName.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getTopicEnName() {
        return topicEnName;
    }

    public void setTopicEnName(String topicEnName) {
        this.topicEnName = topicEnName == null ? null : topicEnName.trim();
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

	public String getUserServIpaasId() {
		return userServIpaasId;
	}

	public void setUserServIpaasId(String userServIpaasId) {
		this.userServIpaasId = userServIpaasId;
	}
    
    
}