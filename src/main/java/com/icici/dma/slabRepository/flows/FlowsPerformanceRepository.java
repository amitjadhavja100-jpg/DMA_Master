package com.icici.dma.slabRepository.flows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsPerformanceMaster;

@Repository
public interface FlowsPerformanceRepository
        extends JpaRepository<FlowsPerformanceMaster, Long> {
	
	List<FlowsPerformanceMaster> findByBucketIdAndStatusOrderByOrderId(
	        Long bucketId,
	        String status);
	
	//duplicate order:
	Optional<FlowsPerformanceMaster> findByBucketIdAndOrderId(Long bucketId, Integer orderId);
	
	//overlap
	@Query("SELECT COUNT(p) FROM FlowsPerformanceMaster p "
		     + "WHERE p.bucketId = :bucketId "
		     + "AND p.status = 'APPROVED' "
		     + "AND (:fromValue <= p.toValue "
		     + "AND :toValue >= p.fromValue)")
		Long checkOverlap(
		        @Param("bucketId") Long bucketId,
		        @Param("fromValue") BigDecimal fromValue,
		        @Param("toValue") BigDecimal toValue);
	
	//update
	@Query("SELECT COUNT(p) FROM FlowsPerformanceMaster p "
		     + "WHERE p.bucketId = :bucketId "
		     + "AND p.status = 'APPROVED' "
		     + "AND p.id <> :id "
		     + "AND (:fromValue <= p.toValue "
		     + "AND :toValue >= p.fromValue)")
		Long checkOverlapForUpdate(
		        @Param("bucketId") Long bucketId,
		        @Param("id") Long id,
		        @Param("fromValue") BigDecimal fromValue,
		        @Param("toValue") BigDecimal toValue);

}