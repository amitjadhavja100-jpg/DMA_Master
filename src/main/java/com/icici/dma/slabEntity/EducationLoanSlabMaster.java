package com.icici.dma.slabEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.*;
 
@Entity
@Table(name = "EDUCATION_LOAN_SLAB_MASTER")
public class EducationLoanSlabMaster {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SLAB_ID")
    private Long slabId;
 
    @Column(name = "PRODUCT_CATEGORY", nullable = false)
    private String productCategory;
 
    @Column(name = "BUCKET_CODE", nullable = false)
    private String bucketCode;
 
    @Column(name = "MIN_AMOUNT")
    private BigDecimal minAmount;
 
    @Column(name = "MAX_AMOUNT")
    private BigDecimal maxAmount;
 
    @Column(name = "DESCRIPTION")
    private String description;
 
    @Column(name = "PAYOUT_PERCENTAGE", nullable = false)
    private BigDecimal payoutPercentage;
 
    @Column(name = "RECORD_STATUS", nullable = false)
    private String recordStatus = "APPROVED";
 
    @Column(name = "CREATED_BY", nullable = false)
    private String createdBy;
 
    @Column(name = "CREATED_DATE", nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();
 
    @Column(name = "CHECKER_BY")
    private String checkerBy;
 
    @Column(name = "CHECKER_DATE")
    private LocalDateTime checkerDate;
 
	// Getters and Setters
	// ... (Generate Getters/Setters)
	public Long getSlabId() {
		return slabId;
	}

	public void setSlabId(Long slabId) {
		this.slabId = slabId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getBucketCode() {
		return bucketCode;
	}

	public void setBucketCode(String bucketCode) {
		this.bucketCode = bucketCode;
	}

	public BigDecimal getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(BigDecimal minAmount) {
		this.minAmount = minAmount;
	}

	public BigDecimal getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(BigDecimal maxAmount) {
		this.maxAmount = maxAmount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPayoutPercentage() {
		return payoutPercentage;
	}

	public void setPayoutPercentage(BigDecimal payoutPercentage) {
		this.payoutPercentage = payoutPercentage;
	}

	public String getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getCheckerBy() {
		return checkerBy;
	}

	public void setCheckerBy(String checkerBy) {
		this.checkerBy = checkerBy;
	}

	public LocalDateTime getCheckerDate() {
		return checkerDate;
	}

	public void setCheckerDate(LocalDateTime checkerDate) {
		this.checkerDate = checkerDate;
	}
}
