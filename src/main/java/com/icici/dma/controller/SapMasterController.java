package com.icici.dma.controller;

import java.io.ByteArrayInputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.SapMasterDto;
import com.icici.dma.service.SapMasterService;
import com.icici.dma.serviceImpl.SapMasterUploadService;

@RestController
@RequestMapping("/DMAPayoutWeb3")
public class SapMasterController {
	
	private static final Logger logger = LogManager.getLogger(MasterController.class);

    @Autowired
    private SapMasterService sapMasterService;

    @Autowired
    private SapMasterUploadService sapMasterUploadService;
    
  //snz
  	@PostMapping("/uploadSAPMaster")
  	public ResponseEntity<?> uploadSAPMaster(
  	        @RequestParam("file") MultipartFile file,
  	        HttpServletRequest request) {

  	    logger.info("SAP MASTER UPLOAD API");

  	    try {
  	    	//USERNAME
  	        String user =
  	                request.getHeader("user");

  	        if (user == null || user.trim().isEmpty()) {
  	            return ResponseEntity.badRequest()
  	                    .body("User header missing");
  	        }
  	        // FILE VALIDATION
  	        if (file == null || file.isEmpty()) {

  	            return ResponseEntity.badRequest()
  	                    .body("Please upload valid file");
  	        }
  	        // FILE EXTENSION VALIDATION
  	        String fileName =
  	                file.getOriginalFilename();
  	        if (fileName == null
  	                || !(fileName.endsWith(".xlsx")
  	                || fileName.endsWith(".xls"))) {

  	            return ResponseEntity.badRequest()
  	                    .body("Only Excel files allowed");
  	        }
  	        // CALL SERVICE
  	        Map<String, Object> response =
  	                sapMasterUploadService
  	                        .upload(file, user);
  	        logger.info(
  	                "========== SAP MASTER UPLOAD SUCCESS ==========");
  	        return ResponseEntity.ok(response);
  	    } catch (Exception e) {
  	        logger.error(
  	                "SAP Upload Failed",
  	                e);

  	        return ResponseEntity
  	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@GetMapping("/downloadSAPErrorExcel")
  	public ResponseEntity<?> downloadSAPErrorExcel(
  	        @RequestParam("uploadId") String uploadId) {
  	    logger.info(
  	            "========== DOWNLOAD SAP ERROR FILE =========");
  	    try {

  	        ByteArrayInputStream in = sapMasterUploadService.exportErrorExcel(uploadId);
  	        HttpHeaders headers = new HttpHeaders();

  	        headers.add(
  	                "Content-Disposition",
  	                "attachment; filename=SAP_UPLOAD_ERRORS.xlsx");

  	        return ResponseEntity.ok()
  	                .headers(headers)
  	                .contentType(
  	                        MediaType.parseMediaType(
  	                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
  	                .body(
  	                        new InputStreamResource(in));
  	    } catch (Exception e) {
  	        logger.error(
  	                "Error while downloading SAP error file",
  	                e);
  	        return ResponseEntity
  	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@PostMapping("/createSAPMasterByMaker")
  	public ResponseEntity<?> createSAPMasterByMaker(
  	        @RequestBody SapMasterDto dto,
  	        HttpServletRequest request) {
  		
  	    try {
  	        String user = request.getHeader("user");
  	        sapMasterService.createSAPMaster(dto, user);
  	        return ResponseEntity.ok( "SAP Master created successfully");
  	        
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@PostMapping("/updateSAPMasterByMaker")
  	public ResponseEntity<?> updateSAPMasterByMaker(
  	        @RequestBody SapMasterDto dto,
  	        HttpServletRequest request) {

  	    try {
  	        String user = request.getHeader("user");
  	        sapMasterService.updateSAPMasterByMaker(dto, user);
  	        return ResponseEntity.ok(
  	                "SAP Master updated successfully");
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@PostMapping("/updateSAPMasterByChecker")
  	public ResponseEntity<?> updateSAPMasterByChecker(
  	        @RequestBody CheckerDecisionReq requestPayload,
  	        HttpServletRequest request) {
  	    try {
  	        String user = request.getHeader("user");
  	        sapMasterService .updateSAPMasterByChecker(requestPayload, user);
  	        return ResponseEntity.ok(
  	                "SAP Master checker action completed");
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@GetMapping("/getAllSAPMasterByMaker")
  	public ResponseEntity<?> getAllSAPMasterByMaker() {
  	    try {
  	        return ResponseEntity.ok(
  	                sapMasterService.getSAPMasterMaker());
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@GetMapping("/getAllSAPMasterByChecker")
  	public ResponseEntity<?> getAllSAPMasterByChecker(
  	        HttpServletRequest request) {
  	    try {
  	        String user =
  	                request.getHeader("user");
  	        
  	        return ResponseEntity.ok(
  	                sapMasterService
  	                        .getAllSAPMasterChecker(user));
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
  	
  	@GetMapping("/getSAPMasterByStatus")
  	public ResponseEntity<?> getSAPMasterByStatus(
  	        @RequestParam("status") String status) {
  	    try {
  	        return ResponseEntity.ok(
  	                sapMasterService
  	                        .getSAPMasterByStatus(status));
  	    } catch (Exception e) {
  	        return ResponseEntity.badRequest()
  	                .body(e.getMessage());
  	    }
  	}
    
    

}