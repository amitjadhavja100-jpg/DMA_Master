package com.icici.dma.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.dto.CheckerDecisionReq;
import com.icici.dma.dto.TdsCodeRateDto;
import com.icici.dma.dto.VendorMasterDto;
import com.icici.dma.globalException.ResourceNotFoundException;
import com.icici.dma.helper.ActionConstant;
import com.icici.dma.helper.StatusConstant;
import com.icici.dma.model.TdsCodeRate;
import com.icici.dma.model.TdsCodeRateTemp;
import com.icici.dma.model.VendorMaster;
import com.icici.dma.model.VendorMasterTemp;
import com.icici.dma.repository.TdsCodeRateRepository;
import com.icici.dma.repository.TdsCodeRateTempRepository;


@Service
@Transactional
public class TdsCodeRateServiceImpl
        implements TdsCodeRateService {
 
    @Autowired
    private TdsCodeRateRepository tdsCodeRateRepository;
 
    @Autowired
    private TdsCodeRateTempRepository tdsCodeRateTempRepository;
    
    @Override
    public List<TdsCodeRateDto> getTdsCodeRateMaker() {
     
        List<TdsCodeRateDto> dtoList =
                new ArrayList<>();
     
        tdsCodeRateRepository.findAll()
                .forEach(master ->
                        dtoList.add(
                                convertMasterToDto(master)));
     
        tdsCodeRateTempRepository
                .findAllByStatus(StatusConstant.PENDING)
                .forEach(temp ->
                        dtoList.add(
                                convertTempToDto(temp)));
     
        return dtoList;
    }
    
    @Override
    public String createTdsCodeRate(
    		TdsCodeRateDto dto,
            String user) {
     
        if (tdsCodeRateTempRepository
                .existsByIdAndStatus(
                        dto.getId(),
                        StatusConstant.PENDING)) {
     
            throw new RuntimeException(
                    "tds already waiting for approval");
        }
     
        if (tdsCodeRateRepository
                .existsById(
                        dto.getId())) {
     
            throw new RuntimeException(
                    "tds already approved");
        }
     
        TdsCodeRateTemp temp =
                new TdsCodeRateTemp();
     
        //temp.setId(dto.getId());
        temp.setWtaxType(dto.getWtaxType());
        temp.setWtx(dto.getWtx());
        temp.setTdsRate(dto.getTdsRate());
     
        temp.setStatus(StatusConstant.PENDING);
     
        temp.setActionType(
                ActionConstant.INSERT);
     
        temp.setCreatedBy(user);
     
        temp.setCreatedDate(new Date());
     
        temp.setActionUser(user);
     
        temp.setActionDate(new Date());
     
        tdsCodeRateTempRepository.save(temp);
     
        return "Vendor Master created successfully";
    }
    
    @Override
    public String updateTdsCodeRateByMaker(
    		TdsCodeRateDto dto,
            String user) {
     
    	TdsCodeRate master =
        		tdsCodeRateRepository
                .findById(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "tds not found"));
     
    	TdsCodeRateTemp temp =
                convertMasterToTemp(master);
    	
    	//temp.setId(dto.getId());
    	temp.setWtaxType(dto.getWtaxType());
    	temp.setWtx(dto.getWtx());
    	temp.setTdsRate(dto.getTdsRate());
     
        temp.setStatus(StatusConstant.PENDING);
     
        temp.setActionType(
                ActionConstant.UPDATE);
     
        temp.setModifiedBy(user);
     
        temp.setModifiedDate(new Date());
     
        temp.setActionUser(user);
     
        temp.setActionDate(new Date());
     
        tdsCodeRateTempRepository.save(temp);
     
        return "tds Master updated successfully";
    }
    
    
    @Override
    public List<TdsCodeRateDto>
    getAllTdsCodeRateChecker(
            String user) {
     
        return tdsCodeRateTempRepository
                .findAllByStatusAndCreatedByNot(
                        StatusConstant.PENDING,
                        user)
                .stream()
                .map(this::convertTempToDto)
                .collect(Collectors.toList());
    }
     
    @Override
    @Transactional
    public String updateTdsCodeRateByChecker(
            CheckerDecisionReq req,
            String user) {
     
        if (req.getPrimaryIds() == null
                || req.getPrimaryIds().isEmpty()) {
     
            throw new RuntimeException(
                    "No records selected");
        }
     
        if (StatusConstant.APPROVE
                .equalsIgnoreCase(
                        req.getDecision())) {
        	
        	for (String idStr : req.getPrimaryIds()) {
        		 
        	    Long id = Long.valueOf(idStr);
        	 
        	    TdsCodeRateTemp temp =
        	        tdsCodeRateTempRepository
        	            .findByIdAndStatus(
        	                id,
        	                StatusConstant.PENDING)
        	            .orElseThrow(() ->
        	                new RuntimeException("Record not found"));
     
				
            	TdsCodeRate master = new TdsCodeRate();
                
                BeanUtils.copyProperties(temp, master);
                 
                master.setStatus(StatusConstant.APPROVE);
                 
                master.setModifiedBy(user);
                master.setModifiedDate(new Date());
                 
                master.setApprovedBy(user);
                master.setApprovedDate(new Date());
                 
                tdsCodeRateRepository.save(master);
                 
                tdsCodeRateTempRepository.delete(temp);
            }
     
            return "Approved Successfully";
        }
     
        /*for (String id :
                req.getPrimaryIds()) {*/
        
        for (String idStr : req.getPrimaryIds()) {
   		 
    	    Long id = Long.valueOf(idStr);
     
        	TdsCodeRateTemp temp =
        			tdsCodeRateTempRepository
                    .findByIdAndStatus(
                            id,
                            StatusConstant.PENDING)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Record not found"));
     
            temp.setStatus(
                    StatusConstant.REJECTE);
     
            temp.setRemarks(
                    req.getRemark());
     
            temp.setActionUser(user);
     
            temp.setActionDate(
                    new Date());
     
            tdsCodeRateTempRepository
                    .save(temp);
        }
     
        return "Rejected Successfully";
    }
    
    @Override
    public List<TdsCodeRateDto>
    getTdsCodeRateByStatus(
            String status) {
     
        List<TdsCodeRateDto> dtoList =
                new ArrayList<>();
     
        if (StatusConstant.ALL
                .equalsIgnoreCase(status)) {
     
        	tdsCodeRateRepository
                    .findAll()
                    .forEach(master ->
                            dtoList.add(
                                    convertMasterToDto(master)));
     
        	tdsCodeRateTempRepository
                    .findAll()
                    .forEach(temp ->
                            dtoList.add(
                                    convertTempToDto(temp)));
     
            return dtoList;
        }
     
        if (StatusConstant.APPROVE
                .equalsIgnoreCase(status)) {
     
            return tdsCodeRateRepository
                    .findAllByStatus(
                            StatusConstant.APPROVE)
                    .stream()
                    .map(this::convertMasterToDto)
                    .collect(Collectors.toList());
        }
     
        return tdsCodeRateTempRepository
                .findAllByStatus(status)
                .stream()
                .map(this::convertTempToDto)
                .collect(Collectors.toList());
    }
    
    private TdsCodeRateDto convertMasterToDto(TdsCodeRate master) {
   	 
    	TdsCodeRateDto dto = new TdsCodeRateDto();
     
        dto.setId(master.getId());
        dto.setWtaxType(master.getWtaxType());
        dto.setWtx(master.getWtx());
        dto.setTdsRate(master.getTdsRate());
        dto.setStatus(master.getStatus());
        
     
        return dto;
    }
    
    private TdsCodeRateDto convertTempToDto(TdsCodeRateTemp temp) {
        
    	TdsCodeRateDto dto = new TdsCodeRateDto();
        
        dto.setId(temp.getId());
        dto.setWtaxType(temp.getWtaxType());
        dto.setWtx(temp.getWtx());
        dto.setTdsRate(temp.getTdsRate());
        dto.setStatus(temp.getStatus());
     
     
        return dto;
    }
    
    private TdsCodeRateTemp
    convertMasterToTemp(
    		TdsCodeRate master) {
     
    	TdsCodeRateTemp temp =
                new TdsCodeRateTemp();
    	
    	
    	temp.setId(master.getId());
    	temp.setWtaxType(master.getWtaxType());
    	temp.setWtx(master.getWtx());
    	temp.setTdsRate(master.getTdsRate());
    	temp.setStatus(master.getStatus());
     
        return temp;
    }
}