package com.icici.dma.slabController.flows;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.flows.FlowsPayoutRequest;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;
import com.icici.dma.slabEntity.flows.FlowsFooterMaster;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;
import com.icici.dma.slabEntity.flows.FlowsSlabMaster;
import com.icici.dma.slabService.flows.FlowsFooterService;
import com.icici.dma.slabService.flows.FlowsPayoutService;

@RestController
@RequestMapping("/flowsPayout")
public class FlowsPayoutController {

    @Autowired
    private FlowsPayoutService flowsService;
    
    @Autowired
    private FlowsFooterService footerService;
    
    @GetMapping("/buckets")
    public List<FlowsBucketMaster> getBuckets(
            @RequestParam Long categoryId,
            @RequestParam(required = false) Long cityId) {

        return flowsService.getBuckets(categoryId, cityId);
    }
    
    @GetMapping("/cities")
	public List<FlowsCityMaster> getCities(@RequestParam Long categoryId) {

		return flowsService.getCities(categoryId);
	}

    @GetMapping("/categories")
    public List<FlowsCategoryMaster> getCategories() {

        return flowsService.getCategories();

    }

//    @GetMapping("/resolution")
//    public List<FlowsResolutionMaster> getResolution(
//    		@RequestParam Long cityId,
//            @RequestParam Long bucketId) {
//
//        return flowsService.getResolution(cityId, bucketId);
//
//    }
    
    @GetMapping("/resolution")
    public List<FlowsResolutionMaster> getResolution(
            @RequestParam Long bucketId) {

        return flowsService.getResolution(
                bucketId);
    }

//    @GetMapping("/performance")
//    public List<FlowsPerformanceMaster> getPerformance(
//    		@RequestParam Long cityId,
//            @RequestParam Long bucketId) {
//
//        return flowsService.getPerformance(cityId, bucketId);
//
//    }
    
    @GetMapping("/performance")
	public List<FlowsPerformanceMaster> getPerformance(@RequestParam Long bucketId) {
		return flowsService.getPerformance(bucketId);
    }
    
    @GetMapping("/footers")
    public List<FlowsFooterMaster> getFooters(
            @RequestParam Long bucketId,
            @RequestParam Integer orderId) {

        return footerService.getApprovedByBucket(
                bucketId,
                orderId);
    }

    @GetMapping("/fetch")
    public FlowsSlabMaster fetch(
            @RequestParam String product,
            @RequestParam String subProduct,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Long cityId,
            @RequestParam Long bucketId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return flowsService.fetch(
                product,
                subProduct,
                categoryId,
                cityId,
                bucketId,
                fromDate,
                toDate);
    }

    @PostMapping("/save")
    public String save(
            @RequestBody FlowsPayoutRequest request,
            @RequestParam String user) {

		return flowsService.save(request, user);
    }

    @GetMapping("/maker")
    public List<FlowsSlabMaster> maker() {
        return flowsService.getMakerList();

    }

    @GetMapping("/checker")
	public List<FlowsSlabMaster> checker(@RequestParam String user) {
        return flowsService.getCheckerList(user);
    }

    @GetMapping("/latestApprovedId")
    public Long getLatestApprovedId(
            @RequestParam String product,
            @RequestParam String subProduct,
            @RequestParam Long categoryId,
            @RequestParam(required = false) Long cityId,
            @RequestParam Long bucketId,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

        return flowsService.getLatestApprovedId(
                product,
                subProduct,
                categoryId,
                cityId,
                bucketId,
                fromDate,
                toDate);
    }


    @GetMapping("/compare/{id}")
    public Map<String,Object> compare(
            @PathVariable Long id,
            @RequestParam String mode) {

        return flowsService.compare(id, mode);
    }

    @PostMapping("/approve/{id}")
    public String approve(
            @PathVariable Long id,
            @RequestParam String user) {

        return flowsService.approve(id, user);
    }

    @PostMapping("/reject/{id}")
    public String reject(
            @PathVariable Long id,
            @RequestParam String user,
            @RequestParam String remarks) {

		return flowsService.reject(id, user, remarks);
    }

}