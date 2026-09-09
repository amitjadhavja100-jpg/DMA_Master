package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsPerformanceDTO;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;

public interface FlowsPerformanceService {

	String save(FlowsPerformanceDTO dto, String user);

	String update(FlowsPerformanceDTO dto, String user);

	List<FlowsPerformanceMaster> getAll();

	List<FlowsPerformanceMaster> getApproved(Long bucketId);
	
}