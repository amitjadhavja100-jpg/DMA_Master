package com.icici.dma.slabDto.ccrValuation;

import java.time.LocalDate;
import java.util.List;

public class ValuationPayoutRequest {

	private String product;

    private String subProduct;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String remarks;

    private List<ValuationPayoutDetailDTO> details;

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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<ValuationPayoutDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<ValuationPayoutDetailDTO> details) {
		this.details = details;
	}
    
}
