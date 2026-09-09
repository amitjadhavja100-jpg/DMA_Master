package com.icici.dma.dto;

import java.util.List;

public class VehiclePayoutStructureRequest {
	private String productType;
	private String structureType; // "tw st" or "cv st"
	private String cycleFrom; // yyyy-MM-dd from <input type="date">
	private String cycleTo;
	private List<VehiclePayoutDetailDTO> details;

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getStructureType() {
		return structureType;
	}

	public void setStructureType(String structureType) {
		this.structureType = structureType;
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

	public List<VehiclePayoutDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<VehiclePayoutDetailDTO> details) {
		this.details = details;
	}

}
