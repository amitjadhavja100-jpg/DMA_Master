package com.icici.dma.slabRepository.flows;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsPayoutDetail;

@Repository
public interface FlowsPayoutDetailRepository
        extends JpaRepository<FlowsPayoutDetail, Long> {

    List<FlowsPayoutDetail> findByMasterId(Long masterId);

}