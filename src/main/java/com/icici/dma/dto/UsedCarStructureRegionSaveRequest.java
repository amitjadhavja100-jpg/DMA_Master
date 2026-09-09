package com.icici.dma.dto;

import java.util.List;

public class UsedCarStructureRegionSaveRequest {
	private String state;
	private String cycleFromDate;
	private String cycleToDate;
	private String counsellorType;
	private List<ApsRowRequest> apsPNRows;
	private List<ApsRowRequest> apsKLRows;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCycleFromDate() {
		return cycleFromDate;
	}
	public void setCycleFromDate(String cycleFromDate) {
		this.cycleFromDate = cycleFromDate;
	}
	public String getCycleToDate() {
		return cycleToDate;
	}
	public void setCycleToDate(String cycleToDate) {
		this.cycleToDate = cycleToDate;
	}
	public String getCounsellorType() {
		return counsellorType;
	}
	public void setCounsellorType(String counsellorType) {
		this.counsellorType = counsellorType;
	}
	public List<ApsRowRequest> getApsPNRows() {
		return apsPNRows;
	}
	public void setApsPNRows(List<ApsRowRequest> apsPNRows) {
		this.apsPNRows = apsPNRows;
	}
	public List<ApsRowRequest> getApsKLRows() {
		return apsKLRows;
	}
	public void setApsKLRows(List<ApsRowRequest> apsKLRows) {
		this.apsKLRows = apsKLRows;
	}

	
	
}
