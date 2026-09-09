package com.icici.dma.slabController.osp;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.dto.osp.OspConfigDpdRequest;
import com.icici.dma.dto.osp.OspConfigDpdResponse;
import com.icici.dma.slabService.osp.OspConfigDpdService;

@RestController
@RequestMapping("/osp/config/dpd")
public class OspConfigDpdController {

    @Autowired
    private OspConfigDpdService service;

    @PostMapping("/save")
    public String save(@RequestBody OspConfigDpdRequest request,
                       HttpServletRequest httpRequest) {

        String userId = httpRequest.getHeader("userId");

        return service.save(request, userId);
    }

    @PutMapping("/update")
    public String update(@RequestBody OspConfigDpdRequest request,
                         HttpServletRequest httpRequest) {

        String userId = httpRequest.getHeader("userId");

        return service.update(request, userId);
    }

    @GetMapping("/approved")
    public List<OspConfigDpdResponse> getApprovedDpds() {

        return service.getApprovedDpds();
    }

    @GetMapping("/pending")
    public List<OspConfigDpdResponse> getPendingDpds() {

        return service.getPendingDpds();
    }

    @GetMapping("/{id}")
    public OspConfigDpdResponse getById(@PathVariable Long id) {

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