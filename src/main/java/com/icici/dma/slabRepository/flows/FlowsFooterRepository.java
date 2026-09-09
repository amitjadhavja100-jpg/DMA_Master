package com.icici.dma.slabRepository.flows;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsFooterMaster;

@Repository
public interface FlowsFooterRepository extends JpaRepository<FlowsFooterMaster, Long> {

	List<FlowsFooterMaster>
    findByBucketIdAndOrderIdAndStatusOrderById(
            Long bucketId,
            Integer orderId,
            String status);

    List<FlowsFooterMaster>
    findByBucketIdAndStatusOrderByOrderId(
            Long bucketId,
            String status);

    List<FlowsFooterMaster>
    findByBucketIdAndOrderIdOrderById(
            Long bucketId,
            Integer orderId);

    void deleteByBucketIdAndOrderId(
            Long bucketId,
            Integer orderId);
    
    boolean existsByBucketIdAndOrderIdAndStatus(
            Long bucketId,
            Integer orderId,
            String status);
    
}