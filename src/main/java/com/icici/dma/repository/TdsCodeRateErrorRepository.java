package com.icici.dma.repository;

import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.TdsCodeRateError;

@Repository
public interface TdsCodeRateErrorRepository
        extends JpaRepository<TdsCodeRateError, Long> {
 
    List<TdsCodeRateError>
    findByUploadIdOrderByRowNumber(
            String uploadId);
}
 