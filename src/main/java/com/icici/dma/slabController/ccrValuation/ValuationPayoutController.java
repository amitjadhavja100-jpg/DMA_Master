package com.icici.dma.slabController.ccrValuation;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutRequest;
import com.icici.dma.slabDto.ccrValuation.ValuationPayoutResponse;
import com.icici.dma.slabEntity.ccrValuation.ValuationSlabMaster;
import com.icici.dma.slabService.ccrValuation.ValuationPayoutService;

@RestController
@RequestMapping("/valuationPayout")
public class ValuationPayoutController {

    @Autowired
    private ValuationPayoutService service;
    
    @GetMapping("/valuations")
    public List<ValuationMasterDTO> getValuations() {

        return service.getValuations();
    }
    
    @GetMapping("/fetch")
    public ValuationPayoutResponse fetch(
            @RequestParam String product,
            @RequestParam String subProduct,
            @RequestParam String fromDate,
            @RequestParam String toDate) {

		LocalDate from = LocalDate.parse(fromDate);
		LocalDate to = LocalDate.parse(toDate);

        return service.fetch(
                product,
                subProduct,
                from,
                to
        );
    }
    
    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestBody ValuationPayoutRequest request,
            HttpServletRequest httpRequest) {

		HttpSession session = httpRequest.getSession();
		String userId = (String) session.getAttribute("USER_ID");

		try {
			String result = service.save(request, userId);
			return ResponseEntity.ok(result);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
    
    @PostMapping("/approve/{id}")
    public ResponseEntity<String> approve(
            @PathVariable Long id,
            @RequestParam String user) {

		try {
			return ResponseEntity.ok(service.approve(id, user));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
    
    @PostMapping("/reject/{id}")
    public ResponseEntity<String> reject(
            @PathVariable Long id,
            @RequestParam String user,
            @RequestParam String remarks) {

        try {
            return ResponseEntity.ok(
                    service.reject(
                            id,
                            user,
                            remarks
                    )
            );

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
    
    @GetMapping("/pending")
    public List<ValuationSlabMaster> getPending(
            @RequestParam String user) {

        return service.getCheckerList(user);
    }
    
    @GetMapping("/compare/{id}")
    public ResponseEntity<Map<String, Object>> compare(
            @PathVariable Long id,
            @RequestParam String mode) {

		try {
			return ResponseEntity.ok(service.compare(id, mode));

		} catch (Exception e) {

			return ResponseEntity.badRequest().build();
		}
    }
    
    @GetMapping("/latestApprovedId")
    @ResponseBody
    public Long latestApprovedId(
            @RequestParam String product,
            @RequestParam String subProduct,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        return service.latestApprovedId(
                product,
                subProduct,
                fromDate,
                toDate
        );
    }
}