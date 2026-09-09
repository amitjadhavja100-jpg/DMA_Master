package com.icici.dma.slabService.flows;

import java.util.List;
import java.util.Map;

import com.icici.dma.dto.flows.FlowsPayoutRequest;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;
import com.icici.dma.slabEntity.flows.FlowsSlabMaster;

public interface FlowsPayoutService {

	List<FlowsBucketMaster> getBuckets(Long categoryId, Long cityId);//snz25/8

    List<FlowsCategoryMaster> getCategories();
    
    List<FlowsCityMaster> getCities(Long categoryId);

    List<FlowsResolutionMaster> getResolution(Long bucketId);

    List<FlowsPerformanceMaster> getPerformance(Long bucketId);

    String save(FlowsPayoutRequest request, String userId);

    FlowsSlabMaster fetch(String product,
                              String subProduct,
                              Long categoryId,
                              Long cityId,
                              Long bucketId,
                              String fromDate,
                              String toDate);

    List<FlowsSlabMaster> getMakerList();
    
    List<FlowsSlabMaster> getCheckerList(String user);

    String approve(Long id,String checkerId);

    String reject(Long id,String checkerId,String remarks);

    Map<String,Object> compare(Long id, String mode);

    Long getLatestApprovedId(
            String product,
            String subProduct,
            Long categoryId,
            Long cityId,
            Long bucketId,
            String fromDate,
            String toDate);
}