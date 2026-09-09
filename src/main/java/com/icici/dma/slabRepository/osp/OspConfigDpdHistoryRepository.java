package com.icici.dma.slabRepository.osp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigDpdHistory;

@Repository
public interface OspConfigDpdHistoryRepository
        extends JpaRepository<OspConfigDpdHistory, Long> {
	
	OspConfigDpdHistory findTopByDpdIdOrderByApprovedDateDesc(Long dpdId);


}