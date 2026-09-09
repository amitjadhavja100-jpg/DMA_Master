package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TDS_CODE_RATE_TEMP")
public class TdsCodeRateTemp {
 
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
 
    @Column(name = "WTAX_TYPE")
    private String wtaxType;
 
    @Column(name = "WTX")
    private String wtx;
 
    @Column(name = "TDS_RATE")
    private String tdsRate;
 
    @Column(name = "STATUS")
    private String status;
 
    @Column(name = "CREATED_BY")
    private String createdBy;
 
    @Column(name = "CREATED_DATE")
    private Date createdDate;
 
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
 
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
 
    @Column(name = "ACTION_TYPE")
    private String actionType;
 
    @Column(name = "ACTION_USER")
    private String actionUser;
 
    @Column(name = "ACTION_DATE")
    private Date actionDate;
 
    @Column(name = "REMARKS")
    private String remarks;
 
    @Column(name = "UPLOAD_ID")
    private String uploadId;
 
    @Column(name = "FILE_NAME")
    private String fileName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWtaxType() {
		return wtaxType;
	}

	public void setWtaxType(String wtaxType) {
		this.wtaxType = wtaxType;
	}

	public String getWtx() {
		return wtx;
	}

	public void setWtx(String wtx) {
		this.wtx = wtx;
	}

	public String getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(String tdsRate) {
		this.tdsRate = tdsRate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String string) {
		this.uploadId = string;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
    
}
 