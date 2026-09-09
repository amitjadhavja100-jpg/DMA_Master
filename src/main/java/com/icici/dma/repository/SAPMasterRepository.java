package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.SAPMaster;

@Repository
public interface SAPMasterRepository extends JpaRepository<SAPMaster, Long> {

//    boolean existsBySapVendorCode(String sapVendorCode);
    
    boolean existsByUnitCode(String unitCode);
    Optional<SAPMaster> findByUnitCode(String unitCode);

    @Query(
        value =
            "SELECT t.* " +
            "FROM TM_CCR_SAP_MST t " +
            "WHERE t.status = ?1 " +
            "ORDER BY GREATEST( " +
            "NVL(t.modified_date, DATE '1900-01-01'), " +
            "NVL(t.created_date, DATE '1900-01-01') " +
            ") DESC",
        nativeQuery = true
    )
    List<SAPMaster> findAllByStatus(String status);
}