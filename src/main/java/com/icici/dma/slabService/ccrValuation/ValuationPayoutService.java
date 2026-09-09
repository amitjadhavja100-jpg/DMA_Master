package com.icici.dma.slabService.ccrValuation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutRequest;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutResponse;
import com.icici.dma.slabEntity.ccrValuation.ValuationSlabMaster;

public interface ValuationPayoutService {

	List<ValuationMasterDTO> getValuations();

    ValuationPayoutResponse fetch(
            String product,
            String subProduct,
            LocalDate fromDate,
            LocalDate toDate);

	String save(ValuationPayoutRequest request, String userId);

    List<ValuationSlabMaster> getMakerList();

	List<ValuationSlabMaster> getCheckerList(String userId);

	String approve(Long id, String checkerId);

	String reject(Long id, String checkerId, String remarks);

	Map<String, Object> compare(Long id, String mode);
    
    Long latestApprovedId(
            String product,
            String subProduct,
            LocalDate fromDate,
            LocalDate toDate
    );
}
