package com.ai.paas.ipaas.ccs.service.dto;

import java.io.Serializable;

/**
 * Created by astraea on 2015/5/3.
 */
public class CCSSubListDTO implements Serializable {
    private String path;
    private String data;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	} 
}
