package com.icici.dma.dto.osp;

import java.time.LocalDate;

public class OspPendingResponse {

    private Long id;
    private String product;
    private String subProduct;
    private Long dpdId;
    private String dpdName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private String createdBy;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Long getDpdId() {
		return dpdId;
	}
	public void setDpdId(Long dpdId) {
		this.dpdId = dpdId;
	}
	public String getDpdName() {
		return dpdName;
	}
	public void setDpdName(String dpdName) {
		this.dpdName = dpdName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
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
    
    
    
}