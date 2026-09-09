package com.icici.dma.dto;

import java.util.List;

public class OspPayoutRequest {

    private String product;

    private String subProduct;
    
    private Long dpdId;

    private String fromDate;

    private String toDate;

    private List<OspRowData> rows;

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

	public Long getDpdId() {
		return dpdId;
	}

	public void setDpdId(Long dpdId) {
		this.dpdId = dpdId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public List<OspRowData> getRows() {
		return rows;
	}

	public void setRows(List<OspRowData> rows) {
		this.rows = rows;
	}

    
}