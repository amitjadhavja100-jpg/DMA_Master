package com.icici.dma.service;

import java.util.List;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.TdsCodeRateDto;

 
public interface TdsCodeRateService {
 
    List<TdsCodeRateDto> getTdsCodeRateMaker();
 
    String createTdsCodeRate(
    		TdsCodeRateDto dto,
            String user);
 
    String updateTdsCodeRateByMaker(
    		TdsCodeRateDto dto,
            String user);
 
    List<TdsCodeRateDto> getAllTdsCodeRateChecker(
            String user);
 
    String updateTdsCodeRateByChecker(
            CheckerDecisionReq req,
            String user);
 
    List<TdsCodeRateDto> getTdsCodeRateByStatus(
            String status);
}