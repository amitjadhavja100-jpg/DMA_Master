package com.icici.dma.slabEntity.flows;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "TS_FLOWS_PAYOUT_DETAIL")
public class FlowsPayoutDetail {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "flows_detail_seq")
    @SequenceGenerator(
            name = "flows_detail_seq",
            sequenceName = "TS_FLOWS_PAYOUT_DETAIL_SEQ",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MASTER_ID")
    @JsonBackReference
    private FlowsSlabMaster master;

    @Column(name = "RESOLUTION_ID")
    private Long resolutionId;

    @Column(name = "PERFORMANCE_ID")
    private Long performanceId;

    @Column(name = "PAYOUT_PERCENT")
    private BigDecimal payoutPercent;

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

	public Long getResolutionId() {
		return resolutionId;
	}

	public void setResolutionId(Long resolutionId) {
		this.resolutionId = resolutionId;
	}

	public Long getPerformanceId() {
		return performanceId;
	}

	public void setPerformanceId(Long performanceId) {
		this.performanceId = performanceId;
	}

	public BigDecimal getPayoutPercent() {
		return payoutPercent;
	}

	public void setPayoutPercent(BigDecimal payoutPercent) {
		this.payoutPercent = payoutPercent;
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