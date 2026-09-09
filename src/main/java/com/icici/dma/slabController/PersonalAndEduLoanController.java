package com.icici.dma.slabController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.service.PersonalAndEduLoanService;
import com.icici.dma.slabEntity.PersonalLoanMST;

@RestController
@RequestMapping("/api/slabs")
public class PersonalAndEduLoanController {

	private final PersonalAndEduLoanService personalAndEduLoanService;

	@Autowired
	public PersonalAndEduLoanController(PersonalAndEduLoanService personalAndEduLoanService) {
		this.personalAndEduLoanService = personalAndEduLoanService;
	}

	/*
	 * @PostMapping("/save") public ResponseEntity<Map<String, Object>>
	 * saveSlabs(@RequestBody List<PersonalLoanMST> slabList) { Map<String, Object>
	 * response = new HashMap<>(); try { List<PersonalLoanMST> savedData =
	 * personalAndEduLoanService.saveSlabs(slabList);
	 * 
	 * response.put("status", "SUCCESS"); response.put("message",
	 * "Slab data saved successfully."); response.put("data", savedData); return
	 * ResponseEntity.ok(response); } catch (Exception e) { response.put("status",
	 * "ERROR"); response.put("message", "Failed to save: " + e.getMessage());
	 * return ResponseEntity.status(500).body(response); } }
	 */

	
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveSlabs(@RequestBody List<PersonalLoanMST> slabList) {
        Map<String, Object> response = new HashMap<>();
 
        try {
            // Guard Clause: Check for empty payload
            if (slabList == null || slabList.isEmpty()) {
                response.put("status", "ERROR");
                response.put("message", "Request payload is empty or invalid.");
                return ResponseEntity.badRequest().body(response);
            }
 
            // Save records via service layer
            List<PersonalLoanMST> savedData = personalAndEduLoanService.saveSlabs(slabList);
 
            response.put("status", "SUCCESS");
            response.put("message", "Slab data saved successfully.");
            response.put("data", savedData);
 
            return ResponseEntity.ok(response);
 
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Failed to save slab data: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
	
	
	
	@GetMapping("/category/{categoryName}")
	public ResponseEntity<List<PersonalLoanMST>> getSlabsByCategory(@PathVariable String categoryName) {
		List<PersonalLoanMST> slabs = personalAndEduLoanService.getSlabsByCategory(categoryName);
		return ResponseEntity.ok(slabs);
	}

	@GetMapping("/pending")
	public ResponseEntity<List<PersonalLoanMST>> getPendingRecords() {
		List<PersonalLoanMST> pendingList = personalAndEduLoanService.getPendingRecords();
		return ResponseEntity.ok(pendingList);
	}

	@PostMapping("/updateStatus")
	public ResponseEntity<Map<String, Object>> updateSlabStatus(@RequestBody Map<String, Object> payload) {
		Map<String, Object> response = new HashMap<>();
		try {
			// Extract parameters from payload
			Long id = Long.parseLong(payload.get("id").toString());
			String status = payload.get("status").toString(); // "APPROVED" or "REJECTED"

			boolean updated = personalAndEduLoanService.updateStatus(id, status);

			if (updated) {
				response.put("status", "SUCCESS");
				response.put("message", "Status updated to " + status + " successfully.");
				return ResponseEntity.ok(response);
			} else {
				response.put("status", "FAILED");
				response.put("message", "Record not found with ID: " + id);
				return ResponseEntity.badRequest().body(response);
			}
		} catch (Exception e) {
			response.put("status", "ERROR");
			response.put("message", "Failed to update status: " + e.getMessage());
			return ResponseEntity.status(500).body(response);
		}
	}
}