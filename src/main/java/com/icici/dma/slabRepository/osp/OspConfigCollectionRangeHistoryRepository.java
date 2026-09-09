package com.icici.dma.slabRepository.osp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.osp.OspConfigCollectionRangeHistory;

@Repository
public interface OspConfigCollectionRangeHistoryRepository
        extends JpaRepository<OspConfigCollectionRangeHistory, Long> {
	
	OspConfigCollectionRangeHistory findTopByRangeIdOrderByApprovedDateDesc(Long rangeId);


}