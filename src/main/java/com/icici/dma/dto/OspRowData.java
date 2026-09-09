package com.icici.dma.dto;

import java.math.BigDecimal;

public class OspRowData {

    private BigDecimal fromAmount;

    private BigDecimal toAmount;

    private Double incentive;

    private String dpd;

	public BigDecimal getFromAmount() {
		return fromAmount;
	}

	public void setFromAmount(BigDecimal fromAmount) {
		this.fromAmount = fromAmount;
	}

	public BigDecimal getToAmount() {
		return toAmount;
	}

	public void setToAmount(BigDecimal toAmount) {
		this.toAmount = toAmount;
	}

	public Double getIncentive() {
		return incentive;
	}

	public void setIncentive(Double incentive) {
		this.incentive = incentive;
	}

	public String getDpd() {
		return dpd;
	}

	public void setDpd(String dpd) {
		this.dpd = dpd;
	}

}
