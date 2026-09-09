package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.SAPMasterError;

@Repository
public interface SAPMasterErrorRepository extends JpaRepository<SAPMasterError, Integer> {

    List<SAPMasterError> findByUploadIdOrderByRowNumber(String uploadId);

    
    @Query(
        value =
            "SELECT upload_id " +
            "FROM TM_CCR_SAP_MST_ERROR " +
            "WHERE created_by = ?1 " +
            "ORDER BY created_date DESC " +
            "FETCH FIRST 1 ROW ONLY",
        nativeQuery = true
    )
    String findTopUploadIdByCreatedByOrderByCreatedDateDesc(String createdBy);
}