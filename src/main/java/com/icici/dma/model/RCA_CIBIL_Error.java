package com.icici.dma.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TM_VHL_RCAS_CIBIL_DUMP_ERROR")
public class RCA_CIBIL_Error {

	@Column(name = "RCAS_NO")
	private String rcasNo;

	@Column(name = "LAN_NO", length = 150)
	private String lanNo;

	@Column(name = "DISBURSEMENT_DATE")
	private String disbursementDate;

	@Column(name = "CIBIL_SCORE")
	private String cibilScore;

	@Column(name = "APPLICANT_TYPE")
	private String applicantType;

	@Column(name = "CUSTOMER_NAME")
	private String customerName;

	@Column(name = "CUSTOMER_TYPE")
	private String customerType;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "FROM_CYCLE_DATE")
	private Date fromCycleDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "TO_CYCLE_DATE")
	private Date toCycleDate;

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rcas_cibil_error_seq")
//	@SequenceGenerator(name = "rcas_cibil_error_seq", sequenceName = "ISEQ$$_138497", allocationSize = 1)
	@Column(name = "ERROR_ID", length = 10)
	private Integer errorId;

	@Column(name = "ERROR_MSG", length = 150)
	private String errorMsg;

	@Column(name = "ROW_NUMBER", length = 150)
	private Integer rowNumber;

	@Column(name = "UPLOAD_ID", length = 150)
	private String uploadId;

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

	public String getDisbursementDate() {
		return disbursementDate;
	}

	public void setDisbursementDate(String disbursementDate) {
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

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
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

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public RCA_CIBIL_Error(String rcasNo, String lanNo, String disbursementDate, String cibilScore,
			String applicantType, String customerName, String customerType, String createdBy, Date createdDate,
			Date fromCycleDate, Date toCycleDate, Integer errorId, String errorMsg, Integer rowNumber,
			String uploadId) {
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
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.errorId = errorId;
		this.errorMsg = errorMsg;
		this.rowNumber = rowNumber;
		this.uploadId = uploadId;
	}

	public RCA_CIBIL_Error() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RCA_CIBIL_Error [rcasNo=" + rcasNo + ", lanNo=" + lanNo + ", disbursementDate=" + disbursementDate
				+ ", cibilScore=" + cibilScore + ", applicantType=" + applicantType + ", customerName=" + customerName
				+ ", customerType=" + customerType + ", createdBy=" + createdBy + ", createdDate=" + createdDate
				+ ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate + ", errorId=" + errorId
				+ ", errorMsg=" + errorMsg + ", rowNumber=" + rowNumber + ", uploadId=" + uploadId + "]";
	}

}