package com.icici.dma.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TM_CCR_SAP_MST_ERROR")
public class SAPMasterError {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sap_error_seq")
    @SequenceGenerator(
        name = "sap_error_seq",
        sequenceName = "SAP_ERROR_SEQ",
        allocationSize = 1
    )
    @Column(name = "ERROR_ID")
    private Integer errorId;

    @Column(name = "SR_NO")
    private String srNo;

    @Column(name = "UNIT_CODE")
    private String unitCode;

    @Column(name = "CLEAN_UNIT_ID")
    private String cleanUnitId;

    @Column(name = "VENDOR_NAME")
    private String vendorName;

    @Column(name = "CCA_CALL_CENTER")
    private String ccaCallCenter;

    @Column(name = "PAN")
    private String pan;

    @Column(name = "SAP_VENDOR_CODE")
    private String sapVendorCode;

    @Column(name = "STATE")
    private String state;

    @Column(name = "TDS_RATE")
    private String tdsRate;

    @Column(name = "TAX_CODE")
    private String taxCode;

    @Column(name = "FINALI_BOXIDS")
    private String finaliBoxids;

    @Column(name = "STATUS_OF_BLOCKING")
    private String statusOfBlocking;

    @Column(name = "ACCOUNT_STATUS")
    private String accountStatus;

    @Column(name = "CRED_INFO_NO")
    private String credInfoNo;

    @Column(name = "GSTN_NO")
    private String gstnNo;

    @Column(name = "SAC_CODE")
    private String sacCode;

    @Column(name = "SERVICE_PROVIDER_ID_STATUS")
    private String serviceProviderIdStatus;

    @Column(name = "GST_APPLICABLE")
    private String gstApplicable;

    @Column(name = "HOLD_STATUS")
    private String holdStatus;
    
    @Column(name = "PAYMENT_MODE")
    private String paymentMode;

    @Column(name = "ERROR_MSG")
    private String errorMsg;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "UPLOAD_ID")
    private String uploadId;

    @Column(name = "ROW_NUMBER")
    private Integer rowNumber;
    
    

    public SAPMasterError() {
    }

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getSrNo() {
		return srNo;
	}

	public void setSrNo(String srNo) {
		this.srNo = srNo;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	public String getCleanUnitId() {
		return cleanUnitId;
	}

	public void setCleanUnitId(String cleanUnitId) {
		this.cleanUnitId = cleanUnitId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getCcaCallCenter() {
		return ccaCallCenter;
	}

	public void setCcaCallCenter(String ccaCallCenter) {
		this.ccaCallCenter = ccaCallCenter;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getSapVendorCode() {
		return sapVendorCode;
	}

	public void setSapVendorCode(String sapVendorCode) {
		this.sapVendorCode = sapVendorCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(String tdsRate) {
		this.tdsRate = tdsRate;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getFinaliBoxids() {
		return finaliBoxids;
	}

	public void setFinaliBoxids(String finaliBoxids) {
		this.finaliBoxids = finaliBoxids;
	}

	public String getStatusOfBlocking() {
		return statusOfBlocking;
	}

	public void setStatusOfBlocking(String statusOfBlocking) {
		this.statusOfBlocking = statusOfBlocking;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getCredInfoNo() {
		return credInfoNo;
	}

	public void setCredInfoNo(String credInfoNo) {
		this.credInfoNo = credInfoNo;
	}

	public String getGstnNo() {
		return gstnNo;
	}

	public void setGstnNo(String gstnNo) {
		this.gstnNo = gstnNo;
	}

	public String getSacCode() {
		return sacCode;
	}

	public void setSacCode(String sacCode) {
		this.sacCode = sacCode;
	}

	public String getServiceProviderIdStatus() {
		return serviceProviderIdStatus;
	}

	public void setServiceProviderIdStatus(String serviceProviderIdStatus) {
		this.serviceProviderIdStatus = serviceProviderIdStatus;
	}

	public String getGstApplicable() {
		return gstApplicable;
	}

	public void setGstApplicable(String gstApplicable) {
		this.gstApplicable = gstApplicable;
	}

	public String getHoldStatus() {
		return holdStatus;
	}

	public void setHoldStatus(String holdStatus) {
		this.holdStatus = holdStatus;
	}
	
	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
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

	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}

	

}