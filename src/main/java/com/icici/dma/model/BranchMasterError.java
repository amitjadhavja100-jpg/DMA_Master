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
@Table(name = "TM_VHL_BRANCH_MST_ERROR")
public class BranchMasterError {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_error_seq")
	@SequenceGenerator(name = "branch_error_seq", sequenceName = "ISEQ$$_138497", allocationSize = 1)
	@Column(name = "ERROR_ID", length = 10)
	private Integer errorId;
	
	@Column(name = "BRANCH_CODE", length = 10)
	private String branchCode;

	@Column(name = "BRANCH_NAME",length = 50)
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
	
	@Column(name = "ERROR_MSG", length = 4000)
	private String errorMsg;
	
	@Column(name = "ROW_NUMBER", length = 10)
	private Integer rowNumber;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "UPLOAD_ID", length = 30)
	private String uploadId;

	
	
	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	
	
	
}
