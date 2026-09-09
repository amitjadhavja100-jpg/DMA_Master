package com.icici.dma.dto.flows;

import java.time.LocalDate;
import java.util.List;

public class FlowsPayoutRequest {

    private String product;

    private String subProduct;

    private Long categoryId;
    
    private Long cityId;

    private Long bucketId;

    private String tableType;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String remarks;

    private List<FlowsPayoutDetailDTO> payoutList;
    
    private List<FlowsPayoutFooterDetailDTO> footerList;

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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
	public Long getCityId() {
	    return cityId;
	}

	public void setCityId(Long cityId) {
	    this.cityId = cityId;
	}

	public Long getBucketId() {
		return bucketId;
	}

	public void setBucketId(Long bucketId) {
		this.bucketId = bucketId;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
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

	public List<FlowsPayoutDetailDTO> getPayoutList() {
		return payoutList;
	}

	public void setPayoutList(List<FlowsPayoutDetailDTO> payoutList) {
		this.payoutList = payoutList;
	}

	public List<FlowsPayoutFooterDetailDTO> getFooterList() {
		return footerList;
	}

	public void setFooterList(List<FlowsPayoutFooterDetailDTO> footerList) {
		this.footerList = footerList;
	}

}