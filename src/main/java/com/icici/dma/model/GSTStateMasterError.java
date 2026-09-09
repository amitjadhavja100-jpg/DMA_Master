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
@Table(name = "TM_VHL_GST_STATE_MST_ERROR")
public class GSTStateMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GST_STATE_ERROR_SEQ")
	@SequenceGenerator(name = "GST_STATE_ERROR_SEQ", sequenceName = "TM_VHL_GST_STATE_ERROR_ID_SEQ", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Long errorId;

	@Column(name = "PARTNER_ID")
	private String partnerId;

	@Column(name = "PARTNER_NAME")
	private String partnerName;

//	@Column(name = "TOTAL")
//	private String total;
//
//	@Column(name = "ADDRESS1")
//	private String address1;
//
//	@Column(name = "ADDRESS2")
//	private String address2;
//
//	@Column(name = "ADDRESS3")
//	private String address3;
//
//	@Column(name = "CITY")
//	private String city;
//
//	@Column(name = "PINCODE")
//	private String pincode;
//
//	@Column(name = "STATE")
//	private String state;

	@Column(name = "GST_STATE")
	private String gstState;

//	@Column(name = "OLD_ADDRESS")
//	private String oldAddress;

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

	public Long getErrorId() { return errorId; }
	public void setErrorId(Long errorId) { this.errorId = errorId; }

	public String getPartnerId() { return partnerId; }
	public void setPartnerId(String partnerId) { this.partnerId = partnerId; }

	public String getPartnerName() { return partnerName; }
	public void setPartnerName(String partnerName) { this.partnerName = partnerName; }
	
	public String getGstState() { return gstState; }
	public void setGstState(String gstState) { this.gstState = gstState; }

	public Integer getRowNumber() { return rowNumber; }
	public void setRowNumber(Integer rowNumber) { this.rowNumber = rowNumber; }

	public String getErrorMessage() { return errorMessage; }
	public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

	public String getCreatedBy() { return createdBy; }
	public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

	public Date getCreatedDate() { return createdDate; }
	public void setCreatedDate(Date createdDate) { this.createdDate = createdDate; }

	public String getUploadId() { return uploadId; }
	public void setUploadId(String uploadId) { this.uploadId = uploadId; }
}
