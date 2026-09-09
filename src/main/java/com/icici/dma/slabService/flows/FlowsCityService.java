package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsCityDTO;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;

public interface FlowsCityService {

    String save(FlowsCityDTO dto, String user);

    String update(FlowsCityDTO dto, String user);

    List<FlowsCityMaster> getAll();

    List<FlowsCityMaster> getApprovedByCategory(Long categoryId);
}