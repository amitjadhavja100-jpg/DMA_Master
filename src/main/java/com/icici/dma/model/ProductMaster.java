package com.icici.dma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TM_VHL_PRODUCT_MST")
public class ProductMaster {
	
	@Id
	@Column(name = "PRODUCT_ID")
	private String productId;
	
	@Column(name = "PRODUCT_NAME")
	private String productName;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "REMARKS")
	private String remark;
	
	@Column(name = "VHL_PROD_ID")
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

	public ProductMaster(String productId, String productName, String createdBy, Date createdDate, String modifiedBy,
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

	public ProductMaster() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Master [productId=" + productId + ", productName=" + productName + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", modifiedBy=" + modifiedBy + ", modifiedDate=" + modifiedDate
				+ ", remark=" + remark + ", vhlProductId=" + vhlProductId + "]";
	}
	
	
	
	
	
	
}
