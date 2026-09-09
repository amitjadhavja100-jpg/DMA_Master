package com.icici.dma.slabService.flows;

import java.util.List;

import com.icici.dma.dto.flows.FlowsBucketTypeDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketTypeMaster;

public interface FlowsBucketTypeService {

    String save(FlowsBucketTypeDTO dto, String user);

    String update(FlowsBucketTypeDTO dto, String user);

    List<FlowsBucketTypeMaster> getAll();

    List<FlowsBucketTypeMaster> getApproved();
}