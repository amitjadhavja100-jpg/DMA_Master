package com.icici.dma.dto;

import java.util.Date;
import java.util.List;

public class EducationLoanSlabSubmitRequest {

	private String category;
	private String makerId;
	private List<SlabItem> slabs;
	private Date cycleFromDate;
	private Date cycleToDate;

	// Getters and Setters
	public String getCategory() {
		return category;
	}

	public Date getCycleFromDate() {
		return cycleFromDate;
	}

	public void setCycleFromDate(Date cycleFromDate) {
		this.cycleFromDate = cycleFromDate;
	}

	public Date getCycleToDate() {
		return cycleToDate;
	}

	public void setCycleToDate(Date cycleToDate) {
		this.cycleToDate = cycleToDate;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMakerId() {
		return makerId;
	}

	public void setMakerId(String makerId) {
		this.makerId = makerId;
	}

	public List<SlabItem> getSlabs() {
		return slabs;
	}

	public void setSlabs(List<SlabItem> slabs) {
		this.slabs = slabs;
	}

	public static class SlabItem {
		private String bucket;
		private Double min;
		private Double max;
		private String description;
		private String slab; // e.g. "0.35%"

		// Getters and Setters
		public String getBucket() {
			return bucket;
		}

		public void setBucket(String bucket) {
			this.bucket = bucket;
		}

		public Double getMin() {
			return min;
		}

		public void setMin(Double min) {
			this.min = min;
		}

		public Double getMax() {
			return max;
		}

		public void setMax(Double max) {
			this.max = max;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getSlab() {
			return slab;
		}

		public void setSlab(String slab) {
			this.slab = slab;
		}
	}

}
