package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsCategoryDTO;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;

public interface FlowsCategoryService {

	String save(FlowsCategoryDTO dto, String user);

	String update(FlowsCategoryDTO dto, String user);

	List<FlowsCategoryMaster> getAll();

	List<FlowsCategoryMaster> getApproved();

}