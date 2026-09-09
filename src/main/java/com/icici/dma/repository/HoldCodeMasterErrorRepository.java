package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.HoldCodeMasterError;

@Repository
public interface HoldCodeMasterErrorRepository
        extends JpaRepository<HoldCodeMasterError, Integer> {

    List<HoldCodeMasterError> findByUploadIdOrderByRowNumber(
            String uploadId);
}