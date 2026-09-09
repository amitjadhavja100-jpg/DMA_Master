package com.icici.dma.dto;

public class SourcingRowRequest {
	private String category;
	private Integer slabFrom;
	private Integer slabTo;
	private Double percentage;
	private String remarks;
	private String designation;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSlabFrom() {
		return slabFrom;
	}

	public void setSlabFrom(Integer slabFrom) {
		this.slabFrom = slabFrom;
	}

	public Integer getSlabTo() {
		return slabTo;
	}

	public void setSlabTo(Integer slabTo) {
		this.slabTo = slabTo;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

}
