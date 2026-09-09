package com.icici.dma.slabServiceImpl.flows;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.icici.dma.slabEntity.flows.FlowsBucketMaster;
import com.icici.dma.slabEntity.flows.FlowsBucketTypeMaster;
import com.icici.dma.slabEntity.flows.FooterRule;
import com.icici.dma.slabRepository.flows.FlowsBucketTypeRepository;
import com.icici.dma.slabService.flows.FlowsFooterRuleService;

@Service
public class FlowsFooterRuleServiceImpl implements FlowsFooterRuleService {

	@Autowired
	private FlowsBucketTypeRepository bucketTypeRepo;

	@Override
	public FooterRule getFooterRule(FlowsBucketMaster bucket) {

		if (bucket == null) {
			throw new RuntimeException("Bucket is required.");
		}

		if (bucket.getBucketTypeId() == null) {
			throw new RuntimeException("Bucket Type is not configured.");
		}

        FlowsBucketTypeMaster bucketType =
                bucketTypeRepo.findById(bucket.getBucketTypeId())
						.orElseThrow(() -> new RuntimeException("Bucket Type Not Found"));

		String type = bucketType.getBucketTypeName().trim().toUpperCase();

		Integer orderId = bucket.getOrderId();

		if (orderId == null) {
			throw new RuntimeException("Bucket Order Id is not configured.");
		}

        // NORMAL BUCKETS(A to J)
        if ("NORMAL".equals(type)) {

            switch (orderId) {

                case 1:
                    return new FooterRule(true, true);

                case 2:
                    return new FooterRule(true, false);

                case 3:
                    return new FooterRule(false, false);

                case 4:
                    return new FooterRule(true, true);

                case 5:
                case 6:
                    return new FooterRule(false, false);

                default:
                    return new FooterRule(false, false);
            }
        }

        // FCPU
        if ("FCPU".equals(type)) {
            return new FooterRule(true, true);
        }

        // FR
        if ("FR".equals(type)) {
            return new FooterRule(false, false);
        }

        // DR
        if ("DR".equals(type)) {
            return new FooterRule(false, false);
        }

        return new FooterRule(false, false);
    }

}