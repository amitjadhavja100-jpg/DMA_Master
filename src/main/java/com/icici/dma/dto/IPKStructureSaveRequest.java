package com.icici.dma.dto;

import java.util.List;

public class IPKStructureSaveRequest {
	private String state;
	private String cycleFrom;
	private String cycleTo;
	private String counsellorType;
	private List<CategorySlabRequest> categories;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCycleFrom() {
		return cycleFrom;
	}

	public void setCycleFrom(String cycleFrom) {
		this.cycleFrom = cycleFrom;
	}

	public String getCycleTo() {
		return cycleTo;
	}

	public void setCycleTo(String cycleTo) {
		this.cycleTo = cycleTo;
	}

	public List<CategorySlabRequest> getCategories() {
		return categories;
	}

	public void setCategories(List<CategorySlabRequest> categories) {
		this.categories = categories;
	}

	public String getCounsellorType() {
		return counsellorType;
	}

	public void setCounsellorType(String counsellorType) {
		this.counsellorType = counsellorType;
	}

}
