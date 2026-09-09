package com.icici.dma.dto;

import java.util.List;

public class IncentiveStructureDto {
	private Long id;
	private Integer seqNo;
	private Integer srNo;
	private String type;
	private String state;
	private Integer fromSlab;
	private Integer toSlab;
	private Integer percentage;
	private Integer fixedAmount;
	private Integer maxIncentive;
	private Integer capping;
	private Integer maxSalaryCap;
	private String remark;
	private String status;
	private String createdBy;
//    private String checkedBy;
	private String cycleFromDate;
	private String cycleToDate;
	private List<IncentiveStructureDto> dtos;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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

	public List<IncentiveStructureDto> getDtos() {
		return dtos;
	}

	public void setDtos(List<IncentiveStructureDto> dtos) {
		this.dtos = dtos;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public Integer getCapping() {
		return capping;
	}

	public void setCapping(Integer capping) {
		this.capping = capping;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getMaxIncentive() {
		return maxIncentive;
	}

	public void setMaxIncentive(Integer maxIncentive) {
		this.maxIncentive = maxIncentive;
	}

	public Integer getMaxSalaryCap() {
		return maxSalaryCap;
	}

	public void setMaxSalaryCap(Integer maxSalaryCap) {
		this.maxSalaryCap = maxSalaryCap;
}
}