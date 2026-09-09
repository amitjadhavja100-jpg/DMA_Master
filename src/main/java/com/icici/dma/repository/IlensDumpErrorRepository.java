package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.icici.dma.model.IlensDumpError;

public interface IlensDumpErrorRepository extends JpaRepository<IlensDumpError, String> {
	
	List<IlensDumpError> findByUploadIdOrderByRowNumber(String uploadId); 
	
	@Query(value = " SELECT upload_id FROM TM_VHL_ILENS_DUMP_ERROR WHERE created_by = :createdBy ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	String findTopUploadIdByCreatedByOrderByCreatedDateDesc(String createdBy); 
		
}
