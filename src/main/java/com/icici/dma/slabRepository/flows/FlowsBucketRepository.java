package com.icici.dma.slabRepository.flows;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsBucketMaster;

@Repository
public interface FlowsBucketRepository extends JpaRepository<FlowsBucketMaster, Long> {
    
	
	// CATEGORY A / CITY BASED
	
	List<FlowsBucketMaster> findByStatusOrderById(String status);

	Optional<FlowsBucketMaster> findByBucketName(String bucketName);
	
	Optional<FlowsBucketMaster>
    findByCityIdAndBucketTypeIdAndOrderId(
            Long cityId,
            Long bucketTypeId,
            Integer orderId);
	
	List<FlowsBucketMaster> findByCityIdAndStatusOrderById(Long cityId, String status);
		
    Optional<FlowsBucketMaster> findByCityIdAndBucketTypeIdAndOrderIdAndIdNot(
            Long cityId,
            Long bucketTypeId,
            Integer orderId,
            Long id);

    Optional<FlowsBucketMaster> findByCityIdAndBucketTypeIdAndBucketNameAndIdNot(
            Long cityId,
            Long bucketTypeId,
            String bucketName,
            Long id);
    
	Optional<FlowsBucketMaster> findByCityIdAndBucketTypeIdAndBucketName(
	        Long cityId,
	        Long bucketTypeId,
	        String bucketName);
	
	
	// CFP / CATEGORY BASED
    // CITY IS NULL
	
	Optional<FlowsBucketMaster>
    findByCategoryIdAndBucketTypeIdAndBucketName(
            Long categoryId,
            Long bucketTypeId,
            String bucketName);

    Optional<FlowsBucketMaster>
    findByCategoryIdAndBucketTypeIdAndOrderId(
            Long categoryId,
            Long bucketTypeId,
            Integer orderId);

    Optional<FlowsBucketMaster>
    findByCategoryIdAndBucketTypeIdAndBucketNameAndIdNot(
            Long categoryId,
            Long bucketTypeId,
            String bucketName,
            Long id);

    Optional<FlowsBucketMaster>
    findByCategoryIdAndBucketTypeIdAndOrderIdAndIdNot(
            Long categoryId,
            Long bucketTypeId,
            Integer orderId,
            Long id);

    List<FlowsBucketMaster>
    findByCategoryIdAndCityIdIsNullAndStatusOrderById(
            Long categoryId,
            String status);


}
