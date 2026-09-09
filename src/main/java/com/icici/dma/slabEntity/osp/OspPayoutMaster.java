package com.icici.dma.slabEntity.osp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name="TS_OSP_RECV_PAYOUT_MST")
public class OspPayoutMaster {

    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator="osp_mst_seq")
    @SequenceGenerator( name="osp_mst_seq",
    					sequenceName="TS_OSP_RECV_PAYOUT_MST_SEQ",
    					allocationSize=1)
    private Long id;

    @Column(name="PARENT_MASTER_ID")
    private Long parentMasterId;

    @Column(name="PRODUCT")
    private String product;

    @Column(name="SUB_PRODUCT")
    private String subProduct;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DPD_ID")
    private OspConfigDpdMaster dpd;

    @Column(name="STATUS")
    private String status;

    @Column(name="FROM_DATE")
    private LocalDate fromDate;

    @Column(name="TO_DATE")
    private LocalDate toDate;

    @Column(name="VERSION_NO")
    private Integer version=1;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Column(name="APPROVED_BY")
    private String approvedBy;

//    @Column(name="CREATED_DATE")
//    private LocalDateTime createdDate=LocalDateTime.now();
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name="APPROVED_DATE")
    private LocalDateTime approvedDate;

    @Column(name="REMARKS")
    private String remarks;

    @OneToMany(
            mappedBy = "master",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    @OrderBy("orderNo ASC")
    private List<OspPayoutDetail> details = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentMasterId() {
		return parentMasterId;
	}

	public void setParentMasterId(Long parentMasterId) {
		this.parentMasterId = parentMasterId;
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

	public OspConfigDpdMaster getDpd() {
		return dpd;
	}

	public void setDpd(OspConfigDpdMaster dpd) {
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(LocalDateTime approvedDate) {
		this.approvedDate = approvedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<OspPayoutDetail> getDetails() {
		return details;
	}

	public void setDetails(List<OspPayoutDetail> details) {
		this.details = details;
	}
    
}