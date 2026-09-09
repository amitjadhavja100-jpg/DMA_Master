package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.FlowCityMasterError;

@Repository
public interface FlowCityMasterErrorRepository extends JpaRepository<FlowCityMasterError, Long> {

    /**
     * Get all error records for a particular upload
     */
    List<FlowCityMasterError> findByUploadId(String uploadId);

    /**
     * Delete error records after successful download
     */
    void deleteByUploadId(String uploadId);

    /**
     * Get all error records uploaded by maker
     */
    List<FlowCityMasterError> findByCreatedBy(String createdBy);

    /**
     * Get all error records for maker and upload id
     */
    List<FlowCityMasterError> findByCreatedByAndUploadId(String createdBy, String uploadId);

}