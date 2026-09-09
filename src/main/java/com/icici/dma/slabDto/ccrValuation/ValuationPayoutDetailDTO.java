package com.icici.dma.slabDto.ccrValuation;

import java.math.BigDecimal;

public class ValuationPayoutDetailDTO {

	private Long valuationMasterId;

    private BigDecimal rateUnderGst;

	public Long getValuationMasterId() {
		return valuationMasterId;
	}

	public void setValuationMasterId(Long valuationMasterId) {
		this.valuationMasterId = valuationMasterId;
	}

	public BigDecimal getRateUnderGst() {
		return rateUnderGst;
	}

	public void setRateUnderGst(BigDecimal rateUnderGst) {
		this.rateUnderGst = rateUnderGst;
	}
    
    
}
