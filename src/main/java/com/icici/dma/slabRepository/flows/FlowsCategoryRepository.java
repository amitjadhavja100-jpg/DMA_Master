package com.icici.dma.slabRepository.flows;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.flows.FlowsCategoryMaster;

@Repository
public interface FlowsCategoryRepository
        extends JpaRepository<FlowsCategoryMaster, Long> {

    List<FlowsCategoryMaster> findByStatusOrderByCategoryName(String status);

}
