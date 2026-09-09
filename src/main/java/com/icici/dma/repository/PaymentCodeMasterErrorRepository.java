package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.PaymentCodeMasterError;

//snz

@Repository
public interface PaymentCodeMasterErrorRepository
        extends JpaRepository<PaymentCodeMasterError, Integer> {

    List<PaymentCodeMasterError> findByUploadIdOrderByRowNumber( String uploadId);
}