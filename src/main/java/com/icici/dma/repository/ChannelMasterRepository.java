package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ChannelMaster;

@Repository
public interface ChannelMasterRepository extends JpaRepository<ChannelMaster, Integer> {
//public interface ChannelMasterRepository{

//	@Query(value = "SELECT c FROM ChannelMaster c WHERE c.status = :status")
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_CHANNEL_MST t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<ChannelMaster> findAllByStatus(@Param("status") String status);

	boolean existsByApsCode(Integer apsCode);
	
	boolean existsBy();
}
