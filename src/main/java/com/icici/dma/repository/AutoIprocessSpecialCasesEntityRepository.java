package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.dto.MPKCommonPendingSummary;
import com.icici.dma.slabEntity.AutoIprocessSpecialCasesEntity;

@Repository
public interface AutoIprocessSpecialCasesEntityRepository extends JpaRepository<AutoIprocessSpecialCasesEntity, Long> {

	@Query("SELECT new com.icici.dma.dto.MPKCommonPendingSummary(a.sequence, a.state,"
			+ " MIN(a.cycleFromDate), MIN(a.cycleToDate), MIN(a.createdDate), MIN(a.status)) "
			+ "FROM AutoIprocessSpecialCasesEntity a WHERE a.status = 'N' "
			+ "GROUP BY a.sequence, a.state ORDER BY a.sequence DESC")
	List<MPKCommonPendingSummary> findPendingSummaryOther();
	@Query("SELECT a FROM AutoIprocessSpecialCasesEntity a WHERE a.sequence = :sequence")
	List<AutoIprocessSpecialCasesEntity> findBySequence(Integer sequence);

}
