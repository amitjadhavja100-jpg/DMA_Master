package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.RecoveryCityMasterError;

@Repository
public interface RecoveryCityMasterErrorRepository
        extends JpaRepository<RecoveryCityMasterError, Integer> {
	
    List<RecoveryCityMasterError> findByUploadIdOrderByRowNumber(
            String uploadId);

}