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
@Table(name = "TM_VHL_MODEL_MST_ERROR")
public class ModelMasterError {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "model_error_seq")
	@SequenceGenerator(name = "model_error_seq", sequenceName = "ISEQ$$_138495", allocationSize = 1)
	@Column(name = "ERROR_ID")
	private Integer errorId;

	@Column(name = "MANUFACTURER_ID", length = 50)
	private String manufacturerId;

	@Column(name = "MANUFACTURER_DESC",length = 150)
	private String manufacturerDesc;

//	@Id
	@Column(name = "MODEL_ID")
	private String modelId;

	@Column(name = "MODEL_DESC", length = 150)
	private String modelDesc;

	@Column(name = "ASSET_CATEGORY", length = 50)
	private String assetCategory;

	@Column(name = "BAND", length = 10)
	private String band;
	
	@Column(name = "ERROR_MSG", length = 4000)
	private String errorMsg;

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createDate;
	
	@Column(name = "ROW_NUMBER", length = 10)
	private Integer rowNumber;

	@Column(name = "UPLOAD_ID", length = 30)
	private String uploadId;

	public Integer getErrorId() {
		return errorId;
	}

	public void setErrorId(Integer errorId) {
		this.errorId = errorId;
	}

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerDesc() {
		return manufacturerDesc;
	}

	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getModelDesc() {
		return modelDesc;
	}

	public void setModelDesc(String modelDesc) {
		this.modelDesc = modelDesc;
	}

	public String getAssetCategory() {
		return assetCategory;
	}

	public void setAssetCategory(String assetCategory) {
		this.assetCategory = assetCategory;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public ModelMasterError(Integer errorId, String manufacturerId, String manufacturerDesc, String modelId,
			String modelDesc, String assetCategory, String band, String errorMsg, String createdBy, Date createDate,
			Integer rowNumber, String uploadId) {
		super();
		this.errorId = errorId;
		this.manufacturerId = manufacturerId;
		this.manufacturerDesc = manufacturerDesc;
		this.modelId = modelId;
		this.modelDesc = modelDesc;
		this.assetCategory = assetCategory;
		this.band = band;
		this.errorMsg = errorMsg;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.rowNumber = rowNumber;
		this.uploadId = uploadId;
	}

	public ModelMasterError() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ModelMasterError [errorId=" + errorId + ", manufacturerId=" + manufacturerId + ", manufacturerDesc="
				+ manufacturerDesc + ", modelId=" + modelId + ", modelDesc=" + modelDesc + ", assetCategory="
				+ assetCategory + ", band=" + band + ", errorMsg=" + errorMsg + ", createdBy=" + createdBy
				+ ", createDate=" + createDate + ", rowNumber=" + rowNumber + ", uploadId=" + uploadId + "]";
	}

	
	
	
}
