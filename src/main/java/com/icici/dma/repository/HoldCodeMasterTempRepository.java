package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.HoldCodeMasterTemp;

@Repository
public interface HoldCodeMasterTempRepository
        extends JpaRepository<HoldCodeMasterTemp, String> {

//	List<HoldCodeMasterTemp> findAllByStatus(String status);

	Optional<HoldCodeMasterTemp> findByCodeAndStatus(String code, String status);

	boolean existsByCodeAndStatus(String code, String status);

	List<HoldCodeMasterTemp> findAllByStatusAndCreatedByNot(String status, String user);
	
	@Query(value =
		    "SELECT t.* " +
		    "FROM TM_CCR_HOLD_CODE_MST_TEMP t " +
		    "WHERE t.STATUS = :status " +
		    "AND t.ACTION_USER IS NOT NULL " +
		    "AND UPPER(TRIM(t.ACTION_USER)) <> UPPER(TRIM(:actionUser)) " +
		    "ORDER BY GREATEST( " +
		    "NVL(t.ACTION_DATE, DATE '1900-01-01'), " +
		    "NVL(t.MODIFIED_DATE, DATE '1900-01-01'), " +
		    "NVL(t.CREATED_DATE, DATE '1900-01-01') " +
		    ") DESC",
		    nativeQuery = true)
	List<HoldCodeMasterTemp> findAllByStatusAndActionUserNot(
		        @Param("status") String status,
		        @Param("actionUser") String actionUser);
	
	@Query(
		    value =
		        "SELECT t.* " +
		        "FROM TM_CCR_HOLD_CODE_MST_TEMP t " +
		        "WHERE t.status = :status " +
		        "ORDER BY GREATEST( " +
		        "NVL(t.action_date, DATE '1900-01-01'), " +
		        "NVL(t.modified_date, DATE '1900-01-01'), " +
		        "NVL(t.created_date, DATE '1900-01-01') " +
		        ") DESC",
		    nativeQuery = true
		)
		List<HoldCodeMasterTemp> findAllByStatus(
		        @Param("status") String status);

    @Query("SELECT h.code FROM HoldCodeMasterTemp h " +
           "WHERE h.code IN :codes AND h.status = :status")
	List<String> findPendingCodes(@Param("codes") List<String> codes, @Param("status") String status);

    @Transactional
    @Modifying
    @Query("UPDATE HoldCodeMasterTemp h " +
           "SET h.status = :status, " +
           "h.remarks = :remark, " +
           "h.actionDate = CURRENT_TIMESTAMP, " +
           "h.actionUser = :actionUser " +
           "WHERE h.code IN :codes " +
           "AND h.status = :pendingStatus")
    int bulkUpdateStatus(
            @Param("codes") List<String> codes,
            @Param("status") String status,
            @Param("remark") String remark,
            @Param("actionUser") String actionUser,
            @Param("pendingStatus") String pendingStatus);
    
    @Transactional
    @Modifying
    @Query("DELETE FROM HoldCodeMasterTemp h WHERE h.code IN :codes")
	int deleteApprovedRecords(@Param("codes") List<String> codes);
}