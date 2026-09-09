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
@Table(name = "tm_vhl_model_mst")
public class ModelMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "temp_seq_gen")
	@SequenceGenerator(name = "temp_seq_gen", sequenceName = "ISEQ$$_136535", allocationSize = 1)
	@Column(name = "TEMP_ID")
	private Integer tempId;

	@Column(name = "MANUFACTURER_ID")
	private Integer manufacturerId;

	@Column(name = "MANUFACTURER_DESC", length = 150)
	private String manufacturerDesc;

//	@Id
	@Column(name = "MODEL_ID")
	private Integer modelId;

	@Column(name = "MODEL_DESC", length = 150)
	private String modelDesc;

	@Column(name = "ASSET_CATEGORY", length = 50)
	private String assetCategory;

	@Column(name = "BAND", length = 10)
	private String band;

	@Column(name = "CREATED_BY", length = 50)
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createDate;

	@Column(name = "MODIFIED_BY", length = 50)
	private String modifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;


	@Column(name = "STATUS", length = 1)
	private String status;


	public Integer getTempId() {
		return tempId;
	}


	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}


	public Integer getManufacturerId() {
		return manufacturerId;
	}


	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}


	public String getManufacturerDesc() {
		return manufacturerDesc;
	}


	public void setManufacturerDesc(String manufacturerDesc) {
		this.manufacturerDesc = manufacturerDesc;
	}


	public Integer getModelId() {
		return modelId;
	}


	public void setModelId(Integer modelId) {
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


	public String getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


	public Date getModifiedDate() {
		return modifiedDate;
	}


	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public ModelMaster(Integer tempId, Integer manufacturerId, String manufacturerDesc, Integer modelId,
			String modelDesc, String assetCategory, String band, String createdBy, Date createDate, String modifiedBy,
			Date modifiedDate, String status) {
		super();
		this.tempId = tempId;
		this.manufacturerId = manufacturerId;
		this.manufacturerDesc = manufacturerDesc;
		this.modelId = modelId;
		this.modelDesc = modelDesc;
		this.assetCategory = assetCategory;
		this.band = band;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.status = status;
	}


	public ModelMaster() {
		super();
		// TODO Auto-generated constructor stub
	}


	@Override
	public String toString() {
		return "ModelMaster [tempId=" + tempId + ", manufacturerId=" + manufacturerId + ", manufacturerDesc="
				+ manufacturerDesc + ", modelId=" + modelId + ", modelDesc=" + modelDesc + ", assetCategory="
				+ assetCategory + ", band=" + band + ", createdBy=" + createdBy + ", createDate=" + createDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", status=" + status + "]";
	}

	
	
	
}
