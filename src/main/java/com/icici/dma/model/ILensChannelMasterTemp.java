package com.icici.dma.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TM_VHL_ILENS_CHANNEL_MST_TEMP")
public class ILensChannelMasterTemp {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ILENS_CHANNEL_TEMP_ID_SEQ")
	@SequenceGenerator(name = "ILENS_CHANNEL_TEMP_ID_SEQ", sequenceName = "ILENS_CHANNEL_TEMP_ID_SEQ", allocationSize = 1)
	@Column(name = "TEMP_ID")
	private Integer tempId;
	
	@Column(name = "SR_NO")
	private Long srNo;
	
	@Column(name = "USER_ID")
	private String userID;
	
	@Column(name = "USER_NAME")
	private String userName;
	
	@Column(name = "ID_CREATION_DATE")
	private LocalDateTime iDCreationDate;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "EMAIL_ID")
	private String emailID;
	
	@Column(name = "MOBILE_NUMBER")
	private String mobileNumber;
	
	@Column(name = "AGENCY_ID")
	private String agencyID;
	
	@Column(name = "AGENCY_TYPE")
	private String agencyType;
	
	@Column(name = "VPTS_ID")
	private String vPTSID;
	
	@Column(name = "VPTS_APPROVAL_STATUS")
	private String vPTSApprovalStatus;
	
	@Column(name = "ANNUAL_REVIEW_DUE_DATE")
	private LocalDate annualReviewDueDate;
	
	@Column(name = "MSME_REGISTERED")
	private String mSMERegistered;
	
	@Column(name = "UDYOG_AADHAR_NUMBER")
	private String udyogAadharNumber;
	
	@Column(name = "AGENCY_PAN_NUMBER")
	private String agencyPanNumber;
	
	@Column(name = "USER_GROUP")
	private String userGroup;
	
	@Column(name = "CHANNEL_NAME")
	private String channelName;
	
	@Column(name = "PRODUCT_TYPE")
	private String productType;
	
	@Column(name = "BASE_CPC_PROCESS_SHOP")
	private String baseCPCProcessShop;
	
	@Column(name = "MAPPED_EMPLOYEE_ID")
	private String mappedEmployeeID;
	
	@Column(name = "MAPPED_EMPLOYEE_NAME")
	private String mappedEmployeeName;
	
	@Column(name = "INBOUND_OUTBOUND_TYPE")
	private String inboundOutBoundType;
	
	@Column(name = "MAPPED_SOL_IDS")
	private String mappedSolIDs;
	
	@Column(name = "COUNSELLOR_IDS")
	private String counsellorIDs;
	
	@Column(name = "TSM_ID")
	private String tSMID;
	
	@Column(name = "TSM_NAME")
	private String tSMName;
	
	@Column(name = "CHILD_ALLOWED")
	private String childAllowed;
	
	@Column(name = "OPERATING_LOCATIONS")
	private String operatingLocations;
	
	@Column(name = "ROLE_CODE")
	private String roleCode;
	
	@Column(name = "VSTS_ID")
	private String vSTSID;
	
	@Column(name = "VSTS_NAME")
	private String vSTSName;
	
	@Column(name = "VSTS_STATUS")
	private String vSTSStatus;
	
	@Column(name = "PRODUCT")
	private String product;
	
	//record status maker/checker
	@Column(name = "STATUS_A")
	private String statusA;
	
	@Column(name = "REMARKS")
	private String remarak;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date CreatedDate;
	
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
	
	@Column(name = "UPLOAD_ID")
	private String UploadId;
	
	@Column(name = "FILE_NAME")
	private String fileName;

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public Long getSrNo() {
		return srNo;
	}

	public void setSrNo(Long srNo) {
		this.srNo = srNo;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public LocalDateTime getiDCreationDate() {
		return iDCreationDate;
	}

	public void setiDCreationDate(LocalDateTime iDCreationDate) {
		this.iDCreationDate = iDCreationDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmailID() {
		return emailID;
	}

	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getAgencyID() {
		return agencyID;
	}

	public void setAgencyID(String agencyID) {
		this.agencyID = agencyID;
	}

	public String getAgencyType() {
		return agencyType;
	}

	public void setAgencyType(String agencyType) {
		this.agencyType = agencyType;
	}

	public String getvPTSID() {
		return vPTSID;
	}

	public void setvPTSID(String vPTSID) {
		this.vPTSID = vPTSID;
	}

	public String getvPTSApprovalStatus() {
		return vPTSApprovalStatus;
	}

	public void setvPTSApprovalStatus(String vPTSApprovalStatus) {
		this.vPTSApprovalStatus = vPTSApprovalStatus;
	}

	public LocalDate getAnnualReviewDueDate() {
		return annualReviewDueDate;
	}

	public void setAnnualReviewDueDate(LocalDate annualReviewDueDate) {
		this.annualReviewDueDate = annualReviewDueDate;
	}

	public String getmSMERegistered() {
		return mSMERegistered;
	}

	public void setmSMERegistered(String mSMERegistered) {
		this.mSMERegistered = mSMERegistered;
	}

	public String getUdyogAadharNumber() {
		return udyogAadharNumber;
	}

	public void setUdyogAadharNumber(String udyogAadharNumber) {
		this.udyogAadharNumber = udyogAadharNumber;
	}

	public String getAgencyPanNumber() {
		return agencyPanNumber;
	}

	public void setAgencyPanNumber(String agencyPanNumber) {
		this.agencyPanNumber = agencyPanNumber;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getBaseCPCProcessShop() {
		return baseCPCProcessShop;
	}

	public void setBaseCPCProcessShop(String baseCPCProcessShop) {
		this.baseCPCProcessShop = baseCPCProcessShop;
	}

	public String getMappedEmployeeID() {
		return mappedEmployeeID;
	}

	public void setMappedEmployeeID(String mappedEmployeeID) {
		this.mappedEmployeeID = mappedEmployeeID;
	}

	public String getMappedEmployeeName() {
		return mappedEmployeeName;
	}

	public void setMappedEmployeeName(String mappedEmployeeName) {
		this.mappedEmployeeName = mappedEmployeeName;
	}

	public String getInboundOutBoundType() {
		return inboundOutBoundType;
	}

	public void setInboundOutBoundType(String inboundOutBoundType) {
		this.inboundOutBoundType = inboundOutBoundType;
	}

	public String getMappedSolIDs() {
		return mappedSolIDs;
	}

	public void setMappedSolIDs(String mappedSolIDs) {
		this.mappedSolIDs = mappedSolIDs;
	}

	public String getCounsellorIDs() {
		return counsellorIDs;
	}

	public void setCounsellorIDs(String counsellorIDs) {
		this.counsellorIDs = counsellorIDs;
	}

	public String gettSMID() {
		return tSMID;
	}

	public void settSMID(String tSMID) {
		this.tSMID = tSMID;
	}

	public String gettSMName() {
		return tSMName;
	}

	public void settSMName(String tSMName) {
		this.tSMName = tSMName;
	}

	public String getChildAllowed() {
		return childAllowed;
	}

	public void setChildAllowed(String childAllowed) {
		this.childAllowed = childAllowed;
	}

	public String getOperatingLocations() {
		return operatingLocations;
	}

	public void setOperatingLocations(String operatingLocations) {
		this.operatingLocations = operatingLocations;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getvSTSID() {
		return vSTSID;
	}

	public void setvSTSID(String vSTSID) {
		this.vSTSID = vSTSID;
	}

	public String getvSTSName() {
		return vSTSName;
	}

	public void setvSTSName(String vSTSName) {
		this.vSTSName = vSTSName;
	}

	public String getvSTSStatus() {
		return vSTSStatus;
	}

	public void setvSTSStatus(String vSTSStatus) {
		this.vSTSStatus = vSTSStatus;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getStatusA() {
		return statusA;
	}

	public void setStatusA(String statusA) {
		this.statusA = statusA;
	}

	public String getRemarak() {
		return remarak;
	}

	public void setRemarak(String remarak) {
		this.remarak = remarak;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
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
		return UploadId;
	}

	public void setUploadId(String uploadId) {
		UploadId = uploadId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
