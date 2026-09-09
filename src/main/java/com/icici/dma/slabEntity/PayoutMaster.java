package com.icici.dma.slabEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="TS_CCR_SLAB_MST")
public class PayoutMaster {

	/*
	 * @Id @GeneratedValue private Long id;
	 */
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "slab_mst_seq")
	@SequenceGenerator(
	    name = "slab_mst_seq",
	    sequenceName = "TS_CCR_SLAB_MST_SEQ",
	    allocationSize = 1
	)
	private Long id;
	
    private Long parentMasterId;
    private String category;
    private String city;
    private String dpd; // "181-270" or "271-360"

    private String status; // PENDING / APPROVED / INACTIVE

    private LocalDate fromDate;
    private LocalDate toDate;

    @Column(name="VERSION_NO")
    private Integer version = 1;

    private LocalDateTime createdDate = LocalDateTime.now();

    
    @OneToMany(mappedBy="master", cascade=CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PayoutDetail> details = new ArrayList<>();
    
    private String createdBy;
    private String approvedBy;
    private String remarks;
    

    
	public Long getParentMasterId() {
		return parentMasterId;
	}

	public void setParentMasterId(Long parentMasterId) {
		this.parentMasterId = parentMasterId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDpd() {
		return dpd;
	}

	public void setDpd(String dpd) {
		this.dpd = dpd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<PayoutDetail> getDetails() {
		return details;
	}

	public void setDetails(List<PayoutDetail> details) {
		this.details = details;
	}

    
}

