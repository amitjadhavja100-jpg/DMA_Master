package com.icici.dma.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.TdsCodeRateDto;
import com.icici.dma.service.TdsCodeRateService;
import com.icici.dma.service.TdsCodeRateUploadService;



@RestController
@RequestMapping("/DMAPayoutWeb3")
public class TdsCodeRateController {
 
    @Autowired
    private TdsCodeRateService tdsCodeRateService;
 
    @Autowired
    private TdsCodeRateUploadService tdsCodeRateUploadService;
 
@PostMapping("/uploadTdsCodeRate")
public ResponseEntity<?> uploadTdsCodeRateMaster(
        @RequestParam("file") MultipartFile file,
        HttpServletRequest request)
        throws Exception {
	
	System.out.println("==================== INSIDE uploadVendorMaster ======================");
 
    String user =
            request.getHeader("user");
    
    System.out.println("========== CONTROLLER START ==========");
    System.out.println("File Name : " + file.getOriginalFilename());
    System.out.println("File Size : " + file.getSize());
    System.out.println("User : " + user);
     
    System.out.println("Before Service Call");
 
    return ResponseEntity.ok(
    		tdsCodeRateUploadService.upload(
                    file,
                    user));
}
 
@GetMapping("/downloadTdsCodeRateErrorExcel")
public ResponseEntity<byte[]>
downloadVendorErrorExcel(
        @RequestParam String uploadId)
        throws Exception {
 
    byte[] excelData =
    		tdsCodeRateUploadService
                    .exportErrorExcel(
                            uploadId);
 
    HttpHeaders headers =
            new HttpHeaders();
 
    headers.add(
            "Content-Disposition",
            "attachment; filename=Vendor_Errors.xlsx");
 
    return ResponseEntity.ok()
            .headers(headers)
            .contentType(
                    MediaType.APPLICATION_OCTET_STREAM)
            .body(excelData);
}
 
@PostMapping("/createTdsCodeRateByMaker")
public ResponseEntity<?> createTdsCodeRateByMaker(
        @RequestBody TdsCodeRateDto dto,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .createTdsCodeRate(
                            dto,
                            user));
}
 
@PostMapping("/updateTdsCodeRateByMaker")
public ResponseEntity<?> updateTdsCodeRateByMaker(
        @RequestBody TdsCodeRateDto dto,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .updateTdsCodeRateByMaker(
                            dto,
                            user));
}
 
@PostMapping("/updateTdsCodeRateByChecker")
public ResponseEntity<?> updateTdsCodeRateByChecker(
        @RequestBody CheckerDecisionReq req,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .updateTdsCodeRateByChecker(
                            req,
                            user));
}
 
@GetMapping("/getAllTdsCodeRateByMaker")
public ResponseEntity<?> getAllTdsCodeRateByMaker() {
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .getTdsCodeRateMaker());
}
 
@GetMapping("/getAllTdsCodeRateByChecker")
public ResponseEntity<?> getAllTdsCodeRateByChecker(
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .getAllTdsCodeRateChecker(
                            user));
}
 
@GetMapping("/getTdsCodeRateByStatus")
public ResponseEntity<?> getTdsCodeRateByStatus(
        @RequestParam String status) {
 
    return ResponseEntity.ok(
    		tdsCodeRateService
                    .getTdsCodeRateByStatus(
                            status));
}
}
