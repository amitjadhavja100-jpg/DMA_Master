package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.SAPMasterTemp;

@Repository
public interface SAPMasterTempRepository extends JpaRepository<SAPMasterTemp, Long> {

    List<SAPMasterTemp> findByCreatedByAndStatus(String createdBy, String status);
    
    boolean existsByUnitCode(String unitCode);

    Optional<SAPMasterTemp> findByUnitCode(String unitCode);
    
	boolean existsByUnitCodeAndStatus(String unitCode, String status);

	Optional<SAPMasterTemp> findByUnitCodeAndStatus(String unitCode, String status);

	@Query(value =
	         "SELECT t.* " +
	         "FROM TM_CCR_SAP_MST_TEMP t " +
	         "WHERE t.STATUS = :status " +
	         "AND t.ACTION_TYPE IN ('I','U') " +
	         "AND t.ACTION_USER IS NOT NULL " +
	         "AND UPPER(TRIM(t.ACTION_USER)) <> UPPER(TRIM(:actionUser)) " +
	         "ORDER BY GREATEST( " +
	         "NVL(t.ACTION_DATE, DATE '1900-01-01'), " +
	         "NVL(t.MODIFIED_DATE, DATE '1900-01-01'), " +
	         "NVL(t.CREATED_DATE, DATE '1900-01-01') " +
	         ") DESC",
	         nativeQuery = true)
	     List<SAPMasterTemp> findAllByStatusAndCreatedByNot(
	             @Param("status") String status,
	             @Param("actionUser") String actionUser);
    
    @Query(
        value =
            "SELECT t.* " +
            "FROM TM_CCR_SAP_MST_TEMP t " +
            "WHERE t.status = :status " +
            "ORDER BY GREATEST( " +
            "NVL(t.action_date, DATE '1900-01-01'), " +
            "NVL(t.modified_date, DATE '1900-01-01'), " +
            "NVL(t.created_date, DATE '1900-01-01') " +
            ") DESC",
        nativeQuery = true )
    List<SAPMasterTemp> findAllByStatus(@Param("status") String status);
    
    @Query(
    	    "SELECT s.unitCode " +
    	    "FROM SAPMasterTemp s " +
    	    "WHERE s.unitCode IN :codes" )
	List<String> findExistingUnitCodes(@Param("codes") List<String> codes);
    
    @Query(
    	    "SELECT s.unitCode " +
    	    "FROM SAPMasterTemp s " +
    	    "WHERE s.unitCode IN :codes " +
    	    "AND s.status = :status" )
	List<String> findPendingIds(@Param("codes") List<String> codes, @Param("status") String status);
    
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("DELETE FROM SAPMasterTemp s WHERE s.unitCode IN :ids")
    int deleteApprovedRecords(
            @Param("ids") List<String> ids);
    
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(
        value =
            "UPDATE TM_CCR_SAP_MST_TEMP " +
            "SET STATUS = :status, " +
            "REMARKS = :remark, " +
            "ACTION_DATE = SYSDATE " +
            "WHERE UNIT_CODE IN (:ids) " +
            "AND STATUS = :pendingStatus",
        nativeQuery = true
    )
    int bulkUpdateStatus(
        @Param("ids") List<String> ids,
        @Param("status") String status,
        @Param("remark") String remark,
        @Param("pendingStatus") String pendingStatus
    );
    
//    @Transactional
//    @Modifying(clearAutomatically = true, flushAutomatically = true)
//    @Query(
//        value =
//            "UPDATE TM_CCR_SAP_MST_TEMP " +
//            "SET status = :status, " +
//            "remarks = :remark, " +
//            "action_date = SYSDATE " +
//            "WHERE sap_vendor_code IN (:ids) " +
//            "AND status = :pendingStatus",
//        nativeQuery = true )
//    int bulkUpdateStatus(
//        @Param("ids") List<String> ids,
//        @Param("status") String status,
//        @Param("remark") String remark,
//        @Param("pendingStatus") String pendingStatus
//    );

}