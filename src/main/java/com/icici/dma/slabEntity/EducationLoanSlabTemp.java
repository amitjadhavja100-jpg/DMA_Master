package com.icici.dma.slabEntity;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.*;


 
@Entity
@Table(name = "EDUCATION_LOAN_SLAB")
public class EducationLoanSlabTemp {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TEMP_ID")
    private Long tempId;
 
    @Column(name = "PRODUCT_CATEGORY", nullable = false)
    private String productCategory;
 
    @Column(name = "BUCKET_CODE", nullable = false)
    private String bucketCode;
 
	/*
	 * @Column(name = "MIN_AMOUNT") private BigDecimal minAmount;
	 * 
	 * @Column(name = "MAX_AMOUNT") private BigDecimal maxAmount;
	 */
    
    @Column(name = "MIN_AMOUNT", precision = 15, scale = 2)
    private BigDecimal minAmount;
     
    @Column(name = "MAX_AMOUNT", precision = 15, scale = 2)
    private BigDecimal maxAmount;
 
    @Column(name = "DESCRIPTION")
    private String description;
 
	/*
	 * @Column(name = "PAYOUT_PERCENTAGE", nullable = false) private BigDecimal
	 * payoutPercentage;
	 */
    
    @Column(name = "PAYOUT_PERCENTAGE", precision = 10, scale = 2)
    private BigDecimal payoutPercentage;
    
    @Column(name = "RECORD_STATUS", nullable = false)
    private String recordStatus = "PENDING_APPROVAL";
 
    @Column(name = "MAKER_ID", nullable = false)
    private String makerId;
 
    @Column(name = "MAKER_DATE", nullable = false)
    private LocalDateTime makerDate = LocalDateTime.now();
 
    @Column(name = "CHECKER_ID")
    private String checkerId;
 
    @Column(name = "CHECKER_DATE")
    private LocalDateTime checkerDate;
 
    @Column(name = "REJECTION_REASON")
    private String rejectionReason;
 
    
    @Column(name = "CYCLE_FROM_DATE")
    private Date cycleFromDate;
    
    public Date getCycleFromDate() {
		return cycleFromDate;
	}

	public void setCycleFromDate(Date cycleFromDate) {
		this.cycleFromDate = cycleFromDate;
	}

	public Date getCycleToDate() {
		return cycleToDate;
	}

	public void setCycleToDate(Date cycleToDate) {
		this.cycleToDate = cycleToDate;
	}

	@Column(name = "CYCLE_TO_DATE")
    private Date cycleToDate;
    
	// Getters and Setters
	// ... (Generate Getters/Setters standard)
	public Long getTempId() {
		return tempId;
	}

	public void setTempId(Long tempId) {
		this.tempId = tempId;
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

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public LocalDateTime getMakerDate() {
		return makerDate;
	}

	public void setMakerDate(LocalDateTime makerDate) {
		this.makerDate = makerDate;
	}

	public String getCheckerId() {
		return checkerId;
	}

	public void setCheckerId(String checkerId) {
		this.checkerId = checkerId;
	}

	public LocalDateTime getCheckerDate() {
		return checkerDate;
	}

	public void setCheckerDate(LocalDateTime checkerDate) {
		this.checkerDate = checkerDate;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}
}
 