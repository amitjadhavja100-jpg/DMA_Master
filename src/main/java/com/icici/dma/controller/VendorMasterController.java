package com.icici.dma.controller;

import java.util.Map;

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
import com.icici.dma.dto.VendorMasterDto;
import com.icici.dma.service.VendorMasterService;
import com.icici.dma.service.VendorMasterUploadService;

@RestController
@RequestMapping("/DMAPayoutWeb3")
public class VendorMasterController {
 
    @Autowired
    private VendorMasterService vendorMasterService;
 
    @Autowired
    private VendorMasterUploadService vendorMasterUploadService;
 
@PostMapping("/uploadVendorMaster")
public ResponseEntity<?> uploadVendorMaster(
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
            vendorMasterUploadService.upload(
                    file,
                    user));
}
 
@GetMapping("/downloadVendorErrorExcel")
public ResponseEntity<byte[]>
downloadVendorErrorExcel(
        @RequestParam String uploadId)
        throws Exception {
 
    byte[] excelData =
            vendorMasterUploadService
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
 
@PostMapping("/createVendorMasterByMaker")
public ResponseEntity<?> createVendorMasterByMaker(
        @RequestBody VendorMasterDto dto,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
            vendorMasterService
                    .createVendorMaster(
                            dto,
                            user));
}
 
@PostMapping("/updateVendorMasterByMaker")
public ResponseEntity<?> updateVendorMasterByMaker(
        @RequestBody VendorMasterDto dto,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
            vendorMasterService
                    .updateVendorMasterByMaker(
                            dto,
                            user));
}
 
@PostMapping("/updateVendorMasterByChecker")
public ResponseEntity<?> updateVendorMasterByChecker(
        @RequestBody CheckerDecisionReq req,
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
            vendorMasterService
                    .updateVendorMasterByChecker(
                            req,
                            user));
}
 
@GetMapping("/getAllVendorMasterByMaker")
public ResponseEntity<?> getAllVendorMasterByMaker() {
 
    return ResponseEntity.ok(
            vendorMasterService
                    .getVendorMasterMaker());
}
 
@GetMapping("/getAllVendorMasterByChecker")
public ResponseEntity<?> getAllVendorMasterByChecker(
        HttpServletRequest request) {
 
    String user =
            request.getHeader("user");
 
    return ResponseEntity.ok(
            vendorMasterService
                    .getAllVendorMasterChecker(
                            user));
}
 
@GetMapping("/getVendorMasterByStatus")
public ResponseEntity<?> getVendorMasterByStatus(
        @RequestParam String status) {
 
    return ResponseEntity.ok(
            vendorMasterService
                    .getVendorMasterByStatus(
                            status));
}
} 