package com.icici.dma.model;


import java.util.Date;

import javax.persistence.*;
 
@Entity
@Table(name = "TM_VHL_RCAS_CIBIL_DUMP")
public class RCA_CIBIL {
 
    @Id
    @Column(name = "RCAS_NO", length = 150, nullable = false)
    private String rcasNo;
 
    @Column(name = "LAN_NO", length = 150)
    private String lanNo;
 
    @Temporal(TemporalType.DATE)
    @Column(name = "DISBURSEMENT_DATE")
    private Date disbursementDate;
 
    @Column(name = "CIBIL_SCORE", length = 50)
    private String cibilScore;
 
    @Column(name = "APPLICANT_TYPE", length = 1)
    private String applicantType;
 
    @Column(name = "CUSTOMER_NAME", length = 255)
    private String customerName;
 
    @Column(name = "CUSTOMER_TYPE", length = 1)
    private String customerType;
 
    @Column(name = "CREATED_BY", length = 150)
    private String createdBy;
 
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE")
    private Date createdDate;
 
    @Column(name = "MODIFIED_BY", length = 150)
    private String modifiedBy;
 
    @Temporal(TemporalType.DATE)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;
 
    @Column(name = "REMARKS", length = 255)
    private String remarks;
 
    @Column(name = "STATUS", length = 100)
    private String status;
 
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

	public String getRcasNo() {
		return rcasNo;
	}

	public void setRcasNo(String rcasNo) {
		this.rcasNo = rcasNo;
	}

	public String getLanNo() {
		return lanNo;
	}

	public void setLanNo(String lanNo) {
		this.lanNo = lanNo;
	}

	public Date getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(Date disbursementDate) {
		this.disbursementDate = disbursementDate;
	}

	public String getCibilScore() {
		return cibilScore;
	}

	public void setCibilScore(String cibilScore) {
		this.cibilScore = cibilScore;
	}

	public String getApplicantType() {
		return applicantType;
	}

	public void setApplicantType(String applicantType) {
		this.applicantType = applicantType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
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

	public RCA_CIBIL(String rcasNo, String lanNo, Date disbursementDate, String cibilScore, String applicantType,
			String customerName, String customerType, String createdBy, Date createdDate, String modifiedBy,
			Date modifiedDate, String remarks, String status, Date fromCycleDate, Date toCycleDate, String uploadId,
			String fileName) {
		super();
		this.rcasNo = rcasNo;
		this.lanNo = lanNo;
		this.disbursementDate = disbursementDate;
		this.cibilScore = cibilScore;
		this.applicantType = applicantType;
		this.customerName = customerName;
		this.customerType = customerType;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remarks = remarks;
		this.status = status;
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public RCA_CIBIL() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RCA_CIBIL [rcasNo=" + rcasNo + ", lanNo=" + lanNo + ", disbursementDate=" + disbursementDate
				+ ", cibilScore=" + cibilScore + ", applicantType=" + applicantType + ", customerName=" + customerName
				+ ", customerType=" + customerType + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", remarks=" + remarks + ", status="
				+ status + ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate + ", uploadId="
				+ uploadId + ", fileName=" + fileName + "]";
	}
	
	
}