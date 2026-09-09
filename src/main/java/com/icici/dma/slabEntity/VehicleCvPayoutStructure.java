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
@Table(name = "VEHICLE_CV_PAYOUT_STRUCTURE")
public class VehicleCvPayoutStructure {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	private Long id;

	@Column(name = "PRODUCT_TYPE")
	private String productType;

	@Column(name = "STRUCTURE_TYPE")
	private String structureType;

	@Column(name = "RR_FROMAPRIL22")
	private BigDecimal rrFromApril22;

	@Column(name = "RR_BEFOREAPRIL22")
	private BigDecimal rrBeforeApril22;

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

	public BigDecimal getRrFromApril22() {
		return rrFromApril22;
	}

	public void setRrFromApril22(BigDecimal rrFromApril22) {
		this.rrFromApril22 = rrFromApril22;
	}

	public BigDecimal getRrBeforeApril22() {
		return rrBeforeApril22;
	}

	public void setRrBeforeApril22(BigDecimal rrBeforeApril22) {
		this.rrBeforeApril22 = rrBeforeApril22;
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
