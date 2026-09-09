package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.flows.FlowsPerformanceDTO;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;
import com.icici.dma.slabService.flows.FlowsPerformanceService;

@RestController
@RequestMapping("/flowsPerformance")
public class FlowsPerformanceController {


    @Autowired
    private FlowsPerformanceService service;

	@PostMapping("/save")
	public String save(@RequestBody FlowsPerformanceDTO dto, @RequestParam String user) {
		return service.save(dto, user);
	}

	@PostMapping("/update")
	public String update(@RequestBody FlowsPerformanceDTO dto, @RequestParam String user) {
		return service.update(dto, user);
	}

	@GetMapping("/approved")
	public List<FlowsPerformanceMaster> approved(
	        @RequestParam Long bucketId) {

	    return service.getApproved(bucketId);
	}

	@GetMapping("/all")
	public List<FlowsPerformanceMaster> all() {
		return service.getAll();
	}
}