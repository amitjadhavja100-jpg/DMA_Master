package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.flows.FlowsBucketTypeDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketTypeMaster;
import com.icici.dma.slabService.flows.FlowsBucketTypeService;

@RestController
@RequestMapping("/flowsBucketType")
public class FlowsBucketTypeController {

    @Autowired
    private FlowsBucketTypeService service;


    @PostMapping("/save")
    public String save(
            @RequestBody FlowsBucketTypeDTO dto,
            @RequestParam String user) {

        return service.save(dto, user);
    }


    @PutMapping("/update")
    public String update(
            @RequestBody FlowsBucketTypeDTO dto,
            @RequestParam String user) {

        return service.update(dto, user);
    }


    @GetMapping("/all")
    public List<FlowsBucketTypeMaster> getAll() {

        return service.getAll();
    }


    @GetMapping("/approved")
    public List<FlowsBucketTypeMaster> getApproved() {

        return service.getApproved();
    }
}