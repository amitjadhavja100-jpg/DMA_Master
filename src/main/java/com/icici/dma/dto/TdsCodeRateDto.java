package com.icici.dma.dto;

import java.util.Date;
 
import lombok.Data;
 
@Data
public class TdsCodeRateDto {
	
	private Long id;
 
	private String wtaxType;
	 
    private String wtx;
 
    private String tdsRate;
 
    private String status;
 
    private String remarks;
 
    private String createdBy;
 
    private Date createdDate;
 
    private String modifiedBy;
 
    private Date modifiedDate;
 
    private String actionType;
 
    private String actionUser;
 
    private Date actionDate;
    
    private String uploadId;
    

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

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

	
    
}
 