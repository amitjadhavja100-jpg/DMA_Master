package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_BRANCH_MST_TEMP")
public class BranchMasterTemp {

	@Id
	@Column(name = "BRANCH_CODE", nullable = false,length = 10)
	private String branchCode;

	@Column(name = "BRANCH_NAME",nullable = false ,length = 50)
	private String branchName;

	@Column(name = "HUB", length = 50)
	private String hub;

	@Column(name = "AL_STATE", length = 50)
	private String aLState;

	@Column(name = "ZONE", length = 50)
	private String zone;

	@Column(name = "RBH", length = 100)
	private String rBH;

	@Column(name = "ED_STATE", length = 100)
	private String eDState;

	@Column(name = "ED_ZONE", length = 50)
	private String eDZone;

	@Column(name = "ZH_NAME", length = 100)
	private String zHName;

	@Column(name = "STATE_HEAD", length = 100)
	private String stateHead;

	@Column(name = "MIS_STATE", length = 100)
	private String mISState;

	@Column(name = "RC_STATE", length = 100)
	private String rCState;

	@Column(name = "LOCATION", length = 50)
	private String location;

	@Column(name = "STATUS", length = 1)
	private String status;

	@Column(name = "CREATED_BY", length = 10)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "MODIFIED_BY", length = 10)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;

	@Column(name = "REMARKS", length = 255)
	private String remark;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTION_DATE", length = 255)
	private Date actionDate;
	
	@Column(name = "ACTION_TYPE", length = 1)
	private String actionType;
	
	@Column(name = "ACTION_USER", length = 10)
	private String actionUser;

	@Column(name="UPLOAD_ID")
	private String uploadId;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public String getBranchCode() {
		return branchCode;
	}

	public void setBranchCode(String branchCode) {
		this.branchCode = branchCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getHub() {
		return hub;
	}

	public void setHub(String hub) {
		this.hub = hub;
	}

	public String getaLState() {
		return aLState;
	}

	public void setaLState(String aLState) {
		this.aLState = aLState;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getrBH() {
		return rBH;
	}

	public void setrBH(String rBH) {
		this.rBH = rBH;
	}

	public String geteDState() {
		return eDState;
	}

	public void seteDState(String eDState) {
		this.eDState = eDState;
	}

	public String geteDZone() {
		return eDZone;
	}

	public void seteDZone(String eDZone) {
		this.eDZone = eDZone;
	}

	public String getzHName() {
		return zHName;
	}

	public void setzHName(String zHName) {
		this.zHName = zHName;
	}

	public String getStateHead() {
		return stateHead;
	}

	public void setStateHead(String stateHead) {
		this.stateHead = stateHead;
	}

	public String getmISState() {
		return mISState;
	}

	public void setmISState(String mISState) {
		this.mISState = mISState;
	}

	public String getrCState() {
		return rCState;
	}

	public void setrCState(String rCState) {
		this.rCState = rCState;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
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

	public BranchMasterTemp(String branchCode, String branchName, String hub, String aLState, String zone, String rBH,
			String eDState, String eDZone, String zHName, String stateHead, String mISState, String rCState,
			String location, String status, String createdBy, Date createdDate, String modifiedBy, Date modifiedDate,
			String remark, Date actionDate, String actionType, String actionUser, String uploadId, String fileName) {
		super();
		this.branchCode = branchCode;
		this.branchName = branchName;
		this.hub = hub;
		this.aLState = aLState;
		this.zone = zone;
		this.rBH = rBH;
		this.eDState = eDState;
		this.eDZone = eDZone;
		this.zHName = zHName;
		this.stateHead = stateHead;
		this.mISState = mISState;
		this.rCState = rCState;
		this.location = location;
		this.status = status;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remark = remark;
		this.actionDate = actionDate;
		this.actionType = actionType;
		this.actionUser = actionUser;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public BranchMasterTemp() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "BranchMasterTemp [branchCode=" + branchCode + ", branchName=" + branchName + ", hub=" + hub
				+ ", aLState=" + aLState + ", zone=" + zone + ", rBH=" + rBH + ", eDState=" + eDState + ", eDZone="
				+ eDZone + ", zHName=" + zHName + ", stateHead=" + stateHead + ", mISState=" + mISState + ", rCState="
				+ rCState + ", location=" + location + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", remark=" + remark + ", actionDate=" + actionDate + ", actionType=" + actionType + ", actionUser="
				+ actionUser + ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}
	
	
	
}
