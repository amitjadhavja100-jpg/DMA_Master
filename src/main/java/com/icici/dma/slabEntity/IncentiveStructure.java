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
//@Table(name = "AUTO_IPROCESS_PANINDIA_INCENTIVE_STRUCTURE_DUMMY")
@Table(name = "AUTO_IPROCESS_PANINDIA_INCENTIVE_STRUCTURE")
public class IncentiveStructure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "SEQUENCE_NO")
	private Integer seqNo;
	@Column(name = "SR_NO")
	private Integer srNo;

	@Column(name = "CATEGORY")
	private String type;
	@Column(name = "STATE")
	private String state;
	@Column(name = "SLAB_FROM")
	private Integer fromSlab;
	@Column(name = "SLAB_TO")
	private Integer toSlab;
	@Column(name = "PERCENTAGE")
	private Integer percentage;
	@Column(name = "FIXED_AMOUNT")
	private Integer fixedAmount;
	@Column(name = "MAX_INCENTIVE")
	private Integer maximumIncentive;
	@Column(name = "CAPPING")
	private Integer capping;
	@Column(name = "MAXIMUM_SALARY_CAP")
	private Integer maximumSalaryCap;
	@Column(name = "REMARKS")
	private String remark;

	@Column(name = "CREATED_BY")
	private String createdBy;
	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;
	@Column(name = "CHECKED_BY")
	private String checkedBy;
	@Column(name = "CHECKED_DATE")
	private Date checkedDate;
	@Column(name = "CYCLE_FROM_DATE")
	private String cycleFromDate;
	@Column(name = "CYCLE_TO_DATE")
	private String cycleToDate;

	@Column(name = "STATUS")
	private String status;

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

	// Default Constructor
	public IncentiveStructure() {
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public String getCheckedBy() {
		return checkedBy;
	}

	public void setCheckedBy(String checkedBy) {
		this.checkedBy = checkedBy;
	}

	public Date getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSrNo() {
		return srNo;
	}

	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getFromSlab() {
		return fromSlab;
	}

	public void setFromSlab(Integer fromSlab) {
		this.fromSlab = fromSlab;
	}

	public Integer getToSlab() {
		return toSlab;
	}

	public void setToSlab(Integer toSlab) {
		this.toSlab = toSlab;
	}

	public Integer getPercentage() {
		return percentage;
	}

	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	public Integer getFixedAmount() {
		return fixedAmount;
	}

	public void setFixedAmount(Integer fixedAmount) {
		this.fixedAmount = fixedAmount;
	}

	public Integer getMaximumIncentive() {
		return maximumIncentive;
	}

	public void setMaximumIncentive(Integer maximumIncentive) {
		this.maximumIncentive = maximumIncentive;
	}

	public Integer getCapping() {
		return capping;
	}

	public void setCapping(Integer capping) {
		this.capping = capping;
	}

	public Integer getMaximumSalaryCap() {
		return maximumSalaryCap;
	}

	public void setMaximumSalaryCap(Integer maximumSalaryCap) {
		this.maximumSalaryCap = maximumSalaryCap;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	// Getters, Setters and full constructor mapped systematically from DTO
	// properties...

	// (Omitted other getters/setters here for brevity to maintain density)
}