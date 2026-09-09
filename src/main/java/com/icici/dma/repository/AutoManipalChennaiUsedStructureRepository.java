package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.AutoManipalChennaiUsedStructure;
@Repository
public interface AutoManipalChennaiUsedStructureRepository extends JpaRepository<AutoManipalChennaiUsedStructure, Long> {

	@Query("SELECT a FROM AutoManipalChennaiUsedStructure a WHERE a.sequence = :sequence")
	List<AutoManipalChennaiUsedStructure> findBySequence(@Param("sequence") Integer sequence);
}
