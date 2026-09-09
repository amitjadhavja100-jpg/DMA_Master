package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.flows.FlowsBucketDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabService.flows.FlowsBucketService;

@RestController
@RequestMapping("/flowsBucket")
public class FlowsBucketController {

    @Autowired
    private FlowsBucketService service;

    @PostMapping("/save")
    public String save(
            @RequestBody FlowsBucketDTO dto,
            @RequestParam String user) {

        return service.save(dto, user);

    }

    @PostMapping("/update")
    public String update(
            @RequestBody FlowsBucketDTO dto,
            @RequestParam String user) {

        return service.update(dto, user);

    }

    @GetMapping("/all")
    public List<FlowsBucketMaster> getAll() {
        return service.getAll();

    }

    @GetMapping("/approved")
    public List<FlowsBucketMaster> getApproved(
            @RequestParam Long cityId) {

        return service.getApproved(cityId);
    }

}