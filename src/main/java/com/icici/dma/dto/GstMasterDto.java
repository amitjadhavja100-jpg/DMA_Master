package com.icici.dma.dto;

import java.util.Date;

public class GstMasterDto {

	private Integer apsCode;

	private String name;

	private String state;

	private String location;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	private String remarak;

	private String status;
	
	private String actionType;
	
	private Date actionDate;
	
	private String actionUser;

	public Integer getApsCode() {
		return apsCode;
	}

	public void setApsCode(Integer apsCode) {
		this.apsCode = apsCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getRemarak() {
		return remarak;
	}

	public void setRemarak(String remarak) {
		this.remarak = remarak;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public GstMasterDto(Integer apsCode, String name, String state, String location, String createdBy, Date createdDate,
			String modifiedBy, Date modifiedDate, String remarak, String status, String actionType, Date actionDate,
			String actionUser) {
		super();
		this.apsCode = apsCode;
		this.name = name;
		this.state = state;
		this.location = location;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remarak = remarak;
		this.status = status;
		this.actionType = actionType;
		this.actionDate = actionDate;
		this.actionUser = actionUser;
	}

	public GstMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "GstMasterDto [apsCode=" + apsCode + ", name=" + name + ", state=" + state + ", location=" + location
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", remarak=" + remarak + ", status=" + status + ", actionType="
				+ actionType + ", actionDate=" + actionDate + ", actionUser=" + actionUser + "]";
	}
	
	
	
	
}
