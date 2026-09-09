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
@Table(name = "TM_VHL_GST_TO_MST_ERROR")
public class GstToMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_TO_ERROR_SEQ")
	@SequenceGenerator(name = "GST_TO_ERROR_SEQ", sequenceName = "TM_VHL_GST_TO_ERROR_ID_SEQ", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Long errorId;

	@Column(name = "PROCESS_SHOP")
	private String processShop;

	@Column(name = "GST_STATE_TO")
	private String gstStateTo;

	@Column(name = "ROW_NUMBER")
	private Integer rowNumber;

	@Column(name = "ERROR_MESSAGE")
	private String errorMessage;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPLOAD_ID")
	private String uploadId;

	public Long getErrorId() {
		return errorId;
	}

	public void setErrorId(Long errorId) {
		this.errorId = errorId;
	}

	public String getProcessShop() {
		return processShop;
	}

	public void setProcessShop(String processShop) {
		this.processShop = processShop;
	}

	public String getGstStateTo() {
		return gstStateTo;
	}

	public void setGstStateTo(String gstStateTo) {
		this.gstStateTo = gstStateTo;
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
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

}
