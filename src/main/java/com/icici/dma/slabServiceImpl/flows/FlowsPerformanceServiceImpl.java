package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsPerformanceDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;
import com.icici.dma.slabRepository.flows.FlowsBucketRepository;
import com.icici.dma.slabRepository.flows.FlowsPerformanceRepository;
import com.icici.dma.slabService.flows.FlowsPerformanceService;

@Service
@Transactional
public class FlowsPerformanceServiceImpl implements FlowsPerformanceService {

	@Autowired
	private FlowsPerformanceRepository repo;
	
	@Autowired
	private FlowsBucketRepository bucketRepo;

	@Override
	public String save(FlowsPerformanceDTO dto, String user) {

		if (dto.getBucketId() == null) {
	        throw new RuntimeException("Bucket is required.");
	    }
		
		boolean rangeBased = isRangeBasedBucket(dto.getBucketId());
		
		if (dto.getOrderId() == null) {
	        throw new RuntimeException("Order Id is required.");
	    }


		// Display text required for both Bucket 1-6
		if (dto.getDisplayText() == null || dto.getDisplayText().trim().isEmpty()) {

			throw new RuntimeException("Display Text is required.");
		}
		
		if (repo.findByBucketIdAndOrderId(
	            dto.getBucketId(),
	            dto.getOrderId()
	    ).isPresent()) {

	        throw new RuntimeException(
	                "Order Id already exists."
	        );
	    }
		
		// Bucket 1-4 :From Value + To Value are required
	    if (rangeBased) {
	        validateRange(
	                dto.getFromValue(),
	                dto.getToValue()
	        );

	        Long overlap = repo.checkOverlap(
	                dto.getBucketId(),
	                dto.getFromValue(),
	                dto.getToValue()
	        );
	        
			if (overlap > 0) {
				throw new RuntimeException("Range overlapping.");
			}

	    }

		FlowsPerformanceMaster entity = new FlowsPerformanceMaster();
		entity.setBucketId(dto.getBucketId());
		entity.setOrderId(dto.getOrderId());
		entity.setDisplayText(dto.getDisplayText().trim());

		// Bucket 1-4:Save From / To
	    if (rangeBased) {
	        entity.setFromValue(dto.getFromValue());
	        entity.setToValue(dto.getToValue());
	    }
	    // Bucket 5-6:Do NOT save From / To
	    else {
	        entity.setFromValue(null);
	        entity.setToValue(null);
	    }
	    
	    entity.setStatus("APPROVED");
	    entity.setCreatedBy(user);
	    entity.setCreatedDate(new Date());
	    repo.save(entity);

	    return "Saved Successfully";
	}
	

	// UPDATE
	@Override
	public String update(FlowsPerformanceDTO dto, String user) {

		boolean rangeBased = isRangeBasedBucket(dto.getBucketId());
		
		if (dto.getBucketId() == null) {
	        throw new RuntimeException("Bucket is required.");
	    }

		// Display text required
		if (dto.getDisplayText() == null || dto.getDisplayText().trim().isEmpty()) {

			throw new RuntimeException("Display Text is required.");
		}
		
		if (dto.getOrderId() == null) {
	        throw new RuntimeException("Order Id is required.");
	    }

	    // Bucket 1-4: Validate range and overlap
	    if (rangeBased) {
	        validateRange(
	                dto.getFromValue(),
	                dto.getToValue()
	        );
	    }

	    FlowsPerformanceMaster entity =
	            repo.findById(dto.getId())
	                    .orElseThrow(() ->
	                            new RuntimeException(
	                                    "Performance Not Found"
	                            ));

	    // Duplicate Order ID
	    if (!entity.getOrderId().equals(dto.getOrderId())
	            || !entity.getBucketId().equals(dto.getBucketId())) {

	        if (repo.findByBucketIdAndOrderId(
	                dto.getBucketId(),
	                dto.getOrderId()
	        ).isPresent()) {

	            throw new RuntimeException(
	                    "Order Id already exists."
	            );
	        }
	    }
	    
	    // Range overlap only Bucket 1-4
	    if (rangeBased) {

	    	Long overlap =
	                repo.checkOverlapForUpdate(
	                        dto.getBucketId(),
	                        dto.getId(),
	                        dto.getFromValue(),
	                        dto.getToValue()
	                );

	        if (overlap > 0) {
				throw new RuntimeException("Range overllaping.");
	        }
	    }
	    entity.setBucketId(dto.getBucketId());
	    entity.setOrderId(dto.getOrderId());
	    entity.setDisplayText(
	            dto.getDisplayText().trim()
	    );

	    // Bucket 1-4
	    if (rangeBased) {

	        entity.setFromValue(dto.getFromValue());
	        entity.setToValue(dto.getToValue());

	    }
	    // Bucket 5-6
	    else {

	        entity.setFromValue(null);
	        entity.setToValue(null);
	    }

	    entity.setModifiedBy(user);
	    entity.setModifiedDate(new Date());

	    repo.save(entity);

	    return "Updated Successfully";
	}

	@Override
	public List<FlowsPerformanceMaster> getAll() {
		return repo.findAll();
	}

	@Override
	public List<FlowsPerformanceMaster> getApproved(Long bucketId) {

	    return repo.findByBucketIdAndStatusOrderByOrderId(
	            bucketId,
	            "APPROVED");
	}
	
	private boolean isRangeBasedBucket(Long bucketId) {

	    FlowsBucketMaster bucket =
	            bucketRepo.findById(bucketId)
	                    .orElseThrow(() ->
	                            new RuntimeException("Bucket Not Found"));

	    if (bucket.getRangeRequired() == null) {
	        throw new RuntimeException(
	                "Range Validation is not configured for bucket."
	        );
	    }

	    return "Y".equalsIgnoreCase(
	            bucket.getRangeRequired().trim()
	    );
	}

	// RANGE VALIDATION
	private void validateRange(java.math.BigDecimal fromValue, java.math.BigDecimal toValue) {

		if (fromValue == null || toValue == null) {
			throw new RuntimeException("From Value and To Value are required.");
		}

		if (fromValue.compareTo(toValue) >= 0) {
			throw new RuntimeException("From Value must be less than To Value.");
		}
	}
}