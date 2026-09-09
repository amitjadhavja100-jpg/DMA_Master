package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.BranchMasterError;
import com.icici.dma.model.RCA_CIBIL_Error;

@Repository
public interface RCASCibilErrorRepository extends JpaRepository<RCA_CIBIL_Error, Integer> {

	List<RCA_CIBIL_Error> findByUploadIdOrderByRowNumber(String uploadId);
	
	@Query(value = " SELECT upload_id FROM TM_VHL_RCAS_CIBIL_DUMP_ERROR WHERE created_by = :createdBy ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	String findTopUploadIdByCreatedByOrderByCreatedDateDesc(@Param("createdBy")String createdBy);
	 
}
