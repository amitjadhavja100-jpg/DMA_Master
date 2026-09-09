package com.icici.dma.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icici.dma.dto.IPKPendingSummary;
import com.icici.dma.dto.IPKStructureSaveRequest;
import com.icici.dma.dto.IncentiveStructureDto;
import com.icici.dma.dto.MPKCommonPendingSummary;
import com.icici.dma.dto.RetainerPayloadDto;
import com.icici.dma.dto.UsedCarStructureRegionSaveRequest;
import com.icici.dma.dto.UsedCarStructureSaveRequest;
import com.icici.dma.repository.AutoManipalRetainerMasterRepository;
import com.icici.dma.service.IPKStructureService;
import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterDummy;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterManipal;

@RequestMapping("/counsellor")
@Controller
public class CounsellorController {

	@Autowired
	IPKStructureService ipkStructureService;
	
	 @Autowired
	    private AutoManipalRetainerMasterRepository repository;
	private static final Logger logger = LoggerFactory.getLogger(CounsellorController.class);

	@PostMapping("/save")
	public ResponseEntity<?> saveIPKStructure(@RequestBody IPKStructureSaveRequest request) {
		logger.info("saveIPKStructure called with counsellorType={}", request.getCounsellorType());
		try {
			String structureType = "";
			if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR - I process")) {
				ipkStructureService.saveIPKStructure(request);
				structureType += "Structure submitted for approval successfully";
			} else if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR_ other than Manipal")) {
				ipkStructureService.saveMPKStructure(request);
				structureType += "Structure submitted for approval successfully";
			} else {
				structureType += "InValid Structure";
			}
			logger.info("saveIPKStructure completed: {}", structureType);
			return ResponseEntity.ok(structureType);
		} catch (Exception e) {
			logger.error("Error in saveIPKStructure", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving structure: " + e.getMessage());
		}
	}

	@GetMapping("/pending")
	@ResponseBody
	public List<IPKPendingSummary> pending() {
		logger.info("Fetching IPK pending list");
		return ipkStructureService.getIPPendingList();
	}

	@PostMapping("/view/{sequence}")
	@ResponseBody
	public List<AutoProcessIncentiveStructureMasterDummy> view(@PathVariable Long sequence) {
		logger.info("Viewing IPK structure for sequence={}", sequence);
		return ipkStructureService.getBySequence(sequence);
	}

	@PostMapping("/approve/{sequence}")
	public ResponseEntity<?> approve(@PathVariable Long sequence) {
		logger.info("Approving IPK sequence={}", sequence);
		ipkStructureService.approve(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/reject/{sequence}")
	public ResponseEntity<?> reject(@PathVariable Long sequence) {
		logger.info("Rejecting IPK sequence={}", sequence);
		ipkStructureService.reject(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}

	@GetMapping("/mp/pending")
	@ResponseBody
	public List<IPKPendingSummary> pendingMP() {
		logger.info("Fetching MP pending list");
		return ipkStructureService.getPendingList();
	}

	@PostMapping("/mp/view/{sequence}")
	@ResponseBody
	public List<AutoProcessIncentiveStructureMasterManipal> viewMP(@PathVariable Long sequence) {
		logger.info("Viewing MP structure for sequence={}", sequence);
		return ipkStructureService.getBySequenceMP(sequence);
	}

	@PostMapping("/mp/approve/{sequence}")
	public ResponseEntity<?> approveMP(@PathVariable Long sequence) {
		logger.info("Approving MP sequence={}", sequence);
		ipkStructureService.approveMP(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/mp/reject/{sequence}")
	public ResponseEntity<?> rejectMP(@PathVariable Long sequence) {
		logger.info("Rejecting MP sequence={}", sequence);
		ipkStructureService.rejectMP(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}

	@GetMapping("/ip/common/pending")
	@ResponseBody
	public List<MPKCommonPendingSummary> pendingIPCommon() {
		logger.info("Fetching IP common pending list");
		return ipkStructureService.getPendingIPCommonList();
	}

	@PostMapping("/ip/common/approve/{sequence}")
	public ResponseEntity<?> approveCommonIP(@PathVariable Integer sequence) {
		logger.info("Approving IP common sequence={}", sequence);
		ipkStructureService.approveCommonIP(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/ip/common/reject/{sequence}")
	public ResponseEntity<?> rejectCommonIP(@PathVariable Integer sequence) {
		logger.info("Rejecting IP common sequence={}", sequence);
		ipkStructureService.rejectCommonIP(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}

	/* common structure other than manipal */
	@PostMapping("common/structure/save")
	public ResponseEntity<?> save(@RequestBody UsedCarStructureSaveRequest request) {
		logger.info("Saving common structure with counsellorType={}", request.getCounsellorType());
		try {
			String message = "";
			if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR - I process")) {
				ipkStructureService.saveIPCStructure(request);
				message += "Structure submitted for approval successfully";
			} else if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR_ other than Manipal")) {
				ipkStructureService.saveStructure(request);
				message += "Structure submitted for approval successfully";
			} else {
				message += "invalid structure";
			}
			logger.info("save (common/structure/save) completed: {}", message);
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			logger.error("Error in save (common/structure/save)", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving structure: " + e.getMessage());
		}
	}

	@GetMapping("/mp/common/pending")
	@ResponseBody
	public List<MPKCommonPendingSummary> pendingMPCommon() {
		logger.info("Fetching MP common pending list");
		return ipkStructureService.getPendingMPCommonList();
	}

	@PostMapping("/mp/common/approve/{sequence}")
	public ResponseEntity<?> approveCommonMP(@PathVariable Integer sequence) {
		logger.info("Approving MP common sequence={}", sequence);
		ipkStructureService.approveCommonMP(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/mp/common/reject/{sequence}")
	public ResponseEntity<?> rejectCommonMP(@PathVariable Integer sequence) {
		logger.info("Rejecting MP common sequence={}", sequence);
		ipkStructureService.rejectCommonMP(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}

	/* I process and other than manipal other region */
	@PostMapping("other/structure/save")
	public ResponseEntity<?> save(@RequestBody UsedCarStructureRegionSaveRequest request) {
		logger.info("Saving other-region structure with counsellorType={}", request.getCounsellorType());
		try {
			String message = "";
			if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR - I process")) {
				ipkStructureService.saveIPPNStructure(request);
				message += "Structure submitted for approval successfully";
			} else if (request.getCounsellorType().equalsIgnoreCase("AUTO COUNSELLOR_ other than Manipal")) {
				ipkStructureService.saveMPPNStructure(request);
				message += "Structure submitted for approval successfully";
			} else {
				message += "invalid structure";
			}
			logger.info("save (other/structure/save) completed: {}", message);
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			logger.error("Error in save (other/structure/save)", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving structure: " + e.getMessage());
		}
	}

	@GetMapping("/ip/other/pending")
	@ResponseBody
	public List<MPKCommonPendingSummary> pendingIPOther() {
		logger.info("Fetching IP other pending list");
		return ipkStructureService.getPendingIPOtherList();
	}

	@PostMapping("/ip/other/approve/{sequence}")
	public ResponseEntity<?> approveOtherIP(@PathVariable Integer sequence) {
		logger.info("Approving IP other sequence={}", sequence);
		ipkStructureService.approveOtherIP(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/ip/other/reject/{sequence}")
	public ResponseEntity<?> rejectOtherIP(@PathVariable Integer sequence) {
		logger.info("Rejecting IP other sequence={}", sequence);
		ipkStructureService.rejectOtherIP(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}

	@GetMapping("/mp/other/pending")
	@ResponseBody
	public List<MPKCommonPendingSummary> pendingMPOther() {
		logger.info("Fetching MP other pending list");
		return ipkStructureService.getPendingMPOtherList();
	}

	@PostMapping("/mp/other/approve/{sequence}")
	public ResponseEntity<?> approveOtherMP(@PathVariable Integer sequence) {
		logger.info("Approving MP other sequence={}", sequence);
		ipkStructureService.approveOtherMP(sequence);
		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/mp/other/reject/{sequence}")
	public ResponseEntity<?> rejectOtherMP(@PathVariable Integer sequence) {
		logger.info("Rejecting MP other sequence={}", sequence);
		ipkStructureService.rejectOtherMP(sequence);
		return ResponseEntity.ok("Rejected Successfully");
	}
	
	/* retainer i process*/
	
	@PostMapping("/retainer/save")
    public ResponseEntity<Map<String, Object>> saveRetainerData(@RequestBody RetainerPayloadDto payload) {
        List<AutoManipalRetainerMaster> savedRecords = ipkStructureService.saveRetainerMaster(payload);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Retainer Master records created successfully!");
        response.put("totalRecordsSaved", savedRecords.size());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
	
	 @GetMapping("/retainer/fetchingPending")
	    @ResponseBody
	    public List<AutoManipalRetainerMaster> getPendingRecords() {
	        return repository.findPendingSummary();
	    }
	 
	 
	 @PostMapping("/retainer/approve/{sequence}")
	    public ResponseEntity<?> approveRet(@PathVariable Long sequence) {
	        List<AutoManipalRetainerMaster> list = repository.findBySequence(sequence);
	        for (AutoManipalRetainerMaster obj : list) {
	            obj.setStatus("Y");
	            repository.save(obj);
	        }
	        return ResponseEntity.ok("Approved Successfully");
	    }
	    // --- 6. REJECT BATCH BY SEQUENCE ---
	    @PostMapping("/retainer/reject/{sequence}")
	    public ResponseEntity<?> rejectRet(@PathVariable Long sequence) {
	        List<AutoManipalRetainerMaster> list = repository.findBySequence(sequence);
	        for (AutoManipalRetainerMaster obj : list) {
	            obj.setStatus("R");
	            repository.save(obj);
	        }
	        return ResponseEntity.ok("Rejected Successfully");
	    }
	    
		/*
		 * i process PAN india
		 */
	    
		@PostMapping("/panindia/saveAll")
		public ResponseEntity<Map<String, Object>> saveIncentiveStructures(
				@RequestBody IncentiveStructureDto requestDto) {
			Map<String, Object> response = new HashMap<>();
			try {
				// Validate incoming payload dates
				if (requestDto.getCycleFromDate() == null || requestDto.getCycleToDate() == null) {
					response.put("success", false);
					response.put("message", "Cycle From Date and To Date are mandatory fields.");
					return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
				}
				// Call your service implementation layer to handle the DB transaction saves
				ipkStructureService.saveIncentiveStructuresAll(requestDto);
				response.put("success", true);
				response.put("message", "Incentive structures and cycle dates saved successfully.");
				return new ResponseEntity<>(response, HttpStatus.CREATED);
			} catch (Exception e) {
				response.put("success", false);
				response.put("message", "Error occurred while saving: " + e.getMessage());
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

}
