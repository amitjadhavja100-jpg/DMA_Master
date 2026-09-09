package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "TM_VHL_RCAS_PROCESS_SHOP_DUMP")
public class RCA_ProcessShop {

	@Id
	@Column(name = "RCAS_ID")
	private String rcasId;

	@Column(name = "CUSTOMER_NAME", length = 150)
	private String customerName;

	@Column(name = "LAN_NO", length = 150)
	private String lanNo;

	@Column(name = "LEAD_ID", length = 150)
	private String leadId;

	/* @Temporal(TemporalType.DATE) */
	@Column(name = "SYSTEM_CREATED_DATE", length = 150)
	private Date systemCreatedDate;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", length = 150)
	private Date createdDate;

	@Column(name = "CREATED_BY", length = 150)
	private String createdBy;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", length = 150)
	private Date entryDate;

	@Column(name = "ACTIVITYNAME", length = 150)
	private String activityName;

	@Column(name = "STATUS", length = 150)
	private String status;

	@Column(name = "TRAY", length = 150)
	private String tray;

	@Column(name = "BROKER_NAME", length = 150)
	private String brokerName;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DISBURSAL_DATE", length = 150)
	private Date disbursalDate;

	@Column(name = "MOBILE", length = 150)
	private String Mobile;

	@Column(name = "EMPLOYER_ID", length = 150)
	private String employerId;

	@Column(name = "EMPLOYER_NAME", length = 150)
	private String employerName;

	@Column(name = "CASE_STATUS", length = 150)
	private String caseStatus;

	@Column(name = "RM_NAME", length = 150)
	private String rmName;

	@Column(name = "DME_CODE", length = 150)
	private String dmeCode;

	@Column(name = "DME_NAME", length = 150)
	private String dmeName;

	@Column(name = "HUB_NAME", length = 150)
	private String hubName;

	@Column(name = "LOAN_AMNT", length = 150)
	private String loanAmnt;

	@Column(name = "PROCESS_SHOP", length = 150)
	private String processShop;

	@Column(name = "BRANCH_NAME", length = 150)
	private String branchName;

	@Column(name = "SCHEME_NAME", length = 150)
	private String schemeName;

	@Column(name = "PROMOTION_CODE", length = 150)
	private String promotionCode;

	@Column(name = "CHANNEL_CODE", length = 150)
	private String channelCode;

	@Column(name = "PSL_DESCRIPTION", length = 150)
	private String pslDescription;

	@Column(name = "PSL_TYPE", length = 150)
	private String pslType;

	@Column(name = "PRECFOC", length = 150)
	private String precfoc;

	@Column(name = "LAR_COMMENTS", length = 150)
	private String larComments;

	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
	private Date fromCycleDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "TO_CYCLE_DATE")
	private Date toCycleDate;

	@Column(name="UPLOAD_ID")
	private String uploadId;
	
	@Column(name="FILE_NAME")
	private String fileName;

	public String getRcasId() {
		return rcasId;
	}

	public void setRcasId(String rcasId) {
		this.rcasId = rcasId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLanNo() {
		return lanNo;
	}

	public void setLanNo(String lanNo) {
		this.lanNo = lanNo;
	}

	public String getLeadId() {
		return leadId;
	}

	public void setLeadId(String leadId) {
		this.leadId = leadId;
	}

	public Date getSystemCreatedDate() {
		return systemCreatedDate;
	}

	public void setSystemCreatedDate(Date systemCreatedDate) {
		this.systemCreatedDate = systemCreatedDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTray() {
		return tray;
	}

	public void setTray(String tray) {
		this.tray = tray;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public Date getDisbursalDate() {
		return disbursalDate;
	}

	public void setDisbursalDate(Date disbursalDate) {
		this.disbursalDate = disbursalDate;
	}

	public String getMobile() {
		return Mobile;
	}

	public void setMobile(String mobile) {
		Mobile = mobile;
	}

	public String getEmployerId() {
		return employerId;
	}

	public void setEmployerId(String employerId) {
		this.employerId = employerId;
	}

	public String getEmployerName() {
		return employerName;
	}

	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}

	public String getCaseStatus() {
		return caseStatus;
	}

	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}

	public String getRmName() {
		return rmName;
	}

	public void setRmName(String rmName) {
		this.rmName = rmName;
	}

	public String getDmeCode() {
		return dmeCode;
	}

	public void setDmeCode(String dmeCode) {
		this.dmeCode = dmeCode;
	}

	public String getDmeName() {
		return dmeName;
	}

	public void setDmeName(String dmeName) {
		this.dmeName = dmeName;
	}

	public String getHubName() {
		return hubName;
	}

	public void setHubName(String hubName) {
		this.hubName = hubName;
	}

	public String getLoanAmnt() {
		return loanAmnt;
	}

	public void setLoanAmnt(String loanAmnt) {
		this.loanAmnt = loanAmnt;
	}

	public String getProcessShop() {
		return processShop;
	}

	public void setProcessShop(String processShop) {
		this.processShop = processShop;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getPromotionCode() {
		return promotionCode;
	}

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getPslDescription() {
		return pslDescription;
	}

	public void setPslDescription(String pslDescription) {
		this.pslDescription = pslDescription;
	}

	public String getPslType() {
		return pslType;
	}

	public void setPslType(String pslType) {
		this.pslType = pslType;
	}

	public String getPrecfoc() {
		return precfoc;
	}

	public void setPrecfoc(String precfoc) {
		this.precfoc = precfoc;
	}

	public String getLarComments() {
		return larComments;
	}

	public void setLarComments(String larComments) {
		this.larComments = larComments;
	}

	public Date getFromCycleDate() {
		return fromCycleDate;
	}

	public void setFromCycleDate(Date fromCycleDate) {
		this.fromCycleDate = fromCycleDate;
	}

	public Date getToCycleDate() {
		return toCycleDate;
	}

	public void setToCycleDate(Date toCycleDate) {
		this.toCycleDate = toCycleDate;
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

	public RCA_ProcessShop(String rcasId, String customerName, String lanNo, String leadId, Date systemCreatedDate,
			Date createdDate, String createdBy, Date entryDate, String activityName, String status, String tray,
			String brokerName, Date disbursalDate, String mobile, String employerId, String employerName,
			String caseStatus, String rmName, String dmeCode, String dmeName, String hubName, String loanAmnt,
			String processShop, String branchName, String schemeName, String promotionCode, String channelCode,
			String pslDescription, String pslType, String precfoc, String larComments, Date fromCycleDate,
			Date toCycleDate, String uploadId, String fileName) {
		super();
		this.rcasId = rcasId;
		this.customerName = customerName;
		this.lanNo = lanNo;
		this.leadId = leadId;
		this.systemCreatedDate = systemCreatedDate;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.entryDate = entryDate;
		this.activityName = activityName;
		this.status = status;
		this.tray = tray;
		this.brokerName = brokerName;
		this.disbursalDate = disbursalDate;
		Mobile = mobile;
		this.employerId = employerId;
		this.employerName = employerName;
		this.caseStatus = caseStatus;
		this.rmName = rmName;
		this.dmeCode = dmeCode;
		this.dmeName = dmeName;
		this.hubName = hubName;
		this.loanAmnt = loanAmnt;
		this.processShop = processShop;
		this.branchName = branchName;
		this.schemeName = schemeName;
		this.promotionCode = promotionCode;
		this.channelCode = channelCode;
		this.pslDescription = pslDescription;
		this.pslType = pslType;
		this.precfoc = precfoc;
		this.larComments = larComments;
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public RCA_ProcessShop() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RCA_ProcessShop [rcasId=" + rcasId + ", customerName=" + customerName + ", lanNo=" + lanNo + ", leadId="
				+ leadId + ", systemCreatedDate=" + systemCreatedDate + ", createdDate=" + createdDate + ", createdBy="
				+ createdBy + ", entryDate=" + entryDate + ", activityName=" + activityName + ", status=" + status
				+ ", tray=" + tray + ", brokerName=" + brokerName + ", disbursalDate=" + disbursalDate + ", Mobile="
				+ Mobile + ", employerId=" + employerId + ", employerName=" + employerName + ", caseStatus="
				+ caseStatus + ", rmName=" + rmName + ", dmeCode=" + dmeCode + ", dmeName=" + dmeName + ", hubName="
				+ hubName + ", loanAmnt=" + loanAmnt + ", processShop=" + processShop + ", branchName=" + branchName
				+ ", schemeName=" + schemeName + ", promotionCode=" + promotionCode + ", channelCode=" + channelCode
				+ ", pslDescription=" + pslDescription + ", pslType=" + pslType + ", precfoc=" + precfoc
				+ ", larComments=" + larComments + ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate
				+ ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}
	
	
	
}
