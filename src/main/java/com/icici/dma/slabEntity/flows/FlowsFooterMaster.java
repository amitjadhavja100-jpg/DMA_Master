package com.icici.dma.slabEntity.flows;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TS_FLOWS_FOOTER_MST")
public class FlowsFooterMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flows_footer_seq")
    @SequenceGenerator(
            name = "flows_footer_seq",
            sequenceName = "TS_FLOWS_FOOTER_MST_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "BUCKET_ID")
    private Long bucketId;

    @Column(name = "ORDER_ID")
    private Integer orderId;

    @Column(name = "FOOTER_TYPE")
    private String footerType;

    @Column(name = "FOOTER_TEXT")
    private String footerText;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "MODIFIED_DATE")
    private Date modifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBucketId() {
		return bucketId;
	}

	public void setBucketId(Long bucketId) {
		this.bucketId = bucketId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getFooterType() {
		return footerType;
	}

	public void setFooterType(String footerType) {
		this.footerType = footerType;
	}

	public String getFooterText() {
		return footerText;
	}

	public void setFooterText(String footerText) {
		this.footerText = footerText;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

}