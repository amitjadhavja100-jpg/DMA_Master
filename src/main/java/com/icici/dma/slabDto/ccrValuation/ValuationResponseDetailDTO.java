package com.icici.dma.slabDto.ccrValuation;

import java.math.BigDecimal;

public class ValuationResponseDetailDTO {

	private Long valuationMasterId;

    private String iboxId;

    private String valuerName;

    private String productSubType;

    private BigDecimal rateUnderGst;

	public Long getValuationMasterId() {
		return valuationMasterId;
	}

	public void setValuationMasterId(Long valuationMasterId) {
		this.valuationMasterId = valuationMasterId;
	}

	public String getIboxId() {
		return iboxId;
	}

	public void setIboxId(String iboxId) {
		this.iboxId = iboxId;
	}

	public String getValuerName() {
		return valuerName;
	}

	public void setValuerName(String valuerName) {
		this.valuerName = valuerName;
	}

	public String getProductSubType() {
		return productSubType;
	}

	public void setProductSubType(String productSubType) {
		this.productSubType = productSubType;
	}

	public BigDecimal getRateUnderGst() {
		return rateUnderGst;
	}

	public void setRateUnderGst(BigDecimal rateUnderGst) {
		this.rateUnderGst = rateUnderGst;
	}
    
}
