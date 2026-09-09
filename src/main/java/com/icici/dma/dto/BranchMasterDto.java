package com.icici.dma.dto;

import java.util.Date;

public class BranchMasterDto {
	
	private String branchCode;

	private String branchName;

	private String hub;

	private String aLState;

	private String zone;

	private String rBH;

	private String eDState;

	private String eDZone;

	private String zHName;

	private String stateHead;

	private String mISState;

	private String rCState;

	private String location;
	

	private String status;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	private String remark;
	
	private String actionType;
	
	private Date actionDate;
	
	private String actionUser;

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

	public BranchMasterDto(String branchCode, String branchName, String hub, String aLState, String zone, String rBH,
			String eDState, String eDZone, String zHName, String stateHead, String mISState, String rCState,
			String location, String status, String createdBy, Date createdDate, String modifiedBy, Date modifiedDate,
			String remark, String actionType, Date actionDate, String actionUser) {
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
		this.actionType = actionType;
		this.actionDate = actionDate;
		this.actionUser = actionUser;
	}

	public BranchMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "BranchMasterDto [branchCode=" + branchCode + ", branchName=" + branchName + ", hub=" + hub
				+ ", aLState=" + aLState + ", zone=" + zone + ", rBH=" + rBH + ", eDState=" + eDState + ", eDZone="
				+ eDZone + ", zHName=" + zHName + ", stateHead=" + stateHead + ", mISState=" + mISState + ", rCState="
				+ rCState + ", location=" + location + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", remark=" + remark + ", actionType=" + actionType + ", actionDate=" + actionDate + ", actionUser="
				+ actionUser + "]";
	}



}
