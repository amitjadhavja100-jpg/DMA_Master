package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ModelMasterError;

@Repository
public interface ModelMasterErrorRepository extends JpaRepository<ModelMasterError, Integer> {
	
	List<ModelMasterError> findByUploadIdOrderByRowNumber(String uploadId);
	
	@Query(value = " SELECT upload_id FROM TM_VHL_MODEL_MST_ERROR WHERE created_by = :createdBy ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	String findTopUploadIdByCreatedByOrderByCreateDateDesc(String createdBy);

}
