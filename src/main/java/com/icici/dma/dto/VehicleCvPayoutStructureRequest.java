package com.icici.dma.dto;

import java.util.List;

import com.icici.dma.dto.VehicleCvDetailDTO;

public class VehicleCvPayoutStructureRequest {
	private String productType;
	private String structureType;
	private String cycleFrom;
	private String cycleTo;
	private List<VehicleCvDetailDTO> details;

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

	public List<VehicleCvDetailDTO> getDetails() {
		return details;
	}

	public void setDetails(List<VehicleCvDetailDTO> details) {
		this.details = details;
	}

}
