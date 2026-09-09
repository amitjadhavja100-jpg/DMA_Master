package com.icici.dma.slabService.osp;

import java.util.List;

import com.icici.dma.dto.osp.OspConfigCollectionRangeRequest;
import com.icici.dma.dto.osp.OspConfigCollectionRangeResponse;
import com.icici.dma.dto.osp.OspConfigCompareResponse;


public interface OspConfigCollectionRangeService {
	
    String save(OspConfigCollectionRangeRequest request, String userId);

    String update(OspConfigCollectionRangeRequest request, String userId);

	List<OspConfigCollectionRangeResponse> getApprovedRanges(Long dpdId);

	List<OspConfigCollectionRangeResponse> getPendingRanges();

	String approve(Long tempId, String approvedBy, String remarks);

	String reject(Long tempId, String approvedBy, String remarks);

	OspConfigCollectionRangeResponse getById(Long id);
	
	OspConfigCompareResponse getMakerCompare(Long id);

	OspConfigCompareResponse getCheckerCompare(Long tempId);

}