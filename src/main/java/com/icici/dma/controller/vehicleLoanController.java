package com.icici.dma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.icici.dma.dto.StatusUpdateRequest;
import com.icici.dma.dto.VehicleCvPayoutStructureRequest;
import com.icici.dma.dto.VehiclePayoutStructureRequest;
import com.icici.dma.serviceImpl.VehiclePayoutStructureService;
import com.icici.dma.slabEntity.VehicleCvPayoutStructure;
import com.icici.dma.slabEntity.VehicleTwPayoutStructure;

@Controller
@RequestMapping("/vehicle")
public class vehicleLoanController {

	@Autowired
	private VehiclePayoutStructureService structureService;

	@GetMapping("/maker/page")
	public String getPage() {

		return "autoVehicle";
	}

	@PostMapping("/saveTWStructure")
	public ResponseEntity<String> saveStructure(@RequestBody VehiclePayoutStructureRequest request) {
		try {

			String userId = "SYSTEM";

			structureService.saveStructure(request, userId);
			return ResponseEntity.ok("Data Saved");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/checker/page")
	public String getCheckerPage() {

		return "autoVehicleChecker";
	}

	@GetMapping("/tw/pending")
	@ResponseBody
	public ResponseEntity<?> gettwVehiclePending() {
		try {

			List<VehicleTwPayoutStructure> twCheckerStructure = structureService.getChekcerDataFortwVehicle();
			if (twCheckerStructure == null || twCheckerStructure.size() == 0) {
				return ResponseEntity.status(404).body("No Structure found for give data");
			}
			return ResponseEntity.ok(twCheckerStructure);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/tw/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestBody StatusUpdateRequest request) {
		try {
			String checkedBy = "SYSTEM";

			structureService.updateStatus(request.getIds(), request.getStructureType(), request.getStatus(), checkedBy);

			return ResponseEntity.ok(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/cv/save")
	public ResponseEntity<?> saveCvStructure(@RequestBody VehicleCvPayoutStructureRequest request) {
		try {
			String userId = "SYSTEM";

			structureService.saveCvStructure(request, userId);
			return ResponseEntity.ok("Structure saved successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@GetMapping("/cv/pending")
	@ResponseBody
	public ResponseEntity<?> getcvVehiclePending() {
		try {

			List<VehicleCvPayoutStructure> cvCheckerStructure = structureService.getChekcerDataForcvVehicle();
			if (cvCheckerStructure == null || cvCheckerStructure.size() == 0) {
				return ResponseEntity.status(404).body("No Structure found for give data");
			}
			return ResponseEntity.ok(cvCheckerStructure);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

	@PostMapping("/tw/approve")
	@ResponseBody
	public ResponseEntity<?> approve(@RequestBody List<Long> ids) {

		structureService.approve(ids);

		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/tw/reject")
	@ResponseBody
	public ResponseEntity<?> reject(@RequestBody List<Long> ids) {

		structureService.reject(ids);

		return ResponseEntity.ok("Rejected Successfully");
	}
	
	@PostMapping("/tw/view")
	@ResponseBody
	public List<VehicleTwPayoutStructure> view(@RequestBody List<Long> ids){
	 
	return structureService.getByIds(ids);
	 
	}
	 
	
	@PostMapping("/cv/approve")
	@ResponseBody
	public ResponseEntity<?> approveCV(@RequestBody List<Long> ids) {

		structureService.approveCV(ids);

		return ResponseEntity.ok("Approved Successfully");
	}

	@PostMapping("/cv/reject")
	@ResponseBody
	public ResponseEntity<?> rejectCV(@RequestBody List<Long> ids) {

		structureService.rejectCV(ids);

		return ResponseEntity.ok("Rejected Successfully");
	}

}
