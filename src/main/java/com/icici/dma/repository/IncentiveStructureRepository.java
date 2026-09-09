package com.icici.dma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.IncentiveStructure;

@Repository
public interface IncentiveStructureRepository extends JpaRepository<IncentiveStructure, Long> {
	
	   @Query("SELECT MAX(i.seqNo) FROM IncentiveStructure i")
	    Integer findMaxSequence();

}
