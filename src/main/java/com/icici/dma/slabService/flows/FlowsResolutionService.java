package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsResolutionDTO;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;

public interface FlowsResolutionService {

	String save(FlowsResolutionDTO dto, String user);

	String update(FlowsResolutionDTO dto, String user);

	List<FlowsResolutionMaster> getAll();

	List<FlowsResolutionMaster> getApproved(Long bucketId);

}