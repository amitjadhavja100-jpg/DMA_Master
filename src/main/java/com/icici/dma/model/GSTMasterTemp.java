package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_GST_MST_TEMP")
public class GSTMasterTemp {

	@Id
	@Column(name = "APS_CODE")
	private Integer apsCode;

	@Column(name = "NAME")
	private String name;

	@Column(name = "STATE")
	private String state;

	@Column(name = "LOCATION")
	private String location;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "REMARKS")
	private String remarak;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "ACTION_TYPE")
	private String actionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE")
	private Date actionDate;

	@Column(name = "ACTION_USER")
	private String actionUser;

	@Column(name="UPLOAD_ID")
	private String uploadId;
	
	@Column(name="FILE_NAME")
	private String fileName;

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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public GSTMasterTemp(Integer apsCode, String name, String state, String location, String createdBy,
			Date createdDate, String modifiedBy, Date modifiedDate, String remarak, String status, String actionType,
			Date actionDate, String actionUser, String uploadId, String fileName) {
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
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public GSTMasterTemp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "GSTMasterTemp [apsCode=" + apsCode + ", name=" + name + ", state=" + state + ", location=" + location
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy
				+ ", modifiedDate=" + modifiedDate + ", remarak=" + remarak + ", status=" + status + ", actionType="
				+ actionType + ", actionDate=" + actionDate + ", actionUser=" + actionUser + ", uploadId=" + uploadId
				+ ", fileName=" + fileName + "]";
	}
	
	
	
	
}
