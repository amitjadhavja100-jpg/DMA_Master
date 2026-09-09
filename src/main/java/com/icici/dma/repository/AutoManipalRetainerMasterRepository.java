package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.AutoManipalRetainerMaster;

@Repository
public interface AutoManipalRetainerMasterRepository extends JpaRepository<AutoManipalRetainerMaster, Long> {
	// Cleaned single-line JPQL query
	@Query("SELECT a FROM AutoManipalRetainerMaster a WHERE a.status = 'P' ORDER BY a.sequence DESC")
	List<AutoManipalRetainerMaster> findPendingSummary();

	@Query("SELECT MAX(a.sequence) FROM AutoManipalRetainerMaster a")
	Long findMaxSequnce();

	@Query("SELECT a FROM AutoManipalRetainerMaster a WHERE a.sequence = :sequence")
	List<AutoManipalRetainerMaster> findBySequence(@Param("sequence") Long sequence);

}
