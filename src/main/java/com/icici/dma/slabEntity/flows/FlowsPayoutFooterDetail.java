package com.icici.dma.slabEntity.flows;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "TS_FLOWS_PAYOUT_FOOTER_DETAIL")
public class FlowsPayoutFooterDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "flows_payout_footer_seq")
    @SequenceGenerator(
        name = "flows_payout_footer_seq",
        sequenceName = "SEQ_TS_FLOWS_PAYOUT_FOOTER",
        allocationSize = 1
    )
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID", nullable = false)
    @JsonBackReference
    private FlowsSlabMaster master;

    @Column(name = "SOURCE_FOOTER_ID")
    private Long sourceFooterId;

    @Column(name = "BUCKET_ORDER_ID")
    private Integer bucketOrderId;

    @Column(name = "FOOTER_TYPE")
    private String footerType;

    @Column(name = "FOOTER_TEXT")
    private String footerText;

    @Column(name = "PERFORMANCE_ID")
    private Long performanceId;

    @Column(name = "FOOTER_VALUE")
    private Double footerValue;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "CREATED_DATE")
    private LocalDateTime createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FlowsSlabMaster getMaster() {
		return master;
	}

	public void setMaster(FlowsSlabMaster master) {
		this.master = master;
	}

	public Long getSourceFooterId() {
		return sourceFooterId;
	}

	public void setSourceFooterId(Long sourceFooterId) {
		this.sourceFooterId = sourceFooterId;
	}

	public Integer getBucketOrderId() {
		return bucketOrderId;
	}

	public void setBucketOrderId(Integer bucketOrderId) {
		this.bucketOrderId = bucketOrderId;
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

	public Long getPerformanceId() {
		return performanceId;
	}

	public void setPerformanceId(Long performanceId) {
		this.performanceId = performanceId;
	}

	public Double getFooterValue() {
		return footerValue;
	}

	public void setFooterValue(Double footerValue) {
		this.footerValue = footerValue;
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
    
}