package com.icici.dma.slabEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VEHICLE_TW_PAYOUT_STRUCTURE")
public class VehicleTwPayoutStructure {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "CHANNEL_PARTNERNAME")
	private String channelPartnerName;

	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "STRUCTURE_TYPE")
	private String structureType;

	@Column(name = "RR_TILLDEC22")
	private BigDecimal rrTillDec22;

	@Column(name = "RR_FROMJAN23")
	private BigDecimal rrFromJan23;

	@Column(name = "RR_FROMALLAUG23")
	private BigDecimal rrFromAllAug23;

	@Column(name = "FROM_DATE")
	private LocalDate fromDate;

	@Column(name = "TO_DATE")
	private LocalDate toDate;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;

	@Column(name = "CHECKED_BY")
	private String checked_by;

	@Column(name = "CHECKED_DATE")
	private LocalDate checked_date;

	@Column(name = "STATUS")
	private String status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChannelPartnerName() {
		return channelPartnerName;
	}

	public void setChannelPartnerName(String channelPartnerName) {
		this.channelPartnerName = channelPartnerName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
	}

	public BigDecimal getRrTillDec22() {
		return rrTillDec22;
	}

	public void setRrTillDec22(BigDecimal rrTillDec22) {
		this.rrTillDec22 = rrTillDec22;
	}

	public BigDecimal getRrFromJan23() {
		return rrFromJan23;
	}

	public void setRrFromJan23(BigDecimal rrFromJan23) {
		this.rrFromJan23 = rrFromJan23;
	}

	public BigDecimal getRrFromAllAug23() {
		return rrFromAllAug23;
	}

	public void setRrFromAllAug23(BigDecimal rrFromAllAug23) {
		this.rrFromAllAug23 = rrFromAllAug23;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
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

	public String getChecked_by() {
		return checked_by;
	}

	public void setChecked_by(String checked_by) {
		this.checked_by = checked_by;
	}

	public LocalDate getChecked_date() {
		return checked_date;
	}

	public void setChecked_date(LocalDate checked_date) {
		this.checked_date = checked_date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
