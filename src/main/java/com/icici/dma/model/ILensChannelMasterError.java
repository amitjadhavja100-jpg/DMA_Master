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
@Table(name = "TM_VHL_ILENS_CHANNEL_MST_ERROR")
public class ILensChannelMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ILENS_CHANNEL_ERROR_ID_SEQ ")
	@SequenceGenerator(name = "ILENS_CHANNEL_ERROR_ID_SEQ ", sequenceName = "ILENS_CHANNEL_ERROR_ID_SEQ ", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Integer errorId;
	
	@Column(name = "SR_NO")
	private String sr_No;
	
	@Column(name = "USER_ID")
	private String user_ID;
	
	@Column(name = "USER_NAME")
	private String user_Name;
	
	@Column(name = "ID_CREATION_DATE")
	private String iD_Creation_Date;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "EMAIL_ID")
	private String email_ID;
	
	@Column(name = "MOBILE_NUMBER")
	private String mobile_Number;
	
	@Column(name = "AGENCY_ID")
	private String agency_ID;
	
	@Column(name = "AGENCY_TYPE")
	private String agency_Type;
	
	@Column(name = "VPTS_ID")
	private String vPTS_ID;
	
	@Column(name = "VPTS_APPROVAL_STATUS")
	private String vPTS_Approval_Status;
	
	@Column(name = "ANNUAL_REVIEW_DUE_DATE")
	private String annual_Review_Due_Date;
	
	@Column(name = "MSME_REGISTERED")
	private String mSME_Registered;
	
	@Column(name = "UDYOG_AADHAR_NUMBER")
	private String udyog_Aadhar_Number;
	
	@Column(name = "AGENCY_PAN_NUMBER")
	private String agency_Pan_Number;
	
	@Column(name = "USER_GROUP")
	private String user_Group;
	
	@Column(name = "CHANNEL_NAME")
	private String channel_Name;
	
	@Column(name = "PRODUCT_TYPE")
	private String product_Type;
	
	@Column(name = "BASE_CPC_PROCESS_SHOP")
	private String base_CPC_Process_Shop;
	
	@Column(name = "MAPPED_EMPLOYEE_ID")
	private String mapped_Employee_ID;
	
	@Column(name = "MAPPED_EMPLOYEE_NAME")
	private String mapped_Employee_Name;
	
	@Column(name = "INBOUND_OUTBOUND_TYPE")
	private String inbound_OutBound_Type;
	
	@Column(name = "MAPPED_SOL_IDS")
	private String mapped_Sol_IDs;
	
	@Column(name = "COUNSELLOR_IDS")
	private String counsellor_IDs;
	
	@Column(name = "TSM_ID")
	private String tSM_ID;
	
	@Column(name = "TSM_NAME")
	private String tSM_Name;
	
	@Column(name = "CHILD_ALLOWED")
	private String child_Allowed;
	
	@Column(name = "OPERATING_LOCATIONS")
	private String operating_Locations;
	
	@Column(name = "ROLE_CODE")
	private String role_Code;
	
	@Column(name = "VSTS_ID")
	private String vSTS_ID;
	
	@Column(name = "VSTS_NAME")
	private String vSTS_Name;
	
	@Column(name = "VSTS_STATUS")
	private String vSTS_Status;
	
	@Column(name = "PRODUCT")
	private String product;
	
	@Column(name = "ROW_NUMBER")
	private Integer rowNumber;
	
	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;
	
	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
	
	@Column(name = "UPLOAD_ID")
	private String UploadId;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getSr_No() {
		return sr_No;
	}

	public void setSr_No(String sr_No) {
		this.sr_No = sr_No;
	}

	public String getUser_ID() {
		return user_ID;
	}

	public void setUser_ID(String user_ID) {
		this.user_ID = user_ID;
	}

	public String getUser_Name() {
		return user_Name;
	}

	public void setUser_Name(String user_Name) {
		this.user_Name = user_Name;
	}

	public String getiD_Creation_Date() {
		return iD_Creation_Date;
	}

	public void setiD_Creation_Date(String iD_Creation_Date) {
		this.iD_Creation_Date = iD_Creation_Date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail_ID() {
		return email_ID;
	}

	public void setEmail_ID(String email_ID) {
		this.email_ID = email_ID;
	}

	public String getMobile_Number() {
		return mobile_Number;
	}

	public void setMobile_Number(String mobile_Number) {
		this.mobile_Number = mobile_Number;
	}

	public String getAgency_ID() {
		return agency_ID;
	}

	public void setAgency_ID(String agency_ID) {
		this.agency_ID = agency_ID;
	}

	public String getAgency_Type() {
		return agency_Type;
	}

	public void setAgency_Type(String agency_Type) {
		this.agency_Type = agency_Type;
	}

	public String getvPTS_ID() {
		return vPTS_ID;
	}

	public void setvPTS_ID(String vPTS_ID) {
		this.vPTS_ID = vPTS_ID;
	}

	public String getvPTS_Approval_Status() {
		return vPTS_Approval_Status;
	}

	public void setvPTS_Approval_Status(String vPTS_Approval_Status) {
		this.vPTS_Approval_Status = vPTS_Approval_Status;
	}

	public String getAnnual_Review_Due_Date() {
		return annual_Review_Due_Date;
	}

	public void setAnnual_Review_Due_Date(String annual_Review_Due_Date) {
		this.annual_Review_Due_Date = annual_Review_Due_Date;
	}

	public String getmSME_Registered() {
		return mSME_Registered;
	}

	public void setmSME_Registered(String mSME_Registered) {
		this.mSME_Registered = mSME_Registered;
	}

	public String getUdyog_Aadhar_Number() {
		return udyog_Aadhar_Number;
	}

	public void setUdyog_Aadhar_Number(String udyog_Aadhar_Number) {
		this.udyog_Aadhar_Number = udyog_Aadhar_Number;
	}

	public String getAgency_Pan_Number() {
		return agency_Pan_Number;
	}

	public void setAgency_Pan_Number(String agency_Pan_Number) {
		this.agency_Pan_Number = agency_Pan_Number;
	}

	public String getUser_Group() {
		return user_Group;
	}

	public void setUser_Group(String user_Group) {
		this.user_Group = user_Group;
	}

	public String getChannel_Name() {
		return channel_Name;
	}

	public void setChannel_Name(String channel_Name) {
		this.channel_Name = channel_Name;
	}

	public String getProduct_Type() {
		return product_Type;
	}

	public void setProduct_Type(String product_Type) {
		this.product_Type = product_Type;
	}

	public String getBase_CPC_Process_Shop() {
		return base_CPC_Process_Shop;
	}

	public void setBase_CPC_Process_Shop(String base_CPC_Process_Shop) {
		this.base_CPC_Process_Shop = base_CPC_Process_Shop;
	}

	public String getMapped_Employee_ID() {
		return mapped_Employee_ID;
	}

	public void setMapped_Employee_ID(String mapped_Employee_ID) {
		this.mapped_Employee_ID = mapped_Employee_ID;
	}

	public String getMapped_Employee_Name() {
		return mapped_Employee_Name;
	}

	public void setMapped_Employee_Name(String mapped_Employee_Name) {
		this.mapped_Employee_Name = mapped_Employee_Name;
	}

	public String getInbound_OutBound_Type() {
		return inbound_OutBound_Type;
	}

	public void setInbound_OutBound_Type(String inbound_OutBound_Type) {
		this.inbound_OutBound_Type = inbound_OutBound_Type;
	}

	public String getMapped_Sol_IDs() {
		return mapped_Sol_IDs;
	}

	public void setMapped_Sol_IDs(String mapped_Sol_IDs) {
		this.mapped_Sol_IDs = mapped_Sol_IDs;
	}

	public String getCounsellor_IDs() {
		return counsellor_IDs;
	}

	public void setCounsellor_IDs(String counsellor_IDs) {
		this.counsellor_IDs = counsellor_IDs;
	}

	public String gettSM_ID() {
		return tSM_ID;
	}

	public void settSM_ID(String tSM_ID) {
		this.tSM_ID = tSM_ID;
	}

	public String gettSM_Name() {
		return tSM_Name;
	}

	public void settSM_Name(String tSM_Name) {
		this.tSM_Name = tSM_Name;
	}

	public String getChild_Allowed() {
		return child_Allowed;
	}

	public void setChild_Allowed(String child_Allowed) {
		this.child_Allowed = child_Allowed;
	}

	public String getOperating_Locations() {
		return operating_Locations;
	}

	public void setOperating_Locations(String operating_Locations) {
		this.operating_Locations = operating_Locations;
	}

	public String getRole_Code() {
		return role_Code;
	}

	public void setRole_Code(String role_Code) {
		this.role_Code = role_Code;
	}

	public String getvSTS_ID() {
		return vSTS_ID;
	}

	public void setvSTS_ID(String vSTS_ID) {
		this.vSTS_ID = vSTS_ID;
	}

	public String getvSTS_Name() {
		return vSTS_Name;
	}

	public void setvSTS_Name(String vSTS_Name) {
		this.vSTS_Name = vSTS_Name;
	}

	public String getvSTS_Status() {
		return vSTS_Status;
	}

	public void setvSTS_Status(String vSTS_Status) {
		this.vSTS_Status = vSTS_Status;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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
		return UploadId;
	}

	public void setUploadId(String uploadId) {
		UploadId = uploadId;
	}

	public ILensChannelMasterError(Integer errorId, String sr_No, String user_ID, String user_Name,
			String iD_Creation_Date, String status, String email_ID, String mobile_Number, String agency_ID,
			String agency_Type, String vPTS_ID, String vPTS_Approval_Status, String annual_Review_Due_Date,
			String mSME_Registered, String udyog_Aadhar_Number, String agency_Pan_Number, String user_Group,
			String channel_Name, String product_Type, String base_CPC_Process_Shop, String mapped_Employee_ID,
			String mapped_Employee_Name, String inbound_OutBound_Type, String mapped_Sol_IDs, String counsellor_IDs,
			String tSM_ID, String tSM_Name, String child_Allowed, String operating_Locations, String role_Code,
			String vSTS_ID, String vSTS_Name, String vSTS_Status, String product, Integer rowNumber,
			String errorMessage, String createdBy, Date createdDate, String uploadId) {
		super();
		this.errorId = errorId;
		this.sr_No = sr_No;
		this.user_ID = user_ID;
		this.user_Name = user_Name;
		this.iD_Creation_Date = iD_Creation_Date;
		this.status = status;
		this.email_ID = email_ID;
		this.mobile_Number = mobile_Number;
		this.agency_ID = agency_ID;
		this.agency_Type = agency_Type;
		this.vPTS_ID = vPTS_ID;
		this.vPTS_Approval_Status = vPTS_Approval_Status;
		this.annual_Review_Due_Date = annual_Review_Due_Date;
		this.mSME_Registered = mSME_Registered;
		this.udyog_Aadhar_Number = udyog_Aadhar_Number;
		this.agency_Pan_Number = agency_Pan_Number;
		this.user_Group = user_Group;
		this.channel_Name = channel_Name;
		this.product_Type = product_Type;
		this.base_CPC_Process_Shop = base_CPC_Process_Shop;
		this.mapped_Employee_ID = mapped_Employee_ID;
		this.mapped_Employee_Name = mapped_Employee_Name;
		this.inbound_OutBound_Type = inbound_OutBound_Type;
		this.mapped_Sol_IDs = mapped_Sol_IDs;
		this.counsellor_IDs = counsellor_IDs;
		this.tSM_ID = tSM_ID;
		this.tSM_Name = tSM_Name;
		this.child_Allowed = child_Allowed;
		this.operating_Locations = operating_Locations;
		this.role_Code = role_Code;
		this.vSTS_ID = vSTS_ID;
		this.vSTS_Name = vSTS_Name;
		this.vSTS_Status = vSTS_Status;
		this.product = product;
		this.rowNumber = rowNumber;
		this.errorMessage = errorMessage;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		UploadId = uploadId;
	}

	@Override
	public String toString() {
		return "ILensChannelMasterError [errorId=" + errorId + ", sr_No=" + sr_No + ", user_ID=" + user_ID
				+ ", user_Name=" + user_Name + ", iD_Creation_Date=" + iD_Creation_Date + ", status=" + status
				+ ", email_ID=" + email_ID + ", mobile_Number=" + mobile_Number + ", agency_ID=" + agency_ID
				+ ", agency_Type=" + agency_Type + ", vPTS_ID=" + vPTS_ID + ", vPTS_Approval_Status="
				+ vPTS_Approval_Status + ", annual_Review_Due_Date=" + annual_Review_Due_Date + ", mSME_Registered="
				+ mSME_Registered + ", udyog_Aadhar_Number=" + udyog_Aadhar_Number + ", agency_Pan_Number="
				+ agency_Pan_Number + ", user_Group=" + user_Group + ", channel_Name=" + channel_Name
				+ ", product_Type=" + product_Type + ", base_CPC_Process_Shop=" + base_CPC_Process_Shop
				+ ", mapped_Employee_ID=" + mapped_Employee_ID + ", mapped_Employee_Name=" + mapped_Employee_Name
				+ ", inbound_OutBound_Type=" + inbound_OutBound_Type + ", mapped_Sol_IDs=" + mapped_Sol_IDs
				+ ", counsellor_IDs=" + counsellor_IDs + ", tSM_ID=" + tSM_ID + ", tSM_Name=" + tSM_Name
				+ ", child_Allowed=" + child_Allowed + ", operating_Locations=" + operating_Locations + ", role_Code="
				+ role_Code + ", vSTS_ID=" + vSTS_ID + ", vSTS_Name=" + vSTS_Name + ", vSTS_Status=" + vSTS_Status
				+ ", product=" + product + ", rowNumber=" + rowNumber + ", errorMessage=" + errorMessage
				+ ", createdBy=" + createdBy + ", createdDate=" + createdDate + ", UploadId=" + UploadId + "]";
	}

	public ILensChannelMasterError() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
}
