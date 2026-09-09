package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.flows.FlowsFooterDTO;
import com.icici.dma.slabEntity.flows.FlowsFooterMaster;
import com.icici.dma.slabService.flows.FlowsFooterService;

@RestController
@RequestMapping("/flowsFooter")
public class FlowsFooterController {

    @Autowired
    private FlowsFooterService service;
    
    @PostMapping("/save")
    public String save(
            @RequestBody FlowsFooterDTO dto,
            @RequestParam String user) {

        return service.save(dto, user);
    }
    
    @PostMapping("/update")
    public String update(
            @RequestBody FlowsFooterDTO dto,
            @RequestParam String user) {

        return service.update(dto, user);
    }
    
    @GetMapping("/all")
    public List<FlowsFooterMaster> getAll() {

        return service.getAll();
    }
    
    //bucket
    @GetMapping("/approved")
    public List<FlowsFooterMaster> getApproved(
            @RequestParam Long bucketId) {

        return service.getApproved(
                bucketId);
    }
    
    //footer for selected bucket
    @GetMapping("/bucket")
    public List<FlowsFooterMaster> getByBucket(
            @RequestParam Long bucketId,
            @RequestParam Integer orderId) {

        return service.getApprovedByBucket(
                bucketId,
                orderId);
    }
}
