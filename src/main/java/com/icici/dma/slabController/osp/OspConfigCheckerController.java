package com.icici.dma.slabController.osp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.osp.OspConfigCompareResponse;
import com.icici.dma.dto.osp.OspConfigPendingResponse;
import com.icici.dma.slabService.osp.OspConfigCheckerService;

@RestController
@RequestMapping("/osp/config")
public class OspConfigCheckerController {

    @Autowired
    private OspConfigCheckerService service;

    @GetMapping("/pending")
    public List<OspConfigPendingResponse> pending(
            HttpServletRequest request){

    	System.out.println("USER ID = " + request.getHeader("userId"));
        return service.getPending(
                request.getHeader("userId"));

    }

	@PostMapping("/approve/{tempId}")
	public String approve(
			@PathVariable Long tempId,
			@RequestParam String type,
			@RequestParam(required = false) String remarks,
			HttpServletRequest request) {
		
		return service.approve(
				tempId,
				type,
				request.getHeader("userId"),
				remarks);
	}

    @PostMapping("/reject/{tempId}")
    public String reject(
            @PathVariable Long tempId,
            @RequestParam String type,
            @RequestParam(required=false)
            String remarks,
            HttpServletRequest request){

        return service.reject(
                tempId,
                type,
                request.getHeader("userId"),
                remarks);
    }
    
    @GetMapping("/makerCompare/{id}")
    public OspConfigCompareResponse makerCompare(
            @PathVariable Long id,
            @RequestParam String type){

        return service.makerCompare(id,type);

    }

    @GetMapping("/checkerCompare/{tempId}")
    public OspConfigCompareResponse checkerCompare(
            @PathVariable Long tempId,
            @RequestParam String type){

        return service.checkerCompare(tempId,type);

    }
}