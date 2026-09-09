package com.icici.dma.dto.flows;

import java.math.BigDecimal;

public class FlowsPayoutDetailDTO {

    private Long resolutionId;

    private Long performanceId;

    private BigDecimal payoutPercent;

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

}