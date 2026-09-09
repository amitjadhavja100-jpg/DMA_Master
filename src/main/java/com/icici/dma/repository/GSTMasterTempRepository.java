package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.icici.dma.model.GSTMasterTemp;

@Repository
public interface GSTMasterTempRepository extends JpaRepository<GSTMasterTemp, Integer> {

	boolean existsByApsCodeAndStatus(Integer apsCode, String status);

	List<GSTMasterTemp> findByCreatedByAndStatus(String createdBy, String status);

	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_GST_MST_TEMP t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.action_date, DATE '1900-01-01'), " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<GSTMasterTemp> findAllByStatus(@Param("status") String status);

	Optional<GSTMasterTemp> findByApsCodeAndStatus(Integer apsCode, String pending);

	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_GST_MST_TEMP t " +
	        "WHERE t.STATUS = :status " +
	    	"AND t.ACTION_TYPE IN ('I', 'U') " +
	        "AND t.ACTION_USER IS NOT NULL " +
	    	"AND UPPER(TRIM(t.ACTION_USER)) != UPPER(TRIM(:actionUser)) " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.ACTION_DATE, DATE '1900-01-01'), " +
	        "NVL(t.MODIFIED_DATE, DATE '1900-01-01'), " +
	        "NVL(t.CREATED_DATE, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<GSTMasterTemp> findAllByStatusAndCreatedByNot(@Param("status") String status, @Param("actionUser") String actionUser);

	@Query("SELECT b.apsCode FROM GSTMasterTemp b WHERE b.apsCode IN :codes")
	List<Integer> findExistingApsCode(@Param("codes") List<Integer> codes);

	@Query("SELECT g.apsCode FROM GSTMasterTemp g " + "WHERE g.apsCode IN (:ids) AND g.status = :status")
	List<Integer> findPendingIds(@Param("ids") List<Integer> ids, @Param("status") String status);

	
	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Transactional
	@Query("UPDATE GSTMasterTemp g " +
	       "SET g.status = :status, " +
	       "g.remarak = :remark " +
	       "WHERE g.apsCode IN (:ids) " +
	       "AND g.status = :pendingStatus")
	int bulkUpdateStatus(
	        @Param("ids") List<Integer> ids,
	        @Param("status") String status,
	        @Param("remark") String remark,
	        @Param("pendingStatus") String pendingStatus
	);

}
