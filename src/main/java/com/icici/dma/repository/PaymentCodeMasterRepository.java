package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.PaymentCodeMaster;


//snz
@Repository
public interface PaymentCodeMasterRepository
        extends JpaRepository<PaymentCodeMaster, String> {
	

    @Query(
        value =
            "SELECT t.* " +
            "FROM TM_CCR_PAYMENT_CODE_MST t " +
            "WHERE t.status = ?1 " +
            "ORDER BY GREATEST( " +
            "NVL(t.modified_date, DATE '1900-01-01'), " +
            "NVL(t.created_date, DATE '1900-01-01') " +
            ") DESC",
        nativeQuery = true
    )
    List<PaymentCodeMaster> findAllByStatus(String status);
}