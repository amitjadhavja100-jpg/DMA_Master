package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "TM_VHL_RCAS_CBC_DUMP")
public class RCA_CBC {
	
	@Id
	@Column(name="CBC_CODE" , length = 150, nullable = false)
	private String cbcCode;
	
	@Column(name="PROCESS_SHOP", length = 150)
	private String processShop;	
	
	/*@Temporal(TemporalType.DATE)*/
    @Column(name = "CREATED_DATE", length = 150)
    private Date createdDate;
 
    @Column(name = "CREATED_BY", length = 150)
    private String createdBy;
    
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

	public RCA_CBC(String cbcCode, String processShop, Date createdDate, String createdBy, Date fromCycleDate,
			Date toCycleDate, String uploadId, String fileName) {
		super();
		this.cbcCode = cbcCode;
		this.processShop = processShop;
		this.createdDate = createdDate;
		this.createdBy = createdBy;
		this.fromCycleDate = fromCycleDate;
		this.toCycleDate = toCycleDate;
		this.uploadId = uploadId;
		this.fileName = fileName;
	}

	public RCA_CBC() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "RCA_CBC [cbcCode=" + cbcCode + ", processShop=" + processShop + ", createdDate=" + createdDate
				+ ", createdBy=" + createdBy + ", fromCycleDate=" + fromCycleDate + ", toCycleDate=" + toCycleDate
				+ ", uploadId=" + uploadId + ", fileName=" + fileName + "]";
	}
	
	
}
