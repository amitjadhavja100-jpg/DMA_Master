package com.icici.dma.slabController.ccrValuation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icici.dma.slabDto.ccrValuation.ValuationMasterDTO;
import com.icici.dma.slabService.ccrValuation.ValuationMasterService;

@RestController
@RequestMapping("/valuationMaster")
public class ValuationMasterController {

    @Autowired
    private ValuationMasterService service;

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestBody ValuationMasterDTO request,
            @RequestParam("userId") String userId) {

        try {

            ValuationMasterDTO result =
                    service.create(
                            request,
                            userId
                    );

            return ResponseEntity.ok(result);

        } catch (Exception e) {

            return ResponseEntity.badRequest()
                    .body(e.getMessage());
        }
    }

	@GetMapping("/all")
	public ResponseEntity<?> getAll() {
		try {
			return ResponseEntity.ok(service.getAll());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/active")
	public ResponseEntity<?> getActive() {
		try {
			return ResponseEntity.ok(service.getActive());
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getById(@PathVariable Long id) {

		try {
			return ResponseEntity.ok(service.getById(id));

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody ValuationMasterDTO request,
            HttpServletRequest httpRequest) {

		try {

			HttpSession session = httpRequest.getSession();
			String userId = (String) session.getAttribute("USER_ID");
			ValuationMasterDTO result = service.update(id, request, userId);

			return ResponseEntity.ok(result);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
    }
}