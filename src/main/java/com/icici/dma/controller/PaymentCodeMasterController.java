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
import com.icici.dma.dto.PaymentCodeDto;
import com.icici.dma.service.PaymentCodeService;
import com.icici.dma.serviceImpl.PaymentCodeUploadService;

@RestController
@RequestMapping("/DMAPayoutWeb3")
public class PaymentCodeMasterController {

    @Autowired
    private PaymentCodeService paymentCodeService;

    @Autowired
    private PaymentCodeUploadService paymentCodeUploadService;

    private String getUser(HttpServletRequest request) {
        return request.getHeader("user");
    }

    @PostMapping("/createPaymentCodeMaker")
    public String createPaymentCodeMaker(
            @RequestBody PaymentCodeDto dto,
            HttpServletRequest request) {

        paymentCodeService.createPaymentCodeMaker(
                dto,
                getUser(request));

        return "Payment Code created successfully";
    }

    @PutMapping("/updatePaymentCodeMaker")
    public String updatePaymentCodeMaker(
            @RequestBody PaymentCodeDto dto,
            HttpServletRequest request) {

        paymentCodeService.updatePaymentCodeMaker(
                dto,
                getUser(request));

        return "Payment Code updated successfully";
    }

    @PostMapping("/updatePaymentCodeChecker")
    public String updatePaymentCodeChecker(
            @RequestBody CheckerDecisionReq req,
            HttpServletRequest request) {

        paymentCodeService.updatePaymentCodeChecker(
                req,
                getUser(request));

        return "Checker action completed successfully";
    }

    @GetMapping("/getPaymentCodeMaker")
    public List<PaymentCodeDto> getPaymentCodeMaker() {
        return paymentCodeService.getPaymentCodeMaker();
    }

    @GetMapping("/getAllPaymentCodeChecker")
    public List<PaymentCodeDto> getAllPaymentCodeChecker(
            HttpServletRequest request) {

        return paymentCodeService.getAllPaymentCodeChecker(
                getUser(request));
    }

    @GetMapping("/getPaymentCodeByStatus")
    public List<PaymentCodeDto> getPaymentCodeByStatus(
            @RequestParam String status) {

        return paymentCodeService.getPaymentCodeByStatus(status);
    }

    @PostMapping("/uploadPaymentCodeExcel")
    public Map<String, Object> uploadPaymentCodeExcel(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {

        return paymentCodeUploadService.upload(
                file,
                getUser(request));
    }

    @GetMapping("/downloadPaymentCodeErrorExcel")
    public ResponseEntity<byte[]> downloadPaymentCodeErrorExcel(
            @RequestParam String uploadId) throws Exception {

        byte[] data =
                paymentCodeUploadService.downloadErrorExcel(uploadId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=Payment_Code_Errors.xlsx")
                .contentType(
                        MediaType.APPLICATION_OCTET_STREAM)
                .body(data);
    }
}