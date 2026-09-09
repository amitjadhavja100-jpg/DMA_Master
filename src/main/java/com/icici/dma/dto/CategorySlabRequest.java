package com.icici.dma.dto;

import java.util.List;

public class CategorySlabRequest {
	
	private String category;

	private String remarks;

	private List<SlabRowRequest> rows;
 
	public String getCategory() {

		return category;

	}
 
	public void setCategory(String category) {

		this.category = category;

	}
 
	public String getRemarks() {

		return remarks;

	}
 
	public void setRemarks(String remarks) {

		this.remarks = remarks;

	}
 
	public List<SlabRowRequest> getRows() {

		return rows;

	}
 
	public void setRows(List<SlabRowRequest> rows) {

		this.rows = rows;

	}
 

}

