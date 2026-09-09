package com.icici.dma.slabRepository.ccrValuation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.slabEntity.ccrValuation.ValuationSlabMaster;

@Repository
public interface ValuationSlabMasterRepository extends JpaRepository<ValuationSlabMaster, Long> {

	List<ValuationSlabMaster> findByStatusOrderByCreatedDateDesc(
			String status);

	List<ValuationSlabMaster> findByStatusAndCreatedByNotOrderByCreatedDateDesc(
			String status, 
			String createdBy);

    @Query(
        "SELECT v " +
        "FROM ValuationSlabMaster v " +
        "WHERE v.product = :product " +
        "AND v.subProduct = :subProduct " +
        "AND v.fromDate = :fromDate " +
        "AND v.toDate = :toDate " +
        "AND v.status = 'PENDING' " +
        "ORDER BY v.id DESC"
    )
    List<ValuationSlabMaster> findPending(
            @Param("product") String product,
            @Param("subProduct") String subProduct,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query(
        "SELECT v " +
        "FROM ValuationSlabMaster v " +
        "WHERE v.product = :product " +
        "AND v.subProduct = :subProduct " +
        "AND v.fromDate = :fromDate " +
        "AND v.toDate = :toDate " +
        "AND v.status = 'APPROVED' " +
        "ORDER BY v.versionNo DESC, v.id DESC"
    )
    List<ValuationSlabMaster> findLatestApproved(
            @Param("product") String product,
            @Param("subProduct") String subProduct,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );
    
    @Query(
            "SELECT v " +
            "FROM ValuationSlabMaster v " +
            "WHERE v.product = :product " +
            "AND v.subProduct = :subProduct " +
            "AND v.fromDate = :fromDate " +
            "AND v.toDate = :toDate " +
            "AND v.status IN ('APPROVED', 'INACTIVE') " +
            "AND v.versionNo < :versionNo " +
            "ORDER BY v.versionNo DESC, v.id DESC"
        )
    List<ValuationSlabMaster> findPreviousApproved(
    		@Param("product") String product,
            @Param("subProduct") String subProduct,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("versionNo") Integer versionNo
    );
}