package com.icici.dma.slabEntity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "auto_manipal_common_special_cases")
public class AutoProcessBrokerMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "BROKER_ID")
	private String brokerId;

	@Column(name = "BROKER_NAME")
	private String brokerName;

	@Column(name = "NEW_CAR")
	private String newCar;

	@Column(name = "USED_CAR")
	private String usedCar;

	@Column(name = "ATTACHMENT_INCENTIVE")
	private String attachmentIncentive;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "CYCLE_FROM_DATE")
	private String cycleFromDate;

	@Column(name = "CYCLE_TO_DATE")
	private String cycleToDate;

	@Column(name = "STATUS")
	private String status;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CHECKED_BY")
	private String checkedBy;

	@Column(name = "CHECKED_DATE")
	private LocalDate checkedDate;

	@Column(name = "CAPPING")
	private Double capping;
	
	@Column(name = "SEQUENCE")
	private Integer sequence;
	
	@Column(name = "STATE")
	private String state;

	// getters and setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getNewCar() {
		return newCar;
	}

	public void setNewCar(String newCar) {
		this.newCar = newCar;
	}

	public String getUsedCar() {
		return usedCar;
	}

	public void setUsedCar(String usedCar) {
		this.usedCar = usedCar;
	}

	public String getAttachmentIncentive() {
		return attachmentIncentive;
	}

	public void setAttachmentIncentive(String attachmentIncentive) {
		this.attachmentIncentive = attachmentIncentive;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public LocalDate getCheckedDate() {
		return checkedDate;
	}

	public void setCheckedDate(LocalDate checkedDate) {
		this.checkedDate = checkedDate;
	}

	public Double getCapping() {
		return capping;
	}

	public void setCapping(Double capping) {
		this.capping = capping;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
