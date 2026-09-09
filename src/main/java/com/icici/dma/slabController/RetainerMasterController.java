package com.icici.dma.slabController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.RetainerPayloadDto;
import com.icici.dma.model.BaseOutputVO;
import com.icici.dma.repository.AutoManipalRetainerMasterRepository;
import com.icici.dma.service.RetainerMasterService;
import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
 
@RestController
@RequestMapping("/api/retainer-master")
@CrossOrigin(origins = "*")
public class RetainerMasterController {
 
    @Autowired
    private RetainerMasterService retainerMasterService;
 
    @Autowired
    private AutoManipalRetainerMasterRepository repository;
 
    // --- 1. SAVE SLAB DATA ---
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveRetainerData(@RequestBody RetainerPayloadDto payload) {
        List<AutoManipalRetainerMaster> savedRecords = retainerMasterService.saveRetainerMaster(payload);
 
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Retainer Master records created successfully!");
        response.put("totalRecordsSaved", savedRecords.size());
 
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
 
    // --- 2. FETCH PENDING RECORDS ---
    @GetMapping("/fetchingPending")
    @ResponseBody
    public List<AutoManipalRetainerMaster> getPendingRecords() {
        return repository.findPendingSummary();
    }
 
    // --- 3. UPDATE SINGLE SLAB STATUS ---
    @PostMapping("/update-status")
    public ResponseEntity<?> updateSlabStatus(@RequestBody Map<String, Object> payload) {
        if (payload.get("id") != null && payload.get("status") != null) {
            Long id = Long.valueOf(payload.get("id").toString());
            String status = payload.get("status").toString();
 
            Optional<AutoManipalRetainerMaster> recordOpt = repository.findById(id);
            if (recordOpt.isPresent()) {
                AutoManipalRetainerMaster record = recordOpt.get();
                record.setStatus(status);
                repository.save(record);
 
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Status updated successfully");
                response.put("status", status);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.badRequest().body("Failed to update status");
    }
 
    // --- 4. UPDATE CHECKER STATUS VIA BASEOUTPUTVO ---
    @PostMapping(value = "/updateCheckerStatus", produces = "application/json")
    @ResponseBody
    public BaseOutputVO updateCheckerStatus(@RequestBody Map<String, String> payload) {
        BaseOutputVO output = new BaseOutputVO();
        try {
            String recordIdStr = payload.get("id");
            String targetStatus = payload.get("status");
 
            if (recordIdStr != null && targetStatus != null) {
                Long recordId = Long.parseLong(recordIdStr);
                Optional<AutoManipalRetainerMaster> recordOpt = repository.findById(recordId);
 
                if (recordOpt.isPresent()) {
                    AutoManipalRetainerMaster record = recordOpt.get();
                    record.setStatus(targetStatus);
                    repository.save(record);
 
                    output.setStatus("SUCCESS");
                    output.setMessage("Status saved successfully.");
                } else {
                    output.setStatus("ERROR");
                    output.setMessage("No record found matching primary ID: " + recordId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setStatus("ERROR");
            output.setMessage("Server Exception: " + e.getMessage());
        }
        return output;
    }
 
    // --- 5. APPROVE BATCH BY SEQUENCE ---
    @PostMapping("/approve/{sequence}")
    public ResponseEntity<?> approve(@PathVariable Long sequence) {
        List<AutoManipalRetainerMaster> list = repository.findBySequence(sequence);
        for (AutoManipalRetainerMaster obj : list) {
            obj.setStatus("Y");
            repository.save(obj);
        }
        return ResponseEntity.ok("Approved Successfully");
    }
 
    // --- 6. REJECT BATCH BY SEQUENCE ---
    @PostMapping("/reject/{sequence}")
    public ResponseEntity<?> reject(@PathVariable Long sequence) {
        List<AutoManipalRetainerMaster> list = repository.findBySequence(sequence);
        for (AutoManipalRetainerMaster obj : list) {
            obj.setStatus("R");
            repository.save(obj);
        }
        return ResponseEntity.ok("Rejected Successfully");
    }
}
 