package com.ai.paas.ipaas.rcs.dao.mapper.bo;

public class RcsSysParameter {
	
	private Integer id;

	private String model;

	private String paraValue;

	private String paraCode;

	private String paraComment;

	private String status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model == null ? null : model.trim();
	}

	public String getParaCode() {
		return paraCode;
	}

	public void setParaCode(String paraCode) {
		this.paraCode = paraCode == null ? null : paraCode.trim();
	}

	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue == null ? null : paraValue.trim();
	}

	public String getParaComment() {
		return paraComment;
	}

	public void setParaComment(String paraComment) {
		this.paraComment = paraComment == null ? null : paraComment.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}
}
