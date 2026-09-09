package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.dto.IPKPendingSummary;
import com.icici.dma.slabEntity.AutoProcessIncentiveStructureMasterManipal;
@Repository
public interface AutoProcessIncentiveStructureMasterManipalRepository extends JpaRepository<AutoProcessIncentiveStructureMasterManipal, Long> {
	@Query("select max(a.sequence) from AutoProcessIncentiveStructureMasterManipal a ")
	Long findMaxSequence();
	
	@Query("SELECT new com.icici.dma.dto.IPKPendingSummary(a.sequence, a.state, MIN(a.cycleFrom), MIN(a.cycleTo), MIN(a.createdDate), MIN(a.status)) "
			+ "FROM AutoProcessIncentiveStructureMasterManipal a WHERE a.status = 'N' "
			+ "GROUP BY a.sequence, a.state ORDER BY a.sequence DESC")
	List<IPKPendingSummary> findPendingSummary();
	
	
	@Query("SELECT a FROM AutoProcessIncentiveStructureMasterManipal a WHERE a.sequence = :sequence")
	List<AutoProcessIncentiveStructureMasterManipal> findBySequence(@Param("sequence") Long sequence);
}
