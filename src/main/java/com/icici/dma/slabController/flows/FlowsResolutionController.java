package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.flows.FlowsResolutionDTO;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;
import com.icici.dma.slabService.flows.FlowsResolutionService;

@RestController
@RequestMapping("/flowsResolution")
public class FlowsResolutionController {

    @Autowired
    private FlowsResolutionService service;
    
    
	@PostMapping("/save")
	public String save(@RequestBody FlowsResolutionDTO dto, @RequestParam String user) {
		return service.save(dto, user);
	}

	@PostMapping("/update")
	public String update(@RequestBody FlowsResolutionDTO dto, @RequestParam String user) {
		return service.update(dto, user);
	}

	@GetMapping("/approved")
	public List<FlowsResolutionMaster> approved(@RequestParam Long bucketId) {
		return service.getApproved(bucketId);
	}

	@GetMapping("/all")
	public List<FlowsResolutionMaster> all() {
		return service.getAll();
    }
}
