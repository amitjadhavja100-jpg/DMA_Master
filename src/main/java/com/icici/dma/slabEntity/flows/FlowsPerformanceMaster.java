package com.icici.dma.slabEntity.flows;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

@Entity
@Table(name="TS_FLOWS_PERFORMANCE_MST")
public class FlowsPerformanceMaster {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flows_performance_seq")
    @SequenceGenerator(
            name = "flows_performance_seq",
            sequenceName = "TS_FLOWS_PERFORMANCE_MST_SEQ",
            allocationSize = 1)
    @Column(name="ID")
    private Long id;
   
    @Column(name="BUCKET_ID")
    private Long bucketId;

    @Column(name="ORDER_ID")
    private Integer orderId;

    @Column(name="FROM_VALUE")
    private BigDecimal fromValue;

    @Column(name="TO_VALUE")
    private BigDecimal toValue;

    @Column(name="DISPLAY_TEXT")
    private String displayText;

    @Column(name="STATUS")
    private String status;

    @Column(name="CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_DATE")
    private Date createdDate;

    @Column(name="MODIFIED_BY")
    private String modifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MODIFIED_DATE")
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

	public BigDecimal getFromValue() {
		return fromValue;
	}

	public void setFromValue(BigDecimal fromValue) {
		this.fromValue = fromValue;
	}

	public BigDecimal getToValue() {
		return toValue;
	}

	public void setToValue(BigDecimal toValue) {
		this.toValue = toValue;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
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