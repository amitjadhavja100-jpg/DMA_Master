package com.icici.dma.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.EducationLoanCheckerActionRequest;
import com.icici.dma.dto.EducationLoanSlabSubmitRequest;
import com.icici.dma.repository.EducationLoanSlabMasterRepository;
import com.icici.dma.repository.EducationLoanSlabTempRepository;
import com.icici.dma.slabEntity.EducationLoanSlabMaster;
import com.icici.dma.slabEntity.EducationLoanSlabTemp;

@Service
public class EducationLoanSlabService {
	@Autowired
	private EducationLoanSlabTempRepository tempRepository;

	@Autowired
	private EducationLoanSlabMasterRepository masterRepository;

	// Maker: Submit data
	@Transactional
	public void submitSlabs(EducationLoanSlabSubmitRequest request) {
		for (EducationLoanSlabSubmitRequest.SlabItem item : request.getSlabs()) {
			EducationLoanSlabTemp temp = new EducationLoanSlabTemp();
			temp.setProductCategory(request.getCategory());
			temp.setBucketCode(item.getBucket());
			temp.setMinAmount(item.getMin() != null ? BigDecimal.valueOf(item.getMin()) : null);
			temp.setMaxAmount(item.getMax() != null ? BigDecimal.valueOf(item.getMax()) : null);
			temp.setDescription(item.getDescription());
			temp.setCycleFromDate(request.getCycleFromDate());
			temp.setCycleToDate(request.getCycleToDate());

			// Convert percentage string "0.35%" to decimal 0.0035
			String cleanPct = item.getSlab().replace("%", "").trim();
//			BigDecimal percentage = new BigDecimal(cleanPct).divide(new BigDecimal("100"));
			BigDecimal percentage = new BigDecimal(cleanPct); // Removed division by 100
			temp.setPayoutPercentage(percentage);

			
			
			
			temp.setMakerId(request.getMakerId());
			temp.setMakerDate(LocalDateTime.now());
			temp.setRecordStatus("PENDING");

			tempRepository.save(temp);
		}
	}

	// Checker: Get Pending List
	public List<EducationLoanSlabTemp> getPendingApprovals() {
		return tempRepository.findByRecordStatusOrderByMakerDateDesc("PENDING");
	}

	// Checker: Approve / Reject

	@Transactional
	public void processCheckerAction(EducationLoanCheckerActionRequest request) {
	    // 1. Validate request and IDs
	    if (request == null || request.getTempIds() == null || request.getTempIds().isEmpty()) {
	        throw new IllegalArgumentException("Invalid Request: tempIds list cannot be null or empty.");
	    }
	 
	    // 2. Fetch records from repository
	    List<EducationLoanSlabTemp> tempRecords = tempRepository.findByTempIdIn(request.getTempIds());
	 
	    if (tempRecords == null || tempRecords.isEmpty()) {
	        throw new IllegalArgumentException("No records found for the given IDs.");
	    }
	 
	    // 3. Validate Maker != Checker constraint
	    if (request.getCheckerId() != null) {
	        boolean isSameUser = tempRecords.stream()
	            .anyMatch(rec -> rec.getMakerId() != null && rec.getMakerId().equalsIgnoreCase(request.getCheckerId()));
	 
	        if (isSameUser) {
	            throw new IllegalArgumentException("Action Forbidden: Maker and Checker cannot be the same user.");
	        }
	    }
	 
	    // 4. Process APPROVE or REJECT on the SAME table
	    String action = request.getAction();
	    
	    for (EducationLoanSlabTemp record : tempRecords) {
	        if ("APPROVE".equalsIgnoreCase(action)) {
	            record.setRecordStatus("APPROVED"); // or "ACTIVE" depending on your status flow
	        } else if ("REJECT".equalsIgnoreCase(action)) {
	            record.setRecordStatus("REJECTED");
	            record.setRejectionReason(request.getRejectionReason());
	        } else {
	            throw new IllegalArgumentException("Invalid Action: Must be 'APPROVE' or 'REJECT'.");
	        }
	 
	        // Set Checker metadata on the record
	        record.setCheckerId(request.getCheckerId());
	        record.setCheckerDate(java.time.LocalDateTime.now());
	 
	        // Save updated record back to the SAME table
	        tempRepository.save(record);
	    }
	}
}

