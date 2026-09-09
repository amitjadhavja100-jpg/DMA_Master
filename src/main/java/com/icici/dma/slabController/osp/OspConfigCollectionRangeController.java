package com.icici.dma.slabController.osp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.osp.OspConfigCollectionRangeRequest;
import com.icici.dma.dto.osp.OspConfigCollectionRangeResponse;
import com.icici.dma.slabService.osp.OspConfigCollectionRangeService;

@RestController
@RequestMapping("/osp/config/collection-range")
public class OspConfigCollectionRangeController {

    @Autowired
    private OspConfigCollectionRangeService service;

    @PostMapping("/save")
    public String save(@RequestBody OspConfigCollectionRangeRequest request,
                       HttpServletRequest httpRequest) {

        String userId = httpRequest.getHeader("userId");

        return service.save(request, userId);

    }

    @PutMapping("/update")
    public String update(@RequestBody OspConfigCollectionRangeRequest request,
                         HttpServletRequest httpRequest) {

        String userId = httpRequest.getHeader("userId");

        return service.update(request, userId);

    }

    @GetMapping("/approved")
    public List<OspConfigCollectionRangeResponse> getApprovedRanges(
            @RequestParam Long dpdId) {

        return service.getApprovedRanges(dpdId);

    }

    @GetMapping("/pending")
    public List<OspConfigCollectionRangeResponse> getPendingRanges() {

        return service.getPendingRanges();

    }

    @GetMapping("/{id}")
    public OspConfigCollectionRangeResponse getById(
            @PathVariable Long id) {

        return service.getById(id);

    }

    @PostMapping("/approve/{tempId}")
    public String approve(@PathVariable Long tempId,
                          @RequestParam(required = false) String remarks,
                          HttpServletRequest request) {

        String userId = request.getHeader("userId");

        return service.approve(tempId, userId, remarks);

    }

    @PostMapping("/reject/{tempId}")
    public String reject(@PathVariable Long tempId,
                         @RequestParam(required = false) String remarks,
                         HttpServletRequest request) {

        String userId = request.getHeader("userId");

        return service.reject(tempId, userId, remarks);

    }

}