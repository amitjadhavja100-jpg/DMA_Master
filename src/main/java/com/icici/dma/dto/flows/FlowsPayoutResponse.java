package com.icici.dma.dto.flows;

import java.util.List;

public class FlowsPayoutResponse {

    private List<FlowsResolutionDTO> resolutions;

    private List<FlowsPerformanceDTO> performances;

    private List<FlowsPayoutDetailDTO> payoutList;
    
    private List<FlowsPayoutFooterDetailDTO> footers;

	public List<FlowsResolutionDTO> getResolutions() {
		return resolutions;
	}

	public void setResolutions(List<FlowsResolutionDTO> resolutions) {
		this.resolutions = resolutions;
	}

	public List<FlowsPerformanceDTO> getPerformances() {
		return performances;
	}

	public void setPerformances(List<FlowsPerformanceDTO> performances) {
		this.performances = performances;
	}

	public List<FlowsPayoutDetailDTO> getPayoutList() {
		return payoutList;
	}

	public void setPayoutList(List<FlowsPayoutDetailDTO> payoutList) {
		this.payoutList = payoutList;
	}

	public List<FlowsPayoutFooterDetailDTO> getFooters() {
		return footers;
	}

	public void setFooters(List<FlowsPayoutFooterDetailDTO> footers) {
		this.footers = footers;
	}
}