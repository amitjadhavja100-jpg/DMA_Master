package com.icici.dma.slabEntity;

import java.util.List;


public class PayoutRequest {
    public String category;
    public String city;
    public String dpd;
    public String fromDate;
    public String toDate;
    public List<RowData> matrix;
    public String createdBy;
    private List<String> pendingRows;

    private List<String> pendingColumns;
    
    
    
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDpd() {
		return dpd;
	}
	public void setDpd(String dpd) {
		this.dpd = dpd;
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
	public List<RowData> getMatrix() {
		return matrix;
	}
	public void setMatrix(List<RowData> matrix) {
		this.matrix = matrix;
	}
	public List<String> getPendingRows() {
		return pendingRows;
	}
	public void setPendingRows(List<String> pendingRows) {
		this.pendingRows = pendingRows;
	}
	public List<String> getPendingColumns() {
		return pendingColumns;
	}
	public void setPendingColumns(List<String> pendingColumns) {
		this.pendingColumns = pendingColumns;
	}
    
    
}
