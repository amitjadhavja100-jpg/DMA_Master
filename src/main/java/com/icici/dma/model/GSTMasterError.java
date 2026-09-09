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
@Table(name = "TM_VHL_GST_MST_ERROR")
public class GSTMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gst_error_seq")
	@SequenceGenerator(name = "gst_error_seq", sequenceName = "ISEQ$$_138344", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Integer errorId;

	@Column(name = "APS_CODE", length = 150)
	private String apsCode;

	@Column(name = "NAME", length = 150)
	private String name;

	@Column(name = "STATE", length = 50)
	private String state;

	@Column(name = "LOCATION", length = 50)
	private String location;

	@Column(name = "ERROR_MSG", length = 4000)
	private String errorMsg;

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;

	@Column(name = "UPLOAD_ID", length = 30)
	private String uploadId;

	@Column(name = "ROW_NUMBER", length = 10)
	private Integer rowNumber;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
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

	public GSTMasterError(Integer errorId, String apsCode, String name, String state, String location, String errorMsg,
			String createdBy, Date createdDate, String uploadId, Integer rowNumber) {
		super();
		this.errorId = errorId;
		this.apsCode = apsCode;
		this.name = name;
		this.state = state;
		this.location = location;
		this.errorMsg = errorMsg;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.uploadId = uploadId;
		this.rowNumber = rowNumber;
	}

	public GSTMasterError() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "GSTMasterError [errorId=" + errorId + ", apsCode=" + apsCode + ", name=" + name + ", state=" + state
				+ ", location=" + location + ", errorMsg=" + errorMsg + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", uploadId=" + uploadId + ", rowNumber=" + rowNumber + "]";
	}

	

}
