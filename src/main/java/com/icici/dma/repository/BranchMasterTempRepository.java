package com.icici.dma.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.BranchMasterTemp;
import com.icici.dma.model.GSTMasterTemp;

@Repository
public interface BranchMasterTempRepository extends JpaRepository<BranchMasterTemp, String> {

	boolean existsByBranchCodeAndStatus(String branchCode, String status);

	List<BranchMasterTemp> findByCreatedByAndStatus(String createdBy, String status);

	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_BRANCH_MST_TEMP t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.action_date, DATE '1900-01-01'), " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<BranchMasterTemp> findAllByStatus(@Param("status") String status);

	Optional<BranchMasterTemp> findByBranchCodeAndStatus(String branchCode, String pending);
	
	
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_BRANCH_MST_TEMP t " +
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
	List<BranchMasterTemp> findAllByStatusAndCreatedByNot(@Param("status") String status,@Param("actionUser") String actionUser);
	
	@Query("SELECT b.branchCode FROM BranchMasterTemp b WHERE b.branchCode IN :codes")
	List<String> findExistingBranchCodes(@Param("codes") List<String> codes);
	
	boolean existsByStatus(String status);
}
