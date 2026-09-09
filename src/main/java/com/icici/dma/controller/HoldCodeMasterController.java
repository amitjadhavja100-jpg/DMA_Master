package com.icici.dma.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.HoldCodeDto;
import com.icici.dma.service.HoldCodeService;
import com.icici.dma.serviceImpl.HoldCodeUploadService;

@RestController
@RequestMapping("/DMAPayoutWeb3")
public class HoldCodeMasterController {

    @Autowired
    private HoldCodeService holdCodeService;

    @Autowired
    private HoldCodeUploadService holdCodeUploadService;

    private String getUser(HttpServletRequest request) {
        return request.getHeader("user");
    }

    @PostMapping("/createHoldCodeMaker")
    public String createHoldCodeMaker(
            @RequestBody HoldCodeDto dto,
            HttpServletRequest request) {

        holdCodeService.createHoldCodeMaker(
                dto,
                getUser(request));

        return "Hold Code created successfully";
    }

    @PostMapping("/updateHoldCodeMaker")
    public String updateHoldCodeMaker(
            @RequestBody HoldCodeDto dto,
            HttpServletRequest request) {

        holdCodeService.updateHoldCodeMaker(
                dto,
                getUser(request));

        return "Hold Code updated successfully";
    }

    @PostMapping("/updateHoldCodeChecker")
    public String updateHoldCodeChecker(
            @RequestBody CheckerDecisionReq req,
            HttpServletRequest request) {

        holdCodeService.updateHoldCodeChecker(
                req,
                getUser(request));

        return "Checker action completed successfully";
    }

    @GetMapping("/getHoldCodeMaker")
    public List<HoldCodeDto> getHoldCodeMaker() {
        return holdCodeService.getHoldCodeMaker();
    }

    @GetMapping("/getAllHoldCodeChecker")
    public List<HoldCodeDto> getAllHoldCodeChecker(
            HttpServletRequest request) {

        return holdCodeService.getAllHoldCodeChecker(
                getUser(request));
    }

    @GetMapping("/getHoldCodeByStatus")
    public List<HoldCodeDto> getHoldCodeByStatus(
            @RequestParam String status) {

        return holdCodeService.getHoldCodeByStatus(status);
    }

    @PostMapping("/uploadHoldCodeExcel")
    public Map<String, Object> uploadHoldCodeExcel(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        return holdCodeUploadService.upload(
                file,
                getUser(request));
    }

    @GetMapping("/downloadHoldCodeErrorExcel")
    public ResponseEntity<byte[]> downloadHoldCodeErrorExcel(
            @RequestParam String uploadId) throws Exception {

        byte[] data =
                holdCodeUploadService.downloadErrorExcel(uploadId);

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=Hold_Code_Errors.xlsx"
                )
                .contentType(
                    MediaType.APPLICATION_OCTET_STREAM
                )
                .body(data);
    }
}