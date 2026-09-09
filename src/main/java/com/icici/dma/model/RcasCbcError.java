package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_RCAS_CBC_DUMP_ERROR")
public class RcasCbcError {

	@Column(name="CBC_CODE" , length = 20)
	private String cbcCode;
	
	@Column(name="PROCESS_SHOP")
	private String processShop;	
	
	@Temporal(TemporalType.DATE)
    @Column(name = "CREATED_DATE", length = 150)
    private Date createdDate;
 
    @Column(name = "CREATED_BY", length = 50)
    private String createdBy;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FROM_CYCLE_DATE")
    private Date fromCycleDate;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "TO_CYCLE_DATE")
    private Date toCycleDate;
    
    
    @Column(name = "ERROR_MSG", length = 50)
    private String errorMsg;
    
    @Id
    @Column(name = "ERROR_ID", length = 50)
    private String errorid;
    
    @Column(name = "ROW_NUMBER", length = 50)
    private Integer rowNumber;
    
    @Column(name = "UPLOAD_ID", length = 50)
    private String uploadId;

	public String getCbcCode() {
		return cbcCode;
	}

	public void setCbcCode(String cbcCode) {
		this.cbcCode = cbcCode;
	}

	public String getProcessShop() {
		return processShop;
	}

	public void setProcessShop(String processShop) {
		this.processShop = processShop;
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

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorid() {
		return errorid;
	}

	public void setErrorid(String errorid) {
		this.errorid = errorid;
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

	public RcasCbcError(String cbcCode, String processShop, Date createdDate, String createdBy, Date fromCycleDate,
			Date toCycleDate, String errorMsg, String errorid, Integer rowNumber, String uploadId) {
		super();
		this.cbcCode = cbcCode;
		this.processShop = processShop;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.errorMsg = errorMsg;
		this.errorid = errorid;
		this.rowNumber = rowNumber;
		this.uploadId = uploadId;
	}

	public RcasCbcError() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RCASCbcError [cbcCode=" + cbcCode + ", processShop=" + processShop + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate
				+ ", errorMsg=" + errorMsg + ", errorid=" + errorid + ", rowNumber=" + rowNumber + ", uploadId="
				+ uploadId + "]";
	}
    
    
    
    
}
