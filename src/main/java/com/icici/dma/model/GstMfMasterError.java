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
@Table(name = "TM_VHL_GST_MF_MST_ERROR")
public class GstMfMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_MF_ERROR_SEQ")
	@SequenceGenerator(name = "GST_MF_ERROR_SEQ", sequenceName = "TM_VHL_GST_MF_ERROR_ID_SEQ", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Long errorId;

	@Column(name = "APSCODE")
	private String apsCode;

	@Column(name = "NAME")
	private String name;

	@Column(name = "STATE")
	private String state;

//	@Column(name = "LOCATION")
//	private String location;
//
//	@Column(name = "ADDRESS")
//	private String address;
//
//	@Column(name = "DD")
//	private String dd;
//
//	@Column(name = "LOCATIONMF")
//	private String locationMf;

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

	public String getApsCode() {
		return apsCode;
	}

	public void setApsCode(String apsCode) {
		this.apsCode = apsCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
