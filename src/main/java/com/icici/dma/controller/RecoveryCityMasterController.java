package com.icici.dma.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.RecoveryCityDto;
import com.icici.dma.service.RecoveryCityService;
import com.icici.dma.serviceImpl.RecoveryCityUploadService;

@CrossOrigin("*")
@RestController
@RequestMapping("/DMAPayoutWeb3")
public class RecoveryCityMasterController {

    @Autowired
    private RecoveryCityService recoveryCityService;

    @Autowired
    private RecoveryCityUploadService recoveryCityUploadService;

    // MAKER GRID
    @GetMapping("/getRecoveryCityMaker")
    public ResponseEntity<?> getRecoveryCityMaker() {

        List<RecoveryCityDto> list =
                recoveryCityService.getRecoveryCityMaker();

        return ResponseEntity.ok(list);
    }

    // CREATE MAKER
    @PostMapping("/createRecoveryCityMaker")
    public ResponseEntity<?> createRecoveryCityMaker(
            @RequestBody RecoveryCityDto dto,
            @RequestHeader("user") String user) {

        if (dto == null) {
            return ResponseEntity.badRequest()
                    .body("Request body is null");
        }

        if (dto.getCity() == null) {
            return ResponseEntity.badRequest()
                    .body("City is mandatory");
        }

        recoveryCityService.createRecoveryCity(dto, user);

        return ResponseEntity.ok(
                "Record submitted successfully and sent for approval");
    }

    // UPDATE MAKER  
    @PostMapping("/updateRecoveryCityMaker")
    public ResponseEntity<?> updateRecoveryCityMaker(
            @RequestBody RecoveryCityDto dto,
            @RequestHeader("user") String user) {

        if (dto == null || dto.getCity() == null) {
            return ResponseEntity.badRequest()
                    .body("City is mandatory");
        }

        recoveryCityService.updateRecoveryCityByMaker(dto, user);

        return ResponseEntity.ok(
                "Record updated successfully and sent for approval");
    }

    //CHECKER GRID  
    @GetMapping("/getRecoveryCityChecker")
    public ResponseEntity<?> getRecoveryCityChecker(
            @RequestHeader("user") String user) {

        List<RecoveryCityDto> list =
                recoveryCityService.getAllRecoveryCityChecker(user);

        return ResponseEntity.ok(list);
    }

    //CHECKER APPROVE / REJECT    
    @PostMapping("/updateRecoveryCityChecker")
    public ResponseEntity<?> updateRecoveryCityChecker(
            @RequestBody CheckerDecisionReq reqPayload,
            @RequestHeader("user") String user) {

        if (reqPayload == null) {
            return ResponseEntity.badRequest()
                    .body("Request payload is null");
        }

        if (reqPayload.getDecision() == null
                || reqPayload.getDecision().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Decision is mandatory");
        }

        if (reqPayload.getPrimaryIds() == null
                || reqPayload.getPrimaryIds().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("At least one ID must be selected");
        }

        recoveryCityService.updateRecoveryCityByChecker(
                reqPayload,
                user);

        String action =
                "A".equalsIgnoreCase(reqPayload.getDecision())
                        ? "APPROVE"
                        : "REJECT";

        return ResponseEntity.ok(
                action + " action completed successfully");
    }

    // STATUS GRID
    @GetMapping("/getRecoveryCityByStatus")
    public ResponseEntity<?> getRecoveryCityByStatus(
            @RequestParam("status") String status) {

        List<RecoveryCityDto> list =
                recoveryCityService.getRecoveryCityByStatus(status);

        return ResponseEntity.ok(list);
    }

    // EXCEL UPLOAD 
    @PostMapping("/uploadRecoveryCityExcel")
    public ResponseEntity<?> uploadRecoveryCityExcel(
            @RequestParam("file") MultipartFile file,
            @RequestHeader("user") String user) {

        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Excel file is required");
        }

        Map<String, Object> response =
                recoveryCityUploadService.upload(file, user);

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/downloadRecoveryCityErrorExcel")
    public ResponseEntity<InputStreamResource> downloadRecoveryCityErrorExcel(
            @RequestParam("uploadId") String uploadId) {

        ByteArrayInputStream excel =
                recoveryCityUploadService.downloadErrorExcel(uploadId);

        HttpHeaders headers =
                new HttpHeaders();

        headers.add(
                "Content-Disposition",
                "attachment; filename=Recovery_City_Error.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excel));
    }
}