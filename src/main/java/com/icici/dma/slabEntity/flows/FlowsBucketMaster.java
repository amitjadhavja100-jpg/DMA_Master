package com.icici.dma.slabEntity.flows;

import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name = "TS_FLOWS_BUCKET_MST")
public class FlowsBucketMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flows_bucket_seq")
    @SequenceGenerator(
            name = "flows_bucket_seq",
            sequenceName = "TS_FLOWS_BUCKET_MST_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    
    @Column(name = "CITY_ID")
    private Long cityId;
    
    @Column(name = "BUCKET_TYPE_ID")
    private Long bucketTypeId;
    
    @Column(name = "BUCKET_NAME")
    private String bucketName;

    @Column(name = "TABLE_TYPE")
    private String tableType;
    
    @Column(name = "ORDER_ID")
    private Integer orderId;
    
    @Column(name = "RANGE_REQUIRED")
    private String rangeRequired;

    @Column(name = "STATUS")
    private String status;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCategoryId() {
	    return categoryId;
	}

	public void setCategoryId(Long categoryId) {
	    this.categoryId = categoryId;
	}
	
	public Long getCityId() {
	    return cityId;
	}

	public void setCityId(Long cityId) {
	    this.cityId = cityId;
	}

	public Long getBucketTypeId() {
		return bucketTypeId;
	}

	public void setBucketTypeId(Long bucketTypeId) {
		this.bucketTypeId = bucketTypeId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getRangeRequired() {
		return rangeRequired;
	}

	public void setRangeRequired(String rangeRequired) {
		this.rangeRequired = rangeRequired;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

}