package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.GSTMaster;
import com.icici.dma.model.GSTMasterTemp;

@Repository
public interface GstMasterRepository extends JpaRepository<GSTMaster, Integer> {

//	@Query(value = "SELECT g FROM GstMaster g WHERE g.status = :status")
//	List<GSTMaster> findAllByStatus(@Param("status") String status);
	
	
	boolean existsByApsCode(Integer apsCode);
	
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_GST_MST t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<GSTMaster> findAllByStatus(@Param("status") String status);
}
