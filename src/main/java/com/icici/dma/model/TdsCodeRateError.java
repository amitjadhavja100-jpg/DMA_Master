package com.icici.dma.model;

import java.util.Date;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
 
import lombok.Data;
 
@Data
@Entity
@Table(name = "TDS_CODE_RATE_ERROR")
public class TdsCodeRateError {
 
    @Id
    @Column(name = "ERROR_ID")
    private Long errorId;
 
    @Column(name = "WTAX_TYPE")
    private String wtaxType;
 
    @Column(name = "WTX")
    private String wtx;
 
    @Column(name = "TDS_RATE")
    private String tdsRate;
 
    @Column(name = "ERROR_MSG")
    private String errorMsg;
 
    @Column(name = "UPLOAD_ID")
    private String uploadId;
 
    @Column(name = "ROW_NUMBER")
    private Long rowNumber;
 
    @Column(name = "CREATED_BY")
    private String createdBy;
 
    @Column(name = "CREATED_DATE")
    private Date createdDate;

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getWtaxType() {
		return wtaxType;
	}

	public void setWtaxType(String wtaxType) {
		this.wtaxType = wtaxType;
	}

	public String getWtx() {
		return wtx;
	}

	public void setWtx(String wtx) {
		this.wtx = wtx;
	}

	public String getTdsRate() {
		return tdsRate;
	}

	public void setTdsRate(String tdsRate) {
		this.tdsRate = tdsRate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public Long getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Long rowNumber) {
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

	
    
}
 