package com.icici.dma.slabEntity;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTO_IPROCESS_CHENNAI_USED_STRUCTURE")
public class AutoManipalChennaiUsedStructure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "STATE")
	private String state;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "SLAB_FROM")
	private Integer slabFrom;

	@Column(name = "SLAB_TO")
	private Integer slabTo;

	@Column(name = "PERCENTAGE")
	private Double percentage;

	@Column(name = "FIXED_AMOUNT")
	private BigDecimal fixedAmount;

	@Column(name = "PER_CASE_CAPPING")
	private BigDecimal perCaseCapping;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "DESIGNATION")
	private String designation;

	@Column(name = "CYCLE_FROM_DATE")
	private String cycleFromDate;

	@Column(name = "CYCLE_TO_DATE")
	private String cycleToDate;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CHECKED_BY")
	private String checkedBy;

	@Column(name = "CHECKED_DATE")
	private LocalDate checkedDate;

	@Column(name = "SEQUENCE")
	private Integer sequence;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSlabFrom() {
		return slabFrom;
	}

	public void setSlabFrom(Integer slabFrom) {
		this.slabFrom = slabFrom;
	}

	public Integer getSlabTo() {
		return slabTo;
	}

	public void setSlabTo(Integer slabTo) {
		this.slabTo = slabTo;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public BigDecimal getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(BigDecimal fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public BigDecimal getPerCaseCapping() {
		return perCaseCapping;
	}

	public void setPerCaseCapping(BigDecimal perCaseCapping) {
		this.perCaseCapping = perCaseCapping;
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

	public String getCycleFromDate() {
		return cycleFromDate;
	}

	public void setCycleFromDate(String cycleFromDate) {
		this.cycleFromDate = cycleFromDate;
	}

	public String getCycleToDate() {
		return cycleToDate;
	}

	public void setCycleToDate(String cycleToDate) {
		this.cycleToDate = cycleToDate;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public LocalDate getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(LocalDate checkedDate) {
		this.checkedDate = checkedDate;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}
	
	

}
