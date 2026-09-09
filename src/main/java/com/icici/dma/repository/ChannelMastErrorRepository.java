package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.ChannelMasterError;
@Repository
public interface ChannelMastErrorRepository extends JpaRepository<ChannelMasterError, Integer> {

	List<ChannelMasterError> findByUploadIdOrderByRowNumber(String uploadId);
	
	@Query(value = " SELECT upload_id FROM TM_VHL_CHANNEL_MST_ERROR WHERE created_by = :createdBy ORDER BY created_date DESC FETCH FIRST 1 ROW ONLY", nativeQuery = true)
	String findTopUploadIdByCreatedByOrderByCreatedDateDesc(String createdBy);
}
