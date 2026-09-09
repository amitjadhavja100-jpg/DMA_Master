package com.icici.dma.dto;

import java.time.LocalDate;

public class MPKCommonPendingSummary {
	private Integer sequence;
	private String state;
	private String cycleFrom;
	private String cycleTo;
	private LocalDate createdDate;
	private String status;

	public MPKCommonPendingSummary(Integer sequence, String state, String cycleFrom, String cycleTo, LocalDate createdDate,
			String status) {
		this.sequence = sequence;
		this.state = state;
		this.cycleFrom = cycleFrom;
		this.cycleTo = cycleTo;
		this.createdDate = createdDate;
		this.status = status;
	}

	public Integer getSequence() {
		return sequence;
	}

	public String getState() {
		return state;
	}

	public String getCycleFrom() {
		return cycleFrom;
	}

	public String getCycleTo() {
		return cycleTo;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public String getStatus() {
		return status;
	}
}
