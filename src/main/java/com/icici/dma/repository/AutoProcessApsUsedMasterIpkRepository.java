package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.AutoProcessApsUsedMasterIpk;
@Repository
public interface AutoProcessApsUsedMasterIpkRepository extends JpaRepository<AutoProcessApsUsedMasterIpk, Long> {
	@Query("SELECT a FROM AutoProcessApsUsedMasterIpk a WHERE a.sequence = :sequence")
	List<AutoProcessApsUsedMasterIpk> findBySequence(@Param("sequence") Integer sequence);
}
