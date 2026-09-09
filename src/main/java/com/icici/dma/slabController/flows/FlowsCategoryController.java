package com.icici.dma.slabController.flows;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.flows.FlowsCategoryDTO;
import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;
import com.icici.dma.slabService.flows.FlowsCategoryService;

@RestController
@RequestMapping("/flowsCategory")
public class FlowsCategoryController {

    @Autowired
    private FlowsCategoryService service;

    @PostMapping("/save")
    public String save(
            @RequestBody FlowsCategoryDTO dto,
            @RequestParam String user){

        return service.save(dto,user);

    }

    @PostMapping("/update")
    public String update(
            @RequestBody FlowsCategoryDTO dto,
            @RequestParam String user){

        return service.update(dto,user);

    }

    @GetMapping("/all")
    public List<FlowsCategoryMaster> all(){

        return service.getAll();

    }

    @GetMapping("/approved")
    public List<FlowsCategoryMaster> approved(){

        return service.getApproved();

    }

}