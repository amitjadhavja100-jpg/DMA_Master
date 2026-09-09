package com.icici.dma.slabService.osp;


import java.util.List;

import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.dto.osp.OspConfigDpdRequest;
import com.icici.dma.dto.osp.OspConfigDpdResponse;

public interface OspConfigDpdService {

	List<OspConfigDpdResponse> getApprovedDpds();

	List<OspConfigDpdResponse> getPendingDpds();
	
	String save(OspConfigDpdRequest request, String userId);

    String update(OspConfigDpdRequest request, String userId);

//	String save(OspConfigDpdRequest request);
//
//	String update(OspConfigDpdRequest request);

	String approve(Long tempId, String approvedBy, String remarks);

	String reject(Long tempId, String approvedBy, String remarks);

	OspConfigDpdResponse getById(Long id);
	
	OspConfigCompareResponse getMakerCompare(Long id);

	OspConfigCompareResponse getCheckerCompare(Long tempId);

}