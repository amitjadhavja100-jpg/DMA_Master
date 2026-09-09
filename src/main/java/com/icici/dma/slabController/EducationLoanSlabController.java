package com.icici.dma.slabController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.EducationLoanCheckerActionRequest;
import com.icici.dma.dto.EducationLoanSlabSubmitRequest;
import com.icici.dma.serviceImpl.EducationLoanSlabService;
import com.icici.dma.slabEntity.EducationLoanSlabTemp;

@RestController
@RequestMapping("/api/v1/slabs")
public class EducationLoanSlabController {
 
    @Autowired
    private EducationLoanSlabService slabService;
 
 // Maker Submit API
    @PostMapping("/maker/submit")
    public ResponseEntity<?> submitSlabs(@RequestBody EducationLoanSlabSubmitRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            slabService.submitSlabs(request);
            response.put("success", true);
            response.put("message", "Submitted successfully for approval.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
 
    // Checker Fetch Pending API
    @GetMapping("/checker/pending")
    public ResponseEntity<List<EducationLoanSlabTemp>> getPendingApprovals() {
        return ResponseEntity.ok(slabService.getPendingApprovals());
    }
 
 // Checker Approve/Reject API
    @PostMapping("/checker/action")
    public ResponseEntity<?> processCheckerAction(@RequestBody EducationLoanCheckerActionRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            slabService.processCheckerAction(request);
            response.put("success", true);
            response.put("message", "Records processed successfully.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
 