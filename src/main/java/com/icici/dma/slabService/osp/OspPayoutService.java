package com.icici.dma.slabService.osp;

import java.util.List;
import java.util.Map;

import com.icici.dma.dto.OspPayoutRequest;
import com.icici.dma.dto.osp.OspCompareResponse;
import com.icici.dma.dto.osp.OspPendingResponse;
import com.icici.dma.slabEntity.osp.OspPayoutMaster;


public interface OspPayoutService {

    OspPayoutMaster fetch(
            String product,
            String subProduct,
            Long dpdId,
            String fromDate,
            String toDate);

	OspPayoutMaster save(OspPayoutRequest request, String user);

	void approve(Long id, String user);

	void reject(Long id, String user, String remarks);

	List<OspPendingResponse> pending(String user);

	List<OspPayoutMaster> history();

	List<Map<String, Object>> compare(Long id);
	
	OspCompareResponse makerCompare(Long id);

	OspCompareResponse checkerCompare(Long id);
    
    Long latestApprovedId(
            String product,
            String subProduct,
            Long dpdId,
            String fromDate,
            String toDate);

}
