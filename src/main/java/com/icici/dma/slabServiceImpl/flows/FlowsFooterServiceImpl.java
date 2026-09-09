package com.icici.dma.slabServiceImpl.flows;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.dto.flows.FlowsFooterDTO;
import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsFooterMaster;
import com.icici.dma.slabEntity.flows.FooterRule;
import com.icici.dma.slabRepository.flows.FlowsBucketRepository;
import com.icici.dma.slabRepository.flows.FlowsFooterRepository;
import com.icici.dma.slabService.flows.FlowsFooterRuleService;
import com.icici.dma.slabService.flows.FlowsFooterService;

@Service
@Transactional
public class FlowsFooterServiceImpl
        implements FlowsFooterService {

    @Autowired
    private FlowsFooterRepository repo;

    @Autowired
    private FlowsBucketRepository bucketRepo;
    
    @Autowired
    private FlowsFooterRuleService footerRuleService;
    
    @Override
	public String save(FlowsFooterDTO dto, String user) {

        if (dto.getBucketId() == null) {
            throw new RuntimeException("Bucket is required");
        }
        
		if (dto.getOrderId() == null) {
			throw new RuntimeException("Order Id is required");
		}
        
        if (dto.getFooterType() == null || dto.getFooterType().trim().isEmpty()) {

			throw new RuntimeException("Footer Type is required.");
		}

		if (dto.getFooterText() == null || dto.getFooterText().trim().isEmpty()) {

			throw new RuntimeException("Footer Text is required.");
		}

        FlowsBucketMaster bucket =
                bucketRepo.findById(dto.getBucketId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Bucket Not Found"));

		Integer actualOrderId = bucket.getOrderId();

        if (actualOrderId == null) {
			throw new RuntimeException("Bucket Order Id is not configured.");
		}

        if (!actualOrderId.equals(dto.getOrderId())) {
			throw new RuntimeException("Order Id does not match Bucket Master.");
		}
        
        boolean exists =
                repo.existsByBucketIdAndOrderIdAndStatus(
                        dto.getBucketId(),
                        actualOrderId,
                        "APPROVED");

        if (exists) {
            throw new RuntimeException(
                    "Footer already exists for this Bucket and Order Id.");
        }
		
		FooterRule footerRule = footerRuleService.getFooterRule(bucket);

		if (!footerRule.isFooterRequired()) {
			throw new RuntimeException("Footer is not required for this bucket.");
		}

		FlowsFooterMaster entity = new FlowsFooterMaster();

        entity.setBucketId(dto.getBucketId());
        entity.setOrderId(actualOrderId);

		entity.setFooterType(dto.getFooterType().trim());
		entity.setFooterText(dto.getFooterText().trim());

        entity.setStatus("APPROVED");
        entity.setCreatedBy(user);
        entity.setCreatedDate(new Date());

        repo.save(entity);

        return "Footer Saved Successfully";
    }
    
    @Override
	public String update(FlowsFooterDTO dto, String user) {

		if (dto.getId() == null) {
			throw new RuntimeException("Footer Id is required.");
		}

        FlowsFooterMaster entity =
                repo.findById(dto.getId())
                    .orElseThrow(() ->
                        new RuntimeException(
                                "Footer Not Found"));

        FlowsBucketMaster bucket =
                bucketRepo.findById(dto.getBucketId())
                    .orElseThrow(() ->
                        new RuntimeException(
                                "Bucket Not Found"));
        
		FooterRule footerRule = footerRuleService.getFooterRule(bucket);

		if (!footerRule.isFooterRequired()) {

			throw new RuntimeException("Footer is not required for this bucket.");
		}

		if (!bucket.getOrderId().equals(dto.getOrderId())) {

			throw new RuntimeException("Order Id does not match Bucket Master.");
		}

        if (dto.getFooterText() == null
                || dto.getFooterText().trim().isEmpty()) {

			throw new RuntimeException("Footer Text is required.");
		}

        entity.setBucketId(dto.getBucketId());
        entity.setOrderId(dto.getOrderId());

		entity.setFooterType(dto.getFooterType().trim());
		entity.setFooterText(dto.getFooterText().trim());

        entity.setModifiedBy(user);
        entity.setModifiedDate(new Date());

        repo.save(entity);

        return "Footer Updated Successfully";
    }
    
    @Override
    public List<FlowsFooterMaster> getAll() {

        return repo.findAll();
    }
    
	@Override
	public List<FlowsFooterMaster> getApproved(Long bucketId) {

        return repo.findByBucketIdAndStatusOrderByOrderId(
                        bucketId,
                        "APPROVED");
    }
	
    @Override
    public List<FlowsFooterMaster> getApprovedByBucket(
            Long bucketId,
            Integer orderId) {

        return repo
                .findByBucketIdAndOrderIdAndStatusOrderById(
                        bucketId,
                        orderId,
                        "APPROVED");
    }
    
}
