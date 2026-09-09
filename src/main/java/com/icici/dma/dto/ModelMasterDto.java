package com.icici.dma.dto;

import java.util.Date;

public class ModelMasterDto {

	private Integer tempId;
	private Integer ManufacturerId;
	private String ManufacturerDesc;
	private Integer modelId;
	private String modelDesc;
	private String assetCategory;
	private String band;
	private String createdBy;
	private Date createDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String remarks;
	private String status;
	private String actionType;
	private Date actionDate;
	private String actionUser;

	public Integer getTempId() {
		return tempId;
	}

	public void setTempId(Integer tempId) {
		this.tempId = tempId;
	}

	public Integer getManufacturerId() {
		return ManufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		ManufacturerId = manufacturerId;
	}

	public String getManufacturerDesc() {
		return ManufacturerDesc;
	}

	public void setManufacturerDesc(String manufacturerDesc) {
		ManufacturerDesc = manufacturerDesc;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getActionDate() {
		return actionDate;
	}

	public void setActionDate(Date actionDate) {
		this.actionDate = actionDate;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}

	public ModelMasterDto(Integer tempId, Integer manufacturerId, String manufacturerDesc, Integer modelId,
			String modelDesc, String assetCategory, String band, String createdBy, Date createDate, String modifiedBy,
			Date modifiedDate, String remarks, String status, String actionType, Date actionDate, String actionUser) {
		super();
		this.tempId = tempId;
		ManufacturerId = manufacturerId;
		ManufacturerDesc = manufacturerDesc;
		this.modelId = modelId;
		this.modelDesc = modelDesc;
		this.assetCategory = assetCategory;
		this.band = band;
		this.createdBy = createdBy;
		this.createDate = createDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remarks = remarks;
		this.status = status;
		this.actionType = actionType;
		this.actionDate = actionDate;
		this.actionUser = actionUser;
	}

	public ModelMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "ModelMasterDto [tempId=" + tempId + ", ManufacturerId=" + ManufacturerId + ", ManufacturerDesc="
				+ ManufacturerDesc + ", modelId=" + modelId + ", modelDesc=" + modelDesc + ", assetCategory="
				+ assetCategory + ", band=" + band + ", createdBy=" + createdBy + ", createDate=" + createDate
				+ ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate + ", remarks=" + remarks + ", status="
				+ status + ", actionType=" + actionType + ", actionDate=" + actionDate + ", actionUser=" + actionUser
				+ "]";
	}

}
