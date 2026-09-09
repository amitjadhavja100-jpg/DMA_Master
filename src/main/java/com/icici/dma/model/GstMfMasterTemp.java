package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_GST_MF_MST_TEMP")
public class GstMfMasterTemp {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_MF_TEMP_SEQ")
	@SequenceGenerator(name = "GST_MF_TEMP_SEQ", sequenceName = "TM_VHL_GST_MF_TEMP_ID_SEQ", allocationSize = 1)
	@Column(name = "TEMP_ID")
	private Long tempId;

	@Column(name = "APSCODE")
	private Long apsCode;

	@Column(name = "NAME")
	private String name;

	@Column(name = "STATE")
	private String state;

//	@Column(name = "LOCATION")
//	private String location;
//
//	@Column(name = "ADDRESS")
//	private String address;
//
//	@Column(name = "DD")
//	private String dd;
//
//	@Column(name = "LOCATIONMF")
//	private String locationMf;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY")
	private String modifiedBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "ACTION_TYPE")
	private String actionType;

	@Column(name = "ACTION_USER")
	private String actionUser;

	@Temporal(TemporalType.DATE)
	@Column(name = "ACTION_DATE")
	private Date actionDate;

	@Column(name = "UPLOAD_ID")
	private String uploadId;

	@Column(name = "FILE_NAME")
	private String fileName;

	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}

	public Long getApsCode() {
		return apsCode;
	}

	public void setApsCode(Long apsCode) {
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
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
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
	
	

}
