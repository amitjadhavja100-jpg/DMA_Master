package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsBucketDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;

public interface FlowsBucketService {

	String save(FlowsBucketDTO dto, String user);

	String update(FlowsBucketDTO dto, String user);

    List<FlowsBucketMaster> getAll();

    List<FlowsBucketMaster> getApproved(Long cityId);

}