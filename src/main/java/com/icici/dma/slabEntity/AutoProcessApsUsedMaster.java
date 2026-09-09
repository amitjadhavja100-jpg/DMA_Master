package com.icici.dma.slabEntity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AUTO_PROCESS_APS_USED_MASTER")
public class AutoProcessApsUsedMaster {
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

	@Column(name = "APS_CODE")
	private Long apsCode;

	@Column(name = "CHANNEL_NAME")
	private String channelName;

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

	@Column(name = "CYCLE_FROM_DATE")
	private String cycleFromDate;

	@Column(name = "CYCLE_TO_DATE")
	private String cycleToDate;

	@Column(name = "SEQUENCE")
	private Integer sequence;

	// getters and setters for all fields
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

	public Long getApsCode() {
		return apsCode;
	}

	public void setApsCode(Long apsCode) {
		this.apsCode = apsCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
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

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

}
