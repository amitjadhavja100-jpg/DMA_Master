package com.icici.dma.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.RetainerPayloadDto;
import com.icici.dma.repository.AutoManipalRetainerMasterRepository;
import com.icici.dma.slabEntity.AutoManipalRetainerMaster;
 
@Service
public class RetainerMasterService {
 
    @Autowired
    private AutoManipalRetainerMasterRepository retainerRepository;
 
    @Transactional
    public List<AutoManipalRetainerMaster> saveRetainerMaster(RetainerPayloadDto payload) {
        List<AutoManipalRetainerMaster> entitiesToSave = new ArrayList<>();
 
        String cycleFromDate = payload.getCycleFromDate();
        String cycleToDate = payload.getCycleToDate();
        // Get current user (fallback to "SYSTEM")
        String currentUser = (payload.getUser() != null) ? payload.getUser() : "SYSTEM";
 
        // Handle sequence generation (Long type safely handling null max)
        Long latestSequence = retainerRepository.findMaxSequnce();
        Long nextSeq = (latestSequence == null) ? 1L : latestSequence + 1L;
 
        if (payload.getTableData() != null) {
            for (RetainerPayloadDto.SlabRowDto row : payload.getTableData()) {
 
                // 1. Map NEW_CAR / INBOUND record
                if (row.getNewCarCasesFrom() != null) {
                    AutoManipalRetainerMaster newCarRecord = new AutoManipalRetainerMaster();
                    newCarRecord.setSlab(row.getSLAB());
                    newCarRecord.setCaseType("INBOUND_OUTBOUND");
                    newCarRecord.setCasesFrom(row.getNewCarCasesFrom());
                    newCarRecord.setCasesTo(row.getNewCarCasesTo());
                    newCarRecord.setCurrentFixedSalaryPerc(row.getCurrentFixedSalaryPerc());
                    newCarRecord.setPerCaseAmount(row.getPerCaseAmount());
                    newCarRecord.setRemarks(row.getRemark());
                    newCarRecord.setCreatedBy(currentUser);
                    newCarRecord.setCreatedDate(LocalDateTime.now());
                    newCarRecord.setSequence(nextSeq);
                    newCarRecord.setCurrentIncentivePerc(row.getCurrentIncentivePerc());
                    newCarRecord.setStatus("P");
                    
                    newCarRecord.setCycleFromDate(cycleFromDate);
                    newCarRecord.setCycleToDate(cycleToDate);
                    
                    entitiesToSave.add(newCarRecord);
                }
 
                // 2. Map USED_CAR record
                if (row.getUsedCarCasesFrom() != null) {
                    AutoManipalRetainerMaster usedCarRecord = new AutoManipalRetainerMaster();
                    usedCarRecord.setSlab(row.getSLAB());
                    usedCarRecord.setCaseType("USED");
                    usedCarRecord.setCasesFrom(row.getUsedCarCasesFrom());
                    usedCarRecord.setCasesTo(row.getUsedCarCasesTo());
                    usedCarRecord.setCurrentFixedSalaryPerc(row.getCurrentFixedSalaryPerc());
                    usedCarRecord.setPerCaseAmount(row.getPerCaseAmount());
                    usedCarRecord.setRemarks(row.getRemark());
                    usedCarRecord.setCreatedBy(currentUser);
                    usedCarRecord.setCreatedDate(LocalDateTime.now());
                    usedCarRecord.setSequence(nextSeq);
                    usedCarRecord.setStatus("P");
                    usedCarRecord.setCurrentIncentivePerc(row.getCurrentIncentivePerc());

 
                    usedCarRecord.setCycleFromDate(cycleFromDate);
                    usedCarRecord.setCycleToDate(cycleToDate);
                    entitiesToSave.add(usedCarRecord);
                }
            }
        }
 
        // Batch save all constructed entities & return list
        return retainerRepository.saveAll(entitiesToSave);
    }
}