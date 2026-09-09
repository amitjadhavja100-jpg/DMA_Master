package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.flows.FlowsCityDTO;
import com.icici.dma.slabEntity.flows.FlowsCityMaster;
import com.icici.dma.slabService.flows.FlowsCityService;

@RestController
@RequestMapping("/flowsCity")
public class FlowsCityController {

    @Autowired
    private FlowsCityService service;


	@GetMapping("/approved")
	public List<FlowsCityMaster> getApproved(@RequestParam Long categoryId) {

		return service.getApprovedByCategory(categoryId);
	}

    @GetMapping("/all")
    public List<FlowsCityMaster> getAll() {

        return service.getAll();
    }

    @PostMapping("/save")
    public String save(
            @RequestBody FlowsCityDTO dto,
            @RequestParam String user) {

        return service.save(dto, user);
    }

    @PutMapping("/update")
    public String update(
            @RequestBody FlowsCityDTO dto,
            @RequestParam String user) {

        return service.update(dto, user);
    }
}