package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.icici.dma.slabEntity.AutoProcessApsUsedMaster;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterManipal;

public interface AutoProcessApsUsedMasterRepository extends JpaRepository<AutoProcessApsUsedMaster, Long> {
	
	
	@Query("SELECT MAX(a.sequence) FROM AutoProcessApsUsedMaster a")
	Integer findMaxSequence();

	@Query("SELECT a FROM AutoProcessApsUsedMaster a WHERE a.sequence = :sequence")
	List<AutoProcessApsUsedMaster> findBySequence(@Param("sequence") Integer sequence);

}
