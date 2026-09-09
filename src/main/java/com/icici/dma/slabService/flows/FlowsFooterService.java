package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsFooterDTO;
import com.icici.dma.slabEntity.flows.FlowsFooterMaster;

public interface FlowsFooterService {

    String save(
            FlowsFooterDTO dto,
            String user);

    String update(
            FlowsFooterDTO dto,
            String user);

    List<FlowsFooterMaster> getAll();

	List<FlowsFooterMaster> getApproved(Long bucketId);

	List<FlowsFooterMaster> getApprovedByBucket(Long bucketId, Integer orderId);
}