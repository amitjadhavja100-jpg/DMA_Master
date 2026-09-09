package com.icici.dma.slabRepository.flows;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsBucketTypeMaster;

@Repository
public interface FlowsBucketTypeRepository extends JpaRepository<FlowsBucketTypeMaster, Long> {

	Optional<FlowsBucketTypeMaster> findByBucketTypeName(String bucketTypeName);

	List<FlowsBucketTypeMaster> findByStatusOrderByBucketTypeName(String status);

	List<FlowsBucketTypeMaster> findByStatusOrderById(String status);
	
	Optional<FlowsBucketTypeMaster>
    findByBucketTypeNameAndIdNot(
            String bucketTypeName,
            Long id);
}