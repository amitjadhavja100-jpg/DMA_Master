package com.icici.dma.model;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "TM_CCR_RECOVERY_CITY_MST_TEMP")
public class RecoveryCityMasterTemp {

	@Id
    @Column(name = "CITY")
    private String city;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "MAIN_BRANCH")
    private String mainBranch;

    @Column(name = "ZONE")
    private String zone;

    @Column(name = "EXISTING_CATEGORY")
    private String existingCategory;

    @Column(name = "CATAEGORY_181_360")
    private String cataegory181360;

    @Column(name = "ZONE_CODE")
    private String zoneCode;

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
    private String remarks;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ACTION_TYPE")
    private String actionType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ACTION_DATE")
    private Date actionDate;

    @Column(name = "ACTION_USER")
    private String actionUser;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "FILE_NAME")
    private String fileName;

    public RecoveryCityMasterTemp() {
    }

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getMainBranch() {
		return mainBranch;
	}

	public void setMainBranch(String mainBranch) {
		this.mainBranch = mainBranch;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getExistingCategory() {
		return existingCategory;
	}

	public void setExistingCategory(String existingCategory) {
		this.existingCategory = existingCategory;
	}

	public String getCataegory181360() {
		return cataegory181360;
	}

	public void setCataegory181360(String cataegory181360) {
		this.cataegory181360 = cataegory181360;
	}

	public String getZoneCode() {
		return zoneCode;
	}

	public void setZoneCode(String zoneCode) {
		this.zoneCode = zoneCode;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

    
}