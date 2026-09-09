package com.icici.dma.slabController.osp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.dto.OspPayoutRequest;
import com.icici.dma.dto.osp.OspCompareResponse;
import com.icici.dma.dto.osp.OspConfigCollectionRangeResponse;
import com.icici.dma.dto.osp.OspConfigDpdResponse;
import com.icici.dma.dto.osp.OspPendingResponse;
import com.icici.dma.slabEntity.osp.OspPayoutMaster;
import com.icici.dma.slabService.osp.OspConfigCollectionRangeService;
import com.icici.dma.slabService.osp.OspConfigDpdService;
import com.icici.dma.slabService.osp.OspPayoutService;

@RestController
@RequestMapping("/ospPayout")
@CrossOrigin
public class OspPayoutController {

	@Autowired
	private OspPayoutService service;
	
//	@Autowired
//	private OspCollectionRangeService ospCollectionRangeService;
	
	@Autowired
	private OspConfigCollectionRangeService configRangeService;
	
	//snz
	@Autowired
	private OspConfigDpdService dpdService;

//	@GetMapping("/ranges")
//	public List<OspCollectionRangeMaster> getRanges(@RequestParam String dpd) {
//
//		return ospCollectionRangeService.getRanges(dpd);
//
//	}
	
	@GetMapping("/ranges")
	public List<OspConfigCollectionRangeResponse> getRanges(
	        @RequestParam Long dpdId){

	    return configRangeService.getApprovedRanges(dpdId);

	}
	
	//snz
	@GetMapping("/dpds")
	public List<OspConfigDpdResponse> getDpds(){

	    return dpdService.getApprovedDpds();

	}

	@GetMapping("/fetch")
	public OspPayoutMaster fetch(
			@RequestParam String product,
			@RequestParam String subProduct,
			@RequestParam Long dpdId,
			@RequestParam String fromDate,
			@RequestParam String toDate) {

		return service.fetch(
				product,
				subProduct,
				dpdId,
				fromDate,
				toDate);
	}

//	@PostMapping("/save")
//	public ResponseEntity<?> save(
//			@RequestBody OspPayoutRequest request,
//			@RequestParam String user) {
//		try {
//			return ResponseEntity.ok( service.save(request, user));
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}
	
	@PostMapping("/save")
	public ResponseEntity<?> save(
	        @RequestBody OspPayoutRequest request,
	        @RequestParam String user) {

	    try {

	        service.save(request, user);

	        return ResponseEntity.ok("Submitted Successfully");

	    } catch (Exception e) {

	        return ResponseEntity.badRequest().body(e.getMessage());
	    }
	}

	@GetMapping("/pending")
	public List<OspPendingResponse> pending(
	        @RequestParam String user) {

	    return service.pending(user);

	}

	@GetMapping("/history")
	public List<OspPayoutMaster> history() {
		return service.history();
	}
	

	@PostMapping("/approve/{id}")
	public ResponseEntity<String> approve(
			@PathVariable Long id,
			@RequestParam String user) {
		try {
			service.approve(id, user);
			return ResponseEntity.ok(
					"Approved Successfully");
		} 
		catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	
	@PostMapping("/reject/{id}")
	public ResponseEntity<String> reject(
			@PathVariable Long id,
			@RequestParam String user,
			@RequestParam String remarks) {
		try {
			service.reject(id, user, remarks);
			return ResponseEntity.ok("Rejected Successfully");
		} 
		catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/compare/{id}")
	public List<Map<String, Object>> compare(@PathVariable Long id) {
		return service.compare(id);
	}

	@GetMapping("/makerCompare/{id}")
	public ResponseEntity<OspCompareResponse> makerCompare(
	        @PathVariable Long id) {

	    return ResponseEntity.ok(service.makerCompare(id));
	}

	@GetMapping("/checkerCompare/{id}")
	public ResponseEntity<OspCompareResponse> checkerCompare(
	        @PathVariable Long id) {

	    return ResponseEntity.ok(service.checkerCompare(id));
	}

	@GetMapping("/latestApprovedId")
	public Long latestApprovedId(
	        @RequestParam String product,
	        @RequestParam String subProduct,
	        @RequestParam Long dpdId,
	        @RequestParam String fromDate,
	        @RequestParam String toDate) {

	    return service.latestApprovedId(
	            product,
	            subProduct,
	            dpdId,
	            fromDate,
	            toDate);
	}

}