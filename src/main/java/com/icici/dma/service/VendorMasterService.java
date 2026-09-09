package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.VendorMasterDto;

 
public interface VendorMasterService {
 
    List<VendorMasterDto> getVendorMasterMaker();
 
    String createVendorMaster(
            VendorMasterDto dto,
            String user);
 
    String updateVendorMasterByMaker(
            VendorMasterDto dto,
            String user);
 
    List<VendorMasterDto> getAllVendorMasterChecker(
            String user);
 
    String updateVendorMasterByChecker(
            CheckerDecisionReq req,
            String user);
 
    List<VendorMasterDto> getVendorMasterByStatus(
            String status);
}