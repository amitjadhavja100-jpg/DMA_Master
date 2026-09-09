package com.icici.dma.dto;

import java.util.Date;


public class ProductMasterDto {

	private String productId;

	private String productName;

	private String createdBy;

	private Date createdDate;

	private String modifiedBy;

	private Date modifiedDate;

	private String remark;

	private String vhlProductId;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getVhlProductId() {
		return vhlProductId;
	}

	public void setVhlProductId(String vhlProductId) {
		this.vhlProductId = vhlProductId;
	}

	public ProductMasterDto(String productId, String productName, String createdBy, Date createdDate, String modifiedBy,
			Date modifiedDate, String remark, String vhlProductId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.remark = remark;
		this.vhlProductId = vhlProductId;
	}

	public ProductMasterDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MasterDto [productId=" + productId + ", productName=" + productName + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", remark=" + remark + ", vhlProductId=" + vhlProductId + "]";
	}
	
	

}
