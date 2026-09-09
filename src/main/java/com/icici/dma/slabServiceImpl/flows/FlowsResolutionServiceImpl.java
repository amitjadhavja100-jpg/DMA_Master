package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsResolutionDTO;
import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;
import com.icici.dma.slabRepository.flows.FlowsResolutionRepository;
import com.icici.dma.slabService.flows.FlowsResolutionService;

@Service
@Transactional
public class FlowsResolutionServiceImpl implements FlowsResolutionService {
	
	@Autowired
	private FlowsResolutionRepository repo;
	
	@Override
	public String save(FlowsResolutionDTO dto, String user) {

        // Validate range
        validateRange(
                dto.getFromValue(),
                dto.getToValue());
        
        if (repo.findByBucketIdAndOrderId(
                dto.getBucketId(),
                dto.getOrderId()).isPresent()) {

            throw new RuntimeException("Order Id already exists.");
        }
        
        // Check overlapping range
        Long overlap =
                repo.checkOverlap(
                        dto.getBucketId(),
                        dto.getFromValue(),
                        dto.getToValue());

		if (overlap > 0) {
			throw new RuntimeException("Resolution range overlap.");
		}

		FlowsResolutionMaster entity = new FlowsResolutionMaster();

		entity.setBucketId(dto.getBucketId());
		entity.setOrderId(dto.getOrderId());
		entity.setDisplayText(dto.getDisplayText());
		entity.setFromValue(dto.getFromValue());
		entity.setToValue(dto.getToValue());
		entity.setStatus("APPROVED");
		entity.setCreatedBy(user);
		entity.setCreatedDate(new Date());

		// 5. Save
		repo.save(entity);

		return "Saved Successfully";
    }

	@Override
	public String update(FlowsResolutionDTO dto, String user) {

        //Validate range
		validateRange(dto.getFromValue(), dto.getToValue());

        //Find existing record
        FlowsResolutionMaster entity =
                repo.findById(dto.getId())
                    .orElseThrow(() ->
                        new RuntimeException(
                            "Resolution Not Found"));


		if (!entity.getOrderId().equals(dto.getOrderId())) {

			if (repo.findByBucketIdAndOrderId(
			        dto.getBucketId(),
			        dto.getOrderId()).isPresent()) {

			    throw new RuntimeException("Order Id already exists.");
			}
		}

        //Check overlapping range
        Long overlap =
                repo.checkOverlapForUpdate(
                        dto.getBucketId(),
                        dto.getId(),
                        dto.getFromValue(),
                        dto.getToValue());

		if (overlap > 0) {
			throw new RuntimeException("Resolution range overlap.");
		}

		entity.setBucketId(dto.getBucketId());
		entity.setOrderId(dto.getOrderId());
		entity.setDisplayText(dto.getDisplayText());
		entity.setFromValue(dto.getFromValue());
		entity.setToValue(dto.getToValue());
		entity.setModifiedBy(user);
		entity.setModifiedDate(new Date());

        repo.save(entity);

        return "Updated Successfully";
    }

    @Override
    public List<FlowsResolutionMaster> getAll() {
        return repo.findAll();
    }
    

    @Override
    public List<FlowsResolutionMaster> getApproved(Long bucketId) {

        return repo.findByBucketIdAndStatusOrderByOrderId(
                bucketId,
                "APPROVED");
    }

	
    private void validateRange(
            java.math.BigDecimal fromValue,
            java.math.BigDecimal toValue) {

		if (fromValue == null || toValue == null) {
			throw new RuntimeException("From Value and To Value are required.");
		}

		if (fromValue.compareTo(toValue) >= 0) {
			throw new RuntimeException("From Value must be less than To Value.");
		}
    }
	

}
