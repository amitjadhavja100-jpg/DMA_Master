package com.icici.dma.repository;

import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.icici.dma.model.BranchMaster;

@Repository
public interface BranchMasterRepository extends JpaRepository<BranchMaster, String> {
	
//	@Query(value = "SELECT b FROM BranchMaster b WHERE b.status = :status")
	@Query(value = "SELECT t.* " +
	        "FROM TM_VHL_BRANCH_MST t " +
	        "WHERE t.status = :status " +
	        "ORDER BY GREATEST( " +
	        "NVL(t.modified_date, DATE '1900-01-01'), " +
	        "NVL(t.created_date, DATE '1900-01-01') " +
	        ") DESC",
	        nativeQuery = true)
	List<BranchMaster> findAllByStatus(@Param("status") String status);

	
	boolean existsByBranchCode(String branchCode);
	
	boolean existsBy();
}
