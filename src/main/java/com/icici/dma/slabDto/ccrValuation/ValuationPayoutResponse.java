package com.icici.dma.slabDto.ccrValuation;

import java.time.LocalDate;
import java.util.List;

public class ValuationPayoutResponse {

	private Long slabId;

    private String product;

    private String subProduct;

    private LocalDate fromDate;

    private LocalDate toDate;

    private Integer versionNo;

    private String status;

    private List<ValuationResponseDetailDTO> details;

	public Long getSlabId() {
		return slabId;
	}

	public void setSlabId(Long slabId) {
		this.slabId = slabId;
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

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ValuationResponseDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<ValuationResponseDetailDTO> details) {
		this.details = details;
	}
    
}
