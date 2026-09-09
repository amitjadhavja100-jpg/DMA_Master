package com.icici.dma.slabEntity.flows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "TS_FLOWS_SLAB_MST")
public class FlowsSlabMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flows_slab_seq")
    @SequenceGenerator(
            name = "flows_slab_seq",
            sequenceName = "TS_FLOWS_SLAB_MST_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PRODUCT")
    private String product;

    @Column(name = "SUB_PRODUCT")
    private String subProduct;

    @Column(name = "CATEGORY_ID")
    private Long categoryId;
    
    @Transient
    private String categoryName;
    
    @Column(name = "CITY_ID")
    private Long cityId;
    
    @Transient
    private String cityName;

    @Column(name = "BUCKET_ID")
    private Long bucketId;
    
    @Transient
    private String bucketName;

    @Column(name = "TABLE_TYPE")
    private String tableType;

    @Column(name = "FROM_DATE")
    private LocalDate fromDate;

    @Column(name = "TO_DATE")
    private LocalDate toDate;

    @Column(name = "VERSION_NO")
    private Integer versionNo;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "PARENT_ID")
    private Long parentId;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Column(name = "MODIFIED_DATE")
    private LocalDateTime modifiedDate;

    @Column(name = "APPROVED_BY")
    private String approvedBy;

    @Column(name = "APPROVED_DATE")
    private LocalDateTime approvedDate;

    @OneToMany(
            mappedBy = "master",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<FlowsPayoutDetail> details = new ArrayList<>();

    @OneToMany(
    	    mappedBy = "master",
    	    cascade = CascadeType.ALL,
    	    orphanRemoval = true ,
    	    fetch = FetchType.EAGER)
    @JsonManagedReference
	private List<FlowsPayoutFooterDetail> footers = new ArrayList<>();
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getSubProduct() {
		return subProduct;
	}

	public void setSubProduct(String subProduct) {
		this.subProduct = subProduct;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Long getCityId() {
	    return cityId;
	}

	public void setCityId(Long cityId) {
	    this.cityId = cityId;
	}
	
	public String getCityName() {
	    return cityName;
	}

	public void setCityName(String cityName) {
	    this.cityName = cityName;
	}

	public Long getBucketId() {
		return bucketId;
	}

	public void setBucketId(Long bucketId) {
		this.bucketId = bucketId;
	}

	public String getCategoryName() {
	    return categoryName;
	}

	public void setCategoryName(String categoryName) {
	    this.categoryName = categoryName;
	}

	public String getBucketName() {
	    return bucketName;
	}

	public void setBucketName(String bucketName) {
	    this.bucketName = bucketName;
	}
	
	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
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

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

	public List<FlowsPayoutDetail> getDetails() {
		return details;
	}

	public void setDetails(List<FlowsPayoutDetail> details) {
		this.details = details;
	}

	public List<FlowsPayoutFooterDetail> getFooters() {
		return footers;
	}

	public void setFooters(List<FlowsPayoutFooterDetail> footers) {
		this.footers = footers;
	}
    
}