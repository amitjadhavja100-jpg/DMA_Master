package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.icici.dma.dto.IPKPendingSummary;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterDummy;

public interface AutoProcessIncentiveStructureMasterDummyRepository
		extends JpaRepository<AutoProcessIncentiveStructureMasterDummy, Long> {

	@Query("select max(a.sequence) from AutoProcessIncentiveStructureMasterDummy a ")
	Long findMaxSequence();
	
	@Query("SELECT new com.icici.dma.dto.IPKPendingSummary(a.sequence, a.state, MIN(a.cycleFrom), MIN(a.cycleTo), MIN(a.createdDate), MIN(a.status)) "
			+ "FROM AutoProcessIncentiveStructureMasterDummy a WHERE a.status = 'N' "
			+ "GROUP BY a.sequence, a.state ORDER BY a.sequence DESC")
	List<IPKPendingSummary> findPendingSummary();
	
	
	@Query("SELECT a FROM AutoProcessIncentiveStructureMasterDummy a WHERE a.sequence = :sequence")
	List<AutoProcessIncentiveStructureMasterDummy> findBySequence(@Param("sequence") Long sequence);

}
