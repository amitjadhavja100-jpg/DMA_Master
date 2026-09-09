package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.AutoProcessBrokerMaster;
import com.icici.dma.slabEntity.AutoProcessChennaiUsedMaster;
@Repository
public interface AutoProcessChennaiUsedMasterRepository extends JpaRepository<AutoProcessChennaiUsedMaster, Long>{
	@Query("SELECT MAX(a.sequence) FROM AutoProcessChennaiUsedMaster a")
	Integer findMaxSequence();
	
	@Query("SELECT a FROM AutoProcessChennaiUsedMaster a WHERE a.sequence = :sequence")
	List<AutoProcessChennaiUsedMaster> findBySequence(@Param("sequence") Integer sequence);
}
