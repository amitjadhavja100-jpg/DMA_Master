package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.dto.MPKCommonPendingSummary;
import com.icici.dma.slabEntity.AutoProcessCommonSpecialCases;
@Repository
public interface AutoProcessCommonSpecialCasesRepository extends JpaRepository<AutoProcessCommonSpecialCases, Long>{

	@Query("SELECT new com.icici.dma.dto.MPKCommonPendingSummary(a.sequence, a.state,"
			+ " MIN(a.cycleFromDate), MIN(a.cycleToDate), MIN(a.createdDate), MIN(a.status)) "
			+ "FROM AutoProcessCommonSpecialCases a WHERE a.status = 'N' "
			+ "GROUP BY a.sequence, a.state ORDER BY a.sequence DESC")
	List<MPKCommonPendingSummary> findPendingSummaryCommon();
	
	
	@Query("SELECT a FROM AutoProcessCommonSpecialCases a WHERE a.sequence = :sequence")
	List<AutoProcessCommonSpecialCases> findBySequence(@Param("sequence") Integer sequence);

}
