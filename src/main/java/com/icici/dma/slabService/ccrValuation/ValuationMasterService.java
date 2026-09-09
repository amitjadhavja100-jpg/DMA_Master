package com.icici.dma.slabService.ccrValuation;

import java.util.List;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;

public interface ValuationMasterService {

	ValuationMasterDTO create(
            ValuationMasterDTO request,
            String userId
    );

    List<ValuationMasterDTO> getAll();

    List<ValuationMasterDTO> getActive();

    ValuationMasterDTO getById(Long id);

    ValuationMasterDTO update(
            Long id,
            ValuationMasterDTO request,
            String userId
    );
}