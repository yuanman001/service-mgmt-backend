package com.ai.paas.ipaas.ccs.service.dto;

import java.io.Serializable;

/**
 * Created by astraea on 2015/5/3.
 */
public class CCSResultDTO implements Serializable {
    private String resultCode;
    private String resultMessage;
    private Object data;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
