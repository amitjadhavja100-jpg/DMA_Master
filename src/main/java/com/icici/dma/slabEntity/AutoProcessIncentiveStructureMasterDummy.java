package com.icici.dma.slabEntity;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTO_IPROCESS_KERALA_INCENTIVE_STRUCTURE")
public class AutoProcessIncentiveStructureMasterDummy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "STATE")
	private String state;

	@Column(name = "CATEGORY")
	private String category;

	@Column(name = "SLAB_FROM")
	private Integer slabFrom;

	@Column(name = "SLAB_TO")
	private Integer slabTo;

	@Column(name = "INCENTIVE_AMOUNT")
	private Double incentiveAmount;

	@Column(name = "MAXIMUM_INCENTIVE")
	private Double maximumIncentive;

	@Column(name = "MAXIMUM_SALARY_CAP")
	private Double maximumSalaryCap;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CYCLE_FROM_DATE")
	private String cycleFrom;

	@Column(name = "CYCLE_TO_DATE")
	private String cycleTo;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "SEQUENCE_NO")
	private Long sequence;

	@Column(name = "CHECKED_BY")
	private String checkedBy;

	@Column(name = "CHECKED_DATE")
	private LocalDate checkedDate;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name ="CAPPING")
	private Integer capping;

	public Long getId() {
		return id;
	}

	public Long getSequence() {
		return sequence;
	}

	public void setSequence(Long sequence) {
		this.sequence = sequence;
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

	public Double getIncentiveAmount() {
		return incentiveAmount;
	}

	public void setIncentiveAmount(Double incentiveAmount) {
		this.incentiveAmount = incentiveAmount;
	}

	public Double getMaximumIncentive() {
		return maximumIncentive;
	}

	public void setMaximumIncentive(Double maximumIncentive) {
		this.maximumIncentive = maximumIncentive;
	}

	public Double getMaximumSalaryCap() {
		return maximumSalaryCap;
	}

	public void setMaximumSalaryCap(Double maximumSalaryCap) {
		this.maximumSalaryCap = maximumSalaryCap;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCycleFrom() {
		return cycleFrom;
	}

	public void setCycleFrom(String cycleFrom) {
		this.cycleFrom = cycleFrom;
	}

	public String getCycleTo() {
		return cycleTo;
	}

	public void setCycleTo(String cycleTo) {
		this.cycleTo = cycleTo;
	}

	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getCapping() {
		return capping;
	}

	public void setCapping(Integer capping) {
		this.capping = capping;
	}

}
