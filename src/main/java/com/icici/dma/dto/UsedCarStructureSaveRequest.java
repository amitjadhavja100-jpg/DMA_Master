package com.icici.dma.dto;

import java.util.List;

public class UsedCarStructureSaveRequest {
	private String state;
	private String cycleFromDate;
	private String cycleToDate;
	private String counsellorType;
	private List<ApsRowRequest> apsRows;
	private List<SourcingRowRequest> sourcingRows;
	private List<BrokerRowRequest> brokerRows;


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

	public List<ApsRowRequest> getApsRows() {
		return apsRows;
	}

	public void setApsRows(List<ApsRowRequest> apsRows) {
		this.apsRows = apsRows;
	}

	public List<BrokerRowRequest> getBrokerRows() {
		return brokerRows;
	}

	public void setBrokerRows(List<BrokerRowRequest> brokerRows) {
		this.brokerRows = brokerRows;
	}

	public List<SourcingRowRequest> getSourcingRows() {
		return sourcingRows;
	}

	public void setSourcingRows(List<SourcingRowRequest> sourcingRows) {
		this.sourcingRows = sourcingRows;
	}

	public String getCounsellorType() {
		return counsellorType;
	}

	public void setCounsellorType(String counsellorType) {
		this.counsellorType = counsellorType;
	}
	
	
}
