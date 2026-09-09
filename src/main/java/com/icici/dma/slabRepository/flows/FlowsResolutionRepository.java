package com.icici.dma.slabRepository.flows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsResolutionMaster;

@Repository
public interface FlowsResolutionRepository extends JpaRepository<FlowsResolutionMaster, Long> {
		
	List<FlowsResolutionMaster> findByBucketIdAndStatusOrderByOrderId(Long bucketId, String status);
	
	Optional<FlowsResolutionMaster> findByBucketIdAndOrderId(Long bucketId, Integer orderId);
		
	@Query("SELECT COUNT(r) FROM FlowsResolutionMaster r "
			+ "WHERE r.bucketId = :bucketId "
			+ "AND r.status = 'APPROVED' "
			+ "AND (:fromValue <= r.toValue "
			+ "AND :toValue >= r.fromValue)")
	Long checkOverlap(
			@Param("bucketId") Long bucketId,
			@Param("fromValue") BigDecimal fromValue,
			@Param("toValue") BigDecimal toValue);
		
	@Query("SELECT COUNT(r) FROM FlowsResolutionMaster r "
			+ "WHERE r.bucketId = :bucketId "
			+ "AND r.status = 'APPROVED' "
			+ "AND r.id <> :id "
			+ "AND (:fromValue <= r.toValue "
			+ "AND :toValue >= r.fromValue)")
	Long checkOverlapForUpdate(
			@Param("bucketId") Long bucketId,
			@Param("id") Long id,			    
			@Param("fromValue") BigDecimal fromValue,
			@Param("toValue") BigDecimal toValue);
	
}